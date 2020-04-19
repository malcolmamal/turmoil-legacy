<div class="d3-tooltip d3-tooltip-item" item="${weapon.getFileCode()}_${weapon.id}">

	<g:render template="/tooltip/headline" model="['item': weapon]"/>

	<div class="tooltip-body effect-bg effect-bg-${weapon.damageType.toString().toLowerCase()}">

		<g:render template="/tooltip/item_slot_tooltip" model="['item': weapon]"/>

		<div class="d3-item-properties">

			<ul class="item-type-right">
				<li class="item-slot"><g:message code="turmoil.item.type.weapon.${weapon.weaponType}"/></li>
			</ul>

			<g:render template="rarity" model="['item': weapon]"/>

			<ul class="item-armor-weapon item-weapon-dps">
				<li class="big"><span class="value">${weapon.getAverageDamage()}</span></li>
				<li><g:message code="turmoil.item.info.average.damage"/></li>
			</ul>

			<ul class="item-armor-weapon item-weapon-damage">
				<li><p>
						<span class="value">${weapon.minDamage}</span>–<span class="value">${weapon.maxDamage}</span>
						<span class="d3-color-FF888888"> <g:message code="turmoil.item.info.damage.${weapon.damageType}"/></span>
					</p>
				</li>
			</ul>

			<div class="item-before-effects"></div>

			<g:render template="attributes" model="['item':weapon]"/>

			<g:render template="requirements" model="['item':weapon]"/>

			<span class="clear"><!-- --></span>
		</div>
	</div>

	<g:render template="flavor" model="['item':weapon]"/>

</div>