package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.GunConfig.EffectCriteria;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.ModPotions;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.item.ItemAmmo;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageExplosionStunGrenade;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import javax.annotation.Nullable;

public class EntityGrenadeStun extends EntityGrenade
{

    public EntityGrenadeStun(World world)
    {
        super(world);
    }

    public EntityGrenadeStun(World world, EntityPlayer player)
    {
        super(world, player);
        setItem(new ItemStack(ModGuns.AMMO, 1, ItemAmmo.Type.GRENADE_STUN.ordinal()));
    }

    @Override
    public void onDeath()
    {
        double y = posY + height * 0.5;
        world.playSound(null, posX, y, posZ, ModSounds.getSound("grenade_stun_explosion"), SoundCategory.BLOCKS, 4, (1 + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        PacketHandler.INSTANCE.sendToAllAround(new MessageExplosionStunGrenade(posX, y, posZ), new TargetPoint(world.provider.getDimension(), posX, y, posZ, 4096));

        // Calculate bounds of area where potentially effected players my be
        double diameter = Math.max(GunConfig.SERVER.stunGrenades.deafen.criteria.radius, GunConfig.SERVER.stunGrenades.blind.criteria.radius) * 2 + 1;
        int minX = MathHelper.floor(posX - diameter);
        int maxX = MathHelper.floor(posX + diameter);
        int minY = MathHelper.floor(y - diameter);
        int maxY = MathHelper.floor(y + diameter);
        int minZ = MathHelper.floor(posZ - diameter);
        int maxZ = MathHelper.floor(posZ + diameter);

        // Affect all non-spectating players in range of the blast
        Vec3d grenade = new Vec3d(posX, y, posZ);
        Vec3d eyes, directionGrenade;
        double distance;
        for (EntityPlayerMP player : world.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)))
        {
            if (player.isImmuneToExplosions())
                continue;

            eyes = player.getPositionEyes(1);
            directionGrenade = grenade.subtract(eyes);
            distance = directionGrenade.lengthVector();

            // Calculate angle between eye-gaze line and eye-grenade line
            double angle = Math.toDegrees(Math.acos(player.getLook(1).dotProduct(directionGrenade.normalize())));
 
            // Apply effects as determined by their criteria
            calculateAndApplyEffect(ModPotions.DEAFENED, GunConfig.SERVER.stunGrenades.deafen.criteria, player, grenade, eyes, distance, angle);
            calculateAndApplyEffect(ModPotions.BLINDED, GunConfig.SERVER.stunGrenades.blind.criteria, player, grenade, eyes, distance, angle);
        }
    }

    private void calculateAndApplyEffect(Potion effect, EffectCriteria criteria, EntityPlayerMP player, Vec3d grenade, Vec3d eyes, double distance, double angle)
    {
        double angleMax = criteria.angleEffect * 0.5;
        if (distance <= criteria.radius && angleMax > 0 && angle <= angleMax)
        {
            // Verify that light can pass through all blocks obstructing the player's line of sight to the grenade
            if (effect != ModPotions.BLINDED || rayTraceOpaqueBlocks(world, eyes, grenade, false, false, false) == null)
            {
                // Duration attenuated by distance
                int durationBlinded = (int) Math.round(criteria.durationMax - (criteria.durationMax - criteria.durationMin) * (distance / criteria.radius));

                // Duration further attenuated by angle
                durationBlinded *= 1 - (angle * (1 - criteria.angleAttenuationMax)) / angleMax;
                player.addPotionEffect(new PotionEffect(effect, durationBlinded, 0, false, false));
            }
        }
    }

