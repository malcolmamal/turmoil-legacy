package turmoil

import groovy.util.logging.Log4j
import turmoil.enums.ItemSlot
import turmoil.enums.ItemType
import turmoil.enums.AccessoryType
import turmoil.enums.ArmorType
import turmoil.enums.WeaponType

@Log4j
class Character extends Person
{
	String portrait

	Integer experience = 0

	Account owner

	Weapon slotRightHand
	Item slotLeftHand // Weapon or Accessory

	Armor slotHelm
	Armor slotChest
	Armor slotBelt
	Armor slotGloves
	Armor slotPants
	Armor slotBoots
	Armor slotBracers
	Armor slotPauldrons

	Accessory slotAmulet
	Accessory slotRingOne
	Accessory slotRingTwo
	Accessory slotRingThree
	Accessory slotRingFour

	CharacterState characterState

	String toString() {
		"${name}, lvl ${level}"
	}

	/*
	static hasOne = {
		owner: Account
		slotRightHand: Weapon
		slotLeftHand: Item
		slotHelm: Armor
		slotChest: Armor
		slotBelt: Armor
		slotGloves: Armor
		slotPants: Armor
		slotBoots: Armor
		slotBracers: Armor
		slotPauldrons: Armor
		slotAmulet: Accessory
		slotRingOne: Accessory
		slotRingTwo: Accessory
		slotRingThree: Accessory
		slotRingFour: Accessory

		characterState: CharacterState
	}
	*/

	static constraints = {
		owner nullable: false
		slotRightHand nullable: true
		slotLeftHand nullable: true
		slotHelm nullable: true
		slotChest nullable: true
		slotBelt nullable: true
		slotGloves nullable: true
		slotPants nullable: true
		slotBoots nullable: true
		slotBracers nullable: true
		slotPauldrons nullable: true
		slotAmulet nullable: true
		slotRingOne nullable: true
		slotRingTwo nullable: true
		slotRingThree nullable: true
		slotRingFour nullable: true

		characterState nullable: true // for the moment
	}

	static mapping = {
		table "characters" // Unfortunately 'character' is a reserved word in MySQL therefore plural form is used as an exception
	}

	public Stash getPrimaryStash()
	{
		def stash = Stash.findByOwner(owner)
		if (stash == null)
		{
			stash = new Stash()
			stash.owner = owner
			stash.save()
		}
		return stash
	}

	/**
	 * TODO split into equipAccessory / equipArmor / equipWeapon
	 * TODO for rings and one handed weapons there should be a parameter to wear in left/rightHand and one/twoRing
	 * TODO later: move unequipped item into some inventory/stash
	 *
	 * @param Item
	 * @return
	 */
	def equip(Item item, Boolean saveItem = true)
	{
		def replacedItem
		switch (item.itemType)
		{
			case ItemType.ARMOR:
				switch (((Armor)item).armorType)
				{
					case ArmorType.HELM:
						if (slotHelm != null)
						{
							replacedItem = slotHelm.id
						}
						slotHelm = item
						item.itemSlot = ItemSlot.HELM
						break
					case ArmorType.CHEST:
						if (slotChest != null)
						{
							replacedItem = slotChest.id
						}
						slotChest = item
						item.itemSlot = ItemSlot.CHEST
						break
					case ArmorType.BELT:
						if (slotBelt != null)
						{
							replacedItem = slotBelt.id
						}
						slotBelt = item
						item.itemSlot = ItemSlot.BELT
						break
					case ArmorType.GLOVES:
						if (slotGloves != null)
						{
							replacedItem = slotGloves.id
						}
						slotGloves = item
						item.itemSlot = ItemSlot.GLOVES
						break
					case ArmorType.PANTS:
						if (slotPants != null)
						{
							replacedItem = slotPants.id
						}
						slotPants = item
						item.itemSlot = ItemSlot.PANTS
						break
					case ArmorType.BOOTS:
						if (slotBoots != null)
						{
							replacedItem = slotBoots.id
						}
						slotBoots = item
						item.itemSlot = ItemSlot.BOOTS
						break
					case ArmorType.BRACERS:
						if (slotBracers != null)
						{
							replacedItem = slotBracers.id
						}
						slotBracers = item
						item.itemSlot = ItemSlot.BRACERS
						break
					case ArmorType.PAULDRONS:
						if (slotPauldrons != null)
						{
							replacedItem = slotPauldrons.id
						}
						slotPauldrons = item
						item.itemSlot = ItemSlot.PAULDRONS
						break
				}
				break
			case ItemType.ACCESSORY:
				switch (((Accessory)item).accessoryType)
				{
					case AccessoryType.AMULET:
						if (slotAmulet != null)
						{
							replacedItem = slotAmulet.id
						}
						slotAmulet = item
						item.itemSlot = ItemSlot.AMULET
						break
					case AccessoryType.RING:
						if (slotRingOne == null)
						{
							slotRingOne = item
							item.itemSlot = ItemSlot.RING_ONE
						}
						else if (slotRingTwo == null)
						{
							slotRingTwo = item
							item.itemSlot = ItemSlot.RING_TWO
						}
						else if (slotRingThree == null)
						{
							slotRingThree = item
							item.itemSlot = ItemSlot.RING_THREE
						}
						else
						{
							slotRingFour = item
							item.itemSlot = ItemSlot.RING_FOUR
						}
						break
					case AccessoryType.MOJO:
					case AccessoryType.SOURCE:
					case AccessoryType.SHIELD:
						if (slotLeftHand == null && (slotRightHand == null || slotRightHand.isOneHanded()))
						{
							slotLeftHand = item
							item.itemSlot = ItemSlot.LEFT_HAND
						}
						else
						{
							return false
						}
						break
					case AccessoryType.QUIVER:
						if (slotLeftHand == null
							&& (slotRightHand == null || slotRightHand.weaponType == WeaponType.BOW || slotRightHand.weaponType == WeaponType.CROSSBOW))
						{
							slotLeftHand = item
							item.itemSlot = ItemSlot.LEFT_HAND
						}
						else
						{
							return false
						}
						break
				}
				break
			case ItemType.WEAPON:
				if (item.isOneHanded())
				{
					if (slotRightHand == null)
					{
						slotRightHand = item
						item.itemSlot = ItemSlot.RIGHT_HAND
					}
					else if (slotLeftHand == null)
					{
						slotLeftHand = item
						item.itemSlot = ItemSlot.LEFT_HAND
					}
					else
					{
						replacedItem = slotRightHand.id
						slotRightHand = item
						item.itemSlot = ItemSlot.RIGHT_HAND
					}
				}
				else if (slotLeftHand == null)
				{
					if (slotRightHand != null)
					{
						replacedItem = slotRightHand.id
					}
					slotRightHand = item
					item.itemSlot = ItemSlot.RIGHT_HAND
				}
				else if (slotRightHand == null)
				{
					if (slotLeftHand != null)
					{
						replacedItem = slotLeftHand.id
					}
					slotRightHand = item
					item.itemSlot = ItemSlot.RIGHT_HAND
				}
				else
				{
					return false
				}
				break
		}

		item.isEquipped = true
		item.isStashed = false
		item.stash = null
		if (saveItem)
		{
			item.save()
		}
		return replacedItem
	}
}
