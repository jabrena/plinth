## Context

Issue #876 defines three scoped EU regulation skills: `810` MiFID II, `811` MAR, and `812` Product Liability Directive. This change creates only the MAR skill so implementation, review, rollback, and validation remain independently bounded.

The existing EU regulation skills establish the expected shape: PML XML source, bundled official-source summary, Java-focused engineering examples, questionnaire or report assets where formal review is expected, Gherkin acceptance scenarios, generated local skill inspection, and explicit "not legal advice" constraints.

## Decisions

### Skill Identifier

Use `811-regulations-eu-market-abuse-regulation` for Market Abuse Regulation guidance.

This follows issue #876 and keeps the new EU regulation skills contiguous after the existing `801`-`809` catalog.

### Skill Shape

The skill uses the established XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/811-skill.xml` defines metadata, goal, scope, constraints, triggers, and workflow.
- `skills-generator/src/main/resources/skill-references/811-regulations-eu-market-abuse-regulation-chapters-summary.xml` summarizes MAR structure and official-source anchors.
- `skills-generator/src/main/resources/skill-references/811-regulations-eu-market-abuse-regulation-engineering-examples.xml` provides Java-focused examples and output guidance.
- `skills-generator/src/main/resources/skill-references/assets/questions/811-market-abuse-regulation-engineering-review-questionnaire.md` captures structured review questions.
- `skills-generator/src/main/resources/skill-references/assets/reports/811-market-abuse-regulation-engineering-review-report-template.md` captures review findings, evidence, owner handoffs, and potential non-compliance signals.
- `skills-generator/src/main/resources/skills.xml` registers the skill id, references, and assets.

### MAR Scope

The skill focuses on Java systems that support trading surveillance, suspicious order and transaction monitoring, market-data monitoring, insider-list workflows, disclosure workflows, alert triage, investigation records, AI-assisted market-abuse detection, model/rule provenance, reviewer decisions, false-positive handling, and escalation to compliance owners.

The skill must not decide whether behavior is insider dealing, market manipulation, unlawful disclosure, or a reportable suspicious order/transaction. Those determinations require legal, compliance, market-surveillance, risk, product, and accountable business-owner review.

### Relationship to Other Regulation Skills

- Use `810-regulations-eu-mifid-ii` when the primary concern is investment-service operations, algorithmic trading governance, order handling, client classification, suitability, or appropriateness.
- Use `802-regulations-dora` when the primary concern is ICT operational resilience, third-party ICT risk, resilience testing, or financial-entity incident handling.
- Use `801-regulations-eu-ai-act` when the primary concern is AI-system classification, high-risk AI domains, AI transparency, or agent governance.
- Use `803-regulations-gdpr` when the primary concern is personal-data processing in surveillance, monitoring, or investigation workflows.
- Use multiple regulation skills together when a Java financial system crosses trading, surveillance, AI, privacy, operational resilience, or disclosure boundaries.

## Two-Step Delivery Plan

1. Make the change easy: add the OpenSpec artifacts, XML source files, report/questionnaire assets, Gherkin feature, and acceptance prompt entry without touching generated release output.
2. Make the behavior change: run local generation and verification so `.agents/skills/811-regulations-eu-market-abuse-regulation` is emitted and validated.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `.agents/skills/811-regulations-eu-market-abuse-regulation/SKILL.md`.
- Inspect generated local chapters summary, engineering examples, questionnaire, and report template outputs.
- Execute the listed `811-regulations-eu-market-abuse-regulation` acceptance prompt and verify it passes.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, market-abuse classification, suspicious-order or transaction reportability, and final compliance determinations remain outside the skill and must be escalated to qualified owners.
