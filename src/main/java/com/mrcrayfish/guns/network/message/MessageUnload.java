package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.item.AmmoItem;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.object.Gun;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Author: MrCrayfish
 */
public class MessageUnload implements IMessage, IMessageHandler<MessageUnload, IMessage>
{
    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(MessageUnload message, MessageContext ctx)
    {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() ->
        {
            PlayerEntity player = ctx.getServerHandler().player;
            ItemStack stack = player.inventory.getCurrentItem();
            if(stack.getItem() instanceof GunItem)
            {
                NBTTagCompound tag = stack.getTagCompound();
                if(tag != null && tag.hasKey("AmmoCount", Constants.NBT.TAG_INT))
                {
                    int count = tag.getInteger("AmmoCount");
                    tag.setInteger("AmmoCount", 0);

                    GunItem gunItem = (GunItem) stack.getItem();
                    Gun gun = gunItem.getModifiedGun(stack);
                    ResourceLocation id = gun.projectile.item;

                    AmmoItem ammo = AmmoRegistry.getInstance().getAmmo(id);
                    if(ammo == null)
                        return;

                    int stacks = count / 64;
                    for(int i = 0; i < stacks; i++)
                    {
                        spawnAmmo(player, new ItemStack(ammo, 64));
                    }

                    int remaining = count % 64;
                    if(remaining > 0)
                    {
                        spawnAmmo(player, new ItemStack(ammo, count));
                    }
                }
            }
        });
        return null;
    }

    private void spawnAmmo(PlayerEntity player, ItemStack stack)
    {
        player.inventory.addItemStackToInventory(stack);
        if(stack.getCount() > 0)
        {
            player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, stack.copy()));
        }
    }
}
