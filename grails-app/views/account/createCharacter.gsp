<%@ page import="turmoil.enums.Gender" %>
<!DOCTYPE html>
<html>
	<head>
		<title><g:message code="turmoil.page.name.account"/> - Character creation</title>
		<meta name="layout" content="turmoil">
		<r:require modules="turmoil-general"/>

		<r:script>
			var selectedPortrait = '${cmd.portrait}';

			function showPortraits(gender, offset)
			{
				window.turmoil.ajax.exec({
					url: 'account/listPortraits?gender=' + gender + '&offset=' + offset,
					onSuccess: loadPortraits
				});
			}

			function resetHiddenRadio()
			{
				jQuery('#hiddenRadio').val('');
				jQuery('#hiddenRadio').prop('checked', false);
			}

			function updateHiddenRadio(value)
			{
				selectedPortrait = value;
				jQuery('#hiddenRadio').val(value);
			}

			function loadPortraits(content)
			{
				jQuery('#portraitSection').show();
				jQuery('#portraitsPlaceholder').html(content);
				jQuery("input[id='" + selectedPortrait + "']").prop('checked', true);

				if (typeof(jQuery('input[name=portrait]:checked', '#createCharacterForm').val()) === 'undefined')
				{
					jQuery("input[id='hiddenRadio']").prop('checked', true);
				}
			}

			$(function () {
				if (!jQuery('#radioMale').is(':checked') && !jQuery('#radioFemale').is(':checked'))
				{
					jQuery('#portraitSection').hide();
				}
				else
				{
					showPortraits(jQuery('#radioFemale').is(':checked') ? 'female' : 'male', 0);
					jQuery('#portraitSection').show();
				}
			});
		</r:script>

		<style>
			table.characterCreation {
				width: 700px;
			}
			td.characterCreationLabel {
				width: 150px;
			}

			.portraitRadio {
				vertical-align: middle;
			}
		</style>

	</head>
	<body>
		<g:hasErrors bean="${cmd}">
			<g:if test="${cmd.wasSent}">
				<div class="errors">
					<g:renderErrors bean="${cmd}" as="list" />
				</div>
			</g:if>
		</g:hasErrors>
		<g:form name="createCharacterForm" url="[controller:'account',action:'createCharacter']">
			<input type="radio" name="portrait" value="${cmd.portrait}" id="hiddenRadio" style="display: none;"/>
			<table class="characterCreation">
				<tr>
					<td class="characterCreationLabel">
						Character name ${request.get} ${request.post}
					</td>
					<td>
						<g:textField name="name" value="${cmd.name}" />
					</td>
				</tr>
				<tr>
					<td>
						Gender
					</td>
					<td>
						<g:radio name="gender" value="${Gender.MALE}" checked="${cmd.gender == Gender.MALE}" id="radioMale" onclick="showPortraits(this.value, 0); resetHiddenRadio();"/>
						<label for="radioMale">Male</label>

						<g:radio name="gender" value="${Gender.FEMALE}" checked="${cmd.gender == Gender.FEMALE}" id="radioFemale" onclick="showPortraits(this.value, 0); resetHiddenRadio();"/>
						<label for="radioFemale">Female</label>
					</td>
				</tr>
				<tr id="portraitSection">
					<td>Portrait</td>
					<td><div id="portraitsPlaceholder"></div></td>
				</tr>
				<tr>
					<td colspan="2">
						<g:actionSubmit value="Create" action="createCharacterForm" onclick="showSpinner()"/>
					</td>
				</tr>
			</table>
		</g:form>
	</body>
</html>