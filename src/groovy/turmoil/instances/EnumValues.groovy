package turmoil.instances

import turmoil.enums.AccessoryType
import turmoil.enums.ArmorType
import turmoil.enums.AttributeType
import turmoil.enums.ItemRarity
import turmoil.enums.ItemType
import turmoil.enums.WeaponType

public class EnumValues
{
	ItemType[] itemTypes
	ArmorType[] armorTypes
	WeaponType[] weaponTypes
	AccessoryType[] accessoryTypes
	ItemRarity[] itemRarities
	AttributeType[] attributeTypes

	public void init()
	{
		itemTypes = ItemType.values()
		armorTypes = ArmorType.values()
		weaponTypes = WeaponType.values()
		accessoryTypes = AccessoryType.values()
		itemRarities = ItemRarity.values()
		attributeTypes = AttributeType.values()
	}

	public Enum getRandomItemType()
	{
		return getRandomValue(itemTypes)
	}

	public Enum getRandomWeaponType()
	{
		return getRandomValue(weaponTypes)
	}

	public Enum getRandomArmorType()
	{
		return getRandomValue(armorTypes)
	}

	public Enum getRandomAccessoryType()
	{
		return getRandomValue(accessoryTypes)
	}

	public Enum getRandomItemRarity()
	{
		return getRandomValue(itemRarities)
	}

	public AttributeType getRandomAttributeType()
	{
		return getRandomValue(attributeTypes)
	}

	public Enum getProperRandomItemRarity()
	{
		/*
		35% COMMON
		30% MAGIC
		20% RARE
		5% LEGENDARY
		5% SET
		3% UNIQUE
		2% EPIC
		*/

		switch (new Random().nextInt(100) + 1)
		{
			case 1..2:
				return ItemRarity.EPIC
			case 3..5:
				return ItemRarity.UNIQUE
			case 6..10:
				return ItemRarity.SET
			case 11..15:
				return ItemRarity.LEGENDARY
			case 16..35:
				return ItemRarity.RARE
			case 36..65:
				return ItemRarity.MAGIC
			default:
				return ItemRarity.COMMON
		}
	}

	@SuppressWarnings("rawtypes")
	public static Enum getRandomValue(Enum[] values)
	{
		return values[new Random().nextInt(values.length)]
	}

	public static Enum getRandomValue(ArrayList<Enum> values)
	{
		return values[new Random().nextInt(values.size())]
	}
}
