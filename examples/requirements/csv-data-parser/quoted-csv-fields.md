# Quoted CSV Fields Requirement

## Raw Requirement

`CsvDataParser` in `examples/maven/maven-demo` must support CSV fields wrapped in double quotes.

Commas inside quoted values must be treated as part of the field value, not as field separators. For example, this CSV line:

```text
1,"Doe, Jane",jane@example.com,1990-05-15,75000.50
```

must parse as one person with:

- `id`: `1`
- `name`: `Doe, Jane`
- `email`: `jane@example.com`
- `birthDate`: `1990-05-15`
- `salary`: `75000.50`

Existing unquoted CSV lines must continue to parse as they do today.

Existing validation behavior must remain unchanged for:

- invalid field counts
- empty required fields
- invalid field formats
- field constraint violations
- batch parsing with `skipErrors`
- parse error reporting

Use the two-step change method because delimiter handling is risky: first prepare the design without changing behavior, then make the quoted-field behavior change after the preparation is verified.
