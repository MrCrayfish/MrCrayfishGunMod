package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.util.ItemStackUtil;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class CommonEvents
{
    /**
     * A custom implementation of the cooldown tracker in order to provide the best experience for
     * players. On servers, Minecraft's cooldown tracker is sent to the client but the latency creates
     * an awkward experience as the cooldown applies to the item after the packet has traveled to the
     * server then back to the client. To fix this and still apply security, we just handle the
     * cooldown tracker quietly and not send cooldown packet back to client. The cooldown is still
     * applied on the client in {@link GunItem#onItemRightClick} and {@link GunItem#onUsingTick}.
     */
    private static final Map<UUID, CooldownTracker> COOLDOWN_TRACKER_MAP = new HashMap<>();

    public static CooldownTracker getCooldownTracker(UUID uuid)
    {
        if(!COOLDOWN_TRACKER_MAP.containsKey(uuid))
        {
            COOLDOWN_TRACKER_MAP.put(uuid, new CooldownTracker());
        }
        return COOLDOWN_TRACKER_MAP.get(uuid);
    }

    private Map<UUID, ReloadTracker> reloadTrackerMap = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START && !event.player.world.isRemote)
        {
            PlayerEntity player = event.player;
            if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
            {
                if(!this.reloadTrackerMap.containsKey(player.getUniqueID()))
                {
                    if(!(player.inventory.getCurrentItem().getItem() instanceof GunItem))
                    {
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                        return;
                    }
                    this.reloadTrackerMap.put(player.getUniqueID(), new ReloadTracker(player));
                }
                ReloadTracker tracker = this.reloadTrackerMap.get(player.getUniqueID());
                if(!tracker.isSameWeapon(player) || tracker.isWeaponFull() || !tracker.hasAmmo(player))
                {
                    this.reloadTrackerMap.remove(player.getUniqueID());
                    SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                    return;
                }
                if(tracker.canReload(player))
                {
                    tracker.increaseAmmo(player);
                    if(tracker.isWeaponFull() || !tracker.hasAmmo(player))
                    {
                        this.reloadTrackerMap.remove(player.getUniqueID());
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                    }
                }
            }
            getCooldownTracker(player.getUniqueID()).tick();
        }
    }

    private static class ReloadTracker
    {
        private int startTick;
        private int slot;
        private ItemStack stack;
        private Gun gun;

        private ReloadTracker(PlayerEntity player)
        {
            this.startTick = player.ticksExisted;
            this.slot = player.inventory.currentItem;
            this.stack = player.inventory.getCurrentItem();
            this.gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
        }

        public boolean isSameWeapon(PlayerEntity player)
        {
            return !this.stack.isEmpty() && player.inventory.currentItem == this.slot && player.inventory.getCurrentItem() == this.stack;
        }

        public boolean isWeaponFull()
        {
            CompoundNBT tag = ItemStackUtil.createTagCompound(this.stack);
            return tag.getInt("AmmoCount") >= gun.general.maxAmmo;
        }

        public boolean hasAmmo(PlayerEntity player)
        {
            return GunItem.findAmmo(player, gun.projectile.item) != null;
        }

        public boolean canReload(PlayerEntity player)
        {
            int deltaTicks = player.ticksExisted - startTick;
            return deltaTicks > 0 && deltaTicks % 10 == 0;
        }

        public void increaseAmmo(PlayerEntity player)
        {
            ItemStack ammo = GunItem.findAmmo(player, gun.projectile.item);
            if(!ammo.isEmpty())
            {
                int amount = Math.min(ammo.getCount(), gun.general.reloadSpeed);
                CompoundNBT tag = stack.getTag();
                if(tag != null)
                {
                    amount = Math.min(amount, gun.general.maxAmmo - tag.getInt("AmmoCount"));
                    tag.putInt("AmmoCount", tag.getInt("AmmoCount") + amount);
                }
                ammo.shrink(amount);
            }

            String reloadSound = gun.sounds.getReload(gun);
            SoundEvent event = ModSounds.getSound(reloadSound);
            if(event == null)
            {
                event = SoundEvent.REGISTRY.getObject(new ResourceLocation(reloadSound));
            }
            if(event != null)
            {
                player.world.playSound(null, player.posX, player.posY + 1.0D, player.posZ, event, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
    }
}
