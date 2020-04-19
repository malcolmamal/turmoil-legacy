package turmoil.templates

import java.util.Map

import turmoil.enums.ArmorType

class ArmorTemplate extends ItemTemplate
{
	Integer armorValue

	ArmorType armorType

	public String toString()
	{
		return	"[ " 			+ super.toString()	+ ", " +
				"armorValue: "	+ armorValue		+ ", " +
				"armorType: "	+ armorType			+
				" ]"
	}

	public Map toMap()
	{
		return super.toMap() + [
			armorValue: armorValue,
			armorType: armorType
		]
	}
}
