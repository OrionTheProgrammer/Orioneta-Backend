# Guion de presentación y defensa

Duración objetivo: 12 minutos.

## Distribución del tiempo

| Tiempo | Contenido |
| --- | --- |
| 0:00-0:45 | Presentación del problema y objetivo |
| 0:45-2:15 | Arquitectura y flujo de comunicación |
| 2:15-3:30 | Dockerfile y Docker Compose |
| 3:30-5:15 | Pipeline CI/CD y etiquetas |
| 5:15-6:45 | VPC, EC2, EKS, NLB y persistencia |
| 6:45-7:45 | Secretos, IAM y seguridad |
| 7:45-8:45 | Pruebas y observabilidad |
| 8:45-11:00 | Demostración en vivo |
| 11:00-12:00 | Conclusiones y mejoras |

## Demostración en vivo

1. Abrir <https://orioneta.accesscam.org> y mostrar HTTPS.
2. Iniciar sesión y abrir un chat.
3. Mostrar un mensaje o archivo y explicar REST, RabbitMQ, WebSocket y MinIO.
4. Abrir ambos repositorios y confirmar la rama `main`.
5. Mostrar un Dockerfile, `.dockerignore` y `docker-compose.prod.yml`.
6. Mostrar las ejecuciones exitosas de GitHub Actions.
7. Ejecutar:

```bash
kubectl -n orioneta get deployments
kubectl -n orioneta get pods
kubectl -n orioneta get services
```

8. Mostrar CloudWatch y el estado de la EC2.

## Mensaje técnico breve por tema

### ¿Por qué microservicios?

Cada capacidad tiene responsabilidad y ciclo de despliegue claros. Para el
alcance académico permite demostrar comunicación, eventos y orquestación. La
contrapartida es mayor complejidad operativa, por eso se centralizan versiones,
configuración y pipeline.

### ¿Por qué EKS y no Docker manual?

EKS reconcilia el estado deseado, reinicia pods, aplica probes, ofrece Service
DNS y permite escalar réplicas. Docker manual requiere scripts propios para esas
capacidades.

### ¿Por qué EC2 para el frontend?

Es una solución económica y fácil de observar para archivos estáticos y TLS.
El backend conserva la complejidad de orquestación en EKS. Una evolución natural
sería S3 + CloudFront.

### ¿Por qué Docker Hub?

Era suficiente para repositorios públicos académicos y se integraba con Actions.
Las etiquetas SHA entregan trazabilidad. En una cuenta corporativa se evaluaría
ECR por integración IAM y transferencia privada.

### ¿Cómo se protegen los secretos?

No se versionan. GitHub Secrets los entrega al workflow y este crea el Secret de
Kubernetes. Los logs los enmascaran. En producción se usaría OIDC y Secrets
Manager para evitar credenciales estáticas.

### ¿Cómo se prueba?

`mvn clean verify` ejecuta 68 pruebas y genera JaCoCo. El frontend exige lint y
build. Después del despliegue se comprueban rollout, readiness, HTTPS, OAuth y
WebSocket.

### ¿Qué pasa si un pod falla?

El Deployment crea un reemplazo. Liveness detecta procesos bloqueados y
readiness evita enviar tráfico a instancias no preparadas.

### ¿Cómo escalarían?

Los servicios stateless pueden aumentar réplicas o usar HPA. Para datos se
recomienda migrar a RDS, Amazon MQ, ElastiCache y S3 antes de escalar de forma
horizontal.

## Preguntas probables

**¿Cuál es la diferencia entre CI y CD?**  
CI integra y valida cambios mediante build y pruebas. CD publica artefactos y
despliega automáticamente una versión validada.

**¿Por qué se usan tags latest y SHA?**  
`latest` simplifica el despliegue actual; SHA identifica de forma inmutable el
artefacto de un commit y permite rollback.

**¿Qué diferencia hay entre liveness y readiness?**  
Liveness determina si el contenedor debe reiniciarse. Readiness determina si
puede recibir tráfico.

**¿Qué expone Internet?**  
La EC2 publica 80/443 y EKS publica el Gateway mediante NLB. Bases, brokers y
microservicios usan direcciones internas.

**¿Qué mejorarían primero?**  
OIDC, escaneo Trivy, Container Insights, HPA y servicios de datos administrados.

## Plan ante una falla durante la presentación

- Mantener abiertas las capturas del informe.
- Mostrar los workflows exitosos y el estado Kubernetes registrado.
- Usar los endpoints de health.
- Explicar que AWS Academy usa sesiones temporales y que el código/despliegue
  permanecen reproducibles mediante Actions y Kustomize.

