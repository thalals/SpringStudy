package com.example.WebFlux.router.response;

import lombok.Value;

@Value
public class ExampleResponse {
    Long id;
    String title;
    String content;
}
