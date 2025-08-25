**Question 1**: What type of Java project is this?

Options:
- Java Library (for publishing to Maven Central/Nexus)
- Java CLI Application (command-line tool)
- Java Microservice (Web service/REST API/Modular monolith)
- Serverless (AWS Lambdas, Azure Functions)
- Java POC (Proof of Concept)
- Other (specify)

---

**Question 2**: Which Java version does your project target?

Options:

- Java 17 (LTS - recommended for new projects)
- Java 21 (LTS - latest LTS version)
- Java 24 (latest features)
- Other (specify version)

---

**Question 3**: What build and quality aspects are important for your project?

Options:
- Format source code (Spotless)
- Maven Enforcer
- Unit Testing (Surefire)
- Unit Testing Reports (Surefire Reports)
- Integration testing (Failsafe)
- Code coverage reporting (JaCoCo)
- Mutation testing (PiTest)
- Security vulnerability scanning (OWASP)
- Security static code analysis (SpotBugs, PMD)
- Sonar
- Version management
- JMH (Java Microbenchmark Harness)

---

**Question 4**:  What is your target coverage threshold?

Options:
- 70% (moderate)
- 80% (recommended)
- 90% (high)
- Custom percentage (specify)

**Note**: This question is only asked if "Code coverage reporting (JaCoCo)" was selected in question 3.

---

**Question 5**: Do you want to configure Sonar/SonarCloud integration?** (y/n)

**Note**: This question is only asked if "Static code analysis (SpotBugs, Sonar)" was selected in question 3.

**If yes, please provide the following information:**

---

**Question 5.1**: What is your Sonar organization identifier?

- For SonarCloud: This is typically your GitHub username or organization name
- For SonarQube: This is your organization key as configured in SonarQube
- Example: `my-github-user` or `my-company-org`

---

**Question 5.2**: What is your Sonar project key?

- For SonarCloud: Usually in format `GITHUB_USER_REPOSITORY_NAME` (e.g., `john-doe_my-java-project`)
- For SonarQube: Custom project key as defined in your SonarQube instance
- Must be unique within your Sonar organization
- Example: `john-doe_awesome-java-lib`

---

**Question 5.3**: What is your Sonar project display name?

- Human-readable name for your project as it appears in Sonar dashboard
- Can contain spaces and special characters
- Example: `Awesome Java Library` or `My Microservice API`

---

**Question 5.4**: Which Sonar service are you using? (conditional)

**Note**: This question is only asked if Sonar configuration was enabled in question 5.

Options:
- SonarCloud (https://sonarcloud.io) - recommended for open source projects
- SonarQube Server (specify your server URL)

**If SonarQube Server**: Please provide your SonarQube server URL (e.g., `https://sonar.mycompany.com`)

---
