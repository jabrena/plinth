Feature: Validate changes from usage of DORA regulation skill

Background:
  Given the skill "802-regulations-dora"

@acceptance-test
Scenario: Review a Java financial service with DORA operational resilience controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/802-regulations-dora"
  And the requested report output path is "examples/regulations/dora/DORA-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/dora" has no git changes
  And the feature request is expected to be developed and released through the described CI/CD pipeline
  And the DORA questionnaire answers are based only on information present in "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/802-regulations-dora" is applied to the system description, diagram, and feature request files
  Then the skill reads "references/802-regulations-dora.md"
  And the skill reads "assets/questions/802-dora-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/802-dora-engineering-review-report-template.md"
  And the skill frames DORA findings as engineering controls rather than legal advice
  And the model answers the DORA engineering review questionnaire without human intervention before implementation review
  And questionnaire responses do not use facts outside "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes the business service, system owners, resilience owners, environments, ICT assets, data stores, providers, dependencies, and recovery expectations
  And the skill classifies the operational scope as a financial entity, important business service, critical ICT function, third-party ICT provider relationship, or conventional non-financial service
  And the skill escalates applicability, financial-entity classification, reporting obligations, outsourcing interpretation, and provider criticality decisions to legal, compliance, security, risk, resilience, procurement, or business-continuity owners
  And the skill reviews Java implementation, configuration, tests, runbooks, observability, incident procedures, backup and restore evidence, change records, and provider documentation
  And the skill identifies risk signals for critical business service dependencies, ownerless ICT assets, untested recovery, missing alerting, incomplete logs, weak change control, unsupported provider dependencies, missing exit plans, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential DORA violation or non-compliance signals to article or chapter references using only the reviewed delivery evidence
  And the skill analyzes the CheckoutService feature request as a pipeline-delivered change that modifies order database structure and outbound Kafka event data
  And the skill uses Java examples to explain operational resilience and release-policy controls
  And the skill recommends engineering controls for asset inventory, monitoring, alerting, evidence-safe logging, incident workflow, backup and restore verification, failover testing, rollback plans, provider monitoring, exit planning, database migration approval, Kafka schema compatibility, and change approval
  And the skill reports conclusions and actions using the DORA engineering review report template
  And the skill overwrites the DORA engineering review report at "examples/regulations/dora/DORA-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Review a Java financial service checkout change with direct-to-main DORA controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/802-regulations-dora"
  And the requested report output path is "examples/regulations/dora/DORA-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/dora" has no git changes
  And the feature request is expected to be committed directly to main and released through the described CI/CD pipeline
  And the DORA questionnaire answers are based only on information present in "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/802-regulations-dora" is applied to the direct-to-main system description, diagram, and feature request files
  Then the skill reads "references/802-regulations-dora.md"
  And the skill reads "assets/questions/802-dora-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/802-dora-engineering-review-report-template.md"
  And the skill frames DORA findings as engineering controls rather than legal advice
  And the model answers the DORA engineering review questionnaire without human intervention before implementation review
  And questionnaire responses do not use facts outside "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes the business service, system owners, resilience owners, environments, ICT assets, data stores, providers, dependencies, and recovery expectations
  And the skill classifies the operational scope as a financial entity, important business service, critical ICT function, third-party ICT provider relationship, or conventional non-financial service
  And the skill escalates missing pre-merge review, protected-main bypass, weak change control, applicability, financial-entity classification, reporting obligations, outsourcing interpretation, and provider criticality decisions to legal, compliance, security, platform, risk, resilience, procurement, or business-continuity owners
  And the skill reviews Java implementation, configuration, tests, runbooks, observability, incident procedures, backup and restore evidence, change records, and provider documentation
  And the skill identifies risk signals for critical business service dependencies, ownerless ICT assets, untested recovery, missing alerting, incomplete logs, weak change control, unsupported provider dependencies, missing exit plans, direct-to-main commit policy, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential DORA violation or non-compliance signals to article or chapter references using only the reviewed direct-to-main delivery evidence
  And the skill analyzes the CheckoutService feature request as a direct-to-main pipeline-delivered change that modifies order database structure and outbound Kafka event data
  And the skill uses Java examples to explain operational resilience and release-policy controls
  And the skill recommends engineering controls for pre-commit review, main-branch protection, asset inventory, monitoring, alerting, evidence-safe logging, incident workflow, backup and restore verification, failover testing, rollback plans, provider monitoring, exit planning, database migration approval, Kafka schema compatibility, and change approval
  And the skill reports conclusions and actions using the DORA engineering review report template
  And the skill overwrites the DORA engineering review report at "examples/regulations/dora/DORA-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset
