package com.mrcrayfish.guns.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.guns.client.AimTracker;
import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.*;
import com.mrcrayfish.guns.object.GripType;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.object.Scope;
import com.mrcrayfish.guns.util.ItemStackUtil;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class GunRenderer
{
    private static final double ZOOM_TICKS = 4;

    public static int screenTextureId = -1;

    public boolean drawFlash = false;
    private int zoomProgress;
    private int lastZoomProgress;
    public double normalZoomProgress;
    public double recoilNormal;
    public double recoilAngle;

    private int startTick;
    private int reloadTimer;
    private int prevReloadTimer;
    private boolean lastSmoothCamera = Minecraft.getInstance().gameSettings.smoothCamera;

    @SubscribeEvent
    public void onKeyPressedEvent(InputEvent.KeyInputEvent event)
    {
        if(Minecraft.getInstance().gameSettings.keyBindSmoothCamera.isPressed())
        {
            Minecraft.getInstance().gameSettings.smoothCamera = !Minecraft.getInstance().gameSettings.smoothCamera;
            lastSmoothCamera = Minecraft.getInstance().gameSettings.smoothCamera;
        }
    }

    @SubscribeEvent
    public void onFovUpdate(FOVUpdateEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() && mc.gameSettings.thirdPersonView == 0)
        {
            ItemStack heldItem = mc.player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                ItemStack scopeStack = Gun.getScope(heldItem);
                Scope scope = null;
                if(scopeStack.getItem() instanceof ScopeItem)
                {
                    scope = ((ScopeItem) scopeStack.getItem()).getScope();
                }

                GunItem gun = (GunItem) heldItem.getItem();
                if(isZooming(Minecraft.getInstance().player) && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING))
                {
                    mc.gameSettings.smoothCamera = gun.getGun().modules.zoom.smooth;
                    float newFov = gun.getGun().modules.zoom.fovModifier - 0.1F;
                    if(scope != null)
                    {
                        newFov -= scope.getAdditionalZoom();
                        mc.gameSettings.smoothCamera = gun.getGun().modules.attachments.scope.smooth;
                        if(scope.isSmoothCamera())
                        {
                            mc.gameSettings.smoothCamera = true;
                        }
                    }
                    event.setNewfov(newFov + (1.0F - newFov) * (1.0F - (zoomProgress / (float) ZOOM_TICKS)));
                }
                else
                {
                    mc.gameSettings.smoothCamera = this.lastSmoothCamera;
                }
            }
            else
            {
                mc.gameSettings.smoothCamera = this.lastSmoothCamera;
            }
        }
        else
        {
            mc.gameSettings.smoothCamera = this.lastSmoothCamera;
        }
    }

    @SubscribeEvent
    public void onPreClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START) return;

        this.lastZoomProgress = this.zoomProgress;

        PlayerEntity player = Minecraft.getInstance().player;
        if(isZooming(player) && !SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
        {
            if(this.zoomProgress < ZOOM_TICKS)
            {
                this.zoomProgress++;
            }
        }
        else
        {
            if(this.zoomProgress > 0)
            {
                this.zoomProgress--;
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END) return;

        this.prevReloadTimer = this.reloadTimer;

        PlayerEntity player = Minecraft.getInstance().player;
        if(isZooming(player) && !SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
        {
            Minecraft.getInstance().player.prevCameraYaw = 0.0075F;
            Minecraft.getInstance().player.cameraYaw = 0.0075F;
        }

        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
            {
                IOverrideModel model = ModelOverrides.getModel(heldItem);
                if(model != null)
                {
                    model.tick(player);
                }
            }

            if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
            {
                if(this.startTick == -1)
                {
                    this.startTick = player.ticksExisted + 5;
                }
                if(this.reloadTimer < 5)
                {
                    this.reloadTimer++;
                }
            }
            else
            {
                if(this.startTick != -1)
                {
                    this.startTick = -1;
                }
                if(this.reloadTimer > 0)
                {
                    this.reloadTimer -= 1;
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event)
    {
        normalZoomProgress = (lastZoomProgress + (zoomProgress - lastZoomProgress) * (lastZoomProgress == 0 || lastZoomProgress == ZOOM_TICKS ? 0.0 : event.getPartialTicks())) / ZOOM_TICKS;
        if(normalZoomProgress > 0 && event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderHandEvent event)
    {
        boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? event.getHand() == Hand.MAIN_HAND : event.getHand() == Hand.OFF_HAND;
        ItemStack heldItem = event.getItemStack();

        if(event.getHand() == Hand.OFF_HAND)
        {
            ItemStack mainHandStack = Minecraft.getInstance().player.getHeldItemMainhand();
            if(mainHandStack.getItem() instanceof GunItem)
            {
                if(((GunItem) mainHandStack.getItem()).getGun().general.gripType != GripType.ONE_HANDED)
                {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        if(!(heldItem.getItem() instanceof GunItem)) return;

        /* Cancel it because we are doing our own custom render */
        event.setCanceled(true);

        /* Ignores rendering the gun if the grip type doesn't allow it to be render in the offhand */
        if(event.getHand() == Hand.OFF_HAND)
        {
            return;
        }

        ItemStack scopeStack = Gun.getScope(heldItem);
        Scope scope = null;
        if(scopeStack.getItem() instanceof ScopeItem)
        {
            scope = ((ScopeItem) scopeStack.getItem()).getScope();
        }

        MatrixStack matrixStack = event.getMatrixStack();
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(heldItem);
        float scaleX = model.getItemCameraTransforms().firstperson_right.scale.getX();
        float scaleY = model.getItemCameraTransforms().firstperson_right.scale.getY();
        float scaleZ = model.getItemCameraTransforms().firstperson_right.scale.getZ();
        float translateY = model.getItemCameraTransforms().firstperson_right.translation.getY() * scaleY;
        float translateZ = model.getItemCameraTransforms().firstperson_right.translation.getZ() * scaleZ;

        matrixStack.push();

        if(this.normalZoomProgress > 0)
        {
            GunItem gunItem = (GunItem) heldItem.getItem();
            if(event.getHand() == Hand.MAIN_HAND)
            {
                double xOffset = 0.0;
                double yOffset = 0.0;
                double zOffset = 0.0;

                Gun gun = gunItem.getGun();
                if(gun.canAttachType(IAttachment.Type.SCOPE) && scope != null)
                {
                    Gun.ScaledPositioned scaledPos = gun.modules.attachments.scope;
                    xOffset -= scaledPos.xOffset * 0.0625 * scaleX;
                    yOffset -= scaledPos.yOffset * 0.0625 * scaleY - translateY + scope.getCenterOffset() * scaleY * 0.0625 * scaledPos.scale;
                    zOffset -= scaledPos.zOffset * 0.0625 * scaleZ - translateZ - 0.35;
                }
                else if(gun.modules.zoom != null)
                {
                    xOffset -= gun.modules.zoom.xOffset * 0.0625 * scaleX;
                    yOffset -= gun.modules.zoom.yOffset * 0.0625 * scaleY - translateY;
                    zOffset -= gun.modules.zoom.zOffset * 0.0625 * scaleZ - translateZ;
                }
                double renderOffset = right ? -0.3415 : -0.3775;
                matrixStack.translate((renderOffset + xOffset) * normalZoomProgress * (right ? 1F : -1F), yOffset * normalZoomProgress, zOffset * normalZoomProgress);
            }
            else
            {
                matrixStack.translate(0, -1 * normalZoomProgress, 0);
            }
        }

        matrixStack.translate(0, -event.getEquipProgress(), 0);

        HandSide hand = right ? HandSide.RIGHT : HandSide.LEFT;

        /* Renders the reload arm. Will only render if actually reloading. This is applied before
         * any recoil or reload rotations as the animations would be borked if applied after. */
        this.renderReloadArm(matrixStack, event.getBuffers(), event.getLight(), heldItem, hand);

        /* Translate the item position based on the hand side */
        matrixStack.translate(0.5602F - (right ? 0.0F : 0.72F), -0.55625F, -0.72F);

        /* Applies recoil and reload rotations */
        this.applyRecoil(matrixStack, heldItem.getItem(), ((GunItem) heldItem.getItem()).getGun());
        this.applyReload(matrixStack, event.getPartialTicks());

        /* Render offhand arm so it is holding the weapon. Only applies if it's a two handed weapon */
        matrixStack.push();
        matrixStack.translate(-(0.56F - (right ? 0.0F : 0.72F)), 0.56F, 0.72F);
        this.renderHeldArm(matrixStack, event.getBuffers(), event.getLight(), Minecraft.getInstance().player, heldItem, hand, event.getPartialTicks());
        matrixStack.pop();

        /* Renders the weapon */
        this.renderWeapon(matrixStack, Minecraft.getInstance().player, heldItem, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, event.getPartialTicks());

        matrixStack.pop();
    }

    private void applyReload(MatrixStack matrixStack, float partialTicks)
    {
        float reloadProgress = (this.prevReloadTimer + (this.reloadTimer - this.prevReloadTimer) * partialTicks) / 5.0F;
        matrixStack.translate(0, 0.35 * reloadProgress, 0);
        matrixStack.translate(0, 0, -0.1 * reloadProgress);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(45F * reloadProgress));
    }

    private void applyRecoil(MatrixStack matrixStack, Item item, Gun gun)
    {
        CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
        float cooldown = tracker.getCooldown(item, Minecraft.getInstance().getRenderPartialTicks());
        cooldown = cooldown >= gun.general.recoilDurationOffset ? (cooldown - gun.general.recoilDurationOffset) / (1.0F - gun.general.recoilDurationOffset) : 0.0F;

        if(cooldown >= 0.8)
        {
            float amount = 1.0F * ((1.0F - cooldown) / 0.2F);
            this.recoilNormal = 1 - (--amount) * amount * amount * amount;
        }
        else
        {
            float amount = (cooldown / 0.8F);
            this.recoilNormal = amount < 0.5 ? 2 * amount * amount : -1 + (4 - 2 * amount) * amount;
        }

        this.recoilAngle = gun.general.recoilAngle;

        matrixStack.translate(0, 0, gun.general.recoilKick * 0.0625 * this.recoilNormal);
        matrixStack.translate(0, 0, 0.35);
        matrixStack.rotate(Vector3f.XP.rotationDegrees((float) (gun.general.recoilAngle * this.recoilNormal)));
        matrixStack.translate(0, 0, -0.35);
    }

    private boolean isZooming(PlayerEntity player)
    {
        if(player != null && player.getHeldItemMainhand() != ItemStack.EMPTY)
        {
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof GunItem)
            {
                Gun gun = ((GunItem) stack.getItem()).getGun();
                return gun.modules != null && gun.modules.zoom != null && ClientHandler.isAiming();
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event)
    {
        if(event.phase.equals(TickEvent.Phase.START)) return;

        Minecraft mc = Minecraft.getInstance();
        if(!mc.isGameFocused()) return;

        PlayerEntity player = mc.player;
        if(player == null) return;

        if(Minecraft.getInstance().gameSettings.thirdPersonView != 0) return;

        ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
        if(heldItem.isEmpty() || !(heldItem.getItem() instanceof GunItem)) return;


        //TODO reimplement sword cooldown
        /*ScaledResolution scaledResolution = new ScaledResolution(mc);

        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            Gun gun = ((GunItem) heldItem.getItem()).getGun();
            if(!gun.general.auto)
            {
                float coolDown = player.getCooldownTracker().getCooldown(heldItem.getItem(), event.renderTickTime);
                if(coolDown > 0.0F)
                {
                    double scale = 3;
                    int i = (int) ((scaledResolution.getScaledHeight() / 2 - 7 - 60) / scale);
                    int j = (int) Math.ceil((scaledResolution.getScaledWidth() / 2 - 8 * scale) / scale);

                    GlStateManager.enableAlpha();
                    mc.getTextureManager().bindTexture(Gui.ICONS);

                    GlStateManager.pushMatrix();
                    {
                        GlStateManager.scale(scale, scale, scale);
                        int progress = (int) Math.ceil((coolDown + 0.05) * 17.0F) - 1;
                        GuiScreen.drawModalRectWithCustomSizedTexture(j, i, 36, 94, 16, 4, 256, 256);
                        GuiScreen.drawModalRectWithCustomSizedTexture(j, i, 52, 94, progress, 4, 256, 256);
                    }
                    GlStateManager.popMatrix();
                }
            }
        }*/
    }

    @SubscribeEvent
    public void onRenderHeldItem(RenderItemEvent.Held.Pre event)
    {
        Hand hand = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? event.getHandSide() == HandSide.RIGHT ? Hand.MAIN_HAND : Hand.OFF_HAND : event.getHandSide() == HandSide.LEFT ? Hand.MAIN_HAND : Hand.OFF_HAND;
        LivingEntity entity = event.getEntity();
        ItemStack heldItem = entity.getHeldItem(hand);

        if(heldItem.getItem() instanceof GunItem)
        {
            event.setCanceled(true);
            Gun gun = ((GunItem) heldItem.getItem()).getGun();
            gun.general.gripType.getHeldAnimation().applyHeldItemTransforms(hand, entity instanceof PlayerEntity ? AimTracker.getAimProgress((PlayerEntity) entity, event.getPartialTicks()) : 0.0F, event.getMatrixStack(), event.getRenderTypeBuffer());
            if(hand == Hand.MAIN_HAND)
            {
                this.renderWeapon(event.getMatrixStack(), entity, heldItem, event.getTransformType(), event.getPartialTicks());
            }
        }

        if(hand == Hand.OFF_HAND)
        {
            ItemStack mainHandStack = entity.getHeldItemMainhand();
            if(!mainHandStack.isEmpty() && mainHandStack.getItem() instanceof GunItem)
            {
                Gun mainHandGun = ((GunItem) mainHandStack.getItem()).getGun();
                if(!mainHandGun.general.gripType.canRenderOffhand())
                {
                    event.setCanceled(true);
                }
                else if(heldItem.getItem() instanceof GunItem)
                {
                    Gun gun = ((GunItem) heldItem.getItem()).getGun();
                    if(gun.general.gripType.canRenderOffhand())
                    {
                        this.renderWeapon(event.getMatrixStack(), entity, heldItem, event.getTransformType(), event.getPartialTicks());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onSetupAngles(PlayerModelEvent.SetupAngles.Post event)
    {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            PlayerModel model = event.getModelPlayer();
            Gun gun = ((GunItem) heldItem.getItem()).getGun();
            gun.general.gripType.getHeldAnimation().applyPlayerModelRotation(model, Hand.MAIN_HAND, AimTracker.getAimProgress((PlayerEntity) event.getEntity(), event.getPartialTicks()));
            copyModelAngles(model.bipedRightArm, model.bipedRightArmwear);
            copyModelAngles(model.bipedLeftArm, model.bipedLeftArmwear);
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            Gun gun = ((GunItem) heldItem.getItem()).getGun();
            gun.general.gripType.getHeldAnimation().applyPlayerPreRender(player, Hand.MAIN_HAND, AimTracker.getAimProgress((PlayerEntity) event.getEntity(), event.getPartialRenderTick()), event.getMatrixStack(), event.getBuffers());
        }
    }

    @SubscribeEvent
    public void onModelRender(PlayerModelEvent.Render.Pre event)
    {
        PlayerEntity player = event.getPlayer();
        ItemStack offHandStack = player.getHeldItemOffhand();
        if(offHandStack.getItem() instanceof GunItem)
        {
            switch(player.getPrimaryHand().opposite())
            {
                case LEFT:
                    event.getModelPlayer().leftArmPose = BipedModel.ArmPose.EMPTY;
                    break;
                case RIGHT:
                    event.getModelPlayer().rightArmPose = BipedModel.ArmPose.EMPTY;
                    break;
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(PlayerModelEvent.Render.Post event)
    {
        MatrixStack matrixStack = event.getMatrixStack();
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemOffhand();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            Gun gun = ((GunItem) heldItem.getItem()).getGun();
            if(!gun.general.gripType.canRenderOffhand())
            {
                matrixStack.push();
                matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(180F));
                if(player.isCrouching())
                {
                    matrixStack.translate(0 * 0.0625, -7 * 0.0625, -5 * 0.0625);
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(30F));
                }
                else
                {
                    matrixStack.translate(0 * 0.0625, -5 * 0.0625, -2.75 * 0.0625);
                }
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(-45F));
                matrixStack.scale(0.5F, 0.5F, 0.5F);
                this.renderWeapon(matrixStack, player, heldItem, ItemCameraTransforms.TransformType.FIXED, event.getPartialTicks());
                matrixStack.pop();
            }
            else
            {
                matrixStack.push();
                matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(180F));
                if(player.isCrouching())
                {
                    matrixStack.translate(-4.5 * 0.0625, -15 * 0.0625, -4 * 0.0625);
                }
                else
                {
                    matrixStack.translate(-4.5 * 0.0625, -13 * 0.0625, 1 * 0.0625);
                }
                matrixStack.rotate(Vector3f.YP.rotationDegrees(90F));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(75F));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees((float) (Math.toDegrees(event.getModelPlayer().bipedRightLeg.rotateAngleX) / 10F)));
                matrixStack.scale(0.5F, 0.5F, 0.5F);
                this.renderWeapon(matrixStack, player, heldItem, ItemCameraTransforms.TransformType.FIXED, event.getPartialTicks());
                matrixStack.pop();
            }
        }
    }

    @SubscribeEvent
    public void onRenderEntityItem(RenderItemEvent.Entity.Pre event)
    {
        Minecraft mc = Minecraft.getInstance();
        event.setCanceled(this.renderWeapon(event.getMatrixStack(), mc.player, event.getItem(), event.getTransformType(), event.getPartialTicks()));
    }

    @SubscribeEvent
    public void onRenderEntityItem(RenderItemEvent.Gui.Pre event)
    {
        Minecraft mc = Minecraft.getInstance();
        event.setCanceled(this.renderWeapon(event.getMatrixStack(), mc.player, event.getItem(), event.getTransformType(), event.getPartialTicks()));
    }

    private boolean renderWeapon(MatrixStack matrixStack, LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, float partialTicks)
    {
        if(stack.getItem() instanceof GunItem)
        {
            matrixStack.push();

            ItemStack model = ItemStack.EMPTY;
            if(stack.getTag() != null)
            {
                if(stack.getTag().contains("Model", Constants.NBT.TAG_COMPOUND))
                {
                    model = ItemStack.read(stack.getTag().getCompound("Model"));
                    model.setTag(stack.getTag().copy());
                }
            }

            RenderUtil.applyTransformType(model.isEmpty() ? stack : model, matrixStack, transformType);

            if(transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
            {
                this.renderMuzzleFlash(matrixStack, stack);
            }

            this.renderGun(entity, transformType, model.isEmpty() ? stack : model, partialTicks);
            this.renderAttachments(matrixStack, entity, transformType, stack, partialTicks);

            matrixStack.pop();
            return true;
        }
        return false;
    }

    private void renderGun(LivingEntity entity, ItemCameraTransforms.TransformType transformType, ItemStack stack, float partialTicks)
    {
        if(ModelOverrides.hasModel(stack))
        {
            IOverrideModel model = ModelOverrides.getModel(stack);
            if(model != null)
            {
                model.render(partialTicks, transformType, stack, ItemStack.EMPTY, entity);
            }
        }
        else
        {
            RenderUtil.renderModel(stack);
        }
    }

    private void renderAttachments(MatrixStack matrixStack, LivingEntity entity, ItemCameraTransforms.TransformType transformType, ItemStack stack, float partialTicks)
    {
        if(stack.getItem() instanceof GunItem)
        {
            Gun gun = ((GunItem) stack.getItem()).getGun();
            CompoundNBT gunTag = ItemStackUtil.createTagCompound(stack);
            CompoundNBT attachments = gunTag.getCompound("Attachments");
            for(String attachmentKey : attachments.keySet())
            {
                IAttachment.Type type = IAttachment.Type.getType(attachmentKey);
                if(gun.canAttachType(type))
                {
                    ItemStack attachmentStack = Gun.getAttachment(type, stack);
                    if(!attachmentStack.isEmpty())
                    {
                        Gun.ScaledPositioned positioned = gun.getAttachmentPosition(type);
                        if(positioned != null)
                        {
                            matrixStack.push();
                            double displayX = positioned.xOffset * 0.0625;
                            double displayY = positioned.yOffset * 0.0625;
                            double displayZ = positioned.zOffset * 0.0625;
                            matrixStack.translate(displayX, displayY, displayZ);
                            matrixStack.translate(0, -0.5, 0);
                            matrixStack.scale((float) positioned.scale, (float) positioned.scale, (float) positioned.scale);

                            IOverrideModel model = ModelOverrides.getModel(attachmentStack);
                            if(model != null)
                            {
                                model.render(partialTicks, transformType, attachmentStack, stack, entity);
                            }
                            else
                            {
                                RenderUtil.renderModel(attachmentStack, stack);
                            }

                            matrixStack.pop();
                        }
                    }
                }
            }
        }
    }

    private void renderMuzzleFlash(MatrixStack matrixStack, ItemStack weapon)
    {
        Gun gun = ((GunItem) weapon.getItem()).getGun();
        if(this.drawFlash)
        {
            //TODO reimplement muzzle flash
            /*IBakedModel flashModel = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(flash);
            matrixStack.push();
            matrixStack.translate(gun.display.flash.xOffset * 0.0625, gun.display.flash.yOffset * 0.0625, gun.display.flash.zOffset * 0.0625);

            if(!Gun.getAttachment(IAttachment.Type.BARREL, weapon).isEmpty())
            {
                Gun.ScaledPositioned positioned = gun.getAttachmentPosition(IAttachment.Type.BARREL);
                if(positioned != null)
                {
                    matrixStack.translate(0, 0, (gun.display.flash.zOffset - positioned.zOffset) * 0.0625 - positioned.scale);
                    matrixStack.scale(0.5F, 0.5F, 0.0F);
                }
            }

            RenderUtil.renderModel(flashModel, weapon);

            matrixStack.pop();*/
        }
    }

    private static void copyModelAngles(ModelRenderer source, ModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }

    private void renderHeldArm(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, ClientPlayerEntity player, ItemStack stack, HandSide hand, float partialTicks)
    {
        matrixStack.push();

        Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
        if(gun.general.gripType == GripType.TWO_HANDED)
        {
            matrixStack.translate(0, 0, -1);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));

            float reloadProgress = (prevReloadTimer + (reloadTimer - prevReloadTimer) * partialTicks) / 5F;
            matrixStack.translate(0, -reloadProgress * 2, 0);

            int side = hand.opposite() == HandSide.RIGHT ? 1 : -1;
            matrixStack.translate(6.5 * side * 0.0625, -0.55, -0.5625);

            if(Minecraft.getInstance().player.getSkinType().equals("slim") && hand.opposite() == HandSide.LEFT)
            {
                matrixStack.translate(0.03125F * -side, 0, 0);
            }

            matrixStack.rotate(Vector3f.XP.rotationDegrees(90F));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(15F * -side));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(15F * -side));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-35F));
            matrixStack.scale(0.5F, 0.5F, 0.5F);

            IVertexBuilder builder = buffer.getBuffer(RenderType.getEntitySolid(player.getLocationSkin()));
            this.renderArm(matrixStack, builder, light, hand.opposite());
        }
        else if(gun.general.gripType == GripType.ONE_HANDED)
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

            IVertexBuilder builder = buffer.getBuffer(RenderType.getEntitySolid(player.getLocationSkin()));
            this.renderArm(matrixStack, builder, light, hand);
        }

        matrixStack.pop();
    }

    private void renderReloadArm(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, ItemStack stack, HandSide hand)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null || mc.player.ticksExisted < startTick || reloadTimer != 5)
        {
            return;
        }

        Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
        AmmoItem ammo = AmmoRegistry.getInstance().getAmmo(gun.projectile.item);
        if(ammo == null)
        {
            return;
        }

        matrixStack.push();

        float reload = ((mc.player.ticksExisted - this.startTick + mc.getRenderPartialTicks()) % 10F) / 10F;
        float percent = 1.0F - reload;
        if(percent >= 0.5F)
        {
            percent = 1.0F - percent;
        }
        percent *= 2F;
        percent = percent < 0.5 ? 2 * percent * percent : -1 + (4 - 2 * percent) * percent;

        int side = hand.opposite() == HandSide.RIGHT ? 1 : -1;
        matrixStack.translate(-2.75 * side * 0.0625, -0.5625, -0.5625);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));
        matrixStack.translate(0, -0.35 * (1.0 - percent), 0);
        matrixStack.translate(side * 1 * 0.0625, 0, 0);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(90F));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(35F * -side));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-75F * percent));
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        IVertexBuilder builder = buffer.getBuffer(RenderType.getEntitySolid(mc.player.getLocationSkin()));
        this.renderArm(matrixStack, builder, light, hand.opposite());

        if(reload < 0.5F)
        {
            matrixStack.push();
            matrixStack.translate(-side * 5 * 0.0625, 15 * 0.0625, -1 * 0.0625);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(180F));
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            RenderUtil.renderModel(new ItemStack(ammo), ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
            matrixStack.pop();
        }

        matrixStack.pop();
    }

    private void renderArm(MatrixStack matrixStack, IVertexBuilder builder, int light, HandSide hand)
    {
        ClientPlayerEntity clientPlayerEntity = Minecraft.getInstance().player;
        Minecraft.getInstance().getTextureManager().bindTexture(clientPlayerEntity.getLocationSkin());
        PlayerRenderer renderPlayer = (PlayerRenderer) Minecraft.getInstance().getRenderManager().getRenderer(clientPlayerEntity);

        if(hand == HandSide.RIGHT)
        {
            renderPlayer.getEntityModel().bipedRightArm.rotateAngleX = 0F;
            renderPlayer.getEntityModel().bipedRightArm.rotateAngleY = 0F;
            renderPlayer.getEntityModel().bipedRightArm.rotateAngleZ = 0F;
            renderPlayer.getEntityModel().bipedRightArm.render(matrixStack, builder, light, OverlayTexture.NO_OVERLAY);
        }
        else
        {
            renderPlayer.getEntityModel().bipedLeftArm.rotateAngleX = 0F;
            renderPlayer.getEntityModel().bipedLeftArm.rotateAngleY = 0F;
            renderPlayer.getEntityModel().bipedLeftArm.rotateAngleZ = 0F;
            renderPlayer.getEntityModel().bipedLeftArm.render(matrixStack, builder, light, OverlayTexture.NO_OVERLAY);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void blindPlayer(RenderGameOverlayEvent.Pre event)
    {
        if(event.getType() != ElementType.ALL) return;

        /*EffectInstance effect = Minecraft.getInstance().player.getActivePotionEffect(ModPotions.BLINDED.get().getEffects().get(0).getPotion());
        if(effect != null)
        {
            // Render white screen-filling overlay at full alpha effect when duration is above threshold
            // When below threshold, fade to full transparency as duration approaches 0
            float percent = Math.min((effect.getDuration() / (float) Config.SERVER.alphaFadeThreshold.get()), 1);
            //Screen.blit(0, 0, 100, 100, ((int) (percent * Config.SERVER.alphaOverlay.get() + 0.5) << 24) | 16777215);
        }*/
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderLastWorld(RenderWorldLastEvent event)
    {
        if(screenTextureId == -1)
        {
            screenTextureId = GlStateManager.genTexture();
        }
        Minecraft mc = Minecraft.getInstance();
        GlStateManager.bindTexture(screenTextureId);
        GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight(), 0);
    }

    public double applyZoomSensitivity(double sensitivity)
    {
        return sensitivity * (1.0 - 0.25 * this.normalZoomProgress);
    }
}
