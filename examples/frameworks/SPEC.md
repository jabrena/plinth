# Spec

```bash
Create a basic endpoint POST /api/v1/sum which require an object which has 2 properties param1 as integer and param2 as integer add the controller in controller package
```

## OpenAPI contract

Shared contract: [`openapi.yaml`](openapi.yaml)

## CATS fuzz testing

Contract-driven negative testing with [CATS](https://endava.github.io/cats/) runs in Docker against a running example on port `8080`.

```bash
# From examples/frameworks/ — start an example yourself, then:
./run-cats-fuzz.sh --openapi openapi.yaml

# Or build, start, fuzz, and stop in one step:
./run-cats-fuzz-example.sh spring-boot
./run-cats-fuzz-example.sh quarkus
./run-cats-fuzz-example.sh micronaut
```

Reports are written to `cats/cats-report/<framework>/` (HTML by default). CI runs the same checks via `.github/workflows/examples-cats-fuzz.yaml`.

By default, `run-cats-fuzz-example.sh` enables CATS blackbox mode (`CATS_BLACKBOX=1`) so the quality gate fails only on `5xx` responses. Set `CATS_BLACKBOX=0` for full contract-negative testing.

