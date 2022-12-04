package com.tac.guns.client.handler;

import com.mojang.blaze3d.platform.GlStateManager;
import com.tac.guns.Reference;
import com.tac.guns.client.render.armor.models.LightModernArmor;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public enum ArmorRenderingHandler {
    INSTANCE;
    private static final LightModernArmor model = new LightModernArmor();
    private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/armor/light_armor_1.png");
    private static final RenderType type = RenderType.makeType(Reference.MOD_ID + ":armor", DefaultVertexFormats.ENTITY, GL_QUADS, 1024, true, false, RenderType.State.getBuilder().texture(new RenderState.TextureState(texture, false, false)).build(true));
    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Pre event){

        // Separate this into separate renders, I want to be able to dynamically call
        // PlayerWearableUtil.getArmor.renderModel();
        GlStateManager.enableLighting();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableTexture();
        int blockLight = event.getEntityLiving().isBurning() ? 15 : event.getEntity().world.getLightFor(LightType.BLOCK, new BlockPos(event.getEntity().getEyePosition(event.getPartialRenderTick())));
        int packedLight = LightTexture.packLight(blockLight, event.getEntity().world.getLightFor(LightType.SKY, new BlockPos(event.getEntity().getEyePosition(event.getPartialRenderTick()))));
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
        model.render(event.getMatrixStack(),event.getBuffers().getBuffer(type), -200, -1,0f,0f,0f,0f);
        event.getMatrixStack().pop();

        // Test Head
        // Test Arms
        // Test Legs
    }
}
