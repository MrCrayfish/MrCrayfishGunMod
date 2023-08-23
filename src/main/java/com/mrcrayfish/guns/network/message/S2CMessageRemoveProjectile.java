package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.framework.api.network.message.PlayMessage;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Author: MrCrayfish
 */
public class S2CMessageRemoveProjectile extends PlayMessage<S2CMessageRemoveProjectile>
{
    private int entityId;

    public S2CMessageRemoveProjectile() {}

    public S2CMessageRemoveProjectile(int entityId)
    {
        this.entityId = entityId;
    }

    @Override
    public void encode(S2CMessageRemoveProjectile message, FriendlyByteBuf buffer)
    {
        buffer.writeInt(message.entityId);
    }

    @Override
    public S2CMessageRemoveProjectile decode(FriendlyByteBuf buffer)
    {
        return new S2CMessageRemoveProjectile(buffer.readInt());
    }

    @Override
    public void handle(S2CMessageRemoveProjectile message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleRemoveProjectile(message));
        context.setHandled(true);
    }


    public int getEntityId()
    {
        return this.entityId;
    }
}
