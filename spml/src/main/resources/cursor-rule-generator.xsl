<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="UTF-8"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="/system-prompt">
        <!-- Common frontmatter and header -->
        <xsl:text>---
description:</xsl:text>
        <xsl:if test="normalize-space(metadata/description)">
            <xsl:text> </xsl:text><xsl:value-of select="normalize-space(metadata/description)"/>
        </xsl:if>
        <xsl:text>
globs:</xsl:text>
        <xsl:if test="normalize-space(metadata/globs)">
            <xsl:text> </xsl:text><xsl:value-of select="normalize-space(metadata/globs)"/>
        </xsl:if>
        <xsl:text>
alwaysApply: </xsl:text><xsl:value-of select="normalize-space(metadata/always-apply)"/>
        <xsl:text>
---
# </xsl:text><xsl:value-of select="normalize-space(header/title)"/>
        <xsl:text>

## System prompt characterization

Role definition: </xsl:text><xsl:value-of select="normalize-space(system-characterization/role-definition)"/>
        <xsl:text>

## Description

</xsl:text><xsl:value-of select="normalize-space(description)"/>

        <!-- Table of contents (if present) -->
        <xsl:choose>
            <!-- Handle new toc element with auto-generation -->
            <xsl:when test="toc[@auto-generate='true']">
                <xsl:text>

## Table of contents

</xsl:text>
                <xsl:for-each select="content-sections/rule-section">
                    <xsl:text>- Rule </xsl:text><xsl:value-of select="@number"/><xsl:text>: </xsl:text><xsl:value-of select="normalize-space(rule-header/rule-title)"/>
                    <xsl:text>
</xsl:text>
                </xsl:for-each>
            </xsl:when>
            <!-- Handle manual toc element -->
            <xsl:when test="toc/toc-item">
                <xsl:text>

## Table of contents

</xsl:text>
                <xsl:for-each select="toc/toc-item">
                    <xsl:text>- </xsl:text><xsl:value-of select="normalize-space(.)"/>
                    <xsl:text>
</xsl:text>
                </xsl:for-each>
            </xsl:when>
            <!-- Handle legacy table-of-contents element -->
            <xsl:when test="table-of-contents/toc-item">
                <xsl:text>

## Table of contents

</xsl:text>
                <xsl:for-each select="table-of-contents/toc-item">
                    <xsl:text>- </xsl:text><xsl:value-of select="normalize-space(.)"/>
                    <xsl:text>
</xsl:text>
                </xsl:for-each>
            </xsl:when>
        </xsl:choose>

        <!-- Process all content sections -->
        <xsl:apply-templates select="content-sections/*"/>
    </xsl:template>

    <!-- Rule section template -->
    <xsl:template match="rule-section">
        <xsl:text>
## Rule </xsl:text><xsl:value-of select="@number"/><xsl:text>: </xsl:text><xsl:value-of select="normalize-space(rule-header/rule-title)"/>
        <xsl:text>

Title: </xsl:text><xsl:value-of select="normalize-space(rule-header/rule-subtitle)"/>
        <xsl:text>
Description: </xsl:text>        <xsl:value-of select="normalize-space(rule-description)"/>

        <xsl:for-each select="rule-notes">
            <xsl:text>

</xsl:text>
            <xsl:if test="notes-title">
                <xsl:text>**</xsl:text><xsl:value-of select="normalize-space(notes-title)"/>
                <xsl:text>**

</xsl:text>
            </xsl:if>
            <xsl:for-each select="note-item">
                <xsl:text>*   **</xsl:text><xsl:value-of select="normalize-space(note-term)"/>
                <xsl:text>**: </xsl:text><xsl:value-of select="normalize-space(note-description)"/>
                <xsl:text>
</xsl:text>
            </xsl:for-each>
        </xsl:for-each>

        <xsl:if test="code-examples/good-example">
            <xsl:text>

**Good example:**

