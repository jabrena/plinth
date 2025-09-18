title=Module 1: Foundations - Project Setup & Build Systems
type=course
status=published
date=2025-09-17
author=MyRobot
version=0.11.0-SNAPSHOT
tags=java, profiling, memory-leak, performance, system-prompts, async-profiler
~~~~~~

## 🎯 Learning Objectives

By the end of this module, you will:

- **Master Maven best practices** using automated system prompts
- **Configure quality dependencies** for enterprise-grade projects
- **Generate professional documentation** automatically
- **Understand the fundamentals** of AI-powered development workflows
- **Apply system prompts effectively** in real-world scenarios

## 📚 Module Overview

**Duration:** 4 hours
**Difficulty:** Beginner to Intermediate
**Prerequisites:** Basic Maven knowledge, Java 21+

This foundational module introduces you to the core system prompts that automate project setup and build system configuration. You'll learn to transform manual, error-prone tasks into automated, consistent workflows.

## 🗺️ Learning Path

### **Lesson 1.1: Understanding System Prompts** (45 minutes)

#### 🎯 **Learning Objectives:**
- Understand what system prompts are and why they matter
- Learn the anatomy of effective Cursor Rules
- Explore the benefits of AI-powered development workflows

#### 📖 **Core Concepts:**

**What are System Prompts?**

System prompts are structured instructions that guide AI assistants to perform specific development tasks consistently and professionally. Think of them as "expert consultants" embedded in your IDE.

**Key Benefits:**
- **Consistency**: Same high-quality output every time
- **Efficiency**: Automate repetitive tasks
- **Learning**: Built-in best practices and explanations
- **Scalability**: Apply across teams and projects

#### 💡 **Knowledge Check:**
*Before we continue, can you think of 3 repetitive tasks in Java development that could benefit from automation?*

**Example System Prompt Structure:**
```markdown
## Role
You are a Senior Java Developer...

## Goal
Your task is to...

## Constraints
- Follow Maven best practices
- Use Java 21+ features
- Ensure backward compatibility

## Output Format
- Generate pom.xml modifications
- Provide explanation of changes
- Include validation steps
```

#### 🔧 **Hands-on Exercise 1.1:**

**Scenario:** You've joined a new team and need to understand their system prompt approach.

1. **Explore the System Prompt:** Open `.cursor/rules/100-java-cursor-rules-list.md`
2. **Analyze Structure:** Identify the Role, Goal, and Constraints sections
3. **Test the Prompt:** Use it to generate the main project documentation

**Expected Outcome:** Understanding of how system prompts structure AI interactions for consistent results.

---

### **Lesson 1.2: Maven Best Practices Automation** (75 minutes)

#### 🎯 **Learning Objectives:**
- Apply Maven best practices using `@110-java-maven-best-practices`
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

**Step 2: Apply System Prompt**
Use the prompt: `Apply in the pom.xml the rule @110-java-maven-best-practices`

**Step 3: Interactive Mode**
Try: `Apply in the pom.xml the rule @110-java-maven-best-practices with the behaviour @behaviour-consultative-interaction`

**Expected Improvements:**
- Updated Java version to 21+
- Proper plugin versions
- Dependency scope optimization
- Property consolidation

#### 💡 **Knowledge Check:**
*What's the difference between using a system prompt in "purist way" vs. with consultative behavior?*

**Answer:** Purist way applies changes automatically, while consultative behavior asks questions and provides options for customization.

---

### **Lesson 1.3: Quality Dependencies Integration** (60 minutes)

#### 🎯 **Learning Objectives:**
- Add essential quality dependencies using `@111-java-maven-dependencies`
- Understand the purpose of JSpecify, Error Prone, NullAway, and VAVR
- Learn interactive vs. non-interactive prompt usage

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
Use: `Add essential Maven dependencies for code quality using @111-java-maven-dependencies`

**Step 3: Specific Addition**
Try: `Add VAVR dependency with the help of @111-java-maven-dependencies and not make any question`

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
- Configure essential Maven plugins using `@112-java-maven-plugins`
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
Use: `Improve the pom.xml using the cursor rule @112-java-maven-plugins`

**Step 2: Selective Application**
Try: `Add Maven Enforcer plugin only from the rule @112-java-maven-plugins without any question`

**Step 3: Validation**
```bash
./mvnw clean verify
```

---

### **Lesson 1.5: Professional Documentation Generation** (75 minutes)

#### 🎯 **Learning Objectives:**
- Generate developer documentation using `@113-java-maven-documentation`
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
Use: `Generate developer documentation with essential Maven commands using @113-java-maven-documentation`

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

**Question 1:** What are the three main benefits of using system prompts in Java development?

**Question 2:** Which system prompt would you use to add Error Prone dependency to a project?

**Question 3:** What's the difference between interactive and non-interactive system prompt usage?

### **Practical Assessment Project**

**Project: "Enterprise Project Setup"**

**Scenario:** You're tasked with setting up a new Java microservice project for your team.

**Requirements:**
1. Create a new Maven project structure
2. Apply Maven best practices using system prompts
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
- System prompts were used effectively throughout

### **Time Investment:**
- **Setup**: 30 minutes
- **Implementation**: 90 minutes
- **Validation & Documentation**: 30 minutes
- **Total**: 2.5 hours

---

## 🚀 Next Steps

**Congratulations!** You've mastered the foundational system prompts for Java project setup and build systems.

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
