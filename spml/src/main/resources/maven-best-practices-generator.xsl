<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="UTF-8"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="/system-prompt">
        <xsl:text>---
description: </xsl:text><xsl:value-of select="normalize-space(metadata/description)"/>
        <xsl:text>
globs: </xsl:text><xsl:value-of select="normalize-space(metadata/globs)"/>
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
        <xsl:text>

## Table of contents

</xsl:text>
        <xsl:for-each select="table-of-contents/toc-item">
            <xsl:text>- </xsl:text><xsl:value-of select="normalize-space(.)"/>
            <xsl:text>
</xsl:text>
        </xsl:for-each>

        <xsl:for-each select="content-sections/rule-section">
            <xsl:text>
## Rule </xsl:text><xsl:value-of select="@number"/><xsl:text>: </xsl:text><xsl:value-of select="normalize-space(rule-header/rule-title)"/>
            <xsl:text>

Title: </xsl:text><xsl:value-of select="normalize-space(rule-header/rule-subtitle)"/>
            <xsl:text>
Description: </xsl:text><xsl:value-of select="normalize-space(rule-description)"/>

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

**Bad Example:**

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
        </xsl:for-each>
        <xsl:text>
</xsl:text>
    </xsl:template>

    <!-- Template to trim leading and trailing newlines from code blocks -->
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
        <!-- Remove trailing spaces from each line -->
        <xsl:call-template name="remove-trailing-spaces">
            <xsl:with-param name="text" select="$trimmed-both"/>
        </xsl:call-template>
    </xsl:template>

    <!-- Template to remove trailing spaces from text -->
    <xsl:template name="remove-trailing-spaces">
        <xsl:param name="text"/>
        <xsl:choose>
            <xsl:when test="contains($text, '&#10;')">
                <xsl:variable name="line" select="substring-before($text, '&#10;')"/>
                <xsl:variable name="rest" select="substring-after($text, '&#10;')"/>
                <!-- Trim trailing spaces from this line -->
                <xsl:call-template name="rtrim">
                    <xsl:with-param name="string" select="$line"/>
                </xsl:call-template>
                <xsl:text>&#10;</xsl:text>
                <xsl:call-template name="remove-trailing-spaces">
                    <xsl:with-param name="text" select="$rest"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <!-- Last line, trim trailing spaces -->
                <xsl:call-template name="rtrim">
                    <xsl:with-param name="string" select="$text"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- Template to trim trailing spaces from a string -->
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
