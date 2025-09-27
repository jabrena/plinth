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

- Improvements in System prompts
  - Decoupled specifialized behaviours like `behaviour-consultative-interaction` from System prompts
  - Added specialized behaviours to combine with Pure System prompts
  - Added support for `UML State machine diagrams` in `@170-java-documentation`
- Improvements in the project
  - Added Website based on JBake
  - Added Initial support for AGENTS.md
- Improvements about Support
  - Added a course about How to use `System prompts for Java`

Let's explain one by one the different features released

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

but what happen if you System prompts in `Data pipelines`?

![](/cursor-rules-java/images/data-pipeline-workflow.png)

## Do you still have doubts about the project?

If you feel stuck using this project or you have any doubts, you could attend the following talks at Devoxx BE in October:

- https://devoxx.be/app/talk/4715/the-power-of-cursor-rules-in-java-enterprise-development
- https://devoxx.be/app/talk/4708/101-cursor-ai-learning-to-use-for-java-enterprise-projects/

[![](/cursor-rules-java/images/devoxx-logo.png)](https://devoxx.be/)
