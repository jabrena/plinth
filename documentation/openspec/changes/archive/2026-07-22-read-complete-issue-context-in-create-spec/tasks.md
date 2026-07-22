## 1. Complete issue context for create-spec

- [x] 1.1 Define OpenSpec requirements and design decisions for sanitized complete issue context with fail-closed coverage validation.
- [x] 1.2 Update the canonical `/create-spec` command XML with the external-sanitization boundary, complete-context confirmation, and failure behavior.
- [x] 1.3 Update the `042-planning-openspec` index and reference XML to require sanitized outsider-authored sources without raw third-party ingestion.
- [x] 1.4 Extend command and skill Gherkin coverage plus generator assertions for the complete-context contract.
- [x] 1.5 Validate the OpenSpec change and edited XML sources.
- [x] 1.6 Regenerate local command and skill output and run focused Maven verification.
- [x] 1.7 Execute the listed `/create-spec` and `042-planning-openspec` acceptance prompts before promotion, or record why they were skipped.

Acceptance prompt execution was skipped in this implementation session because no interactive acceptance-prompt runner is available in the shell environment. The command and skill Gherkin sources were updated and remain the required prompts to execute before promotion.
