package com.tac.guns.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.tac.guns.Config;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.RawMouseEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * Static handler for {@link KeyBind}s
 * 
 * @author Giant_Salted_Fish
 */
@OnlyIn( Dist.CLIENT )
@EventBusSubscriber( modid = Reference.MOD_ID, value = Dist.CLIENT )
public final class InputHandler
{
	// FIXME: adapt translation for these keys
	/**
	 * Universal keys. These keys will always update
	 */
	public static final KeyBind
		PULL_TRIGGER = new KeyBind(
			"key.tac.pull_trigger",
			GLFW.GLFW_MOUSE_BUTTON_LEFT,
			Type.MOUSE
		),
		AIM_HOLD = new KeyBind( "key.tac.aim_hold", GLFW.GLFW_MOUSE_BUTTON_RIGHT, Type.MOUSE ),
		AIM_TOGGLE = new KeyBind( "key.tac.aim_toggle", InputMappings.INPUT_INVALID.getKeyCode() );
	
	/**
	 * Normal keys. These keys will update when {@link #CO} is not down.
	 */
	public static final KeyBind
		RELOAD = new KeyBind( "key.tac.reload", GLFW.GLFW_KEY_R ),
		UNLOAD = new KeyBind( "key.tac.unload", InputMappings.INPUT_INVALID.getKeyCode() ),
		ATTACHMENTS = new KeyBind( "key.tac.attachments", GLFW.GLFW_KEY_Z ),
		
		FIRE_SELECT = new KeyBind( "key.tac.fireSelect", GLFW.GLFW_KEY_G ),
		INSPECT = new KeyBind( "key.tac.inspect", GLFW.GLFW_KEY_H ),
		SIGHT_SWITCH = new KeyBind( "key.tac.sight_switch", GLFW.GLFW_KEY_V ),
		ACTIVATE_SIDE_RAIL = new KeyBind( "key.tac.activateSideRail", GLFW.GLFW_KEY_B );
		
		// TODO: remove this key maybe? At least not used now.
//		COLOR_BENCH = new KeyBind( "key.tac.color_bench", GLFW.GLFW_KEY_PAGE_DOWN );
	
	/**
	 * Co-keys. These keys will update when {@link #CO} is down.
	 */
	public static final KeyBind
		CO = new KeyBind( "key.tac.co", GLFW.GLFW_KEY_LEFT_ALT ),
		CO_UNLOAD = new KeyBind( "key.tac.co_unload", GLFW.GLFW_KEY_R ),
		CO_INSPECT = new KeyBind( "key.tac.co_inspect", -1 );
	
	/**
	 * These keys are development only
	 */
	public static final KeyBind
		SHIFTY = new KeyBind( "key.tac.ss", GLFW.GLFW_KEY_LEFT_SHIFT ),
		CONTROLLY = new KeyBind( "key.tac.cc", GLFW.GLFW_KEY_LEFT_CONTROL ),
		ALTY = new KeyBind( "key.tac.aa", GLFW.GLFW_KEY_LEFT_ALT ),
		SHIFTYR = new KeyBind( "key.tac.ssr", GLFW.GLFW_KEY_RIGHT_SHIFT ),
		CONTROLLYR = new KeyBind( "key.tac.ccr", GLFW.GLFW_KEY_RIGHT_CONTROL ),
		ALTYR = new KeyBind( "key.tac.aar", GLFW.GLFW_KEY_RIGHT_ALT ),
		SIZE_OPT = new KeyBind( "key.tac.sizer", GLFW.GLFW_KEY_PERIOD ),
		
		P = new KeyBind( "key.tac.p", GLFW.GLFW_KEY_P ),
		L = new KeyBind( "key.tac.l", GLFW.GLFW_KEY_L ),
		O = new KeyBind( "key.tac.o", GLFW.GLFW_KEY_O ),
		K = new KeyBind( "key.tac.k", GLFW.GLFW_KEY_K ),
		M = new KeyBind( "key.tac.m", GLFW.GLFW_KEY_M ),
		I = new KeyBind( "key.tac.i", GLFW.GLFW_KEY_I ),
		J = new KeyBind( "key.tac.j", GLFW.GLFW_KEY_J ),
		N = new KeyBind( "key.tac.n", GLFW.GLFW_KEY_N );
	
	private static final ArrayList< KeyBind > UNIVERSAL_KEYS = new ArrayList<>();
	
	private static final ArrayList< KeyBind > NORMAL_KEYS = new ArrayList<>();
	
	private static final ArrayList< KeyBind > CO_KEYS = new ArrayList<>();
	
