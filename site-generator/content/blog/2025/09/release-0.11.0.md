title=What's new in Cursor rules for Java 0.11.0?
date=2025-09-29
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What are Cursor rules for Java?

The project provides a collection of System prompts for Java Enterprise development that help software engineers in their daily programming work & data pipelines. The [available System prompts for Java](https://github.com/jabrena/cursor-rules-java/blob/main/CURSOR-RULES-JAVA.md) cover areas such as `Build system based on Maven`, `Design`, `Coding`, `Testing`, `Refactoring & JMH Benchmarking`, `Performance testing with JMeter`, `Profiling with async-profiler/JDK tools`, `Documentation`, and `Diagrams`.

## What's new in this release?

In this release, the project introduces several updates:

- **Improvements in system prompts:**
- Decoupled the specialized behavior `behaviour-consultative-interaction` from some system prompts.
- Added specialized behaviors (`behaviour-consultative-interaction` and `behaviour-progressive-learning`) that can be combined with pure system prompts.
- Added support for `UML state machine diagrams` in `@170-java-documentation`.
- Updated `@161-java-profiling-detect` to add support for`Async-profiler 4.1` and the latest `JFR features` included in `Java 25` and earlier.
- **Project improvements:**
- Published a dedicated website about System prompts for Java to communicate releases, articles, and courses with the help of JBake.

Let's explain one by one the different features released.

## Why decouple Pure System prompts from specialized behaviors?

In previous months, the system prompts evolved to present refactoring alternatives using the `consultative-interaction technique` and then apply the user's decision. However, that design coupled the system prompt with a specific specialized behavior.

With this approach, until `v0.10.0` we used this syntax:

**Interactive user prompt:**

```
Review my code to show several alternatives to apply Java Generics
with the cursor rule @128-java-generics
```

**User prompt:**

```
Apply Java Generics in the class with @128-java-generics
without any question
```

---

But now, starting with `v0.11.0`, you have a cleaner syntax:

**User prompt with consultative interactive behavior:**

```
Improve the classes provided in the context by applying
the system prompt @128-java-generics
with the behavior @behaviour-consultative-interaction
```

**User prompt with training behavior:**

```
Create a course for @128-java-generics.md
using the behavior @behaviour-progressive-learning.md
and place the course here
```

**User prompt:**

```
Improve the classes provided in the context
by applying the system prompt @128-java-generics
```

Further information: https://github.com/jabrena/cursor-rules-java/blob/0.11.0/CURSOR-RULES-JAVA.md

With this evolution, software engineers now can combine pure system prompts and specialized behaviors in `16^3 = 4096` combinations by design.

**Pure system prompts:**

- [`@100-java-cursor-rules-list`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/100-java-cursor-rules-list.md)
- [`@110-java-maven-best-practices`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/110-java-maven-best-practices.md)
- [`@111-java-maven-dependencies`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/111-java-maven-dependencies.md)
- [`@113-java-maven-documentation`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/113-java-maven-documentation.md)
- [`@121-java-object-oriented-design`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/121-java-object-oriented-design.md)
- [`@122-java-type-design`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/122-java-type-design.md)
- [`@124-java-secure-coding`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/124-java-secure-coding.md)
- [`@125-java-concurrency`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/125-java-concurrency.md)
- [`@126-java-logging`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/126-java-logging.md)
- [`@127-java-exception-handling`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/127-java-exception-handling.md)
- [`@128-java-generics`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/128-java-generics.md)
- [`@131-java-unit-testing`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/131-java-unit-testing.md)
- [`@141-java-refactoring-with-modern-features`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/141-java-refactoring-with-modern-features.md)
- [`@142-java-functional-programming`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/142-java-functional-programming.md)
- [`@143-java-functional-exception-handling`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/143-java-functional-exception-handling.md)
- [`@144-java-data-oriented-programming`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/144-java-data-oriented-programming.md)

**Specialized behaviors:**

- Default behavior
- [`@behaviour-consultative-interaction`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/behaviour-consultative-interaction.md)
- [`@behaviour-progressive-learning`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/behaviour-progressive-learning.md)

With this design change applied into the different system prompts, you can use them in your `Data pipelines` for different use cases such as `automatic coding`, `code refactoring`, `continuous profiling`, and more.

![](/cursor-rules-java/images/data-pipeline-workflow.png)

## New UML state machine diagrams for better understanding in complex dependencies.

Sometimes you need to use complex dependencies in your projects, like Apache Kafka clients, and the team may not be strong enough in the early stages. If you don't have much experience with them, a good practice is to run a spike and review how the client works under the hood to mitigate risks.

During the spike, you may need to visualize internal aspects of the clients, so why not generate a `UML state machine diagram` to gain more insights.

**Examples:**

```
Create UML class diagrams with @171-java-diagrams without any questions from
https://github.com/jabrena/kafka/tree/trunk/clients/src/main/java/org/apache/kafka/clients/producer
```

**Result:**

![](/cursor-rules-java/images/0.11.0-uml-state-machine-diagram-example.png)

With this visualization, you can see whether something is missing in your Java integration.

## Profiling improvements

In this release, the script `profile-java-process.sh`, included in  `@161-java-profiling-detect`, was updated to include the latest release (v4.1) of the popular profiling tool Async-profiler. In addition, the script added new JFR options available in Java 25 and earlier.

![](/cursor-rules-java/images/0.11.0-profiling-menu.png)

In recent months, I have been inspired by the work of outstanding engineers in this field: [Francesco Nigro](https://x.com/forked_franz), [Jaromir Hamala](https://x.com/jerrinot), and [Johannes Bechberger](https://x.com/parttimen3rd). I highly recommend following them.

## A new website

In recent releases, the project published an article summarizing its evolution and shared it across channels like Twitter, Reddit, and LinkedIn. To unify the source of truth, and leveraging [GitHub Pages](https://docs.github.com/es/pages), the project now has [a dedicated website](https://jabrena.github.io/cursor-rules-java/index.html).

The website includes information about releases, technical articles you can apply in your projects, and courses.

**What courses were created in this release?**

- [Learn to use System prompts for Java](https://jabrena.github.io/cursor-rules-java/courses/system-prompts-java/index.html)
- [Learn to improve your development with Java Generics](https://jabrena.github.io/cursor-rules-java/courses/java-generics/index.html)
- [Learn how to detect memory leaks with System prompts](https://jabrena.github.io/cursor-rules-java/courses/profile-memory-leak/index.html)

## Do you still have questions about the project?

If you feel stuck using this project or have questions, you can attend the following talks at Devoxx BE in October:

- https://devoxx.be/app/talk/4715/the-power-of-cursor-rules-in-java-enterprise-development
- https://devoxx.be/app/talk/4708/101-cursor-ai-learning-to-use-for-java-enterprise-projects/

[![](/cursor-rules-java/images/devoxx-logo.png)](https://devoxx.be/)
