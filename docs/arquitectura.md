# Arquitectura

Orioneta se organiza como un backend multi-modulo Maven. El `pom.xml` raiz tiene `packaging` tipo `pom` y declara cada microservicio como modulo.

## Flujo principal

```mermaid
flowchart LR
    FE[Frontend] --> GW[gateway-service]
    GW --> BFF[bff-service]
    BFF --> AUTH[auth-service]
    BFF --> USER[user-service]
    BFF --> CONV[conversation-service]
    BFF --> MSG[message-service]
    MSG --> RMQ[(RabbitMQ)]
    CONV --> RMQ
    RMQ --> RT[realtime-service]
    RMQ --> NOTIF[notification-service]
    RT --> REDIS[(Redis)]
    AUTH --> DBA[(orioneta_auth)]
    USER --> DBU[(orioneta_users)]
    CONV --> DBC[(orioneta_conversations)]
    MSG --> DBM[(orioneta_messages)]
```

## Capas por microservicio

- `domain`: negocio puro y puertos, sin depender de controladores ni persistencia.
- `application`: casos de uso que coordinan comandos, consultas, DTOs y mappers.
- `infrastructure`: adaptadores de entrada/salida como REST, JPA, RabbitMQ, clientes HTTP y configuracion.

## Modulos compartidos

- `shared-kernel`: excepciones, respuestas API y utilidades comunes.
- `shared-events`: contratos de eventos entre servicios.
- `shared-security`: objetos comunes de autenticacion y roles.
