# Consumo desde el Frontend Administrativo (Persona 3)

El panel admin (ASP.NET MVC) usa esta API para:

- Listar / filtrar partidos y registrar resultados (`PUT /partidos/{id}/resultado`)
- Consultar selecciones y grupos
- Autenticación de rol administrador

Base local recomendada:

```
http://localhost:8080/demo/api/v1
```

Health: `GET /salud`. Tras un resultado oficial, Guacales notifica a UTNGolCoin para liquidar predicciones.
