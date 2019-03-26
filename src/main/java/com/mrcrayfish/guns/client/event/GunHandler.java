package com.mrcrayfish.guns.client.event;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Buttons;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.controllable.event.ControllerEvent;
import com.mrcrayfish.guns.MrCrayfishGunMod;
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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class GunHandler
{
    private static final Map<UUID, AimTracker> AIMING_MAP = new HashMap<>();

    public static boolean aiming = false;

    @SubscribeEvent
    public void onKeyPressed(InputEvent.MouseInputEvent event)
    {

    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(MrCrayfishGunMod.proxy.isZooming())
        {
            if(!aiming)
            {
                Minecraft.getMinecraft().player.getDataManager().set(CommonEvents.AIMING, true);
                PacketHandler.INSTANCE.sendToServer(new MessageAim(true));
                aiming = true;
            }
        }
        else if(aiming)
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
        AimTracker tracker = getAimTracker(player);
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
    private static AimTracker getAimTracker(EntityPlayer player)
    {
        if(player.getDataManager().get(CommonEvents.AIMING) && !AIMING_MAP.containsKey(player.getUniqueID()))
        {
            AIMING_MAP.put(player.getUniqueID(), new AimTracker());
        }
        return AIMING_MAP.get(player.getUniqueID());
    }

    public static float getAimProgress(EntityPlayer player, float partialTicks)
    {
        AimTracker tracker = getAimTracker(player);
        if(tracker != null)
        {
            return tracker.getNormalProgress(partialTicks);
        }
        return 0F;
    }
}
