<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="UTF-8"/>
    <xsl:strip-space elements="prompt metadata tags example code-examples good-example bad-example output-format"/>

    <xsl:template match="/prompt">
        <!-- Common frontmatter and header -->
        <xsl:text>---
description:</xsl:text>
        <xsl:if test="normalize-space(metadata/cursor-ai/description)">
            <xsl:text> </xsl:text><xsl:value-of select="normalize-space(metadata/cursor-ai/description)"/>
        </xsl:if>
        <xsl:text>
globs:</xsl:text>
        <xsl:if test="normalize-space(metadata/cursor-ai/globs)">
            <xsl:text> </xsl:text><xsl:value-of select="normalize-space(metadata/cursor-ai/globs)"/>
        </xsl:if>
        <xsl:text>
alwaysApply: </xsl:text><xsl:value-of select="normalize-space(metadata/cursor-ai/always-apply)"/>
        <xsl:text>
---
# </xsl:text><xsl:value-of select="metadata/title"/>
        <xsl:text>

## Role

</xsl:text><xsl:value-of select="role"/>
        <!-- Process goal (Instructions for AI) after role -->
        <xsl:apply-templates select="goal"/>
        <!-- Apply constraints template if present -->
        <xsl:apply-templates select="constraints"/>

        <!-- Examples section with auto-generated table of contents -->
        <xsl:if test="examples/toc[@auto-generate='true']">
            <xsl:text>
## Examples

### Table of contents

</xsl:text>
            <xsl:for-each select="examples/example">
                <xsl:text>- Example </xsl:text><xsl:value-of select="@number"/><xsl:text>: </xsl:text><xsl:value-of select="normalize-space(example-header/example-title)"/>
                <xsl:text>
</xsl:text>
            </xsl:for-each>
        </xsl:if>

        <!-- Process all content sections (goal already processed above) -->
        <xsl:apply-templates select="examples | output-format | safeguards"/>
    </xsl:template>

    <!-- Examples container template -->
    <xsl:template match="examples">
        <xsl:apply-templates select="example"/>
    </xsl:template>

    <!-- Example section template -->
    <xsl:template match="example">
        <xsl:text>
### Example </xsl:text><xsl:value-of select="@number"/><xsl:text>: </xsl:text><xsl:value-of select="normalize-space(example-header/example-title)"/>
        <xsl:text>

Title: </xsl:text><xsl:value-of select="normalize-space(example-header/example-subtitle)"/>
        <xsl:text>
Description: </xsl:text>        <xsl:value-of select="normalize-space(example-description)"/>



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
            <xsl:if test="code-examples/bad-example/@last-item = 'true'">
                <xsl:text>
</xsl:text>
            </xsl:if>
        </xsl:if>
        <xsl:if test="position() != last()">
            <xsl:text>
</xsl:text>
        </xsl:if>
    </xsl:template>

    <!-- Goal template - simple goal statement -->
    <xsl:template match="goal">
        <xsl:text>

## Instructions for AI

</xsl:text>
        <xsl:call-template name="trim-goal-content">
            <xsl:with-param name="content" select="."/>
        </xsl:call-template>
    </xsl:template>

    <!-- Output format section template -->
    <xsl:template match="output-format">
        <xsl:text>
## Output Format

</xsl:text>
        <xsl:for-each select="output-format-list/output-format-item">
            <xsl:text>- </xsl:text><xsl:value-of select="normalize-space(.)"/>
            <xsl:text>
</xsl:text>
        </xsl:for-each>
    </xsl:template>

    <!-- Safeguards section template -->
    <xsl:template match="safeguards">
        <xsl:text>
## Safeguards

</xsl:text>
        <xsl:for-each select="safeguards-list/safeguards-item">
            <xsl:text>- </xsl:text><xsl:value-of select="normalize-space(.)"/>
            <xsl:text>
</xsl:text>
        </xsl:for-each>
    </xsl:template>

    <!-- Constraints template -->
    <xsl:template match="constraints">
        <xsl:text>
## Constraints

</xsl:text>
        <xsl:if test="constraints-description">
            <xsl:value-of select="normalize-space(constraints-description)"/>
            <xsl:text>

</xsl:text>
        </xsl:if>
        <xsl:for-each select="constraint-list/constraint">
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

    <!-- Template to trim goal content while preserving paragraph structure -->
    <xsl:template name="trim-goal-content">
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
        <xsl:call-template name="remove-goal-indentation">
            <xsl:with-param name="text" select="$trimmed-both"/>
        </xsl:call-template>
    </xsl:template>

    <!-- Remove leading spaces from each line while preserving paragraph structure -->
    <xsl:template name="remove-goal-indentation">
        <xsl:param name="text"/>
        <xsl:choose>
            <xsl:when test="contains($text, '&#10;')">
                <xsl:variable name="line" select="substring-before($text, '&#10;')"/>
                <xsl:variable name="rest" select="substring-after($text, '&#10;')"/>
                <xsl:call-template name="ltrim">
                    <xsl:with-param name="string" select="$line"/>
                </xsl:call-template>
                <xsl:text>&#10;</xsl:text>
                <xsl:call-template name="remove-goal-indentation">
                    <xsl:with-param name="text" select="$rest"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="ltrim">
                    <xsl:with-param name="string" select="$text"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- Left trim (remove leading spaces) -->
    <xsl:template name="ltrim">
        <xsl:param name="string"/>
        <xsl:choose>
            <xsl:when test="substring($string, 1, 1) = ' '">
                <xsl:call-template name="ltrim">
                    <xsl:with-param name="string" select="substring($string, 2)"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
