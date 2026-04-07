# compatibility-planning Specification

## Purpose
Define the ongoing compatibility policy for this project so contributors can evolve Skills, Agents, AGENTS.md guidance, and OpenSpec workflows without creating avoidable downstream breakage. This specification establishes how compatibility targets are identified, how breaking changes are communicated, and which repository artifacts must stay aligned when compatibility-impacting decisions are made.
## Requirements
### Requirement: Compatibility Surface Definition
The project SHALL maintain a clear, documented compatibility surface aligned with the repository goal and deliverables.

#### Scenario: Compatibility targets are explicit
- **Given** contributors are planning a change that affects generated guidance or project workflows
- **When** they evaluate compatibility impact
- **Then** they identify impacted surfaces across `skills/`, `.cursor/agents`, `AGENTS.md` conventions, and OpenSpec-based planning artifacts
- **And** they document which user-facing contracts are expected to remain stable
- **And** they record any intentionally unsupported legacy behavior as out of scope

### Requirement: Breaking Change Governance
The project SHALL apply explicit governance for breaking changes that affect supported compatibility surfaces.

#### Scenario: Breaking change is proposed
- **Given** a proposed change alters expected behavior for supported consumers
- **When** the change is reviewed
- **Then** the change is marked as breaking
- **And** semantic versioning impact is documented before release
- **And** migration guidance is written for downstream users
- **And** related ADR and/or OpenSpec change records are linked

### Requirement: Ecosystem and Dependency Impact Mapping
The project SHALL evaluate ecosystem and repository dependencies before compatibility-impacting changes are accepted.

#### Scenario: Impact assessment is completed
- **Given** a compatibility-relevant change is in planning
- **When** maintainers perform impact analysis
- **Then** they map affected documentation, CI workflows, examples, and generator modules
- **And** they identify downstream usage assumptions that may fail after the change
- **And** they capture mitigation actions in tasks or migration notes

### Requirement: Documentation and Delivery Alignment
The project SHALL keep public documentation and validated delivery outputs aligned with the compatibility policy.

#### Scenario: Release preparation validates compatibility communication
- **Given** compatibility-impacting work is ready for release
- **When** release preparation is performed
- **Then** `README.md` and relevant getting-started/contributor documentation reflect the current supported workflow
- **And** generated deliverables remain consistent with the documented compatibility surface
- **And** verification commands used by the project continue to pass for the supported pipeline

