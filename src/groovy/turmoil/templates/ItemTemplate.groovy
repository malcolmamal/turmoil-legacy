package turmoil.templates

class ItemTemplate
{
	String itemCode
	Boolean isLegendary = false

	public String toString()
	{
		return "itemCode: " + itemCode + ", isLegendary: " + isLegendary
	}

	public Map toMap()
	{
		return [
			itemCode: itemCode,
			isLegendary: isLegendary
		]
	}
}
