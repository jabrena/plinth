# Required vs optional policy (`agents.xsd`)

This note documents OpenSpec task **1.4** for change `design-pml-agents-schema`.

## Canonical schema

`plinth-agents-generator/src/main/resources/agents.xsd`

OpenSpec mirror (keep identical): `examples/xsd/pml/0.9.0/agents.xsd`  
Legacy include shim: `examples/xsd/pml/0.9.0/agent.xsd` → includes `agents.xsd`

## Required (production)

| Item | Rule |
|------|------|
| `@id` | required |
| `<metadata>` | required |
| `<metadata>/<title>` | required |
| `<metadata>/<description>` | required |
| `<role>` | required |
| `<goal>` | required |

**Decision:** Enforce what all nine shipped agents already provide. Production documents must not ship without identity metadata, a role sentence, and a goal body.

## Optional

| Item | Rule |
|------|------|
| `<authors>`, `<version>`, `<license>` | optional under `<metadata>` |
| `<model>` | optional under `<metadata>`; when present, emitted as YAML `model` |
| `<readonly>` | optional under `<metadata>` (`xs:boolean`); when `true`, emitted as YAML `readonly: true` |
| `<constraints>` | optional, 0..* |
| `<steps>` | optional |
| `<output-format>` | optional |
| `<safeguards>` | optional |

Behavioral/kind-specific contracts remain in `AgentIndexesTest` and Gherkin — not Schematron.

## Inventory scope

`plinth-agents-generator/src/main/resources/agents.xml` (`<agent-inventory>`) is **outside** this XSD. The inventory is for installer discovery. `agents.xsd` validates **individual** agent documents (`robot-*.xml`) only.
