<!DOCTYPE html>
<html>
	<head>
		<title><g:message code="turmoil.page.name.main"/></title>
		<meta name="layout" content="turmoil">
		<r:require modules="turmoil-general, turmoil-tooltip, jquery-ui"/>

		<r:script>
			function printWeapon(data)
			{
				jQuery('#weapon').html('ads:' + data.weaponType.name);
			}

			$(document).ready(function()
			{
				$("#datepicker").datepicker({dateFormat: 'yy/mm/dd'});
			})

			$(function() {

				console.log('page initialized...');

			});

		</r:script>
	</head>
	<body>
		<asset:audio src="argate_001.wav" id="a1"/>
		<asset:audio src="argate_002.wav" id="a2"/>
		<asset:audio src="argate_003.wav" id="a3"/>
		<asset:audio src="change_weapon_004.wav" id="a4"/>

		<div style="margin: 10px;">
			<pre>${log}</pre>
		</div>
		<div>
			<div id="weapon"><g:if test="${weapon != null}">${weapon.getWeaponType()}</g:if></div>
			<div id="error" style="color: red"></div>
			<g:remoteLink action="showWeapon" id="3" update="[success: 'weapon', failure: 'error']" onSuccess="printWeapon(data)">Show item</g:remoteLink>
		</div>
		<div title="my title is fun">trollo</div>
		<div style="margin: 10px;">
			<g:link controller="starter" action="cleanLog">Clean Log</g:link>

			<g:if test="${application.loggedAccount == null}">
				<g:link controller="login" action="index">Login</g:link>
			</g:if>
			<g:else>
				<g:link controller="account" action="index">Manage account</g:link>
				<g:link controller="login" action="logout">Logout</g:link> (logged as ${application.loggedAccount.username})
			</g:else>
		</div>

		<p>Between <input type="text" id="datepicker"></p>

		<p><label for="age">Your age:</label><input id="age" class="tooltip" title="We ask for your age only for statistical purposes. Ala ma kota :)<br/>newline"></p>
		<p>Hover the field to see the tooltip.</p>
		<span item="2" class="tooltip itemTooltip somebody">first</span>
		<span item="4" class="tooltip itemTooltip">second</span>
		<span item="19" class="tooltip itemTooltip">third</span>
		<span item="1" class="tooltip itemTooltip">fourth</span>
		<span item="9" class="tooltip itemTooltip">fifth</span>

		<span onClick="playAudio('a1')">A1&nbsp;</span>
		<span onClick="playAudio('a2')">A2&nbsp;</span>
		<span onClick="playAudio('a3')">A3&nbsp;</span>
		<span onClick="playAudio('a4')">A4&nbsp;</span>

		<g:message code="turmoil.item.weapon.${messageCode}" />

		<g:message code="default.paginate.prev" />

		a[${messageCode}]b

		ajax link:

		<g:remoteLink action="show" id="1" update="successIsGreat" onLoading="showSpinner();" onLoaded="wasLoaded();">Test 3</g:remoteLink>

	</body>
</html>