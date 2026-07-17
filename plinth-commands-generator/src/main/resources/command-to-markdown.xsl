<?xml version="1.0" encoding="UTF-8"?>
<!--
  Command XML → Cursor slash-command Markdown.
  Mirrors agent-to-markdown.xsl for the commands.xsd <command> document shape.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0"
                exclude-result-prefixes="xsl">

  <xsl:output method="text" encoding="UTF-8" indent="no"/>
  <xsl:strip-space elements="command accepted-inputs associations delegation ownership execution-contract branch-worktree-gate workflow output safeguards"/>

  <xsl:template match="/command">
    <xsl:text># </xsl:text>
    <xsl:value-of select="@id"/>
    <xsl:text>

</xsl:text>
    <xsl:apply-templates select="purpose"/>
    <xsl:apply-templates select="usage"/>
    <xsl:apply-templates select="accepted-inputs"/>
    <xsl:apply-templates select="owning-agent"/>
    <xsl:apply-templates select="delegation"/>
    <xsl:apply-templates select="ownership"/>
    <xsl:apply-templates select="associations"/>
    <xsl:apply-templates select="tool-selection"/>
    <xsl:apply-templates select="workflow-position"/>
    <xsl:apply-templates select="execution-contract"/>
    <xsl:apply-templates select="branch-worktree-gate"/>
    <xsl:apply-templates select="workflow"/>
    <xsl:apply-templates select="output"/>
    <xsl:apply-templates select="safeguards"/>
  </xsl:template>

  <xsl:template match="purpose">
    <xsl:text>## Purpose

</xsl:text>
    <xsl:call-template name="trim">
      <xsl:with-param name="text" select="."/>
    </xsl:call-template>
    <xsl:text>

</xsl:text>
  </xsl:template>

  <xsl:template match="usage">
    <xsl:text>## Usage

```text
</xsl:text>
    <xsl:call-template name="trim">
      <xsl:with-param name="text" select="."/>
    </xsl:call-template>
    <xsl:text>
```

</xsl:text>
  </xsl:template>

  <xsl:template match="accepted-inputs">
    <xsl:text>## </xsl:text>
    <xsl:choose>
      <xsl:when test="normalize-space(@heading) != ''">
        <xsl:value-of select="normalize-space(@heading)"/>
      </xsl:when>
      <xsl:when test="../@kind = 'cli'">
        <xsl:text>Inputs</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Accepted Inputs</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>

</xsl:text>
    <xsl:for-each select="input">
      <xsl:text>- </xsl:text>
      <xsl:call-template name="trim">
        <xsl:with-param name="text" select="."/>
      </xsl:call-template>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
    <xsl:for-each select="note">
      <xsl:text>
</xsl:text>
      <xsl:call-template name="trim">
        <xsl:with-param name="text" select="."/>
      </xsl:call-template>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="owning-agent">
    <xsl:text>## </xsl:text>
    <xsl:choose>
      <xsl:when test="normalize-space(@heading) != ''">
        <xsl:value-of select="normalize-space(@heading)"/>
      </xsl:when>
      <xsl:when test="../@kind = 'cli'">
        <xsl:text>Owner</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Owning Agent</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>

</xsl:text>
    <xsl:call-template name="agent-mention">
      <xsl:with-param name="id" select="normalize-space(.)"/>
    </xsl:call-template>
    <xsl:text>

</xsl:text>
  </xsl:template>

  <xsl:template match="delegation">
    <xsl:text>## </xsl:text>
    <xsl:choose>
      <xsl:when test="normalize-space(@heading) != ''">
        <xsl:value-of select="normalize-space(@heading)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Owner and delegation</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>

</xsl:text>
    <xsl:for-each select="item">
      <xsl:text>- </xsl:text>
      <xsl:call-template name="trim">
        <xsl:with-param name="text" select="."/>
      </xsl:call-template>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="ownership">
    <xsl:text>## </xsl:text>
    <xsl:choose>
      <xsl:when test="normalize-space(@heading) != ''">
        <xsl:value-of select="normalize-space(@heading)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Owner and skills</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>

</xsl:text>
    <xsl:for-each select="item">
      <xsl:text>- </xsl:text>
      <xsl:call-template name="trim">
        <xsl:with-param name="text" select="."/>
      </xsl:call-template>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="associations">
    <xsl:variable name="heading">
      <xsl:choose>
        <xsl:when test="normalize-space(@heading) != ''">
          <xsl:value-of select="normalize-space(@heading)"/>
        </xsl:when>
        <xsl:when test="count(association) = 1 and association/@refkind = 'skill'">
          <xsl:text>Associated Skill</xsl:text>
        </xsl:when>
        <xsl:when test="association/@refkind = 'capability'">
          <xsl:text>Associated Capabilities</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>Associated Skills</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:text>## </xsl:text>
    <xsl:value-of select="$heading"/>
    <xsl:text>

