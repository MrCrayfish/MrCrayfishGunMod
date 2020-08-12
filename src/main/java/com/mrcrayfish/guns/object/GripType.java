package com.mrcrayfish.guns.object;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.client.render.HeldAnimation;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class GripType
{
    /**
     * A grip type designed for weapons that are held with only one hand, like a pistol
     */
    public static final GripType ONE_HANDED = new GripType(new ResourceLocation(Reference.MOD_ID, "one_handed"), new HeldAnimation()
    {
        @Override
        @OnlyIn(Dist.CLIENT)
        public void applyPlayerModelRotation(PlayerEntity player, PlayerModel model, Hand hand, float aimProgress)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            ModelRenderer arm = right ? model.bipedRightArm : model.bipedLeftArm;
            copyModelAngles(model.bipedHead, arm);
            arm.rotateAngleX += Math.toRadians(-70F);
        }

        @Override
        public void renderFirstPersonArms(ClientPlayerEntity player, HandSide hand, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, float partialTicks)
        {
            matrixStack.translate(0, 0, -1);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));

            double centerOffset = 2.5;
            if(Minecraft.getInstance().player.getSkinType().equals("slim"))
            {
                centerOffset += hand == HandSide.RIGHT ? 0.2 : 0.8;
            }
            centerOffset = hand == HandSide.RIGHT ? -centerOffset : centerOffset;
            matrixStack.translate(centerOffset * 0.0625, -0.45, -1.0);

            matrixStack.rotate(Vector3f.XP.rotationDegrees(75F));
            matrixStack.scale(0.5F, 0.5F, 0.5F);

            RenderUtil.renderFirstPersonArm(player, hand, matrixStack, buffer, light);
        }
    }, true);

    /**
     * A grip type designed for weapons that are held with two hands, like an assault rifle
     */
    public static final GripType TWO_HANDED = new GripType(new ResourceLocation(Reference.MOD_ID, "two_handed"), new HeldAnimation()
    {
        @Override
        @OnlyIn(Dist.CLIENT)
        public void applyPlayerModelRotation(PlayerEntity player, PlayerModel model, Hand hand, float aimProgress)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            ModelRenderer mainArm = right ? model.bipedRightArm : model.bipedLeftArm;
            ModelRenderer secondaryArm = right ? model.bipedLeftArm : model.bipedRightArm;

            if(Minecraft.getInstance().getRenderViewEntity() == player && Minecraft.getInstance().gameSettings.thirdPersonView == 0)
            {
                mainArm.rotateAngleX = 0;
                mainArm.rotateAngleY = 0;
                mainArm.rotateAngleZ = 0;
                return;
            }

            copyModelAngles(model.bipedHead, mainArm);
            copyModelAngles(model.bipedHead, secondaryArm);
            mainArm.rotateAngleX = (float) Math.toRadians(-60F + aimProgress * -25F);
            mainArm.rotateAngleY = (float) Math.toRadians((-55F + aimProgress * -10F) * (right ? 1F : -1F));
            mainArm.rotationPointX = -5;
            secondaryArm.rotateAngleX = (float) Math.toRadians(-65F + aimProgress * -25F);
            secondaryArm.rotateAngleY = (float) Math.toRadians((-5F + aimProgress * -10F) * (right ? 1F : -1F));
            secondaryArm.rotationPointZ = -1;
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            player.prevRenderYawOffset = player.prevRotationYaw + (right ? 45F : -40F);
            player.renderYawOffset = player.rotationYaw + (right ? 45F : -40F);
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public void applyHeldItemTransforms(Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
        {
            if(hand == Hand.MAIN_HAND)
            {
                boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
                matrixStack.translate(0, 0, 0.05);
                float invertRealProgress = 1.0F - aimProgress;
                matrixStack.rotate(Vector3f.XP.rotationDegrees(30F * invertRealProgress + aimProgress * 5F));
                matrixStack.rotate(Vector3f.YP.rotationDegrees((-10F * invertRealProgress + aimProgress * -20F) * (right ? 1F : -1F)));
            }
        }

        @Override
        public void renderFirstPersonArms(ClientPlayerEntity player, HandSide hand, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, float partialTicks)
        {
            matrixStack.translate(0, 0, -1);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));

            matrixStack.push();

            float reloadProgress = ClientHandler.getGunRenderer().getReloadProgress(partialTicks);
            matrixStack.translate(0, -reloadProgress * 2, 0);

            int side = hand.opposite() == HandSide.RIGHT ? 1 : -1;
            matrixStack.translate(6 * side * 0.0625, -0.585, -0.5);

            if(Minecraft.getInstance().player.getSkinType().equals("slim") && hand.opposite() == HandSide.LEFT)
            {
                matrixStack.translate(0.03125F * -side, 0, 0);
            }

            matrixStack.rotate(Vector3f.XP.rotationDegrees(80F));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(15F * -side));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(15F * -side));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-35F));
            matrixStack.scale(0.5F, 0.5F, 0.5F);

            RenderUtil.renderFirstPersonArm(player, hand.opposite(), matrixStack, buffer, light);

            matrixStack.pop();

            double centerOffset = 2.5;
            if(Minecraft.getInstance().player.getSkinType().equals("slim"))
            {
                centerOffset += hand == HandSide.RIGHT ? 0.2 : 0.8;
            }
            centerOffset = hand == HandSide.RIGHT ? -centerOffset : centerOffset;
            matrixStack.translate(centerOffset * 0.0625, -0.4, -0.975);

            matrixStack.rotate(Vector3f.XP.rotationDegrees(80F));
            matrixStack.scale(0.5F, 0.5F, 0.5F);

            RenderUtil.renderFirstPersonArm(player, hand, matrixStack, buffer, light);
        }
    }, false);

    /**
     * A custom grip type designed for the mini gun simply due it's nature of being a completely
     * unique way to hold the weapon
     */
    public static final GripType MINI_GUN = new GripType(new ResourceLocation(Reference.MOD_ID, "mini_gun"), new HeldAnimation()
    {
        @Override
        @OnlyIn(Dist.CLIENT)
        public void applyPlayerModelRotation(PlayerEntity player, PlayerModel model, Hand hand, float aimProgress)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            ModelRenderer mainArm = right ? model.bipedRightArm : model.bipedLeftArm;
            ModelRenderer secondaryArm = right ? model.bipedLeftArm : model.bipedRightArm;

            mainArm.rotateAngleX = (float) Math.toRadians(-15F);
            mainArm.rotateAngleY = (float) Math.toRadians(-45F) * (right ? 1F : -1F);
            mainArm.rotateAngleZ = (float) Math.toRadians(0F);

            secondaryArm.rotateAngleX = (float) Math.toRadians(-45F);
            secondaryArm.rotateAngleY = (float) Math.toRadians(30F) * (right ? 1F : -1F);
            secondaryArm.rotateAngleZ = (float) Math.toRadians(0F);
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            player.prevRenderYawOffset = player.prevRotationYaw + 45F * (right ? 1F : -1F);
            player.renderYawOffset = player.rotationYaw + 45F * (right ? 1F : -1F);
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public void applyHeldItemTransforms(Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
        {
            if(hand == Hand.OFF_HAND)
            {
                matrixStack.translate(0, -10 * 0.0625F, 0);
                matrixStack.translate(0, 0, -2 * 0.0625F);
            }
        }
    }, false);

    /**
     * A custom grip type designed for the bazooka.
     */
    public static final GripType BAZOOKA = new GripType(new ResourceLocation(Reference.MOD_ID, "bazooka"), new HeldAnimation()
    {
        @Override
        public void applyPlayerModelRotation(PlayerEntity player, PlayerModel model, Hand hand, float aimProgress)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            ModelRenderer mainArm = right ? model.bipedRightArm : model.bipedLeftArm;
            ModelRenderer secondaryArm = right ? model.bipedLeftArm : model.bipedRightArm;

            mainArm.rotateAngleX = (float) Math.toRadians(-90F);
            mainArm.rotateAngleY = (float) Math.toRadians(-35F) * (right ? 1F : -1F);
            mainArm.rotateAngleZ = (float) Math.toRadians(0F);

            secondaryArm.rotateAngleX = (float) Math.toRadians(-91F);
            secondaryArm.rotateAngleY = (float) Math.toRadians(45F) * (right ? 1F : -1F);
            secondaryArm.rotateAngleZ = (float) Math.toRadians(0F);
        }

        @Override
        public void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            player.prevRenderYawOffset = player.prevRotationYaw + 35F * (right ? 1F : -1F);
            player.renderYawOffset = player.rotationYaw + 35F * (right ? 1F : -1F);
        }
    }, false);

    /**
     * The grip type map.
     */
    private static Map<ResourceLocation, GripType> gripTypeMap = new HashMap<>();

    static
    {
        /* Registers the standard grip types when the class is loaded */
        registerType(ONE_HANDED);
        registerType(TWO_HANDED);
        registerType(MINI_GUN);
        registerType(BAZOOKA);
    }

    /**
     * Registers a new grip type. If the id already exists, the grip type will simply be ignored.
     *
     * @param type the instance of the grip type
     */
    public static void registerType(GripType type)
    {
        gripTypeMap.putIfAbsent(type.getId(), type);
    }

    /**
     * Gets the grip type associated the the id. If the grip type does not exist, it will default to
     * one handed.
     *
     * @param id the id of the grip type
     * @return returns an instance of the grip type or ONE_HANDED if it doesn't exist
     */
    public static GripType getType(ResourceLocation id)
    {
        return gripTypeMap.getOrDefault(id, ONE_HANDED);
    }

    private final ResourceLocation id;
    private final HeldAnimation heldAnimation;
    private final boolean renderOffhand;

    /**
     * Creates a new grip type.
     *
     * @param id the id of the grip type
     * @param heldAnimation the animation functions to apply to the held weapon
     * @param renderOffhand if this grip type allows the weapon to be rendered in the off hand
     */
    public GripType(ResourceLocation id, HeldAnimation heldAnimation, boolean renderOffhand)
    {
        this.id = id;
        this.heldAnimation = heldAnimation;
        this.renderOffhand = renderOffhand;
    }

    /**
     * Gets the id of the grip type
     */
    public ResourceLocation getId()
    {
        return this.id;
    }

    /**
     * Gets the held animation instance. Used for rendering
     */
    public HeldAnimation getHeldAnimation()
    {
        return this.heldAnimation;
    }

    /**
     * Determines if this grip type will allow the weapon to be rendered in the off hand
     */
    public boolean canRenderOffhand()
    {
        return this.renderOffhand;
    }

    /**
     * Copies the rotations from one {@link ModelRenderer} instance to another
     *
     * @param source the model renderer to grab the rotations from
     * @param dest   the model renderer to apply the rotations to
     */
    @OnlyIn(Dist.CLIENT)
    private static void copyModelAngles(ModelRenderer source, ModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }
}
