<#assign include_type = "course">
<#assign include_title = content.title!>
<#assign include_subtitle = content.subtitle!>
<#assign include_bigimg = content.bigimg!>
<!DOCTYPE html>
<html lang="${config.site_lang}">

<#include "head.ftl">

<body>
<#include "nav.ftl">

<#include "header.ftl">

<div class="container">
<div class="row">
<div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
<article role="main" class="course-content">

<#if (content.author)?has_content || (content.date)?has_content || (content.version)?has_content>
<div class="course-meta">
  <#if (content.date)?has_content>
    <i class="fa fa-calendar-o"></i>
    ${content.date?string(config.date_format)}
  </#if>
  <#if (content.author)?has_content>
    &nbsp;
    <i class="fa fa-user"></i>
    ${content.author}
  </#if>
  <#if (content.version)?has_content>
    &nbsp;
    <i class="fa fa-tag"></i>
    Version ${content.version}
  </#if>
  <#if (content.tags)?has_content>
  <div class="course-tags">
    &nbsp;
    <i class="fa fa-tags"></i>
    <#list content.tags as tag>
      <a href="${content.rootpath!}tags/${tag}${config.output_extension}" class="course-tag">${tag}</a>
    </#list>
  </div>
  </#if>
</div>
<hr>
</#if>

${content.body}

</article>
</div>
</div>
</div>

<#include "footer.ftl">

<#include "footer-scripts.ftl">

</body>
</html>
