package com.mrcrayfish.guns.item;

import com.google.common.annotations.Beta;
import com.google.common.base.Predicate;
import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.ItemStackUtil;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.common.ProjectileFactory;
import com.mrcrayfish.guns.common.SpreadHandler;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.event.CommonEvents;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageBullet;
import com.mrcrayfish.guns.network.message.MessageMuzzleFlash;
import com.mrcrayfish.guns.network.message.MessageShoot;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

@Beta
public class ItemGun extends ItemColored
{
    private Gun gun;

    private static final Predicate<EntityLivingBase> NOT_AGGRO_EXEMPT = entity -> !(entity instanceof EntityPlayer) && !GunConfig.AggroMobs.exemptClasses.contains(entity.getClass());

    public ItemGun(ResourceLocation id)
    {
        this.setTranslationKey(id.getNamespace() + "." + id.getPath());
        this.setRegistryName(id);
        this.setMaxStackSize(1);
        GunRegistry.getInstance().register(this);
    }

    void setGun(Gun gun)
    {
        this.gun = gun;
    }

    public Gun getGun()
    {
        return gun;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        Gun modifiedGun = getModifiedGun(stack);

        String additionalDamageText = "";
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound != null)
        {
            if(tagCompound.hasKey("AdditionalDamage", Constants.NBT.TAG_FLOAT))
            {
                float additionalDamage = tagCompound.getFloat("AdditionalDamage");
                if(additionalDamage > 0)
                {
                    additionalDamageText = TextFormatting.GREEN + " +" + tagCompound.getFloat("AdditionalDamage");
                }
                else if(additionalDamage < 0)
                {
                    additionalDamageText = TextFormatting.RED + " " + tagCompound.getFloat("AdditionalDamage");
                }
            }
        }

        tooltip.add(TextFormatting.GRAY + I18n.format("info.cgm.damage", TextFormatting.RESET + Float.toString(gun.projectile.getDamage(modifiedGun)) + additionalDamageText));

