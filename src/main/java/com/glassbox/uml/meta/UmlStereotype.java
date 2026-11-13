package com.glassbox.uml.meta; import java.lang.annotation.*; @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) public @interface UmlStereotype{String value();}