</xsl:text>
    <xsl:choose>
      <xsl:when test="$heading = 'Associated Skill' and count(association) = 1">
        <xsl:call-template name="association-text">
          <xsl:with-param name="node" select="association[1]"/>
        </xsl:call-template>
        <xsl:text>

</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:for-each select="association">
          <xsl:text>- </xsl:text>
          <xsl:call-template name="association-text">
            <xsl:with-param name="node" select="."/>
          </xsl:call-template>
          <xsl:text>
</xsl:text>
        </xsl:for-each>
        <xsl:text>
</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="association-text">
    <xsl:param name="node"/>
    <xsl:variable name="body" select="normalize-space($node)"/>
    <xsl:choose>
      <xsl:when test="$node/@refkind = 'capability' or contains($body, '`')">
        <xsl:value-of select="$body"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>`</xsl:text>
        <xsl:value-of select="$body"/>
        <xsl:text>`</xsl:text>
        <xsl:if test="normalize-space($node/@when) != ''">
          <xsl:text> when </xsl:text>
          <xsl:value-of select="normalize-space($node/@when)"/>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="tool-selection">
    <xsl:text>## </xsl:text>
    <xsl:choose>
      <xsl:when test="normalize-space(@heading) != ''">
        <xsl:value-of select="normalize-space(@heading)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Tool selection</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>

</xsl:text>
    <xsl:call-template name="trim">
      <xsl:with-param name="text" select="."/>
    </xsl:call-template>
    <xsl:text>

</xsl:text>
  </xsl:template>

  <xsl:template match="workflow-position">
    <xsl:text>## Workflow position

</xsl:text>
    <xsl:call-template name="trim">
      <xsl:with-param name="text" select="."/>
    </xsl:call-template>
    <xsl:text>

</xsl:text>
  </xsl:template>

  <xsl:template match="execution-contract">
    <xsl:text>## </xsl:text>
    <xsl:choose>
      <xsl:when test="normalize-space(@heading) != ''">
        <xsl:value-of select="normalize-space(@heading)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Mandatory execution contract</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>

</xsl:text>
    <xsl:for-each select="item">
      <xsl:text>- </xsl:text>
      <xsl:call-template name="trim">
        <xsl:with-param name="text" select="."/>
      </xsl:call-template>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="branch-worktree-gate">
    <xsl:text>## </xsl:text>
    <xsl:choose>
      <xsl:when test="normalize-space(@heading) != ''">
        <xsl:value-of select="normalize-space(@heading)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Branch/worktree gate</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>

</xsl:text>
    <xsl:for-each select="item">
      <xsl:text>- </xsl:text>
      <xsl:call-template name="trim">
        <xsl:with-param name="text" select="."/>
      </xsl:call-template>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="workflow">
    <xsl:text>## </xsl:text>
    <xsl:choose>
      <xsl:when test="normalize-space(@heading) != ''">
        <xsl:value-of select="normalize-space(@heading)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Workflow</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>

</xsl:text>
    <xsl:for-each select="step">
      <xsl:choose>
        <xsl:when test="@number and normalize-space(@number) != ''">
          <xsl:value-of select="normalize-space(@number)"/>
          <xsl:text>. </xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="position()"/>
          <xsl:text>. </xsl:text>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:call-template name="trim">
        <xsl:with-param name="text" select="."/>
      </xsl:call-template>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="output">
    <xsl:text>## </xsl:text>
    <xsl:choose>
      <xsl:when test="normalize-space(@heading) != ''">
        <xsl:value-of select="normalize-space(@heading)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Output</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>

</xsl:text>
    <xsl:for-each select="item">
      <xsl:text>- </xsl:text>
      <xsl:call-template name="trim">
        <xsl:with-param name="text" select="."/>
      </xsl:call-template>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="safeguards">
    <xsl:text>## </xsl:text>
    <xsl:choose>
      <xsl:when test="normalize-space(@heading) != ''">
        <xsl:value-of select="normalize-space(@heading)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>Safeguards</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>

</xsl:text>
    <xsl:for-each select="item">
      <xsl:text>- </xsl:text>
      <xsl:call-template name="trim">
        <xsl:with-param name="text" select="."/>
      </xsl:call-template>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="agent-mention">
    <xsl:param name="id"/>
    <xsl:choose>
      <xsl:when test="starts-with($id, '`')">
        <xsl:value-of select="$id"/>
      </xsl:when>
      <xsl:when test="starts-with($id, '@')">
        <xsl:text>`</xsl:text>
        <xsl:value-of select="$id"/>
        <xsl:text>`</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>`@</xsl:text>
        <xsl:value-of select="$id"/>
        <xsl:text>`</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

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
