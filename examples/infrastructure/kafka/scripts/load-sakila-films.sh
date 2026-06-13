#!/usr/bin/env bash
set -euo pipefail

topic="${SAKILA_TOPIC:-sakila.film}"
bootstrap_servers="${KAFKA_BOOTSTRAP_SERVERS:-kafka:29092}"
schema_registry_url="${SCHEMA_REGISTRY_URL:-http://schema-registry:8081}"
schema_file="/schemas/film.avsc"
data_file="/data/sakila-films.avro.json"

echo "Producing Sakila film messages with Avro schema ${schema_file}"
kafka-avro-console-producer \
  --bootstrap-server "${bootstrap_servers}" \
  --topic "${topic}" \
  --property "schema.registry.url=${schema_registry_url}" \
  --property "value.schema=$(tr -d '\n' < "${schema_file}")" \
  < "${data_file}"

echo "Loaded $(wc -l < "${data_file}") Sakila film messages into ${topic}"
