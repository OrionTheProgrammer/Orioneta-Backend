# Pruebas

## Estrategia inicial

- Pruebas unitarias para dominio y casos de uso.
- Pruebas de integracion para adaptadores de persistencia y controladores.
- Pruebas de mensajeria para publicadores y consumidores RabbitMQ.
- Reporte de cobertura con JaCoCo en servicios principales.

## Comandos

```bash
mvn test
mvn clean verify
```

Para ejecutar un modulo especifico:

```bash
mvn -pl message-service test
```
