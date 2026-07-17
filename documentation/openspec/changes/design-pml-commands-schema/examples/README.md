# PML Command Schema Examples

Layout for OpenSpec change `design-pml-commands-schema` ([issue #993](https://github.com/jabrena/plinth/issues/993)).

## Directory layout

| Path | Purpose |
|------|---------|
| `xsd/pml/0.9.0/commands.xsd` | OpenSpec mirror of production `commands.xsd` |
| `md/` | Historical Markdown contracts (pre-XML migration reference) |

**Canonical production paths:**

| Artifact | Path |
|----------|------|
| Schema | `plinth-commands-generator/src/main/resources/commands.xsd` |
| Inventory | `plinth-commands-generator/src/main/resources/commands.xml` |
| Command XML | `plinth-commands-generator/src/main/resources/commands/*.xml` |

## Validation

```bash
xmllint --noout --schema plinth-commands-generator/src/main/resources/commands.xsd \
  plinth-commands-generator/src/main/resources/commands/*.xml
```

## Out of scope in these examples

- Separate valid/invalid XML fixture trees (production `commands/*.xml` is authoritative)
- Live `skills/` release promotion
