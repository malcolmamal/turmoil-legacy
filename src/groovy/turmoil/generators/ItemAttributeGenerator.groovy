package turmoil.generators

import groovy.util.logging.Log4j
import turmoil.Accessory
import turmoil.Armor
import turmoil.Attribute
import turmoil.Item
import turmoil.Weapon
import turmoil.enums.AccessoryType
import turmoil.enums.ArmorType
import turmoil.enums.AttributeType
import turmoil.enums.ItemRarity
import turmoil.enums.ItemType
import turmoil.instances.EnumValues

@Log4j
class ItemAttributeGenerator
{
	public static ArrayList<AttributeType> pickAppropriateAttributeTypes(Item item)
	{
		def appropriateAttributeTypes = [
			AttributeType.PRIMARY_STRENGTH,
			AttributeType.PRIMARY_DEXTERITY,
			AttributeType.PRIMARY_INTELLIGENCE,
			AttributeType.PRIMARY_VITALITY,

			AttributeType.PRIMARY_STRENGTH_AND_VITALITY,
			AttributeType.PRIMARY_DEXTERITY_AND_VITALITY,
			AttributeType.PRIMARY_INTELLIGENCE_AND_VITALITY,

			AttributeType.INDESTRUCTIBLE
		]

		if (item.itemType != ItemType.WEAPON)
		{
			appropriateAttributeTypes += [
				AttributeType.RESIST_FIRE,
				AttributeType.RESIST_COLD,
				AttributeType.RESIST_LIGHTNING,
				AttributeType.RESIST_POISON,
				AttributeType.RESIST_ARCANE,
				AttributeType.RESIST_BLEED,
				AttributeType.RESIST_PIERCING,

				AttributeType.HEALTH_PERCENTAGE,
				AttributeType.MANA_PERCENTAGE
			]

			if (!item.rarity.isPlain())
			{
				appropriateAttributeTypes << AttributeType.RESIST_ALL
			}
		}

		if (item.itemType != ItemType.ARMOR)
		{
			appropriateAttributeTypes += [
				AttributeType.LIFE_HIT,
				AttributeType.LIFE_REGEN,

				AttributeType.MANA_HIT,
				AttributeType.MANA_REGEN
			]

			if (item.level > 15)
			{
				appropriateAttributeTypes += [
					AttributeType.LIFE_LEECH,
					AttributeType.MANA_LEECH
				]
			}

			if (!item.rarity.isPlain())
			{
				appropriateAttributeTypes += [
					AttributeType.CHANCE_TO_STUN,
					AttributeType.CHANCE_TO_SAP,
					AttributeType.CHANCE_TO_CONFUSE,
					AttributeType.CHANCE_TO_BURN,
					AttributeType.CHANCE_TO_CHILL,
					AttributeType.CHANCE_TO_SHOCK,
					AttributeType.CHANCE_TO_DEVASTATE
				]
			}
		}

		switch (item.itemType)
		{
			case ItemType.ACCESSORY:
				if (((Accessory)item).accessoryType == AccessoryType.SHIELD)
				{
					appropriateAttributeTypes << AttributeType.EVASION_BLOCK
				}

				if (((Accessory)item).isJewellery())
				{
					appropriateAttributeTypes += [
						AttributeType.FIND_QUANTITY,
						AttributeType.FIND_QUALITY,
						AttributeType.FIND_GOLD
					]
				}
				break
			case ItemType.ARMOR:
				appropriateAttributeTypes += [
					AttributeType.ARMOR,
					AttributeType.EVASION_DODGE,

					AttributeType.REDUCED_DAMAGE_MELEE,
					AttributeType.REDUCED_DAMAGE_RANGE
				]

				if (((Armor)item).armorType == ArmorType.BOOTS)
				{
					appropriateAttributeTypes << AttributeType.MOVEMENT
				}

				if (((Armor)item).armorType == ArmorType.GLOVES)
				{
					appropriateAttributeTypes << AttributeType.CRITICAL_CHANCE
					appropriateAttributeTypes << AttributeType.CRITICAL_DAMAGE
				}
				break
			case ItemType.WEAPON:
				appropriateAttributeTypes += [
					AttributeType.CRITICAL_CHANCE,
					AttributeType.CRITICAL_DAMAGE,
					AttributeType.EVASION_PARRY,

					AttributeType.EFFECT_BLEED,

					AttributeType.DAMAGE_PHYSICAL_VALUE,
					AttributeType.DAMAGE_PHYSICAL_PERCENTAGE,
					AttributeType.DAMAGE_FIRE_VALUE,
					AttributeType.DAMAGE_FIRE_PERCENTAGE,
					AttributeType.DAMAGE_COLD_VALUE,
					AttributeType.DAMAGE_COLD_PERCENTAGE,
					AttributeType.DAMAGE_LIGHTNING_VALUE,
					AttributeType.DAMAGE_LIGHTNING_PERCENTAGE,
					AttributeType.DAMAGE_POISON_VALUE,
					AttributeType.DAMAGE_POISON_PERCENTAGE,
					AttributeType.DAMAGE_ARCANE_VALUE,
					AttributeType.DAMAGE_ARCANE_PERCENTAGE,

					AttributeType.DAMAGE_VALUE,
					AttributeType.DAMAGE_PERCENTAGE
				]

				if (!item.rarity.isPlain())
				{
					appropriateAttributeTypes << AttributeType.EFFECT_CULL
				}
				break
		}

		return appropriateAttributeTypes
	}

