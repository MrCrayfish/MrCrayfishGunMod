package com.tac.guns.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.Config;
import com.tac.guns.client.BulletTrail;
import com.tac.guns.client.GunRenderType;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.util.OptifineHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class BulletTrailRenderingHandler
{
    private static BulletTrailRenderingHandler instance;

    public static BulletTrailRenderingHandler get()
    {
        if(instance == null)
        {
            instance = new BulletTrailRenderingHandler();
        }
        return instance;
    }

    private Map<Integer, BulletTrail> bullets = new HashMap<>();

    private BulletTrailRenderingHandler() {}

    /**
     * Adds a bullet trail to render into the world
     *
     * @param trail the bullet trail get
     */
    public void add(BulletTrail trail)
    {
        //if(Config.CLIENT.particle.)

        // Prevents trails being added when not in a world
        World world = Minecraft.getInstance().world;
        if(world != null)
        {
            this.bullets.put(trail.getEntityId(), trail);
        }
    }

    /**
     * Removes the bullet for the given entity id.
     *
     * @param entityId the entity id of the bullet
     */
    public void remove(int entityId)
    {
        this.bullets.remove(entityId);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        World world = Minecraft.getInstance().world;
        if(world != null)
        {
            if(event.phase == TickEvent.Phase.END)
            {
                this.bullets.values().forEach(BulletTrail::tick);
                this.bullets.values().removeIf(BulletTrail::isDead);
            }
        }
        else if(!this.bullets.isEmpty())
        {
            this.bullets.clear();
        }
    }

    public void render(MatrixStack stack, float partialSticks)
    {
        for(BulletTrail bulletTrail : this.bullets.values())
        {
            this.renderBulletTrail(bulletTrail, stack, partialSticks);
        }
    }

    @SubscribeEvent
    public void onRespawn(ClientPlayerNetworkEvent.RespawnEvent event)
    {
        this.bullets.clear();
    }

    @SubscribeEvent
    public void onLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        this.bullets.clear();
    }

    /**
     * @param bulletTrail
     * @param matrixStack
     * @param partialTicks
     */
    private void renderBulletTrail(BulletTrail bulletTrail, MatrixStack matrixStack, float partialTicks)
    {
        if(OptifineHelper.isShadersEnabled())
            return;

        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.getRenderViewEntity();
        if(entity == null || bulletTrail.isDead())
            return;
        /*if(!AimingHandler.get().isAiming() && bulletTrail.getAge() < 1)
            return;*/
        matrixStack.push();

        Vector3d view = mc.gameRenderer.getActiveRenderInfo().getProjectedView();
        Vector3d position = bulletTrail.getPosition();
        Vector3d motion = bulletTrail.getMotion();
        double bulletX = position.x + motion.x * partialTicks;
        double bulletY = position.y + motion.y * partialTicks;
        double bulletZ = position.z + motion.z * partialTicks;
        //TODO: Use muzzle flash location of entity render as the render position for muzzle flash start

        if(ShootingHandler.get().isShooting() && Minecraft.getInstance().player.isEntityEqual(entity))
        {
            //Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent(Minecraft.getInstance().player.rotationYaw+"") ,true);
            //matrixStack.translate(bulletX - view.getX(), bulletY - view.getY()-0.165f, bulletZ - view.getZ());
            matrixStack.translate(bulletX - view.getX(), bulletY - view.getY() -0.175f, bulletZ - view.getZ());
            if(!AimingHandler.get().isAiming()) {
                matrixStack.rotate(Vector3f.YP.rotationDegrees(0.05f));
                //matrixStack.rotate(Vector3f.ZN.rotationDegrees(0.0775f));
            }
        }
        else
            matrixStack.translate(bulletX - view.getX(), bulletY - view.getY()-0.205f, bulletZ - view.getZ());
        //matrixStack.translate(0, -0.5, 0);
        //matrixStack.translate(bulletX - view.getX()-0.15f, bulletY - view.getY()-0.3f, bulletZ - view.getZ());

       matrixStack.rotate(Vector3f.YP.rotationDegrees(bulletTrail.getYaw()));
       //matrixStack.rotate(Vector3f.XP.rotationDegrees(-bulletTrail.getPitch() + 90.125f));
       matrixStack.rotate(Vector3f.XP.rotationDegrees(-bulletTrail.getPitch() + 90.125f));

        Vector3d motionVec = new Vector3d(motion.x, motion.y, motion.z);
        float length = (float) motionVec.length();
        if(bulletTrail.getAge() < 1)
            length*=0.965f;
        float trailLength = (float) ((length / 3.0F) * bulletTrail.getTrailLengthMultiplier());
        float red = (float) (bulletTrail.getTrailColor() >> 16 & 255) / 255.0F;
        float green = (float) (bulletTrail.getTrailColor() >> 8 & 255) / 255.0F;
        float blue = (float) (bulletTrail.getTrailColor() & 255) / 255.0F;
        float alpha = 0.275F;

        // Prevents the trail length from being longer than the distance to shooter
        Entity shooter = bulletTrail.getShooter();
        if(shooter != null)
        {
            trailLength = (float) Math.min(trailLength+0.125, shooter.getEyePosition(partialTicks).distanceTo(new Vector3d(bulletX,bulletY, bulletZ)));
        }

        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        IRenderTypeBuffer.Impl renderTypeBuffer = mc.getRenderTypeBuffers().getBufferSource();

        /*if(bulletTrail.isTrailVisible())
        {*/
            RenderType bulletType = GunRenderType.getBulletTrail();
            IVertexBuilder builder = renderTypeBuffer.getBuffer(bulletType);
            builder.pos(matrix4f, 0, 0, -0.2F).color(red, green, blue, alpha).lightmap(15728880).endVertex();
            builder.pos(matrix4f, 0, 0, 0.2F).color(red, green, blue, alpha).lightmap(15728880).endVertex();
            builder.pos(matrix4f, 0,  -trailLength,0).color(red, green, blue, alpha).lightmap(15728880).endVertex();
            builder.pos(matrix4f, 0,  trailLength,0).color(red, green, blue, alpha).lightmap(15728880).endVertex();
            builder.pos(matrix4f, -0.2F, 0, 0).color(red, green, blue, alpha).lightmap(15728880).endVertex();
            builder.pos(matrix4f, 0.2F, 0, 0).color(red, green, blue, alpha).lightmap(15728880).endVertex();
            builder.pos(matrix4f, 0, -trailLength, 0).color(red, green, blue, alpha).lightmap(15728880).endVertex();
            builder.pos(matrix4f, 0, trailLength, 0).color(red, green, blue, alpha).lightmap(15728880).endVertex();
            Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish(bulletType);

            /*RenderType bulletType = GunRenderType.getBulletTrail();
            IVertexBuilder buffer1 = renderTypeBuffer.getBuffer(bulletType);
            buffer1.pos(matrix4f, 0, -0.05F, 0).color(red, green, blue, alpha).endVertex();
            buffer1.pos(matrix4f, 0, 0.05F, 0).color(red, green, blue, alpha).endVertex();
            buffer1.pos(matrix4f, 0, 0.05F, -trailLength).color(red, green, blue, alpha).endVertex();
            buffer1.pos(matrix4f, 0, -0.05F, -trailLength).color(red, green, blue, alpha).endVertex();
            buffer1.pos(matrix4f, -0.05F, 0, 0).color(red, green, blue, alpha).endVertex();
            buffer1.pos(matrix4f, 0.05F, 0, 0).color(red, green, blue, alpha).endVertex();
            buffer1.pos(matrix4f, 0.05F, 0, -trailLength).color(red, green, blue, alpha).endVertex();
            buffer1.pos(matrix4f, -0.05F, 0, -trailLength).color(red, green, blue, alpha).endVertex();
            Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish(bulletType);*/

        //}

        if(!bulletTrail.getItem().isEmpty() && !bulletTrail.isTrailVisible())
        {
            matrixStack.rotate(Vector3f.YP.rotationDegrees((bulletTrail.getAge() + partialTicks) * (float) 50));
            matrixStack.scale(0.25F, 0.25F, 0.25F);

            int combinedLight = WorldRenderer.getCombinedLight(entity.world, new BlockPos(entity.getPositionVec()));
            ItemStack stack = bulletTrail.getItem();
            RenderUtil.renderModel(stack, ItemCameraTransforms.TransformType.NONE, matrixStack, renderTypeBuffer, combinedLight, OverlayTexture.NO_OVERLAY, null, null);
        }

        matrixStack.pop();
    }
}
