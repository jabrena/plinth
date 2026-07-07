## Why

GitHub issue [#940](https://github.com/jabrena/plinth/issues/940) requests a dedicated `707-technologies-onion-architecture` skill. The issue identifies ArchUnit's architecture support as the source reference and frames the outcome as framework-agnostic Onion architecture guidance for Java enterprise developers and architects.

Java teams need focused guidance for reviewing and improving dependency direction between domain, application/use-case, adapter, and infrastructure concerns. The repository already has technology skills for framework-agnostic topics such as OpenAPI, WireMock, fuzz testing, SQL, MongoDB, and Docker; Onion architecture belongs in that same technology guidance family rather than inside Spring Boot, Quarkus, or Micronaut-specific skills.

## What Changes

- Add `707-technologies-onion-architecture` as a new technology skill.
- Provide Onion architecture guidance for Java projects, including layer responsibilities, dependency-direction review, and boundary violation findings.
- Include ArchUnit-oriented architecture verification guidance where teams can encode Onion architecture rules as tests.
- Register the skill so local generation emits `.agents/skills/707-technologies-onion-architecture`.
- Add acceptance coverage for the generated skill and acceptance prompt inventory entry.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `onion-architecture-technology-skill-reference`: Adds framework-agnostic Onion architecture skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#940](https://github.com/jabrena/plinth/issues/940).
- External reference from issue: [ArchUnit architectures user guide](https://www.archunit.org/userguide/html/000_Index.html#_architectures).
- Existing technology skill patterns: `701-technologies-openapi`, `702-technologies-wiremock`, and `706-technologies-containers-docker`.
- Local command workflow: `.cursor/commands/create-spec.md`.
- Local OpenSpec guidance: `042-planning-openspec`.
- Design sequencing guidance: `051-design-two-steps-methods`.
- Derivation direction: issue #940 plus ArchUnit architecture reference plus existing technology skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the observable outcome is one new generated technology skill. The work touches skill index XML, reference XML, skill registration, acceptance-test prompt inventory, and local generated output validation, but those edits share one value boundary and one review boundary.

The first useful slice is a single framework-agnostic Onion architecture skill with ArchUnit-aware verification guidance. Follow-up slices may add richer examples, architecture report templates, or deeper framework-specific examples, but those are not required for the first accepted change.

## Impact

XML skill indexes, XML skill references, `skills.xml`, skill acceptance tests, local generated `.agents/skills` output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
