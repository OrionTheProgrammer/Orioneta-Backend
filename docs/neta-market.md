# Neta Market

Neta Market es la tienda/comunidad interna de templates visuales de Orioneta.

## Servicio Tecnico

```txt
neta-market-service
```

## Tipos De Contenido

```txt
GLOBAL_THEME
CHAT_THEME
GROUP_THEME
BACKGROUND
FONT
ANIMATION_PACK
NOTIFICATION_STYLE
SOUND_PACK
BUBBLE_STYLE
```

## Estados

```txt
DRAFT
PENDING_REVIEW
APPROVED
REJECTED
REMOVED
```

## Flujo

```txt
Usuario sube template
  -> neta-market-service registra metadata
  -> media-service guarda archivos y previews
  -> moderation-service revisa
  -> notification-service avisa resultado
```
