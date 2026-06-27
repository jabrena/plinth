## Context

Issue #876 defines three scoped EU regulation skills: `810` MiFID II, `811` MAR, and `812` Product Liability Directive. This change creates only the Product Liability Directive skill so implementation, review, rollback, and validation remain independently bounded.

The existing EU regulation skills establish the expected shape: PML XML source, bundled official-source summary, Java-focused engineering examples, questionnaire or report assets where formal review is expected, Gherkin acceptance scenarios, generated local skill inspection, and explicit "not legal advice" constraints.

## Decisions

### Skill Identifier

Use `812-regulations-eu-product-liability-directive` for Product Liability Directive guidance.

This follows issue #876 and keeps the new EU regulation skills contiguous after the existing `801`-`809` catalog.

### Skill Shape

The skill uses the established XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/812-skill.xml` defines metadata, goal, scope, constraints, triggers, and workflow.
- `skills-generator/src/main/resources/skill-references/812-regulations-eu-product-liability-directive-chapters-summary.xml` summarizes Directive (EU) 2024/2853 structure and official-source anchors.
- `skills-generator/src/main/resources/skill-references/812-regulations-eu-product-liability-directive-engineering-examples.xml` provides Java-focused examples and output guidance.
- `skills-generator/src/main/resources/skill-references/assets/questions/812-product-liability-directive-engineering-review-questionnaire.md` captures structured review questions.
- `skills-generator/src/main/resources/skill-references/assets/reports/812-product-liability-directive-engineering-review-report-template.md` captures review findings, evidence, owner handoffs, and potential product-liability evidence gaps.
- `skills-generator/src/main/resources/skills.xml` registers the skill id, references, and assets.

### Product Liability Directive Scope

The skill focuses on Java software products, SaaS products, embedded software components, GenAI assistants, RAG workflows, AI agents, generated instructions, automated updates, warnings/instructions for use, cybersecurity-sensitive product features, post-market corrective updates, incident evidence, and product-safety escalation.

The skill must not decide whether a product is defective, whether damage is compensable, whether liability applies, or whether an economic operator is legally responsible. Those determinations require legal, compliance, product, safety, security, risk, support, and accountable business-owner review.

### Relationship to Other Regulation Skills

- Use `801-regulations-eu-ai-act` when the primary concern is AI-system classification, high-risk AI domains, AI transparency, general-purpose model concerns, or AI-agent governance.
- Use `805-regulations-eu-cyber-resilience-act` when the primary concern is product cybersecurity, vulnerability handling, secure development, or conformity evidence for products with digital elements.
- Use `804-regulations-eu-nis2` when the primary concern is essential or important entity cybersecurity governance, incident response, or operational security.
- Use `803-regulations-gdpr` when the primary concern is personal-data processing in product telemetry, prompts, logs, support records, or incident evidence.
- Use multiple regulation skills together when the same Java product crosses AI, product safety, cybersecurity, privacy, operational, or market boundaries.

## Two-Step Delivery Plan

1. Make the change easy: add the OpenSpec artifacts, XML source files, report/questionnaire assets, Gherkin feature, and acceptance prompt entry without touching generated release output.
2. Make the behavior change: run local generation and verification so `.agents/skills/812-regulations-eu-product-liability-directive` is emitted and validated.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `.agents/skills/812-regulations-eu-product-liability-directive/SKILL.md`.
- Inspect generated local chapters summary, engineering examples, questionnaire, and report template outputs.
- Execute the listed `812-regulations-eu-product-liability-directive` acceptance prompt and verify it passes.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, defect determinations, compensable-damage questions, economic-operator responsibility, and final liability assessments remain outside the skill and must be escalated to qualified owners.
