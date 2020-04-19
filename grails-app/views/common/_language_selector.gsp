<div class="flagsArea" style="position: absolute; width: ${flags.size() * 20}px;">
	<g:each in="${flags}" var="lang">
		<a href="${uri + lang}">
			<div class="flag ${lang} ${(lang.equals(selected)) ? 'selected' : 'notSelected'}"></div>
		</a>
	</g:each>
</div>