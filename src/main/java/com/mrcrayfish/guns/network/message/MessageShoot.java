package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Author: MrCrayfish
 */
public class MessageShoot implements IMessage, IMessageHandler<MessageShoot, IMessage>
{
    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(MessageShoot message, MessageContext ctx)
    {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() ->
        {
            EntityPlayer player = ctx.getServerHandler().player;
            World world = player.world;
            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
            if(!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun && (ItemGun.hasAmmo(heldItem) || player.capabilities.isCreativeMode))
            {
                ItemGun item = (ItemGun) heldItem.getItem();
                Gun gun = item.getModifiedGun(heldItem);
                if(gun != null)
                {
                    CooldownTracker tracker = player.getCooldownTracker();
                    if(!tracker.hasCooldown(heldItem.getItem()))
                    {
                        tracker.setCooldown(heldItem.getItem(), gun.general.rate);
                        ItemGun.fire(world, player, heldItem);
                    }
                }
            }
            else
            {
                world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.8F);
            }
        });
        return null;
    }
}
