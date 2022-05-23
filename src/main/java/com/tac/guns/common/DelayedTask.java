package com.tac.guns.common;

import com.tac.guns.Reference;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple system to run synchronized delayed tasks. See {@link #runAfter(int, Runnable)} to add
 * a delayed task.
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class DelayedTask
{
    public static List<Impl> tasks = new ArrayList<>();

    @SubscribeEvent
    public static void onServerStart(FMLServerStartedEvent event)
    {
        tasks.clear();
    }

    @SubscribeEvent
    public static void onServerStopping(FMLServerStoppingEvent event)
    {
        tasks.clear();
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
        {
            MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
            Iterator<Impl> it = tasks.iterator();
            while(it.hasNext())
            {
                Impl impl = it.next();
                if(impl.executionTick <= server.getTickCounter())
                {
                    impl.runnable.run();
                    it.remove();
                }
            }
        }
    }

    /**
     * Adds a new delayed task to the system.
     *
     * @param ticks the amount of ticks to delay the execution
     * @param run   a runnable get with the code to run
     */
    public static void runAfter(int ticks, Runnable run)
    {
        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        if(!server.isOnExecutionThread())
        {
            throw new IllegalStateException("Tried to add a delayed task off the main thread");
        }
        tasks.add(new Impl(server.getTickCounter() + ticks, run));
    }

    private static class Impl
    {
        private int executionTick;
        private Runnable runnable;

        private Impl(int executionTick, Runnable runnable)
        {
            this.executionTick = executionTick;
            this.runnable = runnable;
        }
    }
}
