package com.example.WebFlux.controller;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

@Component
public class helloRouterFunction {

    @Bean
    RouterFunction hello() {
        HandlerFunction helloHandlerFunction = req -> {
            Mono<String> result = Mono.just("Hello handlerFunction");
            return ResponseEntity.ok().body(result).getBody();
        };

        return RouterFunctions.route(GET("/hello"), helloHandlerFunction);
    }
}