    /**
     * Version of {@link net.minecraft.world.World#rayTraceBlocks(Vec3d, Vec3d, boolean, boolean, boolean) rayTraceBlocks} that ignores transparent blocks
     */
    @Nullable
    public RayTraceResult rayTraceOpaqueBlocks(World world, Vec3d start, Vec3d end, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
    {
        if (!Double.isNaN(start.x) && !Double.isNaN(start.y) && !Double.isNaN(start.z))
        {
            if (!Double.isNaN(end.x) && !Double.isNaN(end.y) && !Double.isNaN(end.z))
            {
                int endX = MathHelper.floor(end.x);
                int endY = MathHelper.floor(end.y);
                int endZ = MathHelper.floor(end.z);
                int startX = MathHelper.floor(start.x);
                int startY = MathHelper.floor(start.y);
                int startZ = MathHelper.floor(start.z);
                BlockPos pos = new BlockPos(startX, startY, startZ);
                IBlockState stateInside = world.getBlockState(pos);

                // Added light opacity check
                if (stateInside.getLightOpacity(world, pos) != 0 && (!ignoreBlockWithoutBoundingBox || stateInside.getCollisionBoundingBox(world, pos) != Block.NULL_AABB)
                        && stateInside.getBlock().canCollideCheck(stateInside, stopOnLiquid))
                {
                    RayTraceResult raytraceresult = stateInside.collisionRayTrace(world, pos, start, end);
                    if (raytraceresult != null)
                        return raytraceresult;
                }

                RayTraceResult raytraceresult2 = null;
                int limit = 200;
                while (limit-- >= 0)
                {
                    if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z))
                        return null;

                    if (startX == endX && startY == endY && startZ == endZ)
                        return returnLastUncollidableBlock ? raytraceresult2 : null;

                    boolean completedX = true;
                    boolean completedY = true;
                    boolean completedZ = true;
                    double d0 = 999;
                    double d1 = 999;
                    double d2 = 999;

                    if (endX > startX)
                        d0 = startX + 1;
                    else if (endX < startX)
                        d0 = startX;
                    else
                        completedX = false;

                    if (endY > startY)
                        d1 = startY + 1;
                    else if (endY < startY)
                        d1 = startY;
                    else
                        completedY = false;

                    if (endZ > startZ)
                        d2 = startZ + 1;
                    else if (endZ < startZ)
                        d2 = startZ;
                    else
                        completedZ = false;

                    double d3 = 999;
                    double d4 = 999;
                    double d5 = 999;
                    double d6 = end.x - start.x;
                    double d7 = end.y - start.y;
                    double d8 = end.z - start.z;

                    if (completedX)
                        d3 = (d0 - start.x) / d6;

                    if (completedY)
                        d4 = (d1 - start.y) / d7;

                    if (completedZ)
                        d5 = (d2 - start.z) / d8;

                    if (d3 == -0)
                        d3 = -1.0E-4D;

                    if (d4 == -0)
                        d4 = -1.0E-4D;

                    if (d5 == -0)
                        d5 = -1.0E-4D;

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = endX > startX ? EnumFacing.WEST : EnumFacing.EAST;
                        start = new Vec3d(d0, start.y + d7 * d3, start.z + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = endY > startY ? EnumFacing.DOWN : EnumFacing.UP;
                        start = new Vec3d(start.x + d6 * d4, d1, start.z + d8 * d4);
                    }
                    else
                    {
                        enumfacing = endZ > startZ ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        start = new Vec3d(start.x + d6 * d5, start.y + d7 * d5, d2);
                    }

                    startX = MathHelper.floor(start.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    startY = MathHelper.floor(start.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    startZ = MathHelper.floor(start.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    pos = new BlockPos(startX, startY, startZ);
                    IBlockState state = world.getBlockState(pos);

                    // Added light opacity check
                    if (state.getLightOpacity(world, pos) != 0 && (!ignoreBlockWithoutBoundingBox
                            || state.getMaterial() == Material.PORTAL || state.getCollisionBoundingBox(world, pos) != Block.NULL_AABB))
                    {
                        if (state.getBlock().canCollideCheck(state, stopOnLiquid))
                        {
                            RayTraceResult raytraceresult1 = state.collisionRayTrace(world, pos, start, end);
                            if (raytraceresult1 != null)
                                return raytraceresult1;
                        }
                        else
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, start, enumfacing, pos);
                    }
                }
                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            return null;
        }
        return null;
    }
}