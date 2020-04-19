<!DOCTYPE html>
<html>
	<head>
		<title><g:message code="turmoil.page.name.login"/></title>
		<meta name="layout" content="turmoil">
		<r:require modules="turmoil-general"/>

		<style>
			.loginContentWrapper {
				position:absolute;
				top: 0;
				width: 100%;
				text-align: center;
			}

			.loginContentContainer {
				width: 380px;
				margin: auto;
			}
		</style>

	</head>
	<body>
		<div id="centeredContentWrapper" class="loginContentWrapper">
			<div class="loginContentContainer">
				<g:form name="loginForm" url="[controller:'login',action:'authenticate']">
					<table>
						<tr>
							<td style="width:150px;">
								<g:message code="turmoil.page.login.username"/>
							</td>
							<td>
								<g:textField name="username" value="${cmd.username}" />
							</td>
						</tr>
						<tr>
							<td>
								<g:message code="turmoil.page.login.password"/>
							</td>
							<td>
								<g:passwordField name="password" value="${cmd.password}" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<g:actionSubmit value="Login" action="authenticate" onclick="showSpinner()"/>
							</td>
						</tr>
					</table>
				</g:form>
			</div>
		</div>
	</body>
</html>