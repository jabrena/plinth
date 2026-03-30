<#include "jbake-config-text.ftl">
<head>
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
  <title><#if (include_title)?has_content && (include_title)!=(site_title_string)><#escape x as x?xml>${include_title}</#escape> - </#if>${site_title_string}</title>
  <meta name="author" content="${config.atuhor_name!}" />
  <meta name="description" content="${content.subtitle!}">
  <link rel="alternate" type="application/atom+xml" href="${content.rootpath!}feed.xml" title="${site_title_string}"/>

  <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" />
  <link rel="stylesheet" href="${content.rootpath!}css/bootstrap.min.css">
  <link rel="stylesheet" href="${content.rootpath!}css/bootstrap-social.css" />
  <link rel="stylesheet" href="${content.rootpath!}css/main.css" />

  <link rel="stylesheet" href="//fonts.googleapis.com/css?family=Lora:400,700,400italic,700italic" />
  <link rel="stylesheet" href="//fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800" />

  <link rel="stylesheet" href="${content.rootpath!}css/asciidoctor.css">
  <link rel="stylesheet" href="${content.rootpath!}css/prettify.css">
  <link rel="icon" type="image/png" href="${content.rootpath!}images/java-icon.png">
</head>
