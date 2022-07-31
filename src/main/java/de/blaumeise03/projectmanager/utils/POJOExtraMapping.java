package de.blaumeise03.projectmanager.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Requests an additional mapping of this field. {@link #to()} has to be set to the name of the targeted field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface POJOExtraMapping {
    /**
     * Defines the field of the corresponding class this field should be mapped to.
     *
     * @return the name of the targeted field.
     */
    String to();

    boolean recursive() default false;
}
