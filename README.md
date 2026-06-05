# Orioneta Backend

Backend de Orioneta basado en microservicios con Spring Boot, Spring Cloud Gateway, BFF, JPA, RabbitMQ, Redis y modulos compartidos.

## Modulos

| Modulo | Puerto | Responsabilidad |
| --- | ---: | --- |
| gateway-service | 8080 | Entrada principal y enrutamiento hacia BFF/servicios |
| bff-service | 8081 | Backend For Frontend para respuestas orientadas al cliente |
| auth-service | 8082 | Registro, login, JWT y refresh token |
| user-service | 8083 | Perfil, foto y estado de usuario |
| friendship-service | 8084 | Amistades, solicitudes, bloqueos y busqueda por friend code |
| conversation-service | 8085 | Chats, grupos y participantes |
| message-service | 8086 | Envio, edicion, lectura y eliminacion de mensajes |
| notification-service | 8087 | Notificaciones internas o push |
| customization-service | 8088 | Preferencias visuales, temas, fondos y estilos |
| media-service | 8089 | Archivos, imagenes, audios y documentos |
| neta-market-service | 8090 | Neta Market para templates visuales |
| realtime-service | 8091 | WebSocket y distribucion de eventos en tiempo real |
| moderation-service | 8092 | Revision y aprobacion de contenido subido |
| audit-service | 8093 | Registro de eventos importantes |

## Requisitos

- Java 25 LTS
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

## Imagenes Docker

El pipeline `.github/workflows/dockerhub-images.yml` compila el reactor Maven, ejecuta las pruebas y publica una imagen por microservicio en DockerHub.

Secretos requeridos en GitHub Actions:

```txt
DOCKERHUB_USERNAME
DOCKERHUB_TOKEN
```

Convencion de nombres:

```txt
<DOCKERHUB_USERNAME>/orioneta-auth-service
<DOCKERHUB_USERNAME>/orioneta-user-service
<DOCKERHUB_USERNAME>/orioneta-friendship-service
```

El pipeline publica tags por rama (`develop`, `main`), por SHA corto (`sha-xxxxxxxxxxxx`) y `latest` cuando el commit entra a `main`.

## Despliegue en EC2

El workflow `.github/workflows/dockerhub-images.yml` despliega la pila Docker Compose en una instancia EC2 despues de publicar correctamente las imagenes Docker.

Secretos requeridos:

```txt
EC2_HOST
EC2_PORT
EC2_USER
EC2_SSH_KEY
DOCKERHUB_USERNAME
DOCKERHUB_TOKEN
```

La EC2 debe tener Docker y Docker Compose instalados. El workflow copia estos archivos a `~/orioneta-backend` dentro de la instancia:

```txt
docker-compose.prod.yml
docker/postgres/init/01-create-databases.sql
docker/prometheus/prometheus.prod.yml
```

Luego ejecuta:

```bash
docker compose -f docker-compose.prod.yml pull
docker compose -f docker-compose.prod.yml up -d --remove-orphans
```

El gateway queda publicado en:

```txt
http://<EC2_HOST>:8080
```

Prometheus, Grafana y RabbitMQ Management quedan vinculados a `127.0.0.1` en la EC2 para evitar exponerlos publicamente. Para revisarlos se recomienda usar tunel SSH.

## Pruebas locales con H2 y Swagger

Para probar un microservicio sin PostgreSQL usa el perfil `dev-h2`. Este perfil crea una base H2 en memoria, carga datos de prueba cuando el modulo ya tiene entidades JPA implementadas y habilita la consola H2.

Ejemplo:

```bash
mvn -pl conversation-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
```

Swagger queda disponible en:

```txt
http://localhost:<puerto>/swagger-ui.html
```

La consola H2 queda disponible en:

```txt
http://localhost:<puerto>/h2-console
```

Credenciales H2:

```txt
usuario: sa
password: dejar vacio
```

URLs JDBC del perfil `dev-h2`:

| Modulo | Puerto | JDBC URL |
| --- | ---: | --- |
| auth-service | 8082 | `jdbc:h2:mem:orioneta_auth` |
| user-service | 8083 | `jdbc:h2:mem:orioneta_users` |
| friendship-service | 8084 | `jdbc:h2:mem:orioneta_friendships` |
| conversation-service | 8085 | `jdbc:h2:mem:orioneta_conversations` |
| message-service | 8086 | `jdbc:h2:mem:orioneta_messages` |
| notification-service | 8087 | `jdbc:h2:mem:orioneta_notifications` |
| customization-service | 8088 | `jdbc:h2:mem:orioneta_customization` |
| media-service | 8089 | `jdbc:h2:mem:orioneta_media` |
| neta-market-service | 8090 | `jdbc:h2:mem:orioneta_neta_market` |
| moderation-service | 8092 | `jdbc:h2:mem:orioneta_moderation` |
| audit-service | 8093 | `jdbc:h2:mem:orioneta_audit` |

Comandos utiles para levantar servicios con datos de prueba:

```bash
mvn -pl auth-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
mvn -pl user-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
mvn -pl friendship-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
mvn -pl conversation-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
mvn -pl message-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
mvn -pl notification-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
mvn -pl customization-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
mvn -pl media-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
mvn -pl neta-market-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
mvn -pl moderation-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
mvn -pl audit-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
```

Los IDs de prueba principales son:

```txt
usuario demo 1: 11111111-1111-1111-1111-111111111111
usuario demo 2: 22222222-2222-2222-2222-222222222222
conversacion privada: aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
grupo demo: bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
template aprobado: 60000000-0000-0000-0000-000000000001
```

Usuario demo de auth-service:

```txt
email: orion@orioneta.dev
password: orioneta123
```

## Estructura

Los microservicios con negocio usan una arquitectura por capas. En los servicios mas completos (`auth-service`, `user-service` y `friendship-service`) la capa de aplicacion se llama `app` para mantener el codigo mas directo mientras el proyecto madura:

- `domain`: modelos, puertos, servicios de dominio, eventos y excepciones.
- `app`: DTOs, puertos, servicios de aplicacion y mappers.
- `infrastructure`: controladores, persistencia, mensajeria, clientes externos y configuracion.

Algunos servicios MVP aun usan carpetas `application` porque partieron con casos de uso separados. La regla para futuros cambios es no duplicar estilos dentro del mismo servicio: cada modulo debe tener una sola capa de aplicacion clara.

Los modulos `shared/shared-kernel`, `shared/shared-events` y `shared/shared-security` son librerias reutilizables, no aplicaciones ejecutables.

## Documentacion

- [Arquitectura](docs/arquitectura.md)
- [Arquitectura general](docs/arquitectura-general.md)
- [Arquitectura backend](docs/arquitectura-backend.md)
- [Endpoints](docs/endpoints.md)
- [Eventos RabbitMQ](docs/eventos-rabbitmq.md)
- [Neta Market](docs/neta-market.md)
- [Observabilidad](docs/observabilidad.md)
- [Seguridad](docs/seguridad.md)
- [Decisiones tecnicas](docs/decisiones-tecnicas.md)
- [Pruebas](docs/pruebas.md)
