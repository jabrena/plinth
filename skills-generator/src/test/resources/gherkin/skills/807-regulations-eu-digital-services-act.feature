Feature: Validate changes from usage of EU Digital Services Act regulation skill

Background:
  Given the skill "807-regulations-eu-digital-services-act"

@acceptance-test
Scenario: Review a Java ecommerce platform change with Digital Services Act controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/807-regulations-eu-digital-services-act"
  And the requested report output path is "examples/regulations/eu-digital-services-act/DSA-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-digital-services-act" has no git changes
  And the feature request is expected to be developed and released through the described CI/CD pipeline
  And the Digital Services Act review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/807-regulations-eu-digital-services-act" is applied to the system description, diagram, and feature request files
  Then the skill reads "references/807-regulations-eu-digital-services-act-chapters-summary.md"
  And the skill reads "references/807-regulations-eu-digital-services-act-engineering-examples.md"
  And the skill reads "assets/reports/807-eu-digital-services-act-engineering-review-report-template.md"
  And the skill frames Digital Services Act findings as engineering controls rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes intermediary-service signals, hosting signals, online-platform or marketplace signals, online-search-engine signals, recommender or ranking signals, advertising workflows, trader interactions, user content flows, platform owners, trust-and-safety owners, privacy owners, security owners, risk owners, environments, data stores, observability systems, and VLOP or VLOSE indicators
  And the skill escalates intermediary classification, platform classification, VLOP or VLOSE status, illegal-content determinations, advertising or recommender interpretation, audit or researcher access duties, systemic-risk conclusions, and regulatory interpretation to legal, compliance, trust-and-safety, privacy, security, product, risk, audit, research-access, or executive accountability owners
  And the skill reviews Java implementation, DTOs, controllers, moderation or policy services if present, workflow state, schemas, migrations, messages, logs, metrics, traces, search or ranking behavior, recommender configuration, advertising metadata, user-control settings, complaint and appeal records, transparency reporting jobs, tests, deployment workflows, and provider documentation
  And the skill identifies risk signals for unclear DSA scope, missing owner classification evidence, missing content decision audit logs where moderation exists, missing notice intake and response tracking where hosting exists, missing complaint and appeal workflow evidence where online platform duties apply, missing recommender and ranking explanation evidence, missing ad transparency metadata where ads exist, missing user controls, missing trader traceability where marketplace duties apply, missing risk assessment evidence where VLOP or VLOSE duties may apply, missing incident escalation, missing auditor or researcher data-access controls where applicable, privacy-safe observability gaps, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential Digital Services Act violation or non-compliance signals to article or chapter references with associated official-source links using only the reviewed delivery evidence
  And the skill analyzes the CheckoutService feature request as a pipeline-delivered change that modifies order database structure and outbound Kafka event data for a customer-facing ecommerce platform
  And the skill uses Java examples to explain content decision audit logging, notice tracking, recommender explanation evidence, ad transparency metadata, complaint and appeal workflow state, risk assessment evidence, auditor or researcher data access, incident escalation, and privacy-safe observability controls
  And the skill recommends engineering controls for DSA scope inventory, owner classification evidence, content decision audit logs, moderation workflow state, notice intake and response tracking, statement-of-reasons records, recommender and ranking explanation evidence, ad transparency metadata, user controls, complaint and appeal workflows, trader traceability, risk assessment evidence, incident escalation, auditor or researcher data access where applicable, privacy-safe observability, database migration approval, Kafka schema compatibility, and owner escalation
  And the skill reports conclusions and actions using the Digital Services Act engineering review report template
  And the skill overwrites the Digital Services Act engineering review report at "examples/regulations/eu-digital-services-act/DSA-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Review a Java ecommerce platform change with direct-to-main Digital Services Act controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/807-regulations-eu-digital-services-act"
  And the requested report output path is "examples/regulations/eu-digital-services-act/DSA-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-digital-services-act" has no git changes
  And the feature request is expected to be committed directly to main and released through the described CI/CD pipeline
  And the Digital Services Act review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/807-regulations-eu-digital-services-act" is applied to the direct-to-main system description, diagram, and feature request files
  Then the skill reads "references/807-regulations-eu-digital-services-act-chapters-summary.md"
  And the skill reads "references/807-regulations-eu-digital-services-act-engineering-examples.md"
  And the skill reads "assets/reports/807-eu-digital-services-act-engineering-review-report-template.md"
  And the skill frames Digital Services Act findings as engineering controls rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes intermediary-service signals, hosting signals, online-platform or marketplace signals, online-search-engine signals, recommender or ranking signals, advertising workflows, trader interactions, user content flows, platform owners, trust-and-safety owners, privacy owners, security owners, risk owners, environments, data stores, observability systems, and VLOP or VLOSE indicators
  And the skill escalates missing pre-merge review, protected-main bypass, intermediary classification, platform classification, VLOP or VLOSE status, illegal-content determinations, advertising or recommender interpretation, audit or researcher access duties, systemic-risk conclusions, and regulatory interpretation to legal, compliance, trust-and-safety, privacy, security, platform, product, risk, audit, research-access, or executive accountability owners
  And the skill reviews Java implementation, DTOs, controllers, moderation or policy services if present, workflow state, schemas, migrations, messages, logs, metrics, traces, search or ranking behavior, recommender configuration, advertising metadata, user-control settings, complaint and appeal records, transparency reporting jobs, tests, deployment workflows, and provider documentation
  And the skill identifies risk signals for unclear DSA scope, missing owner classification evidence, missing content decision audit logs where moderation exists, missing notice intake and response tracking where hosting exists, missing complaint and appeal workflow evidence where online platform duties apply, missing recommender and ranking explanation evidence, missing ad transparency metadata where ads exist, missing user controls, missing trader traceability where marketplace duties apply, missing risk assessment evidence where VLOP or VLOSE duties may apply, missing incident escalation, missing auditor or researcher data-access controls where applicable, privacy-safe observability gaps, direct-to-main commit policy, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential Digital Services Act violation or non-compliance signals to article or chapter references with associated official-source links using only the reviewed direct-to-main delivery evidence
  And the skill analyzes the CheckoutService feature request as a direct-to-main pipeline-delivered change that modifies order database structure and outbound Kafka event data for a customer-facing ecommerce platform
  And the skill uses Java examples to explain content decision audit logging, notice tracking, recommender explanation evidence, ad transparency metadata, complaint and appeal workflow state, risk assessment evidence, auditor or researcher data access, incident escalation, and privacy-safe observability controls
  And the skill recommends engineering controls for pre-commit review, main-branch protection, DSA scope inventory, owner classification evidence, content decision audit logs, moderation workflow state, notice intake and response tracking, statement-of-reasons records, recommender and ranking explanation evidence, ad transparency metadata, user controls, complaint and appeal workflows, trader traceability, risk assessment evidence, incident escalation, auditor or researcher data access where applicable, privacy-safe observability, database migration approval, Kafka schema compatibility, and owner escalation
  And the skill reports conclusions and actions using the Digital Services Act engineering review report template
  And the skill overwrites the Digital Services Act engineering review report at "examples/regulations/eu-digital-services-act/DSA-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset
