<#-- JBake splits comma-containing jbake.properties values into sequences -->
<#assign site_title_string = config.site_title!>
<#if site_title_string?is_sequence>
  <#assign site_title_string = site_title_string?join(", ")>
</#if>
