package com.tac.guns.client.render.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.GunMod;
import de.javagl.jgltf.model.GltfAnimations;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.animation.Animation;
import de.javagl.jgltf.model.animation.AnimationManager;
import de.javagl.jgltf.model.animation.AnimationRunner;
import de.javagl.jgltf.model.io.GltfAsset;
import de.javagl.jgltf.model.io.GltfAssetReader;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import de.javagl.jgltf.model.v2.GltfModelV2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Animations {
    private static final Stack<NodeModel> nodeModelStack = new Stack<>();
    private static final Stack<NodeModel> initialModelStack = new Stack<>();
    private static final MatrixStack extraMatrixStack = new MatrixStack();
    private static NodeModel bind;
    private static NodeModel initial;
    private static final Map<String, GltfModelV2> gltfModelV2Map = new HashMap<>();
    private static final Map<String, GltfModelV2> initialModelMap = new HashMap<>();
    private static final Map<String, AnimationRunner> animationRunnerMap = new HashMap<>();
    private static final Map<String, AnimationManager> animationManagerMap = new HashMap<>();

    public static GltfModelV2 load(AnimationMeta animationMeta) throws IOException{
        if(animationMeta != null)  return load(animationMeta.getResourceLocation());
        else return null;
    }

    public static GltfModelV2 load(ResourceLocation resourceLocation) throws IOException {
        IResource resource = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
        InputStream inputStream = resource.getInputStream();
        GltfAssetReader reader = new GltfAssetReader();
        GltfAsset asset = reader.readWithoutReferences(inputStream);
        if (asset instanceof GltfAssetV2) {
            // put GltfModel into the gltfModelMap
            GltfModelV2 model = new GltfModelV2((GltfAssetV2) asset);
            gltfModelV2Map.put(resourceLocation.toString(), model);
            //refresh animationManagerMap
            List<Animation> animations = GltfAnimations.createModelAnimations(model.getAnimationModels());
            AnimationManager animationManager = new AnimationManager(AnimationManager.AnimationPolicy.TIP_STOP);
            animationManager.addAnimations(animations);
            animationManagerMap.put(resourceLocation.toString(),animationManager);
            //refresh animationRunnerMap
            stopAnimation(resourceLocation);
            AnimationRunner newRunner = new AnimationRunner(animationManager);
            animationRunnerMap.put(resourceLocation.toString(),newRunner);
            //initialState
            GltfModelV2 model2 = new GltfModelV2((GltfAssetV2) asset);
            List<Animation> animations2 = GltfAnimations.createModelAnimations(model2.getAnimationModels());
            AnimationManager initialStateManager = new AnimationManager(AnimationManager.AnimationPolicy.TIP_STOP);
            initialStateManager.addAnimations(animations2);
            initialStateManager.reset();
            initialStateManager.performStep(1);
            initialModelMap.put(resourceLocation.toString(),model2);
            return model;
        }
        inputStream.close();
        return null;
    }

    private static GltfModelV2 getGltfModel(ResourceLocation resourceLocation){
        return gltfModelV2Map.get(resourceLocation.toString());
    }

    private static GltfModelV2 getInitialModel(ResourceLocation resourceLocation){
        return initialModelMap.get(resourceLocation.toString());
    }

    public static void pushNode(AnimationMeta animationMeta, int index){
        if(animationMeta!=null) pushNode(animationMeta.getResourceLocation(),index);
    }

    public static void pushNode(ResourceLocation gltfResource, int index) {
        if(gltfResource == null) return;
        GltfModelV2 gltfModel = getGltfModel(gltfResource);
        if(gltfModel == null){
            bind = null;
        }else bind = gltfModel.getNodeModels().get(index);
        GltfModelV2 initialModel = getInitialModel(gltfResource);
        if(initialModel == null){
            initial = null;
        }else initial = initialModel.getNodeModels().get(index);
        nodeModelStack.push(bind);
        initialModelStack.push(initial);
    }

    public static void popNode(){
        if(!nodeModelStack.empty()) nodeModelStack.pop();
        if(!initialModelStack.empty()) initialModelStack.pop();
        bind = (nodeModelStack.empty() ? null : nodeModelStack.peek());
        initial = (initialModelStack.empty() ? null : initialModelStack.peek());
    }

    public static NodeModel peekNodeModel(){
        return bind;
    }

    public static NodeModel peekInitialModel(){
        return initial;
    }

    private static AnimationRunner getAnimationRunner(ResourceLocation resourceLocation){
        return animationRunnerMap.get(resourceLocation.toString());
    }

    public static void runAnimation(AnimationMeta animationMeta){
        if(animationMeta !=null) runAnimation(animationMeta.getResourceLocation());
    }

    public static void runAnimation(ResourceLocation resourceLocation){
        AnimationRunner runner = getAnimationRunner(resourceLocation);
        if(runner!=null) {
            if(runner.isRunning()) return;
            runner.start();
        }
    }

    public static void stopAnimation(AnimationMeta animationMeta){
        if(animationMeta!=null) stopAnimation(animationMeta.getResourceLocation());
    }

    public static void stopAnimation(ResourceLocation resourceLocation){
        AnimationRunner runner = getAnimationRunner(resourceLocation);
        if(runner!=null) runner.stop();
        AnimationManager manager = getAnimationManager(resourceLocation);
        if(manager!=null) manager.reset();
    }

    public static boolean isAnimationRunning(AnimationMeta animationMeta){
        if(animationMeta!=null) return isAnimationRunning(animationMeta.getResourceLocation());
        else return false;
    }

    public static boolean isAnimationRunning(ResourceLocation resourceLocation){
        AnimationRunner runner = getAnimationRunner(resourceLocation);
        if(runner == null) return false;
        return runner.isRunning();
    }

    private static AnimationManager getAnimationManager(ResourceLocation resourceLocation){
        return animationManagerMap.get(resourceLocation.toString());
    }

    public static MatrixStack getExtraMatrixStack() { return extraMatrixStack; }

    public static void applyExtraTransform(MatrixStack matrixStack){
        matrixStack.getLast().getMatrix().mul(extraMatrixStack.getLast().getMatrix());
        matrixStack.getLast().getNormal().mul(extraMatrixStack.getLast().getNormal());
    }

    public static void applyAnimationTransform(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, LivingEntity entity, MatrixStack matrixStack){
        if(itemStack != null && entity != null) {
            IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(itemStack, entity.world, entity);
            applyAnimationTransform(model, transformType, matrixStack);
        }
    }

    public static void applyAnimationTransform(IBakedModel model, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack){
        if(Animations.peekNodeModel() != null && Animations.peekInitialModel() != null) {
            ItemTransformVec3f modelTransformVec3f = (model == null ? null : model.getItemCameraTransforms().getTransform(transformType) );
            if(modelTransformVec3f != null) {
                matrixStack.translate(modelTransformVec3f.translation.getX(), modelTransformVec3f.translation.getY(), modelTransformVec3f.translation.getZ());
                matrixStack.scale(modelTransformVec3f.scale.getX(),modelTransformVec3f.scale.getY(),modelTransformVec3f.scale.getZ());
                matrixStack.translate(-0.5,-0.5,-0.5);
            }
            Matrix4f animationTransition = new Matrix4f(Animations.peekNodeModel().computeGlobalTransform(null));
            Matrix4f initialTransition = new Matrix4f(Animations.peekInitialModel().computeGlobalTransform(null));
            animationTransition.transpose();
            initialTransition.transpose();
            initialTransition.invert();
            matrixStack.getLast().getMatrix().mul(animationTransition);
            matrixStack.getLast().getMatrix().mul(initialTransition);
            if(modelTransformVec3f !=null) {
                matrixStack.translate(0.5, 0.5, 0.5);
                matrixStack.scale(1/modelTransformVec3f.scale.getX(),1/modelTransformVec3f.scale.getY(),1/modelTransformVec3f.scale.getZ());
                matrixStack.translate(-modelTransformVec3f.translation.getX(), -modelTransformVec3f.translation.getY(), -modelTransformVec3f.translation.getZ());
            }
        }
        applyExtraTransform(matrixStack);
    }
}
