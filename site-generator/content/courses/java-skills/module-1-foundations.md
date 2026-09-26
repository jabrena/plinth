title=Module 1: Foundations - Project Setup & Build Systems
type=course
status=published
date=2025-09-17
updated=2026-09-26
author=MyRobot
tags=java, skills
~~~~~~

## 🎯 Learning Objectives

By the end of this module, you will:

- **Master Maven best practices** using automated Agent Skills
- **Configure quality dependencies** for enterprise-grade projects
- **Generate professional documentation** automatically
- **Understand the fundamentals** of AI-powered development workflows
- **Apply Agent Skills effectively** in real-world scenarios

## 📚 Module Overview

**Duration:** 4 hours
**Difficulty:** Beginner to Intermediate
**Prerequisites:** Basic Maven knowledge, Java 25

This foundational module introduces you to the core Agent Skills that automate project setup and build system configuration. You'll learn to transform manual, error-prone tasks into automated, consistent workflows.

## 🗺️ Learning Path

### **Lesson 1.1: Understanding Agent Skills** (45 minutes)

#### 🎯 **Learning Objectives:**
- Understand what Agent Skills are and why they matter
- Learn the anatomy of effective Agent Skills
- Explore the benefits of AI-powered development workflows

#### 📖 **Core Concepts:**

**What are Agent Skills?**

Agent Skills are versioned instruction packages that guide an AI assistant through a bounded engineering workflow. A skill can define prerequisites, questions, constraints, references, scripts, outputs, and validation steps.

**Key Benefits:**
- **Consistency**: Same high-quality output every time
- **Efficiency**: Automate repetitive tasks
- **Learning**: Built-in best practices and explanations
- **Scalability**: Apply across teams and projects

#### 💡 **Knowledge Check:**
*Before we continue, can you think of 3 repetitive tasks in Java development that could benefit from automation?*

**Example Agent Skill Structure:**
```markdown
---
name: 110-java-maven-best-practices
description: Review and improve Maven project configuration.
---

## Workflow
1. Validate the project.
2. Inspect pom.xml.
3. Propose scoped improvements.
4. Apply the approved changes.
5. Verify the build.

## References
- Read references/110-java-maven-best-practices.md before editing.
```

#### 🔧 **Hands-on Exercise 1.1:**

**Scenario:** You've joined a new team and need to understand its Agent Skill workflow.

1. **Explore the Agent Skill:** Open `.agents/skills/110-java-maven-best-practices/SKILL.md`
2. **Analyze Structure:** Identify its trigger, workflow, constraints, references, and validation requirements
3. **Inspect Supporting Material:** Read only the references explicitly required by the skill
4. **Test the Skill:** Ask the agent to review a sample `pom.xml` with `110-java-maven-best-practices`

**Expected Outcome:** Understanding of how Agent Skills structure AI interactions for consistent results.

---

### **Lesson 1.2: Maven Best Practices Automation** (75 minutes)

#### 🎯 **Learning Objectives:**
- Apply Maven best practices using `110-java-maven-best-practices`
- Understand modern Maven project structure
- Learn to validate and optimize `pom.xml` configurations

#### 📖 **Core Concepts:**

**Maven Best Practices Include:**
- **Project Structure**: Standard directory layout
- **Dependency Management**: Version control and scope optimization
- **Plugin Configuration**: Essential plugins with proper versions
- **Property Management**: Centralized configuration
- **Profile Usage**: Environment-specific builds

#### 🔧 **Hands-on Exercise 1.2:**

**Scenario:** You inherit a legacy Maven project with outdated practices.

**Step 1: Assessment**
```bash
# Navigate to the problematic project
cd examples/maven-demo-ko
```

**Step 2: Apply Agent Skill**
Use: `Review and improve this pom.xml with the 110-java-maven-best-practices skill.`

**Step 3: Follow the Skill Workflow**
Answer the skill's questions, review the proposed Maven changes, and approve only the options that match the project.

**Expected Improvements:**
- Updated Java version to the repository's Java 25 baseline
- Proper plugin versions
- Dependency scope optimization
- Property consolidation

#### 💡 **Knowledge Check:**
*Why should you follow a skill's required questions instead of asking the agent to bypass them?*

**Answer:** Required questions capture project-specific decisions and safeguards. Bypassing them can produce incorrect or unsafe changes.

---

### **Lesson 1.3: Quality Dependencies Integration** (60 minutes)

#### 🎯 **Learning Objectives:**
- Add essential quality dependencies using `111-java-maven-dependencies`
- Understand the purpose of JSpecify, Error Prone, NullAway, and VAVR
- Learn how interactive skill decisions constrain dependency changes

#### 📖 **Core Concepts:**

**Essential Quality Dependencies:**

1. **JSpecify**: Null safety annotations
2. **Error Prone**: Compile-time bug detection
3. **NullAway**: Fast null pointer analysis
4. **VAVR**: Functional programming utilities

