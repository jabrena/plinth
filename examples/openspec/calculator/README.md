# Calculator OpenSpec Example

This standalone OpenSpec project provides a compact fixture for reviewing
goal-to-criteria-to-task alignment with `059-design-atdd`.

The project contains two pending changes:

- `add-calculator` is the aligned positive fixture. Its proposal defines the
  execution goal, its capability specification defines five observable
  acceptance scenarios, and its single task checklist covers implementation
  and verification.
- `add-calculator-with-atdd-gaps` is a structurally valid negative fixture with
  deliberate partial, missing, ambiguous, absent, and divergent alignment. It
  proves that the skill returns `changes-requested` and explains every gap.

## Validate

Run OpenSpec from this directory:

```bash
cd examples/openspec/calculator
openspec validate --all
```
