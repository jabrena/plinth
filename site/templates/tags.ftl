<#include "excerpt-function.ftl">
<!DOCTYPE html>
<html lang="${config.site_lang}">

<#include "head.ftl">

<body>
<#include "nav.ftl">

<header class="header-section">
<div class="intro-header no-img">
  <div class="container">
    <div class="row">
      <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
        <div class="page-heading"><h1>#${tag}</h1></div>
      </div>
    </div>
  </div>
</div>
</header>

<div class="container" role="main">
<div class="row">
<div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">

<div class="posts-list">
<#list tagged_documents as post>
<article class="post-preview">
<h2 class="post-title"><a href="${content.rootpath!}${post.uri}"><#escape x as x?xml>${post.title}</#escape></a></h2>
<#if (post.subtitle)?has_content>
  <h3 class="post-subtitle"><a href="${content.rootpath!}${post.uri}"><#escape x as x?xml>${post.subtitle}</#escape></a></h3>
</#if>

<p class="post-meta">
  <#if (post.type) == "post">
  <i class="fa fa-calendar-o"></i>
  ${post.date?string(config.date_format)}
  <#if (post.author)?has_content>
    &nbsp;
    <i class="fa fa-pencil"></i>
    ${post.author}
  </#if>
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

</div>
</div>
</div>

<#include "footer.ftl">

<#include "footer-scripts.ftl">

</body>
</html>
