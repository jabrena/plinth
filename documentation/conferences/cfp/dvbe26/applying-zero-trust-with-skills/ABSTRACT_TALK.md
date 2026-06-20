Abstract

**Aplying Zero Trust with Skills that you use**

AI coding agents are rapidly becoming first-class citizens in enterprise software workflows. But as teams scale their use of agent skills — structured instructions that guide AI behavior across tasks like code generation, testing, and refactoring — a critical question emerges: how do you know your skills are safe and actually work as intended?

Skills are executable guidance, not ordinary Markdown. A broken document can confuse an agent. A risky instruction can ask for excessive permissions, fetch remote code, leak data, or hide behavior that reviewers never intended. This talk explores the discipline of agent skill validation and how to build a layered CI pipeline that treats skills as first-class artifacts worthy of the same rigor applied to production code.

The talk will cover in detail:

- Why skill validation requires a defense-in-depth approach: structural correctness, behavioral safety, and supply-chain risk are separate concerns that no single tool covers alone
- Review the market about Skill validators.
- The concrete risk categories each validator addresses:
  - Prompt injection patterns
  - Data exfiltration instructions
  - Insecure credential handling and hardcoded secrets
  - Suspicious command execution
  - System modification and privilege escalation
  - Hidden or obfuscated content
  - Untrusted content and indirect prompt injection
  - Tool poisoning and tool shadowing
  - Excessive agency and resource abuse
  - Description-behavior mismatch
  - Direct financial execution
  - Supply-chain and agent-risk signals

- How to integrate validators in your pipelines
