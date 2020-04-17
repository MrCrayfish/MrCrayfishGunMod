package com.mrcrayfish.guns.common;

import com.google.common.base.Predicate;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.common.container.ContainerWorkbench;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.*;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageBullet;
import com.mrcrayfish.guns.network.message.MessageMuzzleFlash;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.tileentity.WorkbenchTileEntity;
import com.mrcrayfish.guns.util.InventoryUtil;
import com.mrcrayfish.guns.util.ItemStackUtil;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public class CommonHandler
{
    private static final Predicate<LivingEntity> NOT_AGGRO_EXEMPT = entity -> !(entity instanceof PlayerEntity) && !Config.COMMON.aggroMobs.exemptEntities.get().contains(entity.getType().getRegistryName().toString());

    /**
     * Fires the weapon the player is currently holding.
     * This is only intended for use on the logical server.
     *
     * @param player the player for who's weapon to fire
     */
    public static void fireHeldGun(ServerPlayerEntity player)
    {
        if(!player.isSpectator())
        {
            World world = player.world;
            ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
            if(heldItem.getItem() instanceof GunItem && (GunItem.hasAmmo(heldItem) || player.isCreative()))
            {
                GunItem item = (GunItem) heldItem.getItem();
                Gun gun = item.getModifiedGun(heldItem);
                if(gun != null)
                {
                    CooldownTracker tracker = player.getCooldownTracker();
                    if(tracker.hasCooldown(heldItem.getItem()))
                    {
                        GunMod.LOGGER.warn(player.getName() + "(" + player.getUniqueID() + ") tried to fire before cooldown finished or server is lagging?");
                        return;
                    }
                    tracker.setCooldown(heldItem.getItem(), gun.general.rate);

                    if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
                    {
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                    }

                    if(!gun.general.alwaysSpread && gun.general.spread > 0.0F)
                    {
                        SpreadHandler.getSpreadTracker(player.getUniqueID()).update(item);
                    }

                    boolean silenced = Gun.getAttachment(IAttachment.Type.BARREL, heldItem).getItem() == ModItems.SILENCER.get();

                    for(int i = 0; i < gun.general.projectileAmount; i++)
                    {
                        ProjectileFactory factory = AmmoRegistry.getInstance().getFactory(gun.projectile.item);
                        EntityProjectile bullet = factory.create(world, player, item, gun);
                        bullet.setWeapon(heldItem);
                        bullet.setAdditionalDamage(Gun.getAdditionalDamage(heldItem));
                        if(silenced)
                        {
                            bullet.setDamageModifier(0.75F);
                        }
                        world.addEntity(bullet);

                        if(!gun.projectile.visible)
                        {
                            MessageBullet messageBullet = new MessageBullet(bullet.getEntityId(), bullet.getPosX(), bullet.getPosY(), bullet.getPosZ(), bullet.getMotion().getX(), bullet.getMotion().getY(), bullet.getMotion().getZ(), gun.projectile.trailColor, gun.projectile.trailLengthMultiplier);
                            PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), Config.COMMON.network.projectileTrackingRange.get(), player.world.getDimension().getType())), messageBullet);
                        }
                    }

                    if(Config.COMMON.aggroMobs.enabled.get())
                    {
                        double radius = silenced ? Config.COMMON.aggroMobs.rangeSilenced.get() : Config.COMMON.aggroMobs.rangeUnsilenced.get();
                        double x = player.getPosX();
                        double y = player.getPosY() + 0.5;
                        double z = player.getPosZ();
                        AxisAlignedBB box = new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
                        radius *= radius;
                        double dx, dy, dz;
                        for(LivingEntity entity : world.getEntitiesWithinAABB(LivingEntity.class, box, NOT_AGGRO_EXEMPT))
                        {
                            dx = x - entity.getPosX();
                            dy = y - entity.getPosY();
                            dz = z - entity.getPosZ();
                            if(dx * dx + dy * dy + dz * dz <= radius)
                            {
                                entity.setRevengeTarget(Config.COMMON.aggroMobs.angerHostileMobs.get() ? player : entity);
                            }
                        }
                    }

                    String fireSound = silenced ? gun.sounds.silencedFire : gun.sounds.fire;
                    SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(fireSound));
                    if(event != null)
                    {
                        world.playSound(null, player.getPosition(), event, SoundCategory.HOSTILE, silenced ? 1.0F : 5.0F, 0.8F + world.rand.nextFloat() * 0.2F);
                    }

                    if(gun.display.flash != null)
                    {
                        PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() -> player), new MessageMuzzleFlash());
                    }

                    if(!player.isCreative())
                    {
                        CompoundNBT tag = ItemStackUtil.createTagCompound(heldItem);
                        if(!tag.getBoolean("IgnoreAmmo"))
                        {
                            tag.putInt("AmmoCount", Math.max(0, tag.getInt("AmmoCount") - 1));
                        }
                    }
                }
            }
            else
            {
                world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.8F);
            }
        }
    }

    /**
     * Crafts the specified item at the workstation the player is currently using.
     * This is only intended for use on the logical server.
     *
     * @param player the player who is crafting
     * @param id     the id of an item which is registered as a valid workstation recipe
     * @param pos    the block position of the workstation the player is using
     */
    public static void craftVehicle(ServerPlayerEntity player, ResourceLocation id, BlockPos pos)
    {
        World world = player.world;
        if(player.openContainer instanceof ContainerWorkbench)
        {
            ContainerWorkbench workbench = (ContainerWorkbench) player.openContainer;
            if(workbench.getPos().equals(pos))
            {
                List<ItemStack> materials = WorkbenchRegistry.getMaterialsForStack(message.stack);
                if(materials != null)
                {
                    for(ItemStack stack : materials)
                    {
                        if(!InventoryUtil.hasItemStack(player, stack))
                        {
                            return null;
                        }
                    }

                    for(ItemStack stack : materials)
                    {
                        InventoryUtil.removeItemStack(player, stack);
                    }

                    WorkbenchTileEntity workbenchTileEntity = workbench.getWorkbench();

                    /* Gets the color based on the dye */
                    int color = -1;
                    ItemStack dyeStack = workbenchTileEntity.getInventory().get(0);
                    if(dyeStack.getItem() instanceof ItemDye)
                    {
                        Optional<EnumDyeColor> optional = DyeUtils.colorFromStack(dyeStack);
                        if(optional.isPresent())
                        {
                            float[] colorComponentValues = optional.get().getColorComponentValues();
                            int red = (int) (colorComponentValues[0] * 255F);
                            int green = (int) (colorComponentValues[1] * 255F);
                            int blue = (int) (colorComponentValues[2] * 255F);
                            color = ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF));
                            workbenchTileEntity.getInventory().set(0, ItemStack.EMPTY);
                        }
                    }

                    ItemStack stack = message.stack.copy();
                    if(finalColor != -1 && stack.getItem() instanceof ColoredItem)
                    {
                        ColoredItem colored = (ColoredItem) stack.getItem();
                        colored.setColor(stack, finalColor);
                    }
                    world.spawnEntity(new EntityItem(world, message.pos.getX() + 0.5, message.pos.getY() + 1.125, message.pos.getZ() + 0.5, stack));
                }
            }
        }
    }

    public static void unloadHeldGun(ServerPlayerEntity player)
    {
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() instanceof GunItem)
        {
            CompoundNBT tag = stack.getTag();
            if(tag != null && tag.contains("AmmoCount", Constants.NBT.TAG_INT))
            {
                int count = tag.getInt("AmmoCount");
                tag.putInt("AmmoCount", 0);

                GunItem gunItem = (GunItem) stack.getItem();
                Gun gun = gunItem.getModifiedGun(stack);
                ResourceLocation id = gun.projectile.item;

                AmmoItem ammo = AmmoRegistry.getInstance().getAmmo(id);
                if(ammo == null)
                {
                    return;
                }

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
    }

    private static void spawnAmmo(ServerPlayerEntity player, ItemStack stack)
    {
        player.inventory.addItemStackToInventory(stack);
        if(stack.getCount() > 0)
        {
            player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), stack.copy()));
        }
    }
}
