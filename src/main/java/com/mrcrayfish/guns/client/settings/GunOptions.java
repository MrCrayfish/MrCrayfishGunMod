package com.mrcrayfish.guns.client.settings;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.Crosshair;
import com.mrcrayfish.guns.client.event.AimingHandler;
import com.mrcrayfish.guns.client.event.CrosshairHandler;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class GunOptions
{
    private static final DecimalFormat FORMAT = new DecimalFormat("0.0#");

    public static final SliderPercentageOption ADS_SENSITIVITY = new GunSliderPercentageOption("cgm.options.adsSensitivity", 0.0, 2.0, 0.01F, gameSettings -> {
        return GunMod.getOptions().adsSensitivity;
    }, (gameSettings, value) -> {
        GunMod.getOptions().adsSensitivity = MathHelper.clamp(value, 0.0, 2.0);
    }, (gameSettings, option) -> {
        double adsSensitivity = GunMod.getOptions().adsSensitivity;
        return new TranslationTextComponent("cgm.options.adsSensitivity.format", FORMAT.format(adsSensitivity));
    });

    public static final AbstractOption CROSSHAIR = new GunListOption<>("cgm.options.crosshair", () -> {
        return CrosshairHandler.get().getRegisteredCrosshairs();
    }, () -> {
        return GunMod.getOptions().crosshair;
    }, (value) -> {
        GunMod.getOptions().crosshair = value;
        CrosshairHandler.get().setCrosshair(value);
    }, (value) -> {
        ResourceLocation id = value.getLocation();
        return new TranslationTextComponent(id.getNamespace() + ".crosshair." + id.getPath());
    });

    public static final Splitter COLON_SPLITTER = Splitter.on(':');

    private File optionsFile;
    private double adsSensitivity = 0.75;
    private ResourceLocation crosshair = Crosshair.DEFAULT.getLocation();

    public GunOptions(File dataDir)
    {
        this.optionsFile = new File(dataDir, "cgm-options.txt");
        this.loadOptions();
    }

    private void loadOptions()
    {
        try
        {
            if(!this.optionsFile.exists())
            {
                return;
            }

            List<String> lines = IOUtils.readLines(new FileInputStream(this.optionsFile), Charsets.UTF_8);
            CompoundNBT compound = new CompoundNBT();

            for(String line : lines)
            {
                try
                {
                    Iterator<String> iterator = COLON_SPLITTER.omitEmptyStrings().limit(2).split(line).iterator();
                    compound.putString(iterator.next(), iterator.next());
                }
                catch(Exception var10)
                {
                    GunMod.LOGGER.warn("Skipping bad option: {}", line);
                }
            }

            for(String key : compound.keySet())
            {
                String value = compound.getString(key);

                try
                {
                    switch(key)
                    {
                        case "adsSensitivity":
                            this.adsSensitivity = Double.parseDouble(value);
                            break;
                        case "crosshair":
                            this.crosshair = new ResourceLocation(value);
                            break;
                    }
                }
                catch(Exception e)
                {
                    GunMod.LOGGER.warn("Skipping bad option: {}:{}", key, value);
                }
            }
        }
        catch(Exception e)
        {
            GunMod.LOGGER.error("Failed to load options", e);
        }
    }

    public void saveOptions()
    {
        try(PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8)))
        {
            writer.println("adsSensitivity:" + this.adsSensitivity);
            writer.println("crosshair:" + this.crosshair);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Gets the ads sensitivity
     */
    public double getAdsSensitivity()
    {
        return this.adsSensitivity;
    }

    public ResourceLocation getCrosshair()
    {
        return this.crosshair;
    }
}