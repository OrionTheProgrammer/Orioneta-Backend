# Arquitectura AWS

Esta guia deja una ruta de despliegue futura. El desarrollo local sigue usando Docker Compose.

## Mapeo Sugerido

| Necesidad | AWS |
| --- | --- |
| Contenedores | ECS Fargate o EKS |
| Imagenes Docker | ECR |
| PostgreSQL | RDS PostgreSQL |
| RabbitMQ | Amazon MQ for RabbitMQ |
| Redis | ElastiCache Redis |
| Archivos | S3 |
| Secretos | Secrets Manager |
| Logs | CloudWatch Logs |
| Metricas | CloudWatch o Prometheus gestionado |

## Consideraciones

- Cada microservicio debe tener variables de entorno por ambiente.
- Los health checks deben usar `/actuator/health/liveness` y `/actuator/health/readiness`.
- `media-service` puede evolucionar desde almacenamiento local a S3 sin cambiar el resto del sistema.
