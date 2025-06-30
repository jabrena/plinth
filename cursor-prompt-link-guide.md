# Creating Markdown Links to Send Prompts to Cursor AI

This guide explains how to create markdown links that can send prompts to Cursor AI, similar to bugbot functionality.

## Overview

While Cursor doesn't have direct URL scheme support for sending prompts like some other tools, there are several approaches to achieve similar functionality:

## Method 1: Using Cursor Rules with Markdown Templates

### 1. Create a Rule Template System

Create a `.cursor/rules/core-rules/prompt-templates-agent.mdc` file:

```markdown
---
description: Handles template-based prompts and quick actions for common development tasks. Activates when users reference specific template keywords or request standardized operations like bug analysis, code review, or documentation generation.
globs: 
alwaysApply: false
---

# Prompt Templates Agent

## Template Keywords

When users mention these keywords, apply the corresponding template:

- `@bugbot` - Analyze code for potential bugs and issues
- `@reviewer` - Perform comprehensive code review
- `@documenter` - Generate documentation for code
- `@optimizer` - Suggest performance optimizations
- `@tester` - Generate test cases

## Bug Analysis Template (@bugbot)

When `@bugbot` is mentioned:

1. Analyze the provided code for:
   - Logic errors
   - Potential runtime exceptions
   - Security vulnerabilities
   - Performance issues
   - Code smell patterns

2. Provide structured output:
   - **Issue Type**: Category of the problem
   - **Severity**: High/Medium/Low
   - **Description**: Clear explanation
   - **Suggested Fix**: Concrete solution
   - **Prevention**: How to avoid similar issues

## Examples

<example>
User: "@bugbot analyze this function"
Response: Structured bug analysis with categorized findings
</example>

<example type="invalid">
User: "@bugbot analyze this function"  
Response: Generic code review without structured bug analysis format
</example>
```

### 2. Create Markdown Documentation with Quick Links

Create a `quick-actions.md` file in your project:

```markdown
# Quick Action Links for Cursor AI

Click these links to quickly access common AI prompts:

## 🐛 Bug Analysis
**Markdown to copy:** `@bugbot analyze the selected code for potential bugs and security issues`

## 📝 Code Review  
**Markdown to copy:** `@reviewer perform a comprehensive code review focusing on best practices and maintainability`

## 📚 Documentation
**Markdown to copy:** `@documenter generate comprehensive documentation for the selected code including examples`

## ⚡ Performance Optimization
**Markdown to copy:** `@optimizer analyze and suggest performance improvements for the selected code`

## 🧪 Test Generation
**Markdown to copy:** `@tester generate comprehensive test cases for the selected code including edge cases`

## Usage Instructions

1. Select the code you want to analyze
2. Copy one of the prompts above
3. Open Cursor Chat (Cmd/Ctrl + L)
4. Paste the prompt and press Enter

## Custom Prompts

You can also create custom prompts by combining keywords:

- `@bugbot @reviewer` - Bug analysis + code review
- `@documenter @tester` - Documentation + test generation
- `@optimizer @reviewer` - Performance review

## Advanced Usage

For more complex scenarios, reference specific files:
```
@bugbot analyze @src/utils/auth.ts for security vulnerabilities
```
```

## Method 2: Using Cursor's @ Symbol System

Cursor has built-in @ symbols that can be used in markdown documentation:

```markdown
# Development Workflow Links

## Quick Actions

### Bug Analysis
```
@Cursor Rules @Files
Analyze the current file for potential bugs, focusing on:
- Logic errors and edge cases
- Security vulnerabilities  
- Performance bottlenecks
- Code maintainability issues
```

### Code Review
```
@Recent Changes @Lint Errors
Perform a comprehensive code review of recent changes:
- Check for adherence to coding standards
- Identify potential improvements
- Suggest refactoring opportunities
```

### Documentation Generation
```
@Code @Definitions
Generate comprehensive documentation including:
- Function/class descriptions
- Parameter explanations
- Usage examples
- Return value documentation
```
```

## Method 3: Creating Custom Cursor Modes

You can create custom modes in Cursor that act like specialized bots:

### 1. Create a modes.json file

Create `.cursor/modes.json`:

```json
{
  "bugbot": {
    "name": "Bug Detective",
    "description": "Specialized in finding and fixing bugs",
    "systemPrompt": "You are a bug detection specialist. When analyzing code, focus on:\n1. Logic errors and edge cases\n2. Security vulnerabilities\n3. Performance issues\n4. Potential runtime exceptions\n\nAlways provide structured output with severity levels and concrete fixes.",
    "model": "claude-3.5-sonnet",
    "tools": ["terminal", "file_operations"]
  },
  "reviewer": {
    "name": "Code Reviewer",
    "description": "Comprehensive code review specialist", 
    "systemPrompt": "You are a senior code reviewer. Provide thorough reviews focusing on:\n1. Code quality and best practices\n2. Architecture and design patterns\n3. Performance optimization\n4. Security considerations\n5. Maintainability and readability",
    "model": "claude-3.5-sonnet",
    "tools": ["file_operations"]
  }
}
```

### 2. Reference in Markdown

```markdown
# Quick AI Assistants

## 🐛 Bug Detective
Switch to Bug Detective mode and ask: "Analyze this code for bugs"

## 👨‍💻 Code Reviewer  
Switch to Code Reviewer mode and ask: "Review this implementation"

## How to Use
1. Open Cursor Chat
2. Select the appropriate mode from the dropdown
3. Paste your code or reference files with @
4. Ask your question
```

## Method 4: Browser Bookmarklets (Advanced)

For a more "link-like" experience, you can create browser bookmarklets:

```javascript
javascript:(function(){
  const prompt = "Analyze the selected code for potential bugs and security issues";
  navigator.clipboard.writeText(prompt).then(() => {
    alert("Prompt copied! Switch to Cursor and paste in chat.");
  });
})();
```

Save this as a bookmark with the title "Copy Bugbot Prompt".

## Best Practices

1. **Use Descriptive Keywords**: Make your prompt templates easy to remember
2. **Structure Your Prompts**: Use consistent formatting for better AI responses
3. **Version Control**: Keep your rules and templates in version control
4. **Team Sharing**: Share prompt templates with your team through documentation
5. **Iterate and Improve**: Refine your prompts based on AI responses

## Example Implementation

Here's a complete example of a markdown file with "bugbot-like" functionality:

```markdown
# AI Assistant Quick Actions

## 🚀 Quick Start
1. Select code in Cursor
2. Copy a prompt below
3. Open Chat (Cmd/Ctrl + L)
4. Paste and run

## 🐛 Bug Detection
```
@Cursor Rules
Act as a bug detection specialist. Analyze the selected code and provide:

**BUGS FOUND:**
- Issue type and severity
- Exact location and description  
- Recommended fix
- Prevention strategy

Focus on: logic errors, security issues, performance problems, edge cases.
```

## 📋 Code Review
```
@Recent Changes @Lint Errors
Perform a senior-level code review covering:

**REVIEW CHECKLIST:**
- [ ] Code quality and standards
- [ ] Security considerations
- [ ] Performance implications
- [ ] Maintainability
- [ ] Test coverage needs

Provide specific, actionable feedback.
```

## 📖 Documentation
```
@Definitions @Code
Generate comprehensive documentation:

**DOCUMENTATION STRUCTURE:**
- Overview and purpose
- Parameters and return values
- Usage examples
- Edge cases and limitations
- Related functions/classes

Use clear, professional language.
```
```

This approach gives you bugbot-like functionality within Cursor's existing framework, allowing you to create reusable, shareable prompts that can be easily accessed through markdown documentation.