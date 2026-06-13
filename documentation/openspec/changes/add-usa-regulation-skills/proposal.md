## Why

GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848) requests regulation support for AI-related Java enterprise work across multiple jurisdictions. The EU and UK portions are already planned separately; this change focuses only on the USA sources named in the issue.

Java enterprise teams building AI systems, AI agents, financial services integrations, privacy-sensitive workflows, and cybersecurity-governed platforms need USA-specific guidance because GLBA Safeguards, NYDFS cybersecurity, and SEC cybersecurity disclosure obligations use different source authorities, terminology, governance owners, and evidence expectations than the EU and UK regulation sets.

## What Changes

- Add `806-regulations-usa-glba-safeguards` for engineering review of GLBA Safeguards Rule concerns in Java enterprise systems.
- Add `807-regulations-usa-nydfs-cybersecurity` for engineering review of NYDFS 23 NYCRR 500 cybersecurity concerns in Java enterprise systems.
- Add `808-regulations-usa-sec-cybersecurity-disclosure` for engineering review of SEC cybersecurity disclosure readiness concerns in Java enterprise systems.
- Model all three skills after the regulation skill pattern: regulation-aware engineering guidance, "not legal advice" constraints, source-first review workflow, explicit escalation to legal/compliance/privacy/security/risk/disclosure owners, and concrete architecture controls.
- Register all three skills in the skill inventory so local skill generation emits `.agents/skills/806-regulations-usa-glba-safeguards`, `.agents/skills/807-regulations-usa-nydfs-cybersecurity`, and `.agents/skills/808-regulations-usa-sec-cybersecurity-disclosure`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `usa-regulation-skill-references`: Adds USA regulation skill references for GLBA Safeguards, NYDFS cybersecurity, and SEC cybersecurity disclosure readiness.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848).
- Adjacent OpenSpec context: `documentation/openspec/changes/add-eu-regulation-skills`.
- Adjacent OpenSpec context: `documentation/openspec/changes/add-uk-regulation-skills`.
- Existing implementation model: `801-regulations-eu-ai-act`.
- External reference: [FTC GLBA Safeguards Rule](https://www.ftc.gov/legal-library/browse/rules/safeguards-rule).
- External reference: [NYDFS Cybersecurity Regulation 23 NYCRR 500](https://www.dfs.ny.gov/system/files/documents/2023/03/23NYCRR500_0.pdf).
- External reference: [SEC Cybersecurity Risk Management, Strategy, Governance, and Incident Disclosure](https://www.sec.gov/rules/2023/07/cybersecurity-risk-management-strategy-governance-and-incident-disclosure).
- Derivation direction: issue #848 plus official USA references plus the existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because it delivers one atomic outcome: USA regulation skills in the same `800` regulations band. GLBA Safeguards, NYDFS cybersecurity, and SEC cybersecurity disclosure readiness are separate skills, but they share the same jurisdiction, source issue, generator registration path, validation workflow, and user-facing regulatory-support theme.

The change does not require splitting by regulation because no independent deployment, runtime code path, website publication, or public release output is requested. The active EU and UK changes remain separate because those jurisdictions have different source authorities. Future regulation support outside the USA can be separate changes because it will likely have different source authorities and review owners.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
