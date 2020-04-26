package com.mrcrayfish.guns.client;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.event.BulletRenderer;
import com.mrcrayfish.guns.client.event.GunRenderer;
import com.mrcrayfish.guns.client.event.SoundEvents;
import com.mrcrayfish.guns.client.render.entity.RenderGrenade;
import com.mrcrayfish.guns.client.render.entity.RenderProjectile;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.render.gun.model.LongScopeModel;
import com.mrcrayfish.guns.client.render.gun.model.MediumScopeModel;
import com.mrcrayfish.guns.client.render.gun.model.ShortScopeModel;
import com.mrcrayfish.guns.client.settings.GunOptions;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.init.ModEntities;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.item.ColoredItem;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.message.MessageBullet;
import com.mrcrayfish.guns.network.message.MessageStunGrenade;
import com.mrcrayfish.guns.object.Bullet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MouseSettingsScreen;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ClientHandler
{
    private static Field mouseOptionsField;
    private static final GunRenderer GUN_RENDERER = new GunRenderer();
    private static final BulletRenderer BULLET_RENDERER = new BulletRenderer();

    public static GunRenderer getGunRenderer()
    {
        return GUN_RENDERER;
    }

    public static BulletRenderer getBulletRenderer()
    {
        return BULLET_RENDERER;
    }

    public static void setup()
    {
        MinecraftForge.EVENT_BUS.register(GUN_RENDERER);
        MinecraftForge.EVENT_BUS.register(BULLET_RENDERER);
        if(GunMod.controllableLoaded)
        {
            MinecraftForge.EVENT_BUS.register(new ControllerEvents());
        }
        KeyBinds.register();
        SoundEvents.initReflection();
        
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.PROJECTILE.get(), RenderProjectile::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GRENADE.get(), RenderProjectile::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.MISSILE.get(), RenderProjectile::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.THROWABLE_GRENADE.get(), RenderGrenade::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.THROWABLE_STUN_GRENADE.get(), RenderGrenade::new);

        IItemColor color = (stack, index) ->
        {
            if(index == 0 && stack.hasTag() && stack.getTag().contains("Color", Constants.NBT.TAG_INT))
            {
                return stack.getTag().getInt("Color");
            }
            return -1;
        };
        ForgeRegistries.ITEMS.forEach(item ->
        {
            if(item instanceof ColoredItem)
            {
                Minecraft.getInstance().getItemColors().register(color, item);
            }
        });

		//ModelOverrides.register(new ItemStack(ModItems.MINI_GUN), new MiniGunModel());
		ModelOverrides.register(new ItemStack(ModItems.SHORT_SCOPE.get()), new ShortScopeModel());
		ModelOverrides.register(new ItemStack(ModItems.MEDIUM_SCOPE.get()), new MediumScopeModel());
		ModelOverrides.register(new ItemStack(ModItems.LONG_SCOPE.get()), new LongScopeModel());

		/*WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.PISTOL), new DisplayProperty(0.0F, 0.55F, -0.25F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.SHOTGUN), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.RIFLE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.GRENADE_LAUNCHER), new DisplayProperty(0.0F, 0.55F, -0.1F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.BAZOOKA), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 2.5F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.MINI_GUN), new DisplayProperty(0.0F, 0.55F, 0.1F, 0.0F, 0.0F, 0.0F, 2.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.ASSAULT_RIFLE), new DisplayProperty(0.0F, 0.55F, -0.15F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.BASIC_AMMO), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.ADVANCED_AMMO), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.SHELL), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.GRENADE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.MISSILE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.STUN_GRENADE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.SMALL.ordinal()), new DisplayProperty(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.MEDIUM.ordinal()), new DisplayProperty(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.LONG.ordinal()), new DisplayProperty(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.SILENCER), new DisplayProperty(0.0F, 0.25F, 0.5F, 0.0F, 0.0F, 0.0F, 1.5F));*/

        //ModelOverrides.getModelMap().forEach((item, map) -> map.values().forEach(IOverrideModel::init));
    }

    public static void handleMessageBullet(MessageBullet message)
    {
        World world = Minecraft.getInstance().world;
        if(world != null)
        {
            Entity entity = world.getEntityByID(message.getEntityId());
            EntityProjectile projectile = (EntityProjectile) entity; //TODO test if this works. entity could be null but does java still cast?
            BULLET_RENDERER.addBullet(new Bullet(projectile, message));
        }
    }

    public static void handleExplosionStunGrenade(MessageStunGrenade message)
    {
        Minecraft mc = Minecraft.getInstance();
        ParticleManager particleManager = mc.particles;
        World world = mc.world;
        Random rand = world.rand;
        double x = message.getX();
        double y = message.getY();
        double z = message.getZ();

        /* Spawn lingering smoke particles */
        for(int i = 0; i < 30; i++)
        {
            spawnParticle(particleManager, ParticleTypes.CLOUD, x, y, z, world.rand, 0.2);
        }

        /* Spawn fast moving smoke/spark particles */
        for(int i = 0; i < 30; i++)
        {
            Particle smoke = spawnParticle(particleManager, ParticleTypes.SMOKE, x, y, z, world.rand, 4.0);
            smoke.setMaxAge((int) ((8 / (Math.random() * 0.1 + 0.4)) * 0.5));
            spawnParticle(particleManager, ParticleTypes.CRIT, x, y, z, world.rand, 4.0);
        }
    }

    private static Particle spawnParticle(ParticleManager manager, IParticleData data, double x, double y, double z, Random rand, double velocityMultiplier)
    {
        return manager.addParticle(data, x, y, z, (rand.nextDouble() - 0.5) * velocityMultiplier, (rand.nextDouble() - 0.5) * velocityMultiplier, (rand.nextDouble() - 0.5) * velocityMultiplier);
    }

    public static boolean isLookingAtInteractableBlock()
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.objectMouseOver != null && mc.world != null)
        {
            if(mc.objectMouseOver instanceof BlockRayTraceResult)
            {
                BlockRayTraceResult result = (BlockRayTraceResult) mc.objectMouseOver;
                BlockState state = mc.world.getBlockState(result.getPos());
                Block block = state.getBlock();
                return block instanceof ContainerBlock || block.hasTileEntity(state) || block == Blocks.CRAFTING_TABLE;
            }
        }
        return false;
    }

    public static boolean isAiming()
    {
        Minecraft mc = Minecraft.getInstance();
        if(!mc.isGameFocused())
            return false;

        if(mc.player == null)
            return false;

        if(mc.player.isSpectator())
            return false;

        if(!(mc.player.inventory.getCurrentItem().getItem() instanceof GunItem))
            return false;

        boolean zooming = GLFW.glfwGetMouseButton(mc.getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        if(GunMod.controllableLoaded)
        {
            Controller controller = Controllable.getController();
            if(controller != null)
            {
                zooming |= controller.getLTriggerValue() >= 0.5;
            }
        }

        return zooming;
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
                list.addOption(GunOptions.ADS_SENSITIVITY);
            }
            catch(IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }
}
