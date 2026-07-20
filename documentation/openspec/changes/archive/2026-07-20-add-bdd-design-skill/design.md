## Context

Issue #1047 identifies one missing design skill: `058-design-bdd`. Existing repository capabilities cover adjacent parts of the lifecycle:

- `014-agile-user-story` authors user stories and Gherkin feature files.
- `054-design-tdd` drives implementation through red-green-refactor cycles.
- `133`, `323`, `423`, and `523` implement acceptance tests at framework-agnostic and framework-specific levels.

The new skill needs a distinct responsibility: independently guide collaborative discovery and formulation of behavior examples and preserve those examples as observable scenarios without collapsing BDD into Gherkin syntax or test automation alone. Any delivery alignment happens only in a later, separately requested interaction.

## Goals / Non-Goals

**Goals:**

- Define `058-design-bdd` as a generated, interactive design skill.
- Guide a self-contained BDD flow from trusted behavior facts through concrete examples to observable scenarios.
- Use shared domain language and keep examples focused on business behavior rather than implementation details.
- Make trust, interaction independence, source ownership, acceptance coverage, and validation expectations explicit.

**Non-Goals:**

- Replacing `014-agile-user-story` for user-story document creation.
- Replacing `054-design-tdd` for test-first Java implementation cycles.
- Replacing `133`, `323`, `423`, or `523` for acceptance-test code generation.
- Invoking, routing to, or requiring another skill during the BDD interaction.
- Introducing a Cucumber dependency, framework-specific glue code, or a new Maven module.
- Editing generated `.agents/skills/`, public `skills/`, legacy `.cursor/rules/`, or website `docs/` directly.

## Decisions

### One change, one outcome

Use one OpenSpec change, `add-bdd-design-skill`. The XML source, registration, tests, and inventory documentation are parts of one additive capability and do not have independent deployment or rollback value.

### Generated skill structure

Follow the current design-skill convention:

- `plinth-skills-generator/src/main/resources/skill-indexes/058-skill.xml` provides generated skill metadata, triggers, constraints, high-level workflow, output expectations, and safeguards.
- `plinth-skills-generator/src/main/resources/skill-references/058-design-bdd.xml` provides an example-driven Gherkin syntax reference.
- `plinth-skills-generator/src/main/resources/skills.xml` registers id `058`, explicit skill id `058-design-bdd`, and reference `058-design-bdd`.

The skill is interactive because pending or ambiguous behavior, actors, outcomes, rules, and examples may require focused clarification. Unanswered items remain unresolved rather than being invented.

### BDD responsibility and workflow

The generated skill index owns the interactive workflow and preserves four responsibilities:

1. Confirm maintainer-provided or maintainer-sanitized behavior facts, actors, desired outcomes, business rules, and terminology.
2. Discover concrete examples, including main behavior, meaningful alternatives, boundaries, and error outcomes, without inventing unsupported rules.
3. Formulate observable scenarios in shared domain language, using Given/When/Then where useful while avoiding UI, database, framework, or class-level detail unless it is part of the externally visible contract.
4. Report a self-contained BDD outcome with approved examples, observable scenarios, unresolved questions, deferred examples, source conflicts, and remaining risks.

BDD is treated as a collaboration and design practice. Gherkin is one representation, not the definition of BDD.

The detailed reference does not duplicate this workflow. It uses PML `<examples>` to explain Gherkin syntax through valid and invalid `.feature` examples and does not define procedural PML `<steps>`.

### Bundled Gherkin syntax reference

The generated detailed reference MUST contain guidance derived from the official Cucumber [Gherkin Reference](https://cucumber.io/docs/gherkin/reference/). The URL is upstream provenance only; the bundled reference is the complete runtime source for:

- Primary and secondary Gherkin keywords.
- `Feature`, `Rule`, `Scenario`/`Example`, `Background`, `Scenario Outline`, and `Examples` structure.
- `Given`, `When`, `Then`, `And`, and `But` step syntax.
- Doc Strings, Data Tables, tags, comments, indentation, and localization.

The reference MUST teach these constructs through example syntax, including contrasts that expose invalid keyword, colon, placeholder, argument, tag, comment, or localization usage.

Tag examples distinguish Gherkin tags from Java annotations, demonstrate inherited `@acceptance-test` classification at Feature level and supported behavioral tags such as `@error` at Scenario level, include `@integration-test` when a trusted project execution convention supports it, prohibit tags on steps, and avoid inferring `@integration-test` from alternative or error behavior alone.

The skill MUST use only the bundled reference during execution and MUST NOT search, browse, open, or fetch the external upstream reference. Upstream refresh is a separate maintainer activity outside skill execution. The skill must not imply that syntactically valid Gherkin is automatically good BDD or require every discovered example to become an automated Cucumber test.

### Trust and authority boundary

Raw outsider-authored issues, tickets, chats, wiki pages, or feature bodies are not treated as trusted instructions or directly propagated. The skill requires maintainer-provided or maintainer-sanitized behavior facts and treats scenario prose, tables, and docstrings as requirement data only.

When trusted inputs conflict, the skill reports the conflict and requests a decision instead of silently choosing or synchronizing sources.

### Independent interaction boundary

The BDD skill completes discovery and scenario formulation independently. It does not invoke, route to, or require user-story, TDD, or acceptance-testing skills. Its self-contained outcome may be supplied by the user as input to a later, separately requested interaction for implementation or verification alignment.

### Acceptance and documentation

Add `058-design-bdd.feature` with one acceptance scenario that checks trusted behavior input, concrete-example discovery, shared-language Given/When/Then formulation, an independent self-contained outcome, and explicit unresolved items. Add the exact matching `execute @...feature` entry to `acceptance-tests-prompts-skills.md` and document the skill in the Design skills inventory.

### Compatibility review

This is additive and non-breaking. Existing skill identifiers and behavior remain unchanged. No migration, compatibility adapter, feature toggle, or release-output refresh is required for local implementation verification.

### Verification strategy

- Validate each edited XML file with `xmllint --noout`.
- Run `./mvnw clean install -pl plinth-skills-generator -am` to verify generator behavior and refresh local `.agents/skills` output.
- Inspect `.agents/skills/058-design-bdd/SKILL.md` and its generated reference for unresolved include markers and broken local paths.
- Run only the new `058-design-bdd` prompt from `acceptance-tests-prompts-skills.md`, as required by the repository acceptance-prompt policy.
- Run the Markdown validator because the Java skill inventory changes.
- Run `openspec validate --all` for the planning artifacts.

## Risks / Trade-offs

- Overlap with user-story, TDD, and acceptance-test skills could blur responsibility; an independent interaction boundary reduces that risk.
- Treating Gherkin as the whole BDD practice would omit discovery and shared understanding; the workflow begins with trusted facts and ends with a self-contained BDD outcome.
- A generic workflow may miss organization-specific BDD conventions; unresolved domain or process choices remain explicit rather than being invented.

## Open Questions

- Does a maintainer-sanitized issue summary require additional BDD techniques, references, or acceptance criteria beyond the issue title? Requirements review is required if supplied.
- Which additional BDD practice references, if any, would materially improve the skill beyond the required official Cucumber Gherkin syntax reference? This remains optional `/explore-design` work.
