package com.example.WebFlux.router.handler;

import com.example.WebFlux.router.request.ExampleRequest;
import com.example.WebFlux.router.response.ExampleResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class ExampleHandler {

    /**
     *     path variable 추출
     */

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        ExampleRequest post = new ExampleRequest(id, "garden", "hello");

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(post);
    }

    /**
     *     x-www-form-urlencoded 추출 (fromData)
     */

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<MultiValueMap<String, String>> formData = request.formData();

        return formData.flatMap(data -> {
            Map<String, String> dataMap = data.toSingleValueMap();
            String title = dataMap.getOrDefault("title", null);
            String content = dataMap.getOrDefault("content", null);

            ExampleResponse response = new ExampleResponse(1L, title, content);

            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response);
        });
    }

    /**
     *     json data 추출 (Json to dto)
     */
    public Mono<ServerResponse> createFromJson(ServerRequest request) {
        Mono<ExampleResponse> response = request.bodyToMono(ExampleResponse.class);
        return response.flatMap(post ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(post));
    }
}
