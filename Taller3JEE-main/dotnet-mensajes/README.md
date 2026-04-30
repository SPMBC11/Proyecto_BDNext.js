# MensajeriaApi (.NET + Entity Framework)

Reemplazo del módulo JEE de envío de correos por una aplicación .NET que:

- Recibe mensajes por API REST.
- Almacena mensajes en base de datos SQLite con Entity Framework Core.
- Consulta mensajes por tipo.
- Calcula estadísticas por tipo de mensaje.
- Visualiza estadísticas en dashboard web.

## Requisitos

- .NET 8 SDK

## Ejecutar

```bash
cd dotnet-mensajes/MensajeriaApi
dotnet restore
dotnet run
```

La aplicación levanta en `http://localhost:5099`.

## Endpoints

- `POST /api/mensajes` - Registrar mensaje.
- `GET /api/mensajes` - Listar todos los mensajes.
- `GET /api/mensajes?tipo=Correo` - Filtrar por tipo.
- `GET /api/estadisticas/tipo` - Estadísticas por tipo (cantidad y porcentaje).
- `GET /api/estadisticas/resumen` - Resumen general.

## Dashboard de estadísticas

Abrir:

- `http://localhost:5099/`

Muestra total de mensajes, enviados hoy y gráfico de distribución por tipo.

## Ejemplo de creación de mensaje

```bash
curl -X POST http://localhost:5099/api/mensajes \
  -H "Content-Type: application/json" \
  -d '{
    "remitente":"sistema@ucol.mx",
    "destinatario":"estudiante@ucol.mx",
    "asunto":"Recordatorio de curso",
    "cuerpo":"Tu siguiente práctica vence mañana.",
    "tipo":"Recordatorio"
  }'
```
