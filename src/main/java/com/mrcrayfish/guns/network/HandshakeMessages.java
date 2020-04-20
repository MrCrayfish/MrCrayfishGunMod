package com.mrcrayfish.guns.network;

import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.guns.common.CommonHandler;
import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.IntSupplier;

/**
 * Author: MrCrayfish
 */
public class HandshakeMessages
{
    static class LoginIndexedMessage implements IntSupplier
    {
        private int loginIndex;

        void setLoginIndex(final int loginIndex)
        {
            this.loginIndex = loginIndex;
        }

        int getLoginIndex()
        {
            return loginIndex;
        }

        @Override
        public int getAsInt()
        {
            return getLoginIndex();
        }
    }

    static class C2SAcknowledge extends LoginIndexedMessage
    {
        void encode(PacketBuffer buf) {}

        static C2SAcknowledge decode(PacketBuffer buf)
        {
            return new C2SAcknowledge();
        }
    }

    public static class S2CUpdateGuns extends LoginIndexedMessage
    {
        private ImmutableMap<ResourceLocation, Gun> registeredGuns;

        public S2CUpdateGuns() {}

        private S2CUpdateGuns(ImmutableMap<ResourceLocation, Gun> registeredGuns)
        {
            this.registeredGuns = registeredGuns;
        }

        void encode(PacketBuffer buffer)
        {
            CommonHandler.getGunManager().writeRegisteredGuns(buffer);
        }

        static S2CUpdateGuns decode(PacketBuffer buffer)
        {
            return new S2CUpdateGuns(NetworkGunManager.readRegisteredGuns(buffer));
        }

        @Nullable
        public ImmutableMap<ResourceLocation, Gun> getRegisteredGuns()
        {
            return this.registeredGuns;
        }
    }
}