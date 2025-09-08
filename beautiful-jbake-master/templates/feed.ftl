<#include "excerpt-function.ftl">
<?xml version="1.0"?>
<feed xmlns='http://www.w3.org/2005/Atom' xml:lang='${config.site_lang}'>
  <id>${config.site_host}</id>
  <title>${config.site_title}</title>
  <updated>${published_date?string("yyyy-MM-dd")}T${published_date?string("HH:mm:ssZ")}</updated>
  <link rel="alternate" type="text/html" href="${config.site_host}" />
  <link rel='self' type='application/atom+xml' href='${config.site_host}/${config.feed_file}' />
  <#list published_posts as post>
  <entry>
    <id>${config.site_host}/${post.uri}</id>
    <title><#escape x as x?xml>${post.title}</#escape></title>
    <link></link>
    <link rel="alternate" type="text/html" href="${config.site_host}/${post.uri}" />
    <updated>${post.date?string("yyyy-MM-dd")}T${post.date?string("HH:mm:ssZ")}</updated>
    <summary><#escape x as x?xml>${excerpt(post.body)}</#escape></summary>
  </entry>
  </#list>
</feed>
