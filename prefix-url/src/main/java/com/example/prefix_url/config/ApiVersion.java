package com.example.prefix_url.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {

    /**
     * API 버전을 지정합니다.
     * <br> ex | "v1, v2, v3"
     */

    String value() default "";
}