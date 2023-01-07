package com.tac.guns.common.network;

import static net.minecraft.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Config;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.client.handler.MovementAdaptationsHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.screen.UpgradeBenchScreen;
import com.tac.guns.common.Gun;
import com.tac.guns.common.NetworkGunManager;
import com.tac.guns.common.ProjectileManager;
import com.tac.guns.common.Rig;
import com.tac.guns.common.ShootTracker;
import com.tac.guns.common.SpreadTracker;
import com.tac.guns.common.container.AttachmentContainer;
import com.tac.guns.common.container.ColorBenchContainer;
import com.tac.guns.common.container.InspectionContainer;
import com.tac.guns.common.container.UpgradeBenchContainer;
import com.tac.guns.common.container.WorkbenchContainer;
import com.tac.guns.crafting.WorkbenchRecipe;
import com.tac.guns.crafting.WorkbenchRecipes;
import com.tac.guns.entity.ProjectileEntity;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.init.ModBlocks;
import com.tac.guns.init.ModEnchantments;
import com.tac.guns.init.ModItems;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.interfaces.IProjectileFactory;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.IColored;
import com.tac.guns.item.ScopeItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.TransitionalTypes.wearables.ArmorRigItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageBulletTrail;
import com.tac.guns.network.message.MessageGunSound;
import com.tac.guns.network.message.MessageRigInvToClient;
import com.tac.guns.network.message.MessageSaveItemUpgradeBench;
import com.tac.guns.network.message.MessageShoot;
import com.tac.guns.network.message.MessageUpgradeBenchApply;
import com.tac.guns.tileentity.FlashLightSource;
import com.tac.guns.tileentity.UpgradeBenchTileEntity;
import com.tac.guns.tileentity.WorkbenchTileEntity;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import com.tac.guns.util.InventoryUtil;
import com.tac.guns.util.WearableHelper;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;


/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ServerPlayHandler
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);
    private static final Predicate<LivingEntity> HOSTILE_ENTITIES = entity -> entity.getSoundCategory() == SoundCategory.HOSTILE && !Config.COMMON.aggroMobs.exemptEntities.get().contains(entity.getType().getRegistryName().toString());

    /**
     * Fires the weapon the player is currently holding.
     * This is only intended for use on the logical server.
     *
     * @param player the player for who's weapon to fire
     */
    public static void handleShoot(MessageShoot message, ServerPlayerEntity player)
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
                    if(MinecraftForge.EVENT_BUS.post(new GunFireEvent.Pre(player, heldItem)))
                        return;

                    /* Updates the yaw and pitch with the clients current yaw and pitch */
                    player.rotationYaw = message.getRotationYaw();
                    player.rotationPitch = message.getRotationPitch();

                    // CHECK HERE:
                    //     Old server side fire rate control. This has to be disabled to make the \
                    //     demo version of this new RPM system to work. This requires to be \
                    //     refactor if you want server side restriction to work with the new RPM \
                    //     system.
