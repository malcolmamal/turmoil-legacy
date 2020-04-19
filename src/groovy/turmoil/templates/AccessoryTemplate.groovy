package turmoil.templates

import turmoil.enums.AccessoryType

class AccessoryTemplate extends ItemTemplate
{
	AccessoryType accessoryType

	public String toString()
	{
		return	"[ " 				+ super.toString()	+ ", " +
				"accessoryType: "	+ accessoryType		+
				" ]"
	}

	public Map toMap()
	{
		return super.toMap() + [
			accessoryType: accessoryType
		]
	}
}
