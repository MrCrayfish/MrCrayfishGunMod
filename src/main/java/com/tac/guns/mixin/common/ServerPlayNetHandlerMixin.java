package com.tac.guns.mixin.common;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.network.CommonStateBox;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetHandler.class)
public class ServerPlayNetHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "processPlayerDigging")
    public void applyDraw(CPlayerDiggingPacket packetIn, CallbackInfo ci){
        CPlayerDiggingPacket.Action action = packetIn.getAction();
        if(action.name().equals("SWAP_ITEM_WITH_OFFHAND") && !player.isSpectator()){
            CommonStateBox.isSwapped = true;
        }
    }
}
