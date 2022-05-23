package com.tac.guns.integration;

import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import mod.chiselsandbits.api.plugin.ChiselsAndBitsPlugin;
import mod.chiselsandbits.api.plugin.IChiselsAndBitsPlugin;

@ChiselsAndBitsPlugin(requiredMods = Reference.MOD_ID)
public class CABPlugin implements IChiselsAndBitsPlugin
{
    @Override
    public String getId()
    {
        return Reference.MOD_ID;
    }

    @Override
    public void onConstruction()
    {
        GunMod.cabLoaded = true;
    }
}
