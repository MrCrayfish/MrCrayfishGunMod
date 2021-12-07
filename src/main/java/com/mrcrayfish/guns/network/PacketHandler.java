package com.mrcrayfish.guns.network;

import com.mrcrayfish.framework.api.FrameworkAPI;
import com.mrcrayfish.framework.api.network.FrameworkChannelBuilder;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.CustomGunManager;
import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.network.message.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler
{
    private static final SimpleChannel PLAY_CHANNEL = FrameworkChannelBuilder
            .create(Reference.MOD_ID, "play", 1)
            .registerPlayMessage(MessageAim.class, NetworkDirection.PLAY_TO_SERVER)
            .registerPlayMessage(MessageReload.class, NetworkDirection.PLAY_TO_SERVER)
            .registerPlayMessage(MessageShoot.class, NetworkDirection.PLAY_TO_SERVER)
            .registerPlayMessage(MessageUnload.class, NetworkDirection.PLAY_TO_SERVER)
            .registerPlayMessage(MessageStunGrenade.class, NetworkDirection.PLAY_TO_CLIENT)
            .registerPlayMessage(MessageCraft.class, NetworkDirection.PLAY_TO_SERVER)
            .registerPlayMessage(MessageBulletTrail.class, NetworkDirection.PLAY_TO_CLIENT)
            .registerPlayMessage(MessageAttachments.class, NetworkDirection.PLAY_TO_SERVER)
            .registerPlayMessage(MessageUpdateGuns.class, NetworkDirection.PLAY_TO_CLIENT)
            .registerPlayMessage(MessageBlood.class, NetworkDirection.PLAY_TO_CLIENT)
            .registerPlayMessage(MessageShooting.class, NetworkDirection.PLAY_TO_SERVER)
            .registerPlayMessage(MessageGunSound.class, NetworkDirection.PLAY_TO_CLIENT)
            .registerPlayMessage(MessageProjectileHitBlock.class, NetworkDirection.PLAY_TO_CLIENT)
            .registerPlayMessage(MessageProjectileHitEntity.class, NetworkDirection.PLAY_TO_CLIENT)
            .registerPlayMessage(MessageRemoveProjectile.class, NetworkDirection.PLAY_TO_CLIENT)
            .build();

    /**
     * Gets the play network channel for MrCrayfish's Gun Mod
     */
    public static SimpleChannel getPlayChannel()
    {
        return PLAY_CHANNEL;
    }

    public static void init()
    {
        FrameworkAPI.registerLoginData(new ResourceLocation(Reference.MOD_ID, "network_gun_manager"), NetworkGunManager.LoginData::new);
        FrameworkAPI.registerLoginData(new ResourceLocation(Reference.MOD_ID, "custom_gun_manager"), CustomGunManager.LoginData::new);
    }
}
