package turmoil

import turmoil.enums.AttributeType

class Attribute
{
	Item item

	AttributeType type

	double primaryValue = 0
	double secondaryValue = 0
	double tertiaryValue = 0

	static constraints = {
		type nullable: false
	}

	String toString() {
		"${type} [${primaryValue}, ${secondaryValue}, ${tertiaryValue}]"
	}

	public String toStringFull()
	{
		return	"[ " 				+
				"type: "			+ type				+ ", " +
				"primaryValue: "	+ primaryValue		+ ", " +
				"secondaryValue: "	+ secondaryValue	+ ", " +
				"tertiaryValue: "	+ tertiaryValue		+
				" ]"
	}

	def getValues()
	{
		return [primaryValue, secondaryValue, tertiaryValue]
	}
}