	static
	{
		regisAll(
			UNIVERSAL_KEYS,
			
			PULL_TRIGGER,
			AIM_HOLD,
			AIM_TOGGLE,
			CO
		);
		
		regisAll(
			NORMAL_KEYS,
			
			RELOAD,
			UNLOAD,
			ATTACHMENTS,
			FIRE_SELECT,
			INSPECT,
			SIGHT_SWITCH,
			ACTIVATE_SIDE_RAIL
//			, COLOR_BENCH
		);
		
		regisAll(
			CO_KEYS,
			
			CO_UNLOAD,
			CO_INSPECT
		);
		
		// Only register dev keys for dev mode
		if( Config.COMMON.development.enableTDev.get() )
		{
			regisAll(
				UNIVERSAL_KEYS,
				
				SHIFTY,
				CONTROLLY,
				ALTY,
				SHIFTYR,
				CONTROLLYR,
				ALTYR,
				SIZE_OPT,
				
				P, L, O, K, M, I, J, N
			);
		}
	}
	
	/**
	 * Original TAC implementation seems to try to prevent gun from destroying block and bobbing on
	 * use by canceling the {@link RawMouseEvent} event. Hence to receive the update, we need a
	 * higher priority to receive event ahead. But I have to say this kind of implementation is a
	 * bit of ugly. There are actually methods on {@link Item} that can be override to control the
	 * behavior of item on mouse input.
	 * 
	 * TODO: maybe refactor this part
	 */
	@SubscribeEvent( priority = EventPriority.HIGH )
	public static void onMouseInput( InputEvent.RawMouseEvent evt )
	{
		UNIVERSAL_KEYS.forEach( KeyBind::update );
		( CO.down ? CO_KEYS : NORMAL_KEYS ).forEach( KeyBind::update );
		( CO.down ? NORMAL_KEYS : CO_KEYS ).forEach( KeyBind::reset );
	}
	
	@SubscribeEvent
	public static void onKeyInput( InputEvent.KeyInputEvent evt )
	{
		UNIVERSAL_KEYS.forEach( KeyBind::update );
		( CO.down ? CO_KEYS : NORMAL_KEYS ).forEach( KeyBind::update );
		( CO.down ? NORMAL_KEYS : CO_KEYS ).forEach( KeyBind::reset );
	}
	
	private static KeyBind oriAimKey;
	static void restoreKeyBinds()
	{
		oriAimKey = AIM_HOLD.keyCode() != InputMappings.INPUT_INVALID ? AIM_HOLD : AIM_TOGGLE;
		KeyBind.REGISTRY.values().forEach( KeyBind::restoreKeyBind );
	}
	
	static void clearKeyBinds( File file )
	{
		boolean flag = false;
		for( KeyBind key : KeyBind.REGISTRY.values())
			flag |= key.clearKeyBind();
		
		// Make sure only one aim key is bounden
		if(
			AIM_HOLD.keyCode() != InputMappings.INPUT_INVALID
			&& AIM_TOGGLE.keyCode() != InputMappings.INPUT_INVALID
		) {
			( oriAimKey == AIM_HOLD ? AIM_HOLD : AIM_TOGGLE )
				.$keyCode( InputMappings.INPUT_INVALID );
			flag = true;
		}
		
		// Do not forget to update key bind hash
		KeyBinding.resetKeyBindingArrayAndHash();
		
		// If any key bind has changed, save it to the file
		if( flag )
			saveTo( file );
	}
	
	static void saveTo( File file )
	{
		try( BufferedWriter out = new BufferedWriter( new FileWriter( file ) ) )
		{
			for( KeyBind key : KeyBind.REGISTRY.values() )
			{
				out.write( key.name() + "=" + key.keyCode() );
				out.newLine();
			}
		}
		catch( IOException e ) { GunMod.LOGGER.error( "Fail write key bindings", e ); }
	}
	
	static void readFrom( File file )
	{
		try( BufferedReader in = new BufferedReader( new FileReader( file ) ) )
		{
			for( String line; ( line = in.readLine() ) != null; )
			{
				final int i = line.indexOf( '=' );
				try
				{
					KeyBind.REGISTRY.get( line.substring( 0, i ) ).$keyCode(
						InputMappings.getInputByName( line.substring( i + 1 ) )
					);
				}
				catch( NullPointerException e ) {
					GunMod.LOGGER.error( "Key bind " + line + " do not exist");
				}
				catch( StringIndexOutOfBoundsException | NumberFormatException e ) {
					GunMod.LOGGER.error( "Key code format broken: " + line );
				}
			}
		}
		catch( IOException e ) { GunMod.LOGGER.error( "Fail read key bind", e ); }
	}
	
	private static void regisAll( Collection< KeyBind > updateGroup, KeyBind... keys )
	{
		final List< KeyBind > keyList = Arrays.asList( keys );
		
		keyList.forEach( KeyBind::regis );
		updateGroup.addAll( keyList );
	}
}
