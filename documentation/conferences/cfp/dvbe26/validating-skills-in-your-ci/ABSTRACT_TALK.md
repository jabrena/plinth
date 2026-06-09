Abstract

**Validationg Skills in your CI**

AI coding agents are rapidly becoming first-class citizens in enterprise software workflows. But as teams scale their use of agent skills — structured instructions that guide AI behavior across tasks like code generation, testing, and refactoring — a critical question emerges: how do you know your skills actually work as intended?
This talk explores the discipline of agent skill validation, and how to build robust CI pipelines that treat skills as first-class artifacts worthy of the same rigor applied to production code.

The talk will cover in detail:

- What skill validation means and why it matters: correctness, safety, and behavioral consistency
- A multi-tool validation strategy combining static analysis with behavioral inspection
- How to integrate skill validation into GitHub Actions pipelines alongside conventional pre-commit hooks and commit message linting

The session will demonstrate these concepts through a real-world CI pipeline that validates agent skills across multiple dimensions — from schema conformance to behavioral policy enforcement — giving teams the confidence to ship AI-assisted workflows at enterprise scale.