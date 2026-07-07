## Why

GitHub issue [#855](https://github.com/jabrena/plinth/issues/855) identifies the Data Act as a pending part of the GenAI Regulatory Stack (EU). Data Act guidance should be planned separately because data access, portability, sharing, cloud switching, and non-personal data governance have a different engineering focus than AI Act, DORA, GDPR, NIS2, CRA, DSA, or DMA concerns.

Java enterprise teams building APIs, data platforms, SaaS products, connected-device integrations, AI data pipelines, or cloud services need Data Act-aware engineering guidance for data access controls, portability workflows, interoperability, contract/evidence handoffs, logging, non-personal data safeguards, and cloud-switching support.

## What Changes

- Add `806-regulations-eu-data-act` for engineering review of Regulation (EU) 2023/2854 concerns in Java enterprise systems.
- Model the skill after `801`, `802`, and `803`: source-first review, "not legal advice" constraints, scope classification, evidence-driven findings, explicit escalation to legal/compliance/privacy/security/data-governance owners, and concrete Java architecture controls.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/806-regulations-eu-data-act`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `eu-data-act-regulation-skill-reference`: Adds Data Act skill guidance for EU data access and portability engineering review.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#855](https://github.com/jabrena/plinth/issues/855), comment listing the GenAI Regulatory Stack (EU).
- Existing implementation models: `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr`.
- External reference: [Regulation (EU) 2023/2854](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854).
- Derivation direction: issue #855 EU regulatory stack plus official EUR-Lex reference plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for the Data Act only because the user requested one change per pending regulation. The Data Act has its own source authority, data access and portability scope, affected governance owners, and implementation acceptance boundary.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.
