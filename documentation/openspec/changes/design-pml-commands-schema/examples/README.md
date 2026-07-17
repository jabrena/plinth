# PML Command Schema Examples

Layout for OpenSpec change `design-pml-commands-schema` ([issue #993](https://github.com/jabrena/plinth/issues/993)).

## Directory layout

| Path | Purpose |
|------|---------|
| `xml/valid/` | Documents that MUST pass `commands.xsd` (bodies) or illustrate inventory shape |
| `xml/invalid/` | Documents that MUST fail as documented below |
| `xsd/pml/0.9.0/commands.xsd` | OpenSpec mirror of `plinth-commands-generator/src/main/resources/commands.xsd` |
| `md/` | Historical Markdown contracts (migration reference) |

**Canonical schema path:**
`plinth-commands-generator/src/main/resources/commands.xsd`

**Style:** no target namespace; `xsi:noNamespaceSchemaLocation` (agents parity).

## Schema location convention

```xml
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:noNamespaceSchemaLocation="../../xsd/pml/0.9.0/commands.xsd"
```

Inventory samples are **not** validated by `commands.xsd`.

## Valid examples

| File | Kind | Validates against |
|------|------|-------------------|
| `xml/valid/update-issue.xml` | `standard` | `commands.xsd` |
| `xml/valid/close-spec.xml` | `cli` | `commands.xsd` |
| `xml/valid/command-inventory-sample.xml` | — | Inventory shape only (Java / docs) |

## Invalid examples (expected failures)

| File | Expected failure | Layer |
|------|------------------|-------|
| `xml/invalid/update-issue-missing-workflow.xml` | Missing required `<workflow>` | XSD (`commands.xsd`) |
| `xml/invalid/command-inventory-duplicate-id.xml` | Duplicate inventory `@file` | Java / tests (`CommandIndexes`), not XSD |

Kind-profile constraints remain in `analysis-design-commands`, `CommandIndexesTest`, and Gherkin.

## Validation commands

```bash
SCHEMA=examples/xsd/pml/0.9.0/commands.xsd
EX=examples/xml

xmllint --noout --schema "$SCHEMA" "$EX/valid/update-issue.xml"
xmllint --noout --schema "$SCHEMA" "$EX/valid/close-spec.xml"

# Expect failure:
xmllint --noout --schema "$SCHEMA" "$EX/invalid/update-issue-missing-workflow.xml"
```

## Out of scope in these examples

- Namespaced `pml/schemas/command/1.0.0/` artifacts (removed)
- Public `skills/` release promotion
