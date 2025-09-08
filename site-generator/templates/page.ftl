<#assign include_type = "page">
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
<article role="main" class="blog-post">
${content.body}
</article>
</div>
</div>
</div>

<#include "footer.ftl">

<#include "footer-scripts.ftl">

</body>
</html>