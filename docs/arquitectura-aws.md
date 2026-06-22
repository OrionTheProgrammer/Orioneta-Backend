# Arquitectura AWS desplegada

## Resumen

Orioneta utiliza dos planos de ejecucion dentro de la misma VPC:

- Una instancia EC2 `t3.small` entrega el frontend React mediante Nginx y
  Caddy. Caddy gestiona TLS y funciona como proxy same-origin.
- Un cluster Amazon EKS ejecuta los microservicios, PostgreSQL, RabbitMQ, Redis
  y MinIO. Un Network Load Balancer publica solamente el Gateway.

La URL publica es <https://orioneta.accesscam.org>.

## Red

| Recurso | Valor desplegado |
| --- | --- |
| Region | `us-east-1` |
| VPC | `vpc-0962b479974fb073f` |
| CIDR VPC | `10.42.0.0/16` |
| Subred publica A | `10.42.0.0/20` (`us-east-1a`) |
| Subred publica B | `10.42.16.0/20` (`us-east-1b`) |
| Subred privada A | `10.42.128.0/20` (`us-east-1a`) |
| Subred privada B | `10.42.144.0/20` (`us-east-1b`) |

EKS usa las dos subredes privadas. La EC2 del frontend esta en una subred
publica y recibe una IP publica. Esta separacion evita asignar IP publica a cada
microservicio.

## Componentes

| Componente | Implementacion |
| --- | --- |
| DNS | Dynu: `orioneta.accesscam.org` |
| TLS y reverse proxy | Caddy en EC2 |
| Frontend | React/Nginx en Docker |
| Orquestacion backend | Amazon EKS 1.36 |
| Entrada backend | Network Load Balancer internet-facing |
| Persistencia | PostgreSQL sobre EBS `gp3` cifrado |
| Eventos | RabbitMQ sobre StatefulSet |
| Presencia | Redis sobre StatefulSet |
| Multimedia | MinIO sobre StatefulSet |
| Registro de imagenes | Docker Hub |
| CI/CD | GitHub Actions |

## Flujo de una solicitud

1. Dynu resuelve el dominio hacia la EC2.
2. Caddy termina HTTPS y sirve el frontend.
3. Las rutas `/api`, `/oauth2`, `/actuator` y `/ws` se envian al NLB.
4. El NLB entrega la solicitud a `gateway-service` en EKS.
5. El Gateway enruta al BFF o al microservicio responsable por DNS interno.
6. El servicio accede a su base logica o publica un evento en RabbitMQ.

## Escalabilidad

EKS fue elegido porque entrega reconciliacion declarativa, reinicio automatico,
probes, actualizaciones controladas y escalado horizontal. Frente a un despliegue
manual con `docker run`, Kubernetes permite aumentar replicas sin cambiar las
URLs internas ni reconfigurar el frontend.

El laboratorio mantiene una replica por servicio para controlar costos. Los
Deployments estan preparados para elevar `replicas` o incorporar HPA cuando se
disponga de metricas y capacidad adicional.

## Persistencia

PostgreSQL, RabbitMQ, Redis y MinIO usan `StatefulSet` y
`volumeClaimTemplates`. La clase `ebs-gp3` solicita volumenes EBS cifrados.
Por tratarse de un entorno academico se despliegan dentro del cluster; para una
produccion de mayor criticidad se recomienda RDS, Amazon MQ, ElastiCache y S3.
