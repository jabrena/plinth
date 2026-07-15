# Invalid agent XML examples

These documents are expected to **fail** validation against `agents.xsd`.

Schema under test (prefer module canonical):

```text
plinth-agents-generator/src/main/resources/agents.xsd
```

OpenSpec mirror of the same schema:

```text
documentation/openspec/changes/design-pml-agents-schema/examples/xsd/pml/0.9.0/agents.xsd
```

## Cases

| File | Violation |
|------|-----------|
| `missing-id.xml` | Required attribute `@id` omitted on `<agent>` |
| `disallowed-child.xml` | Unexpected child `<tone>` (not in `agents.xsd` sequence) |
| `malformed-constraints.xml` | `<constraints>` without required `<constraint-list>` |

## Expected `xmllint` failures

Run from the repository root (module schema):

```bash
SCHEMA=plinth-agents-generator/src/main/resources/agents.xsd
DIR=documentation/openspec/changes/design-pml-agents-schema/examples/xml/invalid

xmllint --noout --schema "$SCHEMA" "$DIR/missing-id.xml"
xmllint --noout --schema "$SCHEMA" "$DIR/disallowed-child.xml"
xmllint --noout --schema "$SCHEMA" "$DIR/malformed-constraints.xml"
```

Observed outcomes (libxml2 / xmllint, exit code 3):

| File | Representative error |
|------|----------------------|
| `missing-id.xml` | `Element 'agent': The attribute 'id' is required but missing.` |
| `disallowed-child.xml` | `Element 'tone': This element is not expected. Expected is ( goal ).` |
| `malformed-constraints.xml` | `Element 'constraint': This element is not expected. Expected is one of ( constraints-description, constraint-list ).` |

Or against the OpenSpec mirror:

```bash
SCHEMA=documentation/openspec/changes/design-pml-agents-schema/examples/xsd/pml/0.9.0/agents.xsd
```

Valid documents must not live in this folder; see sibling `../robot-*.xml` and module `plinth-agents-generator/src/main/resources/agents/robot-*.xml`.
