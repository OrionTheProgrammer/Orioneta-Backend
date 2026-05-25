# Decisiones Tecnicas

## Java y Spring

- Java 17 como version base.
- Spring Boot 3.5.x para mantener compatibilidad con Spring Cloud 2025.0.x.
- Maven multi-modulo para compilar todo desde una raiz comun.

## Arquitectura

- Microservicios independientes por responsabilidad.
- BFF para evitar que el frontend tenga que orquestar varias APIs.
- Gateway como entrada unica del sistema.
- Arquitectura hexagonal en servicios con reglas de negocio.

## Datos y mensajeria

- PostgreSQL con una base por microservicio.
- RabbitMQ para eventos entre servicios.
- Contratos de eventos compartidos en `shared-events`.

## Seguridad

- `auth-service` emite y valida tokens.
- Los demas servicios preparan dependencias para funcionar como resource servers.
- Constantes y claims compartidos viven en `shared-security`.