#### 🔧 **Hands-on Exercise 1.3:**

**Scenario:** Enhance a working project with quality dependencies.

**Step 1: Setup**
```bash
cd examples/maven-demo
```

**Step 2: Interactive Approach**
Use: `Evaluate quality dependencies for this Maven project with the 111-java-maven-dependencies skill.`

**Step 3: Specific Addition**
Try: `Evaluate whether Vavr is justified for this project with the 111-java-maven-dependencies skill.`

**Step 4: Validation**
```bash
./mvnw clean compile
```

#### 💡 **Deep Dive: Why These Dependencies?**

- **JSpecify**: Prevents NullPointerException at compile time
- **Error Prone**: Catches common Java mistakes (e.g., string comparison with ==)
- **NullAway**: Fast static analysis for null safety
- **VAVR**: Immutable collections and functional programming patterns

---

### **Lesson 1.4: Maven Plugins Mastery** (45 minutes)

#### 🎯 **Learning Objectives:**
- Configure essential Maven plugins using `112-java-maven-plugins`
- Understand plugin lifecycle and execution
- Learn selective plugin application

#### 📖 **Core Concepts:**

**Essential Maven Plugins:**
- **Maven Compiler Plugin**: Java compilation configuration
- **Maven Surefire Plugin**: Unit test execution
- **Maven Enforcer Plugin**: Build environment validation
- **JaCoCo Plugin**: Code coverage analysis
- **SpotBugs Plugin**: Static analysis

#### 🔧 **Hands-on Exercise 1.4:**

**Step 1: Interactive Enhancement**
Use: `Review this pom.xml with the 112-java-maven-plugins skill and recommend only justified plugins.`

**Step 2: Selective Application**
Try: `Evaluate and configure Maven Enforcer with the 112-java-maven-plugins skill.`

**Step 3: Validation**
```bash
./mvnw clean verify
```

---

### **Lesson 1.5: Professional Documentation Generation** (75 minutes)

#### 🎯 **Learning Objectives:**
- Generate developer documentation using `113-java-maven-documentation`
- Create comprehensive README-DEV.md files
- Understand documentation-driven development

#### 📖 **Core Concepts:**

**Professional Documentation Includes:**
- **Project Overview**: Purpose and architecture
- **Build Commands**: Development workflow
- **Testing Strategy**: How to run and write tests
- **Deployment Guide**: Production considerations
- **Contributing Guidelines**: Team collaboration

#### 🔧 **Hands-on Exercise 1.5:**

**Step 1: Generate Documentation**
Use: `Generate Maven developer documentation with the 113-java-maven-documentation skill.`

**Step 2: Review Generated Content**
- Examine the generated `README-DEV.md`
- Understand the Maven command explanations
- Note the professional formatting

**Step 3: Customization**
- Add project-specific sections
- Include team-specific workflows

---

## 🏆 Module Assessment

### **Knowledge Validation Checkpoint**

**Question 1:** What are the three main benefits of using Agent Skills in Java development?

**Question 2:** Which Agent Skill would you use to add Error Prone dependency to a project?

**Question 3:** What information should be confirmed before an interactive skill changes a build?

### **Practical Assessment Project**

**Project: "Enterprise Project Setup"**

**Scenario:** You're tasked with setting up a new Java microservice project for your team.

**Requirements:**
1. Create a new Maven project structure
2. Apply Maven best practices using Agent Skills
3. Add all quality dependencies
4. Configure essential plugins
5. Generate comprehensive documentation

**Deliverables:**
- Working `pom.xml` with all enhancements
- Generated `README-DEV.md` with build instructions
- Validation that project builds successfully

**Success Criteria:**
- Project builds without warnings
- All quality tools are properly configured
- Documentation is comprehensive and professional
- Agent Skills were used effectively and their validation requirements were completed

### **Time Investment:**
- **Setup**: 30 minutes
- **Implementation**: 90 minutes
- **Validation & Documentation**: 30 minutes
- **Total**: 2.5 hours

---

## 🚀 Next Steps

**Congratulations!** You've mastered the foundational Agent Skills for Java project setup and build systems.

**What You've Accomplished:**
- ✅ Automated Maven project configuration
- ✅ Integrated quality dependencies and plugins
- ✅ Generated professional documentation
- ✅ Established efficient development workflows

**Ready for the next level?**

👉 **[Continue to Module 2: Code Quality →](module-2-code-quality.html)**

**In Module 2, you'll learn to:**
- Generate comprehensive unit tests automatically
- Apply object-oriented design principles
- Implement type-safe design patterns
- Create robust, maintainable code structures

---

## 📚 Additional Resources

- **[Maven Best Practices Guide](https://maven.apache.org/guides/best-practices.html)**

---

*Continue your learning journey with structured, progressive modules that build upon these foundational concepts.*
