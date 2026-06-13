# Kafka Sakila Film Stream

This example starts local Kafka with Confluent Schema Registry, creates the `sakila.film` topic, registers the `Film` Avro schema, and loads focused Sakila film messages.

## How to run in local?

```bash
docker compose up
docker compose up -d
docker compose ps
docker compose logs -f sakila-loader
docker compose exec schema-registry kafka-avro-console-consumer --bootstrap-server kafka:29092 --property schema.registry.url=http://schema-registry:8081 --topic sakila.film --from-beginning --max-messages 5
curl --silent http://localhost:8081/subjects
docker compose down
docker compose down -v
```

## How to test with Confluent MCP?

Use `mcp-confluent-local.yaml` with the Kafka MCP server in `.cursor/mcp.json`:

```json
{
  "kafka": {
    "command": "npx",
    "args": [
      "-y",
      "@confluentinc/mcp-confluent",
      "--config",
      "./examples/infrastructure/kafka/mcp-confluent-local.yaml"
    ]
  }
}
```

The local deployment enables these MCP tool groups:

- `kafka`: `list-topics`, `create-topics`, `delete-topics`, `produce-message`, `consume-messages`, `list-consumer-groups`, `describe-consumer-group`, `get-consumer-group-lag`
- `schema-registry`: `list-schemas`, `delete-schema`

Smoke-test the MCP server configuration:

```bash
npx -y @confluentinc/mcp-confluent --config ./mcp-confluent-local.yaml --list-tools
```
