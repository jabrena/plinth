title=Mastering Java Memory Leak Detection - Complete Learning Path
type=course
status=published
date=2025-09-17
author=Juan Antonio Breña Moral
version=0.11.0-SNAPSHOT
tags=java, profiling, memory-leak, performance, system-prompts, async-profiler
~~~~~~

🎯 **Master Java memory leak detection through hands-on profiling with the Spring Boot memory leak demo**

---

## 📚 Course Structure

- [Module 1: Foundations](module-1-foundations.html) - 2 hours (Focus: Memory leak theory and setup; Key learning: Understanding leak patterns; profiling infrastructure)
- [Module 2: Profiling](module-2-profiling.html) - 3 hours (Focus: Hands-on profiling mastery; Key learning: 21 profiling options; flamegraph interpretation)
- [Module 3: Analysis](module-3-analysis.html) - 2 hours (Focus: Systematic analysis; Key learning: Evidence documentation; prioritization frameworks)
- [Module 4: Refactoring](module-4-refactoring.html) - 2 hours (Focus: Solution implementation; Key learning: Resource lifecycle patterns; coco=false fix)
- [Module 5: Validation](module-5-validation.html) - 1 hour (Focus: Before/after comparison; Key learning: Quantitative validation; success measurement)

**Total Duration:** 8-12 hours (depending on learning path)

### 📚 Course details

### **Module 1: Memory Leak Foundations and Detection Setup** (2 hours)
**Learning Focus:** Understanding memory leaks and setting up detection infrastructure

**Key Topics:**
- What are memory leaks and why they matter?
- Types of memory leaks in Java applications
- Introduction to the Spring Boot memory leak demo
- Setting up profiling infrastructure with system prompts
- Understanding the `coco=true/false` configuration pattern

**Hands-on Activities:**
- Explore the CocoController vs NoCocoController implementations
- Set up profiling scripts using @161-java-profiling-detect
- Run initial baseline profiling session

**Learning Outcomes:**
- Identify different types of memory leak patterns
- Set up automated profiling environment
- Understand the demo application architecture

---

### **Module 2: Hands-on Profiling with System Prompts** (3 hours)
**Learning Focus:** Using system prompts to systematically collect profiling data

**Key Topics:**
- Deep dive into @161-java-profiling-detect system prompt
- Interactive profiling script walkthrough (21 profiling options)
- Memory leak detection strategies (5-minute vs 30-second profiles)
- JMeter load testing integration for realistic scenarios
- Understanding flamegraph interpretation

**Hands-on Activities:**
- Execute memory allocation profiling (Option 2)
- Run memory leak detection workflow (Option 8)
- Generate JMeter load tests for consistent profiling conditions
- Create comprehensive memory analysis workflow (Option 9)

**Learning Outcomes:**
- Master the interactive profiling script
- Generate meaningful profiling data under load
- Interpret flamegraph visualizations effectively

---

### **Module 3: Analysis and Evidence Collection** (2 hours)
**Learning Focus:** Systematic analysis using @162-java-profiling-analyze

**Key Topics:**
- Systematic analysis framework for profiling data
- Problem categorization and severity assessment
- Evidence documentation with quantitative metrics
- Cross-correlation analysis techniques
- Impact vs Effort prioritization framework

**Hands-on Activities:**
- Analyze flamegraphs to identify memory leak patterns
- Create problem analysis documents following templates
- Develop prioritized solution recommendations
- Document evidence with specific file references

**Learning Outcomes:**
- Systematically analyze profiling results
- Create structured documentation for findings
- Prioritize fixes using Impact/Effort scoring

---

### **Module 4: Refactoring and Solution Implementation** (2 hours)
**Learning Focus:** Implementing fixes based on analysis findings

**Key Topics:**
- Understanding the `coco=false` refactoring strategy
- Thread pool lifecycle management
- Bounded collections implementation
- Resource cleanup patterns (@PreDestroy)
- Deployment verification procedures

**Hands-on Activities:**
- Switch from CocoController to NoCocoController
- Verify code changes are properly applied
- Implement monitoring and alerting
- Test application stability after refactoring

**Learning Outcomes:**
- Apply systematic refactoring strategies
- Implement proper resource management patterns
- Validate refactoring through testing

---

### **Module 5: Validation and Comparison** (1 hour)
**Learning Focus:** Using @164-java-profiling-compare to validate improvements

**Key Topics:**
- Before/after comparison methodology
- Quantitative metrics extraction
- Visual flamegraph comparison techniques
- Success criteria validation
- Documentation of improvements

**Hands-on Activities:**
- Generate post-refactoring profiling reports
- Perform side-by-side flamegraph comparison
- Create comparison analysis documentation
- Validate performance improvement targets

**Learning Outcomes:**
- Rigorously validate performance improvements
- Document quantified results
- Establish ongoing monitoring strategies

---

## 🛠️ Tools and Technologies

