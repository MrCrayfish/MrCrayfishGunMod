package com.tac.guns.client.handler;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Config;
import com.tac.guns.common.Gun;
import com.tac.guns.common.SpreadTracker;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class RecoilHandler
{
    private static RecoilHandler instance;

    public static RecoilHandler get()
    {
        if(instance == null)
        {
            instance = new RecoilHandler();
        }
        return instance;
    }

    private Random random = new Random();
    private int recoilRand;
    private double gunRecoilNormal;
    private double gunRecoilAngle;
    private double gunHorizontalRecoilAngle;
    public float cameraRecoil; // READONLY

    private float progressCameraRecoil;

    public float horizontalCameraRecoil; // READONLY

    private float horizontalProgressCameraRecoil;

    private int timer;

    private long prevTime = System.currentTimeMillis();

    private final int recoilDuration = 200; //0.20s

    private RecoilHandler() {}

    @SubscribeEvent
    public void preShoot(GunFireEvent.Pre event) {
        if(!(event.getStack().getItem() instanceof GunItem))
            return;
        this.recoilRand = this.random.nextInt(2);
    }

    @SubscribeEvent
    public void onGunFire(GunFireEvent.Post event)
    {
        if(!event.isClient())
            return;

        if(!Config.SERVER.enableCameraRecoil.get())
            return;

        ItemStack heldItem = event.getStack();
        GunItem gunItem = (GunItem) heldItem.getItem();
        Gun modifiedGun = gunItem.getModifiedGun(heldItem);

        float verticalRandomAmount = this.random.nextFloat()*(1.22f - 0.75f) + 0.75f;

        float recoilModifier = 1.0F - GunModifierHelper.getRecoilModifier(heldItem);
        recoilModifier *= this.getAdsRecoilReduction(modifiedGun);
        recoilModifier *= GunEnchantmentHelper.getBufferedRecoil(heldItem);
        recoilModifier *= verticalRandomAmount;
        this.cameraRecoil = modifiedGun.getGeneral().getRecoilAngle() * recoilModifier;
        this.progressCameraRecoil = 0F;

        // Horizontal Recoil
        this.lastRandPitch = random.nextFloat();
        this.lastRandYaw = random.nextFloat();

        float horizontalRandomAmount = this.random.nextFloat()*(1.22f - 0.75f) + 0.75f;

        float horizontalRecoilModifier = 1.0F - GunModifierHelper.getHorizontalRecoilModifier(heldItem);
        horizontalRecoilModifier *= this.getAdsRecoilReduction(modifiedGun);
        horizontalRecoilModifier *= GunEnchantmentHelper.getBufferedRecoil(heldItem);
        horizontalRecoilModifier *= horizontalRandomAmount;
        horizontalCameraRecoil = (modifiedGun.getGeneral().getHorizontalRecoilAngle() * horizontalRecoilModifier * 0.75F);
        horizontalProgressCameraRecoil = 0F;

        timer = recoilDuration;
    }
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if(timer > 0) timer -= System.currentTimeMillis() - prevTime;
        prevTime = System.currentTimeMillis();
        if(timer < 0) timer = 0;

        if(!Config.SERVER.enableCameraRecoil.get())
            return;

        if(event.phase != TickEvent.Phase.END || this.cameraRecoil <= 0)
            return;

        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        float cameraRecoilModifer = mc.player.getHeldItemMainhand().getItem() instanceof GunItem ? ((GunItem) mc.player.getHeldItemMainhand().getItem()).getGun().getGeneral().getCameraRecoilModifier() : 1.0F;

        float recoilAmount = this.cameraRecoil * mc.getTickLength() * 0.2F;//0.25F;//0.1F;
        float HorizontalRecoilAmount = this.horizontalCameraRecoil * mc.getTickLength() * 0.1F;//0.25F;//* 0.1F;
        float startProgress = (this.progressCameraRecoil / this.cameraRecoil);
        float endProgress = ((this.progressCameraRecoil + recoilAmount) / this.cameraRecoil);

        float progressForward = mc.player.getHeldItemMainhand().getItem() instanceof GunItem ? ((GunItem) mc.player.getHeldItemMainhand().getItem()).getGun().getGeneral().getRecoilDuration() *
                GunModifierHelper.getRecoilSmootheningTime(mc.player.getHeldItemMainhand()) : 0.25F;
        if(startProgress < progressForward) // && startProgress > 0.125F
        {
            mc.player.rotationPitch -= ((endProgress - startProgress) / progressForward) * this.cameraRecoil / cameraRecoilModifer;
            if(recoilRand == 1)
                mc.player.rotationYaw -= ((endProgress - startProgress) / progressForward) * this.horizontalCameraRecoil / cameraRecoilModifer;
            else
                mc.player.rotationYaw -= ((endProgress - startProgress) / progressForward) * -this.horizontalCameraRecoil / cameraRecoilModifer;
        }
        else if(startProgress > progressForward)
        {
            mc.player.rotationPitch += ((endProgress - startProgress) / (1-progressForward) ) * this.cameraRecoil / (cameraRecoilModifer*1.025); // 0.75F
            if(recoilRand == 1)
                mc.player.rotationYaw -= ((endProgress - startProgress) / (1-progressForward)) * -this.horizontalCameraRecoil / (cameraRecoilModifer*1.025);
            else
                mc.player.rotationYaw -= ((endProgress - startProgress) / (1-progressForward)) * this.horizontalCameraRecoil / (cameraRecoilModifer*1.025);
        }

        this.progressCameraRecoil += recoilAmount;

        if(this.progressCameraRecoil >= this.cameraRecoil)
        {
            this.cameraRecoil = 0;
            this.progressCameraRecoil = 0;
        }

        this.horizontalProgressCameraRecoil += HorizontalRecoilAmount;

        if(this.horizontalProgressCameraRecoil >= this.horizontalCameraRecoil)
        {
            this.horizontalCameraRecoil = 0;
            this.horizontalProgressCameraRecoil = 0;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderOverlay(RenderHandEvent event)
    {
        if(event.getHand() != Hand.MAIN_HAND)
            return;

        ItemStack heldItem = event.getItemStack();
        if(!(heldItem.getItem() instanceof GunItem))
            return;

        GunItem gunItem = (GunItem) heldItem.getItem();
        Gun modifiedGun = gunItem.getModifiedGun(heldItem);
        float cooldown = (float) timer / recoilDuration;

        float recoilTimeOffset = modifiedGun.getGeneral().getWeaponRecoilOffset();
        /*float cooldown ;
        if((tracker.getCooldown(gunItem, Minecraft.getInstance().getRenderPartialTicks()))<0.5f)
            cooldown = 0;/*(tracker.getCooldown(gunItem,
                    Minecraft.getInstance().getRenderPartialTicks()));
        else
        cooldown = (tracker.getCooldown(gunItem,
                Minecraft.getInstance().getRenderPartialTicks())-0.5f)*2f;*/
        if(cooldown >= recoilTimeOffset)// || tooFast) // Actually have any visual recoil at Rate 1???
        {
            float amount = 1F * ((1.0F - cooldown) / (1-modifiedGun.getGeneral().getWeaponRecoilOffset()));
            this.gunRecoilNormal = 1 - (--amount);
        }
        else {
            float amount = ( (cooldown) / modifiedGun.getGeneral().getWeaponRecoilOffset() );
            this.gunRecoilNormal = amount < 0.5 ? 2 * amount * amount : -1 + (4 - 2 * amount) * amount;
        }

        this.gunRecoilAngle = modifiedGun.getGeneral().getRecoilAngle();
        this.gunHorizontalRecoilAngle = modifiedGun.getGeneral().getHorizontalRecoilAngle();
    }

    public double getAdsRecoilReduction(Gun gun)
    {
        return 1.0 - gun.getGeneral().getRecoilAdsReduction() * AimingHandler.get().getNormalisedAdsProgress();
    }

    public double getGunRecoilNormal()
    {
        return this.gunRecoilNormal;
    }

    public double getGunRecoilAngle()
    {
        return this.gunRecoilAngle;
    }

    public double getGunHorizontalRecoilAngle()
    {
        return this.gunHorizontalRecoilAngle;
    }

    public double getRecoilProgress() {return timer / (double)recoilDuration;}

    private static Vector3d getVectorFromRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vector3d((double) (f1 * f2), (double) f3, (double) (f * f2));
    }

    private static Random rand = new Random();
    private Vector3d nextShotAccuracy = null;
    public float lastRandPitch = 0f;
    public float lastRandYaw = 0f;

    public Vector3d setNextAccuracyRecoilVectorRendered(LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun)
    {
        float gunSpread = GunModifierHelper.getModifiedSpread(weapon, modifiedGun.getGeneral().getSpread()) * GunEnchantmentHelper.getSpreadModifier(weapon);
        if(shooter instanceof PlayerEntity)
        {
            if(!modifiedGun.getGeneral().isAlwaysSpread())
            {
                float modSpread = SpreadTracker.get((PlayerEntity) shooter).getSpread(item);
                if(modSpread != 0)
                    gunSpread *= SpreadTracker.get((PlayerEntity) shooter).getSpread(item);
                else
                    gunSpread = modifiedGun.getGeneral().getFirstShotSpread();
            }
            if(!SyncedPlayerData.instance().get((PlayerEntity) shooter, ModSyncedDataKeys.AIMING))
            {
                gunSpread *= modifiedGun.getGeneral().getHipFireInaccuracy();
                if(SyncedPlayerData.instance().get((PlayerEntity) shooter, ModSyncedDataKeys.MOVING) != 0)
                {
                    gunSpread *= Math.max(1 , (2F * ( 1 + SyncedPlayerData.instance().get((PlayerEntity) shooter, ModSyncedDataKeys.MOVING))) * modifiedGun.getGeneral().getMovementInaccuracy());
                }
            }
            if(((PlayerEntity) shooter).isCrouching() && modifiedGun.getGeneral().getProjectileAmount() == 1)
            {
                gunSpread *= 0.75F;
            }
        }
        lastRandPitch = rand.nextFloat();
        lastRandYaw = rand.nextFloat();
        this.nextShotAccuracy = getVectorFromRotation((gunSpread / 2.0F) + lastRandPitch * gunSpread, (gunSpread / 2.0F) + lastRandYaw * gunSpread);
        return getVectorFromRotation((gunSpread / 2.0F) + lastRandPitch * gunSpread, (gunSpread / 2.0F) + lastRandYaw * gunSpread);
    }

    /*public Vector3d setNextAccuracyRecoilVectorProjectile(LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun)
    {
        float gunSpread = GunModifierHelper.getModifiedSpread(weapon, modifiedGun.getGeneral().getSpread()) * GunEnchantmentHelper.getSpreadModifier(weapon);
        if(shooter instanceof PlayerEntity)
        {
            if(!modifiedGun.getGeneral().isAlwaysSpread())
            {
                float modSpread = SpreadTracker.get((PlayerEntity) shooter).getSpread(item);
                if(modSpread != 0)
                    gunSpread *= SpreadTracker.get((PlayerEntity) shooter).getSpread(item);
                else
                    gunSpread = modifiedGun.getGeneral().getFirstShotSpread();
            }
            if(!SyncedPlayerData.instance().get((PlayerEntity) shooter, ModSyncedDataKeys.AIMING))
            {
                gunSpread *= modifiedGun.getGeneral().getHipFireInaccuracy();
                if(SyncedPlayerData.instance().get((PlayerEntity) shooter, ModSyncedDataKeys.MOVING) != 0)
                {
                    gunSpread *= Math.max(1 , (2F * ( 1 + SyncedPlayerData.instance().get((PlayerEntity) shooter, ModSyncedDataKeys.MOVING))) * modifiedGun.getGeneral().getMovementInaccuracy());
                }
            }
            if(((PlayerEntity) shooter).isCrouching() && modifiedGun.getGeneral().getProjectileAmount() == 1)
            {
                gunSpread *= 0.75F;
            }
        }
        return getVectorFromRotation(shooter.rotationPitch - (gunSpread / 2.0F) + lastRand * gunSpread, shooter.rotationYaw - (gunSpread / 2.0F) + lastRand * gunSpread);
    }*/
}
