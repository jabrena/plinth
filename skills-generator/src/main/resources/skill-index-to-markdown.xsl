<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="xs">

  <xsl:output method="text" encoding="UTF-8" indent="no"/>

  <!-- Support both legacy <skill> and PML <prompt> root elements -->
  <xsl:template match="/skill">
    <xsl:call-template name="generate-skill-markdown">
      <xsl:with-param name="description" select="normalize-space(description)"/>
      <xsl:with-param name="reference-paths" select="reference/@path"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="/prompt">
    <xsl:call-template name="generate-skill-markdown">
      <xsl:with-param name="description" select="normalize-space(metadata/description)"/>
      <xsl:with-param name="reference-paths" select="references/reference-list/reference"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="generate-skill-markdown">
    <xsl:param name="description"/>
    <xsl:param name="reference-paths"/>
    <xsl:text>---
name: </xsl:text>
    <xsl:value-of select="@id"/>
    <xsl:text>
description: </xsl:text>
    <xsl:value-of select="$description"/>
    <xsl:text>
license: </xsl:text>
    <xsl:value-of select="metadata/license"/>
    <xsl:text>
metadata:
  author: </xsl:text>
    <xsl:value-of select="metadata/author"/>
    <xsl:text>
  version: </xsl:text>
    <xsl:value-of select="metadata/version"/>
    <xsl:text>
---
# </xsl:text>
    <xsl:value-of select="normalize-space(title)"/>
    <xsl:text>
</xsl:text>
    <xsl:apply-templates select="goal"/>
    <xsl:apply-templates select="constraints"/>
    <xsl:apply-templates select="triggers"/>
    <xsl:call-template name="emit-references">
      <xsl:with-param name="reference-paths" select="$reference-paths"/>
    </xsl:call-template>
  </xsl:template>

  <!-- Preserve line breaks: do not use normalize-space() which collapses newlines -->
  <!-- Single trailing newline: goal CDATA already ends with \n, so one more = one blank line before constraints -->
  <xsl:template match="goal">
    <xsl:value-of select="."/>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <!-- Constraints: matches .cursor/rules rendering (## Constraints, list items with - ) -->
  <xsl:template match="constraints">
    <xsl:text>## Constraints

</xsl:text>
    <xsl:if test="constraints-description">
      <xsl:value-of select="normalize-space(constraints-description)"/>
      <xsl:text>

</xsl:text>
    </xsl:if>
    <xsl:for-each select="constraint-list/constraint">
      <xsl:text>- </xsl:text>
      <xsl:value-of select="normalize-space(.)"/>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <!-- Triggers: When to use this skill (PML triggers/trigger-list/trigger or legacy equivalent) -->
  <xsl:template match="triggers">
    <xsl:if test="trigger-list/trigger[normalize-space(.) != '']">
      <xsl:text>## When to use this skill

</xsl:text>
      <xsl:for-each select="trigger-list/trigger[normalize-space(.) != '']">
        <xsl:text>- </xsl:text>
        <xsl:value-of select="normalize-space(.)"/>
        <xsl:text>
</xsl:text>
      </xsl:for-each>
      <xsl:text>
</xsl:text>
    </xsl:if>
  </xsl:template>

  <!-- Legacy: reference with @path attribute -->
  <xsl:template match="reference">
    <xsl:if test="@path">
      <xsl:call-template name="emit-single-reference">
        <xsl:with-param name="path" select="@path"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <!-- Emit reference section from path list (PML references/reference-list/reference or legacy reference/@path) -->
  <xsl:template name="emit-references">
    <xsl:param name="reference-paths"/>
    <xsl:for-each select="$reference-paths">
      <xsl:variable name="path" select="normalize-space(.)"/>
      <xsl:if test="string-length($path) &gt; 0">
        <xsl:call-template name="emit-single-reference">
          <xsl:with-param name="path" select="$path"/>
        </xsl:call-template>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="emit-single-reference">
    <xsl:param name="path"/>
    <xsl:text>## Reference

For detailed guidance, examples, and constraints, see [</xsl:text>
    <xsl:value-of select="$path"/>
    <xsl:text>](</xsl:text>
    <xsl:value-of select="$path"/>
    <xsl:text>).
</xsl:text>
  </xsl:template>

</xsl:stylesheet>