//                    ShootTracker tracker = ShootTracker.getShootTracker(player);
//                    if(tracker.hasCooldown(item))
//                    {
//                        GunMod.LOGGER.warn(player.getName().getUnformattedComponentText() + "(" + player.getUniqueID() + ") tried to fire before cooldown finished or server is lagging? Remaining milliseconds: " + tracker.getRemaining(item));
//                        ShootingHandler.get().setShootingError(true);
//                        return;
//                    }
//                    tracker.putCooldown(heldItem, item, modifiedGun);


                    //tracker.putCooldown(heldItem, item, modifiedGun);

                    if(!modifiedGun.getGeneral().isAlwaysSpread() && modifiedGun.getGeneral().getSpread() > 0.0F)
                    {
                        SpreadTracker.get(player).update(player, item);
                    }
                    //TODO: Change the function of the spread tracker so it trackers accuracy per specificlly held weapon, so first shot accuracy and
                    //  total bullets before hitting max accuracy is tracked per weapon.
                    //m4 og spread 2.925
                    int count = modifiedGun.getGeneral().getProjectileAmount();
                    Gun.Projectile projectileProps = modifiedGun.getProjectile();
                    ProjectileEntity[] spawnedProjectiles = new ProjectileEntity[count];
                    for(int i = 0; i < count; i++)
                    {
                        IProjectileFactory factory = ProjectileManager.getInstance().getFactory(projectileProps.getItem());
                        ProjectileEntity projectileEntity = factory.create(world, player, heldItem, item, modifiedGun);
                        projectileEntity.setWeapon(heldItem);
                        projectileEntity.setAdditionalDamage(Gun.getAdditionalDamage(heldItem));
                        world.addEntity(projectileEntity);
                        spawnedProjectiles[i] = projectileEntity;
                        projectileEntity.tick();
                    }
                    if(!projectileProps.isVisible())
                    {
                        MessageBulletTrail messageBulletTrail = new MessageBulletTrail(spawnedProjectiles, projectileProps, player.getEntityId());
                        PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), Config.COMMON.network.projectileTrackingRange.get(), player.world.getDimensionKey())), messageBulletTrail);
                    }

                    MinecraftForge.EVENT_BUS.post(new GunFireEvent.Post(player, heldItem));

                    if(Config.COMMON.aggroMobs.enabled.get())
                    {
                        double radius = GunModifierHelper.getModifiedFireSoundRadius(heldItem, Config.COMMON.aggroMobs.range.get());
                        double x = player.getPosX();
                        double y = player.getPosY() + 0.5;
                        double z = player.getPosZ();
                        AxisAlignedBB box = new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
                        radius *= radius;
                        double dx, dy, dz;
                        for(LivingEntity entity : world.getEntitiesWithinAABB(LivingEntity.class, box, HOSTILE_ENTITIES))
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

                    boolean silenced = GunModifierHelper.isSilencedFire(heldItem);
                    ResourceLocation fireSound = silenced ? modifiedGun.getSounds().getSilencedFire() : modifiedGun.getSounds().getFire();
                    if(fireSound != null)
                    {
                        double posX = player.getPosX();
                        double posY = player.getPosY() + player.getEyeHeight();
                        double posZ = player.getPosZ();
                        float volume = GunModifierHelper.getFireSoundVolume(heldItem);
                        
                        // PATCH NOTE: Neko required to remove the random pitch effect in sound
                        final float pitch = 1F; // 0.9F + world.rand.nextFloat() * 0.2F;
                        
                        double radius = GunModifierHelper.getModifiedFireSoundRadius(heldItem, Config.SERVER.gunShotMaxDistance.get());
                        boolean muzzle = modifiedGun.getDisplay().getFlash() != null;
                        MessageGunSound messageSound = new MessageGunSound(fireSound, SoundCategory.PLAYERS, (float) posX, (float) posY, (float) posZ, volume, pitch, player.getEntityId(), muzzle, false);
                        PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(posX, posY, posZ, radius, player.world.getDimensionKey());
                        PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> targetPoint), messageSound);
                    }

                    if(!player.isCreative())
                    {
                        CompoundNBT tag = heldItem.getOrCreateTag();
                        if(!tag.getBoolean("IgnoreAmmo"))
                        {
                            int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.RECLAIMED.get(), heldItem);
                            if(level == 0 || player.world.rand.nextInt(4 - MathHelper.clamp(level, 1, 2)) != 0)
                            {
                                tag.putInt("AmmoCount", Math.max(0, tag.getInt("AmmoCount") - 1));
                            }
                        }
                    }
                }
            }
            else
            {
                world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.8F);
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
    public static void handleCraft(ServerPlayerEntity player, ResourceLocation id, BlockPos pos)
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
                    ItemStack stack = recipe.getItem();
                    ItemStack dyeStack = workbenchTileEntity.getInventory().get(0);
                    if(dyeStack.getItem() instanceof DyeItem)
                    {
                        DyeItem dyeItem = (DyeItem) dyeStack.getItem();
                        int color = dyeItem.getDyeColor().getColorValue();

                        if(stack.getItem() instanceof IColored && ((IColored) stack.getItem()).canColor(stack))
                        {
                            IColored colored = (IColored) stack.getItem();
                            colored.setColor(stack, color);
                            workbenchTileEntity.getInventory().set(0, ItemStack.EMPTY);
                        }
                    }

                    if(stack.getItem() instanceof TimelessGunItem)
                    {
                        if(stack.getTag() == null)
                        {
                            stack.getOrCreateTag();
                        }
                        GunItem gunItem = (GunItem) stack.getItem();
                        Gun gun = gunItem.getModifiedGun(stack);
                        int[] gunItemFireModes = stack.getTag().getIntArray("supportedFireModes");
                        if(ArrayUtils.isEmpty(gunItemFireModes))
                        {
                            gunItemFireModes = gun.getGeneral().getRateSelector();
                            stack.getTag().putIntArray("supportedFireModes", gunItemFireModes);
                        }
                        else if(!Arrays.equals(gunItemFireModes, gun.getGeneral().getRateSelector()))
                        {
                            stack.getTag().putIntArray("supportedFireModes", gun.getGeneral().getRateSelector());
                        }
                    }
                    InventoryHelper.spawnItemStack(world, pos.getX() + 0.5, pos.getY() + 1.125, pos.getZ() + 0.5, stack);
                }
            }
        }
    }

    /**
     * @param player
     */
    public static void handleUnload(ServerPlayerEntity player)
    {
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() instanceof GunItem)
        {
            CompoundNBT tag = stack.getTag();
            GunItem gunItem = (GunItem) stack.getItem();
            Gun gun = gunItem.getModifiedGun(stack);
            if(tag != null && tag.contains("AmmoCount", Constants.NBT.TAG_INT))
            {
                int count = tag.getInt("AmmoCount");
                tag.putInt("AmmoCount", 0);

                ResourceLocation id = gun.getProjectile().getItem();

                Item item = ForgeRegistries.ITEMS.getValue(id);
                if(item == null)
                {
                    return;
                }

                int maxStackSize = item.getMaxStackSize();
                int stacks = count / maxStackSize;
                for(int i = 0; i < stacks; i++)
                {
                    spawnAmmo(player, new ItemStack(item, maxStackSize));
                }

                int remaining = count % maxStackSize;
                if(remaining > 0)
                {
                    spawnAmmo(player, new ItemStack(item, remaining));
                }
            }
            ResourceLocation reloadSound = gun.getSounds().getCock();
            if(reloadSound != null)
            {
                MessageGunSound message = new MessageGunSound(reloadSound, SoundCategory.PLAYERS, (float) player.getPosX(), (float) player.getPosY() + 1.0F, (float) player.getPosZ(), 1.0F, 1.0F, player.getEntityId(), false, true);
                PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), (player.getPosY() + 1.0), player.getPosZ(), 16.0, player.world.getDimensionKey())), message);
            }
        }
    }

    /**
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
     * @param player
     */
    public static void handleAttachments(ServerPlayerEntity player)
    {
        ItemStack heldItem = player.getHeldItemMainhand();
        if(heldItem.getItem() instanceof GunItem || heldItem.getItem() instanceof ScopeItem)
        {
            NetworkHooks.openGui(player, new SimpleNamedContainerProvider((windowId, playerInventory, player1) -> new AttachmentContainer(windowId, playerInventory, heldItem), new TranslationTextComponent("container.tac.attachments")));
        }
    }

    /**
     * @param player
     */
    public static void handleColorbenchGui(ServerPlayerEntity player)
    {
        ItemStack heldItem = player.getHeldItemMainhand();
        if(heldItem.getItem() instanceof GunItem || heldItem.getItem() instanceof ScopeItem)
        {
            NetworkHooks.openGui(player, new SimpleNamedContainerProvider((windowId, playerInventory, player1) -> new ColorBenchContainer(windowId, playerInventory), new TranslationTextComponent("container.tac.color_bench")));
        }
    }

    /**
     * @param player
     */
    public static void handleInspection(ServerPlayerEntity player)
    {
        ItemStack heldItem = player.getHeldItemMainhand();
        if(heldItem.getItem() instanceof GunItem)
        {
            NetworkHooks.openGui(player, new SimpleNamedContainerProvider((windowId, playerInventory, player1) -> new InspectionContainer(windowId, playerInventory, heldItem), new TranslationTextComponent("container.tac.inspection")));
        }
    }

    /**
     * @param player
     */
    public static void handleFireMode(ServerPlayerEntity player)
    {
        ItemStack heldItem = player.getHeldItemMainhand();
        if(heldItem.getItem() instanceof GunItem)
        {
            if(heldItem.getTag() == null)
            {
                heldItem.getOrCreateTag();
            }
            GunItem gunItem = (GunItem) heldItem.getItem();
            Gun gun = gunItem.getModifiedGun(heldItem.getStack());
            int[] gunItemFireModes = heldItem.getTag().getIntArray("supportedFireModes");
            if(ArrayUtils.isEmpty(gunItemFireModes))
            {
                gunItemFireModes = gun.getGeneral().getRateSelector();
                heldItem.getTag().putIntArray("supportedFireModes", gunItemFireModes);
            }
            else if(!Arrays.equals(gunItemFireModes, gun.getGeneral().getRateSelector()))
            {
                heldItem.getTag().putIntArray("supportedFireModes", gun.getGeneral().getRateSelector());
            }
            int toCheck = ArrayUtils.indexOf(gunItemFireModes, heldItem.getTag().getInt("CurrentFireMode"));
            if(toCheck >= (heldItem.getTag().getIntArray("supportedFireModes").length))
            {
                heldItem.getTag().remove("CurrentFireMode");
                heldItem.getTag().putInt("CurrentFireMode", gunItemFireModes[0]);
            }
            else
            {
                heldItem.getTag().remove("CurrentFireMode");
                heldItem.getTag().putInt("CurrentFireMode", heldItem.getTag().getIntArray("supportedFireModes")[toCheck+1]);
            }

            if(!Config.COMMON.gameplay.safetyExistence.get() && heldItem.getTag().getInt("CurrentFireMode") == 0 && gunItemFireModes.length > 1)
            {
                heldItem.getTag().remove("CurrentFireMode");
                heldItem.getTag().putInt("CurrentFireMode", heldItem.getTag().getIntArray("supportedFireModes")[1]);
            }
            else if(!Config.COMMON.gameplay.safetyExistence.get() && heldItem.getTag().getInt("CurrentFireMode") == 0)
            {
                heldItem.getTag().remove("CurrentFireMode");
                heldItem.getTag().putInt("CurrentFireMode", heldItem.getTag().getIntArray("supportedFireModes")[0]);
            }

            ResourceLocation fireModeSound = gun.getSounds().getCock(); // Use cocking sound for now
            if(fireModeSound != null && player.isAlive())
            {
                MessageGunSound messageSound = new MessageGunSound(fireModeSound, SoundCategory.PLAYERS, (float) player.getPosX(), (float) (player.getPosY() + 1.0), (float) player.getPosZ(), 1F, 1F, player.getEntityId(), false, false);
                PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() ->(ServerPlayerEntity) player), messageSound);
            }
        }
    }

    /**
     * @param player
     */
    public static void EmptyMag(ServerPlayerEntity player)
    {
        ItemStack heldItem = player.getHeldItemMainhand();
        if(heldItem.getItem() instanceof GunItem)
        {
            GunItem gunItem = (GunItem) heldItem.getItem();
            Gun gun = gunItem.getModifiedGun(heldItem.getStack());
            ResourceLocation fireModeSound = gun.getSounds().getCock(); // Use cocking sound for now
            if(fireModeSound != null && player.isAlive())
            {
                MessageGunSound messageSound = new MessageGunSound(fireModeSound, SoundCategory.PLAYERS, (float) player.getPosX(), (float) (player.getPosY() + 1.0), (float) player.getPosZ(), 1.2F, 0.75F, player.getEntityId(), false, false);
                PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() ->(ServerPlayerEntity) player), messageSound);
            }
        }
    }
    //ARCHIVE, THIS MAY CHANGE IN THE FUTURE CAUSE WTF
    /*public static void handleFireMode(ServerPlayerEntity player)
    {
        ItemStack heldItem = player.getHeldItemMainhand();
        if(heldItem.getItem() instanceof TimelessGunItem)
        {
            TimelessGunItem gunItem = (TimelessGunItem) heldItem.getItem();
            Gun gun = gunItem.getModifiedGun(heldItem.getStack());
            int[] gunItemFireModes = heldItem.getTag().getIntArray("supportedFireModes");
            if(ArrayUtils.isEmpty(gunItemFireModes))
            {
                gunItemFireModes = gun.getGeneral().getRateSelector();
                heldItem.getTag().putIntArray("supportedFireModes", gunItemFireModes);
            }
            else if(!Arrays.equals(gunItemFireModes, gun.getGeneral().getRateSelector()))
            {
                heldItem.getTag().putIntArray("supportedFireModes", gun.getGeneral().getRateSelector());
            }
            //int currMode = gun.getGeneral().getRateSelector().length >  ? heldItem.getTag().getInt("CurrentFireMode") : gun.getGeneral().getRateSelector()[0];
            int toCheck = ArrayUtils.indexOf(gunItemFireModes, heldItem.getTag().getInt("CurrentFireMode"));
            if(toCheck >= (heldItem.getTag().getIntArray("supportedFireModes").length))
            {
                heldItem.getTag().remove("CurrentFireMode");
                heldItem.getTag().putInt("CurrentFireMode", gunItemFireModes[0]);
            }
            else
            {
                heldItem.getTag().remove("CurrentFireMode");
                heldItem.getTag().putInt("CurrentFireMode", heldItem.getTag().getIntArray("supportedFireModes")[toCheck+1]);
            }

            if(!Config.COMMON.gameplay.safetyExistence.get() && heldItem.getTag().getInt("CurrentFireMode") == 0 && gunItemFireModes.length > 1)
            {
                heldItem.getTag().remove("CurrentFireMode");
                heldItem.getTag().putInt("CurrentFireMode", heldItem.getTag().getIntArray("supportedFireModes")[1]);
            }
            else if(!Config.COMMON.gameplay.safetyExistence.get() && heldItem.getTag().getInt("CurrentFireMode") == 0)
            {
                heldItem.getTag().remove("CurrentFireMode");
                heldItem.getTag().putInt("CurrentFireMode", heldItem.getTag().getIntArray("supportedFireModes")[0]);
            }

            ResourceLocation fireModeSound = gun.getSounds().getCock(); // Use cocking sound for now
            if(fireModeSound != null && player.isAlive())
            {
                MessageGunSound messageSound = new MessageGunSound(fireModeSound, SoundCategory.PLAYERS, (float) player.getPosX(), (float) (player.getPosY() + 1.0), (float) player.getPosZ(), 1F, 1F, player.getEntityId(), false, false);
                PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() ->(ServerPlayerEntity) player), messageSound);
            }
        }
    }*/

    /**
     * @param player
     * @param lookingRange maximum range to attempt flashlight rendering
     */
    public static void handleFlashLight(ServerPlayerEntity player, int[] lookingRange)
    {
        if(player.getHeldItemMainhand().getItem() instanceof GunItem)
        {
            if(Gun.getAttachment(IAttachment.Type.SIDE_RAIL,player.getHeldItemMainhand()) != null) {
                IWorld world = player.world;
                TileEntity tile = null;
                for (int itor: lookingRange)
                {
                    int x = lookingAt(player, itor).getX();
                    int y = lookingAt(player, itor).getY();
                    int z = lookingAt(player, itor).getZ();
                    boolean createLight = false;
                    for (int i = 0; i < 5; ++i) {
                        tile = world.getTileEntity(new BlockPos(x, y, z));
                        if (tile instanceof FlashLightSource) {
                            createLight = true;
                            break;
                        }

                        if (!world.isAirBlock(new BlockPos(x, y, z))) {
                            int pX = (int) player.getPositionVec().getX();
                            int pY = (int) player.getPositionVec().getY();
                            int pZ = (int) player.getPositionVec().getZ();
                            if (pX > x) {
                                ++x;
                            } else if (pX < x) {
                                --x;
                            }
                            if (pY > y) {
                                ++y;
                            } else if (pY < y) {
                                --y;
                            }
                            if (pZ > z) {
                                ++z;
                            } else if (pZ < z) {
                                --z;
                            }
                        } else if (world.isAirBlock(new BlockPos(x, y, z))) {
                            createLight = true;
                            break;
                        }
                    }

                    if (createLight) {
                        tile = world.getTileEntity(new BlockPos(x, y, z));
                        if (tile instanceof FlashLightSource) {
                            ((FlashLightSource) tile).ticks = 0;
                        } else if (world.getBlockState(new BlockPos(x, y, z)).getBlock() != ModBlocks.FLASHLIGHT_BLOCK.get()) { //
                            world.setBlockState(new BlockPos(x, y, z), (ModBlocks.FLASHLIGHT_BLOCK.get()).getDefaultState(), 3);
                        }
                        world.setBlockState(new BlockPos(x, y, z), (ModBlocks.FLASHLIGHT_BLOCK.get()).getDefaultState(), 3);
                    }
                }
            }
        }
    }
    protected static BlockPos lookingAt(PlayerEntity player, int rangeL)
    {
        //RayTraceResult entityPos = lookingAtEntity(player,rangeL);GunMod.LOGGER.log(Level.FATAL, entityPos.getType().toString());
        ///if(entityPos instanceof EntityRayTraceResult)
         //   return new BlockPos(entityPos.getHitVec());
        //else
            return ((BlockRayTraceResult)player.pick((double)rangeL, 0.0F, false)).getPos();

    }
    protected static RayTraceResult lookingAtEntity(PlayerEntity player, int rangeL)
    {
        return ((RayTraceResult)player.pick((double)rangeL, 0.0F, false));
    }
    /**
     * @param player
     */
    /*public static void handleIronSightSwitch(ServerPlayerEntity player)
    {
        ItemStack heldItem = player.getHeldItemMainhand();
        if(heldItem.getItem() instanceof TimelessGunItem)
        {
            Gun gun = ((TimelessGunItem) heldItem.getItem()).getModifiedGun(heldItem.getStack());

            if(!ArrayUtils.isEmpty(gun.getModules().getZoom()))
            {
                int currentZoom = heldItem.getTag().getInt("currentZoom");

                if(currentZoom == (gun.getModules().getZoom().length-2))
                {
                    heldItem.getTag().remove("currentZoom");
                }
                else
                {
                    heldItem.getTag().remove("currentZoom");
                    heldItem.getTag().putInt("currentZoom",currentZoom+1);
                }
            }
        }
    }*/


    private static final UUID speedUptId = UUID.fromString("923e4567-e89b-42d3-a456-556642440000");

    // https://forums.minecraftforge.net/topic/40878-1102-solved-increasedecrease-walkspeed-without-fov-change/ Adapted for 1.16.5 use and for proper server function
    private static void changeGunSpeedMod(ServerPlayerEntity entity, String name, double modifier)
    {
        AttributeModifier speedModifier = (new AttributeModifier(speedUptId, name, modifier, AttributeModifier.Operation.MULTIPLY_TOTAL));
        ModifiableAttributeInstance attributeInstance = entity.getAttribute(MOVEMENT_SPEED);

        if (attributeInstance.getModifier(speedUptId) != null) {
            attributeInstance.removeModifier(speedModifier);
        }
        attributeInstance.applyPersistentModifier(speedModifier);
    }

    private static void removeGunSpeedMod(ServerPlayerEntity entity, String name, double modifier)
    {
        AttributeModifier speedModifier = (new AttributeModifier(speedUptId, name, modifier, AttributeModifier.Operation.MULTIPLY_TOTAL));
        ModifiableAttributeInstance attributeInstance = entity.getAttribute(MOVEMENT_SPEED);

        if (attributeInstance.getModifier(speedUptId) != null) {
            attributeInstance.removeModifier(speedModifier);
        }
    }

    public static void handleMovementUpdate(ServerPlayerEntity player, boolean handle)
    {
        if (player == null)
            return;
        if(player.isSpectator())
            return;
        if(!player.isAlive())
            return;

        ItemStack heldItem = player.getHeldItemMainhand();
        if(player.getAttribute(MOVEMENT_SPEED) != null && MovementAdaptationsHandler.get().isReadyToReset())
        {
            removeGunSpeedMod(player,"GunSpeedMod", 0.1);
            MovementAdaptationsHandler.get().setReadyToReset(false);
            MovementAdaptationsHandler.get().setReadyToUpdate(true);
        }
        player.sendPlayerAbilities();

        if (!(heldItem.getItem() instanceof TimelessGunItem))
            return;

        Gun gun = ((TimelessGunItem) heldItem.getItem()).getGun();
        //if(MovementAdaptationsHandler.get().previousGun == null || gun.serializeNBT().getId() == MovementAdaptationsHandler.get().previousGun)
            if (((gun.getGeneral().getWeightKilo() > 0) && MovementAdaptationsHandler.get().isReadyToUpdate()) || MovementAdaptationsHandler.get().getPreviousWeight() != gun.getGeneral().getWeightKilo())
            {
                float speed = (float)player.getAttribute(MOVEMENT_SPEED).getValue() / (1+((gun.getGeneral().getWeightKilo()*(1+GunModifierHelper.getModifierOfWeaponWeight(heldItem)) + GunModifierHelper.getAdditionalWeaponWeight(heldItem) - GunEnchantmentHelper.getWeightModifier(heldItem)) * 0.0275f)); // * 0.01225f));// //(1+GunModifierHelper.getModifierOfWeaponWeight(heldItem)) + GunModifierHelper.getAdditionalWeaponWeight(heldItem)) / 3.775F));
                if(player.isSprinting())
                    speed = Math.max(Math.min(speed, 0.12F), 0.075F) * 0.775F;
                else
                    speed = Math.max(Math.min(speed, 0.095F), 0.075F);
                changeGunSpeedMod(player, "GunSpeedMod", -((double)((0.1 - speed)*10)));//*1000

                MovementAdaptationsHandler.get().setReadyToReset(true);
                MovementAdaptationsHandler.get().setReadyToUpdate(false);
                MovementAdaptationsHandler.get().setSpeed(speed);
            }
            else
                MovementAdaptationsHandler.get().setSpeed((float)player.getAttribute(MOVEMENT_SPEED).getValue());
        player.sendPlayerAbilities();

        MovementAdaptationsHandler.get().setPreviousWeight(gun.getGeneral().getWeightKilo());
        //DEBUGGING AND BALANCE TOOL
        //player.sendStatusMessage(new TranslationTextComponent(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.MOVING)+""), true);
        //new TranslationTextComponent("Speed is: " + player
                // .getAttribute(MOVEMENT_SPEED).getValue()) ,true);
    }

    public static void handleGunID(ServerPlayerEntity player, boolean regenerate)
    {
        if(!player.isAlive())
            return;
        if(NetworkGunManager.get() != null && NetworkGunManager.get().StackIds != null) {
            if (player.getHeldItemMainhand().getItem() instanceof TimelessGunItem && player.getHeldItemMainhand().getTag() != null) {
                if (regenerate||!player.getHeldItemMainhand().getTag().contains("ID")) {
                    UUID id;
                    while (true) {
                        LOGGER.log(Level.INFO, "NEW UUID GEN FOR TAC GUN");
                        id = UUID.randomUUID();
                        if (NetworkGunManager.get().Ids.add(id))
                            break;
                    }
                    player.getHeldItemMainhand().getTag().putUniqueId("ID", id);
                    NetworkGunManager.get().StackIds.put(id, player.getHeldItemMainhand());
                }
                initLevelTracking(player.getHeldItemMainhand());
            }
        }
    }

    private static void initLevelTracking(ItemStack gunStack)
    {
        if(gunStack.getTag().get("level") == null) {
            gunStack.getTag().putInt("level", 1);
        }
        if(gunStack.getTag().get("levelDmg") == null) {
            gunStack.getTag().putFloat("levelDmg", 0f);
        }
    }

    public static void handleUpgradeBenchItem(MessageSaveItemUpgradeBench message, ServerPlayerEntity player)
    {
        if(!player.isSpectator())
        {
            World world = player.world;
            ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
            TileEntity tileEntity = world.getTileEntity(message.getPos());

            if(player.isCrouching())
            {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
                return;
            }
            world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.8F);
            //if item is empty or air stack in hand, take weapon, if holding weapon, take or replace weapon
            if (tileEntity != null)
            {
                // React to adding an extra Module item
                //if()

                if (!(((UpgradeBenchTileEntity) tileEntity).getStackInSlot(0).getItem() instanceof GunItem) && heldItem.getItem() instanceof GunItem)
                {
                    ((UpgradeBenchTileEntity) tileEntity).setInventorySlotContents(0, heldItem);
                    player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.AIR));
                    // I hate this last part, this is used in order to reset the TileRenderer,
                    // without this the item stack is added, but the visual is only reset on
                    // entering GUI, gotta Check what Yor said about this portion.
                    NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
                    player.closeScreen();
                }
                else if (heldItem.getItem() == ModItems.MODULE.get() && ((UpgradeBenchTileEntity) tileEntity).getStackInSlot(1).getCount() < 3)
                {
                    if( ((UpgradeBenchTileEntity) tileEntity).getStackInSlot(1).getItem() != ModItems.MODULE.get() ) {
                        ((UpgradeBenchTileEntity) tileEntity).setInventorySlotContents(1,
                                heldItem.copy());
                        ((UpgradeBenchTileEntity) tileEntity).getStackInSlot(1).setCount(1);
                    }
                    else {
                        ((UpgradeBenchTileEntity) tileEntity).getStackInSlot(1).setCount(((UpgradeBenchTileEntity) tileEntity).getStackInSlot(1).getCount() + 1);
                    }
                    player.getHeldItem(Hand.MAIN_HAND).setCount(player.getHeldItem(Hand.MAIN_HAND).getCount()-1);
                    /// I hate this last part, this is used in order to reset the TileRenderer,
                    // without this the item stack is added, but the visual is only reset on
                    // entering GUI, gotta Check what Yor said about this portion.
                    NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
                    player.closeScreen();
                }
                else
                {
                    player.inventory.addItemStackToInventory(((UpgradeBenchTileEntity) tileEntity).getStackInSlot(0));
                    ((UpgradeBenchTileEntity) tileEntity).setInventorySlotContents(0, ItemStack.EMPTY);
                    // I hate this last part, this is used in order to reset the TileRenderer,
                    // without this the item stack is added, but the visual is only reset on
                    // entering GUI, gotta Check what Yor said about this portion.
                    NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
                    player.closeScreen();
                }
            }
        }
    }
    //remove enchant minecraft "modding" 1.16
    /**
     * Crafts the specified item at the workstation the player is currently using.
     * This is only intended for use on the logical server.
     *
     * @param player the player who is crafting
     */
    public static void handleUpgradeBenchApply(MessageUpgradeBenchApply message, ServerPlayerEntity player)
    {
        if(player.openContainer instanceof UpgradeBenchContainer)
        {
            UpgradeBenchContainer workbench = (UpgradeBenchContainer) player.openContainer;
            UpgradeBenchScreen.RequirementItem req =
                    GunEnchantmentHelper.upgradeableEnchs.get(message.reqKey);
            if(workbench.getPos().equals(message.pos))
            {
                ItemStack toUpdate = workbench.getBench().getInventory().get(0);
                int currLevel =
                        EnchantmentHelper.getEnchantmentLevel(req.enchantment, toUpdate);
                if(toUpdate.getTag() == null)
                    return;
                int currWeaponLevel = toUpdate.getTag().getInt("level");
                TimelessGunItem gunItem = (TimelessGunItem) toUpdate.getItem();
                if(workbench.getBench().getStackInSlot(1).getCount() >= req.getModuleCount()[currLevel] && currWeaponLevel >= req.getLevelReq()[currLevel] && gunItem.getGun().getGeneral().getUpgradeBenchMaxUses() > toUpdate.getTag().getInt("upgradeBenchUses"))
                {
                    if (currLevel > 0) {
                        Map<Enchantment, Integer> listNBT =
                                EnchantmentHelper.deserializeEnchantments(toUpdate.getEnchantmentTagList());
                        listNBT.replace(req.enchantment, currLevel + 1);
                        EnchantmentHelper.setEnchantments(listNBT, toUpdate);
                    } else
                        toUpdate.addEnchantment(req.enchantment, 1);

                    workbench.getBench().getStackInSlot(1).setCount(workbench.getBench().getStackInSlot(1).getCount()-req.getModuleCount()[currLevel]);
                    toUpdate.getTag().putInt("upgradeBenchUses", toUpdate.getTag().getInt(
                            "upgradeBenchUses")+1);
                }
                else
                    player.sendStatusMessage(new TranslationTextComponent("Cannot apply enchants anymore"), true);
            }
        }
    }

    /**
     * @param player
     */
    public static void handleArmorFixApplication(ServerPlayerEntity player)
    {
        if(WearableHelper.PlayerWornRig(player) != null && !WearableHelper.isFullDurability(WearableHelper.PlayerWornRig(player)))
        {
            Rig rig = ((ArmorRigItem)WearableHelper.PlayerWornRig(player).getItem()).getRig();
            if(player.getHeldItemMainhand().getItem().getRegistryName().equals(rig.getRepair().getItem()))
            {
                WearableHelper.tickRepairCurrentDurability(WearableHelper.PlayerWornRig(player));
                player.getHeldItemMainhand().setCount(player.getHeldItemMainhand().getCount()-1);
                ResourceLocation repairSound = rig.getSounds().getRepair();
                if (repairSound != null && player.isAlive()) {
                    MessageGunSound messageSound = new MessageGunSound(repairSound, SoundCategory.PLAYERS, (float) player.getPosX(), (float) (player.getPosY() + 1.0), (float) player.getPosZ(), 1F, 1F, player.getEntityId(), false, false);
                    PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), messageSound);
                }
            }
        }
    }

    /**
     * @param player
     */
    public static void handleRigAmmoCount(ServerPlayerEntity player, ResourceLocation id)
    {
        if(WearableHelper.PlayerWornRig(player) != null)
        {
            ItemStack rig = WearableHelper.PlayerWornRig(player);
            if(rig != null) {
                PacketHandler.getPlayChannel().sendTo(new MessageRigInvToClient(rig, id), ((ServerPlayerEntity)player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
        }
        else
            PacketHandler.getPlayChannel().sendTo(new MessageRigInvToClient(), ((ServerPlayerEntity)player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }
}
