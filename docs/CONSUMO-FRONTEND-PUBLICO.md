# Consumo desde el Frontend Público (Persona 4)

Notas de integración aportadas por el líder del frontend público para alinear este backend con las apps:

- `frontend-estadisticas-mvc` → `http://localhost:5080` (invitado)
- `frontend-publico-mvc` → `http://localhost:5081` (apuestas, registro obligatorio)

## Base URL esperada

```
http://localhost:8080/demo/api/v1
```

## Endpoints que consume el frontend

| Uso | Método y ruta |
|---|---|
| Registro / sesión | `POST /autenticacion/registro`, `POST /autenticacion/sesion` |
| Calendario | `GET /partidos`, `GET /partidos/{id}` |
| Posiciones | `GET /grupos` |
| Estadísticas | `GET /estadisticas/selecciones` |

El frontend usa clientes HTTP con flag `UsarSimulado`; en integración real apunta a esta API.

## Contrato con UTNGolCoin

Tras `registro` y `PUT /partidos/{id}/resultado`, este servicio notifica a UTNGolCoin
(`POST /api/billeteras` y `POST /api/liquidaciones/{id}`). El frontend de apuestas
depende de esa cadena para saldo y liquidación de predicciones.
