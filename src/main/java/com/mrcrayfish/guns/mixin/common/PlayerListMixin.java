package com.mrcrayfish.guns.mixin.common;

import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageUpdateGuns;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Author: MrCrayfish
 */
@Mixin(PlayerList.class)
public class PlayerListMixin
{
    //TODO Can be changed to an event now!
    @Inject(method = "reloadResources", at = @At(value = "TAIL"))
    private void onReload(CallbackInfo ci)
    {
        NetworkGunManager manager = NetworkGunManager.get();
        if(manager != null)
        {
            PacketHandler.getPlayChannel().send(PacketDistributor.ALL.noArg(), new MessageUpdateGuns());
        }
    }
}
