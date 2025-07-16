# Project Dependencies Assessment

I need to understand what dependencies to add to enhance your project's code quality and safety. I'll ask you a few targeted questions:

## 1. JSpecify Nullness Annotations

**Question**: Do you want to add JSpecify for enhanced nullness annotations?

JSpecify provides modern nullness annotations that help prevent null pointer exceptions at compile time. It's particularly useful for new projects or those looking to improve null safety.

**Options**:
- **y** - Add JSpecify dependency (recommended for new projects)
- **n** - Skip JSpecify dependency

**Recommendation**: Choose 'y' for better null safety and modern annotation support.

## 2. Enhanced Compiler Analysis (Conditional)

**Note**: This question is asked ONLY if you selected 'y' for JSpecify.

**Question**: Do you want to enable enhanced compiler analysis with Error Prone and NullAway?

This adds Error Prone static analysis and NullAway nullness checking to your build process. It will catch more potential issues at compile time but may initially show warnings in existing code.

**Options**:
- **y** - Enable enhanced analysis with Error Prone and NullAway (recommended)
- **n** - Just add JSpecify dependency without enhanced analysis

**Recommendation**: Choose 'y' to get the full benefit of nullness checking and additional static analysis.

## 3. Project Package Name (Conditional)

**Note**: This question is asked ONLY if you selected enhanced compiler analysis.

**Question**: What is your main project package name?

This is needed to configure NullAway to analyze your code. For example, if your classes are in `com.example.myproject`, enter `com.example.myproject`.

**Format**: Use dot notation (e.g., `com.example.myproject` or `org.mycompany.myapp`)

**Example**: `com.example.myproject`
