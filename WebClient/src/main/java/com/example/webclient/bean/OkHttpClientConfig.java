package com.example.webclient.bean;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpClientConfig {

    @Primary
    @Bean
    public OkHttpClient okHttpClient() {

        return new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();
    }

    @Bean
    public OkHttpClient okHttpClientLongConnection() {

        return new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .build();
    }
}
