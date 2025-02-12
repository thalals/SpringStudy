package com.example.prefix_url;

import com.example.prefix_url.config.ApiVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StarController {

    @GetMapping("/star")
    public String fooV1() {

        return "foo v1";
    }

    @ApiVersion("v2")
    @GetMapping("/star")
    public String fooV2() {

        return "foo v2";
    }

    @ApiVersion("v3")
    @GetMapping("/star")
    public String fooV3() {

        return "foo v3";
    }
}
