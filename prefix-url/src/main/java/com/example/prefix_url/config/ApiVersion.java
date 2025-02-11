package com.example.prefix_url.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {
    /**
     * API 버전을 지정합니다. 예를 들어 "v2"
     */
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String value() default "";
}