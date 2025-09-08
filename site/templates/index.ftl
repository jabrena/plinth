<#include "excerpt-function.ftl">
<#assign include_type = "page">
<#assign include_title = config.site_title>
<#assign include_subtitle = config.site_subtitle!>
<#assign include_bigimg = config.index_bigimg!>
<!DOCTYPE html>
<html lang="${config.site_lang}">

<#include "head.ftl">

<body>

<#include "nav.ftl">

<#include "header.ftl">

<div class="container" role="main">
<div class="row">
<div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">

<div class="posts-list">
<#list published_posts as post>
<article class="post-preview">
<h2 class="post-title"><a href="${content.rootpath!}${post.uri}"><#escape x as x?xml>${post.title}</#escape></a></h2>
<#if (post.subtitle)?has_content>
  <h3 class="post-subtitle"><a href="${content.rootpath!}${post.uri}"><#escape x as x?xml>${post.subtitle}</#escape></a></h3>
</#if>

<p class="post-meta">
  <i class="fa fa-calendar-o"></i>
  ${post.date?string(config.date_format)}
  <#if (post.author)?has_content>
    &nbsp;
    <i class="fa fa-pencil"></i>
    ${post.author}
  </#if>
  <#if (post.tags)?has_content>
  <span class="blog-tags">
    &nbsp;
    <i class="fa fa-tags"></i>
    <#list post.tags as tag>
      <a href="${content.rootpath!}tags/${tag}${config.output_extension}">${tag}</a>
    </#list>
  </span>
  </#if>
</p>

<div class="post-entry-container">
  <#if (post.image)?has_content>
    <div class="post-image">
      <a href="${content.rootpath!}${post.uri}"><img src="${content.rootpath!}${post.image}"/></a>
    </div>
  </#if>
  <div class="post-entry">
    ${excerpt(post.body)}
    <p></p>
    <a href="${content.rootpath!}${post.uri}" class="post-read-more">[Read More]</a>
  </div>
</div>
</article>
</#list>
</div>

<#if (nextFileName )?? || (previousFileName)??>
<ul class="pager main-pager">
<#if (previousFileName)??>
<li class="previous">
  <a href="${content.rootpath!}${previousFileName}">&larr; Newer Posts</a>
</li>
</#if>
<#if (nextFileName)??>
<li class="next">
  <a href="${content.rootpath!}${nextFileName}">Older Posts &rarr;</a>
</li>
</#if>
</ul>
</#if>

</div>
</div>
</div>

<#include "footer.ftl">

<#include "footer-scripts.ftl">

</body>
</html>
