title=What's new in Cursor rules for Java 0.11.0?
date=2025-09-29
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What are Cursor rules for Java?

The project provides a collection of System prompts for Java Enterprise development that help software engineers in their daily programming work. The [available System prompts for Java](https://github.com/jabrena/cursor-rules-java/blob/main/CURSOR-RULES-JAVA.md) cover aspects like `Build system based on Maven`, `Design`, `Coding`, `Testing`, `Refactoring & JMH Benchmarking`, `Performance testing with JMeter`, `Profiling with Async profiler/JDK tools` & `Documentation` & `Diagrams`.

## What is new in this release?

In this release, the project has released several features:

**Improvements in System prompts:**

- Decoupled specifialized behaviours like `behaviour-consultative-interaction` from System prompts.
- Added specialized behaviours to combine with Pure System prompts.
- Added support for `UML State machine diagrams` in `@170-java-documentation`.

**Improvements in the project:**

- Added Website based on JBake
- Added Initial support for AGENTS.md

**Improvements about Support:**

- Added a course about How to use `System prompts for Java`

Let's explain one by one the different features released.

## Why decouple Pure System prompts from Specialized behaviours?

During the previous months, the System prompts helped Sofware engineers using the  `consultative-interaction` technique in order to use the System prompts in a way that the execution show a set of alternatives and the software engineer take the decision to update the code in a dynamic way.

With this idea, we had until `v0.10.0` this syntax:

```
Interactive User prompt: Review my code to show several alternatives to apply Java Generics with the cursor rule @128-java-generics
User Prompt: Apply Java Generics in the class with @128-java-generics without any question
```

https://github.com/jabrena/cursor-rules-java/blob/0.10.0/CURSOR-RULES-JAVA.md

But from `v0.11.0`, you have a better and clean syntax:

```
User Prompt with Consultative Interactive Behaviour: Improve the class/classes added in the context applying the system prompt @128-java-generics with the behaviour @behaviour-consultative-interaction
User Prompt with Training behaviour: Create a course about @128-java-generics.md using the behavior @behaviour-progressive-learning.md and put the course here
User Prompt: Improve the class/classes added in the context applying the system prompt @128-java-generics
```

https://github.com/jabrena/cursor-rules-java/blob/0.11.0/CURSOR-RULES-JAVA.md

![](/cursor-rules-java/images/workflow.png)

but what happen if you System prompts with `Data pipelines`?

![](/cursor-rules-java/images/data-pipeline-workflow.png)

In this situations, it is better provide `Pure System prompts`.

What Pure system prompts does exist in the project?

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

What Specialized behaviour does exist?

- Default behaviour
- [`@behaviour-consultative-interaction`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/behaviour-consultative-interaction.md)
- [`@behaviour-progressive-learning`](https://github.com/jabrena/cursor-rules-java/blob/main/.cursor/rules/behaviour-progressive-learning.md)

Now, you have `16^3=4096` alternatives to interact with them by design.

## Added UML State machine diagrams to understand better complex Dependencies.

![](/cursor-rules-java/images/0.11.0-uml-state-machine-diagram-example.png)

## Do you still have doubts about the project?

If you feel stuck using this project or you have any doubts, you could attend the following talks at Devoxx BE in October:

- https://devoxx.be/app/talk/4715/the-power-of-cursor-rules-in-java-enterprise-development
- https://devoxx.be/app/talk/4708/101-cursor-ai-learning-to-use-for-java-enterprise-projects/

[![](/cursor-rules-java/images/devoxx-logo.png)](https://devoxx.be/)
