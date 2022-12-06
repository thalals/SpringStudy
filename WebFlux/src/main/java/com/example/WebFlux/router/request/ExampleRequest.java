package com.example.WebFlux.router.request;

import lombok.Value;

@Value
public class ExampleRequest {
    Long id;
    String title;
    String content;
}
