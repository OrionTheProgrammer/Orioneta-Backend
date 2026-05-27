# Seguridad

## Responsabilidades

- `auth-service` registra usuarios, autentica credenciales y emite JWT.
- `gateway-service` valida el acceso de entrada.
- Los servicios internos se configuran como resource servers para validar JWT.

## Dependencias Base

```txt
spring-boot-starter-security
spring-boot-starter-oauth2-resource-server
spring-security-oauth2-jose
```

## Reglas Iniciales

- No guardar credenciales fuera de `auth-service`.
- No mezclar perfil publico con autenticacion.
- Usar roles compartidos desde `shared-security`.
- Mantener secretos fuera de Git mediante variables de entorno o secretos de Kubernetes.
