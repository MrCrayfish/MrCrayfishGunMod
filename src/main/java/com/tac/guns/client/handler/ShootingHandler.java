package com.tac.guns.client.handler;

import com.tac.guns.common.SpreadTracker;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.EventPriority;
import org.lwjgl.glfw.GLFW;

import com.tac.guns.Config;
import com.tac.guns.client.InputHandler;
import com.tac.guns.common.Gun;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageEmptyMag;
import com.tac.guns.network.message.MessageShoot;
import com.tac.guns.network.message.MessageShooting;
import com.tac.guns.network.message.MessageUpdateMoveInacc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class  ShootingHandler
{
    private static ShootingHandler instance;

    public static ShootingHandler get()
    {
        if(instance == null)
        {
            instance = new ShootingHandler();
        }
        return instance;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
    public void setShootingError(boolean shootErr) {
        this.shootErr = shootErr;
    }

    private boolean shooting;

    private boolean shootErr;
    private boolean clickUp = false;
    private int burstTracker = 0;
    private int burstCooldown = 0;
    private ShootingHandler() {}

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        if(!isInGame())
            return;
        this.burstTracker = 0;
        this.burstCooldown = 0;
        this.clickUp = false;
        this.shootErr = false;
    }

    private boolean isInGame()
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.loadingGui != null)
            return false;
        if(mc.currentScreen != null)
            return false;
        if(!mc.mouseHelper.isMouseGrabbed())
            return false;
        return mc.isGameFocused();
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.RawMouseEvent event)
    {
        if(!this.isInGame())
            return;

        if(event.getAction() != GLFW.GLFW_PRESS)
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player == null)
            return;

        ItemStack heldItem = player.getHeldItemMainhand();
        if(heldItem.getItem() instanceof GunItem)
        {
            int button = event.getButton();
            if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT || (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && AimingHandler.get().isLookingAtInteractableBlock()))
            {
                event.setCanceled(true);
            }
            if( InputHandler.PULL_TRIGGER.down )
            {
                if(heldItem.getItem() instanceof TimelessGunItem && heldItem.getTag().getInt("CurrentFireMode") == 3 && this.burstCooldown == 0)
                {

                    this.burstTracker = ((TimelessGunItem)heldItem.getItem()).getGun().getGeneral().getBurstCount();
                    fire(player, heldItem);
                    this.burstCooldown = ((TimelessGunItem)heldItem.getItem()).getGun().getGeneral().getBurstRate();
                }
                else if(this.burstCooldown == 0)
                    fire(player, heldItem);

                if(!(heldItem.getTag().getInt("AmmoCount") > 0)) {
                    player.sendStatusMessage(new TranslationTextComponent("info.tac.out_of_ammo").mergeStyle(TextFormatting.UNDERLINE).mergeStyle(TextFormatting.BOLD).mergeStyle(TextFormatting.RED), true);
                    PacketHandler.getPlayChannel().sendToServer(new MessageEmptyMag());
                }
            }
        }
    }
    
    // CHECK HERE: Indicates the ticks left for next shot
    private static float shootTickGapLeft = 0F;

    public float getShootTickGapLeft(){
        return shootTickGapLeft;
    }

    public static float shootMsGap = 0F;
    public float getshootMsGap(){
        return shootMsGap;
    }
    public static float calcShootTickGap(int rpm)
    {
        float shootTickGap = 60F / rpm * 20F;
        return shootTickGap;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderTick(TickEvent.RenderTickEvent evt)
    {
        if(shootMsGap > 0F)
            shootMsGap -= evt.renderTickTime;
    }

    @SubscribeEvent
    public void onHandleShooting( TickEvent.ClientTickEvent evt )
    {
        if(evt.phase != TickEvent.Phase.START)
            return;

        if(!this.isInGame())
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if( player != null )
        {
        	// CHECK HERE: Reduce by 1F in each tick until it is less than 0F
        	shootTickGapLeft -= shootTickGapLeft > 0F ? 1F : 0F;
        	
            ItemStack heldItem = player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem && (Gun.hasAmmo(heldItem) || player.isCreative()))
            {
                final float dist = Math.abs( player.moveForward ) / 2.5F
                	+ Math.abs( player.moveStrafing ) / 1.25F
                    + ( player.getMotion().y > 0D ? 0.5F : 0F );
                PacketHandler.getPlayChannel().sendToServer( new MessageUpdateMoveInacc( dist ) );
                
                // Update #shooting state if it has changed
                final boolean shooting = InputHandler.PULL_TRIGGER.down && GunRenderingHandler.get().sprintTransition == 0;
                // TODO: check if this is needed
//              if(GunMod.controllableLoaded)
//              {
//                  shooting |= ControllerHandler.isShooting();
//              }
                if(shooting ^ this.shooting )
                {
                	this.shooting = shooting;
                	PacketHandler.getPlayChannel().sendToServer( new MessageShooting( shooting ) );
                }
            }
            else if(this.shooting)
            {
                this.shooting = false;
                PacketHandler.getPlayChannel().sendToServer(new MessageShooting(false));
            }
        }
        else
        {
            this.shooting = false;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (!isInGame())
            return;
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player != null)
            if(this.burstCooldown > 0)
                this.burstCooldown -= 1;
    }
    
    @SubscribeEvent
    public void onPostClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;
        if(!isInGame())
            return;
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
//            player.sendStatusMessage(new TranslationTextComponent(this.burstCooldown+"| | |"+this.burstTracker+"| | |"+heldItem.getTag().getInt("CurrentFireMode")), true);
            if(heldItem.getItem() instanceof TimelessGunItem)
            {
                TimelessGunItem gunItem = (TimelessGunItem) heldItem.getItem();
                if(heldItem.getTag().getInt("CurrentFireMode") == 3 && Config.CLIENT.controls.burstPress.get())
                {

                    if(this.burstTracker > 0)
                        fire(player, heldItem);
                    return;
                }
                else if( InputHandler.PULL_TRIGGER.down )
                {
                    Gun gun = ((TimelessGunItem) heldItem.getItem()).getModifiedGun(heldItem);
                    if (gun.getGeneral().isAuto() && heldItem.getTag().getInt("CurrentFireMode") == 2) {
                        fire(player, heldItem);
                        return;
                    }
                    if (heldItem.getTag().getInt("CurrentFireMode") == 3 && !Config.CLIENT.controls.burstPress.get() && !this.clickUp && this.burstCooldown == 0) {
                        if (this.burstTracker < gun.getGeneral().getBurstCount()) {
                            if (ShootingHandler.get().getshootMsGap() <= 0) {
                                fire(player, heldItem);
                                this.burstTracker++;
                            }
                        } else if (heldItem.getTag().getInt("AmmoCount") > 0 && this.burstTracker > 0) {
                            if (!tracker.hasCooldown(heldItem.getItem())) {
                                this.burstTracker = 0;
                                this.clickUp = true;
                                this.burstCooldown = gun.getGeneral().getBurstRate();
                            }
                        }
                        return;
                    }
                }
                else if(this.clickUp || InputHandler.PULL_TRIGGER.down )
                {
                    if(heldItem.getTag().getInt("CurrentFireMode") == 3 && this.burstTracker > 0) {
                        this.burstCooldown = gunItem.getGun().getGeneral().getBurstRate();
                    }
                    this.burstTracker = 0;
                    this.clickUp = false;
                }
                // CONTINUER FOR BURST. USING BURST TRACKER AS A REVERSE ITOR WHEN BURST IS ON PRESS MODE.
            }
        }
    }

    public void fire(PlayerEntity player, ItemStack heldItem)
    {
        if(!(heldItem.getItem() instanceof GunItem))
            return;

        if(!Gun.hasAmmo(heldItem) && !player.isCreative())
            return;
        
        if(player.isSpectator())
            return;

        player.setSprinting(false);
        if(GunRenderingHandler.get().sprintTransition != 0) {
            this.shooting = false;
            return;
        }

        // CHECK HERE: Restrict the fire rate
//      if(!tracker.hasCooldown(heldItem.getItem()))
		if( shootTickGapLeft <= 0F )
        {
            GunItem gunItem = (GunItem) heldItem.getItem();
            Gun modifiedGun = gunItem.getModifiedGun(heldItem);

            if(MinecraftForge.EVENT_BUS.post(new GunFireEvent.Pre(player, heldItem)))
                return;
            
            // CHECK HERE: Change this to test different rpm settings.
            // TODO: Test serverside, possible issues 0.3.4-alpha
            final float rpm = modifiedGun.getGeneral().getRate(); // Rounds per sec. Should come from gun properties in the end.
            shootTickGapLeft += calcShootTickGap((int) rpm);
            shootMsGap += calcShootTickGap((int) rpm);
            RecoilHandler.get().lastRandPitch = RecoilHandler.get().lastRandPitch;
            RecoilHandler.get().lastRandYaw = RecoilHandler.get().lastRandYaw;
            PacketHandler.getPlayChannel().sendToServer(new MessageShoot(player.getYaw(1), player.getPitch(1), RecoilHandler.get().lastRandPitch, RecoilHandler.get().lastRandYaw));

            if(Config.CLIENT.controls.burstPress.get()) this.burstTracker--;
            else this.burstTracker++;
            MinecraftForge.EVENT_BUS.post(new GunFireEvent.Post(player, heldItem));
        }
    }
}
