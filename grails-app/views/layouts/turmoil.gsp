<g:if test="${session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE' != null}">
	<g:set var="lang" value="${session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'}"/>
</g:if>
<g:else>
	<g:set var="lang" value="${Locale.getDefault().toString().substring(0, 2)}"/>
</g:else>
<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="${lang}" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="${lang}" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="${lang}" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="${lang}" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="${lang}" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Turmoil"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
		<asset:stylesheet src="turmoilApplication.css"/>
		<asset:javascript src="turmoil.js"/>

		<g:layoutHead/>
		<r:layoutResources/>
	</head>
	<body>
		<div class="turmoilContainer">
			<div role="banner" id="turmoilHeader" class="turmoilHeader" style="position: relative;">
				<div style="position: absolute; left: 0px; top: 0px;">
					<language:selector langs="en, pl"/>
				</div>
				<div style="position: absolute; right: 0; bottom: 0;">
					<g:render template="/common/menu"/>
				</div>
			</div>
			<div id="turmoilBody" class="turmoilBody">
				<div id="shadows">
					<div class="shadowTop"></div>
					<div class="shadowLeft"></div>
					<div class="shadowRight"></div>
					<div class="shadowBottom"></div>
				</div>
				<g:layoutBody/>
			</div>
			<div id="turmoilFooter" class="turmoilFooter" role="contentinfo">
				<div class="footerBlock">
					<g:if test="${application.loggedAccount != null}">
						<g:link controller="login" action="logout">Logout</g:link> (logged as ${application.loggedAccount.username})
					</g:if>
				</div>
				<div id="spinner" class="spinner" style="display: none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
			</div>
		</div>
		<r:layoutResources/>
	</body>
</html>
