# Observabilidad

Cada microservicio expone Actuator y Prometheus.

## Endpoints

```txt
/actuator/health
/actuator/info
/actuator/metrics
/actuator/prometheus
/actuator/loggers
/actuator/health/liveness
/actuator/health/readiness
```

## Desarrollo Local

`docker-compose.yml` incluye:

- Prometheus en `http://localhost:9090`
- Grafana en `http://localhost:3000`
- SonarQube en `http://localhost:9000`

## Logs

En desarrollo:

```txt
root=INFO
org.springframework=WARN
org.hibernate=WARN
cl.orioneta=DEBUG
```
