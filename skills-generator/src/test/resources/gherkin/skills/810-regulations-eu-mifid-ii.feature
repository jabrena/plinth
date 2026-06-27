Feature: Validate changes from usage of MiFID II regulation skill

Background:
  Given the skill "810-regulations-eu-mifid-ii"

@acceptance-test
Scenario: Review a Java investment-service change with MiFID II controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/810-regulations-eu-mifid-ii"
  And the requested report output path is "examples/regulations/eu-mifid-ii/MIFID-II-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-mifid-ii" has no git changes
  And the feature request is expected to be developed and released through the described CI/CD pipeline
  And the MiFID II questionnaire answers are based only on information present in "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/810-regulations-eu-mifid-ii" is applied to the system description, diagram, and feature request files
  Then the skill reads "references/810-regulations-eu-mifid-ii-chapters-summary.md"
  And the skill reads "references/810-regulations-eu-mifid-ii-engineering-examples.md"
  And the skill reads "assets/questions/810-mifid-ii-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/810-mifid-ii-engineering-review-report-template.md"
  And the skill frames MiFID II findings as engineering controls rather than legal advice
  And the model answers the MiFID II engineering review questionnaire without human intervention before implementation review
  And questionnaire responses do not use facts outside "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes possible investment-service signals, investment-activity signals, financial instruments, client categories, order-evidence flows, advisory workflows, portfolio-management workflows, trading-venue evidence, market data flows, system owners, product owners, compliance owners, risk owners, operations owners, trading owners, security owners, environments, APIs, data stores, event streams, algorithmic-trading governance controls, reporting paths, monitoring paths, and release paths
  And the skill escalates regulated-service classification, investment-firm status, jurisdiction, client-impact decisions, advice classification, best-execution interpretation, algorithmic trading duties, transaction reporting duties, and regulatory interpretation to legal, compliance, risk, product, operations, trading, market-structure, data-protection, security, or accountable business owners
  And the skill reviews Java implementation, configuration, APIs, DTOs, repositories, schemas, migrations, trading-protocol adapters, Kafka messages, event contracts, order-state evidence, suitability and appropriateness logic, client classification records, product catalogs, algorithm inventory, feature flags, release approvals, audit logs, metrics, traces, dashboards, alerts, retention jobs, transaction records, incident procedures, complaints evidence, documentation, tests, and compliance reports
  And the skill identifies risk signals for unclear investment-service scope, unclear client category, missing client classification evidence, missing suitability or appropriateness evidence, missing product-governance evidence, missing order-handling evidence, missing best-execution evidence, missing client-instruction evidence, missing algorithmic trading controls, missing clock-synchronisation evidence, missing immutable record keeping, incomplete monitoring, weak access control, incomplete documentation, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential MiFID II violation or non-compliance signals to title, article, annex, or topic references with associated official-source links using only the reviewed delivery evidence
  And the skill analyzes the CheckoutService feature request as a pipeline-delivered change that modifies order database structure and outbound Kafka event data
  And the skill uses Java examples to explain investment-service scope inventory, client classification, suitability and appropriateness workflows, order handling, best-execution evidence, algorithmic trading release gates, clock synchronisation, record keeping, and compliance evidence handoff
  And the skill recommends engineering controls for investment-service scope inventory, client classification, suitability and appropriateness evidence, product governance, order lifecycle evidence audit, best-execution metrics, algorithm inventory, pre-trade-control evidence, emergency-stop evidence, timestamp precision, record retention, evidence-safe logging, access control, observability, change control, documentation, database migration approval, Kafka schema compatibility, and compliance evidence handoff
  And the skill reports conclusions and actions using the MiFID II engineering review report template
  And the generated report contains a potential violation or non-compliance mapping table with associated official-source links
  And the skill overwrites the MiFID II engineering review report at "examples/regulations/eu-mifid-ii/MIFID-II-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Review a Java investment-service change with direct-to-main MiFID II controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/810-regulations-eu-mifid-ii"
  And the requested report output path is "examples/regulations/eu-mifid-ii/MIFID-II-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-mifid-ii" has no git changes
  And the feature request is expected to be committed directly to main and released through the described CI/CD pipeline
  And the MiFID II questionnaire answers are based only on information present in "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/810-regulations-eu-mifid-ii" is applied to the direct-to-main system description, diagram, and feature request files
  Then the skill reads "references/810-regulations-eu-mifid-ii-chapters-summary.md"
  And the skill reads "references/810-regulations-eu-mifid-ii-engineering-examples.md"
  And the skill reads "assets/questions/810-mifid-ii-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/810-mifid-ii-engineering-review-report-template.md"
  And the skill frames MiFID II findings as engineering controls rather than legal advice
  And the model answers the MiFID II engineering review questionnaire without human intervention before implementation review
  And questionnaire responses do not use facts outside "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes possible investment-service signals, investment-activity signals, financial instruments, client categories, order-evidence flows, advisory workflows, portfolio-management workflows, trading-venue evidence, market data flows, system owners, product owners, compliance owners, risk owners, operations owners, trading owners, security owners, environments, APIs, data stores, event streams, algorithmic-trading governance controls, reporting paths, monitoring paths, and release paths
  And the skill escalates missing pre-merge review, protected-main bypass, regulated-service classification, investment-firm status, jurisdiction, client-impact decisions, advice classification, best-execution interpretation, algorithmic trading duties, transaction reporting duties, and regulatory interpretation to legal, compliance, risk, product, operations, trading, market-structure, data-protection, security, platform, or accountable business owners
  And the skill reviews Java implementation, configuration, APIs, DTOs, repositories, schemas, migrations, trading-protocol adapters, Kafka messages, event contracts, order-state evidence, suitability and appropriateness logic, client classification records, product catalogs, algorithm inventory, feature flags, release approvals, audit logs, metrics, traces, dashboards, alerts, retention jobs, transaction records, incident procedures, complaints evidence, documentation, tests, and compliance reports
  And the skill identifies risk signals for unclear investment-service scope, unclear client category, missing client classification evidence, missing suitability or appropriateness evidence, missing product-governance evidence, missing order-handling evidence, missing best-execution evidence, missing client-instruction evidence, missing algorithmic trading controls, missing clock-synchronisation evidence, missing immutable record keeping, incomplete monitoring, weak access control, incomplete documentation, direct-to-main commit policy, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential MiFID II violation or non-compliance signals to title, article, annex, or topic references with associated official-source links using only the reviewed direct-to-main delivery evidence
  And the skill analyzes the CheckoutService feature request as a direct-to-main change that modifies order database structure and outbound Kafka event data
  And the skill uses Java examples to explain investment-service scope inventory, client classification, suitability and appropriateness workflows, order handling, best-execution evidence, algorithmic trading release gates, clock synchronisation, record keeping, and compliance evidence handoff
  And the skill recommends engineering controls for pre-commit review, main-branch protection, investment-service scope inventory, client classification, suitability and appropriateness evidence, product governance, order lifecycle evidence audit, best-execution metrics, algorithm inventory, pre-trade-control evidence, emergency-stop evidence, timestamp precision, record retention, evidence-safe logging, access control, observability, change control, documentation, database migration approval, Kafka schema compatibility, and compliance evidence handoff
  And the skill reports conclusions and actions using the MiFID II engineering review report template
  And the generated report contains a potential violation or non-compliance mapping table with associated official-source links
  And the skill overwrites the MiFID II engineering review report at "examples/regulations/eu-mifid-ii/MIFID-II-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset
