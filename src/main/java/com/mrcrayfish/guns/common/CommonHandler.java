package com.mrcrayfish.guns.common;

import com.google.common.base.Predicate;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.common.container.AttachmentContainer;
import com.mrcrayfish.guns.common.container.WorkbenchContainer;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.crafting.WorkbenchRecipes;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.AmmoItem;
import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.item.ColoredItem;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageBullet;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.tileentity.WorkbenchTileEntity;
import com.mrcrayfish.guns.util.InventoryUtil;
import com.mrcrayfish.guns.util.ItemStackUtil;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class CommonHandler
{
    private static final Predicate<LivingEntity> NOT_AGGRO_EXEMPT = entity -> !(entity instanceof PlayerEntity) && !Config.COMMON.aggroMobs.exemptEntities.get().contains(entity.getType().getRegistryName().toString());

    /**
     * A custom implementation of the cooldown tracker in order to provide the best experience for
     * players. On servers, Minecraft's cooldown tracker is sent to the client but the latency creates
     * an awkward experience as the cooldown applies to the item after the packet has traveled to the
     * server then back to the client. To fix this and still apply security, we just handle the
     * cooldown tracker quietly and not send cooldown packet back to client. The cooldown is still
     * applied on the client in {@link GunItem#onItemRightClick} and {@link GunItem#onUsingTick}.
     */
    private static final Map<UUID, ShootTracker> SHOOT_TRACKER_MAP = new HashMap<>();

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
            if(heldItem.getItem() instanceof GunItem && (Gun.hasAmmo(heldItem) || player.isCreative()))
            {
                GunItem item = (GunItem) heldItem.getItem();
                Gun modifiedGun = item.getModifiedGun(heldItem);
                if(modifiedGun != null)
                {
                    ShootTracker tracker = getShootTracker(player.getUniqueID());
                    if(tracker.hasCooldown(item))
                    {
                        GunMod.LOGGER.warn(player.getName().getUnformattedComponentText() + "(" + player.getUniqueID() + ") tried to fire before cooldown finished or server is lagging? Remaining milliseconds: " + tracker.getRemaining(item));
                        return;
                    }
                    tracker.putCooldown(item, modifiedGun);

                    if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
                    {
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                    }

                    if(!modifiedGun.general.alwaysSpread && modifiedGun.general.spread > 0.0F)
                    {
                        SpreadTracker.get(player.getUniqueID()).update(item);
                    }

                    boolean silenced = Gun.getAttachment(IAttachment.Type.BARREL, heldItem).getItem() == ModItems.SILENCER.get();

                    for(int i = 0; i < modifiedGun.general.projectileAmount; i++)
                    {
                        ProjectileFactory factory = AmmoRegistry.getInstance().getFactory(modifiedGun.projectile.item);
                        EntityProjectile bullet = factory.create(world, player, item, modifiedGun);
                        bullet.setWeapon(heldItem);
                        bullet.setAdditionalDamage(Gun.getAdditionalDamage(heldItem));
                        if(silenced)
                        {
                            bullet.setDamageModifier(0.75F);
                        }
                        world.addEntity(bullet);

                        if(!modifiedGun.projectile.visible)
                        {
                            MessageBullet messageBullet = new MessageBullet(bullet.getEntityId(), bullet.getPosX(), bullet.getPosY(), bullet.getPosZ(), bullet.getMotion().getX(), bullet.getMotion().getY(), bullet.getMotion().getZ(), modifiedGun.projectile.trailColor, modifiedGun.projectile.trailLengthMultiplier);
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

                    ResourceLocation fireSound = silenced ? modifiedGun.sounds.silencedFire : modifiedGun.sounds.fire;
                    SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(fireSound);
                    if(event != null)
                    {
                        world.playSound(null, player.getPosition(), event, SoundCategory.HOSTILE, silenced ? 1.0F : 5.0F, 0.8F + world.rand.nextFloat() * 0.2F);
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

        if(player.openContainer instanceof WorkbenchContainer)
        {
            WorkbenchContainer workbench = (WorkbenchContainer) player.openContainer;
            if(workbench.getPos().equals(pos))
            {
                WorkbenchRecipe recipe = WorkbenchRecipes.getRecipeById(world, id);
                if(recipe == null)
                {
                    return;
                }

                List<ItemStack> materials = recipe.getMaterials();
                if(materials != null)
                {
                    for(ItemStack stack : materials)
                    {
                        if(!InventoryUtil.hasItemStack(player, stack))
                        {
                            return;
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
                    if(dyeStack.getItem() instanceof DyeItem)
                    {
                        DyeItem dyeItem = (DyeItem) dyeStack.getItem();
                        color = dyeItem.getDyeColor().getColorValue();
                        workbenchTileEntity.getInventory().set(0, ItemStack.EMPTY);
                    }

                    ItemStack stack = recipe.getItem();
                    if(stack.getItem() instanceof ColoredItem)
                    {
                        ColoredItem colored = (ColoredItem) stack.getItem();
                        colored.setColor(stack, color);
                    }
                    world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.125, pos.getZ() + 0.5, stack));
                }
            }
        }
    }

    /**
     *
     * @param player
     */
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

    /**
     *
     * @param player
     * @param stack
     */
    private static void spawnAmmo(ServerPlayerEntity player, ItemStack stack)
    {
        player.inventory.addItemStackToInventory(stack);
        if(stack.getCount() > 0)
        {
            player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), stack.copy()));
        }
    }

    /**
     *
     * @param player
     */
    public static void openAttachmentsScreen(ServerPlayerEntity player)
    {
        ItemStack heldItem = player.getHeldItemMainhand();
        if(heldItem.getItem() instanceof GunItem)
        {
            NetworkHooks.openGui(player, new SimpleNamedContainerProvider((windowId, playerInventory, player1) -> new AttachmentContainer(windowId, playerInventory, heldItem), new TranslationTextComponent("container.cgm.attachments")));
        }
    }

    /**
     * Gets the cooldown tracker for the specified player UUID.
     *
     * @param uuid the player's uuid
     * @return a cooldown tracker instance
     */
    public static ShootTracker getShootTracker(UUID uuid)
    {
        if(!SHOOT_TRACKER_MAP.containsKey(uuid))
        {
            SHOOT_TRACKER_MAP.put(uuid, new ShootTracker());
        }
        return SHOOT_TRACKER_MAP.get(uuid);
    }
}
