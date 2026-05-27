# Decisiones Tecnicas

## Java y Spring

- Java 21 como version base.
- Spring Boot 3.5.x para mantener compatibilidad con Spring Cloud 2025.0.x.
- Maven multi-modulo para compilar todo desde una raiz comun.

## Arquitectura

- Microservicios independientes por responsabilidad.
- BFF para evitar que el frontend tenga que orquestar varias APIs.
- Gateway como entrada unica del sistema.
- Arquitectura hexagonal en servicios con reglas de negocio.
- `friendship-service` reemplaza el alcance de contactos para separar identidad de relaciones sociales.
- `customization-service` y `neta-market-service` separan personalizacion de identidad, mensajes y grupos.

## Datos y mensajeria

- PostgreSQL con una base por microservicio.
- RabbitMQ para eventos entre servicios.
- Redis para presencia y sesiones del servicio de tiempo real.
- Contratos de eventos compartidos en `shared-events`.

## Seguridad

- `auth-service` emite y valida tokens.
- Los demas servicios preparan dependencias para funcionar como resource servers.
- Constantes y claims compartidos viven en `shared-security`.
