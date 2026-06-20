Abstract

**The Importance of HITL to Avoid Chaos and EU Regulations for AI**

AI coding agents are rapidly becoming first-class citizens in enterprise software workflows. Teams are already delegating consequential decisions to autonomous agents: generating database migrations, refactoring service boundaries, modifying schemas, producing runbooks, and calling external tools. But as agent autonomy grows, a critical question emerges — who is responsible when something goes wrong?

This talk explores the discipline of Human-in-the-Loop (HITL) design, and how to build AI-assisted workflows that keep humans meaningfully in control without sacrificing the productivity gains that make agents worth adopting in the first place. It also examines the growing body of EU regulation that makes HITL not just a good engineering practice, but a legal requirement for certain classes of systems.

The talk will cover in detail:

- What HITL means in practice: approval gates, confidence thresholds, audit trails, and owner handoffs as first-class engineering artifacts
- High-stakes scenarios in microservices architectures: agent-driven database migrations, schema drift across service boundaries, and cascading failures triggered by automated refactoring — and how the right checkpoints would have caught them early
- How the EU AI Act classifies AI agents and autonomous coding tools, distinguishing between low-risk AI assistance and high-risk workflows that generate code, execute tool calls, or make decisions that directly affect production systems — and what compliance obligations those distinctions carry
- How DORA's operational resilience requirements apply when AI-assisted delivery pipelines become part of critical ICT processes: incident handling, recovery evidence, third-party provider risk, and the human approval gates that must remain in the loop
- Why engineering teams must prepare reviewable evidence — classification signals, tool access controls, prompt and tool-call audit logs, rollback paths, and owner handoffs — before qualified compliance, legal, or risk owners can make a governance decision
- Practical patterns for embedding HITL checkpoints into CI/CD pipelines without turning every deployment into a bottleneck: confidence-gated approvals, structured review prompts, and layered human oversight that scales with risk

The session will demonstrate these concepts through a realistic microservices scenario — a Java enterprise platform with AI-assisted delivery — showing how the absence of human oversight leads to cascading failures, and how a well-designed HITL architecture, grounded in both engineering discipline and regulatory awareness, would have prevented them. Attendees will leave with a concrete framework for moving fast with AI agents without losing control of their systems or their compliance posture.
