package com.example.prefix_url;

import com.example.prefix_url.config.ApiVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StarController {

    @GetMapping("/star")
    public void fooV1() {

    }

    @ApiVersion("v2")
    @GetMapping("/star")
    public void fooV2() {

    }
}
