<#assign include_type = "page">
<#assign include_title = config.archive_title!>
<#if !include_title?has_content>
  <#assign include_title = "Archive">
</#if>
<#assign include_subtitle = config.archive_subtitle!>
<#assign include_bigimg = config.archive_bigimg!>
<!DOCTYPE html>
<html lang="${config.site_lang}">

<#include "head.ftl">

<body>
<#include "nav.ftl">

<#include "header.ftl">

<div class="container" role="main">
<article class="post-preview">
<div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
<#list published_posts as post>
<#if (last_month)??>
	<#if post.date?string(config.month_format) != last_month>
  <span class="list-group-item"><h4>${post.date?string(config.month_format)}</h4></span>
	</#if>
<#else>
  <span class="list-group-item"><h4>${post.date?string(config.month_format)}</h4></span>
</#if>
<a href="${content.rootpath!}${post.uri}" class="list-group-item">
  ${post.date?string(config.day_format)} - <#escape x as x?xml>${post.title}</#escape>
</a>
<#assign last_month = post.date?string(config.month_format)>
</#list>

</div>
</article>
</div>

<#include "footer.ftl">

<#include "footer-scripts.ftl">

</body>
</html>