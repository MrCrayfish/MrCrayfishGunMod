package com.tac.guns.client.handler;

import com.tac.guns.Reference;
import com.tac.guns.client.GunRenderType;
import com.tac.guns.client.render.armor.CloakPilot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public enum ArmorRenderingHandler {
    INSTANCE;
    private static final CloakPilot model = new CloakPilot();
    private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/armor/cloak_pilot.png");
    private static final RenderType type = RenderType.makeType(Reference.MOD_ID + ":armor", DefaultVertexFormats.ENTITY, GL_QUADS, 1024, true, false,
            RenderType.State.getBuilder().texture(new RenderState.TextureState(texture, false, false)).build(true));
    //@SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Pre event){
        //Minecraft.getInstance().player.sendChatMessage("test");

        event.getMatrixStack().push();
        event.getMatrixStack().rotate(Vector3f.XN.rotationDegrees(180));
        //event.getMatrixStack().rotate(ev);
        model.render(event.getMatrixStack(),event.getBuffers().getBuffer(type),event.getLight(),event.getLight(),1f,1f,1f,1f);
        event.getMatrixStack().pop();
    }
}
