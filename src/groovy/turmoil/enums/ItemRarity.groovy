package turmoil.enums

public enum ItemRarity {

	COMMON,
	MAGIC,
	RARE,
	LEGENDARY,
	SET,
	UNIQUE,
	EPIC

	private Boolean isPlain = true
	private Integer minAttributes = 0
	private Integer maxAttributes = 0

	static {
		LEGENDARY.isPlain = false
		SET.isPlain = false
		UNIQUE.isPlain = false
		EPIC.isPlain = false

		COMMON.minAttributes = 0
		COMMON.maxAttributes = 0

		MAGIC.minAttributes = 1
		MAGIC.maxAttributes = 2

		RARE.minAttributes = 3
		RARE.maxAttributes = 4

		LEGENDARY.minAttributes = 5
		LEGENDARY.maxAttributes = 5

		SET.minAttributes = 5
		SET.maxAttributes = 5

		UNIQUE.minAttributes = 6
		UNIQUE.maxAttributes = 6

		EPIC.minAttributes = 7
		EPIC.maxAttributes = 7
	}

	public Boolean isPlain()
	{
		return isPlain
	}

	public Integer getAttributesQuantity()
	{
		Integer attributesQuantity = minAttributes
		if (maxAttributes - minAttributes > 0)
		{
			def random = new Random()
			attributesQuantity += random.nextInt(maxAttributes - minAttributes + 1)
		}
		return attributesQuantity
	}

}