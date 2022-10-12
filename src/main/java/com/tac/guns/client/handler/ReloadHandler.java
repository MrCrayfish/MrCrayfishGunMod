package com.tac.guns.client.handler;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.client.InputHandler;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PumpShotgunAnimationController;
import com.tac.guns.common.Gun;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.event.GunReloadEvent;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageReload;
import com.tac.guns.network.message.MessageUnload;
import com.tac.guns.network.message.MessageUpdateGunID;
import com.tac.guns.util.GunEnchantmentHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;


/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
/*public class ReloadHandler
{
    private static ReloadHandler instance;

    public static ReloadHandler get()
    {
        if(instance == null)
        {
            instance = new ReloadHandler();
        }
        return instance;
    }

    private int startReloadTick;
    private int reloadTimer;
    private int prevReloadTimer;
    private int reloadingSlot;

    private ReloadHandler()
    {
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        this.prevReloadTimer = this.reloadTimer;

        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
            if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
            {
                if(this.reloadingSlot != player.inventory.currentItem)
                {
                    this.setReloading(false);
                }
            }

            this.updateReloadTimer(player);
        }
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event)
    {
        if(Minecraft.getInstance().player == null)
        {
            return;
        }

        if(KeyBinds.KEY_RELOAD.isKeyDown() && event.getAction() == GLFW.GLFW_PRESS)
        {
            if(!SyncedPlayerData.instance().get(Minecraft.getInstance().player, ModSyncedDataKeys.RELOADING))
            {
                this.setReloading(true);
            }
            else
            {
                this.setReloading(false);
            }
        }
        if(KeyBinds.KEY_UNLOAD.isPressed() && event.getAction() == GLFW.GLFW_PRESS)
        {
            this.setReloading(false);
            PacketHandler.getPlayChannel().sendToServer(new MessageUnload());
        }
    }

    public void setReloading(boolean reloading)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
            if(reloading)
            {
                ItemStack stack = player.getHeldItemMainhand();
                if(stack.getItem() instanceof GunItem)
                {
                    CompoundNBT tag = stack.getTag();
                    if(tag != null && !tag.contains("IgnoreAmmo", Constants.NBT.TAG_BYTE))
                    {
                        Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
                        if(tag.getInt("AmmoCount") >= GunEnchantmentHelper.getAmmoCapacity(stack, gun))
                        {
                            return;
                        }
                        if(Gun.findAmmo(player, gun.getProjectile().getItem()).isEmpty())
                        {
                            return;
                        }
                        if(MinecraftForge.EVENT_BUS.post(new GunReloadEvent.Pre(player, stack)))
                            return;
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, true);
                        PacketHandler.getPlayChannel().sendToServer(new MessageReload(true));
                        this.reloadingSlot = player.inventory.currentItem;
                        MinecraftForge.EVENT_BUS.post(new GunReloadEvent.Post(player, stack));
                    }
                }
            }
            else
            {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                PacketHandler.getPlayChannel().sendToServer(new MessageReload(false));
                this.reloadingSlot = -1;
            }
        }
    }

    private void updateReloadTimer(PlayerEntity player)
    {
        if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
        {
            if(this.startReloadTick == -1)
            {
                this.startReloadTick = player.ticks + 5;
            }
            if(this.reloadTimer < 5)
            {
                this.reloadTimer++;
            }
        }
        else
        {
            if(this.startReloadTick != -1)
            {
                this.startReloadTick = -1;
            }
            if(this.reloadTimer > 0)
            {
                this.reloadTimer--;
            }
        }
    }

    public int getStartReloadTick()
    {
        return this.startReloadTick;
    }

    public int getReloadTimer()
    {
        return this.reloadTimer;
    }

    public float getReloadProgress(float partialTicks)
    {
        return (this.prevReloadTimer + (this.reloadTimer - this.prevReloadTimer) * partialTicks) / 5F;
    }
}*/
public class ReloadHandler {
    private static ReloadHandler instance;

    public static ReloadHandler get() {
        if (instance == null) {
            instance = new ReloadHandler();
        }
        return instance;
    }

    private int startReloadTick;
    private int reloadTimer;
    private int prevReloadTimer;
    private int reloadingSlot;

    private int startUpReloadTimer;
    private boolean empty;
    private boolean prevState = false;
    private ItemStack prevItemStack;

