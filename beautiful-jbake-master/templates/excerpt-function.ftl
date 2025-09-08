<#function excerpt body>
  <#local size = config.index_excerpt_size?number>
  <#local target = body?replace(config.remove_tag_regex, "", "r")>
  <#if target?length gt size>
    <#local target = target[0..size] + "...">
  </#if>
  <#return target>
</#function>