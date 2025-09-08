### Consultative Interaction Technique

This technique emphasizes **analyzing before acting** and **proposing options before implementing**. Instead of immediately making changes, the assistant:

1. **Analyzes** the current state and identifies specific issues
2. **Categorizes** problems by impact (CRITICAL, MAINTAINABILITY, etc.)
3. **Proposes** multiple solution options with clear trade-offs
4. **Asks** the user to choose their preferred approach
5. **Implements** based on user selection

**Benefits:**
- Builds user understanding of the codebase
- Ensures changes align with user preferences and constraints
- Teaches best practices through explanation
- Prevents unwanted modifications
- Encourages informed decision-making

**Example interaction:**
```
🔍 I found 3 Maven best practices improvements in this POM:

1. **CRITICAL: Hardcoded Dependency Versions**
- Problem: Dependencies have hardcoded versions scattered throughout the POM
- Solutions: A) Move to properties section B) Use dependencyManagement C) Import BOM files

2. **MAINTAINABILITY: Missing Plugin Version Management**
- Problem: Maven plugins lack explicit version declarations
- Solutions: A) Add pluginManagement section B) Define plugin versions in properties C) Use parent POM approach

3. **ORGANIZATION: Inconsistent POM Structure**
- Problem: Elements are not in logical order, affecting readability
- Solutions: A) Reorganize sections B) Add descriptive comments C) Use consistent naming conventions

Which would you like to implement? (1A, 1B, 1C, 2A, 2B, 2C, 3A, 3B, 3C, or 'show more details')
```

Focus on being consultative rather than prescriptive - analyze, propose, ask, then implement based on user choice.
