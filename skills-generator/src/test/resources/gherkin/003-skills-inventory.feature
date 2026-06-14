Feature: Validate changes from usage of Java skills inventory skill

Background:
  Given the skill "003-skills-inventory"
  And the inventory sandbox folder "examples/skills/inventory" has no git changes

@acceptance-test
Scenario: Generate skills inventory with every skill declared in skills.xml
  Given the local generated skill path ".agents/skills/003-skills-inventory"
  And the skill inventory source file "skills-generator/src/main/resources/skills.xml"
  And the requested inventory output path is "examples/skills/inventory/INVENTORY-SKILLS-JAVA.md"
  And any existing report at the requested output path must be overwritten
  When the skill ".agents/skills/003-skills-inventory" is applied to generate the Java skills inventory
  Then the skill reads "references/003-skills-inventory.md"
  And the skill reads "skills-generator/src/main/resources/skills.xml"
  And the generated inventory file exists at "examples/skills/inventory/INVENTORY-SKILLS-JAVA.md"
  And the generated inventory file includes the heading "# Skills for Java"
  And the generated inventory file includes the section "## Inventory"
  And the generated inventory file includes the section "## Installation"
  And the generated inventory file includes one inventory row for every skill declared in "skills-generator/src/main/resources/skills.xml"
  And each generated skill row uses the skillId attribute when the source skill declares one
  And each generated skill row uses the single reference name when the source skill does not declare a skillId attribute
  And no skill declared in "skills-generator/src/main/resources/skills.xml" is missing from "examples/skills/inventory/INVENTORY-SKILLS-JAVA.md"
  And the generated inventory file does not include skill rows absent from "skills-generator/src/main/resources/skills.xml"
  And the generated inventory file includes every skill id from the source inventory, including the installer, architecture, Java, framework, technology, and regulation skill groups
  And any git changes produced under "examples/skills/inventory" during skill execution and verification are reset
