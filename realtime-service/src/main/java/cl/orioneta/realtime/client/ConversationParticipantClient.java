package cl.orioneta.realtime.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Cliente interno para obtener participantes de una conversacion.
 *
 * <p>Realtime necesita esta informacion para enviar cada evento solo a los
 * usuarios que pertenecen al chat. La logica de conversaciones sigue viviendo
 * en conversation-service; este cliente solo lee la respuesta publica de su API.</p>
 */
@Component
public class ConversationParticipantClient {

    private final RestClient restClient;

    public ConversationParticipantClient(
            RestClient.Builder restClientBuilder,
            @Value("${orioneta.services.conversations:http://localhost:8085}") String conversationServiceUrl
    ) {
        this.restClient = restClientBuilder.baseUrl(conversationServiceUrl).build();
    }

    /**
     * Busca los usuarios activos de una conversacion.
     *
     * @param conversationId identificador de la conversacion.
     * @return lista sin duplicados de participantes no eliminados.
     */
    public List<UUID> findParticipantIds(UUID conversationId) {
        try {
            ConversationResponse response = restClient.get()
                    .uri("/api/conversations/{id}", conversationId)
                    .retrieve()
                    .body(ConversationResponse.class);

            if (response == null || response.participants() == null) {
                return List.of();
            }

            return response.participants()
                    .stream()
                    .filter(participant -> !participant.deletedForUser())
                    .map(ParticipantResponse::userId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
        } catch (RestClientException exception) {
            throw new IllegalStateException("No se pudieron obtener los participantes de la conversacion", exception);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ConversationResponse(List<ParticipantResponse> participants) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ParticipantResponse(UUID userId, boolean deletedForUser) {
    }
}
