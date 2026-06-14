Feature: Validate changes from usage of GDPR regulation skill

Background:
  Given the skill "803-regulations-gdpr"

@acceptance-test
Scenario: Review a Java personal-data service with GDPR privacy controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/803-regulations-gdpr"
  And the requested report output path is "examples/regulations/gdpr/GDPR-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/gdpr" has no git changes
  And the feature request is expected to be developed and released through the described CI/CD pipeline
  And the GDPR questionnaire answers are based only on information present in "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/803-regulations-gdpr" is applied to the system description, diagram, and feature request files
  Then the skill reads "references/803-regulations-gdpr.md"
  And the skill reads "assets/questions/803-gdpr-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/803-gdpr-engineering-review-report-template.md"
  And the skill frames GDPR findings as engineering controls rather than legal advice
  And the model answers the GDPR engineering review questionnaire without human intervention before implementation review
  And questionnaire responses do not use facts outside "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes personal-data categories, data subjects, purposes, sources, owners, stores, logs, caches, indexes, exports, backups, processors, controllers, vendors, transfers, retention, and deletion paths
  And the skill classifies the privacy scope as personal-data processing, special-category data, rights-impacting processing, controller or processor relationship, third-country transfer, or no personal data expected
  And the skill escalates lawful basis, controller or processor role, special-category data, jurisdiction, transfer mechanism, DPIA need, and regulatory interpretation to legal, privacy, data protection officer, compliance, security, or risk owners
  And the skill reviews Java implementation, DTOs, controllers, repositories, schemas, migrations, messages, logs, metrics, traces, retention jobs, deletion workflows, access controls, exports, tests, documentation, and vendor integrations
  And the skill identifies risk signals for excessive fields, entity exposure, unclear purpose, missing deletion propagation, payload logging, unbounded retention, weak authorization, unreviewed transfer, missing breach evidence, database migration, Kafka message contract, CI/CD pipeline, production side-effect, and free-text personal-data handling
  And the skill maps potential GDPR violation or non-compliance signals to article or chapter references using only the reviewed delivery evidence
  And the skill analyzes the CheckoutService feature request as a pipeline-delivered change that modifies order database structure, outbound Kafka event data, and delivery instruction personal data
  And the skill uses Java examples to explain personal-data minimization, rights workflows, privacy-safe logging, and field-level authorization controls
  And the skill recommends engineering controls for minimization, field-level authorization, pseudonymization, redaction, retention jobs, deletion propagation, rights request orchestration, transfer evidence, breach evidence, privacy-safe observability, database migration approval, Kafka schema compatibility, and owner escalation
  And the skill reports conclusions and actions using the GDPR engineering review report template
  And the skill overwrites the GDPR engineering review report at "examples/regulations/gdpr/GDPR-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Review a Java personal-data checkout change with direct-to-main GDPR controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/803-regulations-gdpr"
  And the requested report output path is "examples/regulations/gdpr/GDPR-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/gdpr" has no git changes
  And the feature request is expected to be committed directly to main and released through the described CI/CD pipeline
  And the GDPR questionnaire answers are based only on information present in "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/803-regulations-gdpr" is applied to the direct-to-main system description, diagram, and feature request files
  Then the skill reads "references/803-regulations-gdpr.md"
  And the skill reads "assets/questions/803-gdpr-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/803-gdpr-engineering-review-report-template.md"
  And the skill frames GDPR findings as engineering controls rather than legal advice
  And the model answers the GDPR engineering review questionnaire without human intervention before implementation review
  And questionnaire responses do not use facts outside "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes personal-data categories, data subjects, purposes, sources, owners, stores, logs, caches, indexes, exports, backups, processors, controllers, vendors, transfers, retention, and deletion paths
  And the skill classifies the privacy scope as personal-data processing, special-category data, rights-impacting processing, controller or processor relationship, third-country transfer, or no personal data expected
  And the skill escalates missing pre-merge review, protected-main bypass, lawful basis, controller or processor role, special-category data, jurisdiction, transfer mechanism, DPIA need, and regulatory interpretation to legal, privacy, data protection officer, compliance, security, platform, or risk owners
  And the skill reviews Java implementation, DTOs, controllers, repositories, schemas, migrations, messages, logs, metrics, traces, retention jobs, deletion workflows, access controls, exports, tests, documentation, and vendor integrations
  And the skill identifies risk signals for excessive fields, entity exposure, unclear purpose, missing deletion propagation, payload logging, unbounded retention, weak authorization, unreviewed transfer, missing breach evidence, direct-to-main commit policy, database migration, Kafka message contract, CI/CD pipeline, production side-effect, and free-text personal-data handling
  And the skill maps potential GDPR violation or non-compliance signals to article or chapter references using only the reviewed direct-to-main delivery evidence
  And the skill analyzes the CheckoutService feature request as a direct-to-main pipeline-delivered change that modifies order database structure, outbound Kafka event data, and delivery instruction personal data
  And the skill uses Java examples to explain personal-data minimization, rights workflows, privacy-safe logging, and field-level authorization controls
  And the skill recommends engineering controls for pre-commit review, main-branch protection, minimization, field-level authorization, pseudonymization, redaction, retention jobs, deletion propagation, rights request orchestration, transfer evidence, breach evidence, privacy-safe observability, database migration approval, Kafka schema compatibility, and owner escalation
  And the skill reports conclusions and actions using the GDPR engineering review report template
  And the skill overwrites the GDPR engineering review report at "examples/regulations/gdpr/GDPR-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset
