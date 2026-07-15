## Why

GitHub issue [#1025](https://github.com/jabrena/plinth/issues/1025) requests a dedicated command to automate archiving an OpenSpec change by name. Today, maintainers and contributors must run the OpenSpec CLI manually, which is slower and more error-prone than a single, standardized command in the Plinth command bundle.

## What Changes

- Add a new command `/close-spec` that accepts a required `<change-name>` argument.
- The command runs the equivalent of `openspec archive <change-name>` in the correct project context (typically from `documentation/`).
- Provide clear success output and actionable error output for missing arguments, unknown changes, and missing OpenSpec CLI.
- Synchronize command inventory, installer bundle, and focused tests for the new command contract.

## Capabilities

### New Capabilities

- `close-spec-command`: Defines the `/close-spec` contract, inputs, behaviors, and safeguards.

### Modified Capabilities

None.

## Impact

This change adds a new command to the embedded command bundle. It touches command inventory sources, command installation assets, and tests. It does not require refreshing the public `skills/` release output.

