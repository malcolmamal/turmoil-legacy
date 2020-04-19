<div class="d3-tooltip d3-tooltip-item" item="${accessory.getFileCode()}_${accessory.id}">

	<g:render template="/tooltip/headline" model="['item': accessory]"/>

	<div class="tooltip-body effect-bg">

		<g:render template="/tooltip/item_slot_tooltip" model="['item': accessory]"/>

		<div class="d3-item-properties">

			<ul class="item-type-right">
				<li class="item-slot"><g:message code="turmoil.item.type.accessory.${accessory.accessoryType}"/></li>
			</ul>

			<g:render template="rarity" model="['item': accessory]"/>

			<div class="item-before-effects"></div>

			<g:render template="attributes" model="['item': accessory]"/>

			<g:render template="requirements" model="['item': accessory]"/>

			<span class="clear"><!-- --></span>
		</div>
	</div>

	<g:render template="flavor" model="['item': accessory]"/>

</div>