package com.tac.guns.client;

import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.client.handler.*;
import com.tac.guns.client.render.entity.GrenadeRenderer;
import com.tac.guns.client.render.entity.MissileRenderer;
import com.tac.guns.client.render.entity.ProjectileRenderer;
import com.tac.guns.client.render.entity.ThrowableGrenadeRenderer;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.render.gun.model.*;
import com.tac.guns.client.screen.*;
import com.tac.guns.client.settings.GunOptions;
import com.tac.guns.common.BoundingBoxManager;
import com.tac.guns.common.FloodLightSource.FloodLightSource;
import com.tac.guns.init.ModBlocks;
import com.tac.guns.init.ModContainers;
import com.tac.guns.init.ModEntities;
import com.tac.guns.init.ModItems;
import com.tac.guns.interfaces.IHeadshotBox;
import com.tac.guns.item.IColored;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageAttachments;
import com.tac.guns.network.message.MessageColorBench;
import com.tac.guns.network.message.MessageInspection;
import com.tac.guns.network.message.MessageIronSightSwitch;
import com.tac.guns.tileentity.FlashLightSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.MouseSettingsScreen;
import net.minecraft.client.gui.screen.VideoSettingsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ClientHandler
{
    private static Field mouseOptionsField;

    public static void setup()
    {
        MinecraftForge.EVENT_BUS.register(AimingHandler.get());
        MinecraftForge.EVENT_BUS.register(BulletTrailRenderingHandler.get());
        MinecraftForge.EVENT_BUS.register(CrosshairHandler.get());
        MinecraftForge.EVENT_BUS.register(GunRenderingHandler.get());
        MinecraftForge.EVENT_BUS.register(RecoilHandler.get());
        MinecraftForge.EVENT_BUS.register(ReloadHandler.get());
        MinecraftForge.EVENT_BUS.register(ShootingHandler.get());
        MinecraftForge.EVENT_BUS.register(SoundHandler.get());
        MinecraftForge.EVENT_BUS.register(HUDRenderingHandler.get());
        MinecraftForge.EVENT_BUS.register(FireModeSwitchEvent.get()); // Technically now a handler but, yes I need some naming reworks
        MinecraftForge.EVENT_BUS.register(IronSightSwitchEvent.get()); // Still, as well an event, am uncertain on what to name it, in short handles upcoming advanced iron sights

        //MinecraftForge.EVENT_BUS.register(FlashlightHandler.get()); // Completely broken... Needs a full rework
        //MinecraftForge.EVENT_BUS.register(FloodLightSource.get());

        MinecraftForge.EVENT_BUS.register(ScopeJitterHandler.getInstance()); // All built by MayDay memory part of the Timeless dev team, amazing work!!!!!!!!!!!

        MinecraftForge.EVENT_BUS.register(MovementAdaptationsHandler.get());

        KeyBinds.register();

        setupRenderLayers();
        registerEntityRenders();
        registerColors();
        registerModelOverrides();
        registerScreenFactories();
    }

    private static void setupRenderLayers()
    {
        RenderTypeLookup.setRenderLayer(ModBlocks.WORKBENCH.get(), RenderType.getCutout());
    }

    private static void registerEntityRenders()
    {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.PROJECTILE.get(), ProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GRENADE.get(), GrenadeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.THROWABLE_GRENADE.get(), ThrowableGrenadeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.THROWABLE_STUN_GRENADE.get(), ThrowableGrenadeRenderer::new);
        //RenderingRegistry.registerEntityRenderingHandler(ModEntities.MISSILE.get(), MissileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.RPG7_MISSILE.get(), MissileRenderer::new);
    }

    private static void registerColors()
    {
        IItemColor color = (stack, index) -> {
            if(!((IColored) stack.getItem()).canColor(stack))
            {
                return -1;
            }
            if(index == 0 && stack.hasTag() && stack.getTag().contains("Color", Constants.NBT.TAG_INT))
            {
                return stack.getTag().getInt("Color");
            }
            return -1;
        };
        ForgeRegistries.ITEMS.forEach(item -> {
            if(item instanceof IColored)
            {
                Minecraft.getInstance().getItemColors().register(color, item);
            }
        });
    }

    private static void registerModelOverrides()
    {
        ModelOverrides.register(ModItems.COYOTE_SIGHT.get(), new CoyoteSightModel());
        ModelOverrides.register(ModItems.LONGRANGE_8x_SCOPE.get(), new LongRange8xScopeModel());
        ModelOverrides.register(ModItems.VORTEX_LPVO_1_6.get(), new VortexLPVO_1_4xScopeModel());
        ModelOverrides.register(ModItems.ACOG_4.get(), new ACOG_4x_ScopeModel());
        ModelOverrides.register(ModItems.AIMPOINT_T1_SIGHT.get(), new AimpointT1SightModel());
        ModelOverrides.register(ModItems.EOTECH_N_SIGHT.get(), new EotechNSightModel());
        ModelOverrides.register(ModItems.VORTEX_UH_1.get(), new VortexUh1SightModel());
        ModelOverrides.register(ModItems.EOTECH_SHORT_SIGHT.get(), new EotechShortSightModel());
        ModelOverrides.register(ModItems.SRS_RED_DOT_SIGHT.get(), new SrsRedDotSightModel());
        ModelOverrides.register(ModItems.QMK152.get(), new Qmk152ScopeModel());

        ModelOverrides.register(ModItems.OLD_LONGRANGE_8x_SCOPE.get(), new OldLongRange8xScopeModel());
        ModelOverrides.register(ModItems.OLD_LONGRANGE_4x_SCOPE.get(), new OldLongRange4xScopeModel());

        ModelOverrides.register(ModItems.MINI_DOT.get(), new MiniDotSightModel());
        ModelOverrides.register(ModItems.MICRO_HOLO_SIGHT.get(), new MicroHoloSightModel());
     }

    private static void registerScreenFactories()
    {
        ScreenManager.registerFactory(ModContainers.WORKBENCH.get(), WorkbenchScreen::new);
        ScreenManager.registerFactory(ModContainers.ATTACHMENTS.get(), AttachmentScreen::new);
        ScreenManager.registerFactory(ModContainers.INSPECTION.get(), InspectScreen::new);
        ScreenManager.registerFactory(ModContainers.COLOR_BENCH.get(), ColorBenchAttachmentScreen::new);
    }

    @SubscribeEvent
    public static void onScreenInit(GuiScreenEvent.InitGuiEvent.Post event)
    {
        if(event.getGui() instanceof MouseSettingsScreen)
        {
            MouseSettingsScreen screen = (MouseSettingsScreen) event.getGui();
            if(mouseOptionsField == null)
            {
                mouseOptionsField = ObfuscationReflectionHelper.findField(MouseSettingsScreen.class, "field_213045_b");
                mouseOptionsField.setAccessible(true);
            }
            try
            {
                OptionsRowList list = (OptionsRowList) mouseOptionsField.get(screen);
                list.addOption(GunOptions.ADS_SENSITIVITY, GunOptions.CROSSHAIR);
                list.addOption(GunOptions.TOGGLE_ADS);
            }
            catch(IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        if(event.getGui() instanceof VideoSettingsScreen)
        {
            VideoSettingsScreen screen = (VideoSettingsScreen) event.getGui();

            event.addWidget((new Button(screen.width / 2 - 215, 10, 75, 20, new TranslationTextComponent("tac.options.gui_settings"), (p_213126_1_) -> {
                Minecraft.getInstance().displayGuiScreen(new TaCSettingsScreen(screen, Minecraft.getInstance().gameSettings));
            })));
        }

    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && mc.currentScreen == null)
        {
            /*if(KeyBinds.KEY_SIGHT_SWITCH.isPressed())
            {
                PacketHandler.getPlayChannel().sendToServer(new MessageIronSightSwitch());
            }*/
            if(KeyBinds.KEY_ATTACHMENTS.isPressed())
            {
                PacketHandler.getPlayChannel().sendToServer(new MessageAttachments());
            }
            /*else if(KeyBinds.COLOR_BENCH.isPressed())
            {
                PacketHandler.getPlayChannel().sendToServer(new MessageColorBench());
            }*/
            else if(KeyBinds.KEY_INSPECT.isPressed())
            {
                PacketHandler.getPlayChannel().sendToServer(new MessageInspection());
            }
        }
    }

    /* Uncomment for debugging headshot hit boxes */

    /*@SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onRenderLiving(RenderLivingEvent.Post event)
    {
        LivingEntity entity = event.getEntity();
        IHeadshotBox<LivingEntity> headshotBox = (IHeadshotBox<LivingEntity>) BoundingBoxManager.getHeadshotBoxes(entity.getType());
        if(headshotBox != null)
        {
            AxisAlignedBB box = headshotBox.getHeadshotBox(entity);
            if(box != null)
            {
                WorldRenderer.drawBoundingBox(event.getMatrixStack(), event.getBuffers().getBuffer(RenderType.getLines()), box, 1.0F, 1.0F, 0.0F, 1.0F);

                AxisAlignedBB boundingBox = entity.getBoundingBox().offset(entity.getPositionVec().inverse());
                boundingBox = boundingBox.grow(Config.COMMON.gameplay.growBoundingBoxAmount.get(), 0, Config.COMMON.gameplay.growBoundingBoxAmount.get());
                WorldRenderer.drawBoundingBox(event.getMatrixStack(), event.getBuffers().getBuffer(RenderType.getLines()), boundingBox, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }*/
}
