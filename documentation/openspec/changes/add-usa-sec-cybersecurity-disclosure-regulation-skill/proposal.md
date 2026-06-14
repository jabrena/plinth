## Why

GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848) requests regulation support for AI-related Java enterprise work across multiple jurisdictions. The previous combined USA planning grouped GLBA Safeguards, NYDFS cybersecurity, and SEC cybersecurity disclosure together; this change splits SEC cybersecurity disclosure readiness into its own reviewable regulation change.

Java enterprise teams building AI systems, AI agents, public-company platforms, cybersecurity incident workflows, and executive reporting pipelines need SEC cybersecurity disclosure readiness guidance because disclosure evidence, governance reporting, incident timeline reconstruction, and materiality escalation handoffs use different owners and controls than GLBA safeguards or NYDFS cybersecurity programs.

## What Changes

- Add `833-regulations-usa-sec-cybersecurity-disclosure` for engineering review of SEC cybersecurity disclosure readiness concerns in Java enterprise systems.
- Model the skill after the regulation skill pattern: regulation-aware engineering guidance, "not legal advice" constraints, source-first review workflow, explicit escalation to legal/disclosure committee/compliance/investor relations/security/risk/executive owners, and concrete architecture controls.
- Register the skill in the skill inventory so local skill generation emits `.agents/skills/833-regulations-usa-sec-cybersecurity-disclosure`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `usa-sec-cybersecurity-disclosure-regulation-skill-reference`: Adds SEC cybersecurity disclosure readiness regulation skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848).
- Split source artifact: previous combined USA regulation planning.
- Existing implementation model: `801-regulations-eu-ai-act`.
- Adjacent security and disclosure patterns: `832-regulations-usa-nydfs-cybersecurity` and `802-regulations-dora`.
- External reference: [SEC Cybersecurity Risk Management, Strategy, Governance, and Incident Disclosure](https://www.sec.gov/rules/2023/07/cybersecurity-risk-management-strategy-governance-and-incident-disclosure).
- Derivation direction: issue #848 plus official SEC reference plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for SEC cybersecurity disclosure readiness only because the user requested the combined USA change be split per regulation. SEC cybersecurity disclosure has its own source authority, disclosure-readiness scope, governance owners, and implementation acceptance boundary.

The former combined USA regulation planning is replaced by this change, `add-usa-glba-safeguards-regulation-skill`, and `add-usa-nydfs-cybersecurity-regulation-skill`.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
