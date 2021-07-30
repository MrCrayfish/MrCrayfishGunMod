package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageGunSound;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Author: MrCrayfish
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ReloadTracker
{
    private static final Map<PlayerEntity, ReloadTracker> RELOAD_TRACKER_MAP = new WeakHashMap<>();

    private int startTick;
    private int slot;
    private ItemStack stack;
    private Gun gun;

    private ReloadTracker(PlayerEntity player)
    {
        this.startTick = player.tickCount;
        this.slot = player.inventory.selected;
        this.stack = player.inventory.getSelected();
        this.gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
    }

    /**
     * Tests if the current item the player is holding is the same as the one being reloaded
     *
     * @param player the player to check
     * @return True if it's the same weapon and slot
     */
    private boolean isSameWeapon(PlayerEntity player)
    {
        return !this.stack.isEmpty() && player.inventory.selected == this.slot && player.inventory.getSelected() == this.stack;
    }

    /**
     * @return
     */
    private boolean isWeaponFull()
    {
        CompoundNBT tag = this.stack.getOrCreateTag();
        return tag.getInt("AmmoCount") >= GunEnchantmentHelper.getAmmoCapacity(this.stack, this.gun);
    }

    private boolean hasNoAmmo(PlayerEntity player)
    {
        return Gun.findAmmo(player, this.gun.getProjectile().getItem()).isEmpty();
    }

    private boolean canReload(PlayerEntity player)
    {
        int deltaTicks = player.tickCount - this.startTick;
        int interval = GunEnchantmentHelper.getReloadInterval(this.stack);
        return deltaTicks > 0 && deltaTicks % interval == 0;
    }

    private void increaseAmmo(PlayerEntity player)
    {
        ItemStack ammo = Gun.findAmmo(player, this.gun.getProjectile().getItem());
        if(!ammo.isEmpty())
        {
            int amount = Math.min(ammo.getCount(), this.gun.getGeneral().getReloadAmount());
            CompoundNBT tag = this.stack.getTag();
            if(tag != null)
            {
                int maxAmmo = GunEnchantmentHelper.getAmmoCapacity(this.stack, this.gun);
                amount = Math.min(amount, maxAmmo - tag.getInt("AmmoCount"));
                tag.putInt("AmmoCount", tag.getInt("AmmoCount") + amount);
            }
            ammo.shrink(amount);
        }

        ResourceLocation reloadSound = this.gun.getSounds().getReload();
        if(reloadSound != null)
        {
            PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new MessageGunSound(reloadSound, SoundCategory.PLAYERS, (float) player.getX(), (float) player.getY() + 1.0F, (float) player.getZ(), 1.0F, 1.0F, player.getId(), false));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START && !event.player.level.isClientSide)
        {
            PlayerEntity player = event.player;
            if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
            {
                if(!RELOAD_TRACKER_MAP.containsKey(player))
                {
                    if(!(player.inventory.getSelected().getItem() instanceof GunItem))
                    {
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                        return;
                    }
                    RELOAD_TRACKER_MAP.put(player, new ReloadTracker(player));
                }
                ReloadTracker tracker = RELOAD_TRACKER_MAP.get(player);
                if(!tracker.isSameWeapon(player) || tracker.isWeaponFull() || tracker.hasNoAmmo(player))
                {
                    RELOAD_TRACKER_MAP.remove(player);
                    SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                    return;
                }
                if(tracker.canReload(player))
                {
                    tracker.increaseAmmo(player);
                    if(tracker.isWeaponFull() || tracker.hasNoAmmo(player))
                    {
                        RELOAD_TRACKER_MAP.remove(player);
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);

                        final PlayerEntity finalPlayer = player;
                        final Gun gun = tracker.gun;
                        DelayedTask.runAfter(4, () ->
                        {
                            ResourceLocation cockSound = gun.getSounds().getCock();
                            if(cockSound != null && finalPlayer.isAlive())
                            {
                                MessageGunSound messageSound = new MessageGunSound(cockSound, SoundCategory.PLAYERS, (float) finalPlayer.getX(), (float) (finalPlayer.getY() + 1.0), (float) finalPlayer.getZ(), 1.0F, 1.0F, finalPlayer.getId(), false);
                                PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) finalPlayer), messageSound);
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
        MinecraftServer server = event.getPlayer().getServer();
        if(server != null)
        {
            server.execute(() -> RELOAD_TRACKER_MAP.remove(event.getPlayer()));
        }
    }
}
