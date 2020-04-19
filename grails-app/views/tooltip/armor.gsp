<div class="d3-tooltip d3-tooltip-item" item="${armor.getFileCode()}_${armor.id}">

	<g:render template="/tooltip/headline" model="['item': armor]"/>

	<div class="tooltip-body effect-bg effect-bg-armor">

		<g:render template="/tooltip/item_slot_tooltip" model="['item': armor]"/>

		<div class="d3-item-properties">

			<ul class="item-type-right">
				<li class="item-slot"><g:message code="turmoil.item.type.armor.${armor.armorType}"/></li>
			</ul>

			<g:render template="rarity" model="['item': armor]"/>

			<ul class="item-armor-weapon item-armor-armor">
				<li class="big"><span class="value">${armor.armorValue}</span></li>
				<li><g:message code="turmoil.item.info.armor"/></li>
			</ul>

			<div class="item-before-effects"></div>

			<g:render template="attributes" model="['item': armor]"/>

			<g:render template="requirements" model="['item': armor]"/>

			<span class="clear"><!-- --></span>
		</div>
	</div>

	<g:render template="flavor" model="['item': armor]"/>

</div>