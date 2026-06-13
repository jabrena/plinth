# Maven Multi-Module Demo

This example exists to validate Maven best-practices skill behavior for multi-module builds.

The root `pom.xml` declares two child modules:

- `demo-api`
- `demo-service`

Each child module includes a distinct configuration smell so acceptance tests can verify that the skill reads every child `pom.xml` before making recommendations.
