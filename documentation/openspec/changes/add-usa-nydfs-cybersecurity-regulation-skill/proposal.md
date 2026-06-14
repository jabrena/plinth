## Why

GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848) requests regulation support for AI-related Java enterprise work across multiple jurisdictions. The previous combined USA planning grouped GLBA Safeguards, NYDFS cybersecurity, and SEC cybersecurity disclosure together; this change splits NYDFS cybersecurity into its own reviewable regulation change.

Java enterprise teams building AI systems, AI agents, regulated financial services platforms, cybersecurity-governed workflows, and third-party service-provider integrations need NYDFS-specific guidance because 23 NYCRR 500 uses its own source authority, covered-entity concepts, certification evidence, governance expectations, and cybersecurity control vocabulary.

## What Changes

- Add `832-regulations-usa-nydfs-cybersecurity` for engineering review of NYDFS 23 NYCRR 500 cybersecurity concerns in Java enterprise systems.
- Model the skill after the regulation skill pattern: regulation-aware engineering guidance, "not legal advice" constraints, source-first review workflow, explicit escalation to legal/compliance/cybersecurity/risk/audit/executive owners, and concrete architecture controls.
- Register the skill in the skill inventory so local skill generation emits `.agents/skills/832-regulations-usa-nydfs-cybersecurity`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `usa-nydfs-cybersecurity-regulation-skill-reference`: Adds NYDFS cybersecurity regulation skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848).
- Split source artifact: previous combined USA regulation planning.
- Existing implementation model: `801-regulations-eu-ai-act`.
- Adjacent security patterns: `831-regulations-usa-glba-safeguards`, `804-regulations-eu-nis2`, and `805-regulations-eu-cyber-resilience-act`.
- External reference: [NYDFS Cybersecurity Regulation 23 NYCRR 500](https://www.dfs.ny.gov/system/files/documents/2023/03/23NYCRR500_0.pdf).
- Derivation direction: issue #848 plus official NYDFS reference plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for NYDFS cybersecurity only because the user requested the combined USA change be split per regulation. NYDFS cybersecurity has its own source authority, cybersecurity program scope, governance owners, and implementation acceptance boundary.

The former combined USA regulation planning is replaced by this change, `add-usa-glba-safeguards-regulation-skill`, and `add-usa-sec-cybersecurity-disclosure-regulation-skill`.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
