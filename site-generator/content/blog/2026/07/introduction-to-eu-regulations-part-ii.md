title=Introduction to EU regulations Part II
date=2026-07-03
type=post
tags=blog,skills,java,regulations,eu,compliance
author=MyRobot
status=published
~~~~~~

## Compliance as code

The [first article](introduction-to-eu-regulations-part-i.html) introduced the EU regulatory skill family and focused on broad digital regulation: AI systems, operational resilience, privacy, cybersecurity, product security, data access, online platforms, and digital markets.

This second article explains the remaining regulation and ISO skills added for the future `0.17.0` release, and connects them to a more specialized idea: compliance as code for AI-assisted development.

In enterprise software, regulatory work often arrives late. A feature is implemented, reviewed, documented, and only then mapped to obligations, policies, evidence, and residual risks. That late review is expensive because the engineering decisions have already hardened.

AI-assisted development changes the timing. If an agent can help create code, tests, OpenSpec changes, runbooks, release notes, and architectural evidence, then it can also help ask regulatory questions while the change is still small enough to steer.

That is the purpose of regulation engineering in this project. **The skills do not provide legal advice and do not replace qualified legal, compliance, security, risk, product, or audit owners. They increase engineering awareness by helping teams collect better evidence earlier, identify owner handoffs sooner, and make release decisions easier to review.**

## The skills added in release 0.17.0

The second group of regulation skills includes:

- `@810-regulations-eu-mifid-ii`
- `@811-regulations-eu-market-abuse-regulation`
- `@812-regulations-eu-product-liability-directive`
- `@813-regulations-iso-42001`

These skills are especially relevant for fintech, regulated enterprise platforms, AI-enabled products, and organizations that need repeatable governance for AI systems.

The common pattern is:

1. Classify whether the system, workflow, product, or AI capability is in scope.
2. Ask for concrete engineering evidence.
3. Map regulatory or standard concerns to controls, logs, tests, approvals, and documentation.
4. Produce a report for qualified owner review.

## Why AI agents should understand regulation

AI agents are becoming part of the development workflow. They read issues, draft OpenSpec changes, edit Java code, generate tests, explain pull requests, and prepare release notes. If they ignore regulation, they can accidentally optimize only for local implementation speed.

For regulated systems, that is not enough.

An AI agent working on a trading workflow should know that order handling, transaction reporting, client categorization, suitability evidence, market data, best execution, and audit trails may matter. An agent touching issuer disclosure, surveillance, alerts, trading restrictions, or suspicious order flows should recognize Market Abuse Regulation concerns. An agent modifying an AI-enabled product should ask whether product safety evidence, defect handling, update delivery, documentation, or post-market monitoring are affected.

Regulation-aware agents do not make legal conclusions. They improve the engineering conversation before the legal conclusion is needed.

## Shift-left compliance

Shift-left compliance means moving compliance evidence earlier in the delivery process.

For Java teams, that can mean:

- Writing OpenSpec requirements that name regulatory assumptions
- Adding Gherkin scenarios for regulated workflows
- Preserving audit-relevant events in tests
- Capturing data lineage, decision inputs, and approval gates
- Treating logs, retention, access control, and monitoring as reviewable evidence
- Recording residual risks and owner handoffs before release

This is where compliance as code becomes practical. A regulation skill can review the same artifacts the team already maintains: APIs, DTOs, schemas, migrations, event contracts, policies, runbooks, CI evidence, dependency metadata, and generated documentation.

The result is not a magical certificate. It is a better evidence trail.

## MiFID II

`MiFID II` is part of the EU framework for investment services and regulated markets. The European Commission explains that MiFID II and MiFIR revised the previous MiFID framework, with MiFID II reinforcing rules on securities markets.

Use `@810-regulations-eu-mifid-ii` when a Java system supports investment services, trading venues, order handling, execution workflows, client onboarding, client categorization, suitability or appropriateness checks, transaction reporting, market data, investment advice, portfolio management, or record-keeping for regulated financial services.

The main benefit is traceability. The skill helps engineers connect financial-service behavior to technical evidence that can be reviewed by compliance, legal, risk, product, or operations owners.

The skill helps produce evidence for:

