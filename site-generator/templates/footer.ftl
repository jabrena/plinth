<footer>
<div class="container beautiful-jekyll-footer">
<div class="row">
<div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
<ul class="list-inline text-center footer-links">
  <#if (config.author_twitter)?has_content>
  <li>
    <a href="https://twitter.com/${config.author_twitter}" title="Twitter">
      <span class="fa-stack fa-lg">
        <i class="fa fa-circle fa-stack-2x"></i>
        <i class="fa fa-twitter fa-stack-1x fa-inverse"></i>
      </span>
    </a>
  </li>
  </#if>
  <#if (config.author_github)?has_content>
  <li>
    <a href="https://github.com/${config.author_github}/cursor-rules-java" title="GitHub">
      <span class="fa-stack fa-lg">
        <i class="fa fa-circle fa-stack-2x"></i>
        <i class="fa fa-github fa-stack-1x fa-inverse"></i>
      </span>
    </a>
  </li>
  </#if>
  <#if (config.render_feed)?has_content>
  <li>
    <a href="${content.rootpath!}feed.xml" title="RSS">
      <span class="fa-stack fa-lg">
        <i class="fa fa-circle fa-stack-2x"></i>
        <i class="fa fa-rss fa-stack-1x fa-inverse"></i>
      </span>
    </a>
  </li>
  </#if>
</ul>
<p class="copyright text-muted">
<#if (config.author_name)?has_content>
&copy; ${config.author_name}, ${published_date?string('yyyy')} |
</#if>
Baked with <a href="http://jbake.org">JBake v2.7.0</a>
</p>
<p class="theme-by text-muted">
  Theme by <a href="https://github.com/Yamane/beautiful-jbake/" target="_blank">beautiful-jbake</a>
  adapted from <a href="http://deanattali.com/beautiful-jekyll/" target="_blank">beautiful-jekyll</a>
</p>
</div>
</div>
</div>
</footer>
