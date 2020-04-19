<!DOCTYPE html>
<html>
	<head>
		<title><g:message code="turmoil.page.name.account"/> - Characters</title>
		<meta name="layout" content="turmoil">
		<r:require modules="turmoil-general, custom-scrollbar"/>

		<style>
			.characterListSelected {
				font-weight: bold;
				color: lime;
			}

			.portraitRadio {
				vertical-align: middle;
				width: 16px;
			}

			.portraitImage {
				width: 140px;
			}
		</style>

		<r:script>

			function setSelectedCharacter(data)
			{
				if (data.success && typeof(data.selectedCharacter) != 'undefined')
				{
					jQuery('.characterListRow').removeClass('characterListSelected');
					jQuery('#character_' + data.selectedCharacter).addClass('characterListSelected');
					jQuery('input[name="radioCharacters"][value="' + data.selectedCharacter + '"]').prop('checked', true);
				}
			}

			$(function () {

				jQuery('#charactersList').css('height', (jQuery('#turmoilBody').css('height')));

			});

		</r:script>

	</head>
	<body>
		<div class="scrollableContainer" id="charactersList" style="width: 100%;">
			<table>
			<g:each in="${characters}">
				<tr class="characterListRow <g:if test="${it.id == selectedCharacter}">characterListSelected</g:if>" id="character_${it.id}" onclick="<g:remoteFunction action="selectCharacter" id="${it.id}" before="showSpinner()" onSuccess="setSelectedCharacter(data); hideSpinner()" onFailure="handleAjaxError(XMLHttpRequest.responseText, errorThrown)" />">
					<td class="portraitRadio"><g:radio name="radioCharacters" value="${it.id}" checked="${it.id == selectedCharacter}"/></td>
					<td class="portraitImage">
						<img src="<g:resource dir="/images/portraits/${it.gender.toString().toLowerCase()}" file="${it.portrait}"/>"/>
					</td>
					<td>${it.name}</td>
					<td>${it.id}</td>
					<td>${it.gender}</td>
				</tr>
			</g:each>
			</table>
		</div>
	</body>
</html>