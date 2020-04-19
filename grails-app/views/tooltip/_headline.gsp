<div class="tooltip-head tooltip-head-${item.getRarityClass()}">
	<h3 class="d3-color-${item.getRarityClass()}">
		<g:if test="${item.rarity.isPlain()}">
			${item.itemName}
		</g:if>
		<g:else>
			<g:message code="turmoil.item.${item.itemType.toString().toLowerCase()}.${item.itemCode}"/>
		</g:else>
	</h3>
</div>