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
import com.mrcrayfish.guns.client.render.entity.RenderGrenade;
import com.mrcrayfish.guns.client.render.entity.RenderProjectile;
import com.mrcrayfish.guns.entity.EntityGrenade;
import com.mrcrayfish.guns.entity.EntityGrenadeStun;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.init.RegistrationHandler;
import com.mrcrayfish.guns.item.ItemColored;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
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

import java.awt.*;
import java.util.Random;

public class ClientProxy extends CommonProxy
{
	public static boolean controllableLoaded = false;

	@Override
	public void preInit()
	{
		super.preInit();

		MinecraftForge.EVENT_BUS.register(new RenderEvents());
		MinecraftForge.EVENT_BUS.register(new GunHandler());
		MinecraftForge.EVENT_BUS.register(new ReloadHandler());
		RenderingRegistry.registerEntityRenderingHandler(EntityProjectile.class, RenderProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, RenderGrenade::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityGrenadeStun.class, RenderGrenade::new);
		KeyBinds.register();
		SoundEvents.initReflection();

		if(Loader.isModLoaded("controllable"))
		{
			controllableLoaded = true;
			MinecraftForge.EVENT_BUS.register(new ControllerEvents());
		}
	}

	@Override
	public void init()
	{
		super.init();
		IItemColor color = (stack, index) ->
		{
			if(index == 0 && stack.hasTagCompound() && stack.getTagCompound().hasKey("color", Constants.NBT.TAG_INT))
			{
				return stack.getTagCompound().getInteger("color");
			}
			return Color.decode("#66FFFFFF").getRGB();
		};
		RegistrationHandler.Items.getItems().forEach(item ->
		{
			if(item instanceof ItemColored)
			{
				Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, item);
			}
		});
	}

	@Override
	public void showMuzzleFlash()
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		player.rotationPitch -= 0.4f;
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
		GunConfig.ID_TO_GUN.forEach((id, gun) -> gun.serverGun = null);
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
}
