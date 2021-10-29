package com.tac.guns.item.TransitionalTypes;

/*
import com.tac.guns.item.GunItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.util.Process;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import net.minecraft.item.Item.Properties;

public class AnimatedTimelessGunItem extends TimelessGunItem implements IAnimatable,ITimelessAnimated{
    public static final String CONTROLLER_NAME = "gunStatusController";

    public AnimatedTimelessGunItem(Process<Properties> properties, Supplier<Callable<ItemStackTileEntityRenderer>> ister){
        super( p-> properties.process(p.setISTER(ister)) );
    }

    public AnimationFactory factory = new AnimationFactory(this);

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, CONTROLLER_NAME, 20, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public void playAnimation(String animationName, ItemStack stack, boolean coercive){
        AnimationController controller = GeckoLibUtil.getControllerForStack(this.factory,stack,CONTROLLER_NAME);
        if(coercive || controller.getAnimationState() == AnimationState.Stopped) {
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation(animationName, false));
        }
    }
}
*/
