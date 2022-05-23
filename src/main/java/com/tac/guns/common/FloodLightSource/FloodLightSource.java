package com.tac.guns.common.FloodLightSource;

import atomicstryker.dynamiclights.server.DynamicLights;
import atomicstryker.dynamiclights.server.IDynamicLightSource;
import atomicstryker.dynamiclights.server.ItemConfigHelper;
import com.electronwill.nightconfig.core.Config;
import com.tac.guns.GunMod;
import com.tac.guns.client.handler.ScopeJitterHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.state.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;

/**
 *
 * @author AtomicStryker
 *
 * Offers Dynamic Light functionality emulating portable or static Flood Lights
 *
 */
//@Mod(modid = "dynamiclights_floodlights", name = "Dynamic Lights Flood Light", version = "1.0.3", dependencies = "required-after:dynamiclights")
public class FloodLightSource
{
    private PlayerEntity thePlayer;
    /*Property itemsList = ;
    private static ItemConfigHelper itemsMap = new ItemConfigHelper(Property);*/
    private PartialLightSource[] partialLights;
    private boolean enabled = true;
    private boolean simpleMode = true;

    public FloodLightSource()
    {
        partialLights = new PartialLightSource[5];
    }
    private static FloodLightSource instance;
    public static FloodLightSource get()
    {
        return instance == null ? instance = new FloodLightSource() : instance;
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent tick) //  RenderTickEvent
    {
        if (tick.phase == TickEvent.Phase.END)
        {
            Minecraft mc = Minecraft.getInstance();
            this.thePlayer = mc.player;
            if (this.thePlayer != null && this.thePlayer.isAlive())
            {
                int lightLevel = 15;/*Math.max(*//*getLightFromItemStack(this.thePlayer.getHeldItemMainhand()), getLightFromItemStack(thePlayer.getHeldItemOffhand()*//*)*///);
                RayTraceResult mop = this.thePlayer.pick(24,0F,false);// rayTraceBlocks(posvec, look);
                boolean doLight = true;
                if(mop != null)
                {
                    //doLight = true;
                    checkDummyInit(this.thePlayer.world, mop.getHitVec());
                }
                //else
                    //doLight = false;

//                if (true)//(lightLevel > 0)
//                {
                handleLight(partialLights[0], lightLevel, doLight);
                setLightsEnabled(false);
                /*
                    *//*if (!simpleMode)
                    {
                        handleLight(partialLights[1], lightLevel, doLight);
                        handleLight(partialLights[2], lightLevel, doLight);
                        handleLight(partialLights[3], lightLevel, doLight);
                        handleLight(partialLights[4], lightLevel, doLight);
                    }*//*
                    setLightsEnabled(true);
                }
                else
                {
                    setLightsEnabled(true);
                }*/
          }
        }
    }

    private void handleLight(PartialLightSource source, int light, boolean doLight)
    {
        if (doLight)
        {
            source.lightLevel = light;
        }
        else
        {
            source.lightLevel = 0;
        }
    }

    private void setLightsEnabled(boolean newEnabled)
    {
        if (newEnabled != enabled)
        {
            enabled = newEnabled;

            if (!simpleMode)
            {
                for (PartialLightSource p : partialLights)
                {
                    if (true)
                    {
                        System.out.println();
                        //GunMod.LOGGER.log(
                                System.out.println("GAMING GAMING GAMING");
                        DynamicLights.addLightSource((IDynamicLightSource) p);
                    }
                    else
                    {
                        //GunMod.LOGGER.log(
                                System.out.println("GAMING GAMING                          GaEw");
                        DynamicLights.removeLightSource((IDynamicLightSource)p);
                    }
                }
            }
            else
            {
                if (newEnabled)
                {
                    DynamicLights.addLightSource(partialLights[0]);
                }
                else
                {
                    //GunMod.LOGGER.log(
                    System.out.println("GAMING            GAMING");
                    DynamicLights.removeLightSource(partialLights[0]);
                }
            }
        }
    }

    private void checkDummyInit(World world, Vector3d pos)
    {
        if (partialLights[0] == null)
        {/*
            for (int i = 0; i < partialLights.length; i++)
            {
                partialLights[i] = new PartialLightSource(new DummyEntity(world, pos, i));
                DynamicLights.addLightSource(partialLights[i]);
            }*/
            partialLights[0] = new PartialLightSource(new DummyEntity(world, pos, 1));
            DynamicLights.addLightSource(partialLights[0]);
        }
    }

    /*private int getLightFromItemStack(ItemStack stack)
    {
        if (stack != null)
        {
            int r = itemsMap.retrieveValue(stack.getItem().getRegistryName(), stack.getItemDamage());
            return r < 0 ? 0 : r;
        }
        return 0;
    }*/

    private class PartialLightSource implements IDynamicLightSource
    {
        private final Entity entity;
        private int lightLevel = 15;

        private PartialLightSource(Entity e)
        {
            entity = e;
        }

        @Override
        public Entity getAttachmentEntity()
        {
            return entity;
        }

        @Override
        public int getLightLevel()
        {
            return lightLevel;
        }
    }

    private class DummyEntity extends Entity
    {
        public DummyEntity(World par1World, Vector3d pos, int i)
        {
            super(EntityType.ITEM,par1World); // .create(Integer.MAX_VALUE-i,par1World).getType()
            this.noClip = true;
            this.recalculateSize();
            this.setEntityId(Integer.MAX_VALUE-i);
            this.setPosition(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D);
        }

        @Override
        protected void registerData() {

        }

        @Override
        protected void readAdditional(CompoundNBT compound) {

        }

        @Override
        protected void writeAdditional(CompoundNBT compound) {

        }

        @Override
        public IPacket<?> createSpawnPacket() {
            return null;
        }

        @Override
        public EntitySize getSize(Pose poseIn) {
            // To make the entity visible for debugging, don't set its size to zero
            return new EntitySize(0f, 0f, true);
        }
    }

}