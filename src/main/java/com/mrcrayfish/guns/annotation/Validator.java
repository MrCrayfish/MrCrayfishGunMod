package com.mrcrayfish.guns.annotation;

import javax.annotation.Nonnull;
import java.io.InvalidObjectException;
import java.lang.reflect.Field;

/**
 * Author: MrCrayfish
 */
public class Validator
{
    /**
     * Validates that the deserialized object's required fields are not null. This is an abstracted
     * method and can be used for validating any deserialized object.
     *
     * @param t   the object to validate
     * @param <T> any type
     * @return true if the object is valid
     * @throws IllegalAccessException if it's unable to access a field. This should never happen
     * @throws InvalidObjectException if the object's required fields are null
     */
    public static <T> boolean isValidObject(@Nonnull T t) throws IllegalAccessException, InvalidObjectException
    {
        Field[] fields = t.getClass().getDeclaredFields();
        for(Field field : fields)
        {
            if(field.getDeclaredAnnotation(Optional.class) != null)
            {
                continue;
            }
            
            field.setAccessible(true);

            if(field.get(t) == null)
            {
                throw new InvalidObjectException("Missing required property: " + field.getName());
            }

            if(!field.getType().isPrimitive() && field.getType() != String.class && !field.getType().isEnum() && field.getDeclaredAnnotation(Ignored.class) == null)
            {
                return isValidObject(field.get(t));
            }
        }
        return true;
    }
}
