# Observabilidad

## Aplicacion

Todos los microservicios exponen Spring Boot Actuator:

```text
/actuator/health
/actuator/health/liveness
/actuator/health/readiness
/actuator/metrics
/actuator/prometheus
/actuator/loggers
```

Kubernetes usa liveness y readiness para retirar pods no disponibles y
reiniciarlos cuando corresponde.

## Logs

Los servicios escriben logs estructurados a stdout/stderr, siguiendo el patron
de contenedores. Ejemplos:

```bash
kubectl -n orioneta logs deploy/gateway-service --tail=100
kubectl -n orioneta logs deploy/message-service --since=15m
kubectl -n orioneta describe pod -l app.kubernetes.io/name=media-service
```

GitHub Actions conserva los logs de compilacion, pruebas, publicacion y
rollout. Las ejecuciones verificadas para la entrega son:

- Backend: <https://github.com/OrionTheProgrammer/Orioneta-Backend/actions/runs/27922817629>
- Frontend: <https://github.com/Panditax727/Orioneta-Frontend/actions/runs/27922798798>

## Metricas

En local, `docker-compose.yml` incluye Prometheus y Grafana. Prometheus
consulta los endpoints Actuator.

En AWS, CloudWatch monitorea la EC2 del frontend. En la ventana de evidencia se
observaron:

| Metrica | Resultado |
| --- | --- |
| CPU promedio | Entre 0,16% y 1,13% |
| CPU maxima | 3,44% |
| NetworkIn maximo | 11.240.537 bytes |
| NetworkOut maximo | 389.451 bytes |
| StatusCheckFailed | 0 |

La aplicacion publica ademas la salud del Gateway en:

<https://orioneta.accesscam.org/actuator/health/readiness>

## Alertas recomendadas

- `StatusCheckFailed > 0` durante dos periodos.
- CPU EC2 superior a 80% durante cinco minutos.
- Deployment con replicas disponibles menor a las deseadas.
- Errores HTTP 5xx o reinicios de pod.
- PVC con poco espacio disponible.
