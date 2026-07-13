Feature: Validate changes from usage of EU Market Abuse Regulation skill

Background:
  Given the skill "811-regulations-eu-market-abuse-regulation"

@acceptance-test
Scenario: Review a Java market surveillance change with EU Market Abuse Regulation controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/811-regulations-eu-market-abuse-regulation"
  And the requested report output path is "examples/regulations/eu-market-abuse-regulation/MAR-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-market-abuse-regulation" has no git changes
  And the feature request is expected to be developed and released through the described CI/CD pipeline
  And the MAR review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/811-regulations-eu-market-abuse-regulation" is applied to the system description, diagram, and feature request files
  Then the skill reads "references/811-regulations-eu-market-abuse-regulation-chapters-summary.md"
  And the skill reads "references/811-regulations-eu-market-abuse-regulation-engineering-examples.md"
  And the skill reads "assets/questions/811-market-abuse-regulation-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/811-market-abuse-regulation-engineering-review-report-template.md"
  And the skill frames Market Abuse Regulation findings as engineering controls rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes possible financial-instrument signals, trading venue signals, issuer signals, order flows, transaction flows, market-data feeds, disclosure workflows, insider-list workflows, alert models, rules, reviewers, system owners, product owners, data owners, security owners, compliance owners, market-surveillance owners, environments, APIs, data stores, event streams, dashboards, reports, and release paths
  And the skill escalates insider dealing classification, unlawful disclosure classification, market manipulation classification, suspicious order or transaction reportability, disclosure-delay decisions, market-sounding interpretation, accepted market practices, sanctions, jurisdiction, and regulatory interpretation to legal, compliance, market-surveillance, risk, product, operations, data, security, audit, or executive accountability owners
  And the skill reviews Java implementation, configuration, APIs, DTOs, repositories, schemas, migrations, Kafka messages, market-data ingestion, rule engines, ML models, feature flags, thresholds, alert suppression, reviewer decisions, false-positive reasons, investigation records, insider-list workflows, disclosure events, audit logs, metrics, traces, dashboards, alerts, documentation, tests, release records, and compliance reports
  And the skill identifies risk signals for unclear MAR scope, missing suspicious order and transaction monitoring evidence, missing market-data lineage, missing market manipulation scenario coverage, weak alert explainability, missing model or rule provenance, missing reviewer decision trail, missing false-positive rationale, missing investigation records, missing STOR handoff, missing insider-list workflow evidence, missing disclosure workflow evidence, weak access control, incomplete observability, weak change control, incomplete documentation, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential MAR violation or non-compliance signals to article or chapter references with associated official-source links using only the reviewed delivery evidence
  And the skill analyzes the CheckoutService feature request as a pipeline-delivered Java change that modifies order database structure and outbound Kafka event data for potential market-surveillance evidence impact
  And the skill uses Java examples to explain suspicious order and transaction monitoring, market-data lineage, insider-list workflows, disclosure workflows, alert explainability, model and rule provenance, reviewer decisions, false-positive handling, investigation records, and compliance evidence handoff
  And the skill recommends engineering controls for suspicious order and transaction monitoring coverage, market-data lineage, alert explainability, model and rule provenance, reviewer decisions, false-positive handling, investigation records, STOR handoff, insider-list workflows, disclosure workflows, access control, observability, change control, documentation, database migration approval, Kafka schema compatibility, and compliance evidence handoff
  And the skill reports conclusions and actions using the Market Abuse Regulation engineering review report template
  And the generated report contains a potential violation or non-compliance mapping table with reference area and associated official-source link columns
  And the skill overwrites the Market Abuse Regulation engineering review report at "examples/regulations/eu-market-abuse-regulation/MAR-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Review a Java market surveillance change with direct-to-main EU Market Abuse Regulation controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/811-regulations-eu-market-abuse-regulation"
  And the requested report output path is "examples/regulations/eu-market-abuse-regulation/MAR-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-market-abuse-regulation" has no git changes
  And the feature request is expected to be committed directly to main and released through the described CI/CD pipeline
  And the MAR review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/811-regulations-eu-market-abuse-regulation" is applied to the direct-to-main system description, diagram, and feature request files
  Then the skill reads "references/811-regulations-eu-market-abuse-regulation-chapters-summary.md"
  And the skill reads "references/811-regulations-eu-market-abuse-regulation-engineering-examples.md"
  And the skill reads "assets/questions/811-market-abuse-regulation-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/811-market-abuse-regulation-engineering-review-report-template.md"
  And the skill frames Market Abuse Regulation findings as engineering controls rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes possible financial-instrument signals, trading venue signals, issuer signals, order flows, transaction flows, market-data feeds, disclosure workflows, insider-list workflows, alert models, rules, reviewers, system owners, product owners, data owners, security owners, compliance owners, market-surveillance owners, environments, APIs, data stores, event streams, dashboards, reports, and release paths
  And the skill escalates missing pre-merge review, protected-main bypass, insider dealing classification, unlawful disclosure classification, market manipulation classification, suspicious order or transaction reportability, disclosure-delay decisions, market-sounding interpretation, accepted market practices, sanctions, jurisdiction, and regulatory interpretation to legal, compliance, market-surveillance, risk, product, operations, data, security, platform, audit, or executive accountability owners
  And the skill reviews Java implementation, configuration, APIs, DTOs, repositories, schemas, migrations, Kafka messages, market-data ingestion, rule engines, ML models, feature flags, thresholds, alert suppression, reviewer decisions, false-positive reasons, investigation records, insider-list workflows, disclosure events, audit logs, metrics, traces, dashboards, alerts, documentation, tests, release records, and compliance reports
  And the skill identifies risk signals for unclear MAR scope, missing suspicious order and transaction monitoring evidence, missing market-data lineage, missing market manipulation scenario coverage, weak alert explainability, missing model or rule provenance, missing reviewer decision trail, missing false-positive rationale, missing investigation records, missing STOR handoff, missing insider-list workflow evidence, missing disclosure workflow evidence, weak access control, incomplete observability, weak change control, incomplete documentation, direct-to-main commit policy, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential MAR violation or non-compliance signals to article or chapter references with associated official-source links using only the reviewed direct-to-main delivery evidence
  And the skill analyzes the CheckoutService feature request as a direct-to-main Java change that modifies order database structure and outbound Kafka event data for potential market-surveillance evidence impact
  And the skill uses Java examples to explain suspicious order and transaction monitoring, market-data lineage, insider-list workflows, disclosure workflows, alert explainability, model and rule provenance, reviewer decisions, false-positive handling, investigation records, and compliance evidence handoff
  And the skill recommends engineering controls for pre-commit review, main-branch protection, suspicious order and transaction monitoring coverage, market-data lineage, alert explainability, model and rule provenance, reviewer decisions, false-positive handling, investigation records, STOR handoff, insider-list workflows, disclosure workflows, access control, observability, change control, documentation, database migration approval, Kafka schema compatibility, and compliance evidence handoff
  And the skill reports conclusions and actions using the Market Abuse Regulation engineering review report template
  And the generated report contains a potential violation or non-compliance mapping table with reference area and associated official-source link columns
  And the skill overwrites the Market Abuse Regulation engineering review report at "examples/regulations/eu-market-abuse-regulation/MAR-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset
