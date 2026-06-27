Feature: Validate changes from usage of ISO/IEC 42001 regulation skill

Background:
  Given the skill "813-regulations-iso-42001"

@acceptance-test
Scenario: Review a GenAI-assisted Java change through pull-request delivery with ISO/IEC 42001 controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/813-regulations-iso-42001"
  And the requested report output path is "examples/regulations/iso-42001/ISO-42001-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/iso-42001" has no git changes
  And the feature request is expected to be developed and released through the described CI/CD pipeline
  And the ISO/IEC 42001 review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the reviewed scenario includes GenAI-assisted Java implementation, generated tests, generated dependency suggestions, prompts, model-provider boundaries, and AI-enabled checkout business logic
  When the skill ".agents/skills/813-regulations-iso-42001" is applied to the system description, diagram, and feature request files
  Then the skill reads "references/813-regulations-iso-42001-chapters-summary.md"
  And the skill reads "references/813-regulations-iso-42001-engineering-examples.md"
  And the skill reads "assets/questions/813-iso-42001-engineering-review-questionnaire.md"
  And the skill reads "assets/reports/813-iso-42001-engineering-review-report-template.md"
  And the skill frames ISO/IEC 42001 findings as engineering controls and evidence gaps rather than legal advice, certification advice, audit conclusions, or final conformity decisions
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes GenAI capability, AI-assisted development workflow, generated artifacts, model provider, prompts, retrieval sources, data classes, generated dependencies, business logic, environments, lifecycle stage, system owner, data owner, risk owner, security owner, privacy owner, compliance owner, product owner, platform owner, and release owner
  And the skill identifies risk signals for hallucinated code, insecure generated implementation, generated dependency and supply-chain contamination, IP leakage, confidentiality breach, regulatory non-compliance risk, biased generated business logic, CI/CD pipeline, database migration, Kafka message contract, and production side-effect signals
  And each GenAI risk is connected to actionable Java engineering practices and qualified owner escalation where needed
  And the skill analyzes the CheckoutService feature request as an ISO/IEC 42001 AI management system engineering review scope because AI-assisted development and generated business logic can affect Java software delivery risk
  And the skill uses Java examples to explain AI inventory, generated-code review gates, secure generated implementation controls, generated dependency provenance, prompt and confidentiality boundaries, biased business logic review, monitoring, incident handling, corrective action, and owner handoffs
  And the skill recommends engineering controls for human review, source verification, test evidence, static analysis, secure coding review, dependency policy gates, SBOM, vulnerability scanning, license review, approved model-provider routes, prompt minimization, redaction, access control, bias testing, monitoring, incident response, rollback, disablement, corrective action, and qualified owner escalation
  And the skill reports conclusions and actions using the ISO/IEC 42001 engineering review report template
  And the skill overwrites the ISO/IEC 42001 engineering review report at "examples/regulations/iso-42001/ISO-42001-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset

@integration-test
Scenario: Skill follows the generator registration and local-output workflow
  Given skill content must be maintained through the generator pipeline
  When the ISO/IEC 42001 skill is implemented
  Then the source changes are made under "skills-generator/src/main/resources"
  And "skills-generator/src/main/resources/skills.xml" registers skill id "813" with skillId "813-regulations-iso-42001"
  And the generated local skill output includes ".agents/skills/813-regulations-iso-42001/SKILL.md"
  And the generated local skill output includes ".agents/skills/813-regulations-iso-42001/assets/questions/813-iso-42001-engineering-review-questionnaire.md"
  And the generated local skill output includes ".agents/skills/813-regulations-iso-42001/assets/reports/813-iso-42001-engineering-review-report-template.md"
  And generated references contain no unresolved include markers or broken local reference paths
  And generated release output under "skills/" is not edited directly
  And applicable XML and skill generation validations can be executed before promotion
