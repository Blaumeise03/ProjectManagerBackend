package de.blaumeise03.projectmanager.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a POJO class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface POJO {
    /**
     * Defines the corresponding class for this class.
     *
     * @return The corresponding class.
     */
    Class<?> mappingClass();
}
