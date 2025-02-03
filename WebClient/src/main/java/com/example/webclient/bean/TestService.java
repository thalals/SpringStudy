package com.example.webclient.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TestService {

    private final OkHttpClient okHttpClient;

    @Qualifier("okHttpClientLongConnection")
    private final OkHttpClient okHttpClientLongConnection;

    private final OkHttpClientConfig okHttpClientConfig;

    public void hello2() {
        OkHttpClient okHttpClient1 = okHttpClientConfig.okHttpClient();
        OkHttpClient okHttpClient2 = okHttpClientConfig.okHttpClientLongConnection();
    }

    public void hello() throws IOException {
        var response = okHttpClient.newCall(null).execute();
        var response2 = okHttpClientLongConnection.newCall(null).execute();
    }
}
