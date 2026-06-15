Feature: Validate changes from usage of embedded agents inventory skill

Background:
  Given the skill "002-agents-inventory"
  And the inventory sandbox folder "examples/skills/inventory" has no git changes

@acceptance-test
Scenario: Generate embedded agents inventory with every agent asset
  Given the local generated skill path ".agents/skills/002-agents-inventory"
  And the agent assets directory "skills-generator/src/main/resources/skill-references/assets/agents"
  And the requested inventory output path is "examples/skills/inventory/INVENTORY-AGENTS-JAVA.md"
  And any existing report at the requested output path must be overwritten
  When the skill ".agents/skills/002-agents-inventory" is applied to generate the embedded agents inventory
  Then the skill reads "references/002-agents-inventory.md"
  And the generated inventory file exists at "examples/skills/inventory/INVENTORY-AGENTS-JAVA.md"
  And the generated inventory file includes the heading "# Embedded Agents Inventory"
  And the generated inventory file includes the section "## Embedded agents"
  And the generated inventory file includes the section "## Installation target options"
  And the generated inventory file includes the installation targets ".cursor/agents" and ".claude/agents"
  And the generated inventory file includes exactly one row for each embedded agent asset:
    | assetFile                        | agentName                      |
    | robot-business-analyst.md        | robot-business-analyst         |
    | robot-architect.md               | robot-architect                |
    | robot-tech-lead.md               | robot-tech-lead                |
    | robot-no-java.md                 | robot-no-java                  |
    | robot-java-performance.md        | robot-java-performance         |
    | robot-java-coder.md              | robot-java-coder               |
    | robot-java-micronaut-coder.md    | robot-java-micronaut-coder     |
    | robot-java-quarkus-coder.md      | robot-java-quarkus-coder       |
    | robot-java-spring-boot-coder.md  | robot-java-spring-boot-coder   |
  And every agent row in the generated file corresponds to a same-named source file in "skills-generator/src/main/resources/skill-references/assets/agents"
  And no agent asset from "skills-generator/src/main/resources/skill-references/assets/agents" is missing from the generated inventory
  And the generated inventory file does not include agent rows outside the embedded agent assets directory
  And any git changes produced under "examples/skills/inventory" during skill execution and verification are reset
