Feature: Validate changes from usage of EU Digital Omnibus regulation skill

Background:
  Given the skill "809-regulations-eu-digital-omnibus"

@acceptance-test
Scenario: Review a Java ecommerce platform change for Digital Omnibus proposal-stage impacts
  Given the system description file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/809-regulations-eu-digital-omnibus"
  And the requested report output path is "examples/regulations/eu-digital-omnibus/DIGITAL-OMNIBUS-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-digital-omnibus" has no git changes
  And the feature request is expected to be developed and released through the described CI/CD pipeline
  And the Digital Omnibus review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/809-regulations-eu-digital-omnibus" is applied to the system description, diagram, and feature request files
  Then the skill reads "references/809-regulations-eu-digital-omnibus-source-summary.md"
  And the skill reads "references/809-regulations-eu-digital-omnibus-engineering-examples.md"
  And the skill reads "assets/reports/809-eu-digital-omnibus-engineering-review-report-template.md"
  And the skill frames Digital Omnibus findings as proposal-stage simplification impacts and engineering controls rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill records official source links, source-status wording, review date, source-status owner, and proposal-stage assumptions
  And the skill maps findings to affected regulation areas such as AI Act, GDPR or ePrivacy, DORA, NIS2, Data Act, cybersecurity incident reporting, and data-governance evidence using associated official source links
  And the skill preserves compatibility with existing regulation skills and does not rewrite AI Act, GDPR, DORA, or NIS2 conclusions without qualified owner decision evidence
  And the skill escalates legislative status, applicability, interpretation, adoption decisions, ambiguous proposal-stage language, and any relaxation of regulation-specific controls to legal, compliance, privacy, security, risk, resilience, data-governance, AI governance, product, or executive accountability owners
  And the skill reviews Java implementation, configuration, tests, runbooks, observability, incident procedures, data-rights workflows, AI governance records, dependency inventories, provider records, release gates, questionnaires, report templates, and existing regulation-skill outputs
  And the skill identifies update candidates for source-status checks, affected-regulation mapping, evidence inventory updates, change-control impacts, questionnaire or report-template updates, incident-reporting workflow consolidation, data-rights workflow impacts, AI governance timeline changes, and compatibility with existing regulation skills
  And the skill analyzes the CheckoutService feature request as a pipeline-delivered change that modifies order database structure and outbound Kafka event data
  And the skill uses Java examples to explain source-status records, evidence inventory updates, simplification-dependent release gates, questionnaire and report-template update candidates, incident-reporting workflow consolidation, data-rights workflow impacts, AI governance timeline review, and compatibility checks
  And the skill recommends engineering controls for source-status records, affected-regulation maps, owner approvals, evidence inventory updates, change-control gates, report-template update candidates, incident-reporting consolidation candidates, data-rights impact reviews, AI governance timeline reviews, compatibility checks with existing EU regulation skills, and ambiguity escalation
  And the skill reports conclusions and actions using the Digital Omnibus engineering review report template
  And the skill overwrites the Digital Omnibus engineering review report at "examples/regulations/eu-digital-omnibus/DIGITAL-OMNIBUS-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Review a direct-to-main Java checkout change for Digital Omnibus proposal-stage controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/809-regulations-eu-digital-omnibus"
  And the requested report output path is "examples/regulations/eu-digital-omnibus/DIGITAL-OMNIBUS-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-digital-omnibus" has no git changes
  And the feature request is expected to be committed directly to main and released through the described CI/CD pipeline
  And the Digital Omnibus review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/809-regulations-eu-digital-omnibus" is applied to the direct-to-main system description, diagram, and feature request files
  Then the skill reads "references/809-regulations-eu-digital-omnibus-source-summary.md"
  And the skill reads "references/809-regulations-eu-digital-omnibus-engineering-examples.md"
  And the skill reads "assets/reports/809-eu-digital-omnibus-engineering-review-report-template.md"
  And the skill frames Digital Omnibus findings as proposal-stage simplification impacts and engineering controls rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill records official source links, source-status wording, review date, source-status owner, and proposal-stage assumptions
  And the skill maps findings to affected regulation areas such as AI Act, GDPR or ePrivacy, DORA, NIS2, Data Act, cybersecurity incident reporting, and data-governance evidence using associated official source links
  And the skill preserves compatibility with existing regulation skills and does not rewrite AI Act, GDPR, DORA, or NIS2 conclusions without qualified owner decision evidence
  And the skill escalates missing pre-merge review, direct-to-main control assumptions, legislative status, applicability, interpretation, adoption decisions, ambiguous proposal-stage language, and any relaxation of regulation-specific controls to legal, compliance, privacy, security, platform, risk, resilience, data-governance, AI governance, product, or executive accountability owners
  And the skill reviews Java implementation, configuration, tests, runbooks, observability, incident procedures, data-rights workflows, AI governance records, dependency inventories, provider records, release gates, questionnaires, report templates, branch protection evidence, direct-to-main exception records, and existing regulation-skill outputs
  And the skill identifies update candidates and risk signals for source-status checks, affected-regulation mapping, evidence inventory updates, change-control impacts, questionnaire or report-template updates, incident-reporting workflow consolidation, data-rights workflow impacts, AI governance timeline changes, direct-to-main commit policy, protected-main evidence, and compatibility with existing regulation skills
  And the skill analyzes the CheckoutService feature request as a direct-to-main pipeline-delivered change that modifies order database structure and outbound Kafka event data
  And the skill uses Java examples to explain source-status records, evidence inventory updates, simplification-dependent release gates, questionnaire and report-template update candidates, incident-reporting workflow consolidation, data-rights workflow impacts, AI governance timeline review, and compatibility checks
  And the skill recommends engineering controls for pre-commit review, main-branch protection, source-status records, affected-regulation maps, owner approvals, evidence inventory updates, change-control gates, report-template update candidates, incident-reporting consolidation candidates, data-rights impact reviews, AI governance timeline reviews, compatibility checks with existing EU regulation skills, and ambiguity escalation
  And the skill reports conclusions and actions using the Digital Omnibus engineering review report template
  And the skill overwrites the Digital Omnibus engineering review report at "examples/regulations/eu-digital-omnibus/DIGITAL-OMNIBUS-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset
