package turmoil

import groovy.util.logging.Log4j
import turmoil.enums.AccessoryType
import turmoil.enums.ArmorType
import turmoil.enums.Gender
import turmoil.enums.ItemRarity
import turmoil.enums.ItemSlot
import turmoil.enums.ItemType
import grails.transaction.Transactional
import turmoil.generators.ItemGenerator
import turmoil.helpers.CharacterStateHelper

@Log4j
@Transactional
class CharacterService {

	def gspTagLibraryLookup // being automatically injected by spring
	def g

	public addCharacter(Account owner, String name, Gender gender, String portrait)
	{
		Character character = new Character(name: name, gender: gender, owner: owner, portrait: portrait)
		Item item = ItemGenerator.rollItemOfRarityAndType(character, ItemRarity.COMMON, ItemType.WEAPON)
		item.owner = owner
		character.equip(item)
		character.save()
	}

	public deleteCharacter(Character character)
	{
		//TODO: unequip items and put to stash

		character.delete()
	}

	def getCharacter(String characterId)
	{
		def character = Character.get(characterId)
		if (character != null)
		{
			// eager

			character.owner

			character.slotRightHand
			character.slotLeftHand

			character.slotHelm
			character.slotChest
			character.slotBelt
			character.slotGloves
			character.slotPants
			character.slotBoots
			character.slotBracers
			character.slotPauldrons

			character.slotAmulet
			character.slotRingOne
			character.slotRingTwo
			character.slotRingThree
			character.slotRingFour
		}
		return character
	}

	public static ArrayList<Item> getCharacterItems(Character character)
	{
		ArrayList<Item> items = new ArrayList<Item>()

		if (character.slotRightHand != null)
		{
			character.slotRightHand.itemSlot = ItemSlot.RIGHT_HAND
			items << character.slotRightHand
		}

		if (character.slotLeftHand != null)
		{
			character.slotLeftHand.itemSlot = ItemSlot.LEFT_HAND
			items << character.slotLeftHand
		}

		if (character.slotHelm != null)
		{
			character.slotHelm.itemSlot = ItemSlot.HELM
			items << character.slotHelm
		}

		if (character.slotChest != null)
		{
			character.slotChest.itemSlot = ItemSlot.CHEST
			items << character.slotChest
		}

		if (character.slotBelt != null)
		{
			character.slotBelt.itemSlot = ItemSlot.BELT
			items << character.slotBelt
		}

		if (character.slotGloves != null)
		{
			character.slotGloves.itemSlot = ItemSlot.GLOVES
			items << character.slotGloves
		}

		if (character.slotPants != null)
		{
			character.slotPants.itemSlot = ItemSlot.PANTS
			items << character.slotPants
		}

		if (character.slotBoots != null)
		{
			character.slotBoots.itemSlot = ItemSlot.BOOTS
			items << character.slotBoots
		}

		if (character.slotBracers != null)
		{
			character.slotBracers.itemSlot = ItemSlot.BRACERS
			items << character.slotBracers
		}

		if (character.slotPauldrons != null)
		{
			character.slotPauldrons.itemSlot = ItemSlot.PAULDRONS
			items << character.slotPauldrons
		}

		if (character.slotAmulet != null)
		{
			character.slotAmulet.itemSlot = ItemSlot.AMULET
			items << character.slotAmulet
		}

		if (character.slotRingOne != null)
		{
			character.slotRingOne.itemSlot = ItemSlot.RING_ONE
			items << character.slotRingOne
		}

		if (character.slotRingTwo != null)
		{
			character.slotRingTwo.itemSlot = ItemSlot.RING_TWO
			items << character.slotRingTwo
		}

		if (character.slotRingThree != null)
		{
			character.slotRingThree.itemSlot = ItemSlot.RING_THREE
			items << character.slotRingThree
		}

		if (character.slotRingFour != null)
		{
			character.slotRingFour.itemSlot = ItemSlot.RING_FOUR
			items << character.slotRingFour
		}

		return items
	}

