## Context

Issue #939 defines a new skill request: explain ISO/IEC 42001 in the same practical way as the EU AI Act skill, with explicit focus on GenAI development with Java. The issue body supplies the source references, user story, risk areas, and Gherkin acceptance criteria.

Existing regulation skills establish the expected shape: PML XML source, bundled official-source summary, Java-focused engineering examples, questionnaire or report assets where formal review is expected, Gherkin acceptance scenarios, generated local skill inspection, and explicit constraints that the skill provides engineering guidance rather than legal, compliance, certification, or audit conclusions.

## Decisions

### Skill Identifier

Use `813-regulations-iso-42001` for ISO/IEC 42001 guidance.

The `813` prefix follows the existing regulation sequence after `812-regulations-eu-product-liability-directive`. The identifier avoids encoding "GenAI" only in the skill id so the skill can cover the ISO/IEC 42001 AI management system topic broadly, while the content and examples focus on GenAI development with Java as requested by issue #939.

### Skill Shape

The skill uses the established XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/813-skill.xml` defines metadata, goal, scope, constraints, triggers, and workflow.
- `skills-generator/src/main/resources/skill-references/813-regulations-iso-42001-chapters-summary.xml` summarizes ISO/IEC 42001 concepts using issue-provided official references and stable public descriptions.
- `skills-generator/src/main/resources/skill-references/813-regulations-iso-42001-engineering-examples.xml` provides Java-focused GenAI examples and output guidance.
- Optional questionnaire/report assets may be added if the implementation follows the formal review pattern from neighboring regulation skills.
- `skills-generator/src/main/resources/skills.xml` registers the skill id, references, and any assets.

### ISO/IEC 42001 Scope

The skill focuses on GenAI-oriented Java engineering management practices:

- AI-assisted Java code generation and review.
- LLM, RAG, agent, prompt, tool-calling, and model-provider integration boundaries.
- Generated dependency and supply-chain review.
- Prompt, source-code, log, telemetry, and regulated-data exposure.
- Model and prompt provenance, evaluation evidence, human review, release decisions, and rollback evidence.
- Bias, unfair generated business rules, regulatory non-compliance risk, and qualified owner escalation.

The skill must not decide whether an organization is ISO/IEC 42001 certified, whether a management system is conformant, or whether legal/regulatory obligations are satisfied. Those determinations require qualified legal, compliance, audit, security, risk, privacy, product, and accountable business-owner review.

### Relationship to Other Regulation Skills

- Use `801-regulations-eu-ai-act` when the primary concern is EU AI Act classification, prohibited/high-risk AI obligations, transparency, general-purpose AI, or EU AI Act governance.
- Use `813-regulations-iso-42001` when the primary concern is AI management system practices for GenAI development with Java, including risk identification, evidence, controls, and lifecycle governance for AI-assisted delivery.
- Use `803-regulations-gdpr` when the primary concern is personal-data processing in prompts, logs, telemetry, RAG sources, or model interactions.
- Use `805-regulations-eu-cyber-resilience-act` when the primary concern is product cybersecurity, vulnerability handling, secure development, or conformity evidence for products with digital elements.
- Use multiple regulation skills together when the same Java system crosses AI management, legal, privacy, cybersecurity, product, or operational boundaries.

## Two-Step Delivery Plan

1. Make the change easy: add the OpenSpec artifacts, XML source files, reference files, optional questionnaire/report assets, Gherkin feature, and acceptance prompt entry without touching generated release output.
2. Make the behavior change: run local generation and verification so `.agents/skills/813-regulations-iso-42001` is emitted and validated.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `.agents/skills/813-regulations-iso-42001/SKILL.md`.
- Inspect generated local ISO/IEC 42001 summary and engineering examples outputs.
- Execute the listed `813-regulations-iso-42001` acceptance prompt and verify it passes.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this OpenSpec change. Certification status, audit conclusions, legal applicability, regulatory interpretation, and final risk acceptance remain outside the skill and must be escalated to qualified owners.
