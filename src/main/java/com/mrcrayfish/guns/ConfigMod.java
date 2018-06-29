package com.mrcrayfish.guns;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID)
@LangKey(Reference.MOD_ID + ".config.title")
@EventBusSubscriber
public class ConfigMod
{
	@Name("Server")
	@Comment("Server-only configs.")
	@LangKey(Reference.MOD_ID + ".config.server")
	public static final Server SERVER = new Server();

	public static class Server
	{
		@Name("Aggro Mobs")
		@Comment("Nearby mobs are angered and/or scared by the firing of guns.")
		@LangKey(Reference.MOD_ID + ".config.server.aggro")
		public boolean aggroMobs = true;

		@Name("Aggro Range Silenced")
		@Comment("Any mobs within a sphere of this radius will aggro on the shooter of a silenced gun.")
		@LangKey(Reference.MOD_ID + ".config.server.aggro.silenced")
		@RangeDouble(min = 0)
		public double rangeSilenced = 10;

		@Name("Aggro Range Unsilenced")
		@Comment("Any mobs within a sphere of this radius will aggro on the shooter of an unsilenced gun.")
		@LangKey(Reference.MOD_ID + ".config.server.aggro.unsilenced")
		@RangeDouble(min = 0)
		public double rangeUnsilenced = 20;
	}

	@SubscribeEvent
	public static void onConfigChanged(OnConfigChangedEvent event)
	{
		if (event.getModID().equalsIgnoreCase(Reference.MOD_ID))
			ConfigManager.sync(Reference.MOD_ID, Type.INSTANCE);
	}
}
