title=What's new in Plinth 0.19.0?
date=2026-09-28
type=post
tags=blog,skills,java,agents,commands,openspec,benchmark,regulations
author=Juan Antonio Breña Moral
status=published
~~~~~~

`Plinth` is an AI-native engineering toolkit for the modern Java Enterprise SDLC, built around reusable `Commands`, `Agents`, `Skills`, and `MCP Servers`. You can use this project to provide an `AI-native workflow` to your `Java Engineering teams`, or just use it to refactor aspects of your project with one of the most popular `Agent Skill collections for Java Enterprise development` in marketplaces like [Skills.sh](https://www.skills.sh/).

---

This release focuses on three things. First, a new `/onboarding` command prepares a repository for issue-driven work, so the workflow introduced in `0.18.0` starts from a known, unambiguous state. Second, requirement discovery gets simpler: the `ISO/IEC 25010:2023 quality model` becomes a first-class engineering review skill, replacing the two older ADR-based requirement skills. Third, the benchmark reaches Part 2, isolating what the orchestration command adds once a written OpenSpec plan already exists. The idea behind all three is the same one that guided the previous release: `"better code generation begins before code generation"` — and it also begins with an honest measurement of what each step actually buys. In general, this is a small, operational release covering the period between [`JCConf 2026`](https://jcconf.tw/2026/) and [`Devoxx BE 2026`](https://devoxx.be/).

Thanks to our community members in [`Des Moines`](https://www.google.com/maps/place/Des+Moines), [`Singapore`](https://www.google.com/maps/search/?api=1&query=Singapore), [`Taipei`](https://www.google.com/maps/place/Taipei), [`Madrid`](https://www.google.com/maps/place/Madrid), and [`Shanghai`](https://www.google.com/maps/search/?api=1&query=Shanghai). 👋👋👋

This article is divided into the following sections:

- [Community first!](#community-first)
- [What are the top 10 skills from this project on Skills.sh?](#what-are-the-top-10-skills-from-this-project-in-skillssh)
- [Onboarding a repository before issue-driven work](#onboarding-a-repository-before-issue-driven-work)
- [Reviewing quality attributes with ISO/IEC 25010:2023](#reviewing-quality-attributes-with-iso-iec-25010-2023)
- [Comparing Plinth commands with OpenSpec and Spec Kit](#comparing-plinth-commands-with-openspec-and-spec-kit)
- [Testing the workflow with a reproducible benchmark, Part 2](#testing-the-workflow-with-a-reproducible-benchmark-part-2)
- [How was the experience in JCConf 2026?](#jcconf-2026)
- [Do you still have questions about the project?](#doubts)
- [Next steps](#next-steps)

If you have questions about the project, how to customize it for your team, how to use the skills in daily work, or how to solve tooling issues, use [`GitHub Discussions`](https://github.com/jabrena/plinth/discussions).

**Help this project grow:** [If this project helps your team, become a sponsor.](https://github.com/sponsors/jabrena)

<a id="community-first"></a>

## Community first!

In this release, I want to thank [`Leandro Loureiro`](https://github.com/lealoureiro) and [`Sangwon Park`](https://github.com/wipheg) for contributing benchmark samples. Benchmark Part 2 took roughly `4 weeks` to collect samples across Claude Code, Codex, and Grok and to analyze the results, growing from `54` samples in Part 1 to `217` samples in Part 2.

I also want to thank [`SecurO`](https://secur0.com/en), the cybersecurity company behind the largest community of ethical hackers in Spain, for their security expertise and the insights that helped improve this project. Many thanks to [Javier Juárez Zarruk](https://www.linkedin.com/in/javier-juarez-zarruk/), [Daniel Ximenez](https://www.linkedin.com/in/daniel-ximenez/), and [Arnau Cebrián](https://www.linkedin.com/in/arnau-cebri%C3%A1n-i-ortega-a65360211) for the support.

[![](/plinth/images/2026/9/secur0-logo.png)](https://secur0.com/en)

If you would like to participate, review the open issues labeled [`good first issue`](https://github.com/jabrena/plinth/issues?q=is%3Aissue%20state%3Aopen%20label%3A%22good%20first%20issue%22), propose improvements, test the workflow with another agent tool, or share your experience in [`GitHub Discussions`](https://github.com/jabrena/plinth/discussions).

<a id="what-are-the-top-10-skills-from-this-project-in-skillssh"></a>

## What are the top 10 skills from this project on Skills.sh?

The [Skills.sh registry](https://www.skills.sh/jabrena/plinth) reports `29.1K` installs in total — roughly `48%` growth since the [`0.18.0` release](https://jabrena.github.io/plinth/blog/2026/08/release-0.18.0.html#what-are-the-top-10-skills-from-this-project-in-skillssh). Compared with `0.18.0`, these are the current top 10 skills among Skills.sh users:

<table>
  <thead>
    <tr>
      <th>Plinth rank&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
      <th>Skills.sh Search</th>
      <th>Skills.sh Search rank</th>
      <th>Skill</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>#1</code> ➡️ <code>=</code></td>
      <td><a href="https://www.skills.sh/search?q=maven">Maven</a></td>
      <td><code>#2</code></td>
      <td><a href="https://www.skills.sh/jabrena/plinth/110-java-maven-best-practices"><code>110-java-maven-best-practices</code></a></td>
    </tr>
    <tr>
      <td><code>#2</code> ➡️ <code>=</code></td>
      <td><a href="https://www.skills.sh/search?q=java%20object%20oriented">Java object oriented</a></td>
      <td><code>#1</code></td>
      <td><a href="https://www.skills.sh/jabrena/plinth/121-java-object-oriented-design"><code>121-java-object-oriented-design</code></a></td>
    </tr>
    <tr>
      <td><code>#3</code> ➡️ <code>=</code></td>
      <td><a href="https://www.skills.sh/search?q=java%20security">Java security</a></td>
      <td><code>#28</code></td>
      <td><a href="https://www.skills.sh/jabrena/plinth/124-java-secure-coding"><code>124-java-secure-coding</code></a></td>
    </tr>
    <tr>
      <td><code>#4</code> ➡️ <code>=</code></td>
      <td><a href="https://www.skills.sh/search?q=java%20unit%20testing">Java unit testing</a></td>
      <td><code>#1</code></td>
      <td><a href="https://www.skills.sh/jabrena/plinth/131-java-testing-unit-testing"><code>131-java-testing-unit-testing</code></a></td>
    </tr>
    <tr>
      <td><code>#5</code> ➡️ <code>=</code></td>
      <td><a href="https://www.skills.sh/search?q=java%20refactoring">Java refactoring</a></td>
      <td><code>#3</code></td>
      <td><a href="https://www.skills.sh/jabrena/plinth/141-java-refactoring-with-modern-features"><code>141-java-refactoring-with-modern-features</code></a></td>
    </tr>
    <tr>
      <td><code>#6</code> ↗️ <code>+2</code></td>
      <td><a href="https://www.skills.sh/search?q=maven">Maven</a></td>
      <td><code>#3</code></td>
      <td><a href="https://www.skills.sh/jabrena/plinth/111-java-maven-dependencies"><code>111-java-maven-dependencies</code></a></td>
    </tr>
    <tr>
      <td><code>#7</code> 🆕</td>
      <td><a href="https://www.skills.sh/search?q=java%20type%20design">Java type design</a></td>
      <td><code>#1</code></td>
      <td><a href="https://www.skills.sh/jabrena/plinth/122-java-type-design"><code>122-java-type-design</code></a></td>
    </tr>
    <tr>
      <td><code>#8</code> ↘️ <code>-2</code></td>
      <td><a href="https://www.skills.sh/search?q=java%20concurrency">Java concurrency</a></td>
      <td><code>#2</code></td>
      <td><a href="https://www.skills.sh/jabrena/plinth/125-java-concurrency"><code>125-java-concurrency</code></a></td>
    </tr>
    <tr>
      <td><code>#9</code> ➡️ <code>=</code></td>
      <td><a href="https://www.skills.sh/search?q=spring%20boot">Spring Boot</a></td>
      <td><code>#31</code></td>
      <td><a href="https://www.skills.sh/jabrena/plinth/302-frameworks-spring-boot-rest"><code>302-frameworks-spring-boot-rest</code></a></td>
    </tr>
    <tr>
      <td><code>#10</code> 🆕</td>
      <td><a href="https://www.skills.sh/search?q=spring%20boot">Spring Boot</a></td>
      <td><code>#32</code></td>
      <td><a href="https://www.skills.sh/jabrena/plinth/301-frameworks-spring-boot-core"><code>301-frameworks-spring-boot-core</code></a></td>
    </tr>
  </tbody>
</table>

**Note:** The `Skills.sh Search rank` column shows the skill's position inside that `Skills.sh` search category when results are sorted by install count.

Two changes stand out. `@122-java-type-design` enters the table for the first time, and `Spring Boot` now has two skills in the top 10 — `@301-frameworks-spring-boot-core` joins `@302-frameworks-spring-boot-rest` — which suggests framework-specific guidance is becoming as relevant to users as the core Java skills. `@142-java-functional-programming` and `@128-java-generics` leave the top 10 this time, although both remain close behind.

<a id="onboarding-a-repository-before-issue-driven-work"></a>

## Onboarding a repository before issue-driven work

The `0.18.0` release described a complete command sequence from a raw issue to an archived OpenSpec change. That sequence assumes two things about the repository it runs in: that agents have root guidance describing how the project works, and that there is exactly one OpenSpec project to write changes into. When either assumption fails, the first command in the chain pays the cost — an agent guesses where specifications live, or two OpenSpec directories quietly diverge.

The new `/onboarding` command makes those prerequisites explicit and establishes:

<table>
  <thead>
    <tr>
      <th>Prerequisite</th>
      <th>When present</th>
      <th>When missing</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Root <code>AGENTS.md</code></td>
      <td>Preserved unchanged</td>
      <td>Created by delegating to <a href="https://www.skills.sh/jabrena/plinth/200-agents-md"><code>@200-agents-md</code></a></td>
    </tr>
    <tr>
      <td>Exactly one OpenSpec project</td>
      <td>Preserved, wherever it is located</td>
      <td>Initialized by delegating to <a href="https://www.skills.sh/jabrena/plinth/042-planning-openspec"><code>@042-planning-openspec</code></a>, defaulting to <code>documentation/openspec</code></td>
    </tr>
    <tr>
      <td>More than one OpenSpec project</td>
      <td colspan="2">Reports every conflicting path as technical debt and stops before any change</td>
    </tr>
  </tbody>
</table>

The command is deliberately conservative. It discovers every `openspec` directory recursively before touching anything, never modifies an existing `AGENTS.md` or OpenSpec project, rejects result paths that escape the repository, and never runs its two interactive delegations concurrently. Running it again on an onboarded repository performs no writes and simply reports where the OpenSpec project lives. That makes `/onboarding` safe to run as the first step on any repository — new or existing — before the rest of the workflow begins.

<a id="reviewing-quality-attributes-with-iso-iec-25010-2023"></a>

## Reviewing quality attributes with ISO/IEC 25010:2023

The `0.18.0` release introduced `/explore-problem`, which investigates an issue through five lenses, including [`@025-quality-attribute-discovery`](https://www.skills.sh/jabrena/plinth/025-quality-attribute-discovery). With that lens in place, two older skills — `@031-architecture-adr-functional-requirements` and `@032-architecture-adr-non-functional-requirements` — were covering the same ground through a different entry point. This release removes them: requirement discovery now lives in `/explore-problem` (`@021`–`@025`), and structured quality-attribute review lives in a new skill.

The new [`@814-regulations-iso-25010`](https://www.skills.sh/jabrena/plinth/814-regulations-iso-25010) skill runs a structured, repeatable review of a Java enterprise system against the ISO/IEC 25010:2023 product quality model, covering all nine quality characteristics:

<table>
  <thead>
    <tr>
      <th>Characteristic</th>
      <th>Example Java engineering evidence</th>
    </tr>
  </thead>
  <tbody>
    <tr><td>Functional Suitability</td><td>Acceptance tests traced to requirements</td></tr>
    <tr><td>Performance Efficiency</td><td>Load and soak test results</td></tr>
    <tr><td>Compatibility</td><td>API contracts and contract tests</td></tr>
    <tr><td>Interaction Capability</td><td>Error responses and API usability</td></tr>
    <tr><td>Reliability</td><td>Circuit breakers, retries, and timeouts</td></tr>
    <tr><td>Security</td><td>Authentication and authorization implementation</td></tr>
    <tr><td>Maintainability</td><td>Module boundaries and static analysis output</td></tr>
    <tr><td>Flexibility</td><td>Externalized configuration</td></tr>
    <tr><td>Safety</td><td>Operational guardrails and fail-safe behavior</td></tr>
  </tbody>
</table>

It ships with a questionnaire, a report template, examples, and an acceptance prompt, and it produces an engineering review report rather than running an interactive ADR discovery session. Each finding is classified as a confirmed gap, a potential gap, or no identified concern, and is handed to an explicit owner — architecture, product, security, platform, or operations — when it needs a decision beyond engineering review.

Like the rest of the `8xx` regulations family, the skill is careful about what it is not: it is not certification advice, a compliance decision, or an audit conclusion. It helps a Java team make quality visible and reviewable, and leaves the formal determination to qualified owners.

In upcoming releases, the skill [`@814-regulations-iso-25010`](https://www.skills.sh/jabrena/plinth/814-regulations-iso-25010) will replace [`@025-quality-attribute-discovery`](https://www.skills.sh/jabrena/plinth/025-quality-attribute-discovery) within `/explore-problem`.

<a id="comparing-plinth-commands-with-openspec-and-spec-kit"></a>

## Comparing Plinth commands with OpenSpec and Spec Kit

The [`0.18.0` article](https://jabrena.github.io/plinth/blog/2026/08/release-0.18.0.html#comparing-plinth-commands-with-openspec-and-spec-kit) compared `Plinth commands` against `OpenSpec` and `Spec Kit` phases. `0.19.0` adds `/onboarding` at the start of that mapping:

<table>
  <thead>
    <tr>
      <th>Plinth Command</th>
      <th>OpenSpec phase</th>
      <th>Spec Kit phase</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>/onboarding</code></td>
      <td>Project setup<br><code>openspec init</code></td>
      <td><code>specify init</code>, <code>/speckit.constitution</code></td>
    </tr>
    <tr>
      <td><code>/update-issue</code></td>
      <td>Issue intake<br><code>openspec list</code></td>
      <td><code>/speckit.specify</code> input</td>
    </tr>
    <tr>
      <td><code>/explore-problem</code></td>
      <td>— (precedes OpenSpec)</td>
      <td><code>/speckit.specify</code> input, <code>/speckit.clarify</code></td>
    </tr>
    <tr>
      <td><code>/create-acceptance-criteria</code></td>
      <td>— (precedes OpenSpec)</td>
      <td><code>/speckit.clarify</code>, <code>/speckit.checklist</code></td>
    </tr>
    <tr>
      <td><code>/create-spec</code></td>
      <td>Proposal and Specification<br><code>openspec new change &lt;change-name&gt;</code>, <code>openspec show &lt;change-name&gt;</code></td>
      <td><code>/speckit.specify</code> plus <code>/speckit.checklist</code></td>
    </tr>
    <tr>
      <td><code>/explore-design</code></td>
      <td>Task planning and alignment review<br><code>openspec validate --all</code></td>
      <td><code>/speckit.plan</code> and <code>/speckit.tasks</code></td>
    </tr>
    <tr>
      <td><code>/implement-spec</code></td>
      <td>Implementation<br><code>openspec show &lt;change-name&gt;</code></td>
      <td><code>/speckit.implement</code></td>
    </tr>
    <tr>
      <td><code>/close-spec</code></td>
      <td>Review and closure<br><code>openspec archive &lt;change-name&gt;</code></td>
      <td><code>/speckit.analyze</code> and <code>/speckit.converge</code></td>
    </tr>
  </tbody>
</table>

`/onboarding` differs from `openspec init` and `specify init` in one important way: it does not just initialize; it also refuses to proceed when the repository is ambiguous. A repository with two OpenSpec projects is reported as technical debt instead of silently getting a third.

<a id="testing-the-workflow-with-a-reproducible-benchmark-part-2"></a>

## Testing the workflow with a reproducible benchmark, Part 2

The [`0.18.0` release](https://jabrena.github.io/plinth/blog/2026/08/release-0.18.0.html#testing-the-workflow-with-a-reproducible-benchmark) introduced a benchmark harness under [`benchmarks/`](https://github.com/jabrena/plinth/tree/main/benchmarks). Part 1 showed one scenario clearly ahead of the rest, but it could not say *why*, because that scenario changed two things at once: the OpenSpec plan and the orchestration command that executes it.

Part 2 separates them. This release expands the harness with a new direct scenario, a second problem, and v2 solution snapshot metrics:

<table>
  <thead>
    <tr>
      <th>Addition</th>
      <th>Purpose</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>scenario5</code></td>
      <td>The same OpenSpec plan as <code>scenario4</code>, implemented directly with <code>/implement-spec</code> forbidden — a clean A/B where orchestration is the only variable.</td>
    </tr>
    <tr>
      <td>Problem 2 — Greek Gods API</td>
      <td>A Quarkus persistence-and-scheduling problem that joins the original Spring Boot fan-out-and-sum one, so every finding can be read per problem.</td>
    </tr>
    <tr>
      <td>New samples</td>
      <td>Results from Claude Code (Fable 5, Opus 5, Sonnet 5), Codex GPT-5, and Grok 4.5, growing the corpus from 54 samples to 217 samples.</td>
    </tr>
  </tbody>
</table>

The number of samples grew roughly fourfold, from `54` in Part 1 to `217` in Part 2.

The results revisit the three hypotheses from Part 1:

- **Orchestration reduces rework beyond the written plan — conditionally supported.** On the harder Quarkus problem, the orchestrated scenario passed every run with half the average rework of the direct one. On the simpler fan-out problem, the gap closed, while the direct scenario was about `36%` faster by median wall time.
- **The delegation command drives skill and agent use — supported.** With the same plan but no orchestration, runs used half the skills and almost no agents. Richer documents do not pull the library in; the command does.
- **Written architecture holds regardless of who executes it — supported.** The hexagonal scaffold and its boundary test appeared in 67 of 68 direct runs, because the OpenSpec input spells them out. What neither workflow controls yet is the dependency graph.

The practical conclusion is a more precise recommendation than Part 1 allowed: reach for `/implement-spec` on integration-heavy work, not by default. That is exactly the kind of statement a benchmark should make possible — not a declaration of a universal winner, but evidence about when each step of the workflow pays for itself.

For the detailed methodology and findings, read [Validating Hypotheses About the Plinth Workflow with a Benchmark, Part 2](/plinth/blog/2026/09/validating-hypotheses-about-plinth-workflow-with-a-benchmark-part-2.html).

In upcoming releases, `/implement-spec` will be improved to reduce latency.

<a id="jcconf-2026"></a>

## How was the experience in JCConf 2026?

JCConf 2026 was a one-day conference held in Taipei, and it was an excellent place to learn how people from `Taiwan`, `China`, `Japan`, and other countries in `Asia` use agentic development daily. I am sincerely grateful to [Nicolas Lu](https://www.linkedin.com/in/nicolas-lu-153bb7286) and the whole **JCConf team** for their full support.

<table>
  <tbody>
    <tr>
      <td><a href="https://jcconf.tw/2026/"><img src="/plinth/images/2026/9/jcconf-1.png" alt="JCConf 2026"></a></td>
      <td><a href="https://jcconf.tw/2026/"><img src="/plinth/images/2026/9/jcconf-2.png" alt="JCConf 2026"></a></td>
    </tr>
  </tbody>
</table>

The talk went smoothly, and I achieved my goal of raising awareness of a few points teams should consider to avoid production issues when using this kind of tooling. If you didn't attend the talk, you can review the [slides](https://jabrena.github.io/plinth/jcconf-2026/index.html).

<a id="doubts"></a>

## Do you still have questions about the project?

If you are interested in learning how to apply an AI-native workflow to Java Enterprise development, you can attend the following workshop at `Devoxx Belgium 2026`.

[![](/plinth/images/2026/9/devoxx-workshop.png)](https://devoxx.be/)

**Agenda (180 min):**

- Introduction to Agentic Development
- Run Exercise 1
- Break
- Run Exercise 2
- Advanced concepts
- Q&A

**Plinth Workflow:**

```text
/onboarding
  |
  v
Issue
  |
  v
/update-issue --> /explore-problem --> /create-acceptance-criteria
  |
  v
/create-spec --> /explore-design
  |
  v
/implement-spec
  |
  v
/close-spec
```

The workshop shows how agents implement changes faster. But a green PR can still be wrong: code that compiles, passes tests and security scans, and meets its acceptance criteria can break production when the consequences live outside the repository. A rename like `customerId` → `clientId` is locally correct, yet it can ripple through OpenAPI clients, Kafka events, Flyway migrations, and data pipelines. Implementation speeds up; context and review capacity do not.

[![](/plinth/images/2026/9/devoxx-talk.png)](https://devoxx.be/)

If you want to go beyond the green PR, the talk explains why Human-in-the-Loop (HITL) is still required and how to make it meaningful rather than *human-as-a-button*. It walks through PR gates, shows how laws, standards, and frameworks such as the `EU AI Act`, `GDPR`, `CRA`, `NIS2`, and `DORA` become engineering controls, and closes with steps a team can apply next Monday. Agents may propose; accountable humans decide. Join the talk [`The Importance of HITL to Avoid Chaos and EU Regulations for AI`](https://m.devoxx.com/events/dvbe26/talks/7017/the-importance-of-hitl-to-avoid-chaos-and-eu-regulations-for-ai) at [`Devoxx BE 2026`](https://devoxx.be/).

<a id="next-steps"></a>

## Next steps

For the next release, we plan to work on a few topics:

- Reduce the latency of `/implement-spec` on simple problems, as suggested by the Part 2 benchmark.
- Add `Katas` that help users learn `Plinth` incrementally.
- Add support for `agent plugins`.
- Go deeper into the EU regulation ecosystem for `GenAI`.
