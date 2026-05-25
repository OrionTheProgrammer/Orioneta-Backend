# Orioneta Backend

Backend de Orioneta basado en microservicios con Spring Boot, Spring Cloud Gateway, BFF, JPA, RabbitMQ, Redis y modulos compartidos.

## Modulos

| Modulo | Puerto | Responsabilidad |
| --- | ---: | --- |
| gateway-service | 8080 | Entrada principal y enrutamiento hacia BFF/servicios |
| bff-service | 8081 | Backend For Frontend para respuestas orientadas al cliente |
| auth-service | 8082 | Registro, login, JWT y refresh token |
| user-service | 8083 | Perfil, foto y estado de usuario |
| contact-service | 8084 | Contactos, bloqueos y busqueda |
| conversation-service | 8085 | Chats, grupos y participantes |
| message-service | 8086 | Envio, edicion, lectura y eliminacion de mensajes |
| notification-service | 8087 | Notificaciones internas o push |
| media-service | 8088 | Archivos, imagenes, audios y documentos |
| realtime-service | 8089 | WebSocket y distribucion de eventos en tiempo real |
| audit-service | 8090 | Registro de eventos importantes |

## Requisitos

- Java 21
- Maven 3.9+
- Docker y Docker Compose

## Primeros pasos

```bash
docker compose up -d
mvn clean install
```

Para levantar un servicio individual:

```bash
mvn -pl auth-service spring-boot:run
```

## Estructura

Los microservicios con negocio usan una arquitectura por capas:

- `domain`: modelos, puertos, servicios de dominio, eventos y excepciones.
- `application`: casos de uso, DTOs, comandos, consultas y mappers.
- `infrastructure`: controladores, persistencia, mensajeria, clientes y configuracion.
- `shared`: constantes y utilidades locales del servicio.

Los modulos `shared/shared-kernel`, `shared/shared-events` y `shared/shared-security` son librerias reutilizables, no aplicaciones ejecutables.

## Documentacion

- [Arquitectura](docs/arquitectura.md)
- [Endpoints](docs/endpoints.md)
- [Decisiones tecnicas](docs/decisiones-tecnicas.md)
- [Pruebas](docs/pruebas.md)
