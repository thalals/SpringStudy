package com.example.webclient.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final BeanConfig beanConfig;

    @GetMapping("")
    public String test() {

        beanConfig.get();
        return "test";
    }
}
