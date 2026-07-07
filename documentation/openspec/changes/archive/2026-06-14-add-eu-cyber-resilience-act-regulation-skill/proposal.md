## Why

GitHub issue [#855](https://github.com/jabrena/plinth/issues/855) identifies the Cyber Resilience Act (CRA) as a pending part of the GenAI Regulatory Stack (EU). CRA guidance should be planned separately because security-by-design duties for products with digital elements have a different engineering focus than AI governance, privacy, financial ICT resilience, or critical-sector cybersecurity operations.

Java enterprise teams shipping libraries, services, agents, plugins, connected products, APIs, or platform components need CRA-aware guidance for secure-by-design development, vulnerability handling, update mechanisms, SBOM and dependency evidence, product security documentation, and coordinated disclosure workflows.

## What Changes

- Add `805-regulations-eu-cyber-resilience-act` for engineering review of Regulation (EU) 2024/2847 concerns in Java enterprise systems and products with digital elements.
- Model the skill after `801`, `802`, and `803`: source-first review, "not legal advice" constraints, scope classification, evidence-driven findings, explicit escalation to legal/compliance/product/security/risk owners, and concrete Java architecture controls.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/805-regulations-eu-cyber-resilience-act`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `eu-cyber-resilience-act-regulation-skill-reference`: Adds Cyber Resilience Act skill guidance for EU secure-by-design engineering review.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#855](https://github.com/jabrena/plinth/issues/855), comment listing the GenAI Regulatory Stack (EU).
- Existing implementation models: `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr`.
- External reference: [Regulation (EU) 2024/2847](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847).
- Derivation direction: issue #855 EU regulatory stack plus official EUR-Lex reference plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for the Cyber Resilience Act only because the user requested one change per pending regulation. CRA has its own source authority, product-security scope, affected governance owners, and implementation acceptance boundary.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.
