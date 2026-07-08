title=What's new in Plinth 0.17.0?
date=2026-07-06
type=post
tags=blog,skills,java,agents,design,regulations,validation
author=Juan Antonio Breña Moral
status=draft
~~~~~~

In this release, we introduce new improvements in the development workflow but one of the most important changes is the project name, changing the repository name from `cursor-rules-java` to `plinch`.

## Why Plinch?

In civil architecture, a `plinth` is the base that supports what people actually see. It is not the column, the arch, the road, or the aqueduct, but without a good plinth the visible structure loses alignment, load-bearing capacity, and long-term stability. That image fits the project better than the original name. 

The project is no longer only a collection of `Cursor rules for Java`, today the project includes `Commands`, `Agents`, `Skills`, generated inventories, OpenSpec workflows, validation pipelines, regulation review aids, framework-specific guidance, and release assets. The new name, `Plinth`, gives the project a broader identity without losing its Java roots.

The metaphor comes from classical Roman architecture. In the old Roman empire, durable engineering depended on foundations, proportions, materials, load paths, maintenance, and repeatable methods. [Marcus Vitruvius Pollio](https://en.wikipedia.org/wiki/Vitruvius), described architecture around the enduring qualities often summarized as `firmitas`, `utilitas`, and `venustas`: [strength, usefulness, and beauty](https://en.wikipedia.org/wiki/Firmness,_commodity,_and_delight).

![](/plinth/images/2026/7/firmitas-utilitas-venustas.png)

That is a useful analogy for modern software engineering with AI.

An AI agent can generate code quickly, but speed is not enough. A team still needs a stable base: requirements, design constraints, compatibility strategy, tests, security checks, architecture boundaries, operational evidence, and human review. Without that base, the generated work may look impressive while resting on weak assumptions.

Once it was explained the repository name change, lets continue with the article reviewing the new features included in the release:

- [Enhancing OpenSpec Changes](#enhancing-openspec-changes)
- [Improving migration safety with Flyway, Mongock, and Parallel Change](#improving-migration-safety-with-flyway-mongock-and-parallel-change)
- [Making architecture boundaries visible with Onion Architecture](#making-architecture-boundaries-visible-with-onion-architecture)
- [Improving Maven guidance](#improving-maven-guidance)
- [Extending EU regulations and ISO engineering review skills](#extending-eu-regulations-and-iso-engineering-review-skills)
- [Improving CI and documentation validation](#improving-ci-and-documentation-validation)
- [Next steps](#next-steps)

If you have questions about the project, how to customize it for your team, how to use the skills in daily work, or how to solve tooling issues, use [`GitHub Discussions`](https://github.com/jabrena/plinth/discussions).

**Help this project grow:** [If this project helps your team, become a sponsor.](https://github.com/sponsors/jabrena)

<a id="enhancing-openspec-changes"></a>

## Enhancing OpenSpec changes

AI coding tools are very good at producing code quickly. That is useful, but speed alone is not the same as engineering discipline. Real delivery work also needs design sequencing, compatibility analysis, test strategy, small slices, and reviewable evidence.

This release adds a new family of design skills:

- [`@051-design-two-steps-methods`](https://www.skills.sh/jabrena/plinth/051-design-two-steps-methods)
- [`@052-design-hamburger-method`](https://www.skills.sh/jabrena/plinth/052-design-hamburger-method)
- [`@053-design-simple-rules`](https://www.skills.sh/jabrena/plinth/053-design-simple-rules)
- [`@054-design-tdd`](https://www.skills.sh/jabrena/plinth/054-design-tdd)
- [`@055-design-parallel-change`](https://www.skills.sh/jabrena/plinth/055-design-parallel-change)
- [`@056-design-avoid-breaking-changes`](https://www.skills.sh/jabrena/plinth/056-design-avoid-breaking-changes)

The goal is to move an agent from "generate the final patch" to "understand the change path". If you are interested in this direction, I recommend reading the following article: [From code generation to software engineering](/cursor-rules-java/blog/2026/06/from-code-generation-to-software-engineering.html)

<a id="improving-migration-safety-with-flyway-mongock-and-parallel-change"></a>

## Improving migration safety with Flyway, Mongock, and Parallel Change

Database migrations are one of the easiest places for an AI agent to produce a clean-looking but unsafe patch. Bad migrations can damage a database in ways that are hard to recover from:

- A column is renamed in place while old application instances are still running, so writes start failing during a rolling deployment.
- A column is dropped because the new code no longer uses it, but a delayed worker, report, export, or integration still reads it.
- A backfill updates every row with the wrong default, overwriting meaningful historical data with a value that cannot be reconstructed.
- A migration changes money, time-zone, status, or identifier semantics without preserving the original business meaning.
- A `NOT NULL`, unique, or foreign-key constraint is added before dirty production data has been measured and cleaned.
- A long-running DDL statement locks a hot table and turns a small schema change into an availability incident.
- A MongoDB document migration rewrites nested fields without handling old variants, partial documents, or unknown enum values.
- A rollback restores the previous application version, but the destructive migration has already removed the shape that version needs.

This release improves the migration guidance for:

- [`@313-frameworks-spring-db-migrations-flyway`](https://www.skills.sh/jabrena/plinth/313-frameworks-spring-db-migrations-flyway)
- [`@413-frameworks-quarkus-db-migrations-flyway`](https://www.skills.sh/jabrena/plinth/413-frameworks-quarkus-db-migrations-flyway)
- [`@513-frameworks-micronaut-db-migrations-flyway`](https://www.skills.sh/jabrena/plinth/513-frameworks-micronaut-db-migrations-flyway)
- [`@316-frameworks-spring-mongodb-migrations-mongock`](https://www.skills.sh/jabrena/plinth/316-frameworks-spring-mongodb-migrations-mongock)
- [`@416-frameworks-quarkus-mongodb-migrations-mongock`](https://www.skills.sh/jabrena/plinth/416-frameworks-quarkus-mongodb-migrations-mongock)
- [`@516-frameworks-micronaut-mongodb-migrations-mongock`](https://www.skills.sh/jabrena/plinth/516-frameworks-micronaut-mongodb-migrations-mongock)

The new guidance emphasizes migration safety, antipatterns, and `Parallel Change`.

```text
Phase     Goal
--------- ------------------------------------------------------------
Expand    Add the new schema or document shape without removing the old one
Migrate   Backfill, dual-write, compare, and observe
Contract  Remove the old shape only after rollout evidence exists
```

This is why `@055-design-parallel-change` belongs in the design workflow before framework-specific Flyway or Mongock implementation guidance. The agent should understand the transition before it writes the migration.

> **⚠️ AVOID RUNNING DATABASE MIGRATION TASKS WITHOUT HITL.**
Human-in-the-loop review is necessary because migrations can destroy or reinterpret production data, block critical tables, break rollback paths, and affect systems that are not visible in the local codebase. An agent can draft the plan, checks, and scripts, but a qualified human owner should review the migration intent, data impact, rollback strategy, monitoring evidence, and execution window before it reaches production.

**Note:** If you Domain/Subdomain has Microservices associated with Databases, and you are adopting GenAI tools maybe you should review the current architecture to mitigate potential database production issues. 

If you want to go deeper into this topic, I recommend reading: [Why Do I Need to Use the Parallel Change Pattern?](/cursor-rules-java/blog/2026/07/why-do-i-need-to-use-the-parallel-change-pattern.html)

- https://machinelearning.apple.com/research/illusion-of-thinking
- https://genai.owasp.org/

<a id="making-architecture-boundaries-visible-with-onion-architecture"></a>

## Making architecture boundaries visible with Onion Architecture

This release adds [`@707-technologies-onion-architecture`](https://www.skills.sh/jabrena/plinth/707-technologies-onion-architecture), a framework-agnostic skill for reviewing Java application boundaries.

The skill helps engineers and agents inspect whether dependency direction and responsibility placement are consistent with Onion Architecture:

- Domain code should not depend on framework adapters.
- Application services should coordinate use cases without becoming infrastructure code.
- Adapters should translate between external systems and the application boundary.
- Framework-specific details should stay outside the core model unless the team has made an explicit tradeoff.

The skill also includes `ArchUnit`-aware verification guidance without forcing every project to add a new dependency automatically. That is important because architecture tests should support the existing project context, not become cargo-cult configuration.

```java
@ArchTest
static final ArchRule onion_architecture_boundaries = onionArchitecture()
        .domainModels("info.jab.mv.domain..")
        .applicationServices("info.jab.mv.application..")
        .adapter("cli", "info.jab.mv.adapter.in.cli..")
        .adapter("filesystem", "info.jab.mv.adapter.out.filesystem..")
        .adapter("http", "info.jab.mv.adapter.out.http..")
        .withOptionalLayers(true);
```

https://www.archunit.org/userguide/html/000_Index.html#_onion_architecture

For framework agents, the skill is useful before making changes in `Spring Boot`, `Quarkus`, or `Micronaut` applications. It gives the agent a boundary review language before it starts moving packages, introducing adapters, or changing service responsibilities.

<a id="improving-maven-guidance"></a>

## Improving Maven guidance

Maven support continues to be one of the most used parts of the project, and `0.17.0` improves it in two directions.

First, [`@111-java-maven-dependencies`](https://www.skills.sh/jabrena/plinth/111-java-maven-dependencies) now includes `JavaMoney` guidance. This helps agents recognize when a Java project needs Money and Currency API support, including `JSR 354` and `Moneta` context.

Use this path when a project needs to model money, currencies, amounts, exchange-rate concerns, pricing, billing, financial calculations, or domain values where `BigDecimal` alone is not enough to describe the business meaning.

<a id="extending-eu-regulations-and-iso-engineering-review-skills"></a>

## Extending EU regulations and ISO Standard engineering review skills

`0.16.0` introduced the first family of EU regulation engineering review skills. `0.17.0` extends that family with financial-market, market-integrity, product-liability, and AI management-system review support:

- [`@810-regulations-eu-mifid-ii`](https://www.skills.sh/jabrena/plinth/810-regulations-eu-mifid-ii)
- [`@811-regulations-eu-market-abuse-regulation`](https://www.skills.sh/jabrena/plinth/811-regulations-eu-market-abuse-regulation)
- [`@812-regulations-eu-product-liability-directive`](https://www.skills.sh/jabrena/plinth/812-regulations-eu-product-liability-directive)
- [`@813-regulations-iso-42001`](https://www.skills.sh/jabrena/plinth/813-regulations-iso-42001)

These skills include questionnaires, report templates, examples, and acceptance prompts.

The common workflow is:

1. Classify whether the system, product, workflow, or AI capability is in scope.
2. Ask for concrete engineering evidence.
3. Map regulatory or standard concerns to controls, logs, tests, approvals, documentation, and release gates.
4. Produce a report for qualified owner review.

These skills are engineering review aids. **They do not provide legal advice and they do not replace qualified legal, compliance, privacy, security, risk, product, audit, or governance owners.**

For fintech and enterprise teams, the practical value is early evidence. An AI agent working on trading workflows, issuer disclosure, surveillance alerts, product updates, model governance, RAG systems, or AI-assisted delivery should know which engineering questions may need owner review before release.

The current regulation and standard review coverage is:

- `EU AI Act`: Reviews AI-system scope, risk classification, transparency, human oversight, evidence, and owner handoff.
  Use it for LLMs, RAG systems, AI agents, model-driven decisions, generated artifacts, and AI-assisted workflows.
- `DORA`: Reviews digital operational resilience for financial entities and ICT-dependent business services.
  Use it for incident evidence, continuity, operational resilience testing, third-party ICT risk, and critical-service controls.
- `GDPR`: Reviews privacy engineering concerns around personal data collection, processing, logging, sharing, and deletion.
  Use it for lawful-basis evidence, minimization, retention, data-subject rights, exports, and privacy-safe observability.
- `NIS2`: Reviews cybersecurity and resilience evidence for essential, important, and critical-sector services.
  Use it for asset inventory, incident escalation, vulnerability handling, continuity, supply-chain risk, and secure release gates.
- `Cyber Resilience Act`: Reviews product-security evidence for software and products with digital elements.
  Use it for secure-by-design controls, vulnerability handling, SBOM evidence, security updates, documentation, and support periods.
- `EU Data Act`: Reviews connected-product, related-service, data-sharing, portability, and cloud-switching concerns.
  Use it for data access APIs, request workflows, data recipients, smart-contract-like automation, and portability evidence.
- `Digital Services Act`: Reviews due-diligence and transparency evidence for intermediaries, hosting services, and platforms.
  Use it for notice-and-action flows, moderation logs, appeals, recommender explanations, ad transparency, and marketplace controls.
- `Digital Markets Act`: Reviews gatekeeper-platform concerns around core platform services, interoperability, and fair access.
  Use it for business-user data access, consent evidence, ranking signals, export workflows, anti-circumvention, and compliance handoff.
- `MiFID II`: Reviews investment-services, trading, investor-protection, transparency, and record-keeping evidence.
  Use it for client classification, suitability, appropriateness, order handling, best execution, algorithmic trading, and audit trails.
- `Market Abuse Regulation`: Reviews market-integrity evidence for issuer disclosures, insider information, surveillance, and alerts.
  Use it for insider lists, market soundings, suspicious order reporting, employee dealing controls, watchlists, and restricted lists.
- `Product Liability Directive`: Reviews software, AI-enabled product, component, update, and post-release safety evidence.
  Use it for defect analysis, product documentation, maintenance evidence, monitoring, update provenance, and owner review.
- `ISO/IEC 42001`: Reviews AI management-system evidence for organizational AI governance and lifecycle controls.
  Use it for AI inventories, risk treatment, roles, policies, RAG governance, agent controls, monitoring, incidents, and improvement.

If you are interested in this topic, I recommend reading both articles: [Introduction to EU regulations Part I](/cursor-rules-java/blog/2026/06/introduction-to-eu-regulations-part-i.html) and [Introduction to EU regulations Part II](/cursor-rules-java/blog/2026/07/introduction-to-eu-regulations-part-ii.html)

<a id="improving-ci-and-documentation-validation"></a>

## Improving CI and documentation validation

This release also improves the validation story around generated content and documentation.

The Maven workflow now includes `VirusTotal` checks. In this [example analysis](https://gist.github.com/jabrena/0972041d41c6f1352e518ed4d4ae3c05), `VirusTotal` used `75` security applications/engines and reported `0 malicious` and `0 suspicious` detections: `ALYac`, `APEX`, `AVG`, `Acronis`, `AhnLab-V3`, `Alibaba`, `Antiy-AVL`, `Arcabit`, `Avast`, `Avast-Mobile`, `Avira`, `BitDefender`, `BitDefenderFalx`, `Bkav`, `CAT-QuickHeal`, `CMC`, `CTX`, `ClamAV`, `CrowdStrike`, `Cylance`, `Cynet`, `DeepInstinct`, `DrWeb`, `ESET-NOD32`, `Elastic`, `Emsisoft`, `F-Secure`, `Fortinet`, `GData`, `Google`, `Gridinsoft`, `Ikarus`, `Jiangmin`, `K7AntiVirus`, `K7GW`, `Kaspersky`, `Kingsoft`, `Lionic`, `Malwarebytes`, `MaxSecure`, `McAfeeD`, `MicroWorld-eScan`, `Microsoft`, `NANO-Antivirus`, `Paloalto`, `Panda`, `Rising`, `SUPERAntiSpyware`, `Sangfor`, `SentinelOne`, `Skyhigh`, `Sophos`, `Symantec`, `SymantecMobileInsight`, `TACHYON`, `Tencent`, `Trapmine`, `TrellixENS`, `TrendMicro`, `TrendMicro-HouseCall`, `Trustlook`, `VBA32`, `VIPRE`, `Varist`, `ViRobot`, `VirIT`, `Webroot`, `Xcitium`, `Yandex`, `Zillya`, `ZoneAlarm`, `Zoner`, `alibabacloud`, `huorong`, `tehtris`. The project also adds a Codex-backed OpenSpec workflow for generating specs from issues, including branch creation, generated PR labeling, and tolerance for blocked runs.

For documentation, the previous script-based Markdown validator has been replaced by a Maven-based `markdown-validator` module. The new module includes:

- A CLI entry point.
- Remote-link validation.
- Unit tests.
- PMD configuration.
- Architecture tests.

This change makes documentation validation a first-class project module instead of a loose script beside the build.

The important point is the same as with skill validation: generated and maintained text is part of the product. It needs tests, structure, and repeatable feedback.

<a id="next-steps"></a>

## Next steps

The next phase is to keep connecting the workflow pieces so they feel natural in daily engineering work.

Functionally, the next workstreams are:

- Continue expanding executable acceptance coverage for `Skills`, `Agents`, and `Commands`, especially where generated guidance affects file edits, command execution, or release evidence.
- Make the design workflow easier to apply from real issues, so `two-step`, `hamburger`, `TDD`, `simple rules`, `parallel change`, and `breaking-change` review are selected at the right time.
- Improve framework routing so architecture and migration decisions are made before a `Spring Boot`, `Quarkus`, or `Micronaut` agent edits implementation files.
- Keep improving Maven dependency and version workflows, with better separation between discovery, recommendation, and project modification.
- Continue refining regulation engineering review skills so they produce useful owner handoff reports without pretending to replace qualified human review.
- Improve the OpenSpec-based automation path from issue to proposal, spec, implementation, validation, and release notes.

Enjoy.
