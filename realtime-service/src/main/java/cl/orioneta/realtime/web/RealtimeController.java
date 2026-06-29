package cl.orioneta.realtime.web;

import cl.orioneta.realtime.dto.UserConnectionDTO;
import cl.orioneta.realtime.service.RealtimeEventDispatcher;
import cl.orioneta.realtime.websocket.WebSocketSessionRegistry;
import cl.orioneta.shared.events.UserStatusChangedEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Endpoints de diagnostico para el servicio de tiempo real.
 */
@RestController
@RequestMapping("/api/realtime")
public class RealtimeController {

    private final WebSocketSessionRegistry sessionRegistry;
    private final RealtimeEventDispatcher realtimeEventDispatcher;

    public RealtimeController(WebSocketSessionRegistry sessionRegistry, RealtimeEventDispatcher realtimeEventDispatcher) {
        this.sessionRegistry = sessionRegistry;
        this.realtimeEventDispatcher = realtimeEventDispatcher;
    }

    /**
     * Lista todas las sesiones WebSocket activas.
     *
     * @return conexiones registradas en memoria.
     */
    @GetMapping("/presence")
    public List<UserConnectionDTO> findActiveConnections() {
        return sessionRegistry.findAllConnections();
    }

    /**
     * Resume el estado online de un usuario concreto.
     *
     * @param userId usuario a consultar.
     * @return estado y sesiones activas para ese usuario.
     */
    @PostMapping("/profile-changed")
    public void notifyProfileChanged(@RequestBody UserStatusChangedEvent event) {
        realtimeEventDispatcher.broadcastProfileChanged(event);
    }

    @GetMapping("/presence/{userId}")
    public Map<String, Object> findUserPresence(@PathVariable UUID userId) {
        List<UserConnectionDTO> connections = sessionRegistry.findConnectionsByUser(userId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("userId", userId);
        response.put("online", !connections.isEmpty());
        response.put("connections", connections);

        return response;
    }
}
