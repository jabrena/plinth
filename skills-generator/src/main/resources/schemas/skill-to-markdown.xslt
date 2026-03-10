<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="xs">

  <xsl:output method="text" encoding="UTF-8" indent="no"/>

  <xsl:template match="/skill">
    <xsl:text>---
name: </xsl:text>
    <xsl:value-of select="@id"/>
    <xsl:text>
description: </xsl:text>
    <xsl:value-of select="normalize-space(description)"/>
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
    <xsl:apply-templates select="reference"/>
  </xsl:template>

  <xsl:template match="goal">
    <xsl:value-of select="normalize-space(.)"/>
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

  <xsl:template match="reference">
    <xsl:if test="@path">
      <xsl:text>## Reference

For detailed guidance, examples, and constraints, see [</xsl:text>
      <xsl:value-of select="@path"/>
      <xsl:text>](</xsl:text>
      <xsl:value-of select="@path"/>
      <xsl:text>).
</xsl:text>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
