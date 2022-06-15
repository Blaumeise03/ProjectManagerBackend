package de.blaumeise03.projectmanager.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface POJOExtraMapping {
    String invokeMethodFrom();
    String invokeMethodTo();
    String nullCheckMethod() default "NONE";
    Class<?> type();
}