        if(tagCompound != null)
        {
            if(tagCompound.getBoolean("IgnoreAmmo"))
            {
                tooltip.add(TextFormatting.AQUA + I18n.format("info.cgm.ignore_ammo"));
            }
            else
            {
                int ammoCount = tagCompound.getInteger("AmmoCount");
                tooltip.add(TextFormatting.GRAY + I18n.format("info.cgm.ammo", TextFormatting.RESET + Integer.toString(ammoCount), gun.general.getMaxAmmo(modifiedGun)));
            }
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
        return true;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase entity, int count)
    {
        Gun modifiedGun = getModifiedGun(stack);
        if(!modifiedGun.general.auto)
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;

        if(!player.world.isRemote)
        {
            return;
        }

        if(ItemGun.hasAmmo(stack) || player.capabilities.isCreativeMode)
        {
            CooldownTracker tracker = player.getCooldownTracker();
            if(!tracker.hasCooldown(stack.getItem()))
            {
                tracker.setCooldown(stack.getItem(), modifiedGun.general.rate);
                PacketHandler.INSTANCE.sendToServer(new MessageShoot());
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        if(worldIn.isRemote)
        {
            if(ItemGun.hasAmmo(heldItem) || playerIn.capabilities.isCreativeMode)
            {
                if(playerIn.isHandActive())
                {
                    return new ActionResult<>(EnumActionResult.FAIL, heldItem);
                }
                playerIn.setActiveHand(handIn);

                Gun modifiedGun = getModifiedGun(heldItem);
                if(!modifiedGun.general.auto)
                {
                    CooldownTracker tracker = playerIn.getCooldownTracker();
                    if(!tracker.hasCooldown(heldItem.getItem()))
                    {
                        tracker.setCooldown(heldItem.getItem(), modifiedGun.general.rate);
                        PacketHandler.INSTANCE.sendToServer(new MessageShoot());
                    }
                }
            }
            else
            {
                MrCrayfishGunMod.proxy.playClientSound(SoundEvents.BLOCK_LEVER_CLICK);
            }
        }
        return new ActionResult<>(EnumActionResult.FAIL, heldItem);
    }

    public static void fire(World worldIn, EntityPlayer playerIn, ItemStack heldItem)
    {
        if(worldIn.isRemote)
        {
            return;
        }

        ItemGun item = (ItemGun) heldItem.getItem();
        if(CommonEvents.getCooldownTracker(playerIn.getUniqueID()).hasCooldown(item))
        {
            MrCrayfishGunMod.logger.info(playerIn.getName() + "(" + playerIn.getUniqueID() + ") tried to fire before cooldown finished or server is lagging?");
            return;
        }

        if(playerIn.getDataManager().get(CommonEvents.RELOADING))
        {
            playerIn.getDataManager().set(CommonEvents.RELOADING, false);
        }

        boolean silenced = Gun.getAttachment(IAttachment.Type.BARREL, heldItem).getItem() == ModGuns.SILENCER;

        Gun modifiedGun = item.getModifiedGun(heldItem);
        if(!modifiedGun.general.alwaysSpread && modifiedGun.general.spread > 0F)
        {
            SpreadHandler.getSpreadTracker(playerIn.getUniqueID()).update(item);
        }

        for(int i = 0; i < modifiedGun.general.projectileAmount; i++)
        {
            ProjectileFactory factory = AmmoRegistry.getInstance().getFactory(modifiedGun.projectile.item);
            EntityProjectile bullet = factory.create(worldIn, playerIn, item, modifiedGun);
            bullet.setWeapon(heldItem);
            bullet.setAdditionalDamage(ItemGun.getAdditionalDamage(heldItem));
            if(silenced)
            {
                bullet.setDamageModifier(0.75F);
            }
            worldIn.spawnEntity(bullet);

            if(!modifiedGun.projectile.visible)
            {
                MessageBullet messageBullet = new MessageBullet(bullet.getEntityId(), bullet.posX, bullet.posY, bullet.posZ, bullet.motionX, bullet.motionY, bullet.motionZ, modifiedGun.projectile.trailColor);
                PacketHandler.INSTANCE.sendToAllAround(messageBullet, new NetworkRegistry.TargetPoint(playerIn.dimension, playerIn.posX, playerIn.posY, playerIn.posZ, GunConfig.SERVER.network.projectileTrackingRange));
                PacketHandler.INSTANCE.sendTo(messageBullet, (EntityPlayerMP) playerIn);
            }
        }

        if(GunConfig.SERVER.aggroMobs.enabled)
        {
            double r = silenced ? GunConfig.SERVER.aggroMobs.rangeSilenced : GunConfig.SERVER.aggroMobs.rangeUnsilenced;
            double x = playerIn.posX + 0.5;
            double y = playerIn.posY + 0.5;
            double z = playerIn.posZ + 0.5;
            AxisAlignedBB box = new AxisAlignedBB(x - r, y - r, z - r, x + r, y + r, z + r);
            r *= r;
            double dx, dy, dz;
            for(EntityLivingBase entity : playerIn.world.getEntitiesWithinAABB(EntityLivingBase.class, box, NOT_AGGRO_EXEMPT))
            {
                dx = x - entity.posX;
                dy = y - entity.posY;
                dz = z - entity.posZ;
                if(dx * dx + dy * dy + dz * dz <= r)
                {
                    entity.setRevengeTarget(GunConfig.SERVER.aggroMobs.angerHostileMobs ? playerIn : entity);
                }
            }
        }

        if(silenced)
        {
            String silencedSound = modifiedGun.sounds.getSilencedFire(modifiedGun);
            SoundEvent event = ModSounds.getSound(silencedSound);
            if(event == null)
            {
                event = SoundEvent.REGISTRY.getObject(new ResourceLocation(silencedSound));
            }
            if(event != null)
            {
                worldIn.playSound(null, playerIn.getPosition(), event, SoundCategory.HOSTILE, 1F, 0.8F + itemRand.nextFloat() * 0.2F);
            }
        }
        else
        {
            String fireSound = modifiedGun.sounds.getFire(modifiedGun);
            SoundEvent event = ModSounds.getSound(fireSound);
            if(event == null)
            {
                event = SoundEvent.REGISTRY.getObject(new ResourceLocation(fireSound));
            }
            if(event != null)
            {
                worldIn.playSound(null, playerIn.getPosition(), event, SoundCategory.HOSTILE, 5.0F, 0.8F + itemRand.nextFloat() * 0.2F);
            }
        }

        if(modifiedGun.display.flash != null)
        {
            PacketHandler.INSTANCE.sendTo(new MessageMuzzleFlash(), (EntityPlayerMP) playerIn);
        }

        if(!playerIn.capabilities.isCreativeMode)
        {
            NBTTagCompound tag = ItemStackUtil.createTagCompound(heldItem);
            if(!tag.getBoolean("IgnoreAmmo"))
            {
                tag.setInteger("AmmoCount", Math.max(0, tag.getInteger("AmmoCount") - 1));
            }
        }

        CommonEvents.getCooldownTracker(playerIn.getUniqueID()).setCooldown(item, modifiedGun.general.rate);
    }

    private static float getAdditionalDamage(ItemStack gunStack)
    {
        NBTTagCompound tag = ItemStackUtil.createTagCompound(gunStack);
        return tag.getFloat("AdditionalDamage");
    }

    public static ItemStack findAmmo(EntityPlayer player, ResourceLocation id)
    {
        if(player.capabilities.isCreativeMode)
        {
            ItemAmmo ammo = AmmoRegistry.getInstance().getAmmo(id);
            return ammo != null ? new ItemStack(ammo, 64) : ItemStack.EMPTY;
        }
        for(int i = 0; i < player.inventory.getSizeInventory(); ++i)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(isAmmo(stack, id))
            {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private static boolean isAmmo(ItemStack stack, ResourceLocation id)
    {
        return stack != null && stack.getItem().getRegistryName().equals(id);
    }

    public static boolean hasAmmo(ItemStack gunStack)
    {
        NBTTagCompound tag = ItemStackUtil.createTagCompound(gunStack);
        return tag.getBoolean("IgnoreAmmo") || tag.getInteger("AmmoCount") > 0;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if(isInCreativeTab(tab))
        {
            ItemStack stack = new ItemStack(this);
            ItemStackUtil.createTagCompound(stack).setInteger("AmmoCount", gun.general.maxAmmo);
            items.add(stack);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return slotChanged;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        NBTTagCompound tagCompound = ItemStackUtil.createTagCompound(stack);
        return !tagCompound.getBoolean("IgnoreAmmo") && tagCompound.getInteger("AmmoCount") != gun.general.maxAmmo;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        NBTTagCompound tagCompound = ItemStackUtil.createTagCompound(stack);
        return 1.0 - (tagCompound.getInteger("AmmoCount") / (double) gun.general.maxAmmo);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack)
    {
        return Color.CYAN.getRGB();
    }

    public Gun getModifiedGun(ItemStack stack)
    {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound != null && tagCompound.hasKey("Gun", Constants.NBT.TAG_COMPOUND))
        {
            Gun gunCopy = gun.copy();
            gunCopy.deserializeNBT(tagCompound.getCompoundTag("Gun"));
            return gunCopy;
        }
        return gun;
    }
}
