<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="UTF-8"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="/system-prompt">
        <xsl:text># </xsl:text><xsl:value-of select="normalize-space(header/title)"/>
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

```</xsl:text><xsl:value-of select="code-examples/good-example/code-block/@language"/><xsl:text>
</xsl:text><xsl:value-of select="code-examples/good-example/code-block"/><xsl:text>
```</xsl:text>
            </xsl:if>

            <xsl:if test="code-examples/bad-example">
                <xsl:text>

**Bad Example:**

```</xsl:text><xsl:value-of select="code-examples/bad-example/code-block/@language"/><xsl:text>
</xsl:text><xsl:value-of select="code-examples/bad-example/code-block"/><xsl:text>
```</xsl:text>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
