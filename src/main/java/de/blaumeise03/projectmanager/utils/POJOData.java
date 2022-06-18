package de.blaumeise03.projectmanager.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface POJOData {
    @Deprecated(since = "0.2")
    String invokeMethod() default "NONE";

    boolean blocked() default false;

    String to() default "";
}
