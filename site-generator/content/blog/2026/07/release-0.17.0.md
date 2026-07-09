title=What's new in Plinth 0.17.0?
date=2026-07-13
type=post
tags=blog,skills,java,agents,design,regulations,validation
author=Juan Antonio Breña Moral
status=published
~~~~~~

`Plinth` is an AI-native engineering toolkit for modern Java enterprise development, powered by `Commands`, `Agents`, `Skills`, and `MCP Servers`.

This release brings several improvements to the development workflow, but the most visible change is the new project identity: the repository moves from `cursor-rules-java` to `plinth`.

Thanks to our community members in [`Urumqi`](https://www.google.com/maps/search/?api=1&query=Urumqi), [`Singapore`](https://www.google.com/maps/search/?api=1&query=Singapore), [`Des Moines`](https://www.google.com/maps/search/?api=1&query=Des+Moines), [`Bengaluru`](https://www.google.com/maps/search/?api=1&query=Bengaluru), and [`Flint Hill`](https://www.google.com/maps/search/?api=1&query=Flint+Hill+Virginia). 👋👋👋

## Why Plinth?

In civil architecture, a `plinth` is the base that supports what people actually see. It is not the column, the arch, the road, or the aqueduct, but without a good plinth, the visible structure loses alignment, load-bearing capacity, and long-term stability. That image fits the project better than the original name.

The project is no longer only a collection of `Cursor rules for Java`. Today, it includes `Commands`, `Agents`, `Skills`, generated inventories, OpenSpec workflows, validation pipelines, regulation review aids, framework-specific guidance, and release assets. The new name, `Plinth`, gives the project a broader identity without losing its Java roots.

The metaphor comes from classical Roman architecture. In the Roman Empire, durable engineering depended on foundations, proportions, materials, load paths, maintenance, and repeatable methods. [Marcus Vitruvius Pollio](https://en.wikipedia.org/wiki/Vitruvius) described architecture around the enduring qualities often summarized as `firmitas`, `utilitas`, and `venustas`.

[![](/plinth/images/2026/7/firmitas-utilitas-venustas.png)](https://en.wikipedia.org/wiki/Firmness,_commodity,_and_delight)

That is a useful analogy for modern software engineering with AI.

An AI agent can generate code quickly, but speed is not enough. A team still needs a stable base: requirements, design constraints, compatibility strategy, tests, security checks, architecture boundaries, operational evidence, and human review. Without that base, the generated work may look impressive while resting on weak assumptions.

---

Now that the repository name change has been explained, let's continue by reviewing the new features included in this release:

- [Community first!](#community-first)
- [Enhancing OpenSpec operations](#enhancing-openspec-operations)
- [Improving migration safety with Flyway, Mongock, and Parallel Change](#improving-migration-safety-with-flyway-mongock-and-parallel-change)
- [Making architecture boundaries visible with Hexagonal Architecture](#making-architecture-boundaries-visible-with-hexagonal-architecture)
- [Modeling the domain with stronger Java types](#adding-more-types-possibilities)
- [Extending EU regulations and ISO engineering review skills](#extending-eu-regulations-and-iso-engineering-review-skills)
- [Improving security gates in the pipeline with VirusTotal](#improving-security-gates-in-the-pipeline-with-virustotal)
- [Recommended Books and Talks](#recommended-books-talks)
- [Next steps](#next-steps)
- [Do you still have questions about the project?](#doubts)

If you have questions about the project, how to customize it for your team, how to use the skills in daily work, or how to solve tooling issues, use [`GitHub Discussions`](https://github.com/jabrena/plinth/discussions).

**Help this project grow:** [If this project helps your team, become a sponsor.](https://github.com/sponsors/jabrena)

<a id="community-first"></a>

## Community first!

As `Plinth` grows from a technical repository into an OSS product for software engineers, the community becomes part of the product itself. Feedback, issue reports, pull requests, and real project stories help keep the guidance practical, reviewable, and useful beyond my own context. This release is a good reminder of that. Many thanks to [Leandro Loureiro](https://github.com/lealoureiro) for your contribution, for taking the time to understand the project, and for helping improve it from the perspective of someone using it outside the maintainer's day-to-day work.

[![](/plinth/images/2026/7/the-first-pr-new.png)](https://github.com/jabrena/plinth/pull/966)

Now, it is possible to receive from Business roles, `What` is temptative to be built from [`Azure Devops`](https://learn.microsoft.com/en-us/azure/devops/boards/boards/kanban-overview?view=azure-devops) with the new Skill `@045-planning-azure-devops`.

If you use the project, you can participate as an [individual contributor](https://github.com/jabrena/plinth/issues?q=is%3Aissue%20state%3Aopen%20label%3A%22good%20first%20issue%22) or by sharing your experience in [`GitHub Discussions`](https://github.com/jabrena/plinth/discussions).

<a id="enhancing-openspec-operations"></a>

## Enhancing OpenSpec operations

AI coding tools are very good at producing code quickly. That is useful, but speed alone is not the same as engineering discipline. Real delivery work also needs design sequencing, compatibility analysis, test strategy, small slices, and reviewable evidence.

This release adds a new family of design skills to be used in the workflow:

- [`@051-design-two-steps-methods`](https://www.skills.sh/jabrena/plinth/051-design-two-steps-methods)
- [`@052-design-hamburger-method`](https://www.skills.sh/jabrena/plinth/052-design-hamburger-method)
- [`@053-design-simple-rules`](https://www.skills.sh/jabrena/plinth/053-design-simple-rules)
- [`@054-design-tdd`](https://www.skills.sh/jabrena/plinth/054-design-tdd)
- [`@055-design-parallel-change`](https://www.skills.sh/jabrena/plinth/055-design-parallel-change)
- [`@056-design-avoid-breaking-changes`](https://www.skills.sh/jabrena/plinth/056-design-avoid-breaking-changes)

> Repeat with me:
> “SDD is not Aladdin’s lamp—it won’t grant every software wish”.

Once the OpenSpec change is created, the next step is to refine it with design skills before implementation begins. The spec describes the intent, but the design skills help shape the change into a safer engineering path: smaller slices, clearer compatibility decisions, stronger test strategy, and explicit review points.

That is why the `Plinth commands` are designed around the same delivery phases used by `OpenSpec` and `Spec Kit`. The commands are not isolated shortcuts; they map to a workflow that moves from intake to specification, design refinement, task planning, implementation, and closure:

<table>
  <thead>
    <tr>
      <th>Plinth Commands</th>
      <th>OpenSpec phase</th>
      <th>Spec Kit phase</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>/update-issue</code></td>
      <td>Issue intake<br><code>openspec list</code></td>
      <td><code>/speckit.specify</code> input</td>
    </tr>
    <tr>
      <td><code>/create-spec</code><br><code>/review-alignment</code></td>
      <td>Proposal<br><code>openspec new change &lt;change-name&gt;</code></td>
      <td><code>/speckit.specify</code> and <code>/speckit.clarify</code></td>
    </tr>
    <tr>
      <td><code>/create-spec</code></td>
      <td>Specification<br><code>openspec show &lt;change-name&gt;</code></td>
      <td><code>/speckit.specify</code> plus <code>/speckit.checklist</code></td>
    </tr>
    <tr>
      <td><code>/create-spec</code><br><code>/explore-design</code></td>
      <td>Task planning<br><code>openspec validate --all</code></td>
      <td><code>/speckit.plan</code> and <code>/speckit.tasks</code></td>
    </tr>
    <tr>
      <td><code>/implement-spec</code></td>
      <td>Implementation<br><code>openspec show &lt;change-name&gt;</code></td>
      <td><code>/speckit.implement</code></td>
    </tr>
    <tr>
      <td></td>
      <td>Review and closure<br><code>openspec archive &lt;change-name&gt;</code></td>
      <td><code>/speckit.analyze</code> and <code>/speckit.converge</code></td>
    </tr>
  </tbody>
</table>

This mapping is useful because it makes the change path explicit. The agent is not only asked to generate the final patch; it is guided through issue intake, proposal, specification, planning, implementation, and closure. 

[![](/plinth/images/2026/7/kitchen-timer-new.png)](https://www.amazon.es/dp/B0893242P4?ref=nb_sb_ss_w_as-reorder_k2_1_13&amp=&crid=3MILANAXX5K8P&sprefix=kitchen%2Bclock&th=1)

Remember to invest quality time to gather and refine requirements to be included in your Spec. This Design phase need to go in `1x` and later, when the Spec is clear you can increase your Speed in implementation phase once, everything is clear.

> "There is no favorable wind for the sailor who doesn’t know where to go”
> ― Seneca

If you are interested in this direction, I recommend reading [From code generation to software engineering](/cursor-rules-java/blog/2026/06/from-code-generation-to-software-engineering.html).

<a id="improving-migration-safety-with-flyway-mongock-and-parallel-change"></a>

## Improving migration safety with Flyway, Mongock, and Parallel Change

Database migrations are one of the easiest places for an AI agent to produce a clean-looking but unsafe patch. Bad migrations can damage a database in ways that are hard to recover from:

- A column is renamed in place while old application instances are still running, so writes start failing during a rolling deployment.
- A column is dropped because the new code no longer uses it, but a delayed worker, report, export, or integration still reads it.
- A backfill updates every row with the wrong default, overwriting meaningful historical data with a value that cannot be reconstructed.
- A migration changes money, time-zone, status, or identifier semantics without preserving the original business meaning.
- A `NOT NULL`, unique, or foreign-key constraint is added before dirty production data has been measured and cleaned.
- A long-running DDL statement locks a hot table and turns a small schema change into an availability incident.
- A MongoDB document migration rewrites nested fields without handling old variants, partial documents, or unknown enum values.
- A rollback restores the previous application version, but the destructive migration has already removed the shape that version needs.

This release improves the migration guidance for:

- [`@313-frameworks-spring-db-migrations-flyway`](https://www.skills.sh/jabrena/plinth/313-frameworks-spring-db-migrations-flyway)
- [`@413-frameworks-quarkus-db-migrations-flyway`](https://www.skills.sh/jabrena/plinth/413-frameworks-quarkus-db-migrations-flyway)
- [`@513-frameworks-micronaut-db-migrations-flyway`](https://www.skills.sh/jabrena/plinth/513-frameworks-micronaut-db-migrations-flyway)
- [`@316-frameworks-spring-mongodb-migrations-mongock`](https://www.skills.sh/jabrena/plinth/316-frameworks-spring-mongodb-migrations-mongock)
- [`@416-frameworks-quarkus-mongodb-migrations-mongock`](https://www.skills.sh/jabrena/plinth/416-frameworks-quarkus-mongodb-migrations-mongock)
- [`@516-frameworks-micronaut-mongodb-migrations-mongock`](https://www.skills.sh/jabrena/plinth/516-frameworks-micronaut-mongodb-migrations-mongock)

The new guidance emphasizes migration safety, antipatterns, and `Parallel Change`.

```text
Phase     Goal
--------- ------------------------------------------------------------
Expand    Add the new schema or document shape without removing the old one
Migrate   Backfill, dual-write, compare, and observe
Contract  Remove the old shape only after rollout evidence exists
```

This is why `@055-design-parallel-change` belongs in the design workflow before framework-specific Flyway or Mongock implementation guidance. The agent should understand the transition before it writes the migration.

> **⚠️ AVOID RUNNING DATABASE MIGRATION TASKS WITHOUT HITL.**
Human-in-the-loop review is necessary because migrations can destroy or reinterpret production data, block critical tables, break rollback paths, and affect systems that are not visible in the local codebase. An agent can draft the plan, checks, and scripts, but a qualified human owner should review the migration intent, data impact, rollback strategy, monitoring evidence, and execution window before it reaches production.

**Note:** If your domain or subdomain has microservices associated with databases and you are adopting GenAI tools, you should review the current architecture to mitigate potential database production issues.

If you want to go deeper into this topic, I recommend reading: [Why Do I Need to Use the Parallel Change Pattern?](/cursor-rules-java/blog/2026/07/why-do-i-need-to-use-the-parallel-change-pattern.html)

<a id="making-architecture-boundaries-visible-with-hexagonal-architecture"></a>

## Making architecture boundaries visible with Hexagonal Architecture

This release adds [`@707-technologies-hexagonal-architecture`](https://www.skills.sh/jabrena/plinth/707-technologies-hexagonal-architecture), a framework-agnostic skill for reviewing Java application boundaries.

> The hexagonal architecture, or ports and adapters architecture, is an architectural style used in software design. It aims at creating loosely coupled application components that can be easily connected to their software environment by means of ports and adapters.

The skill helps engineers and agents inspect whether dependency direction and responsibility placement are consistent with Hexagonal Architecture:

- Domain code should not depend on framework adapters.
- Application services should coordinate use cases without becoming infrastructure code.
- Adapters should translate between external systems and the application boundary.
- Framework-specific details should stay outside the core model unless the team has made an explicit tradeoff.

The skill also includes `ArchUnit`-aware verification guidance without forcing every project to add a new dependency automatically. That is important because architecture tests should support the existing project context, not become cargo-cult configuration.

```java
@ArchTest
static final ArchRule should_keep_application_core_independent_from_adapters = noClasses()
        .that()
        .resideInAnyPackage("info.jab.domain..", "info.jab.mv.application..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("info.jab.mv.adapter..");

@ArchTest
static final ArchRule should_keep_domain_independent_from_application_services = noClasses()
        .that()
        .resideInAPackage("info.jab.mv.domain..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("info.jab.mv.application..");

@ArchTest
static final ArchRule should_keep_driving_adapters_independent_from_driven_adapters = noClasses()
        .that()
        .resideInAPackage("info.jab.mv.adapter.in..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("info.jab.mv.adapter.out..");

@ArchTest
static final ArchRule should_keep_driven_adapters_independent_from_driving_adapters = noClasses()
        .that()
        .resideInAPackage("info.jab.mv.adapter.out..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("info.jab.mv.adapter.in..");
```

https://www.archunit.org/userguide/html/000_Index.html

For framework agents, the skill is useful before making changes in `Spring Boot`, `Quarkus`, or `Micronaut` applications. It gives the agent a boundary review language before it starts moving packages, introducing adapters, or changing service responsibilities.

<a id="adding-more-types-possibilities"></a>

## Modeling the domain with stronger Java types

Java is a strongly typed programming language, and that is not only a compiler detail. It is one of the main design tools available to a Java team. Good types make business rules visible, prevent accidental misuse, improve method signatures, and help agents reason about the code before they change it.

This matters especially when a domain concept has more meaning than its storage representation. A `String` can hold an email, an order id, a country code, or a currency code, but those values are not interchangeable. A `BigDecimal` can hold a numeric amount, but it does not explain whether the amount is euros, dollars, tax, discount, revenue, balance, or an exchange rate. When everything is modeled with generic types, invalid combinations become easier to write and harder to review.

For that reason, [`@111-java-maven-dependencies`](https://www.skills.sh/jabrena/plinth/111-java-maven-dependencies) now includes `JavaMoney` guidance. The skill helps agents recognize when a project should use Money and Currency API support, including [`JSR 354`](https://jcp.org/en/jsr/detail?id=354) and [`Moneta`](https://javamoney.github.io/api.html), instead of treating monetary values as isolated numbers.

This update combines well with [`@122-java-type-design`](https://www.skills.sh/jabrena/plinth/122-java-type-design), which focuses on type-safe wrappers, domain-specific value objects, and reducing primitive obsession in Java code.

Use this guidance when a project needs to model money, currencies, amounts, exchange-rate concerns, pricing, billing, financial calculations, or financial domain values where `BigDecimal` alone is too weak to describe the business meaning. The goal is not to add dependencies by default. The goal is to choose types that make the model harder to misuse and easier to evolve.

<a id="extending-eu-regulations-and-iso-engineering-review-skills"></a>

## Extending EU regulations and ISO standard engineering review skills

`0.16.0` introduced the first family of EU regulation engineering review skills. `0.17.0` extends that family with financial-market, market-integrity, product-liability, and AI management-system review support:

- [`@810-regulations-eu-mifid-ii`](https://www.skills.sh/jabrena/plinth/810-regulations-eu-mifid-ii)
- [`@811-regulations-eu-market-abuse-regulation`](https://www.skills.sh/jabrena/plinth/811-regulations-eu-market-abuse-regulation)
- [`@812-regulations-eu-product-liability-directive`](https://www.skills.sh/jabrena/plinth/812-regulations-eu-product-liability-directive)
- [`@813-regulations-iso-42001`](https://www.skills.sh/jabrena/plinth/813-regulations-iso-42001)

These skills include questionnaires, report templates, examples, and acceptance prompts.

The common workflow is:

1. Classify whether the system, product, workflow, or AI capability is in scope.
2. Ask for concrete engineering evidence.
3. Map regulatory or standard concerns to controls, logs, tests, approvals, documentation, and release gates.
4. Produce a report for qualified owner review.

These skills are engineering review aids. **They do not provide legal advice and they do not replace qualified legal, compliance, privacy, security, risk, product, audit, or governance owners.**

For fintech and enterprise teams, the practical value is early evidence. An AI agent working on trading workflows, issuer disclosure, surveillance alerts, product updates, model governance, RAG systems, or AI-assisted delivery should know which engineering questions may need owner review before release.

The current regulation and standard review coverage is:

<table>
  <thead>
    <tr>
      <th>Review skill</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><a href="https://www.skills.sh/jabrena/plinth/801-regulations-eu-ai-act"><code>@801-regulations-eu-ai-act</code></a><br>Reviews AI-system scope, risk classification, transparency, human oversight, evidence, and owner handoff.</td>
    </tr>
    <tr>
      <td><a href="https://www.skills.sh/jabrena/plinth/802-regulations-dora"><code>@802-regulations-dora</code></a><br>Reviews digital operational resilience for financial entities and ICT-dependent business services.</td>
    </tr>
    <tr>
      <td><a href="https://www.skills.sh/jabrena/plinth/803-regulations-gdpr"><code>@803-regulations-gdpr</code></a><br>Reviews privacy engineering concerns around personal data collection, processing, logging, sharing, and deletion.</td>
    </tr>
    <tr>
      <td><a href="https://www.skills.sh/jabrena/plinth/804-regulations-eu-nis2"><code>@804-regulations-eu-nis2</code></a><br>Reviews cybersecurity and resilience evidence for essential, important, and critical-sector services.</td>
    </tr>
    <tr>
      <td><a href="https://www.skills.sh/jabrena/plinth/805-regulations-eu-cyber-resilience-act"><code>@805-regulations-eu-cyber-resilience-act</code></a><br>Reviews product-security evidence for software and products with digital elements.</td>
    </tr>
    <tr>
      <td><a href="https://www.skills.sh/jabrena/plinth/806-regulations-eu-data-act"><code>@806-regulations-eu-data-act</code></a><br>Reviews connected-product, related-service, data-sharing, portability, and cloud-switching concerns.</td>
    </tr>
    <tr>
      <td><a href="https://www.skills.sh/jabrena/plinth/807-regulations-eu-digital-services-act"><code>@807-regulations-eu-digital-services-act</code></a><br>Reviews due-diligence and transparency evidence for intermediaries, hosting services, and platforms.</td>
    </tr>
    <tr>
      <td><a href="https://www.skills.sh/jabrena/plinth/808-regulations-eu-digital-markets-act"><code>@808-regulations-eu-digital-markets-act</code></a><br>Reviews gatekeeper-platform concerns around core platform services, interoperability, and fair access.</td>
    </tr>
    <tr>
      <td><a href="https://www.skills.sh/jabrena/plinth/810-regulations-eu-mifid-ii"><code>@810-regulations-eu-mifid-ii</code></a><br>Reviews investment-services, trading, investor-protection, transparency, and record-keeping evidence.</td>
    </tr>
    <tr>
      <td><a href="https://www.skills.sh/jabrena/plinth/811-regulations-eu-market-abuse-regulation"><code>@811-regulations-eu-market-abuse-regulation</code></a><br>Reviews market-integrity evidence for issuer disclosures, insider information, surveillance, and alerts.</td>
    </tr>
    <tr>
      <td><a href="https://www.skills.sh/jabrena/plinth/812-regulations-eu-product-liability-directive"><code>@812-regulations-eu-product-liability-directive</code></a><br>Reviews software, AI-enabled product, component, update, and post-release safety evidence.</td>
    </tr>
    <tr>
      <td><a href="https://www.skills.sh/jabrena/plinth/813-regulations-iso-42001"><code>@813-regulations-iso-42001</code></a><br>Reviews AI management-system evidence for organizational AI governance and lifecycle controls.</td>
    </tr>
  </tbody>
</table>

If you are interested in this topic, I recommend reading both articles: [Introduction to EU regulations Part I](/cursor-rules-java/blog/2026/06/introduction-to-eu-regulations-part-i.html) and [Introduction to EU regulations Part II](/cursor-rules-java/blog/2026/07/introduction-to-eu-regulations-part-ii.html)

<a id="improving-security-gates-in-the-pipeline-with-virustotal"></a>

## Improving security gates in the pipeline with VirusTotal

This release also strengthens the validation path around generated content, release artifacts, and documentation.

The pipeline now includes `VirusTotal` checks before generated artifacts are promoted. In the latest scan, `VirusTotal` evaluated the artifact with `75` security applications and engines. This is not a replacement for the build, tests, or human review, but it gives maintainers one more piece of evidence when deciding whether a generated artifact is ready to publish.

The scan included the following applications and engines: `ALYac`, `APEX`, `AVG`, `Acronis`, `AhnLab-V3`, `Alibaba`, `Antiy-AVL`, `Arcabit`, `Avast`, `Avast-Mobile`, `Avira`, `BitDefender`, `BitDefenderFalx`, `Bkav`, `CAT-QuickHeal`, `CMC`, `CTX`, `ClamAV`, `CrowdStrike`, `Cylance`, `Cynet`, `DeepInstinct`, `DrWeb`, `ESET-NOD32`, `Elastic`, `Emsisoft`, `F-Secure`, `Fortinet`, `GData`, `Google`, `Gridinsoft`, `Ikarus`, `Jiangmin`, `K7AntiVirus`, `K7GW`, `Kaspersky`, `Kingsoft`, `Lionic`, `Malwarebytes`, `MaxSecure`, `McAfeeD`, `MicroWorld-eScan`, `Microsoft`, `NANO-Antivirus`, `Paloalto`, `Panda`, `Rising`, `SUPERAntiSpyware`, `Sangfor`, `SentinelOne`, `Skyhigh`, `Sophos`, `Symantec`, `SymantecMobileInsight`, `TACHYON`, `Tencent`, `Trapmine`, `TrellixENS`, `TrendMicro`, `TrendMicro-HouseCall`, `Trustlook`, `VBA32`, `VIPRE`, `Varist`, `ViRobot`, `VirIT`, `Webroot`, `Xcitium`, `Yandex`, `Zillya`, `ZoneAlarm`, `Zoner`, `alibabacloud`, `huorong`, `tehtris`.

---

[Secur0](https://secur0.com/en) is a security platform that helps organizations find vulnerabilities through ethical hackers, bug bounty programs, and pentesting conducted by verified experts. Recently, `Plinth` drew attention from `Secur0`, which plans to review the project for potential vulnerabilities in the coming months. This adds another external security signal to the release process.

<a id="recommended-books-talks"></a>

## Recommended Books and Talks


**Spec Driven Development:**

I recommend watching one of the latest [talks](https://www.youtube.com/watch?v=35dH6q18UtI) from [Simon Martinelli](https://martinelli.ch/) about Spec Driven Development. I like the common sense in his ideas.

**LEAN Development:**

If you are interested in improving your LEAN skills, [Eduardo Ferro](https://www.eferro.net/) has published an excellent book:

[![](/plinth/images/2026/7/menos-software-mas-impacto-new.jpg)](https://menos-software.eferro.net/)

**Hexagonal Architecture:**

If you are interested in Hexagonal Architecture, I recommend [Alistair Cockburn's book](https://alistair.cockburn.us/hexagonal-architecture):

[![](/plinth/images/2026/7/hexagonal-architecture-explained-new.jpg)](https://www.amazon.com/Hexagonal-Architecture-Explained-Alistair-Cockburn/dp/173751978X)

<a id="next-steps"></a>

## Next steps

For the next release, we plan to work on a few topics:

- Add `Katas` to improve your knowledge about `Plinth` in an incremental way.
- Improve the different behaviors to improve the `Design phase`.
- Add support for `Spec Kit`.
- Update the `Spring Boot` support for `4.1.0`.
- Add a skill about `JVM Flags`.
- Going down the rabbit hole about EU regulation ecosystem for `GenAI`.

<a id="doubts"></a>

## Do you still have questions about the project?

If you feel stuck using this project or have questions, you can attend the following workshop at [`JCConf 2026`](https://jcconf.tw/2026/):

[![](/plinth/images/2026/7/jcconf-2026.png)](https://jcconf.tw/2026/)
