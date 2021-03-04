package com.mrcrayfish.guns.mixin.common;

import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Author: MrCrayfish
 */
@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin
{
    @Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;changeDimension(Lnet/minecraft/world/server/ServerWorld;)Lnet/minecraft/entity/Entity;"))
    private void beforeChangeDimension(BlockState state, World worldIn, BlockPos pos, Entity entityIn, CallbackInfo ci)
    {
        if(worldIn.getDimensionKey() == World.THE_END && entityIn instanceof ItemEntity)
        {
            ItemStack stack = ((ItemEntity) entityIn).getItem();
            if(stack.getItem() instanceof GunItem)
            {
                ItemStack gun = stack.copy();
                gun.getOrCreateTag().putFloat("Scale", 2.0F);
                ((ItemEntity) entityIn).setItem(gun);
            }
        }
    }
}
