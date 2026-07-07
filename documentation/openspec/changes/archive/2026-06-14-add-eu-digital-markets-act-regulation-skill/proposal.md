## Why

GitHub issue [#855](https://github.com/jabrena/plinth/issues/855) identifies the Digital Markets Act (DMA) as a pending part of the GenAI Regulatory Stack (EU). DMA guidance should be planned separately because gatekeeper obligations, interoperability, data access, self-preferencing, consent, and business-user controls have a different engineering focus than AI Act, DORA, GDPR, NIS2, CRA, Data Act, or DSA concerns.

Java enterprise teams building platform services, marketplaces, app stores, advertising systems, identity services, ranking systems, or data access APIs need DMA-aware engineering guidance for gatekeeper-scope handoff, interoperability, data portability, audit evidence, business-user controls, and compliance-owner escalation.

## What Changes

- Add `808-regulations-eu-digital-markets-act` for engineering review of Regulation (EU) 2022/1925 concerns in Java enterprise systems.
- Model the skill after `801`, `802`, and `803`: source-first review, "not legal advice" constraints, scope classification, evidence-driven findings, explicit escalation to legal/compliance/platform/product/security/risk owners, and concrete Java architecture controls.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/808-regulations-eu-digital-markets-act`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `eu-digital-markets-act-regulation-skill-reference`: Adds Digital Markets Act skill guidance for EU gatekeeper-platform engineering review.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#855](https://github.com/jabrena/plinth/issues/855), comment listing the GenAI Regulatory Stack (EU).
- Existing implementation models: `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr`.
- External reference: [Regulation (EU) 2022/1925](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925).
- Derivation direction: issue #855 EU regulatory stack plus official EUR-Lex reference plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for the Digital Markets Act only because the user requested one change per pending regulation. DMA has its own source authority, gatekeeper-platform scope, affected governance owners, and implementation acceptance boundary.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.
