# Endpoints

Los endpoints quedan definidos como contrato inicial para implementar en iteraciones posteriores.

## Gateway

| Ruta publica | Destino |
| --- | --- |
| `/api/auth/**` | `auth-service` |
| `/api/users/**` | `user-service` |
| `/api/contacts/**` | `contact-service` |
| `/api/conversations/**` | `conversation-service` |
| `/api/messages/**` | `message-service` |
| `/api/notifications/**` | `notification-service` |
| `/api/media/**` | `media-service` |
| `/api/bff/**` | `bff-service` |

## Auth

| Metodo | Ruta | Uso |
| --- | --- | --- |
| `POST` | `/api/auth/register` | Registrar usuario |
| `POST` | `/api/auth/login` | Iniciar sesion |
| `POST` | `/api/auth/refresh` | Renovar token |
| `POST` | `/api/auth/validate` | Validar token |

## Usuarios, conversaciones y mensajes

| Servicio | Ruta base |
| --- | --- |
| user-service | `/api/users` |
| conversation-service | `/api/conversations` |
| message-service | `/api/messages` |
| contact-service | `/api/contacts` |
| notification-service | `/api/notifications` |
| media-service | `/api/media` |
| audit-service | `/api/audit` |
