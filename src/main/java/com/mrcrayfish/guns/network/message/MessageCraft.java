package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.common.container.ContainerWorkbench;
import com.mrcrayfish.guns.init.ModCrafting;
import com.mrcrayfish.guns.item.ItemColored;
import com.mrcrayfish.guns.tileentity.TileEntityWorkbench;
import com.mrcrayfish.guns.util.InventoryUtil;
import com.mrcrayfish.guns.util.ItemStackHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.oredict.DyeUtils;

import java.util.List;
import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public class MessageCraft implements IMessage, IMessageHandler<MessageCraft, IMessage>
{
    private ItemStack stack;
    private BlockPos pos;

    public MessageCraft()
    {
    }

    public MessageCraft(ItemStack stack, BlockPos pos)
    {
        this.stack = stack;
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        ItemStackHelper.writeItemStackToBufIgnoreTag(buf, stack);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
        stack = ItemStackHelper.readItemStackFromBufIgnoreTag(buf);
    }

    @Override
    public IMessage onMessage(MessageCraft message, MessageContext ctx)
    {
        if(message.stack.isEmpty() || ModCrafting.getMaterialsForStack(message.stack) == null)
        {
            ctx.getServerHandler().player.connection.disconnect(new TextComponentString("Attempted to craft a weapon that didn't exist on the server"));
            return null;
        }

        EntityPlayer player = ctx.getServerHandler().player;
        World world = player.world;
        if(player.openContainer instanceof ContainerWorkbench)
        {
            ContainerWorkbench workbench = (ContainerWorkbench) player.openContainer;
            if(workbench.getPos().equals(message.pos))
            {
                List<ItemStack> materials = ModCrafting.getMaterialsForStack(message.stack);
                if(materials != null)
                {
                    for(ItemStack stack : materials)
                    {
                        if(!InventoryUtil.hasItemStack(player, stack))
                        {
                            return null;
                        }
                    }

                    for(ItemStack stack : materials)
                    {
                        InventoryUtil.removeItemStack(player, stack);
                    }

                    TileEntityWorkbench tileEntityWorkbench = workbench.getWorkbench();

                    /* Gets the color based on the dye */
                    int color = -1;
                    ItemStack dyeStack = tileEntityWorkbench.getInventory().get(0);
                    if(dyeStack.getItem() instanceof ItemDye)
                    {
                        Optional<EnumDyeColor> optional = DyeUtils.colorFromStack(dyeStack);
                        if(optional.isPresent())
                        {
                            float[] colorComponentValues = optional.get().getColorComponentValues();
                            int red = (int) (colorComponentValues[0] * 255F);
                            int green = (int) (colorComponentValues[1] * 255F);
                            int blue = (int) (colorComponentValues[2] * 255F);
                            color = ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF));
                            tileEntityWorkbench.getInventory().set(0, ItemStack.EMPTY);
                        }
                    }

                    final int finalColor = color;
                    FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() ->
                    {
                        ItemStack stack = message.stack.copy();
                        if(finalColor != -1 && stack.getItem() instanceof ItemColored)
                        {
                            ItemColored colored = (ItemColored) stack.getItem();
                            colored.setColor(stack, finalColor);
                        }
                        world.spawnEntity(new EntityItem(world, message.pos.getX() + 0.5, message.pos.getY() + 1.125, message.pos.getZ() + 0.5, stack));
                    });
                }
            }
        }
        return null;
    }
}
