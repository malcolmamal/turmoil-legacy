package turmoil.generators

import groovy.util.logging.Log4j
import turmoil.Accessory
import turmoil.Armor
import turmoil.Attribute
import turmoil.Character
import turmoil.Item
import turmoil.Monster
import turmoil.Weapon
import turmoil.enums.DamageType
import turmoil.enums.ItemRarity
import turmoil.enums.ItemType
import turmoil.helpers.EnumHelper
import turmoil.helpers.ServerHelper
import turmoil.templates.AccessoryTemplate
import turmoil.templates.ArmorTemplate
import turmoil.templates.ItemTemplate
import turmoil.templates.WeaponTemplate

@Log4j
public class ItemGenerator
{
	public static Item rollItemOfRarityAndType(Character character, ItemRarity rarity, ItemType itemType)
	{
		Item item = generateItem(rarity, itemType)
		if (item != null)
		{
			item.level = character.level
			if (rarity.isPlain())
			{
				generateName(item)
			}

			addAttributes(item)
			rollStats(item)
		}
		return item
	}

	public static Item rollItem(Character character)
	{
		ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
		ItemType itemType = ServerHelper.getEnumValues().getRandomItemType()

		return rollItemOfRarityAndType(character, rarity, itemType)
	}

	public static Item rollMonsterWeapon(Monster monster)
	{
		ItemRarity rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
		if (!rarity.isPlain())
		{
			// monsters should have lower chance of getting a high quality weapon
			rarity = ServerHelper.getEnumValues().getProperRandomItemRarity()
		}

		Item item = generateItem(rarity, ItemType.WEAPON)
		if (item != null)
		{
			item.level = monster.level
			if (rarity.isPlain())
			{
				generateName(item)
			}

			addAttributes(item)
			rollStats(item)
		}
		return item
	}

	public static generateName(Item item)
	{
		def randomPrefix = new Random().nextInt(ServerHelper.getItemTemplates().itemPrefixes.size())
		def randomSuffix = new Random().nextInt(ServerHelper.getItemTemplates().itemSuffixes.size())
		item.itemName = ServerHelper.getItemTemplates().itemPrefixes[randomPrefix] + " " + ServerHelper.getItemTemplates().itemSuffixes[randomSuffix]
	}

	public static Item generateItem(ItemRarity rarity, ItemType itemType)
	{
		Item item
		switch (itemType)
		{
			case ItemType.WEAPON:
				item = generateWeapon(rarity)
				break
			case ItemType.ARMOR:
				item = generateArmor(rarity)
				break
			case ItemType.ACCESSORY:
				item = generateAccessory(rarity)
				break
		}
		return item
	}

	public static Accessory generateAccessory(ItemRarity rarity)
	{
		AccessoryTemplate template
		if (rarity.isPlain())
		{
			template = getRandomTemplate(ServerHelper.getItemTemplates().accessoryCommonTemplates)
		}
		else
		{
			template = getRandomTemplate(ServerHelper.getItemTemplates().accessoryLegendaryTemplates)
		}

		return new Accessory(template.toMap() << [rarity: rarity])
	}

	public static Armor generateArmor(ItemRarity rarity)
	{
		ArmorTemplate template
		if (rarity.isPlain())
		{
			template = getRandomTemplate(ServerHelper.getItemTemplates().armorCommonTemplates)
		}
		else
		{
			template = getRandomTemplate(ServerHelper.getItemTemplates().armorLegendaryTemplates)
		}
		return new Armor(template.toMap() << [rarity: rarity])
	}

	public static Weapon generateWeapon(ItemRarity rarity)
	{
		WeaponTemplate template
		if (rarity.isPlain())
		{
			template = getRandomTemplate(ServerHelper.getItemTemplates().weaponCommonTemplates)
		}
		else
		{
			template = getRandomTemplate(ServerHelper.getItemTemplates().weaponLegendaryTemplates)
		}
		return new Weapon(template.toMap() << [rarity: rarity])
	}

	public static ItemTemplate getRandomTemplate(ItemTemplate[] values)
	{
		return values[new Random().nextInt(values.length)]
	}

	public static Item addAttributes(Item item)
	{
		item.attributes = []

		ArrayList<Attribute> attributes = ItemAttributeGenerator.rollAttributes(item)
		item.attributes.addAll(attributes)

		return item
	}

	public static Item rollStats(Item item)
	{
		switch (item.itemType)
		{
			case ItemType.ACCESSORY:
				rollAccessoryStats(item)
				break
			case ItemType.ARMOR:
				rollArmorStats(item)
				break
			case ItemType.WEAPON:
				rollWeaponStats(item)
				break
		}
		return item
	}

	public static rollAccessoryStats(Accessory accessory)
	{
		if (accessory.level == null)
		{
			accessory.level = 1
		}

		//todo
	}

	public static rollArmorStats(Armor armor)
	{
		def rarityMultiplier = getRarityMultiplier(armor.rarity)

		if (armor.level == null)
		{
			armor.level = 1
		}

		armor.armorValue = (1 + new Random().nextInt(20) + armor.level * (1 + new Random().nextInt(5))) * rarityMultiplier
	}

	public static rollWeaponStats(Weapon weapon)
	{
		double handMultiplier = weapon.isOneHanded() ? 1 : 1.5
		double rarityMultiplier = getRarityMultiplier(weapon.rarity)

		if (weapon.level == null)
		{
			weapon.level = 1
		}

		weapon.minDamage = (1 + (weapon.level * (1 + new Random().nextInt(5))) * handMultiplier) * rarityMultiplier
		weapon.maxDamage = (2 + (weapon.level * (6 + new Random().nextInt(5))) * handMultiplier) * rarityMultiplier

		if (weapon.rarity.isPlain())
		{
			weapon.damageType = EnumHelper.randomEnum(DamageType.class)
		}
	}

	def static getRarityMultiplier(ItemRarity rarity)
	{
		def rarityMultiplier = 1
		switch (rarity)
		{
			case ItemRarity.COMMON:
				rarityMultiplier = 1.0
				break
			case ItemRarity.MAGIC:
				rarityMultiplier = 1.1
				break
			case ItemRarity.RARE:
				rarityMultiplier = 1.2
				break
			case ItemRarity.SET:
			case ItemRarity.LEGENDARY:
				rarityMultiplier = 1.5
				break
			case ItemRarity.UNIQUE:
				rarityMultiplier = 1.6
				break
			case ItemRarity.EPIC:
				rarityMultiplier = 1.7
		}
		return rarityMultiplier
	}
}

