package com.mrcrayfish.guns.event;

import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.obfuscate.common.event.EntityLivingInitEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.EntityEvent;
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
    public static final DataParameter<Boolean> AIMING = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> RELOADING = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BOOLEAN);

    @SubscribeEvent
    public void onPlayerInit(EntityEvent.EntityConstructing event)
    {
        if(event.getEntity() instanceof EntityPlayer)
        {
            event.getEntity().getDataManager().register(AIMING, false);
            event.getEntity().getDataManager().register(RELOADING, false);
        }
    }

    private Map<UUID, ReloadTracker> reloadTrackerMap = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START && !event.player.world.isRemote)
        {
            EntityPlayer player = event.player;
            if(player.getDataManager().get(RELOADING))
            {
                if(!reloadTrackerMap.containsKey(player.getUniqueID()))
                {
                    reloadTrackerMap.put(player.getUniqueID(), new ReloadTracker(player));
                }
                ReloadTracker tracker = reloadTrackerMap.get(player.getUniqueID());
                if(!tracker.isSameWeapon(player) || tracker.isWeaponFull() || !tracker.hasAmmo(player))
                {
                    reloadTrackerMap.remove(player.getUniqueID());
                    player.getDataManager().set(RELOADING, false);
                    return;
                }
                if(tracker.canReload(player))
                {
                    tracker.increaseAmmo(player, 1);
                    if(tracker.isWeaponFull() || !tracker.hasAmmo(player))
                    {
                        reloadTrackerMap.remove(player.getUniqueID());
                        player.getDataManager().set(RELOADING, false);
                    }
                }
            }
        }
    }

    private static class ReloadTracker
    {
        private int startTick;
        private int slot;
        private ItemStack stack;

        private ReloadTracker(EntityPlayer player)
        {
            this.startTick = player.ticksExisted;
            this.slot = player.inventory.currentItem;
            this.stack = player.inventory.getCurrentItem();
        }

        public boolean isSameWeapon(EntityPlayer player)
        {
            return player.inventory.currentItem == slot && player.inventory.getCurrentItem() == stack;
        }

        public boolean isWeaponFull()
        {
            NBTTagCompound tag = stack.getTagCompound();
            if(tag == null)
                return true;

            Gun gun = ((ItemGun) stack.getItem()).getGun();
            return tag.getInteger("AmmoCount") == gun.general.maxAmmo;
        }

        public boolean hasAmmo(EntityPlayer player)
        {
            if(player.capabilities.isCreativeMode)
                return true;
            Gun gun = ((ItemGun) stack.getItem()).getGun();
            return ItemGun.findAmmo(player, gun.projectile.type) != null;
        }

        public boolean canReload(EntityPlayer player)
        {
            int deltaTicks = player.ticksExisted - startTick;
            return deltaTicks > 0 && deltaTicks % 10 == 0;
        }

        public void increaseAmmo(EntityPlayer player, int amount)
        {
            Gun gun = ((ItemGun) stack.getItem()).getGun();
            ItemStack ammo = ItemGun.findAmmo(player, gun.projectile.type);
            if(ammo != null)
            {
                amount = Math.min(ammo.getCount(), amount);
                NBTTagCompound tag = stack.getTagCompound();
                if(tag != null)
                {
                    tag.setInteger("AmmoCount", tag.getInteger("AmmoCount") + amount);
                }
                ammo.shrink(amount);
            }

            SoundEvent event = ModSounds.getSound(gun.sounds.reload);
            if(event != null)
            {
                player.world.playSound(null, player.posX, player.posY + 1.0D, player.posZ, event, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
    }
}
