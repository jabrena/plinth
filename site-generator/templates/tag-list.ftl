<#assign include_type = "page">
<#assign include_title = config.tags_title!>
<#if !include_title?has_content>
  <#assign include_title = "Tags">
</#if>
<#assign include_subtitle = config.tags_subtitle!>
<#assign include_bigimg = config.tags_bigimg!>
<!DOCTYPE html>
<html lang="${config.site_lang}">

<#include "head.ftl">

<body>
<#include "nav.ftl">

<#include "header.ftl">

<div class="container" role="main">
<article class="post-preview">
<div class="list-group col-lg-4 col-lg-offset-4 col-md-6 col-md-offset-3">
<#list alltags as tag>
<#if tag != "">
<a href="${content.rootpath!}tags/${tag}${config.output_extension}" class="list-group-item">
  ${tag}
  <span class="badge">${count_tag(published_content, tag)}</span>
</a>
</#if>
</#list>
</ul>

</div>
</article>
</div>

<#include "footer.ftl">

<#include "footer-scripts.ftl">

</body>
</html>

<#function count_tag pages tag>
  <#local result = 0>
  <#list pages as page>
    <#if page.tags?has_content>
    <#list page.tags as t>
      <#if t == tag>
        <#local result = result + 1>
        <#break>
      </#if>
    </#list>
    </#if>
  </#list>
  <#return result>
</#function>