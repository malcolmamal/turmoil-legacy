<!DOCTYPE html>
<html>
	<head>
		<title><g:message code="turmoil.page.name.main"/></title>
		<meta name="layout" content="turmoil">
		<r:require modules="turmoil-general, jquery-ui, custom-scrollbar"/>

		<style>
			.starterContentWrapper {
				margin: auto;
				width: 100%;
			}

			.starterContentContainer {
				padding-top: 15px;
				margin: auto;
				width: 900px;
				overflow: auto;
				height: 700px;
			}
		</style>

		<r:script>
			$(document).ready(function()
			{
				$("#datepicker").datepicker({dateFormat: 'yy/mm/dd'});
			})

			$(function() {

				$(document).tooltip({
					content: function () {
						return $(this).prop('title');
					},
				});

			});

		</r:script>
	</head>
	<body>
		<div class="starterContentWrapper">
			<div class="starterContentContainer tallContentContainer scrollableContainer">
				<div style="margin-top: 10px; width: 400px;">
					<pre>${log}</pre>
				</div>


				<p>Between <input type="text" id="datepicker"> :)</p>

				<p><label for="age">Your age:</label><input id="age" title="We ask for your age<br/> only for statistical purposes."></p>
				<p>Hover the field to see the tooltip.</p>
			</div>
		</div>
	</body>
</html>