<%@ page import="turmoil.enums.ItemType" %>

<g:if test="${item.itemType == ItemType.ACCESSORY}">
	<g:if test="${item.isSquareLayout()}">
		<g:set var="itemTypeClass" value="square"/>
	</g:if>
	<g:else>
		<g:set var="itemTypeClass" value="default"/>
	</g:else>
</g:if>
<g:elseif test="${item.itemType == ItemType.ARMOR}">
	<g:if test="${item.isLongLayout()}">
		<g:set var="itemTypeClass" value="long"/>
	</g:if>
	<g:else>
		<g:set var="itemTypeClass" value="default"/>
	</g:else>
</g:elseif>
<g:else>
	<g:set var="itemTypeClass" value="default"/>
</g:else>

<span class="d3-icon d3-icon-item d3-icon-item-large  d3-icon-item-${item.getRarityClass()}">
	<span class="icon-item-gradient">
		<span	class="icon-item-inner icon-item-${itemTypeClass}"
				style="background-image: url('${resource(dir: item.getImagePath(), file: item.getImageFile())}');">
		</span>
	</span>
</span>