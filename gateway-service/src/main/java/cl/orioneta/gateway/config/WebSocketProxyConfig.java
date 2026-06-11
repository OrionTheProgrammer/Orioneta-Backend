package cl.orioneta.gateway.config;

import cl.orioneta.gateway.websocket.RealtimeWebSocketProxyHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Expone el WebSocket de realtime desde el gateway.
 */
@Configuration
@EnableWebSocket
public class WebSocketProxyConfig implements WebSocketConfigurer {

    private final RealtimeWebSocketProxyHandler realtimeWebSocketProxyHandler;

    public WebSocketProxyConfig(RealtimeWebSocketProxyHandler realtimeWebSocketProxyHandler) {
        this.realtimeWebSocketProxyHandler = realtimeWebSocketProxyHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(realtimeWebSocketProxyHandler, "/ws/chat")
                .setAllowedOriginPatterns("*");
    }
}
