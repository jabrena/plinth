title=Mastering Java Enterprise Development with Agent Skills
type=course
status=published
date=2025-09-17
updated=2026-09-26
author=MyRobot
tags=java, skills
~~~~~~

## 🎯 Course Overview

**Transform your Java development workflow** by learning how to select, invoke, and combine Plinth's Java Agent Skills. The course uses progressive, hands-on exercises to improve Maven builds, design, testing, security, modern Java, performance, profiling, and documentation.

Rather than treating a skill as a reusable prompt fragment, each module treats it as an executable workflow with prerequisites, constraints, validation, and explicit outputs.

### 🎓 What You'll Learn

By the end of this course, you'll be able to:

- Select the right Java skill for a concrete engineering objective
- Provide the repository context and trusted inputs each skill needs
- Follow interactive skill workflows without bypassing safeguards
- Validate Maven, code, tests, profiling evidence, and documentation proportionally
- Compose several skills into traceable delivery workflows
- Distinguish generation, analysis, refactoring, and verification responsibilities

### 📚 Course Structure

This course is organized into **7 progressive modules**:

- [Module 1: Foundations](module-1-foundations.html) - Maven project setup with `110-java-maven-best-practices`, `111-java-maven-dependencies`, `112-java-maven-plugins`, and `113-java-maven-documentation`
- [Module 2: Code Quality](module-2-code-quality.html) - Testing and design with `131-java-testing-unit-testing`, `121-java-object-oriented-design`, and `122-java-type-design`
- [Module 3: Secure and Resilient Code](module-3-secure-coding.html) - Security, concurrency, logging, and exceptions with skills 124, 125, 181, and 126
- [Module 4: Modern Java](module-4-modern-java.html) - Generics, functional programming, data-oriented programming, and modern refactoring with skills 128 and 141-144
- [Module 5: Performance](module-5-performance.html) - Load testing and the complete Detect → Analyze → Refactor → Verify profiling lifecycle with skills 151 and 161-164
- [Module 6: Documentation](module-6-documentation.html) - Project documentation and architecture diagrams with `170-java-documentation` and `033-architecture-diagrams`
- [Module 7: Skill Workflows](module-7-advanced-patterns.html) - Skill selection, sequencing, evidence handoffs, validation, and workflow design

**Total Duration:** approximately 34 hours, suitable for a 4-6 week learning path.

### 🚀 Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/jabrena/plinth.git
   cd plinth
   ```

2. Review the generated skills available to your agent:

   ```bash
   find .agents/skills -mindepth 1 -maxdepth 1 -type d | sort
   ```

3. Start with [Module 1: Foundations](module-1-foundations.html).

4. For every exercise:

   - State the engineering objective and relevant file scope.
   - Name the skill explicitly.
   - Answer required workflow questions.
   - Review proposed changes and generated evidence.
   - Run the validation required by the skill and repository.

### 📈 Progress Tracking

- [ ] Module 1: Maven foundations
- [ ] Module 2: Testing and design
- [ ] Module 3: Secure and resilient code
- [ ] Module 4: Modern Java
- [ ] Module 5: Performance and profiling
- [ ] Module 6: Documentation and diagrams
- [ ] Module 7: Multi-skill workflows

---

*This course is part of [Plinth](https://github.com/jabrena/plinth), a collection of Java Agent Skills for repeatable software-engineering workflows.*
