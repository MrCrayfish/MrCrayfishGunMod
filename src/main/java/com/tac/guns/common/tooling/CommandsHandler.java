package com.tac.guns.common.tooling;

import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static com.tac.guns.GunMod.LOGGER;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class CommandsHandler
{
    private static CommandsHandler instance;

    public static CommandsHandler get()
    {
        if(instance == null)
        {
            instance = new CommandsHandler();
        }
        // ---MANUAL, CURRENT SYSTEM FOR IMPLEMENTING DEFAULT HANDLERS--- ///

        // WEAPON EDITING TRACKER
        instance.catGlobals.put(1, new HashMap<String, Object>());

        /*for (Field field : ModItems.class.getDeclaredFields()) {
            RegistryObject<?> object;
            try {
                object = (RegistryObject<?>) field.get(null);
            } catch (ClassCastException | IllegalAccessException e) {
                continue;
            }
            if (TimelessGunItem.class.isAssignableFrom(object.get().getClass()))
            {
                instance.catGlobals.get(1).put(field.getTagName().toLowerCase(Locale.ENGLISH), new HashMap<String, Object>());
            }
        }*/

        // WEAPON EDITING TRACKER   |   END

        instance.catGlobals.put(2, new HashMap<String, Object>()); // GUI EDITOR

        // ---MANUAL, CURRENT SYSTEM FOR IMPLEMENTING DEFAULT HANDLERS   |   END--- ///
        return instance;
    }

    private CommandsHandler() {}

    private HashMap<Integer, HashMap<String, Object>> catGlobals = new HashMap<>();
    private int catCurrentIndex = 0;

    public void setCatCurrentIndex(int catCurrentIndex) {this.catCurrentIndex = catCurrentIndex;}
    public int getCatCurrentIndex() {return this.catCurrentIndex;}
    public boolean catInGlobal(int index) {return catGlobals.containsKey(Integer.valueOf(index));}
    public HashMap<String, Object> getCatGlobal(int i) {return catGlobals.get(i);}
    public boolean putCatGlobals(int index, HashMap<String, Object> data)
    {
        if(this.catGlobals.containsKey(index))
            return false;
        this.catGlobals.put(index, data);
        return true;
    }
}