package com.example.webclient.application;

import com.example.webclient.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WebClientLegacyService {

    private final JpaRepository repository;

    public boolean callWebClient() {

        String string = WebClientUtil.getBaseUrl("/test/할수/없는/주소지롱")
            .put()
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                Mono.error(new RuntimeException("400 에러 입니다"))
            )
            .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                Mono.error(new RuntimeException("500 에러 입니다.")))
            .bodyToMono(String.class)
            .block();

        return Objects.nonNull(string);
    }
}
