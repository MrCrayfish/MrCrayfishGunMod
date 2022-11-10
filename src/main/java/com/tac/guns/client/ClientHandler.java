package com.tac.guns.client;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import com.tac.guns.Config;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.client.handler.AnimationHandler;
import com.tac.guns.client.handler.ArmorRenderingHandler;
import com.tac.guns.client.handler.BulletTrailRenderingHandler;
import com.tac.guns.client.handler.CrosshairHandler;
import com.tac.guns.client.handler.FireModeSwitchEvent;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.HUDRenderingHandler;
import com.tac.guns.client.handler.MovementAdaptationsHandler;
import com.tac.guns.client.handler.RecoilHandler;
import com.tac.guns.client.handler.ReloadHandler;
import com.tac.guns.client.handler.ScopeJitterHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.handler.SightSwitchEvent;
import com.tac.guns.client.handler.SoundHandler;
import com.tac.guns.client.handler.command.GuiEditor;
import com.tac.guns.client.handler.command.GunEditor;
import com.tac.guns.client.handler.command.ObjectRenderEditor;
import com.tac.guns.client.handler.command.ScopeEditor;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.entity.GrenadeRenderer;
import com.tac.guns.client.render.entity.MissileRenderer;
import com.tac.guns.client.render.entity.ProjectileRenderer;
import com.tac.guns.client.render.entity.ThrowableGrenadeRenderer;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.render.gun.model.ACOG_4x_ScopeModel;
import com.tac.guns.client.render.gun.model.AimpointT1SightModel;
import com.tac.guns.client.render.gun.model.CoyoteSightModel;
import com.tac.guns.client.render.gun.model.EotechNSightModel;
import com.tac.guns.client.render.gun.model.EotechShortSightModel;
import com.tac.guns.client.render.gun.model.LongRange8xScopeModel;
import com.tac.guns.client.render.gun.model.MicroHoloSightModel;
import com.tac.guns.client.render.gun.model.MiniDotSightModel;
import com.tac.guns.client.render.gun.model.OldLongRange4xScopeModel;
import com.tac.guns.client.render.gun.model.OldLongRange8xScopeModel;
import com.tac.guns.client.render.gun.model.Qmk152ScopeModel;
import com.tac.guns.client.render.gun.model.SLX_2X_ScopeModel;
import com.tac.guns.client.render.gun.model.SrsRedDotSightModel;
import com.tac.guns.client.render.gun.model.VortexLPVO_1_4xScopeModel;
import com.tac.guns.client.render.gun.model.VortexUh1SightModel;
import com.tac.guns.client.render.gun.model.elcan_14x_ScopeModel;
import com.tac.guns.client.screen.AmmoPackScreen;
import com.tac.guns.client.screen.AttachmentScreen;
import com.tac.guns.client.screen.InspectScreen;
import com.tac.guns.client.screen.TaCSettingsScreen;
import com.tac.guns.client.screen.UpgradeBenchScreen;
import com.tac.guns.client.screen.WorkbenchScreen;
import com.tac.guns.client.settings.GunOptions;
import com.tac.guns.client.util.UpgradeBenchRenderUtil;
import com.tac.guns.init.ModBlocks;
import com.tac.guns.init.ModContainers;
import com.tac.guns.init.ModEntities;
import com.tac.guns.init.ModItems;
import com.tac.guns.init.ModTileEntities;
import com.tac.guns.item.IColored;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageAttachments;
import com.tac.guns.network.message.MessageInspection;

