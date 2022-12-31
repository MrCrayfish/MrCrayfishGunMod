package com.tac.guns.client.handler;

import static com.tac.guns.GunMod.LOGGER;

import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.tac.guns.client.InputHandler;
import com.tac.guns.common.Gun;
import com.tac.guns.common.NetworkGunManager;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageLightChange;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class FlashlightHandler
{
    private static FlashlightHandler instance;

    public static FlashlightHandler get()
    {
        if(instance == null)
        {
            instance = new FlashlightHandler();
        }
        return instance;
    }

    private boolean active = false;
    
    private FlashlightHandler()
    {
    	InputHandler.ACTIVATE_SIDE_RAIL.addPressCallback( () -> {
    		final Minecraft mc = Minecraft.getInstance();
    		final PlayerEntity player = mc.player;
    		if(
    			player != null
    			&& player.getHeldItemMainhand().getItem() instanceof GunItem
    			&& Gun.getAttachment(
    				IAttachment.Type.SIDE_RAIL,
    				player.getHeldItemMainhand()
    			) != null
    		) this.active = !active;
    	} );
    }

    private boolean isInGame()
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.loadingGui != null)
            return false;
        if(mc.currentScreen != null)
            return false;
        if(!mc.mouseHelper.isMouseGrabbed())
            return false;
        return mc.isGameFocused();
    }

    @SubscribeEvent
    public void onPlayerUpdate(PlayerTickEvent event)
    {
        if(!isInGame())
            return;
        PlayerEntity player = event.player;
        if(player == null)
            return;

        if(NetworkGunManager.get() != null && NetworkGunManager.get().StackIds != null) {
            if (player.getHeldItemMainhand().getItem() instanceof TimelessGunItem && player.getHeldItemMainhand().getTag() != null) {
                if (!player.getHeldItemMainhand().getTag().contains("ID")) {
                    UUID id;
                    while (true) {
                        LOGGER.log(Level.INFO, "NEW UUID GEN FOR TAC GUN");
                        id = UUID.randomUUID();
                        if (NetworkGunManager.get().Ids.add(id))
                            break;
                    }
                    player.getHeldItemMainhand().getTag().putUniqueId("ID", id);
                    NetworkGunManager.get().StackIds.put(id, player.getHeldItemMainhand());
                }
            }
        }

        if (event.phase == Phase.START && (player.getHeldItemMainhand() != null && this.active && Gun.getAttachment(IAttachment.Type.SIDE_RAIL, player.getHeldItemMainhand()) != null))
        {
            PacketHandler.getPlayChannel().sendToServer(new MessageLightChange(new int[]{32}));//(new int[]{2,32}));
            //PacketHandler.getPlayChannel().sendToServer(new MessageLightChange(6));
            /*int lightNumber = 32 / 5;
            int lightRange = 32;

            for(int i = 0; i < lightNumber; ++i) {
                lightRange -= 5;
                PacketHandler.getPlayChannel().sendToServer(new MessageLightChange(lightRange));
            }*/

        }/*PlayerEntity player = event.player;
        if (event.phase == Phase.START && (player.getHeldItemMainhand() != null && this.active && Gun.getAttachment(IAttachment.Type.SIDE_RAIL, player.getHeldItemMainhand()) != null))
        {
            this.createLight(player, 32);
            int lightNumber = 32 / 5;
            int lightRange = 32;

            for(int i = 1; i < lightNumber; ++i) {
                lightRange -= 5;
                this.createLight(player, lightRange);
            }
        }*/
    }

/*
    private void createLight(PlayerEntity player, int lookingRange) {
        if(player.getHeldItemMainhand().getItem() instanceof GunItem)
        {
            if(Gun.getAttachment(IAttachment.Type.SIDE_RAIL,player.getHeldItemMainhand()) != null) {
                IWorld world = player.world;
                TileEntity tile = null;
                int x = this.lookingAt(player, lookingRange).getX();
                int y = this.lookingAt(player, lookingRange).getY();
                int z = this.lookingAt(player, lookingRange).getZ();
                */
/*int x = (int)Math.ceil(this.vecLookingAt(player, lookingRange).getX());
                int y = (int)Math.ceil(this.vecLookingAt(player, lookingRange).getY());
                int z = (int)Math.ceil(this.vecLookingAt(player, lookingRange).getZ());*//*

                boolean createLight = false;

                for (int i = 0; i < 5; ++i) {
                    tile = world.getTileEntity(new BlockPos(x, y, z));
                    if (tile instanceof FlashLightSource) {
                        createLight = true;
                        break;
                    }

                    if (!world.isAirBlock(new BlockPos(x, y, z))) {
                        int pX = (int) player.getPositionVec().getX();
                        int pY = (int) player.getPositionVec().getY();
                        int pZ = (int) player.getPositionVec().getZ();
                        if (pX > x) {
                            ++x;
                        } else if (pX < x) {
                            --x;
                        }
                        if (pY > y) {
                            ++y;
                        } else if (pY < y) {
                            --y;
                        }
                        if (pZ > z) {
                            ++z;
                        } else if (pZ < z) {
                            --z;
                        }
                    } else if (world.isAirBlock(new BlockPos(x, y, z))) {
                        createLight = true;
                        break;
                    }
                }

                if (createLight) {
                    tile = world.getTileEntity(new BlockPos(x, y, z));
                    if (tile instanceof FlashLightSource) {
                        ((FlashLightSource) tile).ticks = 0;
                    } else if (world.getBlockState(new BlockPos(x, y, z)).getBlock() != ModBlocks.FLASHLIGHT_BLOCK.get()) { //
                        world.setBlockState(new BlockPos(x, y, z), (ModBlocks.FLASHLIGHT_BLOCK.get()).getDefaultState(), 1);
                    }
                }
            }
        }
    }
*/

}
