title=Introduction to EU regulations Part I
date=2026-06-16
type=post
tags=blog,skills,java,regulations,eu
author=MyRobot
status=published
~~~~~~

## Why this topic matters

Modern Java Enterprise systems are no longer reviewed only for code quality, performance, and availability. Many systems now process personal data, call AI models, automate decisions, operate critical services, integrate with cloud providers, expose online platform workflows, or participate in digital markets.

That means engineering teams need a practical way to translate regulatory concerns into technical evidence.

The new `regulations` skills in this project are designed for that purpose. They do not replace lawyers, compliance officers, privacy teams, security teams, risk owners, or business owners. Instead, they help Java engineers prepare better review material: scope, evidence, gaps, risks, owner handoffs, release decisions, and action plans.

This is the first article in a series about using those skills in real engineering work.

## The skills covered in this article

The current EU regulatory skill family includes:

- `@801-regulations-eu-ai-act`
- `@802-regulations-dora`
- `@803-regulations-gdpr`
- `@804-regulations-eu-nis2`
- `@805-regulations-eu-cyber-resilience-act`
- `@806-regulations-eu-data-act`
- `@807-regulations-eu-digital-services-act`
- `@808-regulations-eu-digital-markets-act`

The common pattern is simple:

1. Classify the system scope.
2. Review local implementation evidence.
3. Map regulatory concerns to engineering controls.
4. Produce a report for qualified owner review.

## EU AI Act

The `EU AI Act` introduces a risk-based framework for AI systems. The European Commission describes it as a legal framework that addresses AI risks and supports trustworthy AI in Europe.

Use `@801-regulations-eu-ai-act` when a Java system uses AI models, LLMs, RAG, AI agents, tool calling, generated artifacts, or model-driven decision support.

The main benefit is that it helps the team separate ordinary AI assistance from higher-risk workflows. For example, a summarization service, a RAG assistant, and an AI agent that can execute database changes do not deserve the same engineering controls.

The skill helps produce evidence for:

- AI system or AI agent classification
- Prohibited-practice or high-risk signals
- Human oversight and approval gates
- Tool access and least privilege
- Prompt, model, retrieval, tool-call, and decision auditability
- Monitoring, incident routing, rollback, and owner handoff

To receive a good report, provide the AI use case, affected users, model/provider information, RAG sources, tool permissions, generated outputs, approval workflow, logs, monitoring evidence, and any known legal or compliance owner.

**Official reference:** https://digital-strategy.ec.europa.eu/en/policies/regulatory-framework-ai

**Legal text:** https://eur-lex.europa.eu/eli/reg/2024/1689/oj/eng

## DORA

`DORA`, the Digital Operational Resilience Act, focuses on ICT risk and cyber resilience across the financial sector. The European Commission describes DORA as a harmonised EU-wide framework to manage ICT and cyber risks across financial services, including critical third-party ICT providers.

Use `@802-regulations-dora` when a Java system supports a financial entity, important business service, critical ICT function, payment flow, trading, lending, insurance, accounting, managed service, or third-party ICT provider integration.

The main benefit is operational clarity. The skill asks whether the system has an asset inventory, incident flow, monitoring, backup, restore, resilience testing, third-party provider evidence, and exit plan.

The skill helps produce evidence for:

- Business service and ICT dependency inventory
- Incident detection, classification, escalation, and reconstruction
- Backup, restore, continuity, failover, and rollback controls
- Digital operational resilience testing
- Third-party ICT provider monitoring, contracts, SLAs, and exit paths
- Release decision and residual-risk ownership

To receive a good report, provide service ownership, dependency diagrams, production topology, data stores, message brokers, cloud and SaaS providers, RTO/RPO expectations, runbooks, incident workflow, alert rules, backup and restore test evidence, and provider documentation.

**Official reference:** https://finance.ec.europa.eu/regulation-and-supervision/financial-services-legislation/implementing-and-delegated-acts/digital-operational-resilience-regulation_en

