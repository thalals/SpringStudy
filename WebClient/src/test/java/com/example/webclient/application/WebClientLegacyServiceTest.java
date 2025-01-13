package com.example.webclient.application;

import mockwebserver3.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebClientLegacyServiceTest {

    private static MockWebServer mockWebServer;
    private static WebClient mockWebClient;

    @Mock
    JpaRepository repository;

    @InjectMocks
    WebClientLegacyService webClientLegacyService;

    @BeforeEach
    void setUp() throws IOException {

        mockWebServer = new MockWebServer();
        mockWebServer.start();

        mockWebClient = WebClient.builder()
            .baseUrl(mockWebServer.url("/").toString())
            .build();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("요청 성공 테스트")
    void success() {

        // given
        String bodyJson = """
                {
                 "result": true,
                 "message": "",
                 }
            """;

        MockResponse mockResponse = new MockResponse()
            .setBody(bodyJson)
            .addHeader("Content-Type", "application/json")
            .setResponseCode(200);

        mockWebServer.enqueue(mockResponse);

        when(webService.webClient(any())).thenReturn(mockWebClient);

        // when
        Delivery delivery = DeliveryFixture.create();
        Boolean refund = orderEcomService.refund(delivery);

        // then
        Assertions.assertTrue(refund);

    }

}