    private ReloadHandler()
    {
    	InputHandler.RELOAD.addPressCallBack( () -> {
    		final ClientPlayerEntity player = Minecraft.getInstance().player;
			if( player == null ) return;
			
			final ItemStack stack = player.getHeldItemMainhand();
			if( stack.getItem() instanceof GunItem )
			{
				PacketHandler.getPlayChannel().sendToServer( new MessageUpdateGunID() );
				if( !SyncedPlayerData.instance().get( player, ModSyncedDataKeys.RELOADING ) )
					this.setReloading( true );
				else if(
					GunAnimationController.fromItem( stack.getItem() )
						instanceof PumpShotgunAnimationController
				) this.setReloading( false );
			}
		} );
    	
    	final Runnable callback = () -> {
    		if( !this.isReloading() )
			{
				final SimpleChannel channel = PacketHandler.getPlayChannel();
				channel.sendToServer( new MessageUpdateGunID() );
				this.setReloading( false );
				channel.sendToServer( new MessageUnload() );
			}
    	};
    	InputHandler.UNLOAD.addPressCallBack( callback );
    	InputHandler.CO_UNLOAD.addPressCallBack( callback );
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        this.prevReloadTimer = this.reloadTimer;

        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            if (SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING)) {
                if (this.reloadingSlot != player.inventory.currentItem) {
                    this.setReloading(false);
                }
            }
            this.updateReloadTimer(player);
            PacketHandler.getPlayChannel().sendToServer(new MessageUpdateGunID());
        }
    }

    private boolean isInGame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.loadingGui != null)
            return false;
        if (mc.currentScreen != null)
            return false;
        if (!mc.mouseHelper.isMouseGrabbed())
            return false;
        return mc.isGameFocused();
    }
    /*@SubscribeEvent
    public void onPlayerUpdate(TickEvent.PlayerTickEvent event)
    {
        if(!isInGame())
            return;
        PlayerEntity player = event.player;
        if(player == null)
            return;

    }*/

    public void setReloading(boolean reloading) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            if (reloading) {
                ItemStack stack = player.getHeldItemMainhand();
                prevItemStack = stack;
                if (stack.getItem() instanceof GunItem) {
                    CompoundNBT tag = stack.getTag();
                    if (tag != null && !tag.contains("IgnoreAmmo", Constants.NBT.TAG_BYTE)) {
                        Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
                        if (tag.getInt("AmmoCount") >= GunEnchantmentHelper.getAmmoCapacity(stack, gun)) {
                            return;
                        }
                        if (Gun.findAmmo(player, gun.getProjectile().getItem()).length < 1) {
                            return;
                        }
                        if (MinecraftForge.EVENT_BUS.post(new GunReloadEvent.Pre(player, stack)))
                            return;
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, true);
                        PacketHandler.getPlayChannel().sendToServer(new MessageReload(true));
                        AnimationHandler.INSTANCE.onGunReload(true, stack);
                        this.reloadingSlot = player.inventory.currentItem;
                        MinecraftForge.EVENT_BUS.post(new GunReloadEvent.Post(player, stack));
                    }
                }
            } else {
                if(prevItemStack != null) AnimationHandler.INSTANCE.onGunReload(false, prevItemStack);
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                PacketHandler.getPlayChannel().sendToServer(new MessageReload(false));
                this.reloadingSlot = -1;
            }
        }
    }

    private void updateReloadTimer(PlayerEntity player) {
        ItemStack stack = player.getHeldItemMainhand();
        if (SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING)) {
            prevState = true;
            if (stack.getItem() instanceof GunItem) {
                CompoundNBT tag = stack.getTag();
                if (tag != null) {
                    Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);

                    if (this.startUpReloadTimer == -1)
                        this.startUpReloadTimer = gun.getReloads().getPreReloadPauseTicks();

                    if (gun.getReloads().isMagFed()) {
                        if (this.startUpReloadTimer == 0) {
                            if (this.startReloadTick == -1) {
                                this.startReloadTick = player.ticksExisted + 5;
                            }
                            if (tag.getInt("AmmoCount") <= 0) {
                                if (this.reloadTimer < gun.getReloads().getReloadMagTimer() + gun.getReloads().getAdditionalReloadEmptyMagTimer()) {
                                    this.reloadTimer++;
                                }
                            } else {
                                if (this.reloadTimer < gun.getReloads().getReloadMagTimer()) {
                                    this.reloadTimer++;
                                }
                            }
                        } else
                            this.startUpReloadTimer--;
                    } else {
                        if (this.startUpReloadTimer == 0) {
                            if (this.startReloadTick == -1) {
                                this.startReloadTick = player.ticksExisted + 5;
                            }
                            if (this.reloadTimer < gun.getReloads().getinterReloadPauseTicks()) {
                                if (!AnimationHandler.INSTANCE.isReloadingIntro(prevItemStack.getItem()))
                                    this.reloadTimer++;
                            }
                            if (this.reloadTimer == gun.getReloads().getinterReloadPauseTicks()) {
                                AnimationHandler.INSTANCE.onReloadLoop(prevItemStack.getItem());
                                this.reloadTimer = 0;
                            }
                        } else
                            this.startUpReloadTimer--;
                    }
                }
            }
        } else {
            if (prevState) {
                prevState = false;
                AnimationHandler.INSTANCE.onReloadEnd(prevItemStack.getItem());
            }
            if (stack.getItem() instanceof GunItem) {
                Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
                if (gun.getReloads().isMagFed()) {
                    if (this.startReloadTick != -1) {
                        this.startReloadTick = -1;
                    }
                    if (this.reloadTimer > 0) {
                        this.reloadTimer = 0;
                    }
                } else {
                    if (this.startReloadTick != -1) {
                        this.startReloadTick = -1;
                    }
                    if (this.reloadTimer > 0) {
                        this.reloadTimer--;
                    }
                }
            } else {
                if (this.startReloadTick != -1) {
                    this.startReloadTick = -1;
                }
                if (this.reloadTimer > 0) {
                    this.reloadTimer = 0;
                }
            }

        }
    }

    public int getStartReloadTick() {
        return this.startReloadTick;
    }

    public int getReloadTimer() {
        return this.reloadTimer;
    }

    public int getStartUpReloadTimer() {
        return this.startUpReloadTimer;
    }

    public boolean isReloading() {
        return this.startReloadTick != -1;
    }

    public float getReloadProgress(float partialTicks, ItemStack stack) {
        boolean isEmpty = false;
        GunItem gunItem = (GunItem) stack.getItem();
        CompoundNBT tag = stack.getTag();
        if (tag != null) {
            isEmpty = tag.getInt("AmmoCount") <= 0;
        }
        return this.startUpReloadTimer == 0 ?
                (
                        gunItem.getGun().getReloads().isMagFed() ?
                                (isEmpty ? ((this.prevReloadTimer + ((this.reloadTimer - this.prevReloadTimer) * partialTicks) + this.startUpReloadTimer) / ((float) gunItem.getGun().getReloads().getReloadMagTimer() + gunItem.getGun().getReloads().getAdditionalReloadEmptyMagTimer())) : ((this.prevReloadTimer + ((this.reloadTimer - this.prevReloadTimer) * partialTicks) + this.startUpReloadTimer) / (float) gunItem.getGun().getReloads().getReloadMagTimer()))
                                : ((this.prevReloadTimer + ((this.reloadTimer - this.prevReloadTimer) * partialTicks)) / ((float) gunItem.getGun().getReloads().getinterReloadPauseTicks()))
                )
                : 1F;
    }

    @SubscribeEvent
    public void onGunFire(GunFireEvent.Pre event) {
        if(Minecraft.getInstance().player == null) return;
        ItemStack stack = Minecraft.getInstance().player.getHeldItemMainhand();
        Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
        if(GunAnimationController.fromItem(stack.getItem()) instanceof PumpShotgunAnimationController && isReloading()) event.setCanceled(true);
        CompoundNBT tag = stack.getOrCreateTag();
        if (tag.getInt("AmmoCount") <= 0) {
            if (gun.getReloads().getReloadMagTimer() + gun.getReloads().getAdditionalReloadEmptyMagTimer() - reloadTimer > 5) {
                if(isReloading()) event.setCanceled(true);
            }
        } else {
            if (gun.getReloads().getReloadMagTimer() - reloadTimer >5) {
                if(isReloading()) event.setCanceled(true);
            }
        }
    }

    /*public float getReloadProgress(float partialTicks, ItemStack stack)
    {
        boolean isEmpty = false;
        GunItem gunItem = (GunItem)stack.getItem();
        CompoundNBT tag = stack.getTag();
        if(tag != null)
        {
            isEmpty=tag.getInt("AmmoCount")<=0;
        }
        return this.startUpReloadTimer == 0 ?
                (
                        gunItem.getGun().getReloads().isMagFed() ?
                                (isEmpty ? ((this.prevReloadTimer + ((this.reloadTimer - this.prevReloadTimer) * partialTicks) + this.startUpReloadTimer) / ((float) gunItem.getGun().getReloads().getReloadMagTimer() + gunItem.getGun().getReloads().getAdditionalReloadEmptyMagTimer())) : ((this.prevReloadTimer + ((this.reloadTimer - this.prevReloadTimer) * partialTicks) + this.startUpReloadTimer) / (float) gunItem.getGun().getReloads().getReloadMagTimer()))
                                : ((this.prevReloadTimer + ((this.reloadTimer - this.prevReloadTimer) * partialTicks) + this.startUpReloadTimer) / ((float) gunItem.getGun().getReloads().getinterReloadPauseTicks()) )
                )
                : 1F;
    }*/

    //public boolean isReloading()
    //{
    //    return this.startReloadTick != -1;
    //}
}