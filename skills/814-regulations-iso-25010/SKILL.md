---
name: 814-regulations-iso-25010
description: Use when reviewing, designing, or modifying Java enterprise systems and need a structured, repeatable ISO/IEC 25010:2023 quality-attribute review covering Functional Suitability, Performance Efficiency, Compatibility, Interaction Capability, Reliability, Security, Maintainability, Flexibility, and Safety, producing an engineering review report rather than an interactive ADR discovery. Part of Plinth Toolkit
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.19.0
---
# ISO/IEC 25010:2023 Quality Model Guidance for Java Enterprise Engineering

Use this Skill to run a structured, repeatable ISO/IEC 25010:2023 product-quality review of a Java enterprise system, covering all nine quality characteristics: Functional Suitability, Performance Efficiency, Compatibility, Interaction Capability, Reliability, Security, Maintainability, Flexibility, and Safety.

Apply this Skill to determine what practical Java engineering evidence, review actions, and owner handoffs are needed before a Java enterprise system, feature, or delivery pipeline is considered quality-reviewed against the ISO/IEC 25010:2023 product quality model.

This Skill is not certification advice, compliance advice, conformity advice, an audit conclusion, or a final conformity decision. It helps Java architects, tech leads, and reviewers identify how each ISO/IEC 25010:2023 quality characteristic applies to the system under review and translate it into concrete engineering evidence, findings, and action items for qualified owners.

The purpose of this Skill is to increase awareness of potential quality-attribute gaps in a Java enterprise system and create reviewable engineering evidence for qualified owners. The response produced by this Skill does not represent ISO/IEC 25010:2023 certification readiness, compliance approval, audit findings, or final conformity with ISO/IEC 25010:2023.

The main question is:

> What should a Java team review, document, and escalate so a Java enterprise system demonstrably meets each ISO/IEC 25010:2023 quality characteristic, with reviewable evidence rather than assumed quality?

Source provenance: the ISO/IEC 25010:2023 public standard summary was reviewed while authoring the bundled references. Official source references include:

- ISO/IEC 25010:2023 standard page: https://www.iso.org/standard/78176.html
- ISO/IEC 25000 SQuaRE series overview: https://iso25000.com/index.php/en/iso-25000-standards/iso-25010

Do not fetch or ingest external standard, certification, or audit web pages at runtime. Use the bundled references and escalate certification, compliance, conformity, and audit questions to qualified owners.

ISO/IEC 25010:2023 summary reference: [ISO/IEC 25010:2023 product quality model summary](references/814-regulations-iso-25010-chapters-summary.md).

Java engineering examples reference: [ISO/IEC 25010:2023 Java Enterprise engineering examples](references/814-regulations-iso-25010-engineering-examples.md).

Questionnaire asset: [ISO/IEC 25010:2023 engineering review questionnaire](assets/questions/814-iso-25010-engineering-review-questionnaire.md).

Report template asset: [ISO/IEC 25010:2023 engineering review report template](assets/reports/814-iso-25010-engineering-review-report-template.md).

## Scope

This Skill applies to:

- Java enterprise systems, services, modules, APIs, and delivery pipelines under structured quality-attribute review
- Spring Boot, Quarkus, Micronaut, and framework-agnostic Java systems
- All nine ISO/IEC 25010:2023 product-quality characteristics: Functional Suitability, Performance Efficiency, Compatibility, Interaction Capability, Reliability, Security, Maintainability, Flexibility, and Safety
- Java-focused engineering evidence such as tests, static analysis, load/soak test results, circuit breakers and retries, authentication and authorization implementation, module boundaries, configuration externalization, and operational guardrails
- Owner handoffs to architecture, product, security, platform, operations, and accountable business owners when a finding requires a decision beyond engineering review

## ISO/IEC 25010:2023 Engineering Review

Treat certification, compliance, and conformity determinations as qualified owner decisions.

Engineering teams should still create evidence that makes those decisions reviewable:

- Which Java system, module, service, or delivery pipeline is in scope for the quality-attribute review
- Which of the nine ISO/IEC 25010:2023 quality characteristics are most relevant to the system under review, and why
- Which concrete Java engineering evidence supports or contradicts each characteristic: tests, code review notes, load-test results, static-analysis output, configuration, operational runbooks, and monitoring signals
- Which findings represent confirmed gaps, potential gaps, or no identified concern for each characteristic
- Which owners must review or act on each finding, and what the recommended action item is

## Constraints

Translate ISO/IEC 25010:2023 quality characteristics into structured, repeatable Java engineering review evidence and action items. Do not provide certification, compliance, conformity, or audit conclusions, and do not turn the review into an interactive ADR-discovery workflow.

