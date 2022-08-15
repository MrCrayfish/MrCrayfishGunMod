package com.tac.guns.client.handler;

import com.tac.guns.Reference;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class CameraAnimationHandler {

    @SubscribeEvent
    public void onRenderOverlay(RenderHandEvent event)
    {
        /*
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        player.chasingPosX
        if(player == null) return;
        double dropSpeed = Math.min( 0D, player.getPosY()-player.prevPosY );
        // Apply drop impact on camera if upon hitting the ground
        if( dropSpeed == 0D && mod.playerAcceleration.y > 0D )
        {
            final Vec3 easingV = this.camEasing.velocity;

//			boolean positive = FMUM.rand.nextBoolean();
            // Make sure the drop impact always makes the head tilt hard on its original direction
            boolean positive = easingV.z > 0D;
            if( positive ^ this.camEasing.curPos.z > 0D )
            {
                easingV.z = -easingV.z;
                positive = !positive;
            }

            final double impact = mod.playerAcceleration.y * mod.camDropImpact;
            easingV.z += positive ? impact : -impact;
        }
         */
    }

}
