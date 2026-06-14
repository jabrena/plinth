Feature: Validate changes from usage of EU Data Act regulation skill

Background:
  Given the skill "806-regulations-eu-data-act"

@acceptance-test
Scenario: Review a Java data-sharing service with EU Data Act controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/806-regulations-eu-data-act"
  And the requested report output path is "examples/regulations/eu-data-act/DATA-ACT-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-data-act" has no git changes
  And the feature request is expected to be developed and released through the described CI/CD pipeline
  And the EU Data Act review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/806-regulations-eu-data-act" is applied to the system description, diagram, and feature request files
  Then the skill reads "references/806-regulations-eu-data-act-chapters-summary.md"
  And the skill reads "references/806-regulations-eu-data-act-engineering-examples.md"
  And the skill reads "assets/reports/806-eu-data-act-engineering-review-report-template.md"
  And the skill frames EU Data Act findings as engineering controls rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-pr-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes service context, possible connected-product or related-service signals, possible data holder signals, user and recipient role signals, data processing service or cloud-switching signals, system owners, data owners, privacy owners, security owners, cloud owners, environments, datasets, metadata, APIs, data stores, providers, dependencies, export paths, and switching pathways
  And the skill escalates applicability, data holder status, user entitlement, data recipient roles, trade-secret decisions, contract interpretation, cloud-switching obligations, international access restrictions, and regulatory interpretation to legal, compliance, privacy, data governance, security, product, procurement, cloud, risk, provider, or business owners
  And the skill reviews Java implementation, configuration, tests, API contracts, DTOs, serializers, schema registries, Kafka contracts, batch exports, object storage layouts, metadata catalogs, access-control rules, request workflows, audit logs, privacy controls, runbooks, cloud contracts, provider exit documentation, monitoring, and release evidence
  And the skill identifies risk signals for missing data inventory, unclear role scope, missing owner handoff, weak access authorization, missing portability APIs, missing export format evidence, incomplete metadata, missing audit logs, unclear cloud-switching support, weak non-personal data safeguards, missing trade-secret or sensitive-data handoff, weak data-sharing request workflow, missing contract evidence, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential EU Data Act violation or non-compliance signals to Data Act reference areas using only the reviewed delivery evidence
  And every potential violation or non-compliance row includes an associated official-source link from "https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854"
  And the skill analyzes the CheckoutService feature request as a pipeline-delivered change that adds delivery instruction data, modifies order database structure, and changes outbound Kafka event data
  And the skill uses Java examples to explain data inventory, access authorization, portability APIs, export formats, interoperability, metadata, audit logs, cloud-switching support, non-personal data safeguards, trade-secret handoffs, data-sharing request workflows, contract evidence, and operational controls for data access requests
  And the skill recommends engineering controls for data inventory, access authorization, request intake, user and recipient role evidence, purpose limitation, portability APIs, machine-readable export formats, metadata catalog entries, schema and event contract evidence, interoperability evidence, audit logs, evidence-safe logging, cloud-switching support, exportable data registers, secure transfer, erasure controls, non-personal data safeguards, mixed-dataset privacy handoff, trade-secret or sensitive-data handoff, contract evidence, complaint routing, database migration approval, Kafka schema compatibility, and release gates
  And the skill reports conclusions and actions using the EU Data Act engineering review report template
  And the skill overwrites the EU Data Act engineering review report at "examples/regulations/eu-data-act/DATA-ACT-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Review a Java data-sharing checkout change with direct-to-main EU Data Act controls
  Given the system description file "examples/diagrams/deployment/system-example-cicd-model.md"
  And the deployment and delivery pipeline diagram file "examples/diagrams/deployment/expected-system-deployment.puml"
  And the simulated feature request file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the local generated skill path ".agents/skills/806-regulations-eu-data-act"
  And the requested report output path is "examples/regulations/eu-data-act/DATA-ACT-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any existing report at the requested output path must be overwritten
  And the folder "examples/regulations/eu-data-act" has no git changes
  And the feature request is expected to be committed directly to main and released through the described CI/CD pipeline
  And the EU Data Act review facts are based only on information present in "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  When the skill ".agents/skills/806-regulations-eu-data-act" is applied to the direct-to-main system description, diagram, and feature request files
  Then the skill reads "references/806-regulations-eu-data-act-chapters-summary.md"
  And the skill reads "references/806-regulations-eu-data-act-engineering-examples.md"
  And the skill reads "assets/reports/806-eu-data-act-engineering-review-report-template.md"
  And the skill frames EU Data Act findings as engineering controls rather than legal advice
  And review findings do not use facts outside "examples/diagrams/deployment/system-example-cicd-model.md" and "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the skill scopes service context, possible connected-product or related-service signals, possible data holder signals, user and recipient role signals, data processing service or cloud-switching signals, system owners, data owners, privacy owners, security owners, cloud owners, environments, datasets, metadata, APIs, data stores, providers, dependencies, export paths, and switching pathways
  And the skill escalates missing pre-merge review, protected-main bypass, applicability, data holder status, user entitlement, data recipient roles, trade-secret decisions, contract interpretation, cloud-switching obligations, international access restrictions, and regulatory interpretation to legal, compliance, privacy, data governance, security, product, procurement, cloud, platform, risk, provider, or business owners
  And the skill reviews Java implementation, configuration, tests, API contracts, DTOs, serializers, schema registries, Kafka contracts, batch exports, object storage layouts, metadata catalogs, access-control rules, request workflows, audit logs, privacy controls, runbooks, cloud contracts, provider exit documentation, monitoring, and release evidence
  And the skill identifies risk signals for missing data inventory, unclear role scope, missing owner handoff, weak access authorization, missing portability APIs, missing export format evidence, incomplete metadata, missing audit logs, unclear cloud-switching support, weak non-personal data safeguards, missing trade-secret or sensitive-data handoff, weak data-sharing request workflow, missing contract evidence, direct-to-main commit policy, database migration, Kafka message contract, CI/CD pipeline, and production side-effect signals
  And the skill maps potential EU Data Act violation or non-compliance signals to Data Act reference areas using only the reviewed direct-to-main delivery evidence
  And every potential violation or non-compliance row includes an associated official-source link from "https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854"
  And the skill analyzes the CheckoutService feature request as a direct-to-main pipeline-delivered change that adds delivery instruction data, modifies order database structure, and changes outbound Kafka event data
  And the skill uses Java examples to explain direct-to-main release gates, data inventory, access authorization, portability APIs, export formats, interoperability, metadata, audit logs, cloud-switching support, non-personal data safeguards, trade-secret handoffs, data-sharing request workflows, contract evidence, and operational controls for data access requests
  And the skill recommends engineering controls for pre-commit review, main-branch protection, data inventory, access authorization, request intake, user and recipient role evidence, purpose limitation, portability APIs, machine-readable export formats, metadata catalog entries, schema and event contract evidence, interoperability evidence, audit logs, evidence-safe logging, cloud-switching support, exportable data registers, secure transfer, erasure controls, non-personal data safeguards, mixed-dataset privacy handoff, trade-secret or sensitive-data handoff, contract evidence, complaint routing, database migration approval, Kafka schema compatibility, and release gates
  And the skill reports conclusions and actions using the EU Data Act engineering review report template
  And the skill overwrites the EU Data Act engineering review report at "examples/regulations/eu-data-act/DATA-ACT-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md"
  And any git changes produced during skill execution and verification are reset
