## 1. Generator Output

- [x] 1.1 Extend `SkillsGenerator.SkillOutput` with explicit skill-root-relative resource content.
- [x] 1.2 Map the five issue #801 resources to skills `151`, `161`, and `703`.
- [x] 1.3 Write mapped resources into generated skill directories and preserve script executability.

## 2. Reference Sources

- [x] 2.1 Replace the JMeter script XInclude with a relative link.
- [x] 2.2 Replace both profiling script XIncludes with relative links.
- [x] 2.3 Replace the CATS script and Dockerfile XIncludes with relative links.
- [x] 2.4 Validate all edited XML files with `xmllint`.

## 3. Verification

- [x] 3.1 Add generator tests for exact paths, contents, isolation, links, and executable scripts.
- [x] 3.2 Run `./mvnw clean verify -pl skills-generator`.
- [x] 3.3 Run local generation and inspect `.agents/skills` without refreshing public `skills/`.
- [x] 3.4 Validate all OpenSpec changes.