**Legal text:** https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2554

## GDPR

`GDPR` remains the core EU data protection regulation. The European Commission explains that it creates rights and obligations for individuals, controllers, and processors around personal data.

Use `@803-regulations-gdpr` when a Java system collects, stores, transforms, logs, exports, deletes, or shares personal data.

The main benefit is privacy engineering discipline. The skill pushes the team to identify personal data before discussing implementation changes. It also keeps the report focused on engineering controls instead of pretending to make a legal determination.

The skill helps produce evidence for:

- Personal-data inventory and data flows
- Processing purposes and owner handoff for lawful-basis review
- Controller, processor, vendor, or subprocessor boundaries
- Data minimization and privacy-by-design controls
- Data-subject rights workflows
- Retention, deletion, cache invalidation, search-index removal, and backup handling
- Privacy-safe logging, breach evidence, and transfer-review concerns

To receive a good report, provide DTOs, schemas, APIs, logs, caches, indexes, exports, backups, retention rules, deletion jobs, vendor integrations, data-subject request workflows, privacy owner, and known transfer or DPIA concerns.

**Official reference:** https://commission.europa.eu/law/law-topic/data-protection_en

**Legal text:** https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32016R0679

## NIS2

`NIS2` is the EU cybersecurity directive for essential and important entities across critical sectors. The Commission explains that it introduces cybersecurity risk-management measures, reporting requirements, cooperation, supervision, and enforcement.

Use `@804-regulations-eu-nis2` when a Java system supports critical-sector services, essential or important entities, managed service providers, supply-chain dependencies, or cybersecurity incident escalation.

The main benefit is stronger cybersecurity evidence. Instead of only saying "we use secure coding", the report asks for inventories, hardening, vulnerabilities, logging, monitoring, access control, cryptography, backup, recovery, and supply-chain evidence.

The skill helps produce evidence for:

- Asset and service inventory
- Secure configuration and vulnerability handling
- Dependency and provider risk
- Incident detection, containment, escalation, and post-incident review
- Backup, recovery, continuity, and rollback
- Access control, MFA signals, secrets handling, encryption, and privileged operation auditability
- Secure change control for releases, configuration, IAM, dependencies, and emergency fixes

To receive a good report, provide sector context, service criticality, architecture, dependency inventory, SBOM or dependency scan evidence, vulnerability records, CI/CD workflow, IAM model, secrets handling, monitoring, runbooks, incident process, backup/restore tests, and security owner.

**Official reference:** https://digital-strategy.ec.europa.eu/en/policies/nis2-directive

**Legal text:** https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022L2555

## Cyber Resilience Act

The `Cyber Resilience Act` introduces mandatory cybersecurity requirements for products with digital elements. The Commission explains that manufacturers must address cybersecurity across planning, design, development, maintenance, and vulnerability handling.

Use `@805-regulations-eu-cyber-resilience-act` when a Java product, library, service, agent tool, desktop app, device backend, embedded integration, or software component may be distributed or made available as a product with digital elements.

The main benefit is product-security readiness. The skill moves the review from "does the service run?" to "can we prove secure-by-design, vulnerability handling, update delivery, documentation, and lifecycle support?"

The skill helps produce evidence for:

- Product and component scope
- Secure-by-design controls
- Vulnerability disclosure and coordinated remediation
- Security update mechanisms
- Dependency, SBOM, build, signing, and release evidence
- User-facing security documentation
- Support period, incident routing, and recall or withdrawal paths

To receive a good report, provide product description, distribution model, component inventory, build pipeline, dependency metadata, SBOM, release signing, update mechanism, vulnerability intake process, security tests, support policy, user documentation, and product/security owner.

**Official reference:** https://digital-strategy.ec.europa.eu/en/policies/cyber-resilience-act

**Legal text:** https://eur-lex.europa.eu/eli/reg/2024/2847/oj/eng

## Data Act

The `Data Act` is designed to improve access to and use of data, especially data from connected products and related services. The Commission explains that it clarifies who can use what data and under which conditions, and it supports data sharing, cloud switching, and interoperability.

Use `@806-regulations-eu-data-act` when a Java system supports connected products, related services, data access APIs, data-sharing request workflows, data recipients, cloud switching, portability, data spaces, or smart-contract-like automation.

The main benefit is making data access reviewable. The skill asks who the user, data holder, data recipient, third party, provider, or public-sector requester is before recommending technical controls.

The skill helps produce evidence for:

- Dataset, metadata, API, Kafka topic, object store, and export inventory
- Entitlement, authorization, purpose, refusal, suspension, deletion, and audit evidence
- Machine-readable exports, API contracts, schemas, and metadata
- Cloud switching and provider-exit runbooks
- Non-personal data safeguards and mixed-dataset privacy handoff
- Trade-secret and commercially sensitive data owner handoff
- Complaint, dispute, request, and release-gate evidence

To receive a good report, provide dataset inventory, API contracts, event schemas, metadata catalogs, user and recipient roles, authorization rules, request workflow, export formats, cloud provider details, switching runbooks, trade-secret concerns, privacy concerns, and data owner.

**Official reference:** https://digital-strategy.ec.europa.eu/en/policies/data-act

**Legal text:** https://eur-lex.europa.eu/eli/reg/2023/2854/oj/eng

## Digital Services Act

The `Digital Services Act` introduces rules for online services used by European citizens, including marketplaces, social networks, app stores, and online travel or accommodation platforms.

Use `@807-regulations-eu-digital-services-act` when a Java system supports hosting, online platforms, marketplaces, content moderation, notice-and-action flows, complaints, appeals, recommender systems, advertising, trader traceability, transparency reporting, or VLOP/VLOSE evidence.

The main benefit is trust-and-safety traceability. The skill helps the team make content decisions, notices, appeals, recommender explanations, ad metadata, and transparency reports auditable.

The skill helps produce evidence for:

- Intermediary, hosting, platform, marketplace, search, or recommender scope
- Notice intake, validation, triage, action, response, and misuse protection
- Content decision audit logs and statement-of-reasons records
- Complaint, appeal, reinstatement, and out-of-court dispute workflows
- Recommender, ranking, advertising, and user-control evidence
- Trader traceability and marketplace controls
- VLOP/VLOSE risk, audit, crisis-response, researcher-access, and transparency evidence where applicable

To receive a good report, provide platform role, user-generated content flows, moderation policies, notice and authority-order workflows, decision logs, appeal records, recommender/ranking logic, ad metadata, trader data, transparency report jobs, VLOP/VLOSE scale signals, and trust-and-safety owner.

**Official reference:** https://digital-strategy.ec.europa.eu/en/policies/digital-services-act

**Legal text:** https://eur-lex.europa.eu/eli/reg/2022/2065/oj/eng

## Digital Markets Act

The `Digital Markets Act` focuses on large digital platforms designated as gatekeepers. The Commission explains that gatekeepers provide core platform services such as search engines, app stores, messenger services, and other major platform services, and must follow specific obligations and prohibitions.

Use `@808-regulations-eu-digital-markets-act` when a Java system may support gatekeeper-platform concerns, core platform services, interoperability, business-user data access, consent-dependent data combination, ranking, self-preferencing, advertising transparency, or anti-circumvention controls.

The main benefit is platform governance evidence. The skill helps platform teams document whether an API, export, ranking change, consent flow, interoperability interface, or access term needs qualified owner review before release.

The skill helps produce evidence for:

- Core platform service and gatekeeper-scope signals
- Business-user and end-user journey maps
- Interoperability interfaces and access terms
- Business-user data access and export workflows
- Consent and preference records
- Ranking, search, recommendation, advertising, and self-preferencing audit signals
- Anti-circumvention guardrails
- Compliance evidence handoff and release gates

