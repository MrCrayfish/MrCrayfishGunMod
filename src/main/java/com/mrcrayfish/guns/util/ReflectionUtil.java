package com.mrcrayfish.guns.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author: MrCrayfish
 */
public class ReflectionUtil
{
    private static final Method updateRedstoneOutputMethod = ObfuscationReflectionHelper.findMethod(TargetBlock.class, "m_57391_", LevelAccessor.class, BlockState.class, BlockHitResult.class, Entity.class);

    public static int updateTargetBlock(TargetBlock block, LevelAccessor accessor, BlockState state, BlockHitResult result, Entity entity)
    {
        try
        {
            return (int) updateRedstoneOutputMethod.invoke(block, accessor, state, result, entity);
        }
        catch(IllegalAccessException | InvocationTargetException ignored)
        {
            return 0;
        }
    }
}
