package com.mrcrayfish.guns.client;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.handler.*;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.render.gun.model.BazookaModel;
import com.mrcrayfish.guns.client.render.gun.model.GrenadeLauncherModel;
import com.mrcrayfish.guns.client.render.gun.model.LongScopeModel;
import com.mrcrayfish.guns.client.render.gun.model.MediumScopeModel;
import com.mrcrayfish.guns.client.render.gun.model.MiniGunModel;
import com.mrcrayfish.guns.client.render.gun.model.ShortScopeModel;
import com.mrcrayfish.guns.client.render.gun.model.SimpleModel;
import com.mrcrayfish.guns.client.screen.AttachmentScreen;
import com.mrcrayfish.guns.client.screen.WorkbenchScreen;
import com.mrcrayfish.guns.client.settings.GunOptions;
import com.mrcrayfish.guns.init.ModBlocks;
import com.mrcrayfish.guns.init.ModContainers;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.item.IColored;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.MouseSettingsScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.Tag;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;

/**
 * Author: MrCrayfish
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
        MinecraftForge.EVENT_BUS.register(new PlayerModelHandler());

        /* Only register controller events if Controllable is loaded otherwise it will crash */
        if(GunMod.controllableLoaded)
        {
            MinecraftForge.EVENT_BUS.register(new ControllerHandler());
            GunButtonBindings.register();
        }

        setupRenderLayers();
        registerColors();
        registerModelOverrides();
        registerScreenFactories();

        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if(resourceManager instanceof ReloadableResourceManager)
        {
            ((ReloadableResourceManager) resourceManager).registerReloadListener((stage, manager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) ->
            {
                return stage.wait(Unit.INSTANCE).thenRunAsync(SpecialModels::clearCache);
            });
        }
    }

    private static void setupRenderLayers()
    {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WORKBENCH.get(), RenderType.cutout());
    }

    private static void registerColors()
    {
        ItemColor color = (stack, index) ->
        {
            if(!IColored.isDyeable(stack))
            {
                return -1;
            }
            if(index == 0 && stack.hasTag() && stack.getTag().contains("Color", Tag.TAG_INT))
            {
                return stack.getTag().getInt("Color");
            }
            if(index == 0 && stack.getItem() instanceof IAttachment)
            {
                ItemStack renderingWeapon = GunRenderingHandler.get().getRenderingWeapon();
                if(renderingWeapon != null)
                {
                    return Minecraft.getInstance().getItemColors().getColor(renderingWeapon, index);
                }
            }
            return -1;
        };
        ForgeRegistries.ITEMS.forEach(item ->
        {
            if(item instanceof IColored)
            {
                Minecraft.getInstance().getItemColors().register(color, item);
            }
        });
    }

    private static void registerModelOverrides()
    {
        /* Weapons */
        ModelOverrides.register(ModItems.ASSAULT_RIFLE.get(), new SimpleModel(SpecialModels.ASSAULT_RIFLE::getModel));
        ModelOverrides.register(ModItems.BAZOOKA.get(), new BazookaModel(SpecialModels.BAZOOKA::getModel));
        ModelOverrides.register(ModItems.GRENADE_LAUNCHER.get(), new GrenadeLauncherModel());
        ModelOverrides.register(ModItems.HEAVY_RIFLE.get(), new SimpleModel(SpecialModels.HEAVY_RIFLE::getModel));
        ModelOverrides.register(ModItems.MACHINE_PISTOL.get(), new SimpleModel(SpecialModels.MACHINE_PISTOL::getModel));
        ModelOverrides.register(ModItems.MINI_GUN.get(), new MiniGunModel());
        ModelOverrides.register(ModItems.PISTOL.get(), new SimpleModel(SpecialModels.PISTOL::getModel));
        ModelOverrides.register(ModItems.RIFLE.get(), new SimpleModel(SpecialModels.RIFLE::getModel));
        ModelOverrides.register(ModItems.SHOTGUN.get(), new SimpleModel(SpecialModels.SHOTGUN::getModel));

        /* Attachments */
        ModelOverrides.register(ModItems.SHORT_SCOPE.get(), new ShortScopeModel());
        ModelOverrides.register(ModItems.MEDIUM_SCOPE.get(), new MediumScopeModel());
        ModelOverrides.register(ModItems.LONG_SCOPE.get(), new LongScopeModel());
    }

    private static void registerScreenFactories()
    {
        MenuScreens.register(ModContainers.WORKBENCH.get(), WorkbenchScreen::new);
        MenuScreens.register(ModContainers.ATTACHMENTS.get(), AttachmentScreen::new);
    }

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event)
    {
        if(event.getScreen() instanceof MouseSettingsScreen screen)
        {
            if(mouseOptionsField == null)
            {
                mouseOptionsField = ObfuscationReflectionHelper.findField(MouseSettingsScreen.class, "f_96218_");
                mouseOptionsField.setAccessible(true);
            }
            try
            {
                OptionsList list = (OptionsList) mouseOptionsField.get(screen);
                //list.addSmall(GunOptions.ADS_SENSITIVITY, GunOptions.CROSSHAIR);
            }
            catch(IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && mc.screen == null)
        {
            if(KeyBinds.KEY_ATTACHMENTS.consumeClick())
            {
                PacketHandler.getPlayChannel().sendToServer(new MessageAttachments());
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