To receive a good report, provide platform scope, user scale signals, business-user journeys, data access APIs, export workflows, consent and preference records, ranking experiment evidence, advertising metrics, interoperability contracts, access policies, change records, and platform governance owner.

**Official reference:** https://digital-markets-act.ec.europa.eu/about-dma_en

**Legal text:** https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925

## What is required to receive a good report?

The quality of the report depends on the quality of the evidence. The skills work best when the user provides concrete project material, not only a generic question.

At minimum, provide:

- The system name, repository, business service, and deployment geography
- The regulation or skill you want to use
- The business owner, technical owner, and likely compliance, legal, privacy, security, risk, or product owner
- The architecture context: APIs, databases, queues, jobs, caches, logs, cloud services, SaaS providers, and external integrations
- The data context: personal data, non-personal data, regulated records, generated content, AI outputs, user-generated content, exports, backups, and retention
- The operational context: environments, release process, monitoring, alerts, incident workflow, runbooks, backup and restore evidence, and rollback path
- The evidence context: code paths, configuration files, tests, dashboards, diagrams, policies, tickets, prior reviews, and known open questions
- Redacted samples only; never paste passwords, tokens, private keys, connection strings, session IDs, or raw sensitive payloads

The best prompt is usually specific:

```text
Use @803-regulations-gdpr to review this Java service for GDPR-aware privacy engineering controls.
Scope: customer profile API, EU users, production and staging.
Evidence: DTOs, database schema, logs, retention job, deletion workflow, vendor integration, and privacy owner notes.
Goal: produce an engineering review report with gaps, owner handoffs, release decision, and prioritized actions.
```

The weakest prompt is vague:

```text
Is this compliant?
```

Compliance is a qualified governance decision. These skills help engineering teams prepare the evidence that makes that decision possible.

## Example: ecommerce CI/CD PR model

