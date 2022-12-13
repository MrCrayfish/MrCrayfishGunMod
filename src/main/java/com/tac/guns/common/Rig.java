package com.tac.guns.common;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.annotation.Optional;
import com.tac.guns.client.handler.command.GunEditor;
import com.tac.guns.interfaces.TGExclude;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;

import javax.annotation.Nullable;


public final class Rig implements INBTSerializable<CompoundNBT>
{
    private General general = new General();
    private Repair repair = new Repair();
    private Sounds sounds = new Sounds();
    /*private Display display = new Display();*/

    public General getGeneral()
    {
        return this.general;
    }

    public Repair getRepair()
    {
        return this.repair;
    }

    public Sounds getSounds() {return this.sounds;}
    /*public Display getDisplay() {return this.display;}*/

    public static class General implements INBTSerializable<CompoundNBT>
    {
        @Optional
        private int armorClass = 1;
        //
        @Optional
        private int ergonomics = 1;
        @Optional
        private float speedReduction = 0.0F;
        @Optional
        private float movementInaccuracy = 1F;
        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("ArmorClass", armorClass);
            tag.putInt("Ergonomics", ergonomics);
            tag.putFloat("SpeedReduction", speedReduction);
            tag.putFloat("MovementInaccuracy", movementInaccuracy); // Movement inaccuracy modifier
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("ArmorClass", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.armorClass = tag.getInt("ArmorClass");
            }
            if(tag.contains("Ergonomics", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.ergonomics = tag.getInt("Ergonomics");
            }
            if(tag.contains("SpeedReduction", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.speedReduction = tag.getFloat("SpeedReduction");
            }
            if(tag.contains("MovementInaccuracy", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.movementInaccuracy = tag.getFloat("MovementInaccuracy");
            }
        }

        /**
         * @return A copy of the general get
         */
        public General copy()
        {
            General general = new General();
            general.armorClass = this.armorClass;
            general.ergonomics = this.ergonomics;
            general.speedReduction = this.speedReduction;
            general.movementInaccuracy = this.movementInaccuracy;
            return general;
        }

        public int getArmorClass()
        {
            return armorClass;
        }
        public int getErgonomics()
        {
            return ergonomics;
        }
        /**
         * @return The default Kilogram weight of the weapon
         */
        public float getSpeedReduction()
        {
            return speedReduction;
        }
        public float getMovementInaccuracy()
        {
            return movementInaccuracy;
        }
    }

    public static class Repair implements INBTSerializable<CompoundNBT>
    {
        @Optional
        private int ticksToRepair = 40;
        @Optional
        private float durability = 40.0f;
        @Optional
        private float quickRepairability = 0.50f;
        @Optional
        private boolean quickRepairable = true;
        @TGExclude
        private ResourceLocation repairItem = new ResourceLocation(Reference.MOD_ID, "armor_plate");

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("RepairItem", this.repairItem.toString());
            tag.putInt("TicksToRepair", this.ticksToRepair);
            tag.putFloat("Durability", this.durability);
            tag.putFloat("QuickRepairability", this.quickRepairability);
            tag.putBoolean("QuickRepairable", this.quickRepairable);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("RepairItem", Constants.NBT.TAG_STRING))
            {
                this.repairItem = new ResourceLocation(tag.getString("RepairItem"));
            }
            if(tag.contains("TicksToRepair", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.ticksToRepair = tag.getInt("TicksToRepair");
            }
            if(tag.contains("Durability", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.durability = tag.getFloat("Durability");
            }
            if(tag.contains("QuickRepairability", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.quickRepairability = tag.getFloat("QuickRepairability");
            }
            if(tag.contains("QuickRepairable", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.quickRepairable = tag.getBoolean("QuickRepairable");
            }
        }

        /**
         * @return A copy of the general get
         */
        public Repair copy()
        {
            Repair repair = new Repair();
            repair.quickRepairable = this.quickRepairable;
            repair.repairItem = this.repairItem;
            repair.durability = this.durability;
            repair.quickRepairability = this.quickRepairability;
            repair.ticksToRepair = this.ticksToRepair;
            return repair;
        }

        public ResourceLocation getItem()
        {
            return this.repairItem;
        }
        public int getTicksToRepair() {return this.ticksToRepair;}
        public float getDurability() {return this.durability;}
        public float getQuickRepairability() {return this.quickRepairability;}
        public boolean isQuickRepairable()
        {
            return this.quickRepairable;
        }
    }

    public static class Sounds implements INBTSerializable<CompoundNBT>
    {
        @Optional
        @Nullable
        @TGExclude
        private ResourceLocation step;
        @Optional
        @Nullable
        @TGExclude
        private ResourceLocation on;
        @Optional
        @Nullable
        @TGExclude
        private ResourceLocation off;
        @Optional
        @Nullable
        @TGExclude
        private ResourceLocation hit;
        @Optional
        @Nullable
        @TGExclude
        private ResourceLocation broken;
        @Optional
        @Nullable
        @TGExclude
        private ResourceLocation repair;

        @Nullable
        public ResourceLocation getOn() {return on;}

        @Nullable
        public ResourceLocation getOff() {return off;}

        @Nullable
        public ResourceLocation getHit() {return hit;}

        @Nullable
        public ResourceLocation getBroken() {return broken;}

        @Nullable
        public ResourceLocation getRepair() {return repair;}

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            if(this.step != null)
            {
                tag.putString("Step", this.step.toString());
            }
            if(this.on != null)
            {
                tag.putString("On", this.on.toString());
            }
            if(this.off != null)
            {
                tag.putString("Off", this.off.toString());
            }
            if(this.hit != null)
            {
                tag.putString("Hit", this.hit.toString());
            }
            if(broken != null)
            {
                tag.putString("Broken", this.broken.toString());
            }
            if(repair != null)
            {
                tag.putString("Repair", this.repair.toString());
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("Step", Constants.NBT.TAG_STRING))
            {
                this.step = this.createSound(tag, "Step");
            }
            if(tag.contains("On", Constants.NBT.TAG_STRING))
            {
                this.on = this.createSound(tag, "On");
            }
            if(tag.contains("Off", Constants.NBT.TAG_STRING))
            {
                this.off = this.createSound(tag, "Off");
            }
            if(tag.contains("Hit", Constants.NBT.TAG_STRING))
            {
                this.hit = this.createSound(tag, "Hit");
            }
            if(tag.contains("Broken", Constants.NBT.TAG_STRING))
            {
                this.broken = this.createSound(tag, "Broken");
            }
            if(tag.contains("Repair", Constants.NBT.TAG_STRING)){
                this.repair = this.createSound(tag, "Repair");
            }
        }

        public Sounds copy()
        {
            Sounds sounds = new Sounds();
            sounds.broken = this.broken;
            sounds.hit = this.hit;
            sounds.off = this.off;
            sounds.on = this.on;
            sounds.repair = this.repair;
            sounds.step = this.step;
            return sounds;
        }

        @Nullable
        private ResourceLocation createSound(CompoundNBT tag, String key)
        {
            String sound = tag.getString(key);
            return sound.isEmpty() ? null : new ResourceLocation(sound);
        }

        @Nullable
        public ResourceLocation getStep() { return this.step; }
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT tag = new CompoundNBT();
        tag.put("General", this.general.serializeNBT());
        tag.put("Repair", this.repair.serializeNBT());
        tag.put("Sounds", this.sounds.serializeNBT());
        /*tag.put("Display", this.display.serializeNBT());*/
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag)
    {
        if(tag.contains("General", Constants.NBT.TAG_COMPOUND))
        {
            this.general.deserializeNBT(tag.getCompound("General"));
        }
        if(tag.contains("Repair", Constants.NBT.TAG_COMPOUND))
        {
            this.repair.deserializeNBT(tag.getCompound("Repair"));
        }
        if(tag.contains("Sounds", Constants.NBT.TAG_COMPOUND))
        {
            this.sounds.deserializeNBT(tag.getCompound("Sounds"));
        }
        /*if(tag.contains("Display", Constants.NBT.TAG_COMPOUND))
        {
            this.display.deserializeNBT(tag.getCompound("Display"));
        }*/
    }



    public static Rig create(CompoundNBT tag)
    {
        Rig gun = new Rig();
        gun.deserializeNBT(tag);
        return gun;
    }

    public Rig copy()
    {
        Rig gun = new Rig();
        gun.general = this.general.copy();
        gun.repair = this.repair.copy();
        gun.sounds = this.sounds.copy();
/*        gun.display = this.display.copy();*/
        return gun;
    }

    private static boolean isAmmo(ItemStack stack, ResourceLocation id)
    {
        return stack != null && stack.getItem().getRegistryName().equals(id);
    }

    public static ExclusionStrategy strategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(TGExclude.class) != null;
        }
    };
}
