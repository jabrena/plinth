---
name: 161-java-profiling-detect
description: Use when you need to set up Java application profiling to detect and measure performance issues — including automated async-profiler v4.0 setup, problem-driven profiling (CPU, memory, threading, GC, I/O), interactive profiling scripts, JFR integration with Java 25 (JEP 518, JEP 520), or collecting profiling data with flamegraphs and JFR recordings. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java Profiling Workflow / Step 1 / Collect data to measure potential issues

Set up the Java profiling detection phase: automated environment setup with async-profiler v4.0, problem-driven interactive profiling scripts, and comprehensive data collection for CPU hotspots, memory leaks, lock contention, GC issues, and I/O bottlenecks. Uses JEP 518 (Cooperative Sampling) and JEP 520 (Method Timing) for reduced overhead.

**What is covered in this Skill?**

- Run application with profiling JVM flags (run-java-process-for-profiling.sh)
- Interactive profiling script (profiler/scripts/profile-java-process.sh) — copy exact template
- Directory structure: profiler/scripts/, profiler/results/, profiler/current/
- Automated OS/architecture detection and async-profiler download
- CPU, memory, lock, GC, I/O profiling modes
- Flamegraph and JFR output with timestamped results

**Scope:** Use the exact bash script templates without modification or interpretation.

## Workflow

1. **Set up directory structure** — Create `profiler/scripts/`, `profiler/results/`, and `profiler/current/` directories in the project root
2. **Create the application runner script** — Copy `run-java-process-for-profiling.sh` exactly from the reference template to launch the Java process with profiling-compatible JVM flags
3. **Create the interactive profiling script** — Copy `profiler/scripts/profile-java-process.sh` from the reference to enable problem-driven profiling (CPU, memory, lock, GC, I/O)
4. **Run the application with profiling flags** — Execute `./run-java-process-for-profiling.sh` to start the Java process with async-profiler-compatible JVM options
5. **Attach the profiler** — Run `./profiler/scripts/profile-java-process.sh` to interactively select a profiling mode and collect flamegraphs or JFR recordings

## Quick Reference

Key JVM flags required for async-profiler compatibility:

```bash
JVM_FLAGS=(
    "-Xms512m"
    "-Xmx512m"
    "-XX:+UnlockDiagnosticVMOptions"
    "-XX:+DebugNonSafepoints"
    "-XX:+PreserveFramePointer"
)
```

Project directory layout:

```text
your-project/
├── run-java-process-for-profiling.sh
└── profiler/
    ├── scripts/profile-java-process.sh
    ├── results/          # flamegraphs (.html) and JFR (.jfr)
    └── current/          # symlink to active profiler version
```

## Constraints

Copy bash scripts exactly from templates. Ensure JVM flags are applied for profiling compatibility. Verify Java processes are running before attaching profiler.

- **CRITICAL**: Copy the bash script templates exactly — do not modify, interpret, or enhance
- **SETUP**: Create profiler directory structure: profiler/scripts, profiler/results
- **EXECUTABLE**: Make scripts executable with chmod +x
- **BEFORE APPLYING**: Read the reference for exact script templates and setup instructions

## When to use this skill

- Review Java code for profiling
- Add profiling support

## Reference

For detailed guidance, examples, and constraints, see [references/161-java-profiling-detect.md](references/161-java-profiling-detect.md).
