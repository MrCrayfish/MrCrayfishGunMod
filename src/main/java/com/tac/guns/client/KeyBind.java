package com.tac.guns.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.util.InputMappings.Input;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Helps to fix key conflict issue. Adapted solution from FMUM.
 * 
 * @see InputHandler
 * @author Giant_Salted_Fish
 */
@OnlyIn( Dist.CLIENT )
public final class KeyBind
{
	public static final HashMap< String, KeyBind > REGISTRY = new HashMap<>();

	private static long getHandle(){
		long value;
		try{
			value = Minecraft.getInstance().getMainWindow().getHandle();
		}
		catch (NullPointerException e){
			value = 0;
		}
		return value;
	}

	private static final Function< Integer, Boolean >
		UPDATER_KEYBOARD = key -> GLFW.glfwGetKey( getHandle(), key ) == GLFW.GLFW_PRESS;
	
	private static final Function< Integer, Boolean >
		UPDATER_MOUSE = key -> GLFW.glfwGetMouseButton( getHandle(), key ) == GLFW.GLFW_PRESS;
	
	private static final Function< Integer, Boolean > UPDATER_NONE = key -> false;
	
	/**
	 * Whether this key is continuously down or not
	 */
	public boolean down = false;
	
	/**
	 * Current bounden key
	 */
	private Input keyCode;
	
	/**
	 * Invoked in {@link #update()} to check whether the bounden key is down or not. This helps to
	 * avoid the if branch that checks whether the bounden key is from keyboard or the mouse.
	 */
	private Function< Integer, Boolean > updater;
	
	/**
	 * The corresponding vanilla key bind object. Its key bind will be set to
	 * {@link InputMappings#INPUT_INVALID} to avoid key conflict in game.
	 */
	private KeyBinding keyBind;
	
	private final LinkedList< Runnable > pressCallbacks = new LinkedList<>();
	
	/**
	 * Use {@link #KeyBind(String, String, int, Type...)} if you want to specify key category
	 * 
	 * @param inputType Will be {@link Type#KEYSYM} if not present
	 */
	KeyBind( String name, int keyCode, Type... inputType ) {
		this( name, "key.categories.tac", keyCode, inputType );
	}

	public int getKeyCode() {
		return keyCode.getKeyCode();
	}
	
	/**
	 * @see #KeyBind(String, int, Type...)
	 */
	KeyBind( String name, String category, int keyCode, Type... inputType )
	{
		this.$keyCode(
			keyCode >= 0
			? ( inputType.length > 0 ? inputType[ 0 ] : Type.KEYSYM ).getOrMakeInput( keyCode )
			: InputMappings.INPUT_INVALID
		);
		this.keyBind = new KeyBinding(
			name,
			GunConflictContext.IN_GAME_HOLDING_WEAPON,
			this.keyCode,
			category
		);
		
		// Bind to none to avoid conflict
		this.keyBind.bind( InputMappings.INPUT_INVALID );
		
		REGISTRY.put( this.name(), this );
	}
	
	public String name() { return this.keyBind.getKeyDescription(); }
	
	public Input keyCode() { return this.keyCode; }
	
	public void $keyCode( Input code )
	{
		this.keyCode = code;
		if( code == InputMappings.INPUT_INVALID )
			this.updater = UPDATER_NONE;
		else switch( code.getType() )
		{
		case KEYSYM:
			this.updater = UPDATER_KEYBOARD;
			break;
		case MOUSE:
			this.updater = UPDATER_MOUSE;
			break;
		case SCANCODE:
			// TODO: what this means?
		}
	}
	
	/**
	 * Add a callback that will be invoked on the press of this key. It will only be invoked once
	 * until the key is released and pressed again.
	 */
	public void addPressCallback( Runnable callback ) { this.pressCallbacks.add( callback ); }
	
	void update()
	{
		if( !this.updater.apply( this.keyCode.getKeyCode() ) )
			this.down = false;
		else if( !this.down )
		{
			// Previously not pressed, update #down and fire callbacks
			this.down = true;
			this.pressCallbacks.forEach( Runnable::run );
		}
	}
	
	void reset() { this.down = false; }
	
	void restoreKeyBind() { this.keyBind.bind( this.keyCode ); }
	
	boolean clearKeyBind()
	{
		final Input code = this.keyBind.getKey();
		if( code == this.keyCode ) return false;
		
		// Key bind has been changed, update it
		this.$keyCode( code );
		this.keyBind.bind( InputMappings.INPUT_INVALID );
		return true;
	}
	
	void regis() { ClientRegistry.registerKeyBinding( this.keyBind ); }
}