	def listCharacters(Account owner)
	{
		return Character.findAllByOwner(owner)
	}

	def equipItem(Character character, Item item)
	{
		def resultMap = [:]
		Item previousItem
		def previousItemId = character.equip(item, false)
		if (previousItemId != false)
		{
			g = gspTagLibraryLookup.lookupNamespaceDispatcher("g")
			if (previousItemId != null)
			{
				previousItem = Item.get(previousItemId)
			}

			resultMap << [success: true]
			resultMap << [equippedItemId: item.id]
			resultMap << [equippedItemSlot: 'slot_' + item.itemSlot.toString().toLowerCase()]
			resultMap << [equippedItemType: item.itemType.toString()]
			if (item.itemType == ItemType.WEAPON)
			{
				resultMap << [equippedWeaponDamageType: item.damageType.toString().toLowerCase()]
			}

			def iconItemSize = 'big'
			if (item.itemType == ItemType.ARMOR && item.armorType == ArmorType.BELT)
			{
				iconItemSize = 'long'
			}
			else if (item.itemType == ItemType.ACCESSORY && (item.accessoryType == AccessoryType.AMULET || item.accessoryType == AccessoryType.RING))
			{
				iconItemSize = 'square'
			}
			resultMap << [iconItemSize: iconItemSize]
			resultMap << [equippedItemContent: g.render(contextPath: "../character/", template: "item_slot_equipment", model: [item: item, iconItemSize: iconItemSize])]

			if (previousItem != null)
			{
				previousItem.isEquipped = false
				previousItem.isStashed = true
				previousItem.stash = character.getPrimaryStash()
				previousItem.save()
				if (previousItem != null)
				{
					resultMap << [stashedItemId: previousItem.id]
					resultMap << [stashedItemContent: g.render(contextPath: "../character/", template: "item_slot_stash", model: [item: previousItem])]
				}
			}
			log.info "saved slot: ${item.itemSlot}"
			item.save()
			character.save()

			CharacterStateHelper.updateCharacterStateForItem(character, item, item.itemSlot)
		}
		else
		{
			resultMap << [success: false]
		}
		return resultMap
	}

	def unequipItem(Character character, Item item)
	{
		def resultMap = [:]

		character.properties.each { property ->
			if (property != null && property.value != null && property.key.toString().startsWith("slot"))
			{
				if (property.value.class == item.class && property.value.id == item.id)
				{
					log.info "a item slot: ${item.itemSlot}"
					item.itemSlot = character.getProperty(property.key).itemSlot
					log.info "b item slot: ${item.itemSlot}"
					character.setProperty(property.key, null)
				}
			}
		}

		if (item != null)
		{
			g = gspTagLibraryLookup.lookupNamespaceDispatcher("g")

			item.isEquipped = false
			item.isStashed = true
			item.stash = character.getPrimaryStash()

			CharacterStateHelper.updateCharacterStateForItem(character, null, item.itemSlot)

			resultMap << [success: true]
			resultMap << [stashedItemId: item.id]
			resultMap << [stashedItemType: item.itemType.toString()]
			resultMap << [stashedItemContent: g.render(contextPath: "../character/", template: "item_slot_stash", model: [item: item])]

			resultMap << [unequippedItemSlot: 'slot_' + item.itemSlot.toString().toLowerCase()]

			item.itemSlot = null
			item.save()
			character.save()

			def iconItemSize = 'big'
			if (item.itemType == ItemType.ARMOR && item.armorType == ArmorType.BELT)
			{
				iconItemSize = 'long'
			}
			else if (item.itemType == ItemType.ACCESSORY && (item.accessoryType == AccessoryType.AMULET || item.accessoryType == AccessoryType.RING))
			{
				iconItemSize = 'square'
			}
			resultMap << [iconItemSize: iconItemSize]
			resultMap << [unequippedItemContent: g.render(contextPath: "../character/", template: "item_slot_equipment", model: [item: null, iconItemSize: iconItemSize])]
		}

		return resultMap
	}
}
