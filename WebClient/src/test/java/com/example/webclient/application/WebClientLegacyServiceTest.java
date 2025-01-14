package com.example.webclient.application;

import com.example.webclient.util.WebClientUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
                 "message": ""
                 }
            """;

        MockResponse mockResponse = new MockResponse()
            .setBody(bodyJson)
            .addHeader("Content-Type", "application/json")
            .setResponseCode(200);

        mockWebServer.enqueue(mockResponse);

        when(WebClientUtil.getBaseUrl(any())).thenReturn(mockWebClient);

        // when
        boolean result = webClientLegacyService.callWebClient();

        // then
        Assertions.assertTrue(result);

    }

    @Test
    @DisplayName("자사몰 환불 요청 실패 테스트 - 400 error")
    void refund_fail_for400() {

        // given
        String bodyJson = """
                {
                 "result": false
                 }
            """;

        MockResponse mockResponse = new MockResponse()
            .setBody(bodyJson)
            .addHeader("Content-Type", "application/json")
            .setResponseCode(400);

        mockWebServer.enqueue(mockResponse);

        when(webService.webClient(any())).thenReturn(mockWebClient);

        // when
        Delivery delivery = DeliveryFixture.create();

        // then
        assertThatThrownBy(() -> orderEcomService.refund(delivery))
            .isInstanceOf(EcomApiException.class)
            .hasMessage("4xx 자사몰 시스템 호출 오류.");

    }

    @Test
    @DisplayName("자사몰 환불 요청 실패 테스트 - 500 error")
    void refund_fail_for500() {

        // given
        String bodyJson = """
                {
                 "result": false
                 }
            """;

        MockResponse mockResponse = new MockResponse()
            .setBody(bodyJson)
            .addHeader("Content-Type", "application/json")
            .setResponseCode(500);

        mockWebServer.enqueue(mockResponse);

        when(webService.webClient(any())).thenReturn(mockWebClient);

        // when
        Delivery delivery = DeliveryFixture.create();

        // then
        assertThatThrownBy(() -> orderEcomService.refund(delivery))
            .isInstanceOf(EcomApiException.class)
            .hasMessage("5xx 자사몰 시스템 호출 오류.");
    }

    @Test
    @DisplayName("자사몰 환불 요청 실패 테스트 - 결과 false")
    void refund_fail() {

        // given
        String bodyJson = """
                {
                  "result" : false,
                  "status" : 400,
                  "message" : "refund error",
                  "subCode" : "INVALID_ACCESS"
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

        // then
        assertThatThrownBy(() -> orderEcomService.refund(delivery))
            .isInstanceOf(EcomApiException.class)
            .hasMessage("refund error");
    }

}