As a practical example, consider [system-example-cicd-pr-model.md](https://github.com/jabrena/plinth/blob/main/examples/diagrams/deployment/system-example-cicd-pr-model.md).

```text
                           +----------------------+
                           |       Shopper        |
                           | Web browser / mobile |
                           +----------+-----------+
                                      |
                                      | HTTPS
                                      v
  +-----------------------------------------------------------------------+
  | Azure Edge                                                            |
  |                                                                       |
  | +------------------+     +------------------+     +----------------+ |
  | | Azure Front Door | --> | Web Application | --> | AKS Ingress    | |
  | | + CDN            |     | Firewall         |     | Controller     | |
  | +------------------+     +------------------+     +-------+--------+ |
  +-----------------------------------------------------------------------+
                                                              |
                                                              v
  +-----------------------------------------------------------------------+
  | Azure Kubernetes Service - ecommerce-prod, Java Quarkus services       |
  |                                                                       |
  | +----------------+      +----------------+      +-------------------+ |
  | | Web Storefront | ---> | API Gateway    | ---> | Identity Service  | |
  | | Quarkus BFF    |      | Quarkus REST   |      | Quarkus OIDC      | |
  | +-------+--------+      +-------+--------+      +---------+---------+ |
  |         |                       |                         |           |
  |         |                       |                         v           |
  |         |                       |              +-------------------+  |
  |         |                       |              | User Profile DB   |  |
  |         |                       |              | Azure PostgreSQL  |  |
  |         |                       |              +-------------------+  |
  |         |                       |                                      |
  |         |                       +------------------+------------------+
  |         |                                          |
  |         v                                          v
  | +----------------+      +----------------+      +-------------------+ |
  | | Catalog        | ---> | Search Service |      | Cart Service      | |
  | | Quarkus REST   |      | Quarkus FTS    |      | Quarkus REST      | |
  | +-------+--------+      +-------+--------+      +---------+---------+ |
  |         |                       |                         |           |
  |         v                       v                         v           |
  | +----------------+      +----------------+      +-------------------+ |
  | | Product DB     |      | Search Index   |      | Cart DB           | |
  | | PostgreSQL     |      | PostgreSQL FTS |      | PostgreSQL        | |
  | +----------------+      +----------------+      +-------------------+ |
  |                                                                       |
  | +----------------+      +----------------+      +-------------------+ |
  | | Pricing and    | ---> | Checkout       | ---> | Payment Service   | |
  | | Quarkus REST   |      | Quarkus Saga   |      | Quarkus adapter   | |
  | +-------+--------+      +-------+--------+      +---------+---------+ |
  |         |                       |                         |           |
  |         v                       v                         v           |
  | +----------------+      +----------------+      +-------------------+ |
  | | Pricing DB     |      | Order DB       |      | External Payment  | |
  | | PostgreSQL     |      | PostgreSQL     |      | Gateway           | |
  | +----------------+      +-------+--------+      +-------------------+ |
  |                                 |                                     |
  |                                 v                                     |
  |                        +----------------+                             |
  |                        | Saga Support   |                             |
  |                        | PostgreSQL     |                             |
  |                        +-------+--------+                             |
  |                                | publish events                       |
  |                                v                                      |
  | +-------------------------------------------------------------------+ |
  | | Kafka Backbone                                                    | |
  | | Kafka topics                                                      | |
  | | order.created, payment.authorized, stock.reserved, slot.booked    | |
  | +----------------+------------------+----------------+---------------+ |
  |                  |                  |                |                 |
  |                  v                  v                v                 |
  | +----------------+      +----------------+      +-------------------+ |
  | | Inventory      |      | Delivery Slot  |      | Notification      | |
  | | Quarkus REST   |      | Quarkus REST   |      | Quarkus worker    | |
  | +-------+--------+      +-------+--------+      +---------+---------+ |
  |         |                       |                         |           |
  |         v                       v                         v           |
  | +----------------+      +----------------+      +-------------------+ |
  | | Inventory DB   |      | Delivery DB    |      | Email / SMS       | |
  | | PostgreSQL     |      | PostgreSQL     |      | Provider          | |
  | +----------------+      +----------------+      +-------------------+ |
  +-----------------------------------------------------------------------+

  +-----------------------------------------------------------------------+
  | Shared Azure Platform Services                                        |
  |                                                                       |
  | Key Vault | Azure Monitor | Log Analytics | App Insights | Private DNS |
  | Private Link | Managed Identities | Azure Policy | Backup Vault        |
  +-----------------------------------------------------------------------+
```

The model describes a fictional ecommerce platform built with Java and Quarkus services, PostgreSQL databases, Kafka events, Azure runtime services, Artifactory, Docker image publication, PR-based CI/CD automation, security scans, SBOM generation, signed images, GitOps deployment, monitoring, logs, traces, and rollback decisions.

That is the kind of source material a regulation skill needs.

```text
system-example-cicd-pr-model.md
        |
        +--> Delivery pipeline evidence
        |       Software engineer + GenAI tools
        |       Pull request review
        |       CI checks, tests, scans, SBOM
        |       Artifactory, signed images, GitOps deployment
        |
        +--> System or domain evidence
        |       Shopper journey
        |       Java Quarkus services
        |       PostgreSQL, Kafka, logs, traces
        |       Azure Key Vault, Monitor, backup, rollback
        |
        +--> Regulation skill review
                Classify scope
                Collect implementation and operational evidence
                Run the relevant EU regulation skills
                Produce engineering report and owner handoffs
```

For regulations, it is usually not enough to review one isolated class or one controller. You need to analyze the whole system, or at least a bounded domain or subdomain with clear ownership.

In this ecommerce example, a useful review scope could be:

- The whole ecommerce platform, when reviewing operational resilience, cybersecurity, platform risk, or release readiness.
- The checkout domain, when reviewing payments, orders, inventory reservation, delivery slot booking, sagas, data flows, and incident impact.
- The identity and user profile subdomain, when reviewing personal data, consent, account access, logging, and deletion.
- The CI/CD pipeline, when reviewing generated artifacts, SBOMs, vulnerability scans, signed images, deployment approvals, and rollback paths.

The regulation skills work better when the user provides both system evidence and pipeline evidence. The system evidence shows what the software does. The pipeline evidence shows how changes become production behavior.

Using `system-example-cicd-pr-model` as evidence, the most important first questions per regulation could be:

- **EU AI Act:** Does the GenAI-assisted delivery process or any production AI capability generate code, decisions, recommendations, SQL, migrations, runbooks, or tool actions that require human approval and audit evidence?
- **DORA:** If this ecommerce platform supports regulated financial operations or payment flows for a financial entity, can the team prove incident handling, recovery, continuity, resilience testing, and third-party ICT provider control?
- **GDPR:** Which personal data appears in identity, profile, cart, order, payment, delivery, notification, logs, traces, analytics, backups, and support workflows?
- **NIS2:** If the platform supports an essential or important service, can the team prove asset inventory, secure configuration, vulnerability handling, incident escalation, access control, cryptography, and supply-chain security?
- **Cyber Resilience Act:** If parts of the platform are distributed as products with digital elements, can the team prove secure-by-design development, vulnerability disclosure, update delivery, SBOM, signing, and lifecycle support?
- **Data Act:** If connected products, related services, data access APIs, cloud switching, or data-sharing workflows are involved, can the team prove data inventory, entitlement checks, machine-readable exports, audit logs, and owner handoffs?
- **Digital Services Act:** If the ecommerce system becomes a marketplace or online platform, can the team prove notice-and-action, trader traceability, recommender explanations, advertising transparency, complaint handling, and moderation evidence?
- **Digital Markets Act:** If the system belongs to a potential gatekeeper platform or core platform service, can the team prove interoperability, business-user data access, consent and preference evidence, ranking audit signals, and anti-circumvention controls?

These questions do not decide compliance. They identify the highest-value engineering evidence to collect before qualified owners make a decision.

## Takeaways for using the EU regulation skills

The new EU regulation skills are most useful when they are treated as engineering review tools, not as generic compliance prompts.

Key takeaways:

- Start with scope. Decide whether the review covers the whole system, a bounded domain, a subdomain, a product, a platform workflow, a data flow, or a CI/CD pipeline.
- Use one skill when the regulatory concern is clear. Combine skills only when the system actually crosses boundaries.
- Provide evidence, not opinions. The best inputs are architecture diagrams, code paths, DTOs, schemas, logs, runbooks, test results, dependency inventories, SBOMs, dashboards, tickets, ownership notes, and release records.
- Keep owner handoff explicit. The skills should identify legal, compliance, privacy, security, risk, product, platform, procurement, or business owners who must make qualified decisions.
- Ask for an engineering report. The useful output is a structured report with scope, evidence reviewed, risk signals, potential gaps, controls, residual risks, release decision, and prioritized actions.
- Treat "Unknown" as a finding. If the team cannot prove data ownership, recovery evidence, consent history, incident routing, vulnerability handling, or platform scope, the report should say so.
- Never paste secrets or raw sensitive data. Use redacted examples and evidence references.

Common combinations:

- A RAG assistant processing employee data may need `@801-regulations-eu-ai-act` and `@803-regulations-gdpr`.
- A payment platform may need `@802-regulations-dora`, `@803-regulations-gdpr`, and `@804-regulations-eu-nis2`.
- A connected product backend may need `@805-regulations-eu-cyber-resilience-act`, `@806-regulations-eu-data-act`, and `@803-regulations-gdpr`.
- A marketplace with recommender systems may need `@807-regulations-eu-digital-services-act`, `@803-regulations-gdpr`, and possibly `@808-regulations-eu-digital-markets-act`.

The purpose is not to turn developers into lawyers. The purpose is to make engineering evidence visible before production decisions are made.
