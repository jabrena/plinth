Feature: Validate changes from usage of Quality Attribute Discovery skill

Background:
  Given the skill "025-quality-attribute-discovery"

@acceptance-test
Scenario: Ground candidate quality attributes in upstream evidence and prioritize them
  Given the user request is "Discover the quality attributes for this order-status problem using @025-quality-attribute-discovery"
  And the local generated skill path ".agents/skills/025-quality-attribute-discovery"
  When the skill ".agents/skills/025-quality-attribute-discovery" is applied to the quality-attribute-discovery request
  Then the skill reads "references/025-quality-attribute-discovery.md"
  And the recommendation grounds each candidate quality attribute in evidence from the problem frame, root causes, assumptions, or context map
  And the recommendation prioritizes candidate quality attributes by stakeholder impact and risk if unmet
  And the recommendation avoids an unmotivated, generic non-functional-requirements checklist

@acceptance-test
Scenario: Stop at the discovery list and hand off the architecture decision
  Given the user request is "Identify quality attributes before we decide on an architecture for this order-status problem using @025-quality-attribute-discovery"
  And the local generated skill path ".agents/skills/025-quality-attribute-discovery"
  When the skill ".agents/skills/025-quality-attribute-discovery" reviews the request
  Then the skill reads "references/025-quality-attribute-discovery.md"
  And the recommendation stops at a prioritized discovery list without selecting an architecture approach
  And the recommendation states that "030-architecture-adr-general", "031-architecture-adr-functional-requirements", "032-architecture-adr-non-functional-requirements", or "/explore-design" own the resulting architecture decisions
  And the recommendation does not duplicate ADR or design-direction content

@acceptance-test
Scenario: Flag an unclear priority for a clarifying question instead of inventing a ranking
  Given the user request is "Discover quality attributes for this problem without enough evidence on growth projections using @025-quality-attribute-discovery"
  And the local generated skill path ".agents/skills/025-quality-attribute-discovery"
  When the skill ".agents/skills/025-quality-attribute-discovery" evaluates the incomplete evidence
  Then the skill reads "references/025-quality-attribute-discovery.md"
  And the recommendation does not invent a quality attribute or priority from the incomplete evidence
  And the recommendation flags the gap for a clarifying question
