package turmoil

import turmoil.enums.ItemRarity
import turmoil.enums.ItemSlot
import turmoil.enums.ItemType

class Item
{
	String itemCode
	String itemName
	Integer level

	Integer durability = 1
	Integer priceValue = 1

	ItemType itemType
	ItemRarity rarity = ItemRarity.COMMON

	Boolean isCrafted = false
	Boolean isEquipped = false
	Boolean isStashed = false

	Stash stash

	Character craftedBy
	Account owner

	ItemSlot itemSlot

	String toString() {
		"${itemName}, #${id}"
	}

	//static transients = ['itemSlot']

	/*
	static hasOne = {
		craftedBy: Character
		owner: Account
		stash: Stash
	}
	*/

	static hasMany = [attributes: Attribute]

	static constraints = {
		itemName nullable: true
		craftedBy nullable: true
		owner nullable: false
		stash nullable: true
		itemSlot nullable: true
	}

	static mapping = {
		tablePerHierarchy false
	}

	def getFileCode()
	{
		return itemCode.toString().toLowerCase()
	}

	def getRarityClass()
	{
		switch (rarity)
		{
			case ItemRarity.COMMON:
				return "white"
			case ItemRarity.MAGIC:
				return "blue"
			case ItemRarity.RARE:
				return "yellow"
			case ItemRarity.LEGENDARY:
				return "orange"
			case ItemRarity.SET:
				return "green"
			case ItemRarity.UNIQUE:
				return "red"
			case ItemRarity.EPIC:
				return "purple"
		}
	}

	def getItemProperties()
	{
		return String[]
	}

	def putToStash(Stash newStash)
	{
		if (newStash != null)
		{
			stash = newStash
			isStashed = true
		}
	}

	def getImagePath()
	{
		return "images/items/"
	}

	def getImageFile()
	{
		return getFileCode() + '.png'
	}

	public String toStringFull()
	{
		def value =	"itemCode: "	+ itemCode		+ ", " +
					"itemName: "	+ itemName		+ ", " +
					"level: "		+ level			+ ", " +
					"rarity: "		+ rarity		+ ", " +
					"isCrafted: "	+ isCrafted		+ ", " +
					"isEquipped: "	+ isEquipped	+ ", " +
					"isStashed: "	+ isStashed		+ ", " +
					"stash: "		+ stash			+ ", " +
					"craftedBy: "	+ craftedBy		+ ", " +
					"owner: "		+ owner			+ ", " +
					"itemSlot: "	+ itemSlot

		value += ", [attributes: "
		attributes.each {
			value += it.toStringFull() + " "
		}
		value += "]"

		return value
	}
}
