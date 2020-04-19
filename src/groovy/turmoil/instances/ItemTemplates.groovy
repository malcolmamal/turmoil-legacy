package turmoil.instances

import turmoil.helpers.ItemTemplatesHelper

import turmoil.templates.AccessoryTemplate
import turmoil.templates.ArmorTemplate
import turmoil.templates.WeaponTemplate

public class ItemTemplates
{
	AccessoryTemplate[] accessoryCommonTemplates
	AccessoryTemplate[] accessoryLegendaryTemplates

	ArmorTemplate[] armorCommonTemplates
	ArmorTemplate[] armorLegendaryTemplates

	WeaponTemplate[] weaponCommonTemplates
	WeaponTemplate[] weaponLegendaryTemplates

	String[] itemPrefixes
	String[] itemSuffixes

	def initialize()
	{
		accessoryCommonTemplates = ItemTemplatesHelper.parseCommonAccessories()
		accessoryLegendaryTemplates = ItemTemplatesHelper.parseLegendaryAccessories()

		armorCommonTemplates = ItemTemplatesHelper.parseCommonArmors()
		armorLegendaryTemplates = ItemTemplatesHelper.parseLegendaryArmors()

		weaponCommonTemplates = ItemTemplatesHelper.parseCommonWeapons()
		weaponLegendaryTemplates = ItemTemplatesHelper.parseLegendaryWeapons()

		itemPrefixes = ItemTemplatesHelper.parseItemNames(true)
		itemSuffixes = ItemTemplatesHelper.parseItemNames(false)
	}
}


