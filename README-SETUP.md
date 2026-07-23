# Servicio de Estadísticas - UTN GolMundial 2026

Guía para compilar y desplegar el backend de forma nativa, sin Docker, y probar la integración con UTNGolCoin.

## Requisitos previos

- Java 21 (JDK)
- Maven
- WildFly 39 preparado con el driver PostgreSQL
- PostgreSQL instalado y corriendo
- Git

## 1. Clonar el repositorio

```bash
git clone https://github.com/Andrea25102025/GuacalesA_ProyectoIntegrador.git
cd GuacalesA_ProyectoIntegrador
```

## 2. Crear la base de datos

```bash
sudo -i -u postgres psql
```

Dentro de la consola:
```sql
CREATE DATABASE guacales_db;
ALTER USER postgres PASSWORD 'TU_PASSWORD_AQUI';
\q
```

Si tu PostgreSQL usa autenticación `ident` en vez de `md5`/`scram-sha-256`, edita tu `pg_hba.conf` (ruta la da `SHOW hba_file;` dentro de psql) y cambia esas líneas a `md5`, luego:
```bash
sudo systemctl restart postgresql
```

## 3. Preparar WildFly

Esta guía parte de un `WILDFLY_HOME` ya preparado con el driver PostgreSQL
registrado con el nombre `postgresql`. El proyecto usa el datasource JNDI
`java:/GuacalesDS`; el `Makefile` lo crea a partir de las variables del paso
siguiente.

## 4. Configurar variables

El `Makefile` usa estas variables para configurar `GuacalesDS` y desplegar:

```bash
export JAVA_HOME=/ruta/al/jdk-21
export WILDFLY_HOME=/ruta/a/wildfly
export GUACALES_DB_HOST=localhost
export GUACALES_DB_PORT=5432
export GUACALES_DB_NAME=guacales_db
export GUACALES_DB_USER=postgres
export GUACALES_DB_PASSWORD=TU_PASSWORD_AQUI
export GUACALES_DB_DRIVER=postgresql
export UTNGOLCOIN_BASE_URL=http://localhost:5001/api/
```

`GUACALES_DB_HOST`, `GUACALES_DB_PORT`, `GUACALES_DB_NAME`, `GUACALES_DB_USER` y
`GUACALES_DB_DRIVER` tienen los valores anteriores por defecto. La contraseña no
tiene valor por defecto.

En la red del equipo, Andrea solo cambia la IP de Mayra:

```bash
export UTNGOLCOIN_BASE_URL=http://IP_DE_MAYRA:5001/api/
```

`make backend-run` escucha en `0.0.0.0:18080`, por lo que los demás consumen
Guacales con `http://IP_DE_ANDREA:18080/demo/api/v1/`.

## 5. Compilar y desplegar

En una terminal, inicia WildFly:

```bash
make backend-run
```

En otra terminal, crea el datasource una sola vez:

```bash
make backend-datasource
```

Compila con Maven (incluye pruebas) y despliega el WAR:

```bash
make backend-deploy
```

También se puede compilar directamente:

```bash
cd demo
mvn clean package
```

Verifica que WildFly cree `demo.war.deployed` y no `demo.war.failed`.

### Windows

Visual Studio no administra proyectos Jakarta EE/WildFly. En Windows usa
PowerShell con JDK 21, Maven, PostgreSQL y WildFly (o abre el código en
IntelliJ/Eclipse):

```powershell
git switch master
git pull
$env:JAVA_HOME = 'C:\ruta\jdk-21'
$env:WILDFLY_HOME = 'C:\ruta\wildfly-39'
$env:UTNGOLCOIN_BASE_URL = 'http://IP_DE_MAYRA:5001/api/'

cd demo
mvn clean package
Copy-Item target\demo.war "$env:WILDFLY_HOME\standalone\deployments\demo.war" -Force
& "$env:WILDFLY_HOME\bin\standalone.bat" -b 0.0.0.0 -bmanagement 127.0.0.1 -Djboss.http.port=18080
```

El datasource `GuacalesDS` y el driver PostgreSQL se configuran una sola vez
como se indica arriba. Permite el puerto `18080` en redes privadas.

## 6. Base vacía y carga manual

En el primer despliegue Hibernate crea las tablas y la aplicación inserta
únicamente los roles técnicos `USUARIO` y `ADMINISTRADOR`. No se cargan
selecciones, grupos, sedes ni partidos automáticamente.

