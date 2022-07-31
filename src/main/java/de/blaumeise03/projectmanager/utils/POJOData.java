package de.blaumeise03.projectmanager.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a field as POJO Data.
 *
 * This field will be mapped to it's corresponding fiel in the corresponding class
 * @see POJO
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface POJOData {
    /**
     * Defines if this field is blocked for mapping. The {@link POJOMapper} will not map this field onto the
     * corresponding object.
     *
     * @return if the field should be ignored.
     */
    boolean blocked() default false;

    /**
     * Defines the name of the field of the target object. By default, it has the same name as this field.
     *
     * @return the name of the target field or "" if the name should be the same.
     */
    String to() default "";

    boolean recursive() default false;
}
