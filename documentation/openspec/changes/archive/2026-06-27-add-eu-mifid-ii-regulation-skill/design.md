## Context

Issue #876 defines three scoped EU regulation skills: `810` MiFID II, `811` MAR, and `812` Product Liability Directive. This change creates only the MiFID II skill so implementation, review, rollback, and validation remain independently bounded.

The existing EU regulation skills establish the expected shape: PML XML source, bundled official-source summary, Java-focused engineering examples, questionnaire or report assets where formal review is expected, Gherkin acceptance scenarios, generated local skill inspection, and explicit "not legal advice" constraints.

## Decisions

### Skill Identifier

Use `810-regulations-eu-mifid-ii` for MiFID II guidance.

This follows issue #876 and keeps the new EU regulation skills contiguous after the existing `801`-`809` catalog.

### Skill Shape

The skill uses the established XML source pattern:

- `plinth-skills-generator/src/main/resources/skill-indexes/810-skill.xml` defines metadata, goal, scope, constraints, triggers, and workflow.
- `plinth-skills-generator/src/main/resources/skill-references/810-regulations-eu-mifid-ii-chapters-summary.xml` summarizes MiFID II structure and official-source anchors.
- `plinth-skills-generator/src/main/resources/skill-references/810-regulations-eu-mifid-ii-engineering-examples.xml` provides Java-focused examples and output guidance.
- `plinth-skills-generator/src/main/resources/skill-references/assets/questions/810-mifid-ii-engineering-review-questionnaire.md` captures structured review questions.
- `plinth-skills-generator/src/main/resources/skill-references/assets/reports/810-mifid-ii-engineering-review-report-template.md` captures review findings, evidence, owner handoffs, and potential non-compliance signals.
- `plinth-skills-generator/src/main/resources/skills.xml` registers the skill id, references, and assets.

### MiFID II Scope

The skill focuses on Java systems that support investment services, algorithmic trading, order handling, trading controls, client classification, suitability/appropriateness workflows, investment advice, portfolio management, record keeping, governance evidence, auditability, and AI-assisted investment decision support.

The skill must not decide whether an organization is an investment firm, whether a service is regulated investment activity, whether a workflow satisfies suitability or best-execution obligations, or whether a MiFID II interpretation is legally sufficient. Those decisions require legal, compliance, risk, product, operations, and accountable business-owner review.

### Relationship to Other Regulation Skills

- Use `802-regulations-dora` when the primary concern is ICT operational resilience, third-party ICT risk, resilience testing, or financial-entity incident handling.
- Use `803-regulations-gdpr` when the primary concern is personal-data processing.
- Use `811-regulations-eu-market-abuse-regulation` when the primary concern is insider dealing, market manipulation, suspicious order/transaction monitoring, disclosure, or market-surveillance evidence.
- Use `801-regulations-eu-ai-act` when the primary concern is AI-system classification, high-risk AI domains, AI transparency, or agent governance.
- Use multiple regulation skills together when a Java financial system crosses trading, AI, privacy, operational resilience, market-surveillance, or product boundaries.

## Two-Step Delivery Plan

1. Make the change easy: add the OpenSpec artifacts, XML source files, report/questionnaire assets, Gherkin feature, and acceptance prompt entry without touching generated release output.
2. Make the behavior change: run local generation and verification so `.agents/skills/810-regulations-eu-mifid-ii` is emitted and validated.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl plinth-skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `.agents/skills/810-regulations-eu-mifid-ii/SKILL.md`.
- Inspect generated local chapters summary, engineering examples, questionnaire, and report template outputs.
- Execute the listed `810-regulations-eu-mifid-ii` acceptance prompt and verify it passes.
- Run `./mvnw clean verify -pl plinth-skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, investment-firm status, regulated-service classification, and final compliance determinations remain outside the skill and must be escalated to qualified owners.
