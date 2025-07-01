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

## </xsl:text><xsl:value-of select="normalize-space(content-sections/template-section/template-header/template-title)"/>
        <xsl:text>:

</xsl:text><xsl:value-of select="normalize-space(content-sections/template-section/template-description)"/>
        <xsl:text>

---
</xsl:text><xsl:value-of select="content-sections/template-section/template-content/code-block"/>
    </xsl:template>
</xsl:stylesheet> 