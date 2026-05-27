# Arquitectura General

Orioneta queda definida como una plataforma de mensajeria privada, grupos personalizables y experiencias visuales configurables. No incluye servidores, comunidades ni espacios publicos tipo Discord.

## Capacidades Principales

- Chats privados entre usuarios.
- Grupos personalizables con roles simples.
- Sistema de amigos mediante correo o friend code hexadecimal.
- Mensajeria y notificaciones en tiempo real.
- Personalizacion global, por conversacion y por grupo.
- Neta Market para templates visuales creados por la comunidad.
- Moderacion para contenido subido por usuarios.

## Servicios

| Dominio | Servicio |
| --- | --- |
| Entrada API | `gateway-service` |
| Adaptacion frontend | `bff-service` |
| Autenticacion | `auth-service` |
| Identidad publica | `user-service` |
| Amistades | `friendship-service` |
| Chats y grupos | `conversation-service` |
| Mensajes | `message-service` |
| Tiempo real | `realtime-service` |
| Notificaciones | `notification-service` |
| Personalizacion | `customization-service` |
| Templates | `neta-market-service` |
| Archivos | `media-service` |
| Moderacion | `moderation-service` |
| Auditoria | `audit-service` |

## Principios

- Cada servicio tiene una responsabilidad clara.
- Las integraciones asincronas usan RabbitMQ.
- El tiempo real usa WebSocket y Redis para presencia.
- Los servicios de negocio siguen arquitectura hexagonal.
- La observabilidad se apoya en Actuator, Prometheus, logs y SonarQube.