### **Primary Tools:**
- **async-profiler v4.1**: Advanced profiling with flamegraph generation
- **JFR (Java Flight Recorder)**: Low-overhead continuous profiling
- **JMeter**: Load testing for realistic profiling scenarios
- **Spring Boot Actuator**: Application monitoring and health checks

### **System Prompts Used:**
- **@161-java-profiling-detect**: Setup and data collection
- **@162-java-profiling-analyze**: Systematic analysis framework
- **@164-java-profiling-compare**: Before/after validation
- **@151-java-performance-jmeter**: Load testing integration

### **Visualization Techniques:**
- **Flamegraphs**: Call stack and allocation visualization
- **Heatmaps**: Temporal analysis of performance hotspots
- **Memory usage charts**: GC retention and heap growth patterns
- **Thread dumps**: Concurrency and threading analysis

---

## 🛠️ System Prompts Integration

This course demonstrates practical usage of three key system prompts:

### **[@161-java-profiling-detect](https://github.com/jabrena/cursor-rules-java/tree/main/.cursor/rules/161-java-profiling-detect.md)**
**Purpose:** Data collection and problem identification
**Usage:** My Java application has performance issues - help me set up comprehensive profiling process using @161-java-profiling-detect and use the location examples/spring-boot-memory-leak-demo/profiler

### **[@162-java-profiling-analyze](https://github.com/jabrena/cursor-rules-java/tree/main/.cursor/rules/162-java-profiling-analyze.md)**
**Purpose:** Systematic analysis and solution development
**Usage:** Analyze the results located in examples/spring-boot-memory-leak-demo/profiler and use the cursor rule @162-java-profiling-analyze

### **[@164-java-profiling-compare](https://github.com/jabrena/cursor-rules-java/tree/main/.cursor/rules/164-java-profiling-compare.md)**
**Purpose:** Before/after validation and improvement measurement
**Usage:** Review if the problems was solved with last refactoring using the reports located in @/results with the cursor rule @164-java-profiling-compare

---

## 🎯 Key Concepts Covered

### **Memory Leak Patterns**
- ✅ Unbounded collection growth
- ✅ Thread pool resource leaks
- ✅ Missing lifecycle management
- ✅ Cache leaks and retention issues

### **Profiling Techniques**
- ✅ async-profiler mastery (21 profiling options)
- ✅ JFR analysis and interpretation
- ✅ Flamegraph visual analysis
- ✅ Load testing integration with JMeter

### **Enterprise Patterns**
- ✅ Bounded collections with graceful degradation
- ✅ Shared resource management
- ✅ `@PreDestroy` lifecycle patterns
- ✅ Monitoring and alerting infrastructure

### **Analysis Methodologies**
- ✅ Problem-driven profiling strategies
- ✅ Impact/Effort prioritization frameworks
- ✅ Cross-correlation analysis techniques
- ✅ Evidence-based documentation

---

## 🔍 Interactive Elements Throughout the Course

### **🧠 Knowledge Checks**
- "Before we continue, can you explain why GC retention grows with active memory leaks?"
- "What would happen if we didn't implement @PreDestroy in our thread pools?"
- "How do you interpret a flamegraph where the canvas height keeps growing?"

### **💡 Learning Reinforcement**
- "Notice how the NoCocoController eliminates the memory leak - that's the power of proper resource lifecycle management!"
- "This connects to our earlier lesson on bounded collections - remember the MAX_OBJECTS pattern?"
- "The 318% memory retention increase we observed demonstrates why systematic profiling is critical!"

### **🎯 Practical Challenges**
- Implement custom bounded collections with error handling
- Design monitoring strategies for production memory leak detection
- Create custom profiling configurations for specific scenarios
- Develop team knowledge transfer materials

---

## 📊 Success Metrics and Validation

### **Technical Success Criteria**
- [ ] Memory leaks successfully detected and resolved
- [ ] Quantified performance improvements documented
- [ ] Systematic profiling workflow mastered
- [ ] Production-ready monitoring strategy developed

### **Learning Validation Methods**
- [ ] Hands-on exercises completed successfully
- [ ] Profiling reports generated and analyzed
- [ ] Documentation created following professional templates
- [ ] Knowledge check questions answered correctly

### **Real-World Application**
- [ ] Techniques applied to actual production applications
- [ ] Team knowledge sharing sessions conducted
- [ ] Performance monitoring integrated into CI/CD pipeline
- [ ] Continuous improvement processes established

---

## 🎓 Course Philosophy

### **Progressive Learning Design**
This course follows the **@behaviour-progressive-learning** pattern:
- **Extract** core concepts from system prompts
- **Structure** content into progressive learning modules
- **Create** interactive exercises with guided solutions
- **Generate** comprehensive courses with multiple paths
- **Provide** assessments and validation checkpoints

---

*"The best time to learn performance optimization was yesterday. The second best time is now."*

**Happy profiling! [Go to the foundations](module-1-foundations.html) 🚀**
