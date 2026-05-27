# Arquitectura Backend

El backend es un monorepo Maven multi-modulo con Spring Boot y Spring Cloud.

## Flujo De Alto Nivel

```txt
Cliente
  -> gateway-service
  -> bff-service
  -> microservicios de dominio
  -> RabbitMQ para eventos
  -> realtime-service para WebSocket
```

## Servicios Activos

```txt
gateway-service
bff-service
auth-service
user-service
friendship-service
conversation-service
message-service
realtime-service
notification-service
customization-service
neta-market-service
media-service
moderation-service
audit-service
```

## Capas Internas

- `domain`: modelos, puertos, servicios de dominio, eventos y excepciones.
- `application`: casos de uso, DTOs, comandos, consultas y mappers.
- `infrastructure`: REST, RabbitMQ, JPA, clientes HTTP y configuracion Spring.

## Modulos Compartidos

- `shared-kernel`: respuestas, excepciones y utilidades comunes.
- `shared-events`: contratos de eventos publicados por RabbitMQ.
- `shared-security`: claims, usuario autenticado y roles.
- `shared-observability`: constantes y soporte comun para metricas/trazas.
