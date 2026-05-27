# Eventos RabbitMQ

RabbitMQ desacopla acciones importantes entre microservicios.

## Eventos Iniciales

| Evento | Publica | Consume |
| --- | --- | --- |
| `FriendRequestSentEvent` | `friendship-service` | `notification-service`, `realtime-service`, `audit-service` |
| `FriendRequestAcceptedEvent` | `friendship-service` | `notification-service`, `realtime-service`, `audit-service` |
| `ConversationCreatedEvent` | `conversation-service` | `notification-service`, `audit-service` |
| `GroupInvitationEvent` | `conversation-service` | `notification-service`, `realtime-service` |
| `MessageSentEvent` | `message-service` | `realtime-service`, `notification-service`, `audit-service` |
| `MessageReadEvent` | `message-service` | `realtime-service`, `audit-service` |
| `TemplateSubmittedEvent` | `neta-market-service` | `moderation-service`, `audit-service` |
| `TemplateApprovedEvent` | `moderation-service` | `neta-market-service`, `notification-service`, `audit-service` |

## Convencion

- Los eventos compartidos viven en `shared/shared-events`.
- Los eventos deben incluir identificadores, usuario actor cuando aplique y `occurredAt`.
- Los consumidores deben ser idempotentes.
