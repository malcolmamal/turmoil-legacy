package turmoil.generators

import turmoil.Item
import turmoil.enums.AttributeType
import turmoil.enums.ItemRarity
import turmoil.enums.ItemType
import turmoil.helpers.ServerHelper

class ItemAttributeGeneratorTest extends GroovyTestCase
{
	public static boolean toOutput = true
	public static boolean toOutputFull = true

	void testRollAttributes()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
			ItemType itemType = ServerHelper.getEnumValues().getRandomItemType()
			Item item = ItemGenerator.generateItem(rarity, itemType)
			item.level = new Random().nextInt(50) + 1
		when:
			def attributes = ItemAttributeGenerator.rollAttributes(item)
		then:
			if (rarity == ItemRarity.COMMON)
			{
				assert attributes.size() == 0
			}
			else
			{
				assert attributes.size() > 0
			}

			if (toOutput) println "level: ${item.level}"
			if (toOutput) println rarity
			if (toOutput) println itemType
			if (toOutput) println "size: ${attributes.size()}"

			if (toOutputFull) println attributes
	}

	void testRollAttribute()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
			ItemType itemType = ServerHelper.getEnumValues().getRandomItemType()
			Item item = ItemGenerator.generateItem(rarity, itemType)
			item.level = new Random().nextInt(50) + 1
			ArrayList<AttributeType> appropriateAttributeTypes = ItemAttributeGenerator.pickAppropriateAttributeTypes(item)
		when:
			def attribute = ItemAttributeGenerator.rollAttribute(item, appropriateAttributeTypes)
		then:
			assert attribute != null

			if (toOutput) println "level: ${item.level}"
			if (toOutput) println rarity
			if (toOutput) println itemType
			if (toOutput) println attribute
	}

	void testGetPrimaryStatValue()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
			ItemType itemType = ServerHelper.getEnumValues().getRandomItemType()
			Item item = ItemGenerator.generateItem(rarity, itemType)
			item.level = new Random().nextInt(50) + 1
		when:
			def statValue = ItemAttributeGenerator.getPrimaryStatValue(item)
		then:
			assert statValue > 0

			if (toOutput) println "value: ${statValue}"
	}

	void testPickAppropriateAttributeTypes()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
			ItemType itemType = ServerHelper.getEnumValues().getRandomItemType()
			Item item = ItemGenerator.generateItem(rarity, itemType)
			item.level = new Random().nextInt(50) + 1
		when:
			def attributeTypes = ItemAttributeGenerator.pickAppropriateAttributeTypes(item)
		then:
			assert attributeTypes != null
			assert attributeTypes.size() > 0

			if (toOutput) println "size: ${attributeTypes.size()}"
			if (toOutput) println attributeTypes
	}
}
