package com.example.notfound_backend.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // WebSocket 메시지 브로커를 활성화합니다.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 메시지 브로커를 구성합니다.
     * 클라이언트가 메시지를 보내고 받을 수 있는 경로를 정의합니다.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // "/topic"으로 시작하는 메시지를 구독하는 클라이언트에게 메시지를 라우팅하는 간단한 메모리 기반 메시지 브로커를 활성화합니다.
        // 서버에서 클라이언트로 메시지를 보낼 때 사용됩니다.
        registry.enableSimpleBroker("/topic");

        // "/app"으로 시작하는 메시지는 @MessageMapping 어노테이션이 달린 컨트롤러 메소드로 라우팅됩니다.
        // 클라이언트에서 서버로 메시지를 보낼 때 사용되는 접두사입니다.
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * STOMP 엔드포인트를 등록합니다.
     * 클라이언트가 WebSocket 연결을 맺을 수 있는 URL을 정의합니다.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // "/ws-game" 경로로 WebSocket 연결을 설정합니다.
        // SockJS를 사용하여 WebSocket을 지원하지 않는 브라우저에서도 연결을 가능하게 합니다.
        // setAllowedOriginPatterns("*")는 모든 도메인에서의 접근을 허용합니다. (보안상 주의 필요)
        registry.addEndpoint("/ws-game").setAllowedOriginPatterns("*").withSockJS();
    }

}
