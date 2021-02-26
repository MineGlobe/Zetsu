package me.blazingtide.zetsu.permission.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Determines weather a command requires a certain permission or not.
 *
 * Easier to do it in a separate annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionNode {

    String value();

}
