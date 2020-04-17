package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.client.ControllerEvents;
import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.client.event.GunHandler;
import com.mrcrayfish.guns.client.event.GunRenderer;
import com.mrcrayfish.guns.client.event.ReloadHandler;
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
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.entity.EntityThrowableGrenade;
import com.mrcrayfish.guns.entity.EntityThrowableStunGrenade;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.init.RegistrationHandler;
import com.mrcrayfish.guns.item.*;
import com.mrcrayfish.guns.item.ColoredItem;
import com.mrcrayfish.guns.item.ScopeItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
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
	public static boolean controllableLoaded = false;

	@Override
	public void preInit()
	{
		super.preInit();

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
			if(item instanceof ColoredItem)
			{
				Minecraft.getInstance().getItemColors().registerItemColorHandler(color, item);
			}
		});

		ModelOverrides.register(new ItemStack(ModItems.MINI_GUN), new ModelChainGun());
		ModelOverrides.register(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.SMALL.ordinal()), new ModelShortScope());
		ModelOverrides.register(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.MEDIUM.ordinal()), new ModelMediumScope());
		ModelOverrides.register(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.LONG.ordinal()), new ModelLongScope());

		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.PISTOL), new DisplayProperty(0.0F, 0.55F, -0.25F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.SHOTGUN), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.RIFLE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.GRENADE_LAUNCHER), new DisplayProperty(0.0F, 0.55F, -0.1F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.BAZOOKA), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 2.5F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.MINI_GUN), new DisplayProperty(0.0F, 0.55F, 0.1F, 0.0F, 0.0F, 0.0F, 2.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.ASSAULT_RIFLE), new DisplayProperty(0.0F, 0.55F, -0.15F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.BASIC_AMMO), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.ADVANCED_AMMO), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.SHELL), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.GRENADE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.MISSILE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.STUN_GRENADE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.SMALL.ordinal()), new DisplayProperty(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.MEDIUM.ordinal()), new DisplayProperty(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.LONG.ordinal()), new DisplayProperty(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
		GuiWorkbench.addDisplayProperty(new ItemStack(ModItems.SILENCER), new DisplayProperty(0.0F, 0.25F, 0.5F, 0.0F, 0.0F, 0.0F, 1.5F));
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
		GunRenderer.drawFlash = true;
	}

	@Override
	public void playClientSound(SoundEvent sound)
	{
		Minecraft.getInstance().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(sound, 1.0F));
	}

	@SubscribeEvent
	public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
	{
		GunRegistry.getInstance().getGuns().forEach((location, gun) -> gun.getGun().serverGun = null);
	}


    private Particle createParticle(double x, double y, double z, World world, Random rand, IParticleFactory factory, double velocityMultiplier)
    {
        return factory.createParticle(0, world, x + (rand.nextDouble() - 0.5) * 0.6, y + (rand.nextDouble() - 0.5) * 0.6, z + (rand.nextDouble() - 0.5) * 0.6,
                (rand.nextDouble() - 0.5) * velocityMultiplier, (rand.nextDouble() - 0.5) * velocityMultiplier, (rand.nextDouble() - 0.5) * velocityMultiplier);
    }

	@Override
	public boolean canShoot()
	{
		return Config.CLIENT.controls.oldControls;
	}

	@Override
	public boolean isZooming()
	{
		Minecraft mc = Minecraft.getInstance();
		if(!mc.inGameHasFocus)
			return false;
		
		if(mc.player.isSpectator())
			return false;

		if(!(mc.player.inventory.getCurrentItem().getItem() instanceof GunItem))
			return false;

		boolean zooming = Config.CLIENT.controls.oldControls ? GuiScreen.isAltKeyDown() : Mouse.isButtonDown(1);
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
		gunRenderer.playAnimation = true;
	}

	public static boolean isLookingAtInteractBlock()
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.objectMouseOver != null)
		{
			if(mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				IBlockState state = mc.world.getBlockState(mc.objectMouseOver.getBlockPos());
				Block block = state.getBlock();
				if(block instanceof BlockContainer || block.hasTileEntity(state) || block == Blocks.CRAFTING_TABLE)
				{
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isLookingAtInteract()
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.objectMouseOver != null)
		{
			if(mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				IBlockState state = mc.world.getBlockState(mc.objectMouseOver.getBlockPos());
				Block block = state.getBlock();
				if(block instanceof BlockContainer || block.hasTileEntity(state) || block == Blocks.CRAFTING_TABLE)
				{
					return true;
				}
			}
			else if(mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY)
			{
				Entity entity = mc.objectMouseOver.entityHit;
				if(entity != null)
				{
					return true;
				}
			}
		}
		return false;
	}
}
