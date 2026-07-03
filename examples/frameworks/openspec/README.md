# OpenSpec Project: Framework Examples

This OpenSpec project records planned changes for the Java framework examples
under `examples/frameworks`.

## Derivation

- Source direction: REST scenario in `../SPEC.md`, shared contract in
  `../openapi.yaml` -> OpenSpec change.
- Change boundary: one Quarkus REST sum endpoint capability implemented with
  Hexagonal architecture.
- Pending change: `add-framework-example-capabilities`.
- Capability delta: `changes/add-framework-example-capabilities/specs/framework-sum-examples/spec.md`.
- Isolated OpenAPI example: `changes/add-framework-example-capabilities/examples/openapi.yaml`.

The source artifacts remain authoritative for this example. The OpenSpec change
captures the Quarkus REST scenario requirements without rewriting `SPEC.md` or
the shared OpenAPI contract. A copy of the OpenAPI contract is included inside
the change for isolated review.

## Validate

Run OpenSpec from `examples/frameworks`:

```bash
cd examples/frameworks
openspec validate --all
```

## Layout

```text
openspec/
├── config.yaml
├── README.md
└── changes/
    └── add-framework-example-capabilities/
        ├── proposal.md
        ├── design.md
        ├── tasks.md
        ├── examples/
        │   └── openapi.yaml
        └── specs/
            └── framework-sum-examples/
                └── spec.md
```
