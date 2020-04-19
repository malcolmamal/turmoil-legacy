package turmoil.generators

import turmoil.Accessory
import turmoil.Armor
import turmoil.Character
import turmoil.Item
import turmoil.Weapon
import turmoil.enums.ItemRarity
import turmoil.enums.ItemType
import turmoil.helpers.ServerHelper

class ItemGeneratorTest extends GroovyTestCase
{
	public static boolean toOutput = true
	public static boolean toOutputFull = true

	void testRollItem()
	{
		given:
			def character = new Character()
			character.level = new Random().nextInt(50) + 1
		when:
			def item = ItemGenerator.rollItem(character)
		then:
			assert item != null

			if (toOutput) println item
	}

	void testGenerateName()
	{
		when:
			def item = new Item()
			ItemGenerator.generateName(item)
		then:
			assert item.itemName != null

			if (toOutput) println item.itemName
	}

	void testGenerateAccessory()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
		when:
			Accessory accessory = ItemGenerator.generateAccessory(rarity)
		then:
			assert accessory != null
			assert accessory.rarity == rarity
			assert accessory.itemType == ItemType.ACCESSORY
			assert accessory.accessoryType != null

			if (toOutput) println accessory.itemType
			if (toOutput) println accessory.rarity
			if (toOutput) println accessory.accessoryType

			if (toOutputFull) println accessory
	}

	void testGenerateArmor()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
		when:
			Armor armor = ItemGenerator.generateArmor(rarity)
		then:
			assert armor != null
			assert armor.rarity == rarity
			assert armor.itemType == ItemType.ARMOR
			assert armor.armorType != null

			if (toOutput) println armor.itemType
			if (toOutput) println armor.rarity
			if (toOutput) println armor.armorType

			if (toOutputFull) println armor
	}

	void testGenerateWeapon()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
		when:
			Weapon weapon = ItemGenerator.generateWeapon(rarity)
		then:
			assert weapon != null
			assert weapon.rarity == rarity
			assert weapon.itemType == ItemType.WEAPON
			assert weapon.weaponType != null

			if (toOutput) println weapon.itemType
			if (toOutput) println weapon.rarity
			if (toOutput) println weapon.weaponType

			if (toOutputFull) println weapon
	}

	void testGetRandomTemplate()
	{
		given:
			def commonAccessoryTemplates = ServerHelper.getItemTemplates().accessoryCommonTemplates
			def commonArmorTemplates = ServerHelper.getItemTemplates().armorCommonTemplates
			def commonWeaponTemplates = ServerHelper.getItemTemplates().weaponCommonTemplates

			def legendaryAccessoryTemplates = ServerHelper.getItemTemplates().accessoryLegendaryTemplates
			def legendaryArmorTemplates = ServerHelper.getItemTemplates().armorLegendaryTemplates
			def legendaryWeaponTemplates = ServerHelper.getItemTemplates().weaponLegendaryTemplates
		when:
			def commonAccessoryTemplate = ItemGenerator.getRandomTemplate(commonAccessoryTemplates)
			def commonArmorTemplate = ItemGenerator.getRandomTemplate(commonArmorTemplates)
			def commonWeaponTemplate = ItemGenerator.getRandomTemplate(commonWeaponTemplates)

			def legendaryAccessoryTemplate = ItemGenerator.getRandomTemplate(legendaryAccessoryTemplates)
			def legendaryArmorTemplate = ItemGenerator.getRandomTemplate(legendaryArmorTemplates)
			def legendaryWeaponTemplate = ItemGenerator.getRandomTemplate(legendaryWeaponTemplates)
		then:
			assert commonAccessoryTemplate != null
			assert !commonAccessoryTemplate.isLegendary
			assert commonAccessoryTemplate.accessoryType != null

			assert commonArmorTemplate != null
			assert !commonArmorTemplate.isLegendary
			assert commonArmorTemplate.armorType != null

			assert commonWeaponTemplate != null
			assert !commonWeaponTemplate.isLegendary
			assert commonWeaponTemplate.weaponType != null

			assert legendaryAccessoryTemplate != null
			assert legendaryAccessoryTemplate.isLegendary
			assert legendaryAccessoryTemplate.accessoryType != null

			assert legendaryArmorTemplate != null
			assert legendaryArmorTemplate.isLegendary
			assert legendaryArmorTemplate.armorType != null

			assert legendaryWeaponTemplate != null
			assert legendaryWeaponTemplate.isLegendary
			assert legendaryWeaponTemplate.weaponType != null

			if (toOutputFull) println commonAccessoryTemplate
			if (toOutputFull) println commonArmorTemplate
			if (toOutputFull) println commonWeaponTemplate
			if (toOutputFull) println legendaryAccessoryTemplate
			if (toOutputFull) println legendaryArmorTemplate
			if (toOutputFull) println legendaryWeaponTemplate
	}

	void testRollStats()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
			ItemType itemType = ServerHelper.getEnumValues().getRandomItemType()
			Item item = ItemGenerator.generateItem(rarity, itemType)
		when:
			ItemGenerator.rollStats(item)
		then:
			assert item != null

			if (toOutput) println rarity
			if (toOutput) println itemType
			if (toOutput) println item
	}

	void testRollAccessoryStats()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
			Accessory accessory = ItemGenerator.generateAccessory(rarity)
		when:
			ItemGenerator.rollAccessoryStats(accessory)
		then:
			assert accessory != null

			if (toOutput) println accessory
	}

	void testRollArmorStats()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
			Armor armor = ItemGenerator.generateArmor(rarity)
		when:
			ItemGenerator.rollArmorStats(armor)
		then:
			assert armor.armorValue > 0

			if (toOutput) println armor
	}

	void testRollWeaponStats()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
			Weapon weapon = ItemGenerator.generateWeapon(rarity)
		when:
			ItemGenerator.rollWeaponStats(weapon)
		then:
			assert weapon.minDamage > 0
			assert weapon.maxDamage >= weapon.minDamage
			assert weapon.getAverageDamage() >= weapon.minDamage
			assert weapon.getAverageDamage() <= weapon.maxDamage

			if (toOutput) println weapon
	}

	void testGetRarityMultiplier()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
		when:
			def multiplier = ItemGenerator.getRarityMultiplier(rarity)
		then:
			assert multiplier >= 1

			if (toOutput) println "${rarity}: ${multiplier}"
	}

	void testAddAttributes()
	{
		given:
			ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
			ItemType itemType = ServerHelper.getEnumValues().getRandomItemType()
			Item item = ItemGenerator.generateItem(rarity, itemType)
			item.level = new Random().nextInt(50) + 1
		when:
			ItemGenerator.addAttributes(item)
		then:
			assert item.attributes != null
			if (rarity == ItemRarity.COMMON)
			{
				assert item.attributes.size() == 0
			}
			else
			{
				assert item.attributes.size() > 0
			}

			if (toOutput) println "level: ${item.level}"
			if (toOutput) println rarity
			if (toOutput) println itemType
			if (toOutput) println "quantity: ${item.attributes.size()}"
			if (toOutput) println item
	}
}
