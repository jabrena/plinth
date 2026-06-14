# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review GitHub issue #855 comment listing the GenAI Regulatory Stack (EU).
- [x] 1.2 Confirm `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr` are already covered and the Cyber Resilience Act remains pending.
- [x] 1.3 Create a per-regulation OpenSpec change for the Cyber Resilience Act.
- [x] 1.4 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-indexes/805-skill.xml`.
- [ ] 1.6 Add `skills-generator/src/main/resources/skill-references/805-regulations-eu-cyber-resilience-act.xml`.
- [ ] 1.7 Include engineering controls for secure-by-design development, threat modeling, secure defaults, vulnerability management, coordinated disclosure, security update mechanisms, dependency and SBOM evidence, cryptography, authentication and authorization, sensitive-data-safe logging, product security documentation, end-of-support signaling, and release readiness.
- [ ] 1.8 Register skill id `805` and reference `805-regulations-eu-cyber-resilience-act` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.9 Validate changed XML files with `xmllint --noout`.
- [ ] 1.10 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.11 Inspect generated local `.agents/skills/805-regulations-eu-cyber-resilience-act/SKILL.md`.
- [ ] 1.12 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.13 Run `openspec validate --all`.
