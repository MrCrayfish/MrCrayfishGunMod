package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.util.ItemStackUtil;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ReloadTracker
{
    private static final Map<UUID, ReloadTracker> RELOAD_TRACKER_MAP = new HashMap<>();

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

    private boolean isSameWeapon(PlayerEntity player)
    {
        return !this.stack.isEmpty() && player.inventory.currentItem == this.slot && player.inventory.getCurrentItem() == this.stack;
    }

    private boolean isWeaponFull()
    {
        CompoundNBT tag = ItemStackUtil.createTagCompound(this.stack);
        return tag.getInt("AmmoCount") >= this.gun.general.maxAmmo;
    }

    private boolean hasAmmo(PlayerEntity player)
    {
        return Gun.findAmmo(player, this.gun.projectile.item) != null;
    }

    private boolean canReload(PlayerEntity player)
    {
        int deltaTicks = player.ticksExisted - this.startTick;
        return deltaTicks > 0 && deltaTicks % 10 == 0;
    }

    private void increaseAmmo(PlayerEntity player)
    {
        ItemStack ammo = Gun.findAmmo(player, this.gun.projectile.item);
        if(!ammo.isEmpty())
        {
            int amount = Math.min(ammo.getCount(), this.gun.general.reloadSpeed);
            CompoundNBT tag = this.stack.getTag();
            if(tag != null)
            {
                amount = Math.min(amount, this.gun.general.maxAmmo - tag.getInt("AmmoCount"));
                tag.putInt("AmmoCount", tag.getInt("AmmoCount") + amount);
            }
            ammo.shrink(amount);
        }

        String reloadSound = this.gun.sounds.getReload(this.gun);
        SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(reloadSound));
        if(event != null)
        {
            player.world.playSound(null, player.getPosX(), player.getPosY() + 1.0D, player.getPosZ(), event, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START && !event.player.world.isRemote)
        {
            PlayerEntity player = event.player;
            if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
            {
                if(!RELOAD_TRACKER_MAP.containsKey(player.getUniqueID()))
                {
                    if(!(player.inventory.getCurrentItem().getItem() instanceof GunItem))
                    {
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                        return;
                    }
                    RELOAD_TRACKER_MAP.put(player.getUniqueID(), new ReloadTracker(player));
                }
                ReloadTracker tracker = RELOAD_TRACKER_MAP.get(player.getUniqueID());
                if(!tracker.isSameWeapon(player) || tracker.isWeaponFull() || !tracker.hasAmmo(player))
                {
                    RELOAD_TRACKER_MAP.remove(player.getUniqueID());
                    SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                    return;
                }
                if(tracker.canReload(player))
                {
                    tracker.increaseAmmo(player);
                    if(tracker.isWeaponFull() || !tracker.hasAmmo(player))
                    {
                        RELOAD_TRACKER_MAP.remove(player.getUniqueID());
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                    }
                }
            }
        }
    }
}
