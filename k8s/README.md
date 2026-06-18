# Kubernetes

Esta carpeta deja una base lista para desplegar Orioneta con Kustomize.

La estructura principal es:

```txt
k8s/
├── kustomization.yaml          # Punto de entrada del despliegue
├── namespace.yaml              # Namespace orioneta
├── shared-config.yaml          # URLs internas y configuracion comun
├── secrets.example.yaml        # Valores de ejemplo, reemplazar en produccion
├── infrastructure.yaml         # PostgreSQL, RabbitMQ, Redis y MinIO
├── applications.yaml           # Deployments y Services de microservicios
└── ingress/
    └── orioneta-ingress.yaml   # Entrada por gateway-service
```

## 1. Configurar imagenes

Antes de aplicar en un cluster real, cambia el usuario DockerHub y el tag:

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

`secrets.example.yaml` existe para desarrollo y pruebas. Para produccion, crea el secret manualmente o desde tu gestor de secretos:

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

Si creas el secret real por fuera, puedes quitar `secrets.example.yaml` de `kustomization.yaml` antes de aplicar.

## 3. TLS e Ingress

El Ingress usa:

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

## 4. Aplicar

Desde la raiz del repo:

```bash
kubectl apply -k k8s
```

Verifica:

```bash
kubectl -n orioneta get pods
kubectl -n orioneta get svc
kubectl -n orioneta get ingress
```

## 5. Health checks

Los microservicios usan Actuator:

```txt
/actuator/health/liveness
/actuator/health/readiness
```

Los probes ya estan configurados en `applications.yaml`.

## 6. Orden mental de arranque

Kubernetes no usa `depends_on` como Docker Compose. Los servicios pueden arrancar en paralelo y reiniciarse hasta que PostgreSQL, RabbitMQ, Redis o MinIO esten listos. Eso es normal.

Para depurar:

```bash
kubectl -n orioneta logs deploy/gateway-service
kubectl -n orioneta describe pod -l app.kubernetes.io/name=message-service
```
