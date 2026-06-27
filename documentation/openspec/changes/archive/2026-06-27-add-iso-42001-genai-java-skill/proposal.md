## Why

GitHub issue [#939](https://github.com/jabrena/cursor-rules-java/issues/939) asks for a skill that explains ISO/IEC 42001 in the same practical style as the existing EU AI Act skill, with emphasis on GenAI development with Java.

Java enterprise teams using AI-assisted development, LLM integrations, RAG workflows, agents, generated code, external model providers, and AI-enabled business logic need source-first guidance that translates ISO/IEC 42001 AI management system concerns into reviewable engineering controls. The skill must help teams identify actionable evidence and escalation points without presenting legal, certification, or audit advice.

## What Changes

- Add an ISO/IEC 42001 skill for GenAI-oriented Java engineering review.
- Model the skill after the existing regulation skill pattern: authoritative source references, "not legal advice / not certification advice" constraints, Java-focused engineering controls, explicit owner escalation, examples, Gherkin acceptance coverage, and local generated skill validation.
- Cover GenAI development risks from issue #939: hallucinated code, insecure AI-generated implementation, generated dependency/supply-chain contamination, IP leakage, confidentiality breach, regulatory non-compliance, and biased generated business logic.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/813-regulations-iso-42001`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `iso-42001-genai-java-skill-reference`: Adds ISO/IEC 42001 GenAI Java engineering guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#939](https://github.com/jabrena/cursor-rules-java/issues/939).
- Official source references from issue #939:
  - [ISO - ISO/IEC 42001 explained](https://www.iso.org/home/insights-news/resources/iso-42001-explained-what-it-is.html)
  - [ISO/IEC 42001 standard page](https://www.iso.org/standard/42001)
  - [Microsoft ISO/IEC 42001 compliance offering overview](https://learn.microsoft.com/en-us/compliance/regulatory/offering-iso-42001)
- Existing implementation model: `801-regulations-eu-ai-act` and the later regulation skill pattern using split chapter/summary and engineering-example references.
- Derivation direction: issue #939 plus official ISO/IEC 42001 references plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for one new ISO/IEC 42001 skill. The implementation has a single review, validation, and rollback boundary: adding the generator source, references, acceptance tests, prompt inventory entry, and generated local skill verification for that skill. It does not require changes to the existing EU AI Act workflow, public release output, website output, or legacy Cursor rules.

## Impact

XML skill indexes, XML skill references, questionnaire/report assets if needed, `skills.xml`, Gherkin acceptance tests, acceptance prompt inventory, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
