Feature: Validate changes from usage of GDPR regulation skill

Background:
  Given the skill "803-regulations-gdpr"

@acceptance-test
Scenario: Review a Java personal-data service with GDPR privacy controls
  Given the system description file "examples/diagrams/deployment/system-example.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the local generated skill path ".agents/skills/803-regulations-gdpr"
  And the requested report output path is "examples/regulations/gdpr/GDPR-ENGINEERING-REVIEW-REPORT.md"
  And the GDPR questionnaire answers are based only on information present in "examples/diagrams/deployment/system-example.md"
  When the skill ".agents/skills/803-regulations-gdpr" is applied to the system description and diagram files
  Then the skill reads "references/803-regulations-gdpr.md"
  And the skill reads "assets/questions/803-gdpr-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/803-gdpr-engineering-review-report-template.md"
  And the skill frames GDPR findings as engineering controls rather than legal advice
  And the skill asks the GDPR engineering review questionnaire one question at a time before implementation review
  And questionnaire responses do not use facts outside "examples/diagrams/deployment/system-example.md"
  And the skill scopes personal-data categories, data subjects, purposes, sources, owners, stores, logs, caches, indexes, exports, backups, processors, controllers, vendors, transfers, retention, and deletion paths
  And the skill classifies the privacy scope as personal-data processing, special-category data, rights-impacting processing, controller or processor relationship, third-country transfer, or no personal data expected
  And the skill escalates lawful basis, controller or processor role, special-category data, jurisdiction, transfer mechanism, DPIA need, and regulatory interpretation to legal, privacy, data protection officer, compliance, security, or risk owners
  And the skill reviews Java implementation, DTOs, controllers, repositories, schemas, migrations, messages, logs, metrics, traces, retention jobs, deletion workflows, access controls, exports, tests, documentation, and vendor integrations
  And the skill identifies risk signals for excessive fields, entity exposure, unclear purpose, missing deletion propagation, payload logging, unbounded retention, weak authorization, unreviewed transfer, and missing breach evidence
  And the skill uses Java examples to explain personal-data minimization, rights workflows, privacy-safe logging, and field-level authorization controls
  And the skill recommends engineering controls for minimization, field-level authorization, pseudonymization, redaction, retention jobs, deletion propagation, rights request orchestration, transfer evidence, breach evidence, privacy-safe observability, and owner escalation
  And the skill reports conclusions and actions using the GDPR engineering review report template
  And the skill writes the GDPR engineering review report to "examples/regulations/gdpr/GDPR-ENGINEERING-REVIEW-REPORT.md"
