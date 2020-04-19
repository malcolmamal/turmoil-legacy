<ul class="flatMenu">
	<g:if test="${application.loggedAccount == null}">
		<li><g:link controller="login" action="index">Login</g:link></li>
	</g:if>
	<g:else>
		<li><g:link controller="account" action="showEquipment">Turmoil</g:link></li>
		<li class="flatSubMenu"><g:link controller="account" action="index">Manage account</g:link>
			<ul>
				<li><g:link controller="account" action="createCharacter">Add character</g:link></li>
				<li><g:link controller="account" action="listCharacters">List characters</g:link></li>
				<li><g:remoteLink controller="account" action="rollItem" onSuccess="putItemToStash(data)">Roll item</g:remoteLink></li>
			</ul>
		</li>
		<li><a href="#" onclick="hideSpinnerWithDelay();">Options</a>
			<ul>
				<li><a href="#" onclick="saveWindowsPositions(true);">Save windows positions</a></li>
			</ul>
		</li>
		<li><a href="#" onclick="showAjaxError();">Ajax</a></li>
		<li class="flatSubMenu"><g:link controller="starter" action="index">Debug</g:link></a>
			<ul>
				<li><g:link controller="starter" action="test">Test Area</g:link></li>
				<li><g:link controller="starter" action="cleanLog">Clean Log</g:link></li>
				<li><g:link controller="starter" action="ajaxTest">Ajax Test</g:link></li>
			</ul>
		</li>
		<li><g:link controller="login" action="logout">Logout</g:link></li>
	</g:else>
</ul>