	public static ArrayList<Attribute> rollAttributes(Item item)
	{
		ArrayList<AttributeType> appropriateAttributeTypes = pickAppropriateAttributeTypes(item)
		ArrayList<Attribute> attributes = []

		int quantity = item.rarity.getAttributesQuantity()
		for (int i = 0; i < quantity; i++)
		{
			if (appropriateAttributeTypes.size() > 0)
			{
				Attribute attribute = ItemAttributeGenerator.rollAttribute(item, appropriateAttributeTypes)
				if (attribute != null)
				{
					attributes << attribute
				}
			}
		}
		return attributes
	}

	public static Attribute rollAttribute(Item item, ArrayList<AttributeType> possibleAttributeTypes)
	{
		def primaryValue = 0
		def secondaryValue = 0
		def tertiaryValue = 0

		AttributeType attributeType = EnumValues.getRandomValue(possibleAttributeTypes)

		def wasProcessed = false

		if (attributeType == AttributeType.INDESTRUCTIBLE)
		{
			wasProcessed = true
		}

		if (attributeType == AttributeType.MOVEMENT)
		{
			primaryValue = 1 + new Random().nextInt(2)
			wasProcessed = true
		}

		if (attributeType.isPrimaryStat())
		{
			primaryValue = getPrimaryStatValue(item)

			if (attributeType.isPrimaryDoubleStat())
			{
				primaryValue = Math.floor(primaryValue / 2)
				secondaryValue = Math.floor(getPrimaryStatValue(item) / 2)

				possibleAttributeTypes.removeAll([
					AttributeType.PRIMARY_STRENGTH_AND_VITALITY,
					AttributeType.PRIMARY_DEXTERITY_AND_VITALITY,
					AttributeType.PRIMARY_INTELLIGENCE_AND_VITALITY
				])
			}
			else
			{
				possibleAttributeTypes.removeAll([
					AttributeType.PRIMARY_STRENGTH,
					AttributeType.PRIMARY_DEXTERITY,
					AttributeType.PRIMARY_INTELLIGENCE,
					AttributeType.PRIMARY_VITALITY
				])
			}

			if (item.itemType == ItemType.WEAPON && !((Weapon)item).isOneHanded())
			{
				// two handers get more primary stats
				primaryValue = Math.ceil(primaryValue * 1.5)
				if (secondaryValue != 0)
				{
					secondaryValue = Math.ceil(secondaryValue * 1.5)
				}
			}

			wasProcessed = true
		}

		if (attributeType == AttributeType.CRITICAL_CHANCE || attributeType == AttributeType.CRITICAL_DAMAGE)
		{
			def minCritValue = 3
			def maxCritValue = 5

			if (item.itemType == ItemType.WEAPON)
			{
				minCritValue += 1
				maxCritValue += 3

				if (!((Weapon)item).isOneHanded())
				{
					minCritValue += 1
					maxCritValue += 2
				}
			}

			primaryValue = (minCritValue + (maxCritValue - minCritValue) * new Random().nextDouble()).round(1)
			if (attributeType == AttributeType.CRITICAL_DAMAGE)
			{
				primaryValue *= 10
			}
			wasProcessed = true
		}

		if (attributeType in [AttributeType.DAMAGE_PHYSICAL_VALUE, AttributeType.DAMAGE_FIRE_VALUE,
							  AttributeType.DAMAGE_COLD_VALUE, AttributeType.DAMAGE_LIGHTNING_VALUE,
							  AttributeType.DAMAGE_POISON_VALUE, AttributeType.DAMAGE_ARCANE_VALUE,
							  AttributeType.DAMAGE_VALUE])
		{
			def baseDamage = item.level * 1.5
			if (attributeType == AttributeType.DAMAGE_VALUE)
			{
				baseDamage *= 0.8
			}

			if (!item.rarity.isPlain())
			{
				baseDamage *= 1.3
			}

			primaryValue = 1 + (baseDamage + (2 * baseDamage - baseDamage) * new Random().nextDouble()).round()
			secondaryValue = 1 + (primaryValue + (2 * primaryValue - primaryValue) * new Random().nextDouble()).round()

			wasProcessed = true

			possibleAttributeTypes.removeAll([
				AttributeType.DAMAGE_PHYSICAL_VALUE,
				AttributeType.DAMAGE_FIRE_VALUE,
				AttributeType.DAMAGE_COLD_VALUE,
				AttributeType.DAMAGE_LIGHTNING_VALUE,
				AttributeType.DAMAGE_POISON_VALUE,
				AttributeType.DAMAGE_ARCANE_VALUE,
				AttributeType.DAMAGE_VALUE
			])
		}

		if (attributeType in [AttributeType.DAMAGE_PHYSICAL_PERCENTAGE, AttributeType.DAMAGE_FIRE_PERCENTAGE,
							  AttributeType.DAMAGE_COLD_PERCENTAGE, AttributeType.DAMAGE_LIGHTNING_PERCENTAGE,
							  AttributeType.DAMAGE_POISON_PERCENTAGE, AttributeType.DAMAGE_ARCANE_PERCENTAGE])
		{
			primaryValue = (10 + (20 - 10) * new Random().nextDouble()).round()
			if (!item.rarity.isPlain())
			{
				primaryValue += 2
			}
			wasProcessed = true

			possibleAttributeTypes.removeAll([
				AttributeType.DAMAGE_PHYSICAL_PERCENTAGE,
				AttributeType.DAMAGE_FIRE_PERCENTAGE,
				AttributeType.DAMAGE_COLD_PERCENTAGE,
				AttributeType.DAMAGE_LIGHTNING_PERCENTAGE,
				AttributeType.DAMAGE_POISON_PERCENTAGE,
				AttributeType.DAMAGE_ARCANE_PERCENTAGE
			])
		}

		if (attributeType == AttributeType.DAMAGE_PERCENTAGE)
		{
			primaryValue = (6 + (12 - 6) * new Random().nextDouble()).round()
			if (!item.rarity.isPlain())
			{
				primaryValue += 1
			}
			wasProcessed = true
		}

		if (attributeType.isResist())
		{
			primaryValue = (10 + (25 - 10) * new Random().nextDouble()).round()
			wasProcessed = true

			possibleAttributeTypes.removeAll([
				AttributeType.RESIST_FIRE,
				AttributeType.RESIST_COLD,
				AttributeType.RESIST_LIGHTNING,
				AttributeType.RESIST_POISON,
				AttributeType.RESIST_ARCANE,
				AttributeType.RESIST_BLEED,
				AttributeType.RESIST_PIERCING,
				AttributeType.RESIST_ALL
			])
		}

		if (attributeType == AttributeType.ARMOR)
		{
			primaryValue = (double)item.level + (10 + (25 - 10) * new Random().nextDouble()).round()
			if (item.itemType == ItemType.ARMOR && ((Armor)item).armorType == ArmorType.CHEST)
			{
				primaryValue = Math.ceil(primaryValue * 1.4)
			}
			wasProcessed = true
		}

		// TODO: if it's a shield then block should be always rolled ? (or is it just additional block to the base one?)
		if (attributeType == AttributeType.EVASION_BLOCK)
		{
			def minBlockChance = 10
			def maxBlockChance = 25
			def blockAmountMultiplier = 1

			if (item.rarity == ItemRarity.RARE)
			{
				minBlockChance += 5
				maxBlockChance += 5
				blockAmountMultiplier = 1.2
			}
			else if (!item.rarity.isPlain())
			{
				minBlockChance += 10
				maxBlockChance += 10
				blockAmountMultiplier = 1.4

				if (item.rarity == ItemRarity.EPIC)
				{
					maxBlockChance += 5
					blockAmountMultiplier = 1.5
				}
			}

			primaryValue = (minBlockChance + (maxBlockChance - minBlockChance) * new Random().nextDouble()).round()

			def baseBlockAmount = 5.0 + (double)item.level * blockAmountMultiplier
			secondaryValue = (baseBlockAmount + (2.0 * baseBlockAmount - baseBlockAmount) * new Random().nextDouble()).round()
			tertiaryValue = (1.0 + secondaryValue + (2.0 * secondaryValue - secondaryValue) * new Random().nextDouble()).round()
			wasProcessed = true
		}

		if (attributeType == AttributeType.EVASION_PARRY)
		{
			primaryValue = (15 + (25 - 15) * new Random().nextDouble()).round()
			wasProcessed = true
		}

		if (attributeType == AttributeType.EVASION_DODGE)
		{
			primaryValue = (4 + (8 - 4) * new Random().nextDouble()).round()
			wasProcessed = true
		}

		if (attributeType in [AttributeType.LIFE_HIT, AttributeType.MANA_HIT])
		{
			def baseOnHit = Math.ceil(item.level / 5)
			primaryValue = 1 + (baseOnHit + (2.0 * baseOnHit - baseOnHit) * new Random().nextDouble()).round()
			wasProcessed = true
		}

		if (attributeType in [AttributeType.LIFE_REGEN, AttributeType.MANA_REGEN])
		{
			def baseRegen = Math.ceil(item.level / 4)
			primaryValue = 5 + (baseRegen + (2.0 * baseRegen - baseRegen) * new Random().nextDouble()).round()
			wasProcessed = true
		}

		if (attributeType in [AttributeType.LIFE_LEECH, AttributeType.MANA_LEECH])
		{
			primaryValue = (1 + (3 - 1) * new Random().nextDouble()).round(1)
			wasProcessed = true
		}

		if (attributeType in [AttributeType.FIND_QUANTITY, AttributeType.FIND_QUALITY, AttributeType.FIND_GOLD])
		{
			primaryValue = (10 + (25 - 10) * new Random().nextDouble()).round()
			wasProcessed = true
		}

		if (attributeType in [AttributeType.CHANCE_TO_STUN, AttributeType.CHANCE_TO_SAP, AttributeType.CHANCE_TO_CONFUSE,
							  AttributeType.CHANCE_TO_BURN, AttributeType.CHANCE_TO_CHILL, AttributeType.CHANCE_TO_SHOCK,
							  AttributeType.CHANCE_TO_DEVASTATE])
		{
			def minConditionChance = 2
			def maxConditionChance = 5

			if (item.itemType == ItemType.ACCESSORY && ((Accessory)item).isJewellery())
			{
				minConditionChance *= 2
				maxConditionChance *= 2
			}

			primaryValue = (minConditionChance + (maxConditionChance - minConditionChance) * new Random().nextDouble()).round()

			possibleAttributeTypes.removeAll([
				AttributeType.CHANCE_TO_STUN,
				AttributeType.CHANCE_TO_SAP,
				AttributeType.CHANCE_TO_CONFUSE,
				AttributeType.CHANCE_TO_BURN,
				AttributeType.CHANCE_TO_CHILL,
				AttributeType.CHANCE_TO_SHOCK,
				AttributeType.CHANCE_TO_DEVASTATE
			])
			wasProcessed = true
		}

		if (attributeType in [AttributeType.HEALTH_PERCENTAGE, AttributeType.MANA_PERCENTAGE])
		{
			primaryValue = (5 + (15 - 5) * new Random().nextDouble()).round()
			wasProcessed = true
		}

		if (attributeType == AttributeType.EFFECT_BLEED)
		{
			def baseBleed = item.level * 2
			primaryValue = (15 + (30 - 15) * new Random().nextDouble()).round()
			secondaryValue = 1 + (baseBleed + (3 * baseBleed - baseBleed) * new Random().nextDouble()).round()
			tertiaryValue = (3 + (6 - 3) * new Random().nextDouble()).round()
			wasProcessed = true
		}

		if (attributeType == AttributeType.EFFECT_CULL)
		{
			primaryValue = (8 + (12 - 8) * new Random().nextDouble()).round()
			wasProcessed = true
		}

		if (attributeType in [AttributeType.REDUCED_DAMAGE_MELEE, AttributeType.REDUCED_DAMAGE_RANGE])
		{
			primaryValue = (5 + (20 - 5) * new Random().nextDouble()).round()
			wasProcessed = true
		}

		if (wasProcessed)
		{
			if (possibleAttributeTypes.contains(attributeType))
			{
				possibleAttributeTypes.remove(attributeType)
			}

			return new Attribute([item: item, type: attributeType, primaryValue: primaryValue, secondaryValue: secondaryValue, tertiaryValue: tertiaryValue])
		}
		return null
	}

	public static double getPrimaryStatValue(Item item)
	{
		def minValue
		def maxValue

		def root = 1.3

		switch (item.itemType)
		{
			case ItemType.ACCESSORY:
				root = 1.2
				break;
			case ItemType.ARMOR:
				root = 1.4
				break;
			case ItemType.WEAPON:
				root = 1.3
				break;
		}
		maxValue = 10.0 + (double)item.level + Math.pow((double)item.level, 1.0 / root)
		minValue = maxValue / 2

		return Math.floor(minValue + (maxValue - minValue) * new Random().nextDouble())
	}
}
