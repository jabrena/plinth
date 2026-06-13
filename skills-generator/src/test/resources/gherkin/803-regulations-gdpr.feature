Feature: Validate changes from usage of GDPR regulation skill

Background:
  Given the skill "803-regulations-gdpr"

@acceptance-test
Scenario: Review a Java personal-data service with GDPR privacy controls
  Given a Java enterprise system processes personal data in APIs, databases, logs, caches, exports, or message flows
  And the system may require rights workflows, retention, deletion, transfer review, DPIA escalation, or breach evidence
  When the skill "803-regulations-gdpr" is applied
  Then the skill reads "references/803-regulations-gdpr.md"
  And the skill reads "assets/questions/803-gdpr-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/803-gdpr-engineering-review-report-template.md"
  And the skill uses "801-regulations-eu-ai-act" as the format and structure baseline for generated skill organization
  And the skill frames GDPR findings as engineering controls rather than legal advice
  And the skill asks the GDPR engineering review questionnaire one question at a time before implementation review
  And the skill does not infer or self-answer questionnaire responses from repository inspection
  And the skill scopes personal-data categories, data subjects, purposes, sources, owners, stores, logs, caches, indexes, exports, backups, processors, controllers, vendors, transfers, retention, and deletion paths
  And the skill escalates lawful basis, controller or processor role, special-category data, jurisdiction, transfer mechanism, DPIA need, and regulatory interpretation to legal, privacy, data protection officer, compliance, security, or risk owners
  And the skill reviews Java implementation, DTOs, controllers, repositories, schemas, migrations, messages, logs, metrics, traces, retention jobs, deletion workflows, access controls, exports, tests, documentation, and vendor integrations
  And the skill identifies risk signals for excessive fields, entity exposure, unclear purpose, missing deletion propagation, payload logging, unbounded retention, weak authorization, unreviewed transfer, and missing breach evidence
  And the skill uses Java examples to explain personal-data minimization, rights workflows, privacy-safe logging, and field-level authorization controls
  And the skill recommends engineering controls for minimization, field-level authorization, pseudonymization, redaction, retention jobs, deletion propagation, rights request orchestration, transfer evidence, breach evidence, privacy-safe observability, and owner escalation
  And the skill reports conclusions and actions using the GDPR engineering review report template
