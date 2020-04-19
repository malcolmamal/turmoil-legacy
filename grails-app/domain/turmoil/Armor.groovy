package turmoil

import turmoil.enums.ArmorType
import turmoil.enums.ItemType

class Armor extends Item
{
	ItemType itemType = ItemType.ARMOR
	ArmorType armorType

	Integer armorValue = 0

	String toString() {
		"${itemName}, #${id}"
	}

	static constraints = {
	}

	def isLongLayout()
	{
		return (armorType == ArmorType.BELT) ? true : false
	}

	def getImagePath()
	{
		return super.getImagePath() + "armors" + (rarity.isPlain() ? "/" + getArmorFileCode() : "")
	}

	def String getArmorFileCode()
	{
		switch (armorType)
		{
			case ArmorType.BELT:
				return "belts"
			case ArmorType.BOOTS:
				return "boots"
			case ArmorType.BRACERS:
				return "bracers"
			case ArmorType.CHEST:
				return "chests"
			case ArmorType.GLOVES:
				return "gloves"
			case ArmorType.HELM:
				return "helms"
			case ArmorType.PANTS:
				return "pants"
			case ArmorType.PAULDRONS:
				return "pauldrons"
		}
	}

	public String toStringFull()
	{
		return	"[ " 			+ super.toStringFull()	+ ", " +
				"itemType: "	+ itemType				+ ", " +
				"armorType: "	+ armorType				+ ", " +
				"armorValue: "	+ armorValue			+
				" ]"
	}
}
