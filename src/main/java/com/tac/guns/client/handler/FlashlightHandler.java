package com.tac.guns.client.handler;

import com.tac.guns.Reference;
import com.tac.guns.client.KeyBinds;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModBlocks;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageLightChange;
import com.tac.guns.network.message.MessageShooting;
import com.tac.guns.tileentity.FlashLightSource;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static net.minecraftforge.eventbus.api.EventPriority.HIGHEST;

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

    private boolean active = false;
    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event)
    {
        if(event.getAction() != GLFW.GLFW_PRESS)
            return;
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player == null)
            return;
        if(KeyBinds.KEY_ACTIVATE_SIDE_RAIL.isPressed() && event.getAction() == GLFW.GLFW_PRESS) // REPLACE KEYBIND
        {
            if(player.getHeldItemMainhand().getItem() instanceof GunItem)
            {
                if(Gun.getAttachment(IAttachment.Type.SIDE_RAIL,player.getHeldItemMainhand()) != null)
                    this.active=!active;
            }
            return;
        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(PlayerTickEvent event)
    {
        if(!isInGame())
            return;
        PlayerEntity player = event.player;
        if(player == null)
            return;
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
