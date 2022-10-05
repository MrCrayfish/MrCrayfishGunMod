package com.tac.guns.client.handler;

import com.tac.guns.Config;
import com.tac.guns.common.Gun;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
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
    private float gunRecoilRandom;

    public float cameraRecoil; // READONLY

    private float progressCameraRecoil;

    public float horizontalCameraRecoil; // READONLY

    private float horizontalProgressCameraRecoil;

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
        this.gunRecoilRandom = random.nextFloat();

        float horizontalRandomAmount = this.random.nextFloat()*(1.22f - 0.75f) + 0.75f;

        float horizontalRecoilModifier = 1.0F - GunModifierHelper.getHorizontalRecoilModifier(heldItem);
        horizontalRecoilModifier *= this.getAdsRecoilReduction(modifiedGun);
        horizontalRecoilModifier *= GunEnchantmentHelper.getBufferedRecoil(heldItem);
        horizontalRecoilModifier *= horizontalRandomAmount;
        horizontalCameraRecoil = (modifiedGun.getGeneral().getHorizontalRecoilAngle() * horizontalRecoilModifier * 0.75F);
        horizontalProgressCameraRecoil = 0F;
    }
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if(!Config.SERVER.enableCameraRecoil.get())
            return;

        if(event.phase != TickEvent.Phase.END || this.cameraRecoil <= 0)
            return;

        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        if(!(mc.player.getHeldItemMainhand().getItem() instanceof TimelessGunItem))
            return;

        TimelessGunItem gunItem = ((TimelessGunItem) mc.player.getHeldItemMainhand().getItem());

        float cameraRecoilModifer = gunItem.getGun().getGeneral().getCameraRecoilModifier()*3;
        float recoilAmount = this.cameraRecoil * mc.getTickLength() * 0.225F;//0.0885F;//0.25F;//0
        // .1F;
        float HorizontalRecoilAmount = this.horizontalCameraRecoil * mc.getTickLength() * 0.225F;
        // 0.0885F;
        //0.25F;//* 0.1F;
        float startProgress =
                (this.progressCameraRecoil / (this.cameraRecoil*gunItem.getGun().getGeneral().getCameraRecoilDuration()));
        float endProgress =
                ((this.progressCameraRecoil + (recoilAmount*2.575f)) / (this.cameraRecoil/gunItem.getGun().getGeneral().getCameraRecoilDuration()));
        // 3f
        // 2.575f
        // LOOKS AMAZING!!!

        float progressForward = mc.player.getHeldItemMainhand().getItem() instanceof TimelessGunItem ?
                gunItem.getGun().getGeneral().getRecoilDuration() : 0.25F;//0.25F;//startProgress < 0.25F && startProgress > 0.125F ? 0.125F : proggress; // 0.25

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
            mc.player.rotationPitch += ((endProgress - startProgress) / (1-progressForward) ) * this.cameraRecoil / (cameraRecoilModifer); // 0.75F
            if(recoilRand == 1)
                mc.player.rotationYaw -= ((endProgress - startProgress) / (1-progressForward)) * -this.horizontalCameraRecoil / (cameraRecoilModifer);
            else
                mc.player.rotationYaw -= ((endProgress - startProgress) / (1-progressForward)) * this.horizontalCameraRecoil / (cameraRecoilModifer);
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
        CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
        float cooldown ;
        if((tracker.getCooldown(gunItem, Minecraft.getInstance().getRenderPartialTicks()))<0.5f)
            cooldown = 0;/*(tracker.getCooldown(gunItem,
                    Minecraft.getInstance().getRenderPartialTicks()));*/
        else
            cooldown = (tracker.getCooldown(gunItem,
                    Minecraft.getInstance().getRenderPartialTicks())-0.5f)*2f;

        if(cooldown >= modifiedGun.getGeneral().getWeaponRecoilOffset())// || tooFast) // Actually have any visual recoil at Rate 1???
        {
            //float amount = 1.0F * ((1.0F - cooldown) / 0.2F);
            float amount = 1F * ((1.0F - cooldown) / (1-modifiedGun.getGeneral().getWeaponRecoilOffset()));
            this.gunRecoilNormal = 1 - (--amount) * amount * amount * amount;
        }
        else
        {
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

    public float getGunRecoilRandom()
    {
        return this.gunRecoilRandom;
    }

}