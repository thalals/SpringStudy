package com.example.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //해당 파라미터의 접두사가 붙은 목적지(구독자)에 메시지를 보낼
        registry.enableSimpleBroker("/topic");

        //전역적인 주소 접두사 지정 하기싫으면 ("/") 으로 두면 됨
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //"/gs-guide-websocket" 이라는 엔드포인트 추가 등록
        registry.addEndpoint("/gs-guide-websocket").addInterceptors().withSockJS();
    }
}
