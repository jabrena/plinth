Feature: Validate changes from usage of DORA regulation skill

Background:
  Given the skill "802-regulations-dora"

@acceptance-test
Scenario: Review a Java financial service with DORA operational resilience controls
  Given the system description file "examples/diagrams/deployment/system-example.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the local generated skill path ".agents/skills/802-regulations-dora"
  And the requested report output path is "examples/regulations/dora/DORA-ENGINEERING-REVIEW-REPORT.md"
  And the DORA questionnaire answers are based only on information present in "examples/diagrams/deployment/system-example.md"
  When the skill ".agents/skills/802-regulations-dora" is applied to the system description and diagram files
  Then the skill reads "references/802-regulations-dora.md"
  And the skill reads "assets/questions/802-dora-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/802-dora-engineering-review-report-template.md"
  And the skill frames DORA findings as engineering controls rather than legal advice
  And the skill asks the DORA engineering review questionnaire one question at a time before implementation review
  And questionnaire responses do not use facts outside "examples/diagrams/deployment/system-example.md"
  And the skill scopes the business service, system owners, resilience owners, environments, ICT assets, data stores, providers, dependencies, and recovery expectations
  And the skill classifies the operational scope as a financial entity, important business service, critical ICT function, third-party ICT provider relationship, or conventional non-financial service
  And the skill escalates applicability, financial-entity classification, reporting obligations, outsourcing interpretation, and provider criticality decisions to legal, compliance, security, risk, resilience, procurement, or business-continuity owners
  And the skill reviews Java implementation, configuration, tests, runbooks, observability, incident procedures, backup and restore evidence, change records, and provider documentation
  And the skill identifies risk signals for critical business service dependencies, ownerless ICT assets, untested recovery, missing alerting, incomplete logs, weak change control, unsupported provider dependencies, and missing exit plans
  And the skill uses Java examples to explain operational resilience and release-policy controls
  And the skill recommends engineering controls for asset inventory, monitoring, alerting, evidence-safe logging, incident workflow, backup and restore verification, failover testing, rollback plans, provider monitoring, exit planning, and change approval
  And the skill reports conclusions and actions using the DORA engineering review report template
  And the skill writes the DORA engineering review report to "examples/regulations/dora/DORA-ENGINEERING-REVIEW-REPORT.md"
