package com.example.webclient.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientUtil {
    public static WebClient getBaseUrl(final String uri) {
        return WebClient.builder()
            .baseUrl(uri)
            .defaultHeader(
                HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .build()
            .mutate()
            .build();
    }
}
