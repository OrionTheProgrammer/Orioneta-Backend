# Orioneta Backend

Backend de Orioneta construido como un monorepo Maven de microservicios con
Spring Boot, Spring Cloud Gateway, PostgreSQL, RabbitMQ, Redis y MinIO.

## Enlaces

- Aplicacion: <https://orioneta.accesscam.org>
- Backend: <https://github.com/OrionTheProgrammer/Orioneta-Backend>
- Frontend: <https://github.com/Panditax727/Orioneta-Frontend>
- Rama de integracion y produccion: `main`

## Arquitectura

```text
Navegador
   |
   | HTTPS / WebSocket
   v
EC2 Frontend (Caddy + React/Nginx)
   |
   | /api, /oauth2, /ws
   v
AWS Network Load Balancer
   |
   v
API Gateway en Amazon EKS
   |
   +--> BFF y microservicios Spring Boot
   +--> PostgreSQL: una base logica por servicio
   +--> RabbitMQ: eventos asincronos
   +--> Redis: presencia y sesiones realtime
   +--> MinIO: archivos y contenido multimedia
```

Todos los servicios internos de Kubernetes son `ClusterIP`. Solo el Gateway
se publica mediante un `Service LoadBalancer`. El frontend entrega una unica
URL HTTPS y Caddy enruta API y WebSocket al balanceador de EKS.

## Modulos

| Modulo | Puerto | Responsabilidad |
| --- | ---: | --- |
| gateway-service | 8080 | Entrada publica, CORS, seguridad y enrutamiento |
| bff-service | 8081 | Respuestas adaptadas al frontend |
| auth-service | 8082 | Registro, login, OAuth2, JWT y refresh token |
| user-service | 8083 | Perfil, avatar, friend code y estado |
| friendship-service | 8084 | Solicitudes, amistades y bloqueos |
| conversation-service | 8085 | Chats privados, grupos y participantes |
| message-service | 8086 | Mensajes, estados y eventos |
| notification-service | 8087 | Notificaciones persistentes |
| customization-service | 8088 | Preferencias visuales |
| media-service | 8089 | Metadatos y archivos almacenados en MinIO |
| neta-market-service | 8090 | Catalogo de templates |
| realtime-service | 8091 | WebSocket y presencia con Redis |
| moderation-service | 8092 | Revision de contenido |
| audit-service | 8093 | Trazabilidad de eventos |

Los modulos `shared-kernel`, `shared-events` y `shared-security` son
librerias y no aplicaciones ejecutables.

## Requisitos

- Java 25 LTS
- Maven 3.9 o superior
- Docker Engine y Docker Compose v2

## Desarrollo local

### Solo infraestructura

Este modo permite ejecutar los microservicios desde el IDE:

```bash
docker compose up -d
mvn clean verify
mvn -pl user-service spring-boot:run
```

`docker-compose.yml` inicia PostgreSQL, RabbitMQ, Redis, MinIO, Prometheus,
Grafana y SonarQube.

### Plataforma completa en contenedores

`docker-compose.prod.yml` integra frontend, proxy, todos los microservicios y
la infraestructura. Usa las imagenes publicadas en Docker Hub.

```bash
cp .env.example .env
docker compose -f docker-compose.prod.yml \
  --profile messaging \
  --profile realtime \
  --profile customization \
  --profile media \
  --profile market \
  --profile audit \
  --profile observability \
  up -d
```

La aplicacion queda disponible en <http://localhost:5173>. El proxy Caddy
mantiene frontend, REST y WebSocket bajo el mismo origen.

Para revisar el estado:

```bash
docker compose -f docker-compose.prod.yml ps
curl http://localhost:5173/actuator/health/readiness
```

## Contenedores

Los Dockerfile Java separan la seleccion del artefacto y la imagen runtime. La
imagen final utiliza `eclipse-temurin:25-jre-alpine`, contiene solo el JAR,
declara el puerto del servicio y ejecuta con el usuario sin privilegios
`orioneta`. Cada modulo incluye un `.dockerignore` que limita el contexto al
Dockerfile y al artefacto compilado.

El frontend utiliza un Dockerfile multietapa:

1. Node Alpine instala dependencias y genera `dist`.
2. Nginx Alpine sirve solo los archivos estaticos.

## Pruebas

```bash
mvn clean verify
```

La compilacion actual ejecuta 68 pruebas sin fallos. JaCoCo genera reportes por
servicio en `target/site/jacoco`. Los servicios con persistencia tambien
disponen del perfil `dev-h2` para pruebas rapidas sin PostgreSQL.

Ejemplo:

```bash
mvn -pl user-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
```

## CI/CD

El workflow [dockerhub-images.yml](.github/workflows/dockerhub-images.yml) se
ejecuta al publicar cambios en `main`:

1. Detecta los microservicios modificados.
2. Compila el reactor Maven y ejecuta las pruebas.
3. Construye las imagenes de forma secuencial para controlar el consumo.
4. Publica `latest` y `sha-<commit>` en Docker Hub.
5. Autentica contra AWS usando GitHub Secrets.
6. Aplica Kustomize, actualiza imagenes y espera cada rollout de EKS.

El frontend posee un workflow independiente que construye su imagen, la publica
en Docker Hub y actualiza la instancia EC2 mediante una regla SSH temporal.

## Kubernetes y AWS

Los manifiestos de `k8s/` definen:

- Deployments y Services para 14 microservicios.
- StatefulSets y volumenes EBS cifrados para PostgreSQL, RabbitMQ, Redis y MinIO.
- ConfigMap para URLs internas y configuracion no sensible.
- Secret de Kubernetes creado durante el pipeline.
- Probes de liveness/readiness y limites de recursos.
- Network Load Balancer internet-facing para el Gateway.

Comandos de verificacion:

```bash
kubectl -n orioneta get deployments
kubectl -n orioneta get pods
kubectl -n orioneta get services
```

## Seguridad

- Secretos fuera de Git mediante GitHub Secrets y Kubernetes Secrets.
- HTTPS automatico con Caddy.
- JWT firmado por `auth-service`.
- Servicios internos no publicados a Internet.
- Contenedores Java sin usuario root.
- Imagenes Alpine y contextos Docker reducidos.
- Security Group del frontend limitado a HTTP/HTTPS; SSH se abre para la IP
  efimera del runner y se revoca al terminar.
- Credenciales AWS Academy temporales; en una cuenta permanente se recomienda
  GitHub OIDC con un rol IAM de minimo privilegio.

## Observabilidad

- Actuator: `/actuator/health`, `/liveness`, `/readiness` y
  `/prometheus`.
- Logs de aplicacion consultables con `kubectl logs`.
- Prometheus y Grafana disponibles en el Compose local.
- CloudWatch entrega CPU, red y verificaciones de estado de la EC2.
- GitHub Actions conserva logs de build, test, publicacion y despliegue.

## Documentacion

- [Arquitectura AWS](docs/arquitectura-aws.md)
- [Arquitectura backend](docs/arquitectura-backend.md)
- [Endpoints](docs/endpoints.md)
- [Eventos RabbitMQ](docs/eventos-rabbitmq.md)
- [Observabilidad](docs/observabilidad.md)
- [Seguridad](docs/seguridad.md)
- [Pruebas](docs/pruebas.md)
- [Encargo EFT](docs/eft/README.md)
