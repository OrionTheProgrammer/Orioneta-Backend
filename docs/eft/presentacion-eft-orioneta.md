# Presentación EFT Orioneta

## 1. Portada

- Orioneta: CI/CD y despliegue en Amazon EKS.
- ISY1101 - Introducción a Herramientas DevOps.
- Integrantes y URL pública.

## 2. Objetivo y alcance

- Automatizar frontend, backend y datos.
- Contenedores reproducibles.
- Despliegue orquestado y observable.
- Demostración de mensajería real.

## 3. Arquitectura desplegada

- Dynu y HTTPS.
- EC2 para Caddy + React/Nginx.
- NLB y Gateway.
- EKS privado con 14 microservicios.
- PostgreSQL, RabbitMQ, Redis y MinIO.

## 4. Integración

- Rutas same-origin.
- REST para operaciones directas.
- RabbitMQ para eventos.
- WebSocket para tiempo real.
- Base lógica por servicio.

## 5. Contenedores y Compose

- Java 25 JRE Alpine, multi-stage y non-root.
- Node/Nginx multietapa.
- .dockerignore.
- Compose de infraestructura y Compose completo por perfiles.

## 6. Pipeline CI/CD

- Push a main.
- Detección de cambios.
- Build y test.
- Docker Hub latest + SHA.
- Deploy EKS/EC2.
- Rollout y healthcheck.

## 7. Infraestructura AWS

- VPC 10.42.0.0/16.
- Dos AZ, subredes públicas y privadas.
- EKS 1.36.
- NLB internet-facing.
- EBS gp3 cifrado.

## 8. Seguridad

- GitHub/Kubernetes Secrets.
- JWT interno y OAuth2.
- HTTPS.
- Puertos mínimos.
- SSH temporal del runner.
- Contenedores sin root.

## 9. Observabilidad y calidad

- 68 pruebas sin fallos.
- JaCoCo.
- Actuator y probes.
- GitHub Actions logs.
- CloudWatch CPU/red/estado.

## 10. Evidencia funcional

- 14/14 Deployments disponibles.
- Frontend HTTP 200.
- Gateway UP.
- OAuth Google/GitHub.
- WebSocket WSS.

## 11. Demostración

- Mostrar aplicación.
- Mostrar repositorios y commits.
- Mostrar Compose y Dockerfile.
- Mostrar Actions.
- Mostrar kubectl y CloudWatch.

## 12. Conclusiones

- Entrega automatizada y trazable.
- EKS aporta resiliencia y escalabilidad.
- Próximos pasos: OIDC, Trivy, Container Insights y HPA.

