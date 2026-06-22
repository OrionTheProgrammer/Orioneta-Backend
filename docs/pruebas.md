# Pruebas

## Estrategia

La piramide de pruebas combina:

- Dominio y servicios de aplicacion con JUnit 5 y Mockito.
- Datos realistas de prueba con DataFaker.
- Adaptadores y controladores con el soporte de Spring Boot Test.
- Seguridad con Spring Security Test.
- Mensajeria con Spring Rabbit Test.
- Cobertura mediante JaCoCo.

## Resultado verificado

El comando de entrega es:

```bash
mvn -B clean verify
```

Resultado del 21 de junio de 2026:

| Indicador | Resultado |
| --- | ---: |
| Archivos de reporte Surefire | 22 |
| Pruebas ejecutadas | 68 |
| Fallos | 0 |
| Errores | 0 |
| Omitidas | 0 |
| Cobertura de instrucciones JaCoCo | 37,11% |

La cobertura se interpreta como linea base, no como garantia aislada de
calidad. Los flujos criticos de usuario, amistad, conversaciones, mensajes,
media y autenticacion poseen pruebas; el siguiente objetivo es elevar cobertura
en controladores y configuracion.

## Pruebas de despliegue

Despues del rollout se verifican:

```bash
curl https://orioneta.accesscam.org/
curl https://orioneta.accesscam.org/actuator/health/readiness
curl https://orioneta.accesscam.org/api/auth/oauth2/providers
```

Tambien se realiza una apertura WebSocket a
`wss://orioneta.accesscam.org/ws/chat`. En la validacion de entrega, frontend,
readiness, OAuth y WebSocket respondieron correctamente.

## Perfil H2

Los servicios persistentes incluyen `dev-h2` para pruebas manuales rapidas:

```bash
mvn -pl user-service spring-boot:run -Dspring-boot.run.profiles=dev-h2
```
