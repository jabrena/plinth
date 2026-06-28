title=Why Do I Need to Use the Parallel Change Pattern?
date=2026-07-02
type=post
tags=blog,skills,agents,design,software-engineering,java
author=MyRobot
status=published
~~~~~~

## The hidden risk in fast code generation

AI coding tools are very good at producing a complete-looking change from a short request.

That is useful when the change is local. It becomes risky when the change crosses compatibility boundaries: database schemas, public APIs, event contracts, configuration keys, command outputs, generated artifacts, or workflows consumed by other services.

A human engineer hears "rename this field" and often asks follow-up questions:

- Is the old version still deployed?
- Do external consumers read this value?
- Does rollback need the old shape?
- Is there production data that needs semantic mapping?
- Can we prove the old path is unused before removing it?

An AI agent may skip those questions unless the workflow forces them into the plan.

That is why this project includes explicit compatibility and parallel-change skills:

- `@055-design-parallel-change`
- `@056-design-avoid-breaking-changes`

These skills move the agent away from "generate the final state" and toward "design the path from current state to target state".

## The big-bang refactor problem

Many AI-generated changes are shaped like a big-bang refactor:

1. Rename the database column.
2. Update the entity field.
3. Update DTOs, mappers, tests, documentation, and examples.
4. Delete the old code.
5. Declare the work complete.

That patch may compile. It may even pass unit tests. But it can still fail in production during a rolling deployment, delayed worker execution, cached projection rebuild, external integration call, reporting query, or rollback.

The problem is not that the generated code is syntactically wrong. The problem is that the change assumes the world switches from old to new at the same instant.

Production rarely behaves that way.

## Parallel change

Parallel change is a design pattern for changing a system while old and new versions coexist.

Instead of replacing old behavior immediately, the team creates a compatibility window:

1. **Expand** the system so the old and new shapes can both exist.
2. **Migrate** reads, writes, data, jobs, and clients gradually.
3. **Contract** the old shape only after evidence proves it is unused.

For database work, this means avoiding destructive changes in the first migration. A column rename is not only a rename. It is a compatibility problem.

The safe shape often looks like this:

```text
Phase     Goal
--------- ------------------------------------------------------------
Expand    Add the new column or table while preserving the old shape
Migrate   Backfill, dual-write, compare old and new behavior
Contract  Remove the old column or table after rollout evidence exists
```

This is slower than a one-shot migration, but it is much easier to deploy, observe, and roll back.

## Expand-contract migrations

Expand-contract is especially important for database evolution because DDL success is not the same as business safety.

For example, an AI agent might propose:

```sql
ALTER TABLE customers RENAME COLUMN full_name TO display_name;
```

That looks clean. It is also dangerous during a rolling deployment because old application instances still expect `full_name`.

A safer plan separates the change:

```sql
-- Expand
ALTER TABLE customers ADD COLUMN display_name VARCHAR(200);

-- Migrate
UPDATE customers
SET display_name = full_name
WHERE display_name IS NULL AND full_name IS NOT NULL;

-- Application release:
-- read display_name with fallback to full_name;
-- write both values while old and new versions coexist.

-- Contract, later:
-- set constraints and drop full_name only after old readers are gone.
```

The key difference is not the SQL. The key difference is the deployment story.

## Strangler-style replacement

The same idea applies outside the database.

The strangler pattern replaces part of a system by routing behavior gradually to the new implementation while the old implementation remains available. It is valuable when a team is replacing a module, endpoint, workflow, integration, rules engine, or legacy subsystem.

An AI agent asked to "modernize this service" may try to rewrite everything in one patch. That is hard to review and hard to trust.

A strangler-style plan asks different questions:

- What is the smallest behavior that can move first?
- Can old and new paths run side by side?
- Can traffic be routed by feature flag, tenant, endpoint, or workflow?
- What evidence proves the new path is equivalent or intentionally different?
- What is the cleanup trigger for removing the old path?

This turns replacement into a sequence of reviewable slices.

## Backward compatibility is an engineering requirement

Backward compatibility is not only an API concern.

It also applies to:

- Database schema and data meaning
- Event payloads and Kafka topics
- REST response fields and error formats
- Configuration keys and defaults
- CLI command names, flags, output, and exit codes
- Generated skills, commands, documentation, and public release output
- Monitoring, dashboards, alerts, and runbooks

`@056-design-avoid-breaking-changes` makes those surfaces visible before the agent edits the code. It asks whether the change is confirmed breaking, potentially breaking, non-breaking, or unknown.

That classification matters because different risks need different responses. Some changes need a compatibility window. Some need aliases, deprecation notes, migration guidance, release notes, or owner decisions. Some are additive and can stay simple.

## Why explicit skills help AI agents

Prompting an agent with "be careful" is weak.

Giving the agent a concrete skill is stronger because the workflow becomes inspectable:

- Classify the migration risk.
- Decide whether parallel change is needed.
- Separate expand, migrate, and contract.
- Preserve rollback compatibility.
- Name the verification evidence.
- Define the cleanup trigger.
- Route framework-specific Flyway work only after the strategy is clear.

That sequence protects the team from the most tempting AI shortcut: generating the target architecture without designing the transition.

## A practical rule

When an AI-generated change removes, renames, narrows, reinterprets, or rewires something that another version or consumer may still use, pause.

Ask whether this is really one change or three:

```text
1. Expand safely.
2. Migrate with evidence.
3. Contract after proof.
```

That tiny pause can prevent a clean-looking patch from becoming a production incident.

## Share your experience

If you are using AI agents for migrations, refactoring, API evolution, or compatibility-sensitive Java work, share what you are learning with the project.

Use [GitHub Discussions](https://github.com/jabrena/cursor-rules-java/discussions) to post where parallel change helps, where the workflow feels heavy, and which compatibility risks agents still miss.
