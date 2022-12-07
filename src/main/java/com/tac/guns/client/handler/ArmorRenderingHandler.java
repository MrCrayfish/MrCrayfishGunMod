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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LightType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glViewport;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public enum ArmorRenderingHandler {
    INSTANCE;
    private static final LightModernArmor model = new LightModernArmor();
    private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/armor/light_armor_1.png");
    private static final RenderType type = RenderType.makeType(Reference.MOD_ID + ":armor", DefaultVertexFormats.ENTITY, GL_QUADS, 256, false, false,
            RenderType.State.getBuilder().texture(new RenderState.TextureState(texture, false, false)).build(true));


    // Render the armor model with the texture and proper lighting
    /*public void renderArmorModel(RenderPlayerEvent.Pre event, float partialTicks) {
        // Get the player's position
        BlockPos pos = new BlockPos(event.getPlayer().getPosX(), event.getPlayer().getPosY() + event.getPlayer().getEyeHeight(), event.getPlayer().getPosZ());

        // Get the light level at the player's position
        int light = event.getPlayer().world.getLightFor(LightType.BLOCK, pos);

        // Get the sky light level at the player's position
        int skyLight = event.getPlayer().world.getLightFor(LightType.SKY, pos);

        // Calculate the light map coordinates
        int lightMapX = light % 65536;
        int lightMapY = skyLight % 65536;

        // Set the light map coordinates
        LightTexture.packLightmapCoords(lightMapX, lightMapY);

        // Push the matrix
        GlStateManager.pushMatrix();

        // Translate the matrix to the player's position
        GlStateManager.translated(event.getX(), event.getY(), event.getZ());

        // Rotate the matrix to the player's rotation
        GlStateManager.rotatef(event.getPlayer().prevRenderYawOffset + (event.getPlayer().renderYawOffset - event.getPlayer().prevRenderYawOffset) * partialTicks, 0, -1, 0);

        // Rotate the matrix to the player's head rotation
        GlStateManager.rotatef(event.getPlayer().prevRotationYawHead + (event.getPlayer().rotationYawHead - event.getPlayer().prevRotationYawHead) * partialTicks, 0, -1, 0);

        // Translate the matrix to the correct position
        GlStateManager.translated(0, -0.5, 0);

        // Rotate the matrix to the correct rotation
        GlStateManager.rotatef(180, 0, 0, 1);

        // Enable lighting
        RenderHelper.enableStandardItemLighting();

        // Bind the texture
        event.getRenderer().bindTexture(texture);

        // Render the model
        model.render(event.getPlayer(), 0, 0, 0, 0, 0, 0.0625f);

        // Pop the matrix
        GlStateManager.popMatrix();
    }
*/

    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Post event){

        // Separate this into separate renders, I want to be able to dynamically call
        // PlayerWearableUtil.getArmor.renderModel();
        /*int blockLight = event.getEntityLiving().isBurning() ? 15 : event.getEntity().world.getLightFor(LightType.BLOCK, new BlockPos(event.getEntity().getPosition()));
        int packedLight = LightTexture.packLight(blockLight, event.getEntity().world.getLightFor(LightType.SKY, new BlockPos(event.getEntity().getPosition())));*/

        int worldLight = (int)(event.getLight());

        // Test ChestPlate
        event.getMatrixStack().push();
        event.getMatrixStack().rotate(Vector3f.XN.rotationDegrees(180));
        event.getMatrixStack().rotate(Vector3f.YP.rotationDegrees(event.getEntityLiving().renderYawOffset));
/*        ObjectRenderEditor.RENDER_Element element = new ObjectRenderEditor.RENDER_Element(0, 0, 0, 0);
        if(ObjectRenderEditor.get().GetFromElements(9) != null) {
            element = ObjectRenderEditor.get().GetFromElements(9);
            event.getMatrixStack().translate(element.getxMod(), -1.325, element.getzMod());
        }*/
        event.getPlayer().sendStatusMessage(new TranslationTextComponent(""+worldLight), true);
        event.getMatrixStack().translate(0, -1.4, 0);
        GlStateManager.enableLighting();
        int tensMillions = worldLight/15000000;

        // Dividing everything by 5 looks like a good minimum brightness for the render, why do I have to do this at all
        model.render(event.getMatrixStack(),event.getBuffers().getBuffer(type), worldLight/tensMillions, Integer.MIN_VALUE,1f/tensMillions,1f/tensMillions,1f/tensMillions,1f);//1f*(worldLight / (15728640*2)),1f*(worldLight / (15728640*2)),1f*
        // (worldLight / (15728640*2)),1f);
        event.getMatrixStack().pop();

        // Test Head
        // Test Arms
        // Test Legs
    }
}
