package com.mrcrayfish.guns.client.util;

import com.mrcrayfish.framework.api.client.FrameworkClientAPI;
import com.mrcrayfish.framework.api.serialize.DataArray;
import com.mrcrayfish.framework.api.serialize.DataNumber;
import com.mrcrayfish.framework.api.serialize.DataObject;
import com.mrcrayfish.framework.api.serialize.DataType;
import com.mrcrayfish.guns.cache.ObjectCache;
import com.mrcrayfish.guns.client.MetaLoader;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.properties.SightAnimation;
import com.mrcrayfish.guns.item.IMeta;
import com.mrcrayfish.guns.item.ScopeItem;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import com.mrcrayfish.guns.item.attachment.IScope;
import com.mrcrayfish.guns.item.attachment.impl.Scope;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

//TODO eventually convert to deserialized objects

/**
 * Author: MrCrayfish
 */
@SuppressWarnings("ConstantConditions")
public final class PropertyHelper
{
    public static final String CACHE_KEY = "properties";
    public static final String MODEL_KEY = "cgm:model";
    public static final String WEAPON_KEY = "cgm:weapon";
    public static final String SCOPE_KEY = "cgm:scope";
    public static final Vec3 GUN_DEFAULT_ORIGIN = new Vec3(8.0, 0.0, 8.0);
    public static final Vec3 ATTACHMENT_DEFAULT_ORIGIN = new Vec3(8.0, 8.0, 8.0);
    public static final Vec3 DEFAULT_SCALE = new Vec3(1.0, 1.0, 1.0);

    public static void resetCache()
    {
        ObjectCache.getInstance(CACHE_KEY).reset();
    }

    private static DataObject getCustomData(ItemStack stack)
    {
        // First try to get data from attachment data
        if(stack.getItem() instanceof IMeta)
        {
            return MetaLoader.getInstance().getData(stack.getItem());
        }
        // Otherwise try to get the data from the model
        return FrameworkClientAPI.getOpenModelData(stack, null, null, 0);
    }

    public static Vec3 getScopeCamera(ItemStack stack)
    {
        // Retrieve position from the model's data
        DataObject customObject = PropertyHelper.getCustomData(stack);
        if(customObject.has(SCOPE_KEY, DataType.OBJECT))
        {
            DataObject scopeObject = customObject.getDataObject(SCOPE_KEY);
            if(scopeObject.has("camera", DataType.ARRAY))
            {
                DataArray cameraArray = scopeObject.getDataArray("camera");
                return arrayToVec3(cameraArray, Vec3.ZERO);
            }
        }

        // Old method of getting the camera position
        if(stack.getItem() instanceof IScope scope)
        {
            Scope properties = scope.getProperties();
            return new Vec3(0, properties.getReticleOffset(), (properties.getViewFinderDistance()) * 16.0).add(ATTACHMENT_DEFAULT_ORIGIN); // 0.72 is magic number I decided to add long ago. Here for backwards compat.
        }

        return ATTACHMENT_DEFAULT_ORIGIN;
    }

    public static Vec3 getIronSightCamera(ItemStack stack, Gun modifiedGun, Vec3 gunOrigin)
    {
        // Retrieve position from the model's data
        DataObject ironSightObject = getObjectByPath(stack, WEAPON_KEY, "ironSight");
        if(ironSightObject.has("camera", DataType.ARRAY))
        {
            DataArray cameraArray = ironSightObject.getDataArray("camera");
            return arrayToVec3(cameraArray, Vec3.ZERO);
        }
        var zoom = modifiedGun.getModules().getZoom();
        if(zoom != null)
        {
            double cameraX = 8 - modifiedGun.getModules().getZoom().getXOffset();
            double cameraY = modifiedGun.getModules().getZoom().getYOffset();
            double cameraZ = 8 - modifiedGun.getModules().getZoom().getZOffset();
            return new Vec3(cameraX, cameraY, cameraZ);
        }
        return Vec3.ZERO;
    }

    public static boolean isLegacyIronSight(ItemStack stack)
    {
        DataObject ironSightObject = getObjectByPath(stack, WEAPON_KEY, "ironSight");
        return !ironSightObject.has("camera", DataType.ARRAY);
    }

    public static Vec3 getModelOrigin(ItemStack stack, Vec3 defaultOrigin)
    {
        // Retrieve position from the model's data
        DataObject customObject = PropertyHelper.getCustomData(stack);
        if(customObject.has(MODEL_KEY, DataType.OBJECT))
        {
            DataObject modelObject = customObject.getDataObject(MODEL_KEY);
            if(modelObject.has("origin", DataType.ARRAY))
            {
                DataArray originArray = modelObject.getDataArray("origin");
                return arrayToVec3(originArray, defaultOrigin);
            }
        }
        return defaultOrigin;
    }

    public static Vec3 getAttachmentPosition(ItemStack stack, Gun modifiedGun, IAttachment.Type type)
    {
        DataObject scopeObject = getObjectByPath(stack, WEAPON_KEY, "attachments", type.getSerializeKey());
        if(scopeObject.has("translation", DataType.ARRAY))
        {
            DataArray translationArray = scopeObject.getDataArray("translation");
            return arrayToVec3(translationArray, Vec3.ZERO);
        }
        Gun.ScaledPositioned positioned = modifiedGun.getAttachmentPosition(type);
        if(positioned != null)
        {
            double displayX = positioned.getXOffset();
            double displayY = positioned.getYOffset();
            double displayZ = positioned.getZOffset();
            return new Vec3(displayX, displayY, displayZ);
        }
        return Vec3.ZERO;
    }

