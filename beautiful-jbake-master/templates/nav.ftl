<#if (config.author_gtm)?has_content>
<!-- Google Tag Manager (noscript) -->
<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=${config.author_gtm}}"
height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
<!-- End Google Tag Manager (noscript) -->
</#if>
<nav class="navbar navbar-default navbar-fixed-top navbar-custom">
<div class="container-fluid">

<div class="navbar-header">
<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#main-navbar">
  <span class="sr-only">Toggle navigation</span>
  <span class="icon-bar"></span>
  <span class="icon-bar"></span>
  <span class="icon-bar"></span>
</button>
<a class="navbar-brand" href="${content.rootpath!}index.html">${config.site_title}</a>
</div>

<div class="collapse navbar-collapse" id="main-navbar">
<ul class="nav navbar-nav navbar-right">
  <li><a href="${content.rootpath!}archive.html">Archive</a></li>
  <li><a href="${content.rootpath!}tags.html">Tags</a></li>
  <li><a href="${content.rootpath!}about.html">About</a></li>
  <li class="navlinks-container">
    <a class="navlinks-parent" href="javascript:void(0)">Others</a>
    <div class="navlinks-children">
      <a href="${content.rootpath!}others/readme.html">README</a>
      <a href="${content.rootpath!}jbake2.5.1doc">JBake<br/>日本語訳</a>
    </div>
  </li>
</ul>
</div>

<#if (content.image)??>
<div class="avatar-container">
  <div class="avatar-img-border">
    <a href="${content.rootpath!}">
      <img class="avatar-img" src="$!{content.rootpath}${content.image}"/>
  </a>
  </div>
</div>
<#elseif (config.site_avatar)??>
<div class="avatar-container">
  <div class="avatar-img-border">
    <a href="${content.rootpath!}index.html">
      <img class="avatar-img" src="${content.rootpath!}${config.site_avatar}"/>
    </a>
  </div>
</div>
</#if>

</div>
</nav>
