# CloudWatch en Orioneta

Este documento resume como se monitorea Orioneta durante la beta cerrada.

## Que se observa

- Frontend en EC2: CPU, red, status checks y, si el agente esta activo, memoria, disco y logs de Docker.
- Backend en EKS: metricas de nodos y pods mediante Container Insights.
- Logs: grupos `/orioneta/frontend/*` y `/aws/containerinsights/<cluster>/*`.
- Alarmas: CPU alta en frontend, status check fallido, CPU alta en nodos EKS y memoria alta en nodos EKS.

## Como se configura

Desde GitHub Actions se ejecuta el workflow:

```txt
Configure CloudWatch observability
```

Ese workflow usa los secretos de AWS del repositorio, conecta con EKS, intenta activar el add-on `amazon-cloudwatch-observability`, configura el agente en la EC2 del frontend cuando el laboratorio permite asociar un instance profile, y crea el dashboard `Orioneta-Beta-Observability`.

Si la EC2 del frontend no se detecta automaticamente, agrega el secreto `FRONTEND_INSTANCE_ID` en GitHub con el ID de la instancia, por ejemplo `i-xxxxxxxxxxxxxxxxx`, y vuelve a ejecutar el workflow.

## Como acceder para mostrarlo en el video

1. Entrar a AWS Console.
2. Cambiar a la region configurada en `AWS_REGION`.
3. Abrir CloudWatch.
4. Entrar a Dashboards.
5. Abrir `Orioneta-Beta-Observability`.
6. Revisar tambien:
   - CloudWatch > Alarms.
   - CloudWatch > Logs > Log groups.
   - CloudWatch > Container Insights.

## Que explicar

CloudWatch permite ver la salud operativa del sistema sin entrar manualmente a cada servidor. Para Orioneta se usa como capa de observabilidad: el frontend se mide como instancia EC2 y el backend se mide como cluster EKS. Si una metrica supera un umbral, CloudWatch puede dejar una alarma en estado `ALARM`, lo que permite reaccionar antes de que el problema afecte a mas usuarios.
