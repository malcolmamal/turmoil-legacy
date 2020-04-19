<!DOCTYPE html>
<html>
	<head>
		<title>Show</title>
		<meta name="layout" content="turmoil">
	</head>
	<body>
		<table>
		<g:each in="${peopleToSee}">
			<tr>
				<td>${it.id}</td>
				<td>${it.name}</td>
				<td>${it.level}</td>
				<td>${it.dateCreated}</td>
			</tr>
		</g:each>
		</table>
	</body>
</html>