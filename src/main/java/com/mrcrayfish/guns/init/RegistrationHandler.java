package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.ISubItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class RegistrationHandler
{
    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class Items
    {
        private static final List<Item> ITEMS = new LinkedList<>();

        static void add(Item item)
        {
            ITEMS.add(item);
        }

        @SubscribeEvent
        public static void register(final RegistryEvent.Register<Item> event)
        {
            ITEMS.forEach(item -> event.getRegistry().register(item));
        }
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Side.CLIENT)
    public static class Models
    {
        @SubscribeEvent
        public static void register(ModelRegistryEvent event)
        {
            Items.ITEMS.forEach(Models::registerRender);
        }

        private static void registerRender(Item item)
        {
            if(item instanceof ISubItems)
            {
                NonNullList<ResourceLocation> modelLocations = ((ISubItems) item).getModels();
                for(int i = 0; i < modelLocations.size(); i++)
                {
                    ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(modelLocations.get(i), "inventory"));
                }
            }
            else
            {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class Sounds
    {
        private static final List<SoundEvent> SOUNDS = new LinkedList<>();

        static void add(SoundEvent sound)
        {
            SOUNDS.add(sound);
        }

        @SubscribeEvent
        public static void registerSounds(final RegistryEvent.Register<SoundEvent> event)
        {
            SOUNDS.forEach(sound -> event.getRegistry().register(sound));
        }
    }
}
