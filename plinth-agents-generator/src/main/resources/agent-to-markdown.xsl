<?xml version="1.0" encoding="UTF-8"?>
<!--
  Agent XML → Cursor agent Markdown.
  Adapted from plinth-skills-generator skill-index-to-markdown.xsl /
  skill-reference-to-markdown.xsl for the agents.xsd <agent> document shape.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0"
                exclude-result-prefixes="xsl">

  <xsl:output method="text" encoding="UTF-8" indent="no"/>
  <xsl:strip-space elements="agent metadata authors constraints constraint-list steps step step-constraints step-constraint-list output-format output-format-list safeguards safeguards-list"/>

  <xsl:template match="/agent">
    <xsl:text>---
name: </xsl:text>
    <xsl:value-of select="@id"/>
    <xsl:text>
description: </xsl:text>
    <xsl:value-of select="normalize-space(metadata/description)"/>
    <xsl:if test="normalize-space(metadata/license) != ''">
      <xsl:text>
license: </xsl:text>
      <xsl:value-of select="normalize-space(metadata/license)"/>
    </xsl:if>
    <xsl:if test="metadata/authors/author[normalize-space(.) != ''] or normalize-space(metadata/version) != ''">
      <xsl:text>
metadata:</xsl:text>
      <xsl:if test="metadata/authors/author[normalize-space(.) != '']">
        <xsl:text>
  author: </xsl:text>
        <xsl:call-template name="authors"/>
      </xsl:if>
      <xsl:if test="normalize-space(metadata/version) != ''">
        <xsl:text>
  version: </xsl:text>
        <xsl:value-of select="normalize-space(metadata/version)"/>
      </xsl:if>
    </xsl:if>
    <xsl:if test="normalize-space(metadata/model) != ''">
      <xsl:text>
model: </xsl:text>
      <xsl:value-of select="normalize-space(metadata/model)"/>
    </xsl:if>
    <xsl:text>
---

</xsl:text>
    <xsl:value-of select="normalize-space(role)"/>
    <xsl:text>

</xsl:text>
    <!-- Preserve goal markdown line breaks; trim only leading/trailing whitespace -->
    <xsl:call-template name="trim">
      <xsl:with-param name="text" select="goal"/>
    </xsl:call-template>
    <xsl:text>
</xsl:text>
    <xsl:apply-templates select="constraints"/>
    <xsl:apply-templates select="steps"/>
    <xsl:apply-templates select="output-format"/>
    <xsl:apply-templates select="safeguards"/>
  </xsl:template>

  <xsl:template name="authors">
    <xsl:choose>
      <xsl:when test="metadata/authors/author[normalize-space(.) != '']">
        <xsl:for-each select="metadata/authors/author[normalize-space(.) != '']">
          <xsl:if test="position() &gt; 1">
            <xsl:text>, </xsl:text>
          </xsl:if>
          <xsl:value-of select="normalize-space(.)"/>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="normalize-space(metadata/author)"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Constraints: heading from constraints-description when present (agent convention) -->
  <xsl:template match="constraints">
    <xsl:text>
## </xsl:text>
    <xsl:choose>
      <xsl:when test="normalize-space(constraints-description) != ''">
        <xsl:value-of select="normalize-space(constraints-description)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Constraints</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>

</xsl:text>
    <xsl:for-each select="constraint-list/constraint">
      <xsl:text>- </xsl:text>
      <xsl:value-of select="normalize-space(.)"/>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="steps">
    <xsl:if test="step">
      <xsl:text>
## Steps

</xsl:text>
      <xsl:for-each select="step">
        <xsl:text>### </xsl:text>
        <xsl:if test="@number and normalize-space(@number) != ''">
          <xsl:value-of select="normalize-space(@number)"/>
          <xsl:text>. </xsl:text>
        </xsl:if>
        <xsl:value-of select="normalize-space(step-title)"/>
        <xsl:text>

</xsl:text>
        <xsl:if test="step-content">
          <xsl:call-template name="trim">
            <xsl:with-param name="text" select="step-content"/>
          </xsl:call-template>
          <xsl:text>
</xsl:text>
        </xsl:if>
        <xsl:for-each select="step-constraints/step-constraint-list/step-constraint[normalize-space(.) != '']">
          <xsl:text>- </xsl:text>
          <xsl:value-of select="normalize-space(.)"/>
          <xsl:text>
</xsl:text>
        </xsl:for-each>
        <xsl:text>
</xsl:text>
      </xsl:for-each>
    </xsl:if>
  </xsl:template>

  <xsl:template match="output-format">
    <xsl:if test="output-format-list/output-format-item[normalize-space(.) != '']">
      <xsl:text>
## Output format

</xsl:text>
      <xsl:for-each select="output-format-list/output-format-item[normalize-space(.) != '']">
        <xsl:text>- **</xsl:text>
        <xsl:value-of select="normalize-space(.)"/>
        <xsl:text>**
</xsl:text>
      </xsl:for-each>
    </xsl:if>
  </xsl:template>

  <xsl:template match="safeguards">
    <xsl:if test="safeguards-list/safeguards-item[normalize-space(.) != '']">
      <xsl:text>
## Safeguards

</xsl:text>
      <xsl:for-each select="safeguards-list/safeguards-item[normalize-space(.) != '']">
        <xsl:text>- </xsl:text>
        <xsl:value-of select="normalize-space(.)"/>
        <xsl:text>
</xsl:text>
      </xsl:for-each>
    </xsl:if>
  </xsl:template>

  <!-- Java String.strip()-equivalent for XSLT 1.0 (preserve internal newlines) -->
  <xsl:template name="trim">
    <xsl:param name="text"/>
    <xsl:choose>
      <xsl:when test="$text = ''"/>
      <xsl:when test="starts-with($text, ' ') or starts-with($text, '&#x9;') or starts-with($text, '&#xA;') or starts-with($text, '&#xD;')">
        <xsl:call-template name="trim">
          <xsl:with-param name="text" select="substring($text, 2)"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="trim-trailing">
          <xsl:with-param name="text" select="$text"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="trim-trailing">
    <xsl:param name="text"/>
    <xsl:variable name="last" select="substring($text, string-length($text), 1)"/>
    <xsl:choose>
      <xsl:when test="$text = ''"/>
      <xsl:when test="$last = ' ' or $last = '&#x9;' or $last = '&#xA;' or $last = '&#xD;'">
        <xsl:call-template name="trim-trailing">
          <xsl:with-param name="text" select="substring($text, 1, string-length($text) - 1)"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$text"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
