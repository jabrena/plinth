## Why

GitHub issue [#855](https://github.com/jabrena/plinth/issues/855) adds a GenAI Regulatory Stack (EU) comment that extends the existing EU regulation skills beyond `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr`. NIS2 remains pending from that stack and needs its own reviewable OpenSpec change because cybersecurity obligations for critical and important entities differ from AI governance, financial ICT resilience, and privacy controls.

Java enterprise teams building AI-enabled services, platform tooling, CI/CD automation, operational systems, or critical-sector integrations need NIS2-aware engineering guidance for cybersecurity risk management, incident handling, supply-chain security, vulnerability management, and audit-ready operational evidence.

## What Changes

- Add `804-regulations-eu-nis2` for engineering review of Directive (EU) 2022/2555 concerns in Java enterprise systems.
- Model the skill after `801`, `802`, and `803`: source-first review, "not legal advice" constraints, scope classification, evidence-driven findings, explicit escalation to legal/compliance/security/risk/resilience owners, concrete Java architecture controls, and generated engineering review reports.
- Register the skill, decoupled NIS2 chapters summary, Java engineering examples, and report template in the generator inventory so local skill generation emits `.agents/skills/804-regulations-eu-nis2`.
- Add Gherkin acceptance coverage and example NIS2 engineering review reports for pull-request based delivery and direct-to-main delivery.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `eu-nis2-regulation-skill-reference`: Adds NIS2 regulation skill guidance for EU cybersecurity engineering review.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#855](https://github.com/jabrena/plinth/issues/855), comment listing the GenAI Regulatory Stack (EU).
- Existing implementation models: `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr`.
- External reference: [Directive (EU) 2022/2555](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022L2555).
- Recent implementation evidence: commit `9bd50c97` (`feat(skills): add EU NIS2 regulation skill (#862)`), NIS2 Gherkin acceptance scenarios, and the generated example reports under `examples/regulations/nis2`.
- Derivation direction: issue #855 EU regulatory stack plus official EUR-Lex reference plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation -> acceptance report examples.

## Change Boundary Assessment

This is one OpenSpec change for NIS2 only because the user requested one change per pending regulation. NIS2 has its own source authority, cybersecurity scope, affected governance owners, and implementation acceptance boundary.

## Impact

XML skill indexes, XML skill references, report assets, `skills.xml`, Gherkin acceptance prompts, NIS2 example reports, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.
