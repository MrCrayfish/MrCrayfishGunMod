package com.mrcrayfish.guns.init;

import java.util.ArrayList;
import java.util.List;

import com.mrcrayfish.guns.potion.PotionIncurable;

import net.minecraft.potion.Potion;

public class ModPotions
{
    private static final List<Potion> POTIONS = new ArrayList<>();

    public static final Potion BLINDED;
    public static final Potion DEAFENED;

    static
    {
        BLINDED = add(new PotionIncurable(true, true, 0, "blinded"));
        DEAFENED = add(new PotionIncurable(true, true, 0, "deafened"));
    }

    private static Potion add(Potion potion)
    {
        POTIONS.add(potion);
        return potion;
    }

    public static void register()
    {
        POTIONS.forEach(potion -> RegistrationHandler.Potions.add(potion));
    }
}