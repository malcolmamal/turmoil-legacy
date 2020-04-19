<!DOCTYPE html>
<html>
<head>
	<title><g:message code="turmoil.page.name.main"/></title>
	<meta name="layout" content="turmoil">
	<r:require modules="turmoil-general, turmoil-tooltip, turmoil-windows, jquery-ui, custom-scrollbar, custom-hotkeys, custom-dates"/>

	<asset:audio src="change_medium_001.wav" id="soundMediumArmor"/>
	<asset:audio src="change_weapon_004.wav" id="soundWeapon"/>
	<asset:audio src="change_bling_004.wav" id="soundAccessoryJewellery"/>

	<asset:audio src="attack_melee_001.wav" id="soundAttackMelee1"/>
	<asset:audio src="attack_melee_002.wav" id="soundAttackMelee2"/>
	<asset:audio src="attack_melee_003.wav" id="soundAttackMelee3"/>

	<asset:audio src="move_leather.wav" id="soundMoveLeather"/>

	<r:script>
		function switchWindow(windowType)
		{
			hideAllTooltips();

			if (jQuery('#window_' + windowType + '_resizer').is(':hidden'))
			{
				jQuery('#window_' + windowType + '_resizer').show();
				bringToTheTop(windowType);
			}
			else
			{
				jQuery('#window_' + windowType + '_resizer').hide();
			}
		}

		function updateStatisticsWindow()
		{
			window.turmoil.ajax.exec({
				url: 'account/updateStatisticsWindow/',
				onSuccess: finalizeUpdateStatisticsWindow
			});
		}

		function finalizeUpdateStatisticsWindow(content)
		{
			jQuery('#statsContent').html(content);
		}

		function actionRightClickOnEquipment(itemId)
		{
			hideAllTooltips();

			if (itemId > 0)
			{
				window.turmoil.ajax.exec({
					url: 'account/unequip/' + itemId,
					onSuccess: finalizeRightClickOnEquipment
				});
			}
		}

		function finalizeRightClickOnEquipment(data)
		{
			if (data != null && data.success == true)
			{
				if (typeof(data.stashedItemId != 'undefined') && typeof(data.stashedItemContent) != 'undefined')
				{
					if (typeof(data.stashedItemType != 'undefined'))
					{
						switch (data.stashedItemType)
						{
							case 'ACCESSORY':
								playAudio('soundAccessoryJewellery');
								break;
							case 'ARMOR':
								playAudio('soundMediumArmor');
								break;
							case 'WEAPON':
								playAudio('soundWeapon');
								if (typeof(data.unequippedItemSlot != 'undefined'))
								{
									jQuery('#' + data.unequippedItemSlot + '_effect').removeClass();
								}
								break;
						}
					}

					jQuery('#stashItemListContainer').append(data.stashedItemContent);
					if (typeof(data.unequippedItemSlot != 'undefined') && typeof(data.unequippedItemContent) != 'undefined')
					{
						jQuery('#' + data.unequippedItemSlot).html(data.unequippedItemContent);
					}
					updateStatisticsWindow();
				}
			}
		}

		function actionRightClickOnStashedItem(itemId)
		{
			hideAllTooltips();

			window.turmoil.ajax.exec({
				url: 'account/equip/' + itemId,
				onSuccess: finalizeRightClickOnStashedItem
			});
		}

		function finalizeRightClickOnStashedItem(data)
		{
			if (data != null && data.success == true)
			{
				if (typeof(data.equippedItemId) != 'undefined' && typeof(data.equippedItemContent) != 'undefined' && typeof(data.equippedItemSlot) != 'undefined')
				{
					//TODO find proper position to put the item
					jQuery('#' + data.equippedItemSlot).html(data.equippedItemContent);

					if (data.equippedItemSlot == 'slot_right_hand' || data.equippedItemSlot == 'slot_left_hand')
					{
						jQuery('#' + data.equippedItemSlot + '_effect').removeClass();

						if (typeof(data.equippedWeaponDamageType) != 'undefined') {
							jQuery('#' + data.equippedItemSlot + '_effect').addClass('item-weapon-bg-' + data.equippedWeaponDamageType);
						}
					}

					if (typeof(data.equippedItemType) != 'undefined')
					{
						//TODO perhaps generate the proper resource name in groovy so enums could be used
						switch (data.equippedItemType)
						{
							case 'ACCESSORY':
								playAudio('soundAccessoryJewellery');
								break;
							case 'ARMOR':
								playAudio('soundMediumArmor');
								break;
							case 'WEAPON':
								playAudio('soundWeapon');
								break;
						}
					}

					updateStatisticsWindow();
				}

				if (typeof(data.equippedItemId) != 'undefined')
				{
					jQuery('#stash_item_' + data.equippedItemId).remove();
				}

				putItemToStash(data);
			}
		}

		function putItemToStash(data)
		{
			if (typeof(data.stashedItemId) != 'undefined' && typeof(data.stashedItemContent) != 'undefined')
			{
				jQuery('#stashItemListContainer').append(data.stashedItemContent);
				hideSpinnerWithDelay();
			}
		}

		<g:if test="${session.windowsSettings != null && session.windowsSettings != "{}"}">
			turmoil.windowSettings = JSON.parse('${raw(session.windowsSettings)}');
		</g:if>

		$(function () {

			var slots = [
				'slot_right_hand',
				'slot_left_hand',
				'slot_amulet',
				'slot_ring_one',
				'slot_ring_two',
				'slot_ring_three',
				'slot_ring_four',
				'slot_helm',
				'slot_chest',
				'slot_belt',
				'slot_pants',
				'slot_boots',
				'slot_pauldrons',
				'slot_gloves',
				'slot_bracers'
			];

			jQuery.each(slots, function (index, value) {
				jQuery('#' + value).draggable({
					revert: true,
					start: function (event, ui) {
						hideAllTooltips();
					},
					stop: function (event, ui) {
						hideAllTooltips();
					}
				});
			});

			jQuery.each(jQuery.find('.windowIcon'), function (index, value) {
				jQuery(value).draggable({
					revert: true
				});
			});

			initWindow('stats', true);
			initWindow('stash', true);
			initWindow('equipment',true);
			initWindow('console', true);
			initWindow('instance', true);

			jQuery(document).bind('keydown', 'i', function () {
				switchWindow('equipment')
			});
			jQuery(document).bind('keydown', 'c', function () {
				switchWindow('stats')
			});
			jQuery(document).bind('keydown', 's', function () {
				switchWindow('stash')
			});
			jQuery(document).bind('keydown', 'n', function () {
				switchWindow('instance')
			});
			jQuery(document).bind('keydown', 'o', function () {
				switchWindow('console')
			});

		});

	</r:script>
</head>
<body>

	<g:render template="/character/window" model="['character': character, 'windowCode': 'equipment', 'windowHandlePosition': -120]"/>
	<g:render template="/character/window" model="['character': character, 'windowCode': 'stash', 'windowHandlePosition': -120]"/>
	<g:render template="/character/window" model="['character': character, 'windowCode': 'instance', 'windowHandlePosition': -120, 'combatState': combatState]"/>
	<g:render template="/character/window" model="['character': character, 'windowCode': 'stats', 'windowHandlePosition': -120]"/>
	<g:render template="/character/window" model="['character': character, 'windowCode': 'console', 'windowHandlePosition': -120]"/>

</body>
</html>