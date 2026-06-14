## Why

GitHub issue [#855](https://github.com/jabrena/cursor-rules-java/issues/855) identifies the Digital Services Act (DSA) as a pending part of the GenAI Regulatory Stack (EU). DSA guidance should be planned separately because intermediary-service, platform, recommender, content moderation, transparency, and systemic-risk controls have a different engineering focus than AI Act, DORA, GDPR, NIS2, CRA, Data Act, or DMA concerns.

Java enterprise teams building online platforms, marketplaces, moderation tooling, ranking systems, recommender systems, ad delivery, complaint workflows, or transparency reporting pipelines need DSA-aware engineering guidance for traceability, explanation, audit evidence, risk mitigation, user controls, and escalation to platform governance owners.

## What Changes

- Add `807-regulations-eu-digital-services-act` for engineering review of Regulation (EU) 2022/2065 concerns in Java enterprise systems.
- Model the skill after `801`, `802`, and `803`: source-first review, "not legal advice" constraints, scope classification, evidence-driven findings, explicit escalation to legal/compliance/trust-and-safety/security/risk owners, and concrete Java architecture controls.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/807-regulations-eu-digital-services-act`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `eu-digital-services-act-regulation-skill-reference`: Adds Digital Services Act skill guidance for EU online platform engineering review.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#855](https://github.com/jabrena/cursor-rules-java/issues/855), comment listing the GenAI Regulatory Stack (EU).
- Existing implementation models: `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr`.
- External reference: [Regulation (EU) 2022/2065](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065).
- Derivation direction: issue #855 EU regulatory stack plus official EUR-Lex reference plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for the Digital Services Act only because the user requested one change per pending regulation. DSA has its own source authority, online-platform scope, affected governance owners, and implementation acceptance boundary.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.
