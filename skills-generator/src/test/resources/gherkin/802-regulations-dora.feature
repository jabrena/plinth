Feature: Validate changes from usage of DORA regulation skill

Background:
  Given the skill "802-regulations-dora"

@acceptance-test
Scenario: Review a Java financial service with DORA operational resilience controls
  Given a Java enterprise system supports financial operations or a critical ICT service
  And the system depends on databases, message brokers, cloud services, and third-party ICT providers
  When the skill "802-regulations-dora" is applied
  Then the skill reads "references/802-regulations-dora.md"
  And the skill reads "assets/questions/802-dora-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/802-dora-engineering-review-report-template.md"
  And the skill uses "801-regulations-eu-ai-act" as the format and structure baseline for generated skill organization
  And the skill frames DORA findings as engineering controls rather than legal advice
  And the skill asks the DORA engineering review questionnaire one question at a time before implementation review
  And the skill does not infer or self-answer questionnaire responses from repository inspection
  And the skill scopes the business service, system owners, resilience owners, environments, ICT assets, data stores, providers, dependencies, and recovery expectations
  And the skill escalates applicability, financial-entity classification, reporting obligations, outsourcing interpretation, and provider criticality decisions to legal, compliance, security, risk, resilience, procurement, or business-continuity owners
  And the skill reviews Java implementation, configuration, tests, runbooks, observability, incident procedures, backup and restore evidence, change records, and provider documentation
  And the skill identifies risk signals for critical business service dependencies, ownerless ICT assets, untested recovery, missing alerting, incomplete logs, weak change control, unsupported provider dependencies, and missing exit plans
  And the skill uses Java examples to explain operational resilience and release-policy controls
  And the skill recommends engineering controls for asset inventory, monitoring, alerting, evidence-safe logging, incident workflow, backup and restore verification, failover testing, rollback plans, provider monitoring, exit planning, and change approval
  And the skill reports conclusions and actions using the DORA engineering review report template
