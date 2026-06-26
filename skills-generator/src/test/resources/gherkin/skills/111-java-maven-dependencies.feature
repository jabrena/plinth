Feature: Validate changes from usage of Maven dependencies skill

Background:
  Given the skill "111-java-maven-dependencies"

@acceptance-test
Scenario: Add JSpecify and Error Prone + NullAway to Maven demo
  Given the example project "examples/maven/maven-demo"
  And the example project has a baseline "pom.xml"
  And the folder "examples/maven/maven-demo" has no git changes
  And the dependency selection answers are:
    | question                  | answer                           |
    | code-quality dependencies | JSpecify; Error Prone + NullAway |
    | main project package name | info.jab                         |
  When the skill "111-java-maven-dependencies" is applied to "examples/maven/maven-demo"
  Then "./mvnw validate" succeeds in "examples/maven/maven-demo"
  And the "pom.xml" declares these dependency version properties:
    | property                       | propertyValue |
    | jspecify.version               | 1.0.0         |
    | error-prone.version            | 2.35.1        |
    | nullaway.version               | 0.12.0        |
    | maven-plugin-compiler.version  | 3.14.0        |
  And the "pom.xml" declares these selected dependencies:
    | groupId      | artifactId | version             | scope    |
    | org.jspecify | jspecify   | ${jspecify.version} | provided |
  And the "pom.xml" does not declare these unselected dependencies:
    | groupId              | artifactId      |
    | io.vavr              | vavr            |
    | com.tngtech.archunit | archunit-junit5 |
    | org.javamoney        | moneta          |
    | javax.money          | money-api       |
  And the "pom.xml" declares the Maven compiler plugin with centralized version property:
    | groupId                  | artifactId            | property                      | propertyValue |
    | org.apache.maven.plugins | maven-compiler-plugin | maven-plugin-compiler.version | 3.14.0        |
  And the Maven compiler plugin declares these compiler arguments:
    | argument                                                         |
    | -Xlint:all                                                       |
    | -Werror                                                          |
    | -XDcompilePolicy=simple                                          |
    | --should-stop=ifError=FLOW                                       |
    | -Xplugin:ErrorProne                                              |
    | -Xep:NullAway:ERROR                                              |
    | -XepOpt:NullAway:JSpecifyMode=true                               |
    | -XepOpt:NullAway:TreatGeneratedAsUnannotated=true                |
    | -XepOpt:NullAway:CheckOptionalEmptiness=true                     |
    | -XepOpt:NullAway:HandleTestAssertionLibraries=true               |
    | -XepOpt:NullAway:AssertsEnabled=true                             |
    | -XepOpt:NullAway:AnnotatedPackages=info.jab                      |
  And the Maven compiler plugin declares these annotation processor paths:
    | groupId               | artifactId       | version                |
    | com.google.errorprone | error_prone_core | ${error-prone.version} |
    | com.uber.nullaway     | nullaway         | ${nullaway.version}    |
  And ".mvn/jvm.config" contains these JVM arguments in "examples/maven/maven-demo":
    | argument                                                            |
    | --add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED       |
    | --add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED      |
    | --add-exports=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED      |
    | --add-exports=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED     |
    | --add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED    |
    | --add-exports=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED |
    | --add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED      |
    | --add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED      |
    | --add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED        |
    | --add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED        |
  And "./mvnw clean verify" fails during compile in "examples/maven/maven-demo"
  And the verification failure is accepted because the required compiler arguments convert existing warnings into errors:
    | argument   |
    | -Xlint:all |
    | -Werror    |
  And the verification output contains:
    | message                              |
    | COMPILATION WARNING                  |
    | COMPILATION ERROR                    |
    | warnings found and -Werror specified |
  And any git changes produced during skill execution and verification are reset
