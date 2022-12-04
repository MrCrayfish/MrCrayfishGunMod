package com.tac.guns.client.handler;

import com.tac.guns.Reference;
import com.tac.guns.client.GunRenderType;
import com.tac.guns.client.handler.command.ObjectRenderEditor;
import com.tac.guns.client.render.armor.CloakPilot;
import com.tac.guns.client.render.armor.LightModernArmor;
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
    private static final LightModernArmor model = new LightModernArmor();
    private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/armor/light_armor_1.png");
    private static final RenderType type = RenderType.makeType(Reference.MOD_ID + ":muzzle_flash", DefaultVertexFormats.ENTITY, GL_QUADS, 1024, true, false, RenderType.State.getBuilder().texture(new RenderState.TextureState(texture, false, false)).build(true));
    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Post event){

        // Separate this into separate renders, I want to be able to dynamically call
        // PlayerWearableUtil.getArmor.renderModel();

        // Test ChestPlate
        event.getMatrixStack().push();
        event.getMatrixStack().rotate(Vector3f.XN.rotationDegrees(180));
        event.getMatrixStack().rotate(Vector3f.YP.rotationDegrees(event.getEntityLiving().renderYawOffset));

/*        ObjectRenderEditor.RENDER_Element element = new ObjectRenderEditor.RENDER_Element(0, 0, 0, 0);
        if(ObjectRenderEditor.get().GetFromElements(9) != null) {
            element = ObjectRenderEditor.get().GetFromElements(9);
            event.getMatrixStack().translate(element.getxMod(), -1.325, element.getzMod());
        }*/
        event.getMatrixStack().translate(0, -1.4, 0);
        model.render(event.getMatrixStack(),event.getBuffers().getBuffer(type),event.getLight(),event.getLight(),1f,1f,1f,1f);
        event.getMatrixStack().pop();

        // Test Head
        // Test Arms
        // Test Legs
    }
}
