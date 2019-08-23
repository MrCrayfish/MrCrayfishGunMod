package com.mrcrayfish.guns.event;

import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.WorldServer;
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

    /**
     * A custom implementation of the cooldown tracker in order to provide the best experience for
     * players. On servers, Minecraft's cooldown tracker is sent to the client but the latency creates
     * an awkward experience as the cooldown applies to the item after the packet has traveled to the
     * server then back to the client. To fix this and still apply security, we just handle the
     * cooldown tracker quietly and not send cooldown packet back to client. The cooldown is still
     * applied on the client in {@link ItemGun#onItemRightClick} and {@link ItemGun#onUsingTick}.
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
    public void onPlayerInit(EntityEvent.EntityConstructing event)
    {
        if(event.getEntity() instanceof EntityPlayer)
        {
            event.getEntity().getDataManager().register(AIMING, false);
            event.getEntity().getDataManager().register(RELOADING, false);
        }
    }

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
                    if(!(player.inventory.getCurrentItem().getItem() instanceof ItemGun))
                    {
                        player.getDataManager().set(RELOADING, false);
                        return;
                    }
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
                    tracker.increaseAmmo(player);
                    if(tracker.isWeaponFull() || !tracker.hasAmmo(player))
                    {
                        reloadTrackerMap.remove(player.getUniqueID());
                        player.getDataManager().set(RELOADING, false);
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

        private ReloadTracker(EntityPlayer player)
        {
            this.startTick = player.ticksExisted;
            this.slot = player.inventory.currentItem;
            this.stack = player.inventory.getCurrentItem();
            this.gun = ((ItemGun) stack.getItem()).getModifiedGun(stack);
        }

        public boolean isSameWeapon(EntityPlayer player)
        {
            return !stack.isEmpty() && player.inventory.currentItem == slot && player.inventory.getCurrentItem() == stack;
        }

        public boolean isWeaponFull()
        {
            if(!stack.hasTagCompound())
            {
                stack.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound tag = stack.getTagCompound();
            return tag.getInteger("AmmoCount") >= gun.general.maxAmmo;
        }

        public boolean hasAmmo(EntityPlayer player)
        {
            return ItemGun.findAmmo(player, gun.projectile.item) != null;
        }

        public boolean canReload(EntityPlayer player)
        {
            int deltaTicks = player.ticksExisted - startTick;
            return deltaTicks > 0 && deltaTicks % 10 == 0;
        }

        public void increaseAmmo(EntityPlayer player)
        {
            ItemStack ammo = ItemGun.findAmmo(player, gun.projectile.item);
            if(!ammo.isEmpty())
            {
                int amount = Math.min(ammo.getCount(), gun.general.reloadSpeed);
                NBTTagCompound tag = stack.getTagCompound();
                if(tag != null)
                {
                    amount = Math.min(amount, gun.general.maxAmmo - tag.getInteger("AmmoCount"));
                    tag.setInteger("AmmoCount", tag.getInteger("AmmoCount") + amount);
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
