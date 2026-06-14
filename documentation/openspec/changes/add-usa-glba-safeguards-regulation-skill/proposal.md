## Why

GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848) requests regulation support for AI-related Java enterprise work across multiple jurisdictions. The previous combined USA planning grouped GLBA Safeguards, NYDFS cybersecurity, and SEC cybersecurity disclosure together; this change splits GLBA Safeguards into its own reviewable regulation change.

Java enterprise teams building AI systems, AI agents, financial services integrations, customer-information workflows, and service-provider platforms need GLBA Safeguards guidance because it uses USA financial privacy and safeguards authorities, terminology, governance owners, and evidence expectations that differ from EU, UK, NYDFS, and SEC concerns.

## What Changes

- Add `831-regulations-usa-glba-safeguards` for engineering review of GLBA Safeguards Rule concerns in Java enterprise systems.
- Model the skill after the regulation skill pattern: regulation-aware engineering guidance, "not legal advice" constraints, source-first review workflow, explicit escalation to legal/compliance/privacy/security/risk/vendor-management owners, and concrete architecture controls.
- Register the skill in the skill inventory so local skill generation emits `.agents/skills/831-regulations-usa-glba-safeguards`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `usa-glba-safeguards-regulation-skill-reference`: Adds GLBA Safeguards Rule regulation skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848).
- Split source artifact: previous combined USA regulation planning.
- Existing implementation model: `801-regulations-eu-ai-act`.
- Adjacent privacy/security patterns: `803-regulations-gdpr`, `822-regulations-uk-gdpr`, and `832-regulations-usa-nydfs-cybersecurity`.
- External reference: [FTC GLBA Safeguards Rule](https://www.ftc.gov/legal-library/browse/rules/safeguards-rule).
- Derivation direction: issue #848 plus official GLBA Safeguards reference plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for GLBA Safeguards only because the user requested the combined USA change be split per regulation. GLBA Safeguards has its own source authority, customer-information safeguards scope, governance owners, and implementation acceptance boundary.

The former combined USA regulation planning is replaced by this change, `add-usa-nydfs-cybersecurity-regulation-skill`, and `add-usa-sec-cybersecurity-disclosure-regulation-skill`.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
