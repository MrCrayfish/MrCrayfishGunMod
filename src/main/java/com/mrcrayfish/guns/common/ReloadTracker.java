package com.mrcrayfish.guns.common;

import com.mrcrayfish.framework.api.network.LevelLocation;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.S2CMessageGunSound;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Author: MrCrayfish
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ReloadTracker
{
    private static final Map<Player, ReloadTracker> RELOAD_TRACKER_MAP = new WeakHashMap<>();

    private final int startTick;
    private final int slot;
    private final ItemStack stack;
    private final Gun gun;

    private ReloadTracker(Player player)
    {
        this.startTick = player.tickCount;
        this.slot = player.getInventory().selected;
        this.stack = player.getInventory().getSelected();
        this.gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
    }

    /**
     * Tests if the current item the player is holding is the same as the one being reloaded
     *
     * @param player the player to check
     * @return True if it's the same weapon and slot
     */
    private boolean isSameWeapon(Player player)
    {
        return !this.stack.isEmpty() && player.getInventory().selected == this.slot && player.getInventory().getSelected() == this.stack;
    }

    /**
     * @return
     */
    private boolean isWeaponFull()
    {
        CompoundTag tag = this.stack.getOrCreateTag();
        return tag.getInt("AmmoCount") >= GunEnchantmentHelper.getAmmoCapacity(this.stack, this.gun);
    }

    private boolean hasNoAmmo(Player player)
    {
        return Gun.findAmmo(player, this.gun.getProjectile().getItem()).stack().isEmpty();
    }

    private boolean canReload(Player player)
    {
        int deltaTicks = player.tickCount - this.startTick;
        int interval = GunEnchantmentHelper.getReloadInterval(this.stack);
        return deltaTicks > 0 && deltaTicks % interval == 0;
    }

    private void increaseAmmo(Player player)
    {
        AmmoContext context = Gun.findAmmo(player, this.gun.getProjectile().getItem());
        ItemStack ammo = context.stack();
        if(!ammo.isEmpty())
        {
            int amount = Math.min(ammo.getCount(), this.gun.getGeneral().getReloadAmount());
            CompoundTag tag = this.stack.getTag();
            if(tag != null)
            {
                int maxAmmo = GunEnchantmentHelper.getAmmoCapacity(this.stack, this.gun);
                amount = Math.min(amount, maxAmmo - tag.getInt("AmmoCount"));
                tag.putInt("AmmoCount", tag.getInt("AmmoCount") + amount);
            }
            ammo.shrink(amount);
            // Trigger the post action on ammo consumption, like Container#setChanged.
            context.onConsume().run();
        }

        ResourceLocation reloadSound = this.gun.getSounds().getReload();
        if(reloadSound != null)
        {
            double radius = Config.SERVER.reloadMaxDistance.get();
            double soundX = player.getX();
            double soundY = player.getY() + 1.0;
            double soundZ = player.getZ();
            S2CMessageGunSound message = new S2CMessageGunSound(reloadSound, SoundSource.PLAYERS, (float) soundX, (float) soundY, (float) soundZ, 1.0F, 1.0F, player.getId(), false, true);
            PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> LevelLocation.create(player.level(), soundX, soundY, soundZ, radius), message);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START && !event.player.level().isClientSide)
        {
            Player player = event.player;
            if(ModSyncedDataKeys.RELOADING.getValue(player))
            {
                if(!RELOAD_TRACKER_MAP.containsKey(player))
                {
                    if(!(player.getInventory().getSelected().getItem() instanceof GunItem))
                    {
                        ModSyncedDataKeys.RELOADING.setValue(player, false);
                        return;
                    }
                    RELOAD_TRACKER_MAP.put(player, new ReloadTracker(player));
                }
                ReloadTracker tracker = RELOAD_TRACKER_MAP.get(player);
                if(!tracker.isSameWeapon(player) || tracker.isWeaponFull() || tracker.hasNoAmmo(player))
                {
                    RELOAD_TRACKER_MAP.remove(player);
                    ModSyncedDataKeys.RELOADING.setValue(player, false);
                    return;
                }
                if(tracker.canReload(player))
                {
                    tracker.increaseAmmo(player);
                    if(tracker.isWeaponFull() || tracker.hasNoAmmo(player))
                    {
                        RELOAD_TRACKER_MAP.remove(player);
                        ModSyncedDataKeys.RELOADING.setValue(player, false);

                        final Player finalPlayer = player;
                        final Gun gun = tracker.gun;
                        DelayedTask.runAfter(4, () ->
                        {
                            ResourceLocation cockSound = gun.getSounds().getCock();
                            if(cockSound != null && finalPlayer.isAlive())
                            {
                                double soundX = finalPlayer.getX();
                                double soundY = finalPlayer.getY() + 1.0;
                                double soundZ = finalPlayer.getZ();
                                double radius = Config.SERVER.reloadMaxDistance.get();
                                S2CMessageGunSound messageSound = new S2CMessageGunSound(cockSound, SoundSource.PLAYERS, (float) soundX, (float) soundY, (float) soundZ, 1.0F, 1.0F, finalPlayer.getId(), false, true);
                                PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> LevelLocation.create(finalPlayer.level(), soundX, soundY, soundZ, radius), messageSound);
                            }
                        });
                    }
                }
            }
            else if(RELOAD_TRACKER_MAP.containsKey(player))
            {
                RELOAD_TRACKER_MAP.remove(player);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerEvent.PlayerLoggedOutEvent event)
    {
        MinecraftServer server = event.getEntity().getServer();
        if(server != null)
        {
            server.execute(() -> RELOAD_TRACKER_MAP.remove(event.getEntity()));
        }
    }
}