```</xsl:text>
            <xsl:if test="code-examples/good-example/code-block/@language">
                <xsl:value-of select="code-examples/good-example/code-block/@language"/>
            </xsl:if>
            <xsl:text>
</xsl:text>
            <xsl:call-template name="trim-code-block">
                <xsl:with-param name="content" select="code-examples/good-example/code-block"/>
            </xsl:call-template>
            <xsl:text>
```</xsl:text>
        </xsl:if>

        <xsl:if test="code-examples/bad-example">
            <xsl:text>

**Bad example:**

```</xsl:text>
            <xsl:if test="code-examples/bad-example/code-block/@language">
                <xsl:value-of select="code-examples/bad-example/code-block/@language"/>
            </xsl:if>
            <xsl:text>
</xsl:text>
            <xsl:call-template name="trim-code-block">
                <xsl:with-param name="content" select="code-examples/bad-example/code-block"/>
            </xsl:call-template>
            <xsl:text>
```</xsl:text>
        </xsl:if>
        <xsl:if test="position() != last()">
            <xsl:text>
</xsl:text>
        </xsl:if>
    </xsl:template>

    <!-- Template section template -->
    <xsl:template match="template-section">
        <xsl:text>

## </xsl:text><xsl:value-of select="normalize-space(template-header/template-title)"/>
        <xsl:text>:

</xsl:text><xsl:value-of select="normalize-space(template-description)"/>
        <xsl:text>

---
</xsl:text><xsl:value-of select="template-content/code-block"/>
    </xsl:template>

    <!-- Question section template -->
    <xsl:template match="question-section">
        <xsl:text>

## </xsl:text><xsl:value-of select="normalize-space(question-header/question-title)"/>
        <xsl:if test="question-header/question-subtitle">
            <xsl:text>: </xsl:text><xsl:value-of select="normalize-space(question-header/question-subtitle)"/>
        </xsl:if>
        <xsl:text>

</xsl:text>
        <xsl:if test="question-description">
            <xsl:value-of select="normalize-space(question-description)"/>
            <xsl:text>

</xsl:text>
        </xsl:if>
        <xsl:for-each select="question-items/question-item">
            <xsl:text>- </xsl:text><xsl:value-of select="normalize-space(option-text)"/>
            <xsl:if test="option-description">
                <xsl:text>: </xsl:text><xsl:value-of select="normalize-space(option-description)"/>
            </xsl:if>
            <xsl:text>
</xsl:text>
        </xsl:for-each>
    </xsl:template>

    <!-- Workflow section template -->
    <xsl:template match="workflow-section">
        <xsl:text>

## </xsl:text><xsl:value-of select="normalize-space(workflow-header/workflow-title)"/>
        <xsl:if test="workflow-header/workflow-subtitle">
            <xsl:text>: </xsl:text><xsl:value-of select="normalize-space(workflow-header/workflow-subtitle)"/>
        </xsl:if>
        <xsl:text>

</xsl:text>
        <xsl:if test="workflow-description">
            <xsl:value-of select="normalize-space(workflow-description)"/>
            <xsl:text>

</xsl:text>
        </xsl:if>
        <xsl:for-each select="workflow-steps/workflow-step">
            <xsl:text>### Step </xsl:text><xsl:value-of select="@number"/><xsl:text>: </xsl:text><xsl:value-of select="normalize-space(step-header/step-title)"/>
            <xsl:text>

</xsl:text><xsl:value-of select="normalize-space(step-description)"/>
            <xsl:if test="step-content/code-block">
                <xsl:text>

```</xsl:text>
                <xsl:if test="step-content/code-block/@language">
                    <xsl:value-of select="step-content/code-block/@language"/>
                </xsl:if>
                <xsl:text>
</xsl:text>
                <xsl:call-template name="trim-code-block">
                    <xsl:with-param name="content" select="step-content/code-block"/>
                </xsl:call-template>
                <xsl:text>
```</xsl:text>
            </xsl:if>
            <xsl:text>

</xsl:text>
        </xsl:for-each>
    </xsl:template>

    <!-- Instruction section template - handles both complex and simple structures -->
    <xsl:template match="instruction-section">
        <xsl:text>

## </xsl:text>
        <xsl:choose>
            <xsl:when test="instruction-header/instruction-title">
                <xsl:value-of select="normalize-space(instruction-header/instruction-title)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="normalize-space(instruction-title)"/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text>

</xsl:text>
        <xsl:if test="instruction-description">
            <xsl:value-of select="normalize-space(instruction-description)"/>
            <xsl:choose>
                <xsl:when test="normalize-space(instruction-description) = '### Template Boundaries:'">
                    <xsl:text>

</xsl:text>
                </xsl:when>
                <xsl:when test="substring(normalize-space(instruction-description), string-length(normalize-space(instruction-description))) = ':'">
                    <xsl:text>
</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>

</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
        <xsl:apply-templates select="restrictions"/>
        <xsl:for-each select="instruction-rules/instruction-rule">
            <xsl:text>- </xsl:text><xsl:value-of select="normalize-space(.)"/>
            <xsl:text>
</xsl:text>
        </xsl:for-each>
    </xsl:template>

    <!-- Output requirements section template - handles both complex and simple structures -->
    <xsl:template match="output-requirements-section">
        <xsl:text>

## </xsl:text>
        <xsl:choose>
            <xsl:when test="output-requirements-header/output-requirements-title">
                <xsl:value-of select="normalize-space(output-requirements-header/output-requirements-title)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="normalize-space(output-requirements-title)"/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text>

</xsl:text>
        <xsl:if test="output-requirements-description">
            <xsl:value-of select="normalize-space(output-requirements-description)"/>
            <xsl:text>

</xsl:text>
        </xsl:if>
        <xsl:for-each select="output-requirements-rules/output-requirements-rule">
            <xsl:text>- </xsl:text><xsl:value-of select="normalize-space(.)"/>
            <xsl:text>
</xsl:text>
        </xsl:for-each>
    </xsl:template>

    <!-- Restrictions template -->
    <xsl:template match="restrictions">
        <xsl:text>### Restrictions

</xsl:text>
        <xsl:if test="restrictions-description">
            <xsl:value-of select="normalize-space(restrictions-description)"/>
            <xsl:text>

</xsl:text>
        </xsl:if>
        <xsl:for-each select="restriction-list/restriction">
            <xsl:text>- </xsl:text><xsl:value-of select="normalize-space(.)"/>
            <xsl:text>
</xsl:text>
        </xsl:for-each>
    </xsl:template>

    <!-- Code block trimming utilities (reused from maven-best-practices-generator) -->
    <xsl:template name="trim-code-block">
        <xsl:param name="content"/>
        <xsl:variable name="trimmed-start">
            <xsl:choose>
                <xsl:when test="starts-with($content, '&#10;')">
                    <xsl:value-of select="substring($content, 2)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$content"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="trimmed-both">
            <xsl:choose>
                <xsl:when test="substring($trimmed-start, string-length($trimmed-start)) = '&#10;'">
                    <xsl:value-of select="substring($trimmed-start, 1, string-length($trimmed-start) - 1)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$trimmed-start"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:call-template name="remove-trailing-spaces">
            <xsl:with-param name="text" select="$trimmed-both"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="remove-trailing-spaces">
        <xsl:param name="text"/>
        <xsl:choose>
            <xsl:when test="contains($text, '&#10;')">
                <xsl:variable name="line" select="substring-before($text, '&#10;')"/>
                <xsl:variable name="rest" select="substring-after($text, '&#10;')"/>
                <xsl:call-template name="rtrim">
                    <xsl:with-param name="string" select="$line"/>
                </xsl:call-template>
                <xsl:text>&#10;</xsl:text>
                <xsl:call-template name="remove-trailing-spaces">
                    <xsl:with-param name="text" select="$rest"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="rtrim">
                    <xsl:with-param name="string" select="$text"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="rtrim">
        <xsl:param name="string"/>
        <xsl:choose>
            <xsl:when test="substring($string, string-length($string)) = ' '">
                <xsl:call-template name="rtrim">
                    <xsl:with-param name="string" select="substring($string, 1, string-length($string) - 1)"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
