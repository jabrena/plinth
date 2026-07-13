## 1. Command Contract

- [ ] 1.1 Add the canonical command asset for `/close-spec` with required argument `<change-name>` and expected outputs.
- [ ] 1.2 Ensure the command executes `openspec archive <change-name>` in the `documentation/` working directory.
- [ ] 1.3 Provide explicit error handling for: missing argument, unknown change, and missing OpenSpec CLI.
- [ ] 1.4 Add pre-flight checks: OpenSpec CLI availability and change existence (before archiving).

## 2. Bundle, Inventory, and Tests

- [ ] 2.1 Update the embedded command installer/bundle so `/close-spec` is installed.
- [ ] 2.2 Update the command inventory so `/close-spec` is discoverable.
- [ ] 2.3 Add or update focused generator tests to assert the command contract and installation.

## 3. Validation

- [ ] 3.1 Validate OpenSpec artifacts and ensure this change passes `openspec validate --all`.
- [ ] 3.2 Run the relevant generator module verification for command changes.