Para cargar octavos manualmente:

1. Crea las sedes con `POST /api/v1/sedes`.
2. Crea las selecciones con `POST /api/v1/selecciones`; `grupoId` es opcional.
3. Crea los partidos con `POST /api/v1/partidos`, usa `fase: "OCTAVOS"` y omite
   `grupoId`.

## 7. Probar que todo responde

Con WildFly corriendo:

```bash
curl http://localhost:18080/demo/api/v1/selecciones
curl http://localhost:18080/demo/api/v1/grupos
curl http://localhost:18080/demo/api/v1/sedes
curl http://localhost:18080/demo/api/v1/partidos
```

Documentación interactiva (Swagger):
```
http://localhost:18080/demo/swagger-ui.html
```

## Endpoints disponibles

### Autenticación
- `POST /api/v1/autenticacion/registro` → `{ nombre, correo, contrasena }` → `{ token, usuario }`
- `POST /api/v1/autenticacion/sesion` → `{ correo, contrasena }` → `{ token, usuario }`
- Token JWT (HS256), válido 24h.

### Datos del torneo
- `GET /api/v1/selecciones` — selecciones registradas
- `POST /api/v1/selecciones` — crea una selección; `grupoId` es opcional
- `GET /api/v1/grupos` — grupos con tabla de posiciones
- `GET /api/v1/sedes` — sedes registradas
- `POST /api/v1/sedes` → `{ nombre, ciudad, pais, capacidad }` — crea una sede
- `GET /api/v1/partidos` — calendario (filtros: `?estado=`, `?grupo=`, `?fase=`, `?fecha=`
- `GET /api/v1/partidos/{id}` — detalle de partido, incluye cuotas
- `POST /api/v1/partidos` → `{ seleccionLocalId, seleccionVisitanteId, sedeId, grupoId, fechaHora, fase, ... }` — crea un partido nuevo
- `PUT /api/v1/partidos/{id}` → actualización parcial de fecha, sede, selecciones, grupo, fase, estado o cuotas
- `PUT /api/v1/partidos/{id}/resultado` → `{ golesLocal, golesVisitante }` — registra resultado y recalcula posiciones
- `GET /api/v1/estadisticas/selecciones` — estadísticas acumuladas por selección

## Notas

- CORS está habilitado (`Access-Control-Allow-Origin: *`), así que tu app puede llamar directo a estos endpoints sin bloqueos del navegador.
- Cuando levantes el proyecto en tu propia máquina, `localhost:18080` te sirve directo — no necesitas la IP de red de nadie más.
- Al **registrar un usuario**, este servicio notifica a UTNGolCoin (`POST http://localhost:5001/api/billeteras` con `{ "usuarioId": N }`) para crear la billetera y el bono de 10 UGC. Si UTNGolCoin no está arriba, el registro en Estadísticas igual se completa (solo se registra un warning en el log).
- Al **registrar un resultado**, notifica `POST /api/liquidaciones/{partidoId}` para liquidar predicciones.
- URL de UTNGolCoin configurable con variable de entorno `UTNGOLCOIN_BASE_URL` o propiedad JVM `-Dutngolcoin.baseUrl=...` (default `http://localhost:5001/api/`).

## Frontends públicos (espejo Persona 4)

Este repositorio incluye una copia de los dos frontends ASP.NET del equipo:

| App | Puerto | Rol |
|---|---|---|
| `frontend-estadisticas-mvc` | 5080 | Solo estadísticas, acceso invitado |
| `frontend-publico-mvc` | 5081 | Estadísticas + apuestas (registro obligatorio) |

```bash
cd frontend-estadisticas-mvc && dotnet run   # http://localhost:5080
cd frontend-publico-mvc && dotnet run       # http://localhost:5081
```

Ambos apuntan por defecto a esta API en `http://localhost:18080/demo/api/v1/`.
La integración del equipo usa `Servicios:Estadisticas:UsarSimulado=false` y datos persistentes.
Para desarrollo integrado: `make run` desde la raíz (si hay `Makefile`) o dos terminales.

## Aportes del frontend público

- [Consumo de la API](docs/CONSUMO-FRONTEND-PUBLICO.md)
- [Checklist de humo](docs/CHECKLIST-HUMO-FRONTEND.md)

- [Consumo frontend administrativo](docs/CONSUMO-FRONTEND-ADMIN.md)
