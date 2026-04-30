#!/usr/bin/env bash
set -euo pipefail

BASE_URL="http://localhost:8080/taller3jee"
LOG_FILE="taller3jee.log"

require_cmd() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "Error: no se encontro el comando '$1'." >&2
    exit 1
  fi
}

require_java_17() {
  if ! command -v java >/dev/null 2>&1; then
    echo "Error: no se encontro 'java'. Instala JDK 17+ y vuelve a intentar." >&2
    exit 1
  fi

  local version
  version="$(java -version 2>&1 | head -n 1 | sed -E 's/.*\"([0-9]+).*/\1/')"
  if [[ -z "${version}" || ! "${version}" =~ ^[0-9]+$ ]]; then
    echo "Error: no pude detectar la version de Java. Salida de 'java -version':" >&2
    java -version >&2 || true
    exit 1
  fi

  if (( version < 17 )); then
    echo "Error: Java ${version} detectado, pero este proyecto requiere Java 17+." >&2
    echo "Instala un JDK 17+ y vuelve a correr este script." >&2
    exit 1
  fi
}

resolve_maven_cmd() {
  if [[ -x "./mvnw" ]]; then
    echo "./mvnw"
    return 0
  fi
  if command -v mvn >/dev/null 2>&1; then
    echo "mvn"
    return 0
  fi
  echo "Error: no se encontro Maven ('mvn') ni Maven Wrapper ('./mvnw')." >&2
  echo "Instala Maven o agrega mvnw al proyecto y vuelve a intentar." >&2
  exit 1
}

start_app() {
  if curl -fsS "${BASE_URL}/api/v1/cursos/total" >/dev/null 2>&1; then
    echo "La aplicacion ya esta corriendo en ${BASE_URL}."
    return
  fi

  echo "Levantando Taller3JEE con Maven..."
  local mvn_cmd
  mvn_cmd="$(resolve_maven_cmd)"
  "${mvn_cmd}" spring-boot:run >"${LOG_FILE}" 2>&1 &
  APP_PID=$!
  echo "PID: ${APP_PID} (logs en ${LOG_FILE})"

  echo "Esperando a que la API este disponible..."
  for _ in $(seq 1 60); do
    if curl -fsS "${BASE_URL}/api/v1/cursos/total" >/dev/null 2>&1; then
      echo "Aplicacion iniciada correctamente."
      return
    fi
    sleep 2
  done

  echo "Error: la aplicacion no inicio a tiempo. Revisa ${LOG_FILE}." >&2
  exit 1
}

post_json() {
  local url="$1"
  local body="$2"
  curl -sS -X POST "${BASE_URL}${url}" \
    -H "Content-Type: application/json" \
    -d "${body}" >/dev/null
}

seed_data() {
  echo "Insertando estudiantes de prueba..."
  for i in $(seq -w 1 30); do
    post_json "/api/v1/estudiantes" "{\"nombre\":\"Estudiante ${i}\",\"email\":\"estudiante${i}@demo.com\",\"matricula\":\"ED-${i}\"}"
  done

  echo "Generando progreso simulado..."
  post_json "/api/v1/simulacion/ejercicio" '{"estudianteId":1,"temaId":1}'
  post_json "/api/v1/simulacion/ejercicio" '{"estudianteId":1,"temaId":2}'
  post_json "/api/v1/simulacion/ejercicio" '{"estudianteId":2,"temaId":1}'
  post_json "/api/v1/simulacion/ejercicio" '{"estudianteId":3,"temaId":3}'

  echo "Datos cargados. Verifica en:"
  echo "  - ${BASE_URL}/"
  echo "  - ${BASE_URL}/api/v1/estudiantes"
  echo "  - ${BASE_URL}/api/v1/cursos/total"
}

main() {
  require_cmd curl
  require_java_17
  resolve_maven_cmd >/dev/null

  start_app
  seed_data
}

main "$@"
