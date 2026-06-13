Feature: Validate changes from usage of EU AI Act regulation skill

Background:
  Given the skill "801-regulations-eu-ai-act"

@acceptance-test
Scenario: Review a Java AI system with EU AI Act controls
  Given a Java enterprise system uses an LLM, RAG, tool calling, and generated deployment actions
  And the system may affect regulated records or production systems
  When the skill "801-regulations-eu-ai-act" is applied
  Then the skill reads "references/801-regulations-eu-ai-act.md"
  And the skill reads "references/801-regulations-eu-ai-act-chapters-summary.md"
  And the skill reads "assets/questions/801-eu-ai-act-risk-questionnaire.md"
  And the skill reads "assets/reports/801-eu-ai-act-engineering-review-report-template.md"
  And the skill asks the EU AI Act questionnaire one question at a time before implementation review
  And the skill does not infer or self-answer questionnaire responses from repository inspection
  And the skill classifies the capability as AI system, decision support, automated decision, AI agent, tool-calling workflow, RAG, generated artifact pipeline, or not an AI system
  And the skill identifies prohibited-practice, high-risk, sensitive-data, regulated-record, production side-effect, and enterprise-system-of-record signals
  And the skill escalates prohibited-use, high-risk, sensitive, or ambiguous cases to legal, compliance, privacy, security, or risk owners
  And the skill recommends engineering controls for human oversight, policy gates, least privilege, audit evidence, data governance, monitoring, incident response, and disablement
  And the skill reports conclusions and actions using the EU AI Act engineering review report template
