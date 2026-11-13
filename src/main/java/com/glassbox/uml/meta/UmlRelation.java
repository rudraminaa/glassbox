package com.glassbox.uml.meta;
import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface UmlRelation {
enum Type { COMPOSITION, AGGREGATION, ASSOCIATION, DEPENDENCY }
Type type();
Class<?> target();
}