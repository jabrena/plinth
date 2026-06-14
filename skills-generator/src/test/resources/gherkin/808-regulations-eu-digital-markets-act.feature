Feature: Validate changes from usage of EU Digital Markets Act regulation skill

Background:
  Given the skill "808-regulations-eu-digital-markets-act"

@acceptance-test
Scenario: Review a Java platform change with EU Digital Markets Act controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/808-regulations-eu-digital-markets-act"
  And the requested report output path is "examples/regulations/eu-digital-markets-act/DMA-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-digital-markets-act" has no git changes
  And the feature request is expected to be developed and released through the described CI/CD pipeline
  And the DMA review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/808-regulations-eu-digital-markets-act" is applied to the system description, diagram, and feature request files
  Then the skill reads "references/808-regulations-eu-digital-markets-act-chapters-summary.md"
  And the skill reads "references/808-regulations-eu-digital-markets-act-engineering-examples.md"
  And the skill reads "assets/reports/808-eu-digital-markets-act-engineering-review-report-template.md"
  And the skill frames Digital Markets Act findings as engineering controls rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes possible core platform service signals, gatekeeper-scope signals, business-user journeys, end-user journeys, system owners, platform owners, product owners, privacy owners, security owners, compliance owners, environments, APIs, data stores, event streams, ranking systems, advertising systems, consent flows, access policies, interoperability interfaces, export workflows, and release paths
  And the skill escalates gatekeeper designation, core platform service scope, obligation applicability, consent interpretation, self-preferencing assessments, fair access terms, suspension or exemption questions, and regulatory interpretation to legal, compliance, platform governance, product, privacy, security, risk, competition, or executive accountability owners
  And the skill reviews Java implementation, configuration, APIs, DTOs, repositories, schemas, migrations, messages, ranking logic, feature flags, experiments, consent and preference records, audit logs, metrics, traces, dashboards, alerts, export jobs, business-user portals, documentation, tests, release records, and compliance reports
  And the skill identifies risk signals for unclear core platform service scope, missing interoperability evidence, missing business-user data access APIs, missing consent and preference evidence, missing ranking and self-preferencing audit signals, missing business-user export workflows, weak anti-circumvention guardrails, weak access control, incomplete observability, weak change control, incomplete documentation, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential DMA violation or non-compliance signals to article or chapter references with associated official-source links using only the reviewed delivery evidence
  And the skill analyzes the CheckoutService feature request as a pipeline-delivered platform change that modifies order database structure and outbound Kafka event data
  And the skill uses Java examples to explain interoperability interfaces, business-user data access, consent and preference evidence, ranking audit signals, business-user export workflows, anti-circumvention release gates, and compliance evidence handoff
  And the skill recommends engineering controls for interoperability interfaces, data access APIs, consent and preference evidence, ranking and self-preferencing audit signals, business-user export workflows, anti-circumvention guardrails, access control, observability, change control, documentation, database migration approval, Kafka schema compatibility, and compliance evidence handoff
  And the skill reports conclusions and actions using the EU Digital Markets Act engineering review report template
  And the generated report contains a potential violation or non-compliance mapping table with associated official-source links
  And the skill overwrites the EU Digital Markets Act engineering review report at "examples/regulations/eu-digital-markets-act/DMA-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Review a Java platform change with direct-to-main EU Digital Markets Act controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/808-regulations-eu-digital-markets-act"
  And the requested report output path is "examples/regulations/eu-digital-markets-act/DMA-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-digital-markets-act" has no git changes
  And the feature request is expected to be committed directly to main and released through the described CI/CD pipeline
  And the DMA review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/808-regulations-eu-digital-markets-act" is applied to the direct-to-main system description, diagram, and feature request files
  Then the skill reads "references/808-regulations-eu-digital-markets-act-chapters-summary.md"
  And the skill reads "references/808-regulations-eu-digital-markets-act-engineering-examples.md"
  And the skill reads "assets/reports/808-eu-digital-markets-act-engineering-review-report-template.md"
  And the skill frames Digital Markets Act findings as engineering controls rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes possible core platform service signals, gatekeeper-scope signals, business-user journeys, end-user journeys, system owners, platform owners, product owners, privacy owners, security owners, compliance owners, environments, APIs, data stores, event streams, ranking systems, advertising systems, consent flows, access policies, interoperability interfaces, export workflows, and release paths
  And the skill escalates missing pre-merge review, protected-main bypass, gatekeeper designation, core platform service scope, obligation applicability, consent interpretation, self-preferencing assessments, fair access terms, suspension or exemption questions, and regulatory interpretation to legal, compliance, platform governance, product, privacy, security, platform, risk, competition, or executive accountability owners
  And the skill reviews Java implementation, configuration, APIs, DTOs, repositories, schemas, migrations, messages, ranking logic, feature flags, experiments, consent and preference records, audit logs, metrics, traces, dashboards, alerts, export jobs, business-user portals, documentation, tests, release records, and compliance reports
  And the skill identifies risk signals for unclear core platform service scope, missing interoperability evidence, missing business-user data access APIs, missing consent and preference evidence, missing ranking and self-preferencing audit signals, missing business-user export workflows, weak anti-circumvention guardrails, weak access control, incomplete observability, weak change control, incomplete documentation, direct-to-main commit policy, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential DMA violation or non-compliance signals to article or chapter references with associated official-source links using only the reviewed direct-to-main delivery evidence
  And the skill analyzes the CheckoutService feature request as a direct-to-main platform change that modifies order database structure and outbound Kafka event data
  And the skill uses Java examples to explain interoperability interfaces, business-user data access, consent and preference evidence, ranking audit signals, business-user export workflows, anti-circumvention release gates, and compliance evidence handoff
  And the skill recommends engineering controls for pre-commit review, main-branch protection, interoperability interfaces, data access APIs, consent and preference evidence, ranking and self-preferencing audit signals, business-user export workflows, anti-circumvention guardrails, access control, observability, change control, documentation, database migration approval, Kafka schema compatibility, and compliance evidence handoff
  And the skill reports conclusions and actions using the EU Digital Markets Act engineering review report template
  And the generated report contains a potential violation or non-compliance mapping table with associated official-source links
  And the skill overwrites the EU Digital Markets Act engineering review report at "examples/regulations/eu-digital-markets-act/DMA-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset
