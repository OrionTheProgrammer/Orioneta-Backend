# Guion avanzado de presentación y defensa técnica

Este guion está diseñado para una exposición de hasta 30 minutos y una defensa
posterior. No está pensado para memorizarse palabra por palabra. La idea es que
ambos integrantes comprendan el relato completo, puedan intercambiar secciones
y respondan con precisión incluso si el docente interrumpe o cambia el orden.

## 1. Cómo usar este documento

1. Lean una vez el guion completo para comprender el hilo.
2. Marquen las frases que cada integrante dirá de forma natural.
3. Practiquen las transiciones y la demostración, no solo las diapositivas.
4. Ambos deben estudiar el banco de preguntas completo, porque la defensa es
   individual.
5. No intenten ocultar limitaciones. Expliquen por qué existen y cuál es la
   mejora técnica concreta.

## 2. Reparto sugerido

El reparto puede invertirse. Los nombres Integrante A e Integrante B permiten
adaptarlo sin reescribir el documento.

| Responsable | Secciones principales |
| --- | --- |
| Integrante A | Apertura, evolución del proyecto, backend, Docker, CI/CD y pruebas |
| Integrante B | Frontend, integración, AWS, Kubernetes, seguridad y demostración |
| Ambos | Conclusiones y defensa técnica |

Cada integrante debe ser capaz de explicar las secciones del otro.

## 3. Distribución recomendada de los 30 minutos

| Tiempo | Tema |
| --- | --- |
| 0:00-1:30 | Apertura, problema y resultado |
| 1:30-4:00 | Evolución cronológica del proyecto |
| 4:00-6:30 | Producto y arquitectura de microservicios |
| 6:30-9:00 | Flujos de integración |
| 9:00-11:30 | Contenedores y Docker Compose |
| 11:30-14:30 | GitHub Flow, registro de imágenes y CI/CD |
| 14:30-18:00 | VPC, EC2, EKS, NLB y almacenamiento |
| 18:00-20:30 | Kubernetes y escalabilidad |
| 20:30-23:00 | Seguridad, secretos e IAM |
| 23:00-25:00 | Pruebas y observabilidad |
| 25:00-28:30 | Demostración en vivo |
| 28:30-30:00 | Resultados, limitaciones y cierre |

## 4. Datos que deben conocer sin mirar apuntes

| Dato | Valor |
| --- | --- |
| URL pública | <https://orioneta.accesscam.org> |
| Repositorio backend | `OrionTheProgrammer/Orioneta-Backend` |
| Repositorio frontend | `Panditax727/Orioneta-Frontend` |
| Rama productiva | `main` |
| Java | 25 LTS |
| Spring Boot | 3.5.x |
| Microservicios desplegados | 14 |
| Pruebas backend | 68, sin fallos ni errores |
| Cobertura JaCoCo medida | 37,11% de instrucciones |
| Región AWS | `us-east-1` |
| VPC | `10.42.0.0/16` |
| Cluster | `orioneta-eks`, Kubernetes 1.36 |
| Frontend | EC2 `t3.small`, Caddy y React/Nginx |
| Entrada backend | Network Load Balancer hacia Gateway |
| Datos | PostgreSQL, RabbitMQ, Redis y MinIO |

No es necesario memorizar IDs de VPC, subredes o instancias. Deben poder
explicar su función y encontrarlos en AWS si se los solicitan.

---

# Parte I. Guion completo de exposición

## 5. Apertura

### Diapositiva 1: Portada

**Integrante A dice:**

> Buenos días. Somos el equipo Orioneta y presentaremos el resultado final de
> nuestro proyecto para Introducción a Herramientas DevOps. Orioneta es una
> plataforma de mensajería privada con amistades, grupos, archivos y
> comunicación en tiempo real.

> El foco de esta evaluación no es solamente que la aplicación funcione. El
> objetivo fue construir un proceso reproducible que toma un cambio en Git,
> ejecuta pruebas, crea imágenes, las publica y despliega automáticamente una
> versión accesible por HTTPS en AWS.

**Integrante B continúa:**

> La aplicación está disponible en orioneta.accesscam.org. Actualmente el
> frontend se ejecuta en una instancia EC2 y el backend completo está
> orquestado en Amazon EKS. Durante la presentación mostraremos el recorrido
> desde las primeras decisiones de arquitectura hasta el despliegue y la
> verificación final.

**Idea que debe quedar clara:** el proyecto se presenta como un sistema
funcional y como una cadena DevOps completa.

## 6. Problema inicial y objetivo

### Diapositiva 2: Objetivo y alcance

**Integrante A dice:**

> Al inicio teníamos dos desafíos. El primero era funcional: construir una
> plataforma con autenticación, perfiles, amistades, conversaciones, mensajes,
> multimedia y tiempo real. El segundo era operacional: evitar que cada versión
> dependiera de compilar, copiar archivos y reiniciar servidores manualmente.

> Definimos cinco objetivos DevOps. Primero, mantener el código trazable en Git.
> Segundo, validar automáticamente cada cambio. Tercero, empaquetar frontend y
> backend en contenedores. Cuarto, publicar imágenes identificables. Quinto,
> desplegar y verificar el sistema en la nube sin intervención manual.

**Integrante B dice:**

> El resultado verificable es que el frontend responde por HTTPS, el Gateway
> reporta estado UP, Google y GitHub aparecen como proveedores OAuth, el
> WebSocket abre correctamente y los catorce Deployments de EKS están
> disponibles.

### Pregunta que puede aparecer aquí

**¿Qué significa que el proceso sea reproducible?**

Respuesta:

> Significa que la fuente de verdad está en Git. Otra persona con Docker, Maven
> y las variables documentadas puede construir el mismo sistema. En nube, los
> manifiestos y workflows describen cómo obtener el mismo despliegue sin
> depender de pasos recordados por una persona.

## 7. Evolución cronológica: desde lo primero hasta lo último

Esta sección puede explicarse sin una diapositiva exclusiva, usando el historial
de commits si el docente desea ver evolución real.

### Etapa 1: base técnica y monorepo Maven

**Integrante A dice:**

> Lo primero fue crear el backend como un monorepo Maven multi-módulo. El POM
> raíz centraliza Java, Spring Boot, Spring Cloud, JaCoCo y los módulos. Esta
> decisión permite ejecutar un solo `mvn clean verify` y validar el reactor
> completo.

> También se definieron módulos compartidos para contratos y seguridad. Se evitó
> convertir shared en un lugar de negocio común; son librerías pequeñas para
> tipos transversales.

### Etapa 2: experimentación de arquitectura en user-service

> El primer servicio desarrollado en profundidad fue user-service. Lo usamos
> para experimentar con separación entre dominio, aplicación e infraestructura.
> El dominio contiene modelos y reglas puras; la aplicación coordina operaciones;
> infraestructura adapta JPA, HTTP y configuración.

> La arquitectura se simplificó deliberadamente. No agregamos una interfaz por
> cada método ni capas sin una responsabilidad real. La regla fue que el dominio
> no dependiera de Spring o JPA, mientras el resto permaneciera legible para el
> equipo.

### Etapa 3: identidad, amistades y autenticación

> Después se implementaron friendship-service y auth-service. Friendship separa
> relaciones sociales del perfil del usuario. Auth separa credenciales de datos
> públicos y emite JWT propios.

> Se añadió inicio de sesión local, refresh token y OAuth2 con Google y GitHub.
> Los proveedores externos validan la identidad, pero Orioneta emite su propio
> JWT. Esto evita que los microservicios dependan del formato de token de cada
> proveedor.

### Etapa 4: conversaciones, mensajes y eventos

> Luego se completaron conversation-service y message-service. Conversation
> administra chats, grupos y participantes. Message conserva mensajes y estados.
> RabbitMQ se incorporó para desacoplar envío, notificaciones, realtime y
> auditoría.

> Esta etapa incluyó correcciones reales de persistencia y del chat privado. El
> historial muestra que el desarrollo no fue una generación lineal: se probaron
> flujos, aparecieron errores y se corrigieron con commits pequeños y
> descriptivos.

### Etapa 5: servicios complementarios

> Se agregaron notification, customization, Neta Market, moderation, audit,
> realtime y media. Redis se usa en realtime para presencia; MinIO se usa en
> media para objetos. La base relacional guarda metadatos, no el contenido
> binario.

### Etapa 6: pruebas locales y Swagger

> Antes de desplegar se creó el perfil dev-h2 para levantar servicios con una
> base en memoria y datos de prueba. Swagger permitió probar endpoints sin
> depender todavía del frontend. Más adelante se agregaron pruebas con JUnit,
> Mockito y DataFaker.

### Etapa 7: integración del frontend

**Integrante B dice:**

> En paralelo, el frontend evolucionó desde una maqueta a un cliente conectado al
> Gateway. Se integraron sesión, perfiles, amistades, chats, WebSocket,
> personalización, archivos, MinIO y llamadas WebRTC.

> También se corrigieron problemas reales, como sesiones cruzadas entre pestañas,
> conversaciones duplicadas, mensajes optimistas que desaparecían y pérdida de
> foco por actualizaciones completas de la página.

### Etapa 8: contenedores y despliegue inicial

> La primera aproximación de nube utilizó Docker Compose en EC2. Fue útil para
> validar imágenes, variables y comunicación entre servicios. Sin embargo, una
> sola máquina limitaba memoria, escalabilidad y aislamiento.

### Etapa 9: migración a EKS

> El backend se migró a EKS. Se crearon Deployments, Services, StatefulSets,
> ConfigMaps, Secrets, probes, recursos y volúmenes EBS. El Gateway se publicó
> mediante NLB y el resto permaneció interno.

### Etapa 10: dominio, HTTPS y endurecimiento final

> DuckDNS presentó problemas, por lo que migramos el DNS a Dynu y configuramos
> orioneta.accesscam.org. Caddy automatiza HTTPS y unifica frontend, API y
> WebSocket bajo el mismo origen.

> Finalmente se endurecieron los Dockerfile Java: dos etapas, JRE Alpine,
> usuario sin privilegios y contextos reducidos con .dockerignore. Se actualizaron
> GitHub Actions, se generaron evidencias y se verificó nuevamente todo el
> despliegue.

### Frase de cierre de la cronología

> Esta evolución demuestra que las decisiones no se tomaron aisladas. Cada etapa
> resolvió un problema observado en la anterior: primero estructura, luego
> funcionalidad, después automatización, finalmente operación y seguridad.

## 8. Arquitectura funcional de microservicios

### Diapositiva 3: Arquitectura desplegada

**Integrante A dice:**

> El backend se dividió por capacidades de negocio, no por tablas ni por capas
> técnicas globales. Auth maneja credenciales; User maneja identidad pública;
> Friendship maneja relaciones; Conversation administra participantes; Message
> persiste mensajes; Media administra archivos; Realtime entrega eventos.

**Integrante B complementa:**

> Gateway es la entrada técnica y BFF adapta respuestas para el frontend. Los
> servicios de notificaciones, personalización, Neta Market, moderación y
> auditoría se mantienen independientes porque tienen datos y ritmos de cambio
> diferentes.

### Responsabilidad de cada servicio

| Servicio | Responsabilidad principal | Dependencias relevantes |
| --- | --- | --- |
| gateway-service | Entrada, rutas, CORS y proxy WebSocket | Todos los endpoints internos |
| bff-service | Agregación para el frontend | User, Friendship, Conversation, Message |
| auth-service | Registro, login, OAuth2, JWT y refresh | PostgreSQL |
| user-service | Perfil, avatar, friend code y estado | PostgreSQL |
| friendship-service | Solicitudes, amistades y bloqueos | User, Conversation, RabbitMQ |
| conversation-service | Chats, grupos y participantes | PostgreSQL, RabbitMQ |
| message-service | Mensajes y estados | Conversation, PostgreSQL, RabbitMQ |
| notification-service | Notificaciones persistentes | PostgreSQL, RabbitMQ |
| customization-service | Preferencias visuales | PostgreSQL |
| media-service | Metadatos y archivos | PostgreSQL, MinIO |
| neta-market-service | Catálogo de templates | PostgreSQL, RabbitMQ |
| realtime-service | WebSocket y presencia | RabbitMQ, Redis |
| moderation-service | Revisión de contenido | PostgreSQL, RabbitMQ |
| audit-service | Trazabilidad de eventos | PostgreSQL, RabbitMQ |

### Si preguntan por qué no usar un monolito

> Para una primera versión comercial pequeña, un monolito modular sería más
> simple y probablemente más económico. En esta evaluación usamos microservicios
> porque el alcance exige integración, contenedores y orquestación, y porque las
> capacidades tienen límites claros. Reconocemos el costo: más despliegues,
> comunicación remota y observabilidad más compleja.

Esta respuesta es mejor que afirmar que microservicios siempre son superiores.

## 9. Integración: cómo viaja una solicitud

### Diapositiva 4: Cómo se integra el sistema

### 9.1 Flujo de login local

**Integrante B dice:**

> El navegador envía el login a `/api/auth/login`. Caddy reconoce la ruta de
> backend y la reenvía al NLB. El NLB entrega al Gateway y el Gateway enruta a
> auth-service. Auth consulta PostgreSQL, verifica el hash de contraseña y emite
> access token y refresh token.

> El frontend guarda la sesión mediante el mecanismo implementado y adjunta el
> bearer token a las solicitudes protegidas. Los servicios validan el JWT como
> Resource Server.

### 9.2 Flujo OAuth2

> Para Google o GitHub, el usuario inicia el flujo desde Orioneta. Auth-service
> redirige al proveedor. Cuando el proveedor confirma identidad, auth-service
> vincula o crea el usuario y emite los mismos tokens internos que en el login
> local. El resto del sistema no necesita saber si la identidad vino de Google,
> GitHub o contraseña.

### 9.3 Flujo de creación de amistad y chat

> Friendship valida al usuario por friend code. Al aceptar una solicitud,
> registra la amistad y coordina la creación de la conversación privada. Se
> publica un evento para notificaciones y realtime. Este flujo fue importante
> porque originalmente la amistad podía aceptarse sin que apareciera un chat;
> la integración se corrigió en backend y frontend.

### 9.4 Flujo de mensaje

> El frontend envía el mensaje al servicio correspondiente a través del Gateway.
> Message valida que la conversación exista y que el usuario participe, persiste
> el mensaje y publica un evento en RabbitMQ.

> Realtime consume el evento y lo entrega por WebSocket. Notification puede
> generar una notificación y Audit registrar trazabilidad. El frontend actualiza
> el estado local sin recargar toda la página, por lo que no pierde el foco del
> input.

### 9.5 Flujo de archivo

> El archivo se envía a media-service. El binario se guarda en MinIO y los
> metadatos en PostgreSQL. El mensaje conserva una referencia al recurso. Esto
> evita guardar objetos grandes dentro de la base relacional y permite cambiar
> MinIO por S3 sin modificar el dominio de mensajes.

### 9.6 Flujo WebRTC

> Las llamadas usan WebRTC para transportar audio, video o pantalla entre
> navegadores. El backend participa en la señalización mediante realtime, pero
> no debería retransmitir el flujo multimedia completo. En una producción global
> se agregaría TURN para redes donde la conexión directa no sea posible.

### Diferencia que deben explicar

| Tecnología | Uso |
| --- | --- |
| REST | Operación directa con respuesta inmediata |
| RabbitMQ | Evento asíncrono y desacoplado |
| WebSocket | Canal persistente servidor-cliente |
| WebRTC | Audio, video y pantalla entre clientes |

## 10. Contenedores

### Diapositiva 5: Contenedores y entorno local

**Integrante A dice:**

> En backend, Maven compila y prueba primero. El Dockerfile tiene una etapa de
> artefacto y una etapa runtime basada en Eclipse Temurin JRE 25 Alpine. La
> imagen final recibe solo el JAR.

> Creamos un grupo y usuario llamado orioneta y ejecutamos Java sin root. Cada
> servicio declara su puerto y tiene un .dockerignore que permite solamente el
> Dockerfile y el JAR. Esto reduce contexto y evita enviar código o archivos del
> entorno al daemon.

### Fragmento para mostrar

```dockerfile
FROM alpine:3.22 AS artifact
WORKDIR /artifact
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

FROM eclipse-temurin:25-jre-alpine
RUN addgroup -S orioneta && adduser -S -G orioneta orioneta
WORKDIR /app
COPY --from=artifact --chown=orioneta:orioneta /artifact/app.jar app.jar
USER orioneta
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Integrante B explica frontend:**

> El frontend usa una construcción multietapa clásica. Node Alpine instala con
> npm ci y genera dist. La etapa final Nginx Alpine recibe solo los archivos
> estáticos. Las dependencias de desarrollo no llegan a producción.

### 10.1 Por qué Alpine

> Alpine reduce el tamaño y la superficie de paquetes en comparación con una
> distribución completa. No significa que sea automáticamente segura; las
> imágenes igual deben actualizarse y escanearse.

### 10.2 Docker Compose

> `docker-compose.yml` levanta infraestructura para trabajar desde el IDE:
> PostgreSQL, RabbitMQ, Redis, MinIO, Prometheus, Grafana y SonarQube.

> `docker-compose.prod.yml` integra frontend, Caddy, los microservicios y los
> datos. Usa perfiles para no consumir todos los recursos cuando una persona
> necesita solo mensajería o media.

### Comando de demostración

```bash
cp .env.example .env
docker compose -f docker-compose.prod.yml \
  --profile messaging \
  --profile realtime \
  --profile customization \
  --profile media \
  --profile market \
  --profile audit \
  --profile observability \
  config --services
```

### Si preguntan por `depends_on`

> En Compose usamos healthchecks y depends_on para el arranque local. En
> Kubernetes no existe un depends_on equivalente. Los servicios arrancan en
> paralelo y deben tolerar temporalmente que PostgreSQL o RabbitMQ todavía no
> estén listos.

## 11. Git y estrategia de ramas

**Integrante A dice:**

> Usamos GitHub Flow. Las ramas describen intención con prefijos como feature,
> fix o chore. Los commits son pequeños y explican ámbito y propósito. Después
> de validar, se fusionan en main. Main es la rama que activa producción.

> El historial conserva tanto funciones como correcciones. Esto es importante:
> no ocultamos errores mediante un único commit final. Se puede seguir la
> evolución de user-service, conversation-service, chats, realtime, MinIO, EKS,
> DNS y la entrega EFT.

### Qué mostrar

```bash
git log --oneline --decorate -15
git branch --show-current
git status --short
```

### Si preguntan por qué existen dos repositorios

> Backend y frontend tienen toolchains, propietarios y pipelines diferentes.
> Separarlos permite desplegarlos de manera independiente. La contrapartida es
> coordinar contratos API; por eso se documentan endpoints y se centraliza el
> acceso en Gateway/BFF.

## 12. Registro de imágenes

**Integrante A dice:**

> GitHub Actions inicia sesión en Docker Hub usando secretos. En backend, cada
> imagen sigue `orioneta-nombre-servicio`. Publicamos `latest` para el estado
> actual de main y una etiqueta `sha-` con doce caracteres para trazabilidad.
> El frontend publica latest y el SHA completo.

> La etiqueta SHA permite identificar el artefacto de un commit y preparar un
> rollback manual. Latest es conveniente, pero es mutable. Una evolución sería
> desplegar siempre la etiqueta SHA o el digest y conservar latest solo como
> alias humano.

### Si preguntan Docker Hub frente a ECR

> Docker Hub fue suficiente para repositorios públicos académicos y ya estaba
> integrado. ECR sería preferible en una operación AWS permanente por IAM,
> escaneo e integración de red. No afirmamos que Docker Hub sea la única o la
> mejor opción universal.

## 13. Pipeline CI/CD

### Diapositiva 6: Pipeline CI/CD

**Integrante A dice:**

> El pipeline backend se activa con push a main o manualmente. Primero detecta
> servicios modificados. Si cambia un servicio, el POM raíz o infraestructura
> compartida, calcula el alcance correspondiente.

> Después configura Java y caché Maven, ejecuta `mvn -B clean verify`,
> configura Buildx, autentica Docker Hub y construye imágenes de forma
> secuencial. Elegimos secuencial para controlar memoria y evitar saturar el
> runner al construir catorce servicios.

> Si las pruebas o una construcción fallan, no se ejecuta el despliegue. Si todo
> pasa, el workflow configura credenciales AWS, obtiene kubeconfig, crea el
> Secret, aplica Kustomize, actualiza imágenes y espera disponibilidad.

**Integrante B explica frontend:**

> El pipeline frontend ejecuta construcción y publicación. Para desplegar en EC2
> obtiene la IP pública del runner, autoriza temporalmente SSH en el Security
> Group, copia el Caddyfile y actualiza los contenedores.

> La revocación de SSH usa una condición always, por lo que se ejecuta incluso si
> falla el despliegue. Finalmente valida la configuración de Caddy y el
> healthcheck del frontend.

### CI y CD

> CI comprende checkout, compilación, lint y pruebas. CD comienza cuando se
> publica el artefacto y se despliega al entorno. La frontera no depende del
> nombre del archivo, sino del propósito de cada etapa.

### Por qué fallar rápido

> Queremos descubrir un error antes de publicar o modificar producción. Una
> prueba fallida detiene el job por el código de salida. Los jobs posteriores
> dependen del resultado exitoso.

### Concurrency

> El workflow usa concurrency para evitar dos despliegues superpuestos sobre la
> misma referencia. Si llega un cambio más nuevo, se cancela la ejecución
> anterior y se prioriza el estado reciente.

### Rollback

Respuesta completa:

> Tenemos imágenes con SHA, por lo que un rollback manual puede cambiar el
> Deployment a una etiqueta anterior y esperar el rollout. Kubernetes también
> conserva historial de ReplicaSets. Aún no automatizamos rollback por métricas;
> sería una mejora junto con despliegue por tags inmutables, canary o blue/green.

## 14. Infraestructura AWS

### Diapositiva 7: Infraestructura AWS

**Integrante B dice:**

> La infraestructura está en us-east-1 dentro de una VPC 10.42.0.0/16. Hay dos
> subredes públicas y dos privadas distribuidas entre us-east-1a y us-east-1b.

> La EC2 del frontend está en una subred pública porque debe recibir 80 y 443.
> El plano de ejecución de EKS utiliza subredes privadas. El backend se publica
> por un Network Load Balancer que apunta únicamente al Gateway.

### 14.1 DNS y TLS

> Dynu resuelve orioneta.accesscam.org hacia la EC2. Caddy obtiene y renueva el
> certificado TLS. Cuando una ruta corresponde a frontend, la envía a Nginx. Si
> comienza con api, oauth2, actuator o ws, la reenvía al NLB.

> Esta topología same-origin simplifica CORS y habilita APIs del navegador que
> exigen contexto seguro, como cámara, micrófono y compartir pantalla.

### 14.2 EC2

> Se usa una t3.small con Amazon Linux. Ejecuta solo frontend y Caddy en Docker.
> Es suficiente para archivos estáticos y proxy en el laboratorio. Una
> alternativa administrada sería S3 y CloudFront.

### 14.3 EKS

> EKS administra el plano de control de Kubernetes. El cluster orioneta-eks usa
> Kubernetes 1.36. Los manifiestos de aplicación se organizan con Kustomize.

### 14.4 NLB

> Elegimos Network Load Balancer como entrada del Gateway porque entrega un
> endpoint estable y maneja conexiones TCP persistentes. Caddy sigue siendo el
> punto HTTPS público para el usuario.

### 14.5 Persistencia

> PostgreSQL, RabbitMQ, Redis y MinIO se ejecutan como StatefulSets con PVC sobre
> EBS gp3 cifrado. Esto conserva datos si un pod se reemplaza.

> Para una producción comercial moveríamos estos componentes a RDS, Amazon MQ,
> ElastiCache y S3. Ejecutarlos en EKS reduce costos y complejidad de cuenta en el
> laboratorio, pero aumenta responsabilidad operativa.

## 15. Kubernetes

### Diapositiva 7 o evidencia funcional

**Integrante B dice:**

> Cada microservicio stateless usa un Deployment y un Service ClusterIP. Los
> componentes con estado usan StatefulSet. El Gateway tiene un Service
> LoadBalancer. ConfigMap almacena configuración no sensible y Secret almacena
> valores sensibles.

### Conceptos que deben dominar

**Pod**

> Es la unidad mínima de ejecución de Kubernetes. Puede contener uno o más
> contenedores que comparten red y volúmenes.

**Deployment**

> Administra pods stateless mediante ReplicaSets, conserva el estado deseado y
> permite rollouts.

**StatefulSet**

> Entrega identidad y almacenamiento estable a componentes con estado.

**Service**

> Proporciona una dirección estable y balancea tráfico hacia pods seleccionados
> por etiquetas.

**ConfigMap y Secret**

> ConfigMap contiene configuración no sensible. Secret contiene datos sensibles
> codificados para ser inyectados. Base64 no es cifrado por sí mismo; el control
> real depende de RBAC, cifrado en reposo y acceso al cluster.

**PVC**

> Es una solicitud de almacenamiento. La StorageClass provisiona el volumen EBS.

### Liveness, readiness y startup

> Startup da tiempo a un servicio lento para comenzar. Liveness determina si
> Kubernetes debe reiniciar el contenedor. Readiness determina si debe recibir
> tráfico. Una aplicación puede estar viva pero no lista, por ejemplo mientras
> espera la base.

### Resources

> Requests ayudan al scheduler a reservar capacidad. Limits restringen consumo.
> Definimos memoria y CPU de forma conservadora para el laboratorio. En una
> producción se ajustarían con métricas y pruebas de carga.

### Escalabilidad

> Los servicios stateless pueden aumentar replicas. Con Metrics Server se puede
> agregar HPA por CPU, memoria o métricas personalizadas. NLB y Services
> distribuyen tráfico sin que el frontend conozca cada pod.

### Alta disponibilidad real

> Tener subredes en dos zonas prepara la red, pero una sola réplica y una sola
> instancia de base no constituyen alta disponibilidad completa. Para eso se
> necesitan múltiples replicas, PodDisruptionBudget, nodos en más de una zona y
> servicios de datos replicados.

Esta precisión evita exagerar la solución.

## 16. Seguridad

### Diapositiva 8: Seguridad y secretos

**Integrante B dice:**

> Aplicamos seguridad en varias capas. En transporte usamos HTTPS. En identidad,
> auth-service emite JWT. En red, solo se exponen Caddy y Gateway. En
> contenedores, Java se ejecuta sin root. En configuración, las credenciales no
> se guardan en Git.

### 16.1 Secretos

> GitHub Secrets contiene credenciales de Docker Hub, AWS, OAuth y SSH. El
> workflow las inyecta en tiempo de ejecución y GitHub enmascara su valor en
> logs. Para Kubernetes, el workflow crea orioneta-secrets y los pods consumen
> claves específicas.

### 16.2 IAM

> AWS Academy entrega una sesión temporal con un rol. Para una cuenta permanente
> usaríamos GitHub OIDC. Así GitHub solicitaría credenciales de corta duración
> para un rol de despliegue y no almacenaríamos access keys.

> El rol debería tener mínimo privilegio: describir el cluster, autenticarse y
> modificar los recursos necesarios, no permisos administrativos generales.

### 16.3 Security Groups

> La EC2 publica 80 y 443. SSH no queda abierto globalmente: el workflow agrega
> la IP /32 del runner y la revoca al terminar. Los datos y microservicios no
> publican puertos directamente.

### 16.4 Contenedores

> El usuario non-root reduce el impacto de una vulnerabilidad. Alpine reduce
> paquetes. .dockerignore evita incorporar secretos o archivos locales. Falta
> integrar escaneo automático de CVE, que proponemos con Trivy o Docker Scout.

### 16.5 JWT

> Un JWT está firmado, no necesariamente cifrado. No debe contener contraseñas ni
> información sensible. Incluye identidad, rol, emisor y expiración. Los
> servicios validan firma, issuer y vencimiento.

### 16.6 CORS y CSRF

> CORS controla qué orígenes del navegador pueden llamar a la API; no reemplaza
> autenticación. En producción usamos same-origin mediante Caddy. Para una API
> stateless con bearer token, CSRF tiene un riesgo diferente al de cookies de
> sesión; aun así se revisa según cómo se almacenen y envíen tokens.

## 17. Pruebas y calidad

### Diapositiva 9: Pruebas y observabilidad

**Integrante A dice:**

> El backend ejecuta 68 pruebas sin fallos ni errores. Usamos JUnit 5, Mockito y
> DataFaker. JaCoCo genera reportes por servicio. El frontend ejecuta ESLint y
> build de Vite.

> Las pruebas cubren dominio y casos de uso relevantes. La cobertura global de
> instrucciones medida es 37,11%. No presentamos ese número como alto; es una
> línea base honesta. Parte del código no cubierto corresponde a configuración,
> controladores y adaptadores.

### Si preguntan por qué no hay 80% de cobertura

> Priorizamos flujos de negocio críticos dentro del tiempo del proyecto. La
> cobertura mide ejecución, no calidad de aserciones. El siguiente paso es
> agregar pruebas de integración para controladores, persistencia, seguridad y
> contratos entre servicios, y definir un umbral progresivo en CI.

### Tipos de prueba

| Tipo | Ejemplo |
| --- | --- |
| Unitaria | Regla de User o caso de uso con Mockito |
| Integración | Adaptador JPA con H2/Testcontainers |
| Seguridad | Endpoint con roles o token inválido |
| Contrato | Compatibilidad entre BFF y servicios |
| End-to-end | Registro, amistad, chat y mensaje desde navegador |
| Operacional | Healthcheck, rollout y conexión WebSocket |

### SonarQube

> El proyecto tiene plugin de Sonar y Compose local incluye SonarQube. La
> ejecución principal de la entrega se concentra en Maven, JaCoCo y Actions.
> Una mejora es incorporar un quality gate obligatorio en el pipeline.

## 18. Observabilidad

**Integrante A dice:**

> Actuator expone health, liveness, readiness, métricas y Prometheus. Kubernetes
> usa los endpoints de salud para operar pods. Los logs van a stdout y pueden
> consultarse con kubectl logs.

> GitHub Actions conserva logs de build, test, publicación y despliegue.
> CloudWatch monitorea la EC2. En la ventana de evidencia la CPU máxima fue
> 3,44%, StatusCheckFailed fue cero y el pico de red coincidió con la descarga
> de imágenes.

### Tres pilares

> Métricas responden cuánto y con qué tendencia. Logs explican eventos
> discretos. Trazas muestran el recorrido de una solicitud entre servicios.
> Actualmente tenemos métricas y logs básicos; las trazas distribuidas son una
> mejora futura con OpenTelemetry.

### Qué alertas pondrían

- StatusCheckFailed mayor que cero.
- CPU o memoria sostenida sobre umbral.
- Réplicas disponibles menores a deseadas.
- Reinicios frecuentes o CrashLoopBackOff.
- HTTP 5xx elevados.
- PVC cerca de capacidad.
- Latencia p95 y cola RabbitMQ creciendo.

## 19. Resultados verificables

### Diapositiva 10: Evidencia de automatización

**Integrante A dice:**

> Esta captura corresponde al pipeline backend final. Detectó cambios, ejecutó
> pruebas, publicó las imágenes y desplegó EKS. La captura frontend muestra
> construcción, publicación y actualización de EC2. Ambas ejecuciones terminaron
> en Success.

**Integrante B dice:**

> Después del despliegue comprobamos catorce Deployments disponibles, pods de
> aplicación e infraestructura en Running, Gateway UP, OAuth disponible y
> apertura WebSocket segura.

### Frase importante

> No consideramos terminado un deploy cuando kubectl acepta el manifiesto. Lo
> consideramos terminado cuando el rollout y las verificaciones funcionales
> confirman disponibilidad.

## 20. Demostración en vivo

### Diapositiva 11: Demostración

La demostración debe ensayarse con las pestañas abiertas antes de presentar.

### Preparación de pestañas

1. Orioneta en login.
2. Orioneta con una sesión ya iniciada, por si falla OAuth.
3. Repositorio backend en Actions.
4. Repositorio frontend en Actions.
5. Dockerfile de auth-service.
6. docker-compose.prod.yml.
7. k8s/applications.yaml.
8. Consola AWS en EKS, EC2 y CloudWatch.
9. Terminal autenticada con kubectl.
10. Informe PDF abierto en evidencias.

### Secuencia exacta

**Paso 1. Aplicación**

> Estamos accediendo por HTTPS al dominio público. El certificado lo gestiona
> Caddy. Esta pantalla se sirve desde React/Nginx en EC2.

Mostrar candado y URL.

**Paso 2. Función de usuario**

> Iniciaremos sesión y abriremos una conversación. Esta operación pasa por
> Caddy, NLB, Gateway y los servicios internos.

Mostrar chat. Si hay dos cuentas disponibles, enviar un mensaje.

**Paso 3. Realtime o archivo**

> El mensaje se persiste antes de confirmarse. RabbitMQ publica el evento y
> realtime lo entrega por WebSocket. Para un archivo, media-service almacena el
> objeto en MinIO y la base conserva metadatos.

No improvisar una videollamada si no hay dos dispositivos preparados.

**Paso 4. Git**

> Aquí está main limpia y el historial descriptivo. La última integración
> corresponde a la entrega y evidencias finales.

**Paso 5. Docker**

> Este Dockerfile muestra las dos etapas y el usuario non-root. Este
> .dockerignore limita el contexto.

**Paso 6. Pipeline**

> Este run demuestra build, test, push y deploy. Las etapas verdes son
> dependientes; si pruebas falla, deploy no se ejecuta.

**Paso 7. Kubernetes**

```bash
kubectl -n orioneta get deployments
kubectl -n orioneta get pods
kubectl -n orioneta get services
```

> Los Deployments muestran una réplica disponible. Solo Gateway es
> LoadBalancer; los demás son ClusterIP.

**Paso 8. Salud**

```bash
curl -s https://orioneta.accesscam.org/actuator/health/readiness
```

> La respuesta UP indica que el Gateway está listo para tráfico.

**Paso 9. CloudWatch**

> CloudWatch muestra CPU, red y verificaciones de estado de la EC2. La evidencia
> final registró cero fallos de estado.

### Qué no hacer en la demo

- No ejecutar un despliegue completo durante la exposición.
- No mostrar el contenido de GitHub Secrets.
- No abrir archivos .env reales.
- No mostrar una llave privada.
- No borrar pods o recursos salvo que se haya ensayado y el docente lo pida.
- No depender de un login OAuth como único camino de entrada.

## 21. Cierre

### Diapositiva 12: Conclusiones

**Integrante A dice:**

> Como conclusión, el proyecto pasó de una estructura inicial de servicios a una
> plataforma integrada con una cadena de entrega automatizada. Cada cambio
> validado produce imágenes trazables y un despliegue verificable.

**Integrante B dice:**

> EKS aporta reconciliación, descubrimiento, probes y escalabilidad. Caddy y la
> separación de red reducen exposición. Actions, Actuator y CloudWatch entregan
> evidencia del estado.

**Ambos cierran:**

> También identificamos mejoras concretas: GitHub OIDC, escaneo Trivy,
> Container Insights, trazas OpenTelemetry, HPA y migración de datos a servicios
> administrados. No presentamos Orioneta como una arquitectura terminada para
> escala global, sino como una base funcional, automatizada y defendible que
> sabemos cómo evolucionar.

---

# Parte II. Banco avanzado de preguntas y respuestas

## 22. Git y gestión de versiones

### ¿Qué diferencia hay entre Git y GitHub?

> Git es el sistema distribuido de control de versiones. GitHub aloja
> repositorios y agrega colaboración, pull requests, Secrets y Actions.

### ¿Qué es un commit?

> Es una instantánea identificada por un hash, con autor, fecha, mensaje y
> relación con uno o más padres.

### ¿Qué es una rama?

> Es una referencia móvil a una línea de commits. Permite trabajar aislado y
> luego integrar.

### ¿Merge y rebase son iguales?

> No. Merge conserva ambas líneas y crea un commit de integración. Rebase vuelve
> a aplicar commits sobre otra base y reescribe hashes. Usamos merges para
> conservar la historia de integración.

### ¿Por qué GitHub Flow?

> Es simple para entrega continua: ramas cortas, revisión, integración a main y
> despliegue. Git Flow con ramas release y develop sería más pesado para este
> ritmo.

### ¿Cómo evitarían subir secretos?

> .gitignore, revisión de diff, escaneo de secretos y GitHub Secrets. Si un
> secreto se filtra, no basta borrarlo del último commit: se revoca, rota y,
> cuando corresponde, se limpia el historial.

### ¿Qué es un pull request?

> Es una propuesta de integración con revisión, conversación y checks antes del
> merge. Aunque algunos merges del laboratorio se ejecutaron desde terminal, la
> metodología y las ramas conservan el mismo principio de integración
> controlada.

## 23. Maven, Java y Spring

### ¿Por qué Maven multi-módulo?

> Centraliza versiones, permite construir dependencias compartidas en orden y
> ejecuta un reactor completo con un comando.

### ¿Qué hace `mvn clean verify`?

> Clean elimina artefactos previos. Verify ejecuta el ciclo hasta validación:
> compila, prueba, empaqueta y ejecuta verificaciones configuradas.

### ¿Por qué Java 25 LTS?

> Es la versión LTS elegida para el proyecto y la misma se usa localmente, en CI
> y runtime. Lo importante es evitar diferencias entre ambientes.

### ¿Qué aporta Spring Boot?

> Auto-configuración, servidor embebido, starters, Actuator y un modelo
> consistente para APIs y servicios.

### ¿Qué aporta Spring Cloud?

> Gateway, OpenFeign y patrones para comunicación y resiliencia entre servicios.

### ¿Qué es inyección de dependencias?

> El contenedor crea y conecta objetos. Reduce acoplamiento a implementaciones y
> facilita pruebas con dobles.

### ¿Qué significa arquitectura hexagonal?

> El dominio queda al centro. Los puertos expresan lo que necesita o ofrece. Los
> adaptadores conectan HTTP, JPA, RabbitMQ u otros detalles. La dirección de
> dependencia apunta hacia el negocio.

### ¿Dominio y entidad JPA son lo mismo?

> No necesariamente. El dominio modela reglas del negocio; la entidad JPA modela
> persistencia. Un mapper permite que cambios de base no contaminen el modelo de
> negocio.

## 24. Bases de datos

### ¿Por qué PostgreSQL?

> Es relacional, transaccional, maduro y compatible con JPA. Los datos de
> usuarios, amistades, participantes y mensajes requieren integridad.

### ¿Realmente hay una base por servicio?

> En el laboratorio hay un motor PostgreSQL con bases lógicas separadas. Cada
> servicio usa su propia URL y no debería consultar tablas ajenas. En una
> separación más estricta podrían ser instancias o cuentas distintas.

### ¿Por qué no una sola base compartida?

> Una base compartida facilita joins, pero acopla esquemas y despliegues. La
> separación obliga a usar contratos y eventos. Aceptamos el costo de
> consistencia distribuida.

### ¿Qué es una transacción?

> Un conjunto de operaciones que cumple atomicidad, consistencia, aislamiento y
> durabilidad dentro de un límite de datos.

### ¿Cómo mantienen consistencia entre servicios?

> No usamos una transacción distribuida global. Cada servicio confirma su
> transacción local y publica eventos. Para mayor robustez se incorporaría
> Outbox Pattern, idempotencia y reintentos.

### ¿Qué es idempotencia?

> Procesar la misma solicitud o evento varias veces produce el mismo resultado
> observable. Es clave porque mensajes y reintentos pueden duplicarse.

### ¿Por qué H2?

> Permite pruebas y desarrollo rápido sin PostgreSQL externo. No reemplaza todas
> las pruebas reales porque dialecto y comportamiento pueden variar. Para mayor
> fidelidad usaríamos Testcontainers con PostgreSQL.

### ¿Por qué MinIO y no guardar archivos en PostgreSQL?

> El almacenamiento de objetos está optimizado para binarios y acceso por clave.
> PostgreSQL conserva metadatos y relaciones. Esto mejora tamaño de backups y
> permite migrar a S3.

## 25. RabbitMQ, Redis y tiempo real

### ¿Qué es RabbitMQ?

> Es un broker AMQP. Los productores publican mensajes y consumidores los
> procesan de forma desacoplada.

### ¿Qué diferencia hay entre queue y exchange?

> El exchange recibe publicaciones y las enruta según tipo y bindings. La queue
> almacena mensajes hasta que un consumidor los procesa.

### ¿Qué ocurre si un consumidor está caído?

> La cola puede conservar el mensaje hasta que vuelva, dependiendo de
> durabilidad, acknowledgements y TTL. Para producción se configuran DLQ,
> reintentos y monitoreo.

### ¿Por qué no usar solo WebSocket?

> WebSocket conecta servidor y navegador, pero no reemplaza un broker entre
> microservicios. RabbitMQ desacopla productores y consumidores y puede conservar
> eventos.

### ¿Por qué Redis?

> Presencia y sesiones cambian frecuentemente y toleran almacenamiento en
> memoria. Redis entrega baja latencia y estructuras adecuadas.

### ¿Cómo evitan que un mensaje desaparezca en frontend?

> Se diferencia estado optimista de confirmación del servidor. El frontend no
> debe reemplazar toda la vista al recibir realtime; debe reconciliar por ID y
> conservar foco y estado local.

### ¿WebSocket es seguro?

> WSS cifra el transporte. La conexión además debe autenticar usuario y validar
> autorización de eventos; cifrado no reemplaza control de acceso.

## 26. Docker

### ¿Imagen y contenedor son lo mismo?

> La imagen es una plantilla inmutable por capas. El contenedor es una instancia
> ejecutable con una capa de escritura.

### ¿Qué es una capa?

> El resultado de una instrucción del Dockerfile. Capas reutilizables mejoran
> cache y transferencia.

### ¿Por qué multietapa?

> Permite usar herramientas en una etapa y copiar solo el resultado a runtime.
> Reduce tamaño y superficie.

### ¿Por qué `npm ci` y no `npm install`?

> npm ci respeta exactamente package-lock y parte de un árbol limpio, por lo que
> es más reproducible en CI.

### ¿Por qué usuario non-root?

> Limita privilegios si el proceso es comprometido. No elimina todas las
> vulnerabilidades, pero reduce impacto.

### ¿Qué hace .dockerignore?

> Excluye archivos del contexto enviado al daemon. Mejora rendimiento y evita
> copiar node_modules, .git o secretos.

### ¿Qué diferencia hay entre CMD y ENTRYPOINT?

> ENTRYPOINT define el ejecutable principal. CMD aporta comando o argumentos por
> defecto que pueden reemplazarse.

### ¿Qué es un volumen?

> Almacenamiento con ciclo de vida independiente del contenedor. Evita perder
> datos al recrearlo.

### ¿Qué es una red Docker?

> Un espacio virtual donde contenedores se resuelven por nombre y se comunican
> sin publicar todos los puertos al host.

## 27. CI/CD y GitHub Actions

### ¿Qué es un runner?

> La máquina que ejecuta los jobs. GitHub-hosted crea un entorno efímero; un
> self-hosted es administrado por nosotros.

### ¿Job y step?

> Un job corre en un runner y contiene steps secuenciales. Jobs pueden ejecutarse
> en paralelo y depender mediante needs.

### ¿Qué son artefactos?

> Archivos producidos por un workflow y almacenados para descarga, por ejemplo
> reportes. Las imágenes de producción se publican en un registry, no como
> artefactos comunes de Actions.

### ¿Por qué cachear Maven?

> Evita descargar dependencias en cada ejecución. No omite pruebas ni reemplaza
> el artefacto final.

### ¿Por qué construir imágenes secuencialmente?

> Catorce construcciones simultáneas aumentan memoria, CPU y conexiones. La
> secuencia es más lenta, pero estable para el laboratorio.

### ¿Cómo se detiene el pipeline ante error?

> Los comandos retornan código distinto de cero y el step falla. Needs evita
> ejecutar deploy si build-and-publish no termina exitoso.

### ¿Cómo asegurarían supply chain?

> Pin de actions por SHA, SBOM, firma Cosign, escaneo Trivy, dependabot y
> políticas que permitan solo imágenes firmadas.

### ¿Qué es un secret de GitHub?

> Un valor cifrado disponible en el contexto del workflow. Se enmascara en logs,
> pero el job que lo usa debe considerarse privilegiado.

## 28. AWS y redes

### ¿Qué es una VPC?

> Una red virtual aislada en AWS con rango CIDR, subredes, rutas y controles de
> seguridad.

### ¿Qué hace pública a una subred?

> Tener una ruta hacia Internet Gateway y condiciones para que el recurso tenga
> dirección pública. El nombre por sí solo no la hace pública.

### ¿Security Group y NACL?

> Security Group es stateful y se asocia a interfaces. NACL es stateless y se
> aplica a nivel de subred.

### ¿Qué es un Internet Gateway?

> El componente que conecta una VPC con Internet para recursos con rutas y
> direcciones adecuadas.

### ¿Qué es NAT Gateway?

> Permite salida a Internet desde subredes privadas sin aceptar conexiones
> entrantes iniciadas desde Internet. Puede aumentar costo.

### ¿Qué es EC2?

> Una máquina virtual administrada por AWS. Nosotros gestionamos sistema
> operativo, Docker, actualizaciones y capacidad.

### ¿Qué es EBS?

> Almacenamiento de bloques asociado a una zona. Kubernetes lo aprovisiona para
> PVC mediante CSI.

### ¿NLB y ALB?

> NLB opera principalmente en capa 4, alto rendimiento y conexiones TCP. ALB
> opera en capa 7 y puede enrutar por host o path. En nuestra topología Caddy
> realiza el enrutamiento HTTP y NLB expone Gateway.

### ¿Por qué no exponer cada microservicio?

> Aumentaría superficie, certificados, CORS y acoplamiento del cliente. Gateway
> entrega un punto controlado.

### ¿Cómo optimizarían costos?

> S3/CloudFront para frontend, servicios administrados según carga, escalado
> automático, requests ajustados, apagar entornos no usados y evaluar ECS
> Fargate o un monolito modular si EKS no se justifica.

## 29. Kubernetes y EKS

### ¿Kubernetes y EKS son lo mismo?

> Kubernetes es la plataforma de orquestación. EKS es el servicio administrado
> de AWS que opera el plano de control e integra IAM, VPC y balanceadores.

### ¿Qué hace el scheduler?

> Selecciona un nodo adecuado para un pod según recursos, restricciones y
> afinidades.

### ¿Qué hace el controller manager?

> Ejecuta controladores que comparan estado deseado y real y realizan acciones
> de reconciliación.

### ¿Qué es etcd?

> La base clave-valor del plano de control que conserva el estado del cluster.
> En EKS la administra AWS.

### ¿Qué es Kustomize?

> Una herramienta declarativa para componer y modificar YAML sin plantillas
> complejas. Tenemos una base k8s y un overlay de laboratorio.

### ¿Helm frente a Kustomize?

> Helm empaqueta y parametriza mediante charts y templates. Kustomize aplica
> overlays sobre YAML. Para este proyecto Kustomize fue suficiente y más directo.

### ¿Qué pasa si borran un pod?

> Si pertenece a Deployment o StatefulSet, el controlador crea otro para
> recuperar el estado deseado.

### ¿Qué pasa si falla una zona?

> La red cubre dos zonas, pero con una réplica y componentes stateful únicos no
> garantizamos continuidad completa. Se requieren réplicas distribuidas y datos
> altamente disponibles.

### ¿Qué es HPA?

> Horizontal Pod Autoscaler ajusta réplicas según métricas. Requiere métricas y
> aplicaciones stateless o preparadas para escalar.

### ¿Qué es un rolling update?

> Reemplaza progresivamente pods manteniendo disponibilidad según estrategia.
> Algunos Deployments del laboratorio usan Recreate por recursos limitados; para
> producción se preferiría RollingUpdate donde sea seguro.

### ¿Por qué algunos servicios usan Recreate?

> Para controlar consumo en el laboratorio y evitar tener dos JVM simultáneas
> durante actualización. El costo es una ventana potencial de indisponibilidad.

## 30. Seguridad

### ¿Hash y cifrado son iguales?

> No. El hash es unidireccional y se usa para contraseñas con salt y algoritmo
> adaptativo. El cifrado es reversible con una clave.

### ¿Access token y refresh token?

> Access token dura menos y autoriza solicitudes. Refresh token permite obtener
> uno nuevo y debe protegerse, rotarse y revocarse cuando corresponda.

### ¿OAuth2 es autenticación?

> OAuth2 es un framework de autorización. OpenID Connect agrega identidad. En
> lenguaje práctico, Google/GitHub permiten login, pero técnicamente debe
> distinguirse el protocolo utilizado por cada proveedor.

### ¿Base64 protege un Secret de Kubernetes?

> No. Base64 solo codifica. La protección viene de RBAC, cifrado en reposo,
> control de etcd y acceso restringido.

### ¿Qué es mínimo privilegio?

> Dar solo acciones, recursos y tiempo necesarios. Un runner de deploy no debería
> administrar toda la cuenta.

### ¿Por qué HTTPS?

> Protege confidencialidad e integridad en tránsito, autentica el servidor y
> habilita APIs seguras del navegador.

### ¿Qué es CORS?

> Una política del navegador basada en cabeceras. No protege llamadas desde
> servidores o herramientas; la API igual necesita autenticación.

### ¿Cómo responder ante un secreto filtrado?

> Revocar y rotar inmediatamente, revisar logs, limitar impacto, actualizar
> consumidores y limpiar el historial si quedó versionado. No reutilizarlo.

## 31. Observabilidad y operación

### ¿Monitoreo y observabilidad son lo mismo?

> Monitoreo comprueba condiciones conocidas. Observabilidad permite inferir
> estados internos desde señales como métricas, logs y trazas.

### ¿Qué es Prometheus?

> Una base de series temporales que recolecta métricas por pull y permite
> consultas PromQL.

### ¿Qué es Grafana?

> Una herramienta de visualización y dashboards que consulta fuentes como
> Prometheus o CloudWatch.

### ¿Qué es CloudWatch?

> El servicio AWS de métricas, logs, alarmas y eventos. En la entrega se usó para
> verificar EC2.

### ¿Qué es latencia p95?

> El 95% de solicitudes tarda ese valor o menos. Es más informativo que solo el
> promedio para detectar colas lentas.

### ¿Cómo investigarían un 500?

1. Identificar endpoint, hora y correlation ID.
2. Revisar Gateway y servicio responsable.
3. Consultar logs y eventos del pod.
4. Verificar readiness, reinicios y recursos.
5. Revisar base, broker y dependencias.
6. Reproducir con request controlada.
7. Corregir, probar y desplegar por pipeline.

## 32. Frontend y experiencia

### ¿Por qué React y Vite?

> React facilita componentes y estado de interfaz. Vite entrega desarrollo y
> build rápidos con configuración simple.

### ¿Por qué Nginx?

> Sirve archivos estáticos eficientemente, maneja fallback SPA, cache y
> healthcheck.

### ¿Por qué Caddy además de Nginx?

> Nginx sirve el bundle dentro del contenedor. Caddy es el edge público:
> certificados, HTTPS y proxy same-origin hacia frontend o backend.

### ¿Cómo evitaron recargar toda la página con realtime?

> Se actualiza el estado puntual de conversaciones y mensajes. No se vuelve a
> montar el árbol completo ni se reemplaza la sesión, por lo que el input conserva
> foco.

### ¿Cómo controlan sesiones entre pestañas?

> La sesión se sincroniza de forma explícita y los eventos de almacenamiento se
> validan por usuario. Cerrar una cuenta no debe mutar silenciosamente la otra
> vista sin que el estado sea reconciliado.

### ¿Las llamadas están listas para cualquier red?

> WebRTC funciona cuando los peers pueden conectarse. Para robustez global falta
> TURN, monitoreo de calidad, control de salas y pruebas detrás de NAT estrictos.

## 33. Preguntas incómodas y respuestas honestas

### ¿Esto está listo para millones de usuarios?

> No. Está listo como proyecto académico funcional y base escalable. Para
> millones se requieren pruebas de carga, datos administrados y particionados,
> autoscaling, CDN, TURN, observabilidad distribuida y trabajo de seguridad.

### ¿Por qué 14 microservicios para un proyecto pequeño?

> Es más complejo de lo necesario para una primera versión comercial. Se eligió
> para practicar límites de negocio, eventos y EKS según el alcance académico.
> En otro contexto evaluaríamos un monolito modular.

### ¿Por qué los datos están dentro de EKS?

> Por costo y alcance del laboratorio. EBS aporta persistencia, pero operar datos
> stateful en Kubernetes exige más trabajo. Producción usaría servicios
> administrados.

### ¿Por qué la cobertura es 37,11%?

> Porque priorizamos dominio y flujos críticos y aún faltan adaptadores,
> configuración e integración. No usamos el porcentaje como sustituto de calidad.
> Proponemos un incremento progresivo y quality gate.

### ¿Por qué latest si dicen trazabilidad?

> latest facilita la referencia actual, mientras SHA conserva el artefacto
> identificable. Reconocemos que producción debería desplegar tags inmutables o
> digest para que la versión en el Deployment sea inequívoca.

### ¿El Secret de Kubernetes está cifrado?

> El manifiesto no contiene secretos y EBS está cifrado, pero base64 no cifra el
> valor. Una producción debe habilitar cifrado de Secrets con KMS, RBAC estricto
> y Secrets Manager.

### ¿Tienen alta disponibilidad?

> Tienen orquestación, dos zonas de red y recuperación de pods, pero no alta
> disponibilidad completa porque el laboratorio usa una réplica por servicio y
> datos únicos.

### ¿Qué ocurre si AWS Academy se apaga?

> Las credenciales temporales dejan de ser útiles y los recursos pueden quedar
> detenidos. El código, imágenes y manifiestos permanecen. Al reactivar se
> renuevan secretos y se reejecuta el pipeline.

### ¿Cuál fue el mayor problema técnico?

> La integración del ciclo completo. No bastaba que un endpoint funcionara:
> amistad debía crear conversación, mensaje debía persistir y llegar por
> realtime, frontend debía reconciliar estado y el despliegue debía conservar
> datos. Resolver esas fronteras produjo las correcciones más relevantes.

### ¿Qué decisión cambiarían?

> Desplegaríamos desde el inicio imágenes inmutables por SHA, configuraríamos
> OIDC y agregaríamos observabilidad distribuida antes de ampliar tantos
> servicios.

---

# Parte III. Material operativo para la defensa

## 34. Comandos preparados

### Git

```bash
git status --short
git branch --show-current
git log --oneline --decorate -15
```

### Maven y pruebas

```bash
mvn -B clean verify
find . -path '*/target/site/jacoco/index.html'
```

No ejecutar `mvn clean verify` completo durante la exposición salvo que el
docente lo solicite. Mostrar el run exitoso es más rápido.

### Docker

```bash
docker compose -f docker-compose.prod.yml --env-file .env.example config --services
docker image inspect orioneta-auth-service:eft --format '{{.Config.User}}'
```

### Kubernetes

```bash
kubectl -n orioneta get deployments
kubectl -n orioneta get pods
kubectl -n orioneta get services
kubectl -n orioneta get pvc
kubectl -n orioneta describe deployment gateway-service
kubectl -n orioneta logs deploy/gateway-service --tail=50
```

### Salud pública

```bash
curl -I https://orioneta.accesscam.org
curl -s https://orioneta.accesscam.org/actuator/health/readiness
curl -s https://orioneta.accesscam.org/api/auth/oauth2/providers
```

### GitHub Actions

```bash
gh run list -R OrionTheProgrammer/Orioneta-Backend --branch main --limit 3
gh run list -R Panditax727/Orioneta-Frontend --branch main --limit 3
```

## 35. Plan de contingencia

### Si la aplicación no abre

1. Confirmar DNS con `getent hosts orioneta.accesscam.org`.
2. Probar `curl -I`.
3. Mostrar la captura de producción del informe.
4. Mostrar los runs exitosos y el estado Kubernetes guardado.
5. Explicar el flujo sin intentar reparar en vivo durante toda la exposición.

### Si AWS Academy está apagado

> El entorno académico usa una sesión temporal. Mostraremos la evidencia del
> último despliegue exitoso y los manifiestos reproducibles. El sistema se
> recupera encendiendo el laboratorio, renovando los secretos AWS y ejecutando el
> workflow.

### Si falla el login OAuth

1. Usar una cuenta local preparada.
2. Mostrar el endpoint de proveedores.
3. Explicar que OAuth depende además de redirect URIs configuradas en el
   proveedor externo.

### Si realtime no llega

1. Mantener abierta la conversación.
2. Mostrar el WebSocket en DevTools.
3. Probar salud de Gateway y pods.
4. Mostrar evidencia WSS del informe.

### Si el docente pide ver secretos

> Podemos mostrar los nombres de los secrets y dónde se consumen, pero no sus
> valores. Ocultar el valor es parte del control de seguridad.

## 36. Lista de preparación el día anterior

- Completar nombres, sección y docente en informe y presentación.
- Descargar una copia local de ambos repositorios.
- Descargar informe y presentación.
- Verificar que el laboratorio esté encendido.
- Renovar credenciales temporales si corresponde.
- Confirmar los tres AWS Secrets en ambos repositorios.
- Probar URL, login local, amistad, chat, mensaje y archivo.
- Confirmar `kubectl get pods` sin reinicios inesperados.
- Confirmar los últimos workflows en verde.
- Preparar dos cuentas de demostración.
- Cargar un archivo pequeño de prueba.
- Mantener capturas disponibles sin Internet.
- Cerrar pestañas con credenciales o datos personales.
- Desactivar notificaciones de escritorio.

## 37. Lista de revisión cinco minutos antes

- Presentación en modo local, no dependiente de descarga.
- Terminal con tamaño de letra legible.
- Navegador con zoom 100%.
- Aplicación y Actions ya abiertos.
- AWS en la región correcta.
- Ningún archivo .env o llave privada visible.
- Orden de intervención acordado.
- Cronómetro visible solo para el equipo.

## 38. Frases útiles durante preguntas

Cuando conozcan la respuesta:

> La decisión fue X porque resolvía Y. El costo que aceptamos fue Z.

Cuando la implementación es parcial:

> Actualmente implementamos X. Para cubrir completamente ese escenario falta Y,
> y lo integraríamos en Z parte del pipeline o arquitectura.

Cuando no sepan un detalle:

> No quiero inventar el dato exacto. Lo que sí puedo explicar es el principio y
> dónde lo verificaríamos.

Cuando el docente proponga otra tecnología:

> Esa alternativa es válida. La compararíamos por costo, complejidad operativa,
> integración y escala. Para nuestro alcance elegimos X por estas razones.

## 39. Resumen mental de emergencia

Si pierden el hilo, recuerden esta secuencia:

1. Código en ramas y commits.
2. Main activa CI.
3. CI compila y prueba.
4. Docker empaqueta.
5. Docker Hub versiona.
6. CD autentica AWS.
7. EC2 entrega frontend y TLS.
8. EKS orquesta backend.
9. NLB publica Gateway.
10. PostgreSQL, RabbitMQ, Redis y MinIO soportan datos.
11. Actuator, Actions y CloudWatch verifican.
12. HTTPS, Secrets y non-root reducen riesgo.

## 40. Cierre final de treinta segundos

> Orioneta demuestra un recorrido completo desde arquitectura y desarrollo hasta
> operación en nube. No solo construimos servicios: definimos cómo se prueban,
> cómo se empaquetan, cómo se publican, cómo se despliegan y cómo se comprueba su
> estado. Conocemos las ventajas de la solución, sus costos y las mejoras
> necesarias para llevarla a una producción de mayor escala.
