# OpenSpec Project: Calculator

This project is the repository-owned aligned fixture for the
`059-design-atdd` acceptance test.

## Authority and Derivation

- The aligned change consolidates the former standalone calculator functional
  requirement, Gherkin scenarios, and task checklist.
- Derivation was one-way: the standalone fixture was migrated into this
  OpenSpec change and then removed.
- The proposal, capability specification, and `tasks.md` in `add-calculator`
  are now authoritative for this example.
- `add-calculator-with-atdd-gaps` is derived one-way from the aligned behavior
  and is authoritative only as a negative ATDD acceptance fixture.
- Each change has one atomic acceptance-test outcome.

## Layout

```text
openspec/
├── config.yaml
├── README.md
└── changes/
    ├── add-calculator/
        ├── proposal.md
        ├── design.md
        ├── tasks.md
        └── specs/
            └── calculator/
                └── spec.md
    └── add-calculator-with-atdd-gaps/
        ├── proposal.md
        ├── design.md
        ├── tasks.md
        └── specs/
            └── calculator-with-atdd-gaps/
                └── spec.md
```
