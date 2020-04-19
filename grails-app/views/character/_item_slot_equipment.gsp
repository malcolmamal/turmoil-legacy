<%@ page import="turmoil.enums.ItemType" %>

<g:if test="${!iconItemSize}">
	<g:set var="iconItemSize" value="big"/>
</g:if>

<g:if test="${item}">
	<g:set var="opacity" value="1.0"/>
</g:if>
<g:else>
	<g:set var="opacity" value="0.8"/>
</g:else>

<span class="d3-icon d3-icon-item d3-icon-item-large d3-icon-item-<g:if test="${item}">${item.getRarityClass()}</g:if><g:else>gray</g:else>"
	style="opacity: ${opacity};"
	oncontextmenu="<g:if test="${item}">actionRightClickOnEquipment('${item.id}'); </g:if>return false;"
>
	<span class="icon-item-gradient">
		<span <g:if test="${item}">id="tooltip_${item.getFileCode()}_${item.id}"</g:if> class="icon-item-inner icon-item-${iconItemSize} <g:if test="${item}">tooltip itemTooltip</g:if>"
			<g:if test="${item}">
				item="${item.id}"
				style="background-image: url('${resource(dir: item.getImagePath(), file: item.getImageFile())}');"
			</g:if>
		>
		</span>
	</span>
</span>