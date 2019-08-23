package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.client.ControllerEvents;
import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.client.event.GunHandler;
import com.mrcrayfish.guns.client.event.ReloadHandler;
import com.mrcrayfish.guns.client.event.RenderEvents;
import com.mrcrayfish.guns.client.event.SoundEvents;
import com.mrcrayfish.guns.client.gui.DisplayProperty;
import com.mrcrayfish.guns.client.gui.GuiWorkbench;
import com.mrcrayfish.guns.client.render.entity.RenderGrenade;
import com.mrcrayfish.guns.client.render.entity.RenderProjectile;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.render.gun.model.ModelChainGun;
import com.mrcrayfish.guns.client.render.gun.model.ModelLongScope;
import com.mrcrayfish.guns.client.render.gun.model.ModelMediumScope;
import com.mrcrayfish.guns.client.render.gun.model.ModelShortScope;
import com.mrcrayfish.guns.entity.*;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.RegistrationHandler;
import com.mrcrayfish.guns.item.GunRegistry;
import com.mrcrayfish.guns.item.ItemAmmo;
import com.mrcrayfish.guns.item.ItemColored;
import com.mrcrayfish.guns.item.ItemScope;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Mouse;

import java.util.Random;

public class ClientProxy extends CommonProxy
{
	public static RenderEvents renderEvents;
	public static boolean controllableLoaded = false;

	@Override
	public void preInit()
	{
		super.preInit();

		MinecraftForge.EVENT_BUS.register(renderEvents = new RenderEvents());
		MinecraftForge.EVENT_BUS.register(new GunHandler());
		MinecraftForge.EVENT_BUS.register(new ReloadHandler());

		RenderingRegistry.registerEntityRenderingHandler(EntityProjectile.class, RenderProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityThrowableGrenade.class, RenderGrenade::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityThrowableStunGrenade.class, RenderGrenade::new);

		KeyBinds.register();

		SoundEvents.initReflection();

		if(Loader.isModLoaded("controllable"))
		{
			controllableLoaded = true;
			MinecraftForge.EVENT_BUS.register(new ControllerEvents());
		}
	}

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public void init()
	{
		super.init();
		IItemColor color = (stack, index) ->
		{
			if(index == 0 && stack.hasTagCompound() && stack.getTagCompound().hasKey("color", Constants.NBT.TAG_INT))
			{
				return stack.getTagCompound().getInteger("color");
			}
			return -1;
		};
		RegistrationHandler.Items.getItems().forEach(item ->
		{
			if(item instanceof ItemColored)
			{
				Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, item);
			}
		});

		ModelOverrides.register(new ItemStack(ModGuns.CHAIN_GUN), new ModelChainGun());
		ModelOverrides.register(new ItemStack(ModGuns.SCOPES, 1, ItemScope.Type.SMALL.ordinal()), new ModelShortScope());
		ModelOverrides.register(new ItemStack(ModGuns.SCOPES, 1, ItemScope.Type.MEDIUM.ordinal()), new ModelMediumScope());
		ModelOverrides.register(new ItemStack(ModGuns.SCOPES, 1, ItemScope.Type.LONG.ordinal()), new ModelLongScope());

		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.PISTOL), new DisplayProperty(0.0F, 0.55F, -0.25F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.SHOTGUN), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.RIFLE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.GRENADE_LAUNCHER), new DisplayProperty(0.0F, 0.55F, -0.1F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.BAZOOKA), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 2.5F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.CHAIN_GUN), new DisplayProperty(0.0F, 0.55F, 0.1F, 0.0F, 0.0F, 0.0F, 2.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.ASSAULT_RIFLE), new DisplayProperty(0.0F, 0.55F, -0.15F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.BASIC_AMMO), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.ADVANCED_AMMO), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.SHELL), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.GRENADE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.MISSILE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.STUN_GRENADE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.SCOPES, 1, ItemScope.Type.SMALL.ordinal()), new DisplayProperty(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.SCOPES, 1, ItemScope.Type.MEDIUM.ordinal()), new DisplayProperty(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.SCOPES, 1, ItemScope.Type.LONG.ordinal()), new DisplayProperty(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModGuns.SILENCER), new DisplayProperty(0.0F, 0.25F, 0.5F, 0.0F, 0.0F, 0.0F, 1.5F));
	}

	@Override
	public void postInit()
	{
		super.postInit();
		ModelOverrides.getModelMap().forEach((item, map) -> map.values().forEach(IOverrideModel::init));
	}

	@Override
	public void showMuzzleFlash()
	{
		RenderEvents.drawFlash = true;
	}

	@Override
	public void playClientSound(SoundEvent sound)
	{
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(sound, 1.0F));
	}

	@Override
	public void playClientSound(double posX, double posY, double posZ, SoundEvent event, SoundCategory category, float volume, float pitch)
	{
		ISound sound = new PositionedSoundRecord(event.getSoundName(), category, volume, pitch, false, 0, ISound.AttenuationType.NONE, 0, 0, 0);
		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
	}

	@SubscribeEvent
	public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
	{
		GunRegistry.getInstance().getGuns().forEach((location, gun) -> gun.getGun().serverGun = null);
	}

	@Override
	public void createExplosionStunGrenade(double x, double y, double z)
	{
        ParticleManager particleManager = Minecraft.getMinecraft().effectRenderer;
        World world = Minecraft.getMinecraft().world;
        Random rand = world.rand;

        // Spawn lingering smoke particles
        for (int i = 0; i < 30; i++)
            particleManager.addEffect(createParticle(x, y, z, world, rand, new ParticleCloud.Factory(), 0.2));

        // Spawn fast moving smoke/spark particles
        for (int i = 0; i < 30; i++)
        {
            Particle smoke = createParticle(x, y, z, world, rand, new ParticleCloud.Factory(), 4);
            smoke.setMaxAge((int)((8 / (Math.random() * 0.1 + 0.4)) * 0.5));
            particleManager.addEffect(smoke);
            particleManager.addEffect(createParticle(x, y, z, world, rand, new ParticleCrit.Factory(), 4));
        }
	}

    private Particle createParticle(double x, double y, double z, World world, Random rand, IParticleFactory factory, double velocityMultiplier)
    {
        return factory.createParticle(0, world, x + (rand.nextDouble() - 0.5) * 0.6, y + (rand.nextDouble() - 0.5) * 0.6, z + (rand.nextDouble() - 0.5) * 0.6,
                (rand.nextDouble() - 0.5) * velocityMultiplier, (rand.nextDouble() - 0.5) * velocityMultiplier, (rand.nextDouble() - 0.5) * velocityMultiplier);
    }

    @Override
	public boolean isZooming()
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(!mc.inGameHasFocus)
			return false;
		
		if(mc.player.isSpectator())
			return false;

		boolean zooming = GunConfig.CLIENT.controls.oldControls ? GuiScreen.isAltKeyDown() : Mouse.isButtonDown(1);
		if(controllableLoaded)
		{
			Controller controller = Controllable.getController();
			if(controller != null)
			{
				zooming |= controller.getLTriggerValue() >= 0.5;
			}
		}
		return zooming;
	}

	@Override
	public void startReloadAnimation()
	{
		renderEvents.playAnimation = true;
	}
}