import com.tac.guns.util.math.SecondOrderDynamics;
import de.javagl.jgltf.model.animation.AnimationRunner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.gui.screen.MouseSettingsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.VideoSettingsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ClientHandler
{
    private static Field mouseOptionsField;

    private static File keyBindsFile;
    
    public static void setup( Minecraft mc )
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
        MinecraftForge.EVENT_BUS.register(SightSwitchEvent.get()); // Still, as well an event, am uncertain on what to name it, in short handles upcoming advanced iron sights

        //MinecraftForge.EVENT_BUS.register(FlashlightHandler.get()); // Completely broken... Needs a full rework
        //MinecraftForge.EVENT_BUS.register(FloodLightSource.get());

        MinecraftForge.EVENT_BUS.register(ScopeJitterHandler.getInstance()); // All built by MayDayMemory part of the Timeless dev team, amazing work!!!!!!!!!!!
        MinecraftForge.EVENT_BUS.register(MovementAdaptationsHandler.get());
        MinecraftForge.EVENT_BUS.register(AnimationHandler.INSTANCE); //Mainly controls when the animation should play.
        MinecraftForge.EVENT_BUS.register(ArmorRenderingHandler.INSTANCE); //Test
        if(Config.COMMON.development.enableTDev.get()) {
            MinecraftForge.EVENT_BUS.register(GuiEditor.get());
            MinecraftForge.EVENT_BUS.register(GunEditor.get());
            MinecraftForge.EVENT_BUS.register(ScopeEditor.get());
            MinecraftForge.EVENT_BUS.register(ObjectRenderEditor.get());
        }

        //ClientRegistry.bindTileEntityRenderer(ModTileEntities.UPGRADE_BENCH.get(), UpgradeBenchRenderUtil::new);

        // Load key binds
        InputHandler.initKeys();
        keyBindsFile = new File( mc.gameDir, "config/tac-key-binds.txt" );
        if( !keyBindsFile.exists() )
        {
        	try { keyBindsFile.createNewFile(); }
        	catch( IOException e ) { GunMod.LOGGER.error( "Fail to create key binds file" ); }
        	InputHandler.saveTo( keyBindsFile );
        }
        else InputHandler.readFrom( keyBindsFile );

        setupRenderLayers();
        registerEntityRenders();
        registerColors();
        registerModelOverrides();
        registerScreenFactories();

        AnimationHandler.preloadAnimations();
        new AnimationRunner(); //preload thread pool
        new SecondOrderDynamics(1f,1f,1f, 1f); //preload thread pool
    }

    private static void setupRenderLayers()
    {
        //RenderTypeLookup.setRenderLayer(ModBlocks.UPGRADE_BENCH.get(), RenderType.getCutout());
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
        ModelOverrides.register(ModItems.SLX_2X.get(), new SLX_2X_ScopeModel());
        ModelOverrides.register(ModItems.ACOG_4.get(), new ACOG_4x_ScopeModel());
        ModelOverrides.register(ModItems.ELCAN_DR_14X.get(), new elcan_14x_ScopeModel());
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
        ScreenManager.registerFactory(ModContainers.UPGRADE_BENCH.get(), UpgradeBenchScreen::new);
        ScreenManager.registerFactory(ModContainers.ATTACHMENTS.get(), AttachmentScreen::new);
        ScreenManager.registerFactory(ModContainers.INSPECTION.get(), InspectScreen::new);
        ScreenManager.registerFactory(ModContainers.ARMOR_TEST.get(), AmmoPackScreen::new);
        //ScreenManager.registerFactory(ModContainers.COLOR_BENCH.get(), ColorBenchAttachmentScreen::new);
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
                /*, GunOptions.BURST_MECH);*/
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
        /*if(event.getGui() instanceof MainMenuScreen)
        {
            MainMenuScreen screen = (MainMenuScreen) event.getGui();

            event.addWidget((new Button(screen.width / 2 - 215, 10, 75, 20, new TranslationTextComponent("tac.options.gui_settings"), (p_213126_1_) -> {
                Minecraft.getInstance().displayGuiScreen(new TaCSettingsScreen(screen, Minecraft.getInstance().gameSettings));
            })));
        }
*/
    }
    
    private static Screen prevScreen = null;

    @SubscribeEvent
    public static void onGUIChange( GuiOpenEvent evt )
    {
    	final Screen gui = evt.getGui();
    	
    	// Show key binds if control GUI is activated
    	if( gui instanceof ControlsScreen )
    		InputHandler.restoreKeyBinds();
    	else if( prevScreen instanceof ControlsScreen )
    		InputHandler.clearKeyBinds( keyBindsFile );
    	
    	prevScreen = gui;
    }
    
    static
    {
    	InputHandler.ATTACHMENTS.addPressCallBack( () -> {
    		final Minecraft mc = Minecraft.getInstance();
    		if( mc.player != null && mc.currentScreen == null )
    			PacketHandler.getPlayChannel().sendToServer( new MessageAttachments() );
    	} );
    	
    	final Runnable callback = () -> {
    		final Minecraft mc = Minecraft.getInstance();
    		if(
    			mc.player != null
    			&& mc.currentScreen == null
    			&& GunAnimationController.fromItem(
    				Minecraft.getInstance().player.inventory.getCurrentItem().getItem()
    			) == null
    		) PacketHandler.getPlayChannel().sendToServer( new MessageInspection() );
    	};
    	InputHandler.INSPECT.addPressCallBack( callback );
    	InputHandler.CO_INSPECT.addPressCallBack( callback );
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
