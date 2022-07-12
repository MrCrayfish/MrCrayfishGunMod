package com.tac.guns.client.audio;

import net.minecraft.client.audio.EntityTickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class ReloadingSound extends EntityTickableSound {


    public ReloadingSound(SoundEvent sound, SoundCategory category, Entity entity) {
        super(sound, category, entity);
    }
    @Override
    public void tick(){
        super.tick();
    }
}