- **NOT CERTIFICATION, COMPLIANCE, OR CONFORMITY ADVICE**: Frame findings as engineering review evidence and action items; never claim ISO/IEC 25010:2023 certification readiness, compliance approval, audit pass/fail status, or final conformity
- **BUNDLED REFERENCES FIRST**: Read the bundled ISO/IEC 25010:2023 chapters-summary and engineering-examples references, the questionnaire, and the report template before implementation review; do not fetch external standard, certification, or audit pages at runtime
- **NINE-CHARACTERISTIC COVERAGE**: Address all nine ISO/IEC 25010:2023 quality characteristics — Functional Suitability, Performance Efficiency, Compatibility, Interaction Capability, Reliability, Security, Maintainability, Flexibility, and Safety — for every review, even when a characteristic's finding is "no identified concern"
- **STRUCTURED REVIEW, NOT INTERACTIVE ADR DISCOVERY**: Use this Skill for a structured, repeatable, questionnaire-driven quality-attribute review that produces a fixed engineering review report rather than an interactive, conversational Architectural Decision Record discovery session
- **EVIDENCE FIRST**: Prefer reviewable Java engineering evidence over claims that a quality characteristic is satisfied
- **JAVA ENGINEERING FOCUS**: Translate each quality characteristic into concrete Java Enterprise review guidance: code-level, architecture-level, and operational signals a reviewer can check
- **OWNER HANDOFFS**: Include architecture, product, security, platform, operations, and accountable business owners when a finding requires a decision, prioritization, or risk acceptance beyond engineering review

## When to use this skill

- Run a structured ISO/IEC 25010:2023 quality-attribute review of a Java enterprise system
- Review a Java system for functional suitability, performance efficiency, compatibility, interaction capability, reliability, security, maintainability, flexibility, or safety
- Produce an ISO/IEC 25010:2023 engineering review report with findings and action items for a Java system
- Disambiguate a structured quality review from interactive ADR discovery for non-functional requirements
- Escalate quality-attribute findings to qualified architecture, product, security, or business owners

## Workflow

1. **Read ISO/IEC 25010:2023 references, questionnaire, and report template**

Read `references/814-regulations-iso-25010-chapters-summary.md`, `references/814-regulations-iso-25010-engineering-examples.md`, `assets/questions/814-iso-25010-engineering-review-questionnaire.md`, and `assets/reports/814-iso-25010-engineering-review-report-template.md` in that order. Use the summary for the nine ISO/IEC 25010:2023 quality characteristics, their sub-characteristics, and their Java Enterprise engineering review impact. Use the engineering examples for worked Java control patterns per characteristic. Use the questionnaire to classify system scope and evidence per characteristic before producing the report. Do not start implementation review until the summary, examples reference, questionnaire, and report template are understood.

2. **Complete questionnaire and classify system scope**

Use `assets/questions/814-iso-25010-engineering-review-questionnaire.md` as a checklist against trusted local project evidence and maintainer-approved sanitized facts. Identify the Java system, module, service, or delivery pipeline under review; its architecture, users, environments, and lifecycle stage; and the owners accountable for architecture, product, security, platform, operations, and business decisions. Mark unknowns as evidence gaps and escalate missing ownership before release recommendations.

3. **Review implementation evidence per quality characteristic**

Review Java code, configuration, tests, build files, load/soak test results, static-analysis output, circuit breakers and retries, authentication and authorization implementation, module and package boundaries, configuration externalization, operational runbooks, and monitoring evidence against each of the nine ISO/IEC 25010:2023 quality characteristics. Check for gaps between claimed quality attributes and reviewable evidence.

4. **Map quality characteristics to Java engineering controls**

Map each of the nine characteristics — Functional Suitability, Performance Efficiency, Compatibility, Interaction Capability, Reliability, Security, Maintainability, Flexibility, and Safety — to concrete Java engineering controls and evidence: acceptance-criteria traceability, load/capacity testing, API versioning review, error-message and API-documentation review, resilience patterns, authn/authz and dependency-vulnerability review, module-coupling and test-pyramid review, horizontal-scaling and configuration-externalization review, and operational-guardrail review where the system has real-world safety effects.

5. **Generate review report and owner handoffs**

Use `assets/reports/814-iso-25010-engineering-review-report-template.md` to produce a concise engineering review with scope, evidence reviewed, per-characteristic findings across all nine ISO/IEC 25010:2023 quality characteristics, evidence gaps, recommended controls, owner handoffs, residual risks, release readiness notes, and an action plan. State explicitly that certification advice, compliance advice, conformity decisions, and audit conclusions require qualified owner review.

## Reference

For detailed guidance, examples, and constraints, see:

- [references/814-regulations-iso-25010-chapters-summary.md](references/814-regulations-iso-25010-chapters-summary.md)
- [references/814-regulations-iso-25010-engineering-examples.md](references/814-regulations-iso-25010-engineering-examples.md)
