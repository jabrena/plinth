## MODIFIED Requirements

### Requirement: Business analyst missions

`robot-business-analyst` SHALL own issue creation while remaining separate from technical implementation. `robot-business-analyst` no longer owns a dedicated read-only alignment-review command; per GitHub issue #1084, the `/review-alignment` command and its supporting "Review alignment and readiness" goal mission are retired.

#### Scenario: Preserve business analyst responsibilities during role-boundary refactor

- **WHEN** planning and OpenSpec creation ownership moves from `robot-tech-lead` to `robot-architect`
- **THEN** `robot-business-analyst` continues owning issue quality and requirements traceability
- **AND** those responsibilities are not transferred to `robot-architect`
- **AND** `robot-business-analyst` remains separate from technical implementation

#### Scenario: Retire the read-only alignment-review mission

- **WHEN** `robot-business-analyst`'s agent definition is inspected
- **THEN** its goal no longer contains a "Review alignment and readiness" mission
- **AND** its output-format and constraints no longer reference alignment or readiness review
- **AND** no other command or agent source still routes work to `robot-business-analyst` through the retired `/review-alignment` command
