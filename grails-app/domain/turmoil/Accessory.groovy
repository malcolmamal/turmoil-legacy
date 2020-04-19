package turmoil

import turmoil.enums.AccessoryType
import turmoil.enums.ItemType

class Accessory extends Item
{
	ItemType itemType = ItemType.ACCESSORY
	AccessoryType accessoryType

	String toString() {
		"${itemName}, #${id}"
	}

	static constraints = {
	}

	def isSquareLayout()
	{
		return (accessoryType == AccessoryType.RING || accessoryType == AccessoryType.AMULET) ? true : false
	}

	def getImagePath()
	{
		return super.getImagePath() + "accessories" + (rarity.isPlain() ? "/" + getAccessoryFileCode() : "")
	}

	def String getAccessoryFileCode()
	{
		switch (accessoryType)
		{
			case AccessoryType.AMULET:
				return "amulets"
			case AccessoryType.MOJO:
				return "mojos"
			case AccessoryType.QUIVER:
				return "quivers"
			case AccessoryType.RING:
				return "rings"
			case AccessoryType.SHIELD:
				return "shields"
			case AccessoryType.SOURCE:
				return "sources"
		}
	}

	def Boolean isJewellery()
	{
		return (accessoryType == AccessoryType.RING || accessoryType == AccessoryType.AMULET)
	}

	public String toStringFull()
	{
		return	"[ " 				+ super.toStringFull()	+ ", " +
				"itemType: "		+ itemType				+ ", " +
				"accessoryType: "	+ accessoryType			+
				" ]"
	}
}
