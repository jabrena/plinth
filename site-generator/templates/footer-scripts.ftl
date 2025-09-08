<script src="${content.rootpath!}js/jquery-1.11.2.min.js"></script>
<script src="${content.rootpath!}js/bootstrap.min.js"></script>
<script src="${content.rootpath!}js/prettify.js"></script>
<script src="${content.rootpath!}js/run_prettify.js"></script>
<script src="${content.rootpath!}js/main.js"></script>
<#if (config.author_google_analytics)?has_content>
<!-- Google Analytics -->
<script>
	(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
	ga('create', '${config.author_google_analytics}', 'auto');
	ga('send', 'pageview');
</script>
<!-- End Google Analytics -->
</#if>