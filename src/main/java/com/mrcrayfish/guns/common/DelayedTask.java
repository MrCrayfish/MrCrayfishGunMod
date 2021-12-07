package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.Reference;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple system to run synchronized delayed tasks. See {@link #runAfter(int, Runnable)} to add
 * a delayed task.
 * <p>
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class DelayedTask
{
    public static List<Impl> tasks = new ArrayList<>();

    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event)
    {
        tasks.clear();
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event)
    {
        tasks.clear();
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
        {
            MinecraftServer server = (MinecraftServer) LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
            Iterator<Impl> it = tasks.iterator();
            while(it.hasNext())
            {
                Impl impl = it.next();
                if(impl.executionTick <= server.getTickCount())
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
        MinecraftServer server = (MinecraftServer) LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
        if(!server.isSameThread())
        {
            throw new IllegalStateException("Tried to add a delayed task off the main thread");
        }
        tasks.add(new Impl(server.getTickCount() + ticks, run));
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
