# UTN GolMundial 2026 — backend nativo y frontends públicos
# Uso: make help

ROOT := $(abspath $(dir $(lastword $(MAKEFILE_LIST))))
ESTADISTICAS := $(ROOT)/frontend-estadisticas-mvc
APUESTAS := $(ROOT)/frontend-publico-mvc
BACKEND := $(ROOT)/demo

GUACALES_DB_HOST ?= localhost
GUACALES_DB_PORT ?= 5432
GUACALES_DB_NAME ?= guacales_db
GUACALES_DB_USER ?= postgres
GUACALES_DB_DRIVER ?= postgresql
GUACALES_DB_URL := jdbc:postgresql://$(GUACALES_DB_HOST):$(GUACALES_DB_PORT)/$(GUACALES_DB_NAME)
HOST ?= 0.0.0.0
HTTP_PORT ?= 18080
MANAGEMENT_PORT ?= 9990
UTNGOLCOIN_BASE_URL ?= http://localhost:5001/api/

.PHONY: help run estadisticas apuestas stop build clean status backend-build backend-datasource backend-deploy backend-run

help: ## Muestra los targets disponibles
	@echo "UTN GolMundial 2026"
	@echo ""
	@echo "  make run            Levanta estadísticas (:5080) y apuestas (:5081) en paralelo"
	@echo "  make estadisticas   Solo portal de estadísticas (invitado) → http://localhost:5080"
	@echo "  make apuestas       Solo portal de apuestas → http://localhost:5081"
	@echo "  make stop           Detiene lo que escuche en 5080 y 5081"
	@echo "  make build          Compila ambos proyectos"
	@echo "  make status         Muestra qué puertos están ocupados"
	@echo "  make clean          Limpia bin/obj de ambos frontends"
	@echo "  make backend-build  Compila y prueba el WAR con Maven"
	@echo "  make backend-datasource  Crea GuacalesDS en WildFly (una sola vez)"
	@echo "  make backend-deploy Compila y copia demo.war a WILDFLY_HOME"
	@echo "  make backend-run    Inicia WildFly en 0.0.0.0:18080 (configurable)"
	@echo "  make help           Esta ayuda"
	@echo ""
	@echo "Nota: no uses 'cd A && dotnet run' seguido de 'cd B && ...' en la misma terminal:"
	@echo "      el primero bloquea. Usa 'make run' o dos terminales / 'make -j2'."

run: ## Ambos frontends a la vez (Ctrl+C detiene los dos si Make los gestiona)
	@echo "→ Estadísticas :5080  |  Apuestas :5081"
	@echo "  (Ctrl+C para detener ambos)"
	@$(MAKE) -j2 estadisticas apuestas

estadisticas: ## Portal invitado (solo consulta)
	@echo "→ http://localhost:5080"
	cd "$(ESTADISTICAS)" && dotnet run --urls http://localhost:5080

apuestas: ## Portal de apuestas (registro para predecir)
	@echo "→ http://localhost:5081"
	cd "$(APUESTAS)" && dotnet run --urls http://localhost:5081

stop: ## Libera los puertos 5080 y 5081
	@-lsof -tiTCP:5080 -sTCP:LISTEN 2>/dev/null | xargs kill 2>/dev/null || true
	@-lsof -tiTCP:5081 -sTCP:LISTEN 2>/dev/null | xargs kill 2>/dev/null || true
	@echo "Puertos 5080 y 5081 liberados (si había procesos)."

status: ## Quién escucha en 5080/5081
	@echo "=== :5080 (estadísticas) ==="
	@-lsof -nP -iTCP:5080 -sTCP:LISTEN 2>/dev/null || echo "(libre)"
	@echo "=== :5081 (apuestas) ==="
	@-lsof -nP -iTCP:5081 -sTCP:LISTEN 2>/dev/null || echo "(libre)"

build: ## Compilar ambos
	cd "$(ESTADISTICAS)" && dotnet build
	cd "$(APUESTAS)" && dotnet build

backend-build: ## Compilar y ejecutar pruebas del backend con Java 21/Maven
	@java -version
	cd "$(BACKEND)" && mvn clean package

backend-datasource: ## Crear GuacalesDS en un WildFly ya preparado y en ejecución
	@test -n "$(WILDFLY_HOME)" || (echo "Falta WILDFLY_HOME"; exit 1)
	@test -n "$(GUACALES_DB_PASSWORD)" || (echo "Falta GUACALES_DB_PASSWORD"; exit 1)
	@test -x "$(WILDFLY_HOME)/bin/jboss-cli.sh" || (echo "WILDFLY_HOME no contiene bin/jboss-cli.sh"; exit 1)
	"$(WILDFLY_HOME)/bin/jboss-cli.sh" --connect --command='data-source add --name=GuacalesDS --jndi-name=java:/GuacalesDS --driver-name="$(GUACALES_DB_DRIVER)" --connection-url="$(GUACALES_DB_URL)" --user-name="$(GUACALES_DB_USER)" --password="$(GUACALES_DB_PASSWORD)" --enabled=true'
	"$(WILDFLY_HOME)/bin/jboss-cli.sh" --connect --command='/subsystem=datasources/data-source=GuacalesDS:test-connection-in-pool'

backend-deploy: backend-build ## Desplegar el WAR en un WILDFLY_HOME ya preparado
	@test -n "$(WILDFLY_HOME)" || (echo "Falta WILDFLY_HOME"; exit 1)
	@test -d "$(WILDFLY_HOME)/standalone/deployments" || (echo "WILDFLY_HOME no tiene standalone/deployments"; exit 1)
	cp "$(BACKEND)/target/demo.war" "$(WILDFLY_HOME)/standalone/deployments/demo.war"
	touch "$(WILDFLY_HOME)/standalone/deployments/demo.war.dodeploy"

backend-run: ## Iniciar WildFly de forma nativa
	@test -n "$(WILDFLY_HOME)" || (echo "Falta WILDFLY_HOME"; exit 1)
	UTNGOLCOIN_BASE_URL="$(UTNGOLCOIN_BASE_URL)" \
	"$(WILDFLY_HOME)/bin/standalone.sh" \
		-b "$(HOST)" -bmanagement 127.0.0.1 \
		-Djboss.http.port="$(HTTP_PORT)" \
		-Djboss.management.http.port="$(MANAGEMENT_PORT)"

clean: ## Limpiar artefactos de build
	cd "$(ESTADISTICAS)" && dotnet clean
	cd "$(APUESTAS)" && dotnet clean
	@rm -rf "$(ESTADISTICAS)/bin" "$(ESTADISTICAS)/obj" "$(APUESTAS)/bin" "$(APUESTAS)/obj"
	@echo "Limpieza lista."
