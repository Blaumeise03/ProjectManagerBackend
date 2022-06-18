package de.blaumeise03.projectmanager.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface POJOExtraMapping {
    @Deprecated
    String invokeMethodFrom() default "";
    @Deprecated
    String invokeMethodTo() default "";
    @Deprecated
    String nullCheckMethod() default "NONE";
    @Deprecated
    Class<?> type() default Object.class;

    String to();
}
