# Kubernetes

Esta carpeta deja una base lista para desplegar Orioneta con Kustomize.

La estructura principal es:

```txt
k8s/
├── kustomization.yaml          # Punto de entrada del despliegue
├── namespace.yaml              # Namespace orioneta
├── shared-config.yaml          # URLs internas y configuracion comun
├── storageclass.yaml           # StorageClass EBS opcional para clusters con permisos IAM
├── secrets.example.yaml        # Valores de ejemplo, no se aplica por defecto
├── infrastructure.yaml         # PostgreSQL, RabbitMQ, Redis y MinIO para entorno de prueba
├── applications.yaml           # Deployments y Services de microservicios
└── ingress/
    └── orioneta-ingress.yaml   # Entrada opcional si instalas ingress-nginx
```

## 1. Configurar imagenes

Por defecto, Kustomize usa las imagenes publicadas en DockerHub bajo:

```txt
oriontheprogrammer
```

Los `Dockerfile` usan `eclipse-temurin:25-jre-alpine` para mantener imagenes Java 25 mas livianas.

Si necesitas cambiar el usuario DockerHub o el tag:

```bash
cd k8s
kustomize edit set image orioneta-gateway-service=docker.io/TU_USUARIO/orioneta-gateway-service:latest
kustomize edit set image orioneta-bff-service=docker.io/TU_USUARIO/orioneta-bff-service:latest
kustomize edit set image orioneta-auth-service=docker.io/TU_USUARIO/orioneta-auth-service:latest
kustomize edit set image orioneta-user-service=docker.io/TU_USUARIO/orioneta-user-service:latest
kustomize edit set image orioneta-friendship-service=docker.io/TU_USUARIO/orioneta-friendship-service:latest
kustomize edit set image orioneta-conversation-service=docker.io/TU_USUARIO/orioneta-conversation-service:latest
kustomize edit set image orioneta-message-service=docker.io/TU_USUARIO/orioneta-message-service:latest
kustomize edit set image orioneta-realtime-service=docker.io/TU_USUARIO/orioneta-realtime-service:latest
kustomize edit set image orioneta-media-service=docker.io/TU_USUARIO/orioneta-media-service:latest
```

Para los servicios de etapa completa, aplica el mismo patrón:

```txt
notification-service
customization-service
neta-market-service
moderation-service
audit-service
```

## 2. Crear secretos reales

`secrets.example.yaml` existe como referencia y no se aplica desde `kustomization.yaml`.
Para produccion, crea el secret manualmente o desde tu gestor de secretos:

```bash
kubectl -n orioneta create secret generic orioneta-secrets \
  --from-literal=POSTGRES_USER=orioneta \
  --from-literal=POSTGRES_PASSWORD='CAMBIAR' \
  --from-literal=RABBITMQ_DEFAULT_USER=orioneta \
  --from-literal=RABBITMQ_DEFAULT_PASS='CAMBIAR' \
  --from-literal=MINIO_ROOT_USER=orioneta \
  --from-literal=MINIO_ROOT_PASSWORD='CAMBIAR' \
  --from-literal=ORIONETA_JWT_ISSUER=orioneta-auth-service \
  --from-literal=ORIONETA_JWT_SECRET='CAMBIAR_POR_UN_SECRETO_LARGO' \
  --from-literal=GOOGLE_CLIENT_ID='CAMBIAR' \
  --from-literal=GOOGLE_CLIENT_SECRET='CAMBIAR' \
  --from-literal=GITHUB_CLIENT_ID='CAMBIAR' \
  --from-literal=GITHUB_CLIENT_SECRET='CAMBIAR'
```

El secret real debe existir antes de ejecutar `kubectl apply -k k8s`.

## 3. Almacenamiento

`infrastructure.yaml` usa `emptyDir` para PostgreSQL, RabbitMQ, Redis y MinIO. Esto permite levantar el stack en AWS Academy/Lab sin permisos IAM para EBS CSI, pero los datos se pierden si el pod se recrea.

Para produccion, cambia esos volumenes a PVCs y usa `storageclass.yaml` junto al add-on `aws-ebs-csi-driver` configurado con IRSA o EKS Pod Identity.

## 4. Entrada externa

Por defecto, `gateway-service` se publica como `Service` tipo `LoadBalancer`.
En EKS esto crea un NLB internet-facing y expone el puerto `80` hacia el puerto interno `8080` del gateway.

Verifica el hostname con:

```bash
kubectl -n orioneta get svc gateway-service
```

## 5. TLS e Ingress opcional

`ingress/orioneta-ingress.yaml` queda como referencia para cuando instales un controlador `nginx`.
Si lo aplicas manualmente, usa:

```txt
host: orioneta.duckdns.org
secretName: orioneta-tls
ingressClassName: nginx
```

Con cert-manager, el TLS puede crearse con un `Certificate` propio. Sin cert-manager, crea el secret:

```bash
kubectl -n orioneta create secret tls orioneta-tls \
  --cert=/ruta/fullchain.pem \
  --key=/ruta/privkey.pem
```

## 6. Aplicar

Desde la raiz del repo:

```bash
kubectl apply -k k8s
```

Verifica:

```bash
kubectl -n orioneta get pods
kubectl -n orioneta get svc
```

## 7. Health checks

Los microservicios usan Actuator:

```txt
/actuator/health/liveness
/actuator/health/readiness
```

Los probes ya estan configurados en `applications.yaml`.

## 8. Orden mental de arranque

Kubernetes no usa `depends_on` como Docker Compose. Los servicios pueden arrancar en paralelo y reiniciarse hasta que PostgreSQL, RabbitMQ, Redis o MinIO esten listos. Eso es normal.

Para depurar:

```bash
kubectl -n orioneta logs deploy/gateway-service
kubectl -n orioneta describe pod -l app.kubernetes.io/name=message-service
```
