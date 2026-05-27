# Kubernetes

Esta carpeta reserva los manifiestos de despliegue por servicio.

Cada servicio deberia tener:

```txt
deployment.yaml
service.yaml
configmap.yaml
secret.yaml
```

Los probes usan Actuator:

```yaml
livenessProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 8080

readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 8080
```
