Feature: Validate changes from usage of EU Cyber Resilience Act regulation skill

Background:
  Given the skill "805-regulations-eu-cyber-resilience-act"

@acceptance-test
Scenario: Review a Java product-adjacent service with Cyber Resilience Act controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/805-regulations-eu-cyber-resilience-act"
  And the requested report output path is "examples/regulations/eu-cyber-resilience-act/CRA-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-cyber-resilience-act" has no git changes
  And the feature request is expected to be developed and released through the described CI/CD pipeline
  And the Cyber Resilience Act review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/805-regulations-eu-cyber-resilience-act" is applied to the system description, diagram, and feature request files
  Then the skill reads "references/805-regulations-eu-cyber-resilience-act-chapters-summary.md"
  And the skill reads "references/805-regulations-eu-cyber-resilience-act-engineering-examples.md"
  And the skill reads "assets/reports/805-eu-cyber-resilience-act-engineering-review-report-template.md"
  And the skill frames Cyber Resilience Act findings as engineering controls rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes product context, possible product-with-digital-elements signals, possible remote data processing signals, product owners, product security owners, support owners, environments, intended purpose, reasonably foreseeable use, update paths, vulnerabilities, dependencies, SBOM evidence, and product documentation
  And the skill escalates product classification, economic-operator role, conformity assessment, CE marking implications, Article 14 reporting obligations, support-period interpretation, cybersecurity risk acceptance, and regulatory interpretation to legal, compliance, product, product-security, market-access, security, risk, support, or executive accountability owners
  And the skill reviews Java implementation, configuration, tests, threat models, product documentation, observability, vulnerability records, coordinated disclosure policy, security update mechanisms, dependency inventories, SBOMs, support-period evidence, release records, and user instructions
  And the skill identifies risk signals for unclear product scope, ownerless product security controls, missing threat model, weak secure defaults, incomplete vulnerability handling, missing coordinated disclosure evidence, weak update mechanism, incomplete dependency inventory, missing SBOM, weak authentication or authorization, weak cryptography, sensitive-data logging, missing product security documentation, missing support-period signal, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential Cyber Resilience Act violation or non-compliance signals to regulation topic areas using only the reviewed delivery evidence with associated official-source links
  And the skill analyzes the CheckoutService feature request as a pipeline-delivered product-adjacent change that modifies order database structure and outbound Kafka event data
  And the skill uses Java examples to explain secure defaults, vulnerability handling, coordinated disclosure, security update delivery, dependency and SBOM evidence, product documentation, support-period signaling, and secure release-policy controls
  And the skill recommends engineering controls for product security scope inventory, secure-by-design development, threat modeling, secure defaults, vulnerability remediation, coordinated disclosure, security advisory workflow, secure update delivery, dependency and SBOM evidence, cryptography, authentication, authorization, sensitive-data-safe logging, product security documentation, support-period and end-of-support signaling, database migration approval, Kafka schema compatibility, and release readiness
  And the skill reports conclusions and actions using the Cyber Resilience Act engineering review report template
  And the skill overwrites the Cyber Resilience Act engineering review report at "examples/regulations/eu-cyber-resilience-act/CRA-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Review a Java product-adjacent checkout change with direct-to-main Cyber Resilience Act controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/805-regulations-eu-cyber-resilience-act"
  And the requested report output path is "examples/regulations/eu-cyber-resilience-act/CRA-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-cyber-resilience-act" has no git changes
  And the feature request is expected to be committed directly to main and released through the described CI/CD pipeline
  And the Cyber Resilience Act review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/805-regulations-eu-cyber-resilience-act" is applied to the direct-to-main system description, diagram, and feature request files
  Then the skill reads "references/805-regulations-eu-cyber-resilience-act-chapters-summary.md"
  And the skill reads "references/805-regulations-eu-cyber-resilience-act-engineering-examples.md"
  And the skill reads "assets/reports/805-eu-cyber-resilience-act-engineering-review-report-template.md"
  And the skill frames Cyber Resilience Act findings as engineering controls rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes product context, possible product-with-digital-elements signals, possible remote data processing signals, product owners, product security owners, support owners, environments, intended purpose, reasonably foreseeable use, update paths, vulnerabilities, dependencies, SBOM evidence, and product documentation
  And the skill escalates missing pre-merge review, protected-main bypass, product classification, economic-operator role, conformity assessment, CE marking implications, Article 14 reporting obligations, support-period interpretation, cybersecurity risk acceptance, and regulatory interpretation to legal, compliance, product, product-security, market-access, security, platform, risk, support, or executive accountability owners
  And the skill reviews Java implementation, configuration, tests, threat models, product documentation, observability, vulnerability records, coordinated disclosure policy, security update mechanisms, dependency inventories, SBOMs, support-period evidence, release records, and user instructions
  And the skill identifies risk signals for unclear product scope, ownerless product security controls, missing threat model, weak secure defaults, incomplete vulnerability handling, missing coordinated disclosure evidence, weak update mechanism, incomplete dependency inventory, missing SBOM, weak authentication or authorization, weak cryptography, sensitive-data logging, missing product security documentation, missing support-period signal, direct-to-main commit policy, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential Cyber Resilience Act violation or non-compliance signals to regulation topic areas using only the reviewed direct-to-main delivery evidence with associated official-source links
  And the skill analyzes the CheckoutService feature request as a direct-to-main pipeline-delivered product-adjacent change that modifies order database structure and outbound Kafka event data
  And the skill uses Java examples to explain secure defaults, vulnerability handling, coordinated disclosure, security update delivery, dependency and SBOM evidence, product documentation, support-period signaling, and secure release-policy controls
  And the skill recommends engineering controls for pre-commit review, main-branch protection, product security scope inventory, secure-by-design development, threat modeling, secure defaults, vulnerability remediation, coordinated disclosure, security advisory workflow, secure update delivery, dependency and SBOM evidence, cryptography, authentication, authorization, sensitive-data-safe logging, product security documentation, support-period and end-of-support signaling, database migration approval, Kafka schema compatibility, and release readiness
  And the skill reports conclusions and actions using the Cyber Resilience Act engineering review report template
  And the skill overwrites the Cyber Resilience Act engineering review report at "examples/regulations/eu-cyber-resilience-act/CRA-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset
