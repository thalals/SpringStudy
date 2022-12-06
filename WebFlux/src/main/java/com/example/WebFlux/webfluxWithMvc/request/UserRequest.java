package com.example.WebFlux.webfluxWithMvc.request;

import lombok.Value;

@Value
public class UserRequest {
    Integer userNumber;
    String name;
    Integer age;
}
