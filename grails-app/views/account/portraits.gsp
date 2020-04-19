<table>
	<tr>
		<td colspan="3"><g:if test="${!isFirst}"><span class="cursorPointer" onclick="showPortraits('${params.gender}', ${params.offset - params.perPage});">prev</span></g:if></td>
		<td colspan="3" style="text-align:right"><g:if test="${!isLast}"><span class="cursorPointer" onclick="showPortraits('${params.gender}', ${params.offset + params.perPage});">next</span></g:if></td>
	</tr>

	<g:each in="${portraits}" var="portrait" status="iteration">
		<g:if test="${iteration%3 == 0}">
			<tr>
		</g:if>
		<td class="portraitRadio"><g:radio name="portrait" value="${portrait}" id="${portrait}" onclick="updateHiddenRadio(this.value);"/></td>
		<td><label for="${portrait}"><img src="<g:resource dir="/images/portraits/${params.gender}" file="${portrait}"/>"/></label></td>
		<g:if test="${iteration%3 == 2}">
			</tr>
		</g:if>
		<g:elseif test="${iteration == portraits.size() - 1}">
			</tr>
		</g:elseif>
	</g:each>
</table>