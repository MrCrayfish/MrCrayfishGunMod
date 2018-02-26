package com.mrcrayfish.guns.client.event;

import com.mrcrayfish.guns.client.AimTracker;
import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.client.render.gun.IGunModel;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.event.CommonEvents;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageAim;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.obfuscate.client.event.ModelPlayerEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class RenderEvents
{
    private static final Map<UUID, AimTracker> AIMING_MAP = new HashMap<>();

    private boolean aiming = false;

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event)
    {
        if(KeyBinds.KEY_AIM.isKeyDown())
        {
            if(!aiming)
            {
                Minecraft.getMinecraft().player.getDataManager().set(CommonEvents.AIMING, true);
                PacketHandler.INSTANCE.sendToServer(new MessageAim(true));
                aiming = true;
            }
        }
        else
        {
            Minecraft.getMinecraft().player.getDataManager().set(CommonEvents.AIMING, false);
            PacketHandler.INSTANCE.sendToServer(new MessageAim(false));
            aiming = false;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        EntityPlayer player = event.player;
        AimTracker tracker = this.getAimTracker(player);
        if(tracker != null)
        {
            tracker.handleAiming(player);
            if(!tracker.isAiming())
            {
                AIMING_MAP.remove(player.getUniqueID());
            }
        }
    }

    @Nullable
    private AimTracker getAimTracker(EntityPlayer player)
    {
        if(player.getDataManager().get(CommonEvents.AIMING) && !AIMING_MAP.containsKey(player.getUniqueID()))
        {
            AIMING_MAP.put(player.getUniqueID(), new AimTracker());
        }
        return AIMING_MAP.get(player.getUniqueID());
    }

    private float getAimProgress(EntityPlayer player, float partialTicks)
    {
        AimTracker tracker = this.getAimTracker(player);
        if(tracker != null)
        {
            return tracker.getNormalProgress(partialTicks);
        }
        return 0F;
    }

    @SubscribeEvent
    public void onRenderHeldItem(RenderItemEvent.Held.Pre event)
    {
        ItemStack heldItem = event.getItem();
        if(heldItem.getItem() instanceof ItemGun)
        {
            event.setCanceled(true);

            Gun gun = ((ItemGun) heldItem.getItem()).getGun();
            gun.general.gripType.getHeldAnimation().applyHeldItemTransforms(this.getAimProgress((EntityPlayer) event.getEntity(), event.getPartialTicks()));

            RenderUtil.applyTransformType(heldItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
            if(ModelOverrides.hasModel(heldItem.getItem()))
            {
                IGunModel model = ModelOverrides.getModel(heldItem.getItem());
                model.registerPieces();
                model.render(event.getPartialTicks(), ItemCameraTransforms.TransformType.NONE);
            }
            else
            {
                Minecraft.getMinecraft().getItemRenderer().renderItemSide(event.getEntity(), heldItem, ItemCameraTransforms.TransformType.NONE, event.getHandSide() == EnumHandSide.LEFT);
            }
            this.renderAttachments(heldItem);
        }
    }

    @SubscribeEvent
    public void onSetupAngles(ModelPlayerEvent.SetupAngles.Post event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun)
        {
            ModelPlayer model = event.getModelPlayer();
            Gun gun = ((ItemGun) heldItem.getItem()).getGun();
            gun.general.gripType.getHeldAnimation().applyPlayerModelRotation(model, this.getAimProgress((EntityPlayer) event.getEntity(), event.getPartialTicks()));
            copyModelAngles(model.bipedRightArm, model.bipedRightArmwear);
            copyModelAngles(model.bipedLeftArm, model.bipedLeftArmwear);
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun)
        {
            Gun gun = ((ItemGun) heldItem.getItem()).getGun();
            gun.general.gripType.getHeldAnimation().applyPlayerPreRender(player, this.getAimProgress((EntityPlayer) event.getEntity(), event.getPartialRenderTick()));
        }
    }

    @SubscribeEvent
    public void onRenderEntityItem(RenderItemEvent.Entity.Pre event)
    {
        event.setCanceled(this.renderGun(event.getItem(), event.getTransformType()));
    }

    @SubscribeEvent
    public void onRenderEntityItem(RenderItemEvent.Gui.Pre event)
    {
        event.setCanceled(this.renderGun(event.getItem(), event.getTransformType()));
    }

    private boolean renderGun(ItemStack stack, ItemCameraTransforms.TransformType transformType)
    {
        if(stack.getItem() instanceof ItemGun)
        {
            GlStateManager.pushMatrix();
            RenderUtil.applyTransformType(stack, transformType);
            RenderUtil.renderModel(stack);
            this.renderAttachments(stack);
            GlStateManager.popMatrix();
            return true;
        }
        return false;
    }

    private void renderAttachments(ItemStack stack)
    {
        if(stack.getItem() instanceof ItemGun)
        {
            Gun gun = ((ItemGun) stack.getItem()).getGun();
            NBTTagCompound itemTag = stack.getTagCompound();
            if(itemTag != null)
            {
                if(gun.canAttachScope() && itemTag.hasKey("attachments", Constants.NBT.TAG_COMPOUND))
                {
                    NBTTagCompound attachment = itemTag.getCompoundTag("attachments");
                    if(attachment.hasKey("scope", Constants.NBT.TAG_COMPOUND))
                    {
                        GlStateManager.pushMatrix();
                        {
                            ItemStack scope = new ItemStack(attachment.getCompoundTag("scope"));
                            GlStateManager.translate(gun.modules.attachments.scope.xOffset, gun.modules.attachments.scope.yOffset, gun.modules.attachments.scope.zOffset);
                            RenderUtil.renderModel(scope);
                        }
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
    }

    private static void copyModelAngles(ModelRenderer source, ModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }
}
