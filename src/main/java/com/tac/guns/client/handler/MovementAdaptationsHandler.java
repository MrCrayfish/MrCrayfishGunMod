package com.tac.guns.client.handler;

import com.tac.guns.GunMod;
import com.tac.guns.client.network.ClientPlayHandler;
import com.tac.guns.common.Gun;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageReload;
import com.tac.guns.network.message.MessageUpdatePlayerMovement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.Level;

public class MovementAdaptationsHandler
{
    private static MovementAdaptationsHandler instance;

    public boolean isReadyToUpdate() {
        return readyToUpdate;
    }

    public void setReadyToUpdate(boolean readyToUpdate) {
        this.readyToUpdate = readyToUpdate;
    }

    public boolean isReadyToReset() {
        return readyToReset;
    }

    public void setReadyToReset(boolean readyToReset) {
        this.readyToReset = readyToReset;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getPreviousWeight() {
        return previousWeight;
    }

    public void setPreviousWeight(float previousWeight) {
        this.previousWeight = previousWeight;
    }

    private boolean readyToUpdate = false;
    private boolean readyToReset = true;

    private float speed = 0.0F;
    private float previousWeight = 0.0F;

    //private Byte previousGun;

    public static MovementAdaptationsHandler get() {
        return instance == null ? instance = new MovementAdaptationsHandler() : instance;
    }
    private MovementAdaptationsHandler() { }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onFovUpdate(FOVUpdateEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() && mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON && mc.gameSettings.fovScaleEffect > 0)
        {
            ItemStack heldItem = mc.player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof TimelessGunItem)
            {
                if(event.getEntity().isSprinting())
                    event.setNewfov(1.125f);
                else
                    event.setNewfov(1f);
            }
        }
    }
    //private float revisionFov(float speed)
    //{
    //    return speed > 0.1 ? speed : 1f;
    //}

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onJump(LivingEvent.LivingJumpEvent event)
    {
        if (!(event.getEntityLiving().getHeldItemMainhand().getItem() instanceof TimelessGunItem))
            return;
        if(speed < 0.077f)
            event.getEntityLiving().setMotion(event.getEntityLiving().getMotion().getX()/2.5,event.getEntityLiving().getMotion().getY()/1.125,event.getEntityLiving().getMotion().getZ()/2.5);
        else if(speed < 0.9f)
            event.getEntityLiving().setMotion(event.getEntityLiving().getMotion().getX()/1.75,event.getEntityLiving().getMotion().getY(),event.getEntityLiving().getMotion().getZ()/1.75);
    }

    @SubscribeEvent//(priority = EventPriority.HIGH)
    public void movementUpdate(TickEvent.ClientTickEvent event)
    {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        PacketHandler.getPlayChannel().sendToServer(new MessageUpdatePlayerMovement());
    }
}