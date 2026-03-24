---
name: 151-java-performance-jmeter
description: Use when you need to set up JMeter performance testing for a Java project — including creating the run-jmeter.sh script from the exact template, configuring load tests with loops, threads, and ramp-up, or running performance tests from the project root with custom or default settings. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Run performance tests based on JMeter

Provide a complete JMeter performance testing solution by creating the run-jmeter.sh script from the exact template, making it executable, and configuring the project structure for load testing. Supports custom loops, threads, ramp-up, and environment variable overrides.

**What is covered in this Skill?**

- Create run-jmeter.sh in project root from the exact template (no modifications)
- Project structure: src/test/resources/jmeter/load-test.jmx, target/ for results
- Script options: -l (loops), -t (threads), -r (ramp-up), -g (GUI), -h (help)
- Environment variables: JMETER_LOOPS, JMETER_THREADS, JMETER_RAMP_UP
- Verify JMeter is installed and available before proceeding

**Scope:** Copy the script template verbatim. Do not modify, interpret, or enhance the template content.

## Workflow

1. **Verify JMeter installed** — run `jmeter --version` to confirm JMeter is available in PATH
2. **Create run-jmeter.sh** — copy the exact template from the reference into the project root
3. **Set permissions** — run `chmod +x run-jmeter.sh` and verify with `ls -la run-jmeter.sh`
4. **Configure test plan** — ensure `src/test/resources/jmeter/load-test.jmx` exists for the script to reference
5. **Run and review** — execute `./run-jmeter.sh -h` to verify, then run tests and check `target/jmeter-report/index.html`

## Quick Reference

**Basic script usage:**

```bash
# Run with defaults (1000 loops, 1 thread, 1s ramp-up)
./run-jmeter.sh

# Custom load: 500 loops, 10 threads, 30s ramp-up
./run-jmeter.sh -l 500 -t 10 -r 30

# Override via environment variables
JMETER_LOOPS=500 JMETER_THREADS=5 ./run-jmeter.sh
```

**Expected project structure:**

```
project-root/
├── run-jmeter.sh
├── src/test/resources/jmeter/load-test.jmx
└── target/
    ├── jmeter-results.jtl
    └── jmeter-report/index.html
```

## Constraints

JMeter must be installed and available in PATH. If not available, show a message and exit. Use only the exact template for the run-jmeter.sh script.

- **PREREQUISITE**: Verify JMeter is installed and accessible via `jmeter --version` before creating the script
- **CRITICAL**: Copy the run-jmeter.sh template exactly — do not modify, interpret, or enhance
- **PERMISSION**: Make the script executable with `chmod +x run-jmeter.sh`
- **BEFORE APPLYING**: Read the reference for the exact script template and usage instructions

## When to use this skill

- Review Java code for JMeter performance testing
- Add JMeter support

## Reference

For detailed guidance, examples, and constraints, see [references/151-java-performance-jmeter.md](references/151-java-performance-jmeter.md).
