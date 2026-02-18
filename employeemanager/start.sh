#!/bin/bash
set -euo pipefail

: "${MYSQL_ROOT_PASSWORD:?MYSQL_ROOT_PASSWORD must be set (use docker-compose.yml)}"

echo "[api] Starting MySQL (background) using the official MySQL entrypoint..."
docker-entrypoint.sh mysqld &
MYSQL_PID=$!

echo "[api] Waiting for MySQL to be ready..."
for i in {1..60}; do
  if mysqladmin ping -h 127.0.0.1 -uroot -p"${MYSQL_ROOT_PASSWORD}" --silent; then
    echo "[api] MySQL is up."
    break
  fi
  sleep 2
done

echo "[api] Starting Spring Boot..."
java -jar /app/app.jar &
APP_PID=$!

term_handler() {
  echo "[api] Shutting down..."
  kill -TERM "${APP_PID}" 2>/dev/null || true
  mysqladmin shutdown -h 127.0.0.1 -uroot -p"${MYSQL_ROOT_PASSWORD}" 2>/dev/null || true
  wait "${APP_PID}" 2>/dev/null || true
  wait "${MYSQL_PID}" 2>/dev/null || true
  exit 0
}

trap term_handler SIGTERM SIGINT

wait -n "${APP_PID}" "${MYSQL_PID}" || true
echo "[api] A process exited. Cleaning up..."
term_handler
