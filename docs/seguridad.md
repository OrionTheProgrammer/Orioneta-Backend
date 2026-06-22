# Seguridad

## Identidad y acceso

- `auth-service` almacena credenciales y emite JWT propios.
- El Gateway centraliza autenticacion, CORS y rutas publicas.
- Los microservicios usan Spring Security como Resource Server.
- Google y GitHub actuan como proveedores de identidad; Orioneta emite su JWT
  interno despues de validar el inicio de sesion externo.

## Secretos

Los valores sensibles no se versionan. GitHub Secrets almacena credenciales de
Docker Hub, AWS, OAuth y claves de aplicacion. Durante el deploy, el workflow
crea o actualiza `orioneta-secrets` en Kubernetes.

`secrets.example.yaml` y `.env.example` contienen solo nombres y valores de
demostracion.

En una cuenta AWS permanente se debe reemplazar la sesion temporal de AWS
Academy por GitHub OIDC y un rol IAM limitado al cluster y al despliegue.

## Red

- Solo Caddy publica 80/443 en la EC2.
- Solo el Gateway posee un Service tipo LoadBalancer.
- Los microservicios, bases de datos y brokers usan ClusterIP.
- SSH se abre exclusivamente para la IP efimera del runner de GitHub y se
  revoca en el paso final del workflow.
- Las subredes privadas alojan el plano de ejecucion de EKS.

## Contenedores

- Runtime Java basado en `eclipse-temurin:25-jre-alpine`.
- Usuario final `orioneta` sin privilegios.
- Imagen final sin Maven, codigo fuente ni herramientas de compilacion.
- `.dockerignore` limita el contexto al JAR.
- Frontend compilado en Node Alpine y servido desde Nginx Alpine.

## Aplicacion

- HTTPS automatico y redireccion segura con Caddy.
- Validaciones Jakarta para entradas REST.
- Password hashing mediante Spring Security.
- Tokens con expiracion y refresh token.
- Limite de carga de archivos en el proxy.
- Credenciales separadas de los datos publicos de perfil.

## Pendientes de una produccion comercial

- Escaneo automatico con Trivy o Docker Scout.
- Politicas NetworkPolicy entre namespaces/pods.
- AWS WAF y rate limiting distribuido.
- Rotacion automatica con Secrets Manager.
- Centralizacion de logs de aplicacion en CloudWatch Logs.
