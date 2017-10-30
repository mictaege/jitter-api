package com.github.mictaege.jitter.api;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
@Repeatable(Alternatives.class)
public @interface Alter {

    String ifActive();

    String with();

    boolean nested() default false;

}