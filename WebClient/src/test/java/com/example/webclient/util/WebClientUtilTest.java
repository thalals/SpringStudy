package com.example.webclient.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

class WebClientUtilTest {

    private static final String KAKAO_REST_API_KEY = "key";
    private static final String REDIRECT_URI = "http://localhost:8080/user/kakao/callback";

    @Test
    @DisplayName("WebClient build Singleton 사용하기 - kakao api get")
    void getKakaoToken() {
        WebClient webClient = WebClientUtil.getBaseUrl(REDIRECT_URI);
        String token = webClient.get()
            .uri(
                "/oauth/authorize?client_id=" + KAKAO_REST_API_KEY
                    + "&redirect_uri=" + REDIRECT_URI
                    + "&response_type=code")
            .retrieve()
            .bodyToMono(String.class)
            .block();
        System.out.println("token = " + token);
        Assertions.assertThat(token).isNotNull();
    }
}