package com.example.WebFlux.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.example.WebFlux.router.handler.ExampleHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RouterController {

    /**
     *  Spring MVC 어노테이션 방식이 아닌, Router 를 이용한 함수형 방식을 지원
     */

    @Bean
    RouterFunction<ServerResponse> hello() {
        HandlerFunction<ServerResponse> helloHandlerFunction = req -> {
            Mono<String> result = Mono.just("Hello handlerFunction");
            return ServerResponse.ok().body(result, String.class);
        };

        return RouterFunctions.route(GET("/hello"), helloHandlerFunction);
    }

    /**
     * Request, Response CURD 예제
     */
    @Bean
    RouterFunction<ServerResponse> routerExample(ExampleHandler postHandler) {
        return RouterFunctions.route()
                .GET("/post/{id}", request -> postHandler.getById(request))
                .POST("/post", postHandler::create)
                .POST("/post/json", accept(MediaType.APPLICATION_JSON), postHandler::createFromJson)
                .build(); //5
    }
}