- Client, product, instrument, venue, and service scope
- Order lifecycle and execution evidence
- Suitability, appropriateness, disclosure, consent, and client communication records
- Transaction reporting and record retention
- Market data, transparency, and timestamping concerns
- Algorithmic trading, kill-switch, testing, monitoring, and governance evidence where applicable
- Release gates, residual risks, and qualified owner handoff

To receive a good report, provide service scope, regulated entity context, client journeys, order workflows, execution logic, instrument metadata, transaction reporting flows, audit logs, retention rules, monitoring dashboards, incident workflow, and compliance owner.

**Official reference:** https://finance.ec.europa.eu/financial-markets/financial-markets-policy/securities-markets/investment-services-and-regulated-markets_en

**Implementing and delegated acts:** https://finance.ec.europa.eu/regulation-and-supervision/financial-services-legislation/implementing-and-delegated-acts/markets-financial-instruments-directive-ii_en

## Market Abuse Regulation

The `Market Abuse Regulation`, usually shortened to `MAR`, focuses on market integrity. The European Commission provides implementing and delegated acts for Regulation (EU) No 596/2014, and the legal text covers concerns such as inside information and market manipulation.

Use `@811-regulations-eu-market-abuse-regulation` when a Java system supports issuer disclosures, insider lists, market soundings, trading surveillance, order monitoring, suspicious transaction and order reporting, employee dealing controls, restricted lists, watchlists, price-sensitive information workflows, or market integrity alerts.

The main benefit is abuse-signal discipline. The skill helps a team separate ordinary operational logs from evidence that may matter for inside information, unlawful disclosure, insider dealing, market manipulation, alert triage, and escalation.

The skill helps produce evidence for:

- Inside-information handling and disclosure workflow
- Insider lists, restricted lists, watchlists, and access control
- Market sounding records and approval gates
- Surveillance alerts, alert triage, escalation, and closure rationale
- Suspicious transaction and order reporting evidence
- Employee dealing, conflicts, and communications review handoff
- Immutable audit trails, timestamping, retention, and investigation readiness

To receive a good report, provide market context, instrument scope, issuer or trading role, information-flow diagrams, access-control model, surveillance rules, alert samples, case-management workflow, audit logs, retention rules, escalation process, and market compliance owner.

**Official reference:** https://finance.ec.europa.eu/financial-markets/financial-markets-policy/securities-markets/markets-integrity-benchmarks-and-market-abuse_en

**Implementing and delegated acts:** https://finance.ec.europa.eu/regulation-and-supervision/financial-services-legislation/implementing-and-delegated-acts/market-abuse-regulation_en

**Legal text:** https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng

## Product Liability Directive

The new `Product Liability Directive` updates the EU liability framework for defective products. The official text is Directive (EU) 2024/2853 of 23 October 2024 on liability for defective products, replacing the earlier product liability regime.

Use `@812-regulations-eu-product-liability-directive` when a Java product, AI-enabled product, software component, embedded service, cloud-connected product, model-driven feature, update mechanism, or digital product evidence may be relevant to product defect, safety, documentation, maintenance, or post-release monitoring concerns.

The main benefit is product evidence readiness. The skill pushes the team to treat software behavior, AI outputs, updates, warnings, lifecycle support, and incident records as product evidence rather than only implementation details.

The skill helps produce evidence for:

- Product, component, software, AI, and service scope
- Intended use, foreseeable misuse, and user-facing warnings
- Design, testing, release, update, rollback, and monitoring evidence
- Defect reports, incident handling, field signals, and corrective actions
- Dependency, SBOM, build, signing, and provenance records
- Documentation, support period, maintenance, and end-of-life evidence
- Qualified owner handoff for liability, product, safety, legal, and compliance review

To receive a good report, provide product description, distribution model, user profile, intended use, architecture, update mechanism, release process, QA evidence, incident records, support policy, dependency metadata, documentation, and product owner.

**Legal text:** https://eur-lex.europa.eu/eli/dir/2024/2853/oj/eng

## ISO 42001

`ISO/IEC 42001` is an AI management system standard. ISO describes it as the first AI management system standard and explains that it gives organizations a structured way to manage AI-related risks and opportunities.

Use `@813-regulations-iso-42001` when an organization wants to govern AI systems, AI-assisted development, AI products, model use, RAG systems, agentic workflows, third-party AI services, AI risk management, lifecycle controls, or evidence for an AI management system.

The main benefit is management-system thinking. Instead of reviewing a single prompt, model, or feature in isolation, the skill asks whether the organization has repeatable policies, roles, risk assessment, lifecycle controls, monitoring, improvement loops, and evidence.

The skill helps produce evidence for:

- AI system inventory and lifecycle scope
- Roles, responsibilities, policies, and governance cadence
- Risk assessment and treatment records
- Data, model, prompt, retrieval, tool, and output controls
- Human oversight, approval gates, and accountability evidence
- Monitoring, incidents, corrective actions, and continual improvement
- Supplier, third-party AI service, and integration evidence

To receive a good report, provide AI inventory, AI policy, governance model, risk methodology, system descriptions, model/provider details, data sources, prompt and tool controls, monitoring, incident records, audit evidence, supplier records, and AI governance owner.

**Official reference:** https://www.iso.org/standard/42001

**ISO overview:** https://www.iso.org/artificial-intelligence/ai-management-systems

## Generating compliance evidence during development

The practical question is not "can an AI agent certify this system?"

It cannot.

The practical question is "can an AI agent help the team generate better review material while the work is happening?"

It can.

For example, during an OpenSpec workflow, an agent can:

- Add regulatory assumptions to the proposal
- Turn regulated behavior into Given/When/Then scenarios
- Ask for missing evidence before implementation
- Flag audit-relevant logs, retention rules, and approval gates
- Keep risky changes behind explicit release tasks
- Generate an owner handoff section in the final report

During implementation, an agent can:

- Add tests that protect evidence-producing behavior
- Preserve event schemas and audit fields during refactoring
- Check that monitoring, alerting, and runbooks are updated with code changes
- Document residual risks in the pull request
- Avoid presenting compliance conclusions as engineering facts

This is compliance as code in the useful sense: not reducing regulation to a checklist, but making regulatory evidence part of normal engineering artifacts.

## The future of compliance agents

Very few open-source AI projects tackle regulatory engineering systematically. That is the opportunity.

The next generation of compliance agents will probably not be single-purpose bots that answer legal questions. They will be workflow participants that help with:

- Scope classification
- Evidence collection
- Policy-to-control mapping
- Traceability from requirement to test to release
- Audit-ready summaries
- Residual-risk handoff
- Continuous monitoring of compliance-relevant drift

For fintech and enterprise teams, this is where AI assistance becomes more than code generation. It becomes an engineering partner that understands the environment around the code: regulation, risk, operations, product governance, and evidence.

That does not make the agent the decision-maker. It makes the agent a better preparer.

The human owners still decide. The system gets better evidence.

## Regulation and ISO support summary

Release `0.17.0` extends the regulation skill family with support for financial-market engineering, market-integrity engineering, product-liability evidence, and AI management-system governance. The full regulation and standard support now looks like this:

```text
+---------+-------------------------------------+
| Skill   | Regulation or standard              |
+---------+-------------------------------------+
| @801    | EU AI Act                           |
| @802    | DORA                                |
| @803    | GDPR                                |
| @804    | NIS2                                |
| @805    | Cyber Resilience Act                |
| @806    | Data Act                            |
| @807    | Digital Services Act                |
| @808    | Digital Markets Act                 |
| @810    | MiFID II                            |
| @811    | Market Abuse Regulation             |
| @812    | Product Liability Directive         |
| @813    | ISO/IEC 42001                       |
+---------+-------------------------------------+
```

The common theme is evidence. Each skill helps Java teams turn regulatory or standard concerns into reviewable architecture notes, OpenSpec assumptions, tests, logs, runbooks, release gates, and owner handoffs.

## Share your experience

If you are using these workflows, experimenting with OpenSpec, or trying to introduce regulation skills into AI-assisted delivery, share your experience with the project.

Use [GitHub Discussions](https://github.com/jabrena/plinth/discussions) to post what is working, where the workflow feels unclear, which doubts you have, and what worries you about applying agents to real Java systems.

Those conversations help improve the skills, the documentation, and the project workflow itself.
