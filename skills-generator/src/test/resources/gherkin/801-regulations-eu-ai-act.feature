Feature: Validate changes from usage of EU AI Act regulation skill

Background:
  Given the skill "801-regulations-eu-ai-act"

@acceptance-test
Scenario: Review a Java AI system with EU AI Act controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/801-regulations-eu-ai-act"
  And the requested report output path is "examples/regulations/eu-ai-act/EU-AI-ACT-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-ai-act" has no git changes
  And the feature request is expected to be developed and released through the described CI/CD pipeline
  And the EU AI Act questionnaire answers are based only on information present in "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/801-regulations-eu-ai-act" is applied to the system description, diagram, and feature request files
  Then the skill reads "references/801-regulations-eu-ai-act.md"
  And the skill reads "references/801-regulations-eu-ai-act-chapters-summary.md"
  And the skill reads "assets/questions/801-eu-ai-act-risk-questionnaire.md"
  And the skill reads "assets/reports/801-eu-ai-act-engineering-review-report-template.md"
  And the model answers the EU AI Act questionnaire without human intervention before implementation review
  And questionnaire responses do not use facts outside "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill classifies the capability as AI system, decision support, automated decision, AI agent, tool-calling workflow, RAG, generated artifact pipeline, or not an AI system
  And the skill identifies prohibited-practice, high-risk, sensitive-data, regulated-record, production side-effect, database migration, Kafka message contract, CI/CD pipeline, and enterprise-system-of-record signals
  And the skill analyzes the CheckoutService feature request as a pipeline-delivered change that modifies order database structure and outbound Kafka event data
  And the skill escalates prohibited-use, high-risk, sensitive, or ambiguous cases to legal, compliance, privacy, security, or risk owners
  And the skill recommends engineering controls for human oversight, policy gates, least privilege, audit evidence, data governance, database migration approval, Kafka schema compatibility, monitoring, incident response, rollback, and disablement
  And the skill reports conclusions and actions using the EU AI Act engineering review report template
  And the skill overwrites the EU AI Act engineering review report at "examples/regulations/eu-ai-act/EU-AI-ACT-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Review a Java AI-assisted checkout change with direct-to-main CI/CD controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/801-regulations-eu-ai-act"
  And the requested report output path is "examples/regulations/eu-ai-act/EU-AI-ACT-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-ai-act" has no git changes
  And the feature request is expected to be committed directly to main and released through the described CI/CD pipeline
  And the EU AI Act questionnaire answers are based only on information present in "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/801-regulations-eu-ai-act" is applied to the direct-to-main system description, diagram, and feature request files
  Then the skill reads "references/801-regulations-eu-ai-act.md"
  And the skill reads "references/801-regulations-eu-ai-act-chapters-summary.md"
  And the skill reads "assets/questions/801-eu-ai-act-risk-questionnaire.md"
  And the skill reads "assets/reports/801-eu-ai-act-engineering-review-report-template.md"
  And the model answers the EU AI Act questionnaire without human intervention before implementation review
  And questionnaire responses do not use facts outside "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill classifies the capability as AI system, decision support, automated decision, AI agent, tool-calling workflow, RAG, generated artifact pipeline, or not an AI system
  And the skill identifies prohibited-practice, high-risk, sensitive-data, regulated-record, production side-effect, direct-to-main commit policy, database migration, Kafka message contract, CI/CD pipeline, and enterprise-system-of-record signals
  And the skill analyzes the CheckoutService feature request as a direct-to-main pipeline-delivered change that modifies order database structure and outbound Kafka event data
  And the skill escalates missing pre-merge review, protected-main bypass, prohibited-use, high-risk, sensitive, or ambiguous cases to legal, compliance, privacy, security, platform, or risk owners
  And the skill recommends engineering controls for pre-commit review, main-branch protection, policy gates, least privilege, audit evidence, data governance, database migration approval, Kafka schema compatibility, monitoring, incident response, rollback, and disablement
  And the skill reports conclusions and actions using the EU AI Act engineering review report template
  And the skill overwrites the EU AI Act engineering review report at "examples/regulations/eu-ai-act/EU-AI-ACT-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset
