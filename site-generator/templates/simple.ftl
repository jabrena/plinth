<#assign include_type = "page">
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
<div class="page-heading">
<h2><#if (content.title)?has_content>${content.title}<#else><br/></#if></h2>
</div>
</div>
</div>
</div>
</div>
</header>

<div class="container">
<div class="row">
<div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
<article role="main" class="blog-page">
${content.body}
</article>
</div>
</div>
</div>

<#include "footer.ftl">

<#include "footer-scripts.ftl">

</body>
</html>