<ul class="item-effects">
	<g:if test="${item.attributes.size() > 0}">
		<p class="item-property-category"><g:message code="turmoil.item.info.effects"/></p>
	</g:if>

	<!-- default/utility/enchant -->
	<g:each in="${item.attributes}">
		<li class="d3-color-blue d3-item-property-default">
			<p class="attributeEntry">
				<g:message code="turmoil.item.attribute.${it.type}" args="${it.getValues()}"/>
			</p>
		</li>
	</g:each>

	<!--
	<li class="d3-color-white full-socket">
		<img class="gem" src="http://media.blizzard.com/d3/icons/items/small/emerald_18_demonhunter_male.png" />
		<span class="socket-effects">
			Lorem ipsum dolor sit amet 125,0%
		</span>
	</li>

	<li class="d3-color-orange d3-item-property-utility">
		<p>
			Lorem ipsum dolor sit amet, <span class="value">197</span>, consectetur adipisicing elit, sed do eiusmod tempor
		</p>
	</li>
	-->
</ul>