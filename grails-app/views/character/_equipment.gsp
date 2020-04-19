<g:set var="windowWidth" value="800"/>
<g:set var="windowHeight" value="830"/>

<r:script>
	windowSizes.${windowCode}Width = ${windowWidth};
	windowSizes.${windowCode}Height = ${windowHeight};
</r:script>

<div id="window_${windowCode}" class="windowContent ${windowCode}WindowContent" style="transform:scale(${panelScale}); -webkit-transform:scale(${panelScale}); -moz-transform:scale(${panelScale}); -o-transform:scale(${panelScale});">
	<div class="windowContentInner" style="background-image: url(${assetPath(src: 'character_' + character.gender.toString().toLowerCase() + '.png')}); width: ${windowWidth}px; height: ${windowHeight}px;">

		<span id="slot_right_hand_effect" <g:if test="${character.slotRightHand != null}">class="item-weapon-bg-${character.slotRightHand.damageType.toString().toLowerCase()}"</g:if> style="position: absolute; top: 143px; left: 61px; width: 150px; height: 210px;"></span>
		<span id="slot_left_hand_effect"  <g:if test="${character.slotLeftHand != null}">class="item-weapon-bg-${character.slotLeftHand.damageType.toString().toLowerCase()}"</g:if> style="position: absolute; top: 143px; left: 632px; width: 150px; height: 210px;"></span>

		<a class="slot-link" id="slot_right_hand" style="position: absolute; top: 175px; left: 90px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotRightHand]"/>
		</a>

		<a class="slot-link" id="slot_left_hand" style="position: absolute; top: 175px; left: 660px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotLeftHand]"/>
		</a>

		<a class="slot-link" id="slot_amulet" style="position: absolute; top: 170px; left: 450px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotAmulet, 'iconItemSize': 'square']"/>
		</a>

		<a class="slot-link" id="slot_ring_one" style="position: absolute; top: 100px; left: 10px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotRingOne, 'iconItemSize': 'square']"/>
		</a>

		<a class="slot-link" id="slot_ring_two" style="position: absolute; top: 90px; left: 705px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotRingTwo, 'iconItemSize': 'square']"/>
		</a>

		<a class="slot-link" id="slot_ring_three" style="position: absolute; top: 175px; left: 10px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotRingThree, 'iconItemSize': 'square']"/>
		</a>

		<a class="slot-link" id="slot_ring_four" style="position: absolute; top: 90px; left: 630px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotRingFour, 'iconItemSize': 'square']"/>
		</a>

		<a class="slot-link" id="slot_helm" style="position: absolute; top: 35px; left: 355px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotHelm]"/>
		</a>

		<a class="slot-link" id="slot_chest" style="position: absolute; top: 210px; left: 355px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotChest]"/>
		</a>

		<a class="slot-link" id="slot_belt" style="position: absolute; top: 385px; left: 355px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotBelt, 'iconItemSize': 'long']"/>
		</a>

		<a class="slot-link" id="slot_pants" style="position: absolute; top: 460px; left: 355px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotPants]"/>
		</a>

		<a class="slot-link" id="slot_boots" style="position: absolute; top: 635px; left: 355px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotBoots]"/>
		</a>

		<a class="slot-link" id="slot_pauldrons" style="position: absolute; top: 90px; left: 220px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotPauldrons]"/>
		</a>

		<a class="slot-link" id="slot_gloves" style="position: absolute; top: 0px; left: 90px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotGloves]"/>
		</a>

		<a class="slot-link" id="slot_bracers" style="position: absolute; top: 90px; left: 530px;">
			<g:render template="/character/item_slot_equipment" model="['item': character.slotBracers]"/>
		</a>

	</div>
</div>