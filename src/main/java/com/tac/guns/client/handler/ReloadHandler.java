package com.tac.guns.client.handler;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.client.KeyBinds;
import com.tac.guns.common.Gun;
import com.tac.guns.common.ReloadTracker;
import com.tac.guns.event.GunReloadEvent;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageGunSound;
import com.tac.guns.network.message.MessageReload;
import com.tac.guns.network.message.MessageUnload;
import com.tac.guns.util.GunEnchantmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

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
public class ReloadHandler
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

    private int startUpReloadTimer;
    private boolean empty;

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
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if(player == null)
        {
            return;
        }

        ItemStack stack = player.getHeldItemMainhand();
        if(KeyBinds.KEY_RELOAD.isKeyDown() && event.getAction() == GLFW.GLFW_PRESS && stack.getItem() instanceof GunItem)
        {
            if(!SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
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
                        if(Gun.findAmmo(player, gun.getProjectile().getItem()).length < 1)
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
        ItemStack stack = player.getHeldItemMainhand();
        if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
        {
            if(stack.getItem() instanceof GunItem)
            {
                CompoundNBT tag = stack.getTag();
                if (tag != null)
                {
                    Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);

                    if (this.startUpReloadTimer == -1)
                        this.startUpReloadTimer = gun.getReloads().getPreReloadPauseTicks();

                    if (gun.getReloads().isMagFed())
                    {
                        if (this.startUpReloadTimer == 0)
                        {
                            if (this.startReloadTick == -1)
                            {
                                this.startReloadTick = player.ticksExisted + 5;
                            }
                            if (tag.getInt("AmmoCount") <= 0)
                            {
                                if (this.reloadTimer < gun.getReloads().getReloadMagTimer() + gun.getReloads().getAdditionalReloadEmptyMagTimer())
                                {
                                    this.reloadTimer++;
                                }
                            }
                            else
                            {
                                if (this.reloadTimer < gun.getReloads().getReloadMagTimer())
                                {
                                    this.reloadTimer++;
                                }
                            }
                        }
                        else
                            this.startUpReloadTimer--;
                    }
                    else {
                        if (this.startUpReloadTimer == 0) {
                            if (this.startReloadTick == -1) {
                                this.startReloadTick = player.ticksExisted + 5;
                            }
                            if (this.reloadTimer < gun.getReloads().getinterReloadPauseTicks()) {
                                this.reloadTimer++;
                            }
                            if (this.reloadTimer == gun.getReloads().getinterReloadPauseTicks()) {
                                this.reloadTimer = 0;
                            }
                        } else
                            this.startUpReloadTimer--;
                    }
                }
            }
        }
        else
        {
            if(stack.getItem() instanceof GunItem)
            {
                Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
                if(gun.getReloads().isMagFed())
                {
                    if(this.startReloadTick != -1)
                    {
                        this.startReloadTick = -1;
                    }
                    if(this.reloadTimer > 0)
                    {
                        this.reloadTimer = 0;
                    }
                }
                else
                {
                    if (this.startReloadTick != -1)
                    {
                        this.startReloadTick = -1;
                    }
                    if (this.reloadTimer > 0) {
                        this.reloadTimer--;
                    }
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
                    this.reloadTimer = 0;
                }
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

    public int getStartUpReloadTimer()
    {
        return this.startUpReloadTimer;
    }

    public boolean isReloading()
    {
        return this.startReloadTick != -1;
    }

    public float getReloadProgress(float partialTicks, ItemStack stack)
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
                                : ((this.prevReloadTimer + ((this.reloadTimer - this.prevReloadTimer) * partialTicks)) / ((float) gunItem.getGun().getReloads().getinterReloadPauseTicks()) )
                )
                : 1F;
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