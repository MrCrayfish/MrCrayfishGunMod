package com.tac.guns.client.handler;

import com.tac.guns.Config;
import com.tac.guns.GunMod;
import com.tac.guns.common.Gun;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.item.GunItem;
import com.tac.guns.util.GunModifierHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Level;

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
        recoilModifier *= verticalRandomAmount;
        this.cameraRecoil = modifiedGun.getGeneral().getRecoilAngle() * recoilModifier;
        this.progressCameraRecoil = 0F;

        // Horizontal Recoil
        this.gunRecoilRandom = random.nextFloat();

        float horizontalRandomAmount = this.random.nextFloat()*(1.22f - 0.75f) + 0.75f;

        float horizontalRecoilModifier = 1.0F - GunModifierHelper.getHorizontalRecoilModifier(heldItem);
        horizontalRecoilModifier *= this.getAdsRecoilReduction(modifiedGun);
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

        float cameraRecoilModifer = mc.player.getHeldItemMainhand().getItem() instanceof GunItem ? ((GunItem) mc.player.getHeldItemMainhand().getItem()).getGun().getGeneral().getCameraRecoilModifier() : 1.0F;

        float recoilAmount = this.cameraRecoil * mc.getTickLength() * 0.1F;//0.25F;//0.1F;
        float HorizontalRecoilAmount = this.horizontalCameraRecoil * mc.getTickLength() * 0.1F;//0.25F;//* 0.1F;
        float startProgress = (this.progressCameraRecoil / this.cameraRecoil);
        float endProgress = ((this.progressCameraRecoil + recoilAmount) / this.cameraRecoil);

        //float cameraRecoilDuration = mc.player.getHeldItemMainhand().getItem() instanceof GunItem ? ((GunItem) mc.player.getHeldItemMainhand().getItem()).getGun().getGeneral().getCameraRecoilModifier() : 1.0F;

        //float proggress = 0.25F; // 0.25
        float progressForward = mc.player.getHeldItemMainhand().getItem() instanceof GunItem ? ((GunItem) mc.player.getHeldItemMainhand().getItem()).getGun().getGeneral().getRecoilDuration() : 0.25F;//0.25F;//startProgress < 0.25F && startProgress > 0.125F ? 0.125F : proggress; // 0.25

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
            mc.player.rotationPitch += ((endProgress - startProgress) / (1-progressForward) ) * this.cameraRecoil / (cameraRecoilModifer*3); // 0.75F
            if(recoilRand == 1)
                mc.player.rotationYaw -= ((endProgress - startProgress) / (1-progressForward)) * -this.horizontalCameraRecoil / (cameraRecoilModifer*3);
            else
                mc.player.rotationYaw -= ((endProgress - startProgress) / (1-progressForward)) * this.horizontalCameraRecoil / (cameraRecoilModifer*3);
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
        float cooldown = tracker.getCooldown(gunItem, Minecraft.getInstance().getRenderPartialTicks());
        //cooldown = cooldown >= modifiedGun.getGeneral().getRecoilDurationOffset() ? (cooldown - modifiedGun.getGeneral().getRecoilDurationOffset()) / (1.0F - modifiedGun.getGeneral().getRecoilDurationOffset()) : 0.0F;

        //float durationRandom = this.random.nextFloat()*(1.22f - 0.75f) + 0.75f;  * durationRandom
        //float weaponRecoilDuration = modifiedGun.getGeneral().getWeaponRecoilDuration() >= 0.0F && modifiedGun.getGeneral().getWeaponRecoilDuration() <= 1.0F ? modifiedGun.getGeneral().getWeaponRecoilDuration(): 0.5F;

        //boolean tooFast = modifiedGun.getGeneral().getRate() < 2;

        if(cooldown >= modifiedGun.getGeneral().getWeaponRecoilDuration())// || tooFast) // Actually have any visual recoil at Rate 1???
        {
            //float amount = 1.0F * ((1.0F - cooldown) / 0.2F);
            float amount = 1F * ((1.0F - cooldown) / (1-modifiedGun.getGeneral().getWeaponRecoilDuration()));
            this.gunRecoilNormal = 1 - (--amount) * amount * amount * amount;
        }
        else
        {
            /*if(modifiedGun.getGeneral().getRate() < 15)
            {
                //float amount = (cooldown / 0.8F);
                float amount = (cooldown / modifiedGun.getGeneral().getWeaponRecoilDuration());
                //this.gunRecoilNormal = amount < 0.5 ? 2 * amount * amount : -1 + (4 - 2 * amount) * amount;
                this.gunRecoilNormal = amount < 0.5 ? 2 * amount * amount : -1 + (4 - 2 * amount) * amount;
                //this.gunRecoilNormal*=1.2F;
            }
            else
            {*/
                float amount = ( (cooldown) / modifiedGun.getGeneral().getWeaponRecoilDuration() );
                this.gunRecoilNormal = amount < 0.5 ? 2 * amount * amount : -1 + (4 - 2 * amount) * amount;
            //}
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
