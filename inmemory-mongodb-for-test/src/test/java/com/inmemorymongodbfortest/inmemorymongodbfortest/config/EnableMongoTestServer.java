package com.inmemorymongodbfortest.inmemorymongodbfortest.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MongoTestServerConfig.class)
public @interface EnableMongoTestServer {

}
