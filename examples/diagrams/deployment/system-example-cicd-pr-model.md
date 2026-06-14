# Ecommerce Deployment Example

This document describes a fictional ecommerce platform built with Java and
Quarkus services, databases, messaging systems, CI/CD automation, Docker image
publication, Artifactory, and an Azure runtime environment.

The example is intentionally detailed so it can be used as source material for
deployment diagrams, delivery pipeline diagrams, and architecture discussions.

## Business Actors

The main customer-facing actor is a shopper. A shopper uses the ecommerce system to
authenticate, browse the shop, add products to a cart, check out, pay, and book a
delivery time slot.

Internal actors operate and evolve the platform:

- Software engineers use Extreme Programming practices, IDEs, GenAI coding
  assistants, local containers, and test suites to design and implement changes.
- Tech leads and reviewers inspect small pull requests, architecture impacts,
  test coverage, and generated documentation as part of trunk-based continuous
  delivery.
- Platform engineers maintain CI/CD templates, Artifactory, Docker registries,
  Azure Kubernetes Service clusters, secrets, ingress, observability, and release
  policies.
- Support and operations teams use dashboards, logs, traces, and alerts to diagnose
  order, payment, inventory, and delivery issues.

## Customer Journey

1. The shopper opens the web or mobile storefront.
2. The storefront sends authentication requests to the identity service through the
   API gateway.
3. The shopper searches and navigates products from the catalog and search services.
4. The shopper adds products to the cart, which stores active cart state in
   PostgreSQL.
5. Checkout validates prices, promotions, inventory, delivery address, and payment
   intent.
6. The delivery service offers available time slots based on capacity and routing
   rules.
7. The order service confirms the order after payment authorization and publishes
   domain events.
8. Downstream services consume events to reserve stock, schedule delivery, send
   notifications, update analytics, and expose order status to the shopper.

## Runtime Topology

The production system runs on Azure. Public traffic enters through Azure Front
Door and a web application firewall before reaching the ingress controller in
Azure Kubernetes Service. Stateless Java services are implemented with Quarkus and
run as containers in AKS. Stateful data is kept in PostgreSQL, and asynchronous
communication runs through Kafka.

```text
                           +----------------------+
                           |       Shopper        |
                           | Web browser / mobile |
                           +----------+-----------+
                                      |
                                      | HTTPS
                                      v
  +-----------------------------------------------------------------------+
  | Azure Edge                                                            |
  |                                                                       |
  | +------------------+     +------------------+     +----------------+ |
  | | Azure Front Door | --> | Web Application | --> | AKS Ingress    | |
  | | + CDN            |     | Firewall         |     | Controller     | |
  | +------------------+     +------------------+     +-------+--------+ |
  +-----------------------------------------------------------------------+
                                                              |
                                                              v
  +-----------------------------------------------------------------------+
  | Azure Kubernetes Service - ecommerce-prod, Java Quarkus services       |
  |                                                                       |
  | +----------------+      +----------------+      +-------------------+ |
  | | Web Storefront | ---> | API Gateway    | ---> | Identity Service  | |
  | | Quarkus BFF    |      | Quarkus REST   |      | Quarkus OIDC      | |
  | +-------+--------+      +-------+--------+      +---------+---------+ |
  |         |                       |                         |           |
  |         |                       |                         v           |
  |         |                       |              +-------------------+  |
  |         |                       |              | User Profile DB   |  |
  |         |                       |              | Azure PostgreSQL  |  |
  |         |                       |              +-------------------+  |
  |         |                       |                                      |
  |         |                       +------------------+------------------+
  |         |                                          |
  |         v                                          v
  | +----------------+      +----------------+      +-------------------+ |
  | | Catalog        | ---> | Search Service |      | Cart Service      | |
  | | Quarkus REST   |      | Quarkus FTS    |      | Quarkus REST      | |
  | +-------+--------+      +-------+--------+      +---------+---------+ |
  |         |                       |                         |           |
  |         v                       v                         v           |
  | +----------------+      +----------------+      +-------------------+ |
  | | Product DB     |      | Search Index   |      | Cart DB           | |
  | | PostgreSQL     |      | PostgreSQL FTS |      | PostgreSQL        | |
  | +----------------+      +----------------+      +-------------------+ |
  |                                                                       |
  | +----------------+      +----------------+      +-------------------+ |
  | | Pricing and    | ---> | Checkout       | ---> | Payment Service   | |
  | | Quarkus REST   |      | Quarkus Saga   |      | Quarkus adapter   | |
  | +-------+--------+      +-------+--------+      +---------+---------+ |
  |         |                       |                         |           |
  |         v                       v                         v           |
  | +----------------+      +----------------+      +-------------------+ |
  | | Pricing DB     |      | Order DB       |      | External Payment  | |
  | | PostgreSQL     |      | PostgreSQL     |      | Gateway           | |
  | +----------------+      +-------+--------+      +-------------------+ |
  |                                 |                                     |
  |                                 v                                     |
  |                        +----------------+                             |
  |                        | Saga Support   |                             |
  |                        | PostgreSQL     |                             |
  |                        +-------+--------+                             |
  |                                | publish events                       |
  |                                v                                      |
  | +-------------------------------------------------------------------+ |
  | | Kafka Backbone                                                    | |
  | | Kafka topics                                                      | |
  | | order.created, payment.authorized, stock.reserved, slot.booked    | |
  | +----------------+------------------+----------------+---------------+ |
  |                  |                  |                |                 |
  |                  v                  v                v                 |
  | +----------------+      +----------------+      +-------------------+ |
  | | Inventory      |      | Delivery Slot  |      | Notification      | |
  | | Quarkus REST   |      | Quarkus REST   |      | Quarkus worker    | |
  | +-------+--------+      +-------+--------+      +---------+---------+ |
  |         |                       |                         |           |
  |         v                       v                         v           |
  | +----------------+      +----------------+      +-------------------+ |
  | | Inventory DB   |      | Delivery DB    |      | Email / SMS       | |
  | | PostgreSQL     |      | PostgreSQL     |      | Provider          | |
  | +----------------+      +----------------+      +-------------------+ |
  +-----------------------------------------------------------------------+

  +-----------------------------------------------------------------------+
  | Shared Azure Platform Services                                        |
  |                                                                       |
  | Key Vault | Azure Monitor | Log Analytics | App Insights | Private DNS |
  | Private Link | Managed Identities | Azure Policy | Backup Vault        |
  +-----------------------------------------------------------------------+
```

## Service Responsibilities

All backend and browser-facing BFF services are Java applications built with
Quarkus. They use Quarkus REST for HTTP APIs, CDI for application components,
SmallRye Config for environment-specific settings, Quarkus Kafka integration for
event consumers and producers, and Quarkus JDBC or reactive SQL access for
PostgreSQL.

The web storefront is a browser-facing backend-for-frontend that renders product
discovery, account, cart, checkout, payment status, and delivery booking screens.
It calls the API gateway instead of calling internal services directly.

The API gateway centralizes TLS termination, request routing, rate limits,
authentication enforcement, correlation IDs, API versioning, and coarse traffic
policy. It forwards authenticated service requests to internal containers.

The identity service manages login, token validation, session metadata, profile
links, and user consent. It integrates with an external identity provider when
social login or enterprise login is required.

The catalog service owns product metadata, categories, availability flags, and
product media references. It updates the search service asynchronously when product
data changes.

The cart service owns temporary basket state. It stores active carts in
PostgreSQL and emits cart lifecycle events for analytics.

The checkout service starts the checkout saga for price validation, promotion
validation, inventory reservation, payment authorization, order creation, and
delivery slot booking. Saga support stores workflow state in PostgreSQL, publishes
commands and events through Kafka, retries idempotent steps, and emits compensating
actions when a later step fails. The checkout service does not own every downstream
capability; it coordinates the workflow and persists the final order state.

The payment service isolates payment-provider APIs from the rest of the platform.
It handles provider-specific tokens, idempotency keys, retries, provider callbacks,
and payment authorization events.

The inventory service owns stock availability, reservation, release, and stock
adjustment. It consumes order and payment events to keep stock state consistent.

The delivery slot service owns delivery capacity, available slots, route planning
constraints, slot booking, and slot release when checkout expires or payment fails.

The notification service sends transactional email, SMS, and push notifications
for account events, order confirmation, delivery booking, payment failure, and
delivery updates.

The services use a few common Java libraries for shared technical concerns only:
correlation IDs and observability, HTTP error envelopes, Kafka event envelopes,
schema versioning helpers, Quarkus test fixtures, and Maven build conventions.
Business rules, persistence models, and saga decisions remain owned by each
service to avoid coupling through shared domain libraries.

## Messaging And Data Flow

The platform uses synchronous HTTP for customer-facing requests that need an
immediate answer. It uses asynchronous events for cross-service state propagation,
auditing, notifications, analytics, and long-running operational workflows.

```text
Checkout Service
      |
      | 1. validate price, cart, inventory, and delivery constraints
      v
Order DB
      |
      | 2. persist pending order
      v
Saga State
      |
      | 3. persist saga step and emit command/event
      v
Kafka Topics
      |
      +--> order.created -----------> Inventory Service
      |                               reserves stock
      |
      +--> payment.requested -------> Payment Service
      |                               authorizes card or wallet
      |
      +--> delivery.slot.requested -> Delivery Slot Service
      |                               books capacity window
      |
      +--> order.confirmed ---------> Notification Service
      |                               emails shopper
      |
      +--> order.confirmed ---------> Analytics Pipeline
      |                               updates dashboards and forecasts
      |
      +--> checkout.compensate -----> Saga participants
                                      release stock, cancel slot, void payment
```

Each event carries a correlation ID, causation ID, tenant or market identifier,
order ID, saga ID, saga step, and schema version. Consumers are idempotent because
retries and duplicate delivery can happen during saga recovery, broker failover,
or deployment rollouts.

## Deployment Environment

The ecommerce platform is deployed into Azure using separate subscriptions or
resource groups for development, test, staging, and production. Production uses
private networking for service-to-database traffic and exposes only the edge layer
to the public internet.

```text
+---------------------+      +---------------------+      +---------------------+
| Dev Azure Env       | ---> | Test Azure Env      | ---> | Staging Azure Env   |
| AKS dev namespace   |      | AKS test namespace  |      | AKS staging ns      |
+----------+----------+      +----------+----------+      +----------+----------+
           |                            |                            |
           | validated images           | validated images           | validated release
           v                            v                            v
+-------------------------------------------------------------------------------+
| Production Azure Environment                                                   |
|                                                                               |
| Azure Front Door -> WAF -> AKS prod namespaces -> managed data services        |
| Private Link -> PostgreSQL, Kafka, Key Vault                                   |
| Azure Monitor -> App Insights, Log Analytics, alert rules, dashboards          |
+-------------------------------------------------------------------------------+
```

## Software Delivery Pipeline

Software engineers use Extreme Programming with trunk-based development, small
short-lived branches, and pull requests that merge quickly back to the main
branch. They practice pair or mob programming, test-driven development,
continuous refactoring, collective code ownership, and sustainable pace. GenAI
tools assist with design exploration, code generation, refactoring suggestions,
test creation, documentation updates, and pull request summaries. Engineers
remain responsible for reviewing generated output, validating behavior, and
accepting or rejecting changes.

The pipeline produces immutable build artifacts, stores dependencies and packages
in Artifactory, builds Docker images, scans them, pushes them to a registry, and
deploys validated images into Azure automatically.

```text
 +-------------------+
 | Software Engineer |
 | XP + GenAI tools  |
 +---------+---------+
           |
          | commit + push short-lived branch
           v
 +-------------------+       +---------------------+
 | Git Repository    | ----> | Pull Request        |
 | trunk + branches  |       | review + checks     |
 +---------+---------+       +----------+----------+
           |                            |
           | PR opened / updated        | merge to main when green
           v                            v
 +--------------------------------------------------------------------------+
 | CI/CD Pipeline                                                            |
 |                                                                          |
 | 1. restore Maven and tool dependencies from Artifactory                   |
 | 2. compile Java Quarkus services and BFF                                  |
 | 3. run unit, integration, contract, security, and architecture tests      |
 | 4. run SAST, dependency scanning, secret scanning, and license checks     |
 | 5. build container images for changed services                           |
 | 6. generate SBOM and provenance metadata                                 |
 +-------------------------------+------------------------------------------+
                                 |
                                 | signed artifacts and scan results
                                 v
 +-------------------+       +---------------------+       +----------------+
 | Artifactory       | ----> | Docker Registry     | ----> | CD Pipeline    |
 | Maven/cache       |       | ACR or JFrog Docker |       | GitOps/Deploy  |
 | release bundles   |       | immutable images    |       | automated prod |
 +-------------------+       +----------+----------+       +-------+--------+
                                           |                          |
                                          | validated image tags     | deploy manifests
                                           v                          v
                                  +--------------------------------------------+
                                  | Azure Runtime                              |
                                  | AKS, Key Vault, Kafka, PostgreSQL,         |
                                  | Monitor, App Insights                      |
                                  +--------------------------------------------+
```

## CI/CD Stages

The pull request pipeline validates every small change before it merges back to
main. It checks formatting, compilation, unit tests, service-level integration
tests, API contract compatibility, container build reproducibility, dependency
health, and security policy compliance.

After a pull request merges, the trunk pipeline creates deployable artifacts from
the main branch after all automatic checks pass. It publishes versioned
Java application artifacts to Artifactory, creates signed Docker images, stores
SBOMs, and pushes deployable images to Azure Container Registry or the selected
Docker registry.

The deployment pipeline deploys the same immutable image through environments.
Development, test, staging, and production deploy automatically when the required
smoke, contract, migration, performance, and security checks pass. The pipeline
verifies database migration safety, applies Kubernetes manifests or Helm charts,
waits for health checks, and runs post-deployment smoke tests.

```text
Small Change Branch
      |
      v
Pull Request CI
      |
      +--> compile
      +--> unit tests
      +--> integration tests
      +--> contract tests
      +--> static analysis
      +--> dependency and image scan
      |
      v
Merge To Trunk (Main)
      |
      v
Main Branch CI Passed
      |
      v
Release Candidate Build
      |
      +--> publish Maven packages to Artifactory
      +--> build Docker images
      +--> sign images
      +--> publish SBOM and provenance
      +--> push images to Docker registry / Azure Container Registry
      |
      v
Deploy Dev -> Deploy Test -> Deploy Staging -> Deploy Production
      |
      v
Azure Monitor, logs, traces, metrics, alerts, rollback decision
```

## Operational Concerns

Secrets are stored in Azure Key Vault and accessed through managed identities.
Services do not store credentials in container images, source code, Kubernetes
manifests, or application configuration repositories.

Each service emits structured logs, metrics, and traces with correlation IDs so an
order can be followed from storefront interaction through checkout, payment,
inventory reservation, delivery slot booking, notification, and analytics.

Database changes are versioned and deployed through the pipeline. Backward-compatible
migrations are applied before new application code relies on the changed schema.

Container images are immutable. A production deployment references a digest or
validated tag, not a mutable local build. Rollback uses the previous validated
image and compatible database state.

## Summary

This Java and Quarkus ecommerce system separates customer-facing workflows into
focused services, uses PostgreSQL data stores for each ownership boundary, relies
on a Kafka event backbone for cross-service consistency, and supports checkout
sagas for multi-service consistency. A few common Java libraries standardize
technical concerns without centralizing business rules. Its trunk-based delivery
cycle combines human software engineering judgment, GenAI-assisted development,
CI validation, Artifactory package management, Docker image publication, and
automatic Azure deployment.
