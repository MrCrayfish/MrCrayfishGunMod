package com.tac.guns.network;

import com.google.common.collect.ImmutableMap;
import com.tac.guns.common.CustomGun;
import com.tac.guns.common.CustomGunLoader;
import com.tac.guns.common.Gun;
import com.tac.guns.common.NetworkGunManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.function.IntSupplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
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

    public static class S2CUpdateGuns extends LoginIndexedMessage implements NetworkGunManager.IGunProvider
    {
        private ImmutableMap<ResourceLocation, Gun> registeredGuns;
        private ImmutableMap<ResourceLocation, CustomGun> customGuns;

        public S2CUpdateGuns() {}

        void encode(PacketBuffer buffer)
        {
            /* This shouldn't be null as it's encoded from the logical server but
             * it's just here to avoiding IDE warnings */
            Validate.notNull(NetworkGunManager.get());
            NetworkGunManager.get().writeRegisteredGuns(buffer);
            Validate.notNull(CustomGunLoader.get());
            CustomGunLoader.get().writeCustomGuns(buffer);
        }

        static S2CUpdateGuns decode(PacketBuffer buffer)
        {
            S2CUpdateGuns message = new S2CUpdateGuns();
            message.registeredGuns = NetworkGunManager.readRegisteredGuns(buffer);
            message.customGuns = CustomGunLoader.readCustomGuns(buffer);
            return message;
        }

        @Nullable
        @Override
        public ImmutableMap<ResourceLocation, Gun> getRegisteredGuns()
        {
            return this.registeredGuns;
        }

        @Override
        @Nullable
        public ImmutableMap<ResourceLocation, CustomGun> getCustomGuns()
        {
            return this.customGuns;
        }
    }
}