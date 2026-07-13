Feature: Validate changes from usage of EU Product Liability Directive regulation skill

Background:
  Given the skill "812-regulations-eu-product-liability-directive"

@acceptance-test
Scenario: Review a Spring Boot RAG maintenance assistant through pull-request delivery with Product Liability Directive controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/812-regulations-eu-product-liability-directive"
  And the requested report output path is "examples/regulations/eu-product-liability-directive/PLD-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-product-liability-directive" has no git changes
  And the feature request is expected to be developed and released through the described CI/CD pipeline
  And the Product Liability Directive review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the reviewed scenario includes a Spring Boot RAG-based GenAI maintenance assistant for industrial machinery whose generated repair instructions could contribute to injury, property damage, unsafe configuration, defective updates, or security compromise
  When the skill ".agents/skills/812-regulations-eu-product-liability-directive" is applied to the system description, diagram, and feature request files
  Then the skill reads "references/812-regulations-eu-product-liability-directive-chapters-summary.md"
  And the skill reads "references/812-regulations-eu-product-liability-directive-engineering-examples.md"
  And the skill reads "assets/questions/812-product-liability-directive-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/812-product-liability-directive-engineering-review-report-template.md"
  And the skill frames Product Liability Directive findings as engineering controls and evidence gaps rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes software product context, related service signals, generated instructions, AI-agent tool actions, automated updates, warnings and instructions, vulnerability handling, corrective updates, product safety owners, support owners, environments, intended purpose, reasonably foreseeable use, foreseeable misuse, incident records, audit logs, and product-liability evidence
  And the skill escalates defectiveness determinations, compensable-damage questions, causal-link conclusions, economic-operator responsibility, jurisdiction, limitation periods, development-risk defences, disclosure duties, and final liability assessment to legal, compliance, product, safety, security, risk, support, or executive accountability owners
  And the skill reviews Java implementation, Spring Boot configuration, prompts, RAG retrieval sources, model version records, generated instructions, AI-agent tool policies, automated update mechanisms, vulnerability records, corrective update procedures, warnings, user instructions, support records, incident evidence, audit logs, product-safety tests, hazardous-scenario tests, CI/CD workflows, deployment records, release approvals, and owner handoffs
  And the skill identifies risk signals for unclear software product scope, unclear related service scope, ownerless product-safety controls, missing intended use evidence, missing foreseeable misuse review, unapproved RAG sources, missing generated instruction provenance, unsafe AI-agent tool actions, weak human oversight, missing warning or instruction review, undocumented automated update path, incomplete vulnerability handling, missing corrective update procedure, missing incident reconstruction, CI/CD pipeline, database migration, Kafka message contract, and production side-effect signals
  And the skill maps potential Product Liability Directive violation or non-compliance signals to regulation topic areas using only the reviewed delivery evidence and bundled Product Liability Directive references
  And the skill does not fetch or ingest external regulatory web pages at runtime
  And the skill analyzes the Spring Boot RAG maintenance assistant as product-liability-relevant engineering review scope because generated repair instructions could contribute to injury, property damage, unsafe configuration, defective updates, or security compromise
  And the skill uses Java examples to explain product-liability scope inventory, source governance, model and retrieval provenance, generated instruction review, AI-agent tool-action controls, automated update traceability, warning and instruction versioning, vulnerability handling, corrective update procedures, incident evidence, and release gates
  And the skill recommends engineering controls for product-safety review, human oversight where appropriate, RAG source governance, generated-instruction hazardous-scenario testing, AI-agent tool-action allowlists, model and version provenance, automated update validation, corrective update procedures, incident evidence, support escalation, warning and instruction review, vulnerability handling, database migration approval, Kafka schema compatibility, and qualified owner escalation
  And the skill reports conclusions and actions using the Product Liability Directive engineering review report template
  And the skill overwrites the Product Liability Directive engineering review report at "examples/regulations/eu-product-liability-directive/PLD-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Review a Spring Boot RAG maintenance assistant direct-to-main change with Product Liability Directive controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/812-regulations-eu-product-liability-directive"
  And the requested report output path is "examples/regulations/eu-product-liability-directive/PLD-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-product-liability-directive" has no git changes
  And the feature request is expected to be committed directly to main and released through the described CI/CD pipeline
  And the Product Liability Directive review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the reviewed scenario includes a Spring Boot RAG-based GenAI maintenance assistant for industrial machinery whose generated repair instructions could contribute to injury, property damage, unsafe configuration, defective updates, or security compromise
  When the skill ".agents/skills/812-regulations-eu-product-liability-directive" is applied to the direct-to-main system description, diagram, and feature request files
  Then the skill reads "references/812-regulations-eu-product-liability-directive-chapters-summary.md"
  And the skill reads "references/812-regulations-eu-product-liability-directive-engineering-examples.md"
  And the skill reads "assets/questions/812-product-liability-directive-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/812-product-liability-directive-engineering-review-report-template.md"
  And the skill frames Product Liability Directive findings as engineering controls and evidence gaps rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes software product context, related service signals, generated instructions, AI-agent tool actions, automated updates, warnings and instructions, vulnerability handling, corrective updates, product safety owners, support owners, environments, intended purpose, reasonably foreseeable use, foreseeable misuse, incident records, audit logs, and product-liability evidence
  And the skill escalates missing pre-merge review, protected-main bypass, defectiveness determinations, compensable-damage questions, causal-link conclusions, economic-operator responsibility, jurisdiction, limitation periods, development-risk defences, disclosure duties, and final liability assessment to legal, compliance, product, safety, security, platform, risk, support, or executive accountability owners
  And the skill reviews Java implementation, Spring Boot configuration, prompts, RAG retrieval sources, model version records, generated instructions, AI-agent tool policies, automated update mechanisms, vulnerability records, corrective update procedures, warnings, user instructions, support records, incident evidence, audit logs, product-safety tests, hazardous-scenario tests, CI/CD workflows, deployment records, release approvals, and owner handoffs
  And the skill identifies risk signals for unclear software product scope, unclear related service scope, ownerless product-safety controls, missing intended use evidence, missing foreseeable misuse review, unapproved RAG sources, missing generated instruction provenance, unsafe AI-agent tool actions, weak human oversight, missing warning or instruction review, undocumented automated update path, incomplete vulnerability handling, missing corrective update procedure, missing incident reconstruction, direct-to-main commit policy, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential Product Liability Directive violation or non-compliance signals to regulation topic areas using only the reviewed direct-to-main delivery evidence and bundled Product Liability Directive references
  And the skill does not fetch or ingest external regulatory web pages at runtime
  And the skill analyzes the Spring Boot RAG maintenance assistant as product-liability-relevant engineering review scope because generated repair instructions could contribute to injury, property damage, unsafe configuration, defective updates, or security compromise
  And the skill uses Java examples to explain product-liability scope inventory, source governance, model and retrieval provenance, generated instruction review, AI-agent tool-action controls, automated update traceability, warning and instruction versioning, vulnerability handling, corrective update procedures, incident evidence, and release gates
  And the skill recommends engineering controls for pre-commit review, main-branch protection, product-safety review, human oversight where appropriate, RAG source governance, generated-instruction hazardous-scenario testing, AI-agent tool-action allowlists, model and version provenance, automated update validation, corrective update procedures, incident evidence, support escalation, warning and instruction review, vulnerability handling, database migration approval, Kafka schema compatibility, and qualified owner escalation
  And the skill reports conclusions and actions using the Product Liability Directive engineering review report template
  And the skill overwrites the Product Liability Directive engineering review report at "examples/regulations/eu-product-liability-directive/PLD-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset
