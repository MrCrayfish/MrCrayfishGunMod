package com.mrcrayfish.guns.mixin.common;

import com.mrcrayfish.guns.item.IColored;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LayeredCauldronBlock.class)
public abstract class LayeredCauldronBlockMixin extends AbstractCauldronBlock {
    public LayeredCauldronBlockMixin(Properties p, Map<Item, CauldronInteraction> m) {
        super(p, m);
    }

    @Inject(method = "entityInside", at = @At("HEAD"))
    private void removeColor(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci)
    {
        if (level.isClientSide) return;
        if (level.getGameTime() % 40 != 0) return; // Reduced frequency of check
        if (!(entity instanceof ItemEntity ie)) return;
        final ItemStack stack = ie.getItem();
        if (!(stack.getItem() instanceof IColored colored)) return;
        if (!colored.hasColor(stack)) return;
        if (!this.isEntityInsideContent(state, pos, ie)) return;
        colored.removeColor(stack);
        final Vec3 p = ie.position();
        ie.discard();
        level.addFreshEntity(new ItemEntity(level, p.x, p.y, p.z, stack));
    }
}
