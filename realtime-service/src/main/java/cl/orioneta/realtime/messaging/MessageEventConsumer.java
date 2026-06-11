package cl.orioneta.realtime.messaging;

import cl.orioneta.realtime.service.RealtimeEventDispatcher;
import org.springframework.stereotype.Component;

@Component
public class MessageEventConsumer {

    private final RealtimeEventDispatcher realtimeEventDispatcher;

    public MessageEventConsumer(RealtimeEventDispatcher realtimeEventDispatcher) {
        this.realtimeEventDispatcher = realtimeEventDispatcher;
    }

    public void broadcastMessageEvent(String payload) {
        realtimeEventDispatcher.dispatchSystemPayload(payload);
    }
}
