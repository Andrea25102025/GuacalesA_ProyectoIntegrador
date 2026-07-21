# Checklist de humo (Persona 4)

Verificación mínima antes de una demo conjunta Estadísticas + frontends:

1. WildFly con `demo.war` desplegado y `GuacalesDS` OK.
2. `curl http://localhost:8080/demo/api/v1/grupos` responde 200.
3. `curl http://localhost:8080/demo/api/v1/partidos` responde 200.
4. Swagger: `http://localhost:8080/demo/swagger-ui.html`.
5. Frontends: desde el monorepo de Persona 4, `make run` (:5080 y :5081).
6. Con `UsarSimulado=false`, el portal de estadísticas carga partidos reales.

Si falla CORS o el contexto `/demo`, revisar `README-SETUP.md`.