    public static Vec3 getAttachmentScale(ItemStack stack, Gun modifiedGun, IAttachment.Type type)
    {
        DataObject scopeObject = getObjectByPath(stack, WEAPON_KEY, "attachments", type.getSerializeKey());
        if(scopeObject.has("scale", DataType.ARRAY))
        {
            DataArray scaleArray = scopeObject.getDataArray("scale");
            return arrayToVec3(scaleArray, DEFAULT_SCALE);
        }
        Gun.ScaledPositioned positioned = modifiedGun.getAttachmentPosition(type);
        if(positioned != null)
        {
            return new Vec3(positioned.getScale(), positioned.getScale(), positioned.getScale());
        }
        return DEFAULT_SCALE;
    }

    public static int getScopeReticleColor(ItemStack stack)
    {
        // Prioritise getting the reticle colour from the ItemStack tag
        CompoundTag tag = stack.getTag();
        if(tag != null && tag.contains("ReticleColor", Tag.TAG_INT))
        {
            return tag.getInt("ReticleColor");
        }

        // Attempt to get the colour from Open Model or CGM Meta
        DataObject customObject = PropertyHelper.getCustomData(stack);
        if(customObject.has(SCOPE_KEY, DataType.OBJECT))
        {
            DataObject scopeObject = customObject.getDataObject(SCOPE_KEY);
            if(scopeObject.has("reticleColor", DataType.NUMBER))
            {
                return scopeObject.getDataNumber("reticleColor").asInt();
            }
        }

        // Default is red
        return 0xFFFF0000;
    }

    public static SightAnimation getSightAnimations(ItemStack weapon, Gun modifiedGun)
    {
        // Try and get the animations from the scope
        if(Gun.hasAttachmentEquipped(weapon, modifiedGun, IAttachment.Type.SCOPE))
        {
            ItemStack scopeStack = Gun.getScopeStack(weapon);
            if(scopeStack.getItem() instanceof IScope scope)
            {
                DataObject scopeObject = getObjectByPath(scopeStack, SCOPE_KEY);
                if(scopeObject.get("sightAnimation") instanceof DataObject sightObject)
                {
                    return objectToSightAnimation(sightObject);
                }
                return scope.getProperties().getSightAnimation();
            }
        }

        // Try and get the animations from the weapon
        DataObject customObject = getObjectByPath(weapon, WEAPON_KEY);
        if(customObject.get("sightAnimation") instanceof DataObject sightObject)
        {
            return objectToSightAnimation(sightObject);
        }

        // Fallback to the zoom object
        var zoom = modifiedGun.getModules().getZoom();
        return zoom != null ? zoom.getAnimation() : SightAnimation.DEFAULT;
    }

    private static SightAnimation objectToSightAnimation(DataObject object)
    {
        ObjectCache cache = ObjectCache.getInstance(CACHE_KEY);
        Optional<SightAnimation> cachedValue = cache.get(object.getId());
        return cachedValue.orElseGet(() -> cache.store(object.getId(), () -> {
            return SightAnimation.builder()
                .setViewportCurve(Easings.byName(getString(object, "viewportCurve").orElse("linear")))
                .setSightCurve(Easings.byName(getString(object, "sightCurve").orElse("linear")))
                .setFovCurve(Easings.byName(getString(object, "fovCurve").orElse("linear")))
                .setAimTransformCurve(Easings.byName(getString(object, "aimTransformCurve").orElse("linear")))
                .build();
        }));
    }

    private static Optional<String> getString(DataObject src, String key)
    {
        if(src.has(key, DataType.STRING))
        {
            return Optional.ofNullable(src.getDataString(key).asString());
        }
        return Optional.empty();
    }

    private static DataObject getObjectByPath(ItemStack stack, String ... path)
    {
        DataObject result = PropertyHelper.getCustomData(stack);
        for(String key : path)
        {
            if(result.has(key, DataType.OBJECT))
            {
                result = result.getDataObject(key);
                continue;
            }
            return DataObject.EMPTY;
        }
        return result;
    }

    private static Vec3 arrayToVec3(DataArray array, Vec3 defaultValue)
    {
        // Ignore immediately if not correct length
        if(array.length() != 3)
            return defaultValue;

        // Return cached vector, otherwise convert array and cache the vector
        ObjectCache cache = ObjectCache.getInstance(CACHE_KEY);
        Optional<Vec3> cachedValue = cache.get(array.getId());
        return cachedValue.orElseGet(() -> cache.store(array.getId(), () ->
        {
            if(array.values().stream().allMatch(entry -> entry.getType() == DataType.NUMBER))
            {
                double x = ((DataNumber) array.get(0)).asDouble();
                double y = ((DataNumber) array.get(1)).asDouble();
                double z = ((DataNumber) array.get(2)).asDouble();
                return new Vec3(x, y, z);
            }
            return defaultValue;
        }));


    }
}
