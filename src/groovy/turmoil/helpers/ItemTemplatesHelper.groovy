package turmoil.helpers

import grails.util.Holders
import groovy.util.logging.Log4j
import turmoil.templates.AccessoryTemplate
import turmoil.templates.ArmorTemplate
import turmoil.templates.WeaponTemplate

@Log4j
class ItemTemplatesHelper
{
	public static AccessoryTemplate[] parseCommonAccessories()
	{
		def accessoryTemplates = []
		def configObject = new ConfigSlurper().parse(ItemTemplatesHelper.getContext().getResource("properties/items-accessories.groovy").getURL())

		configObject.accessories.each() {
			def values = it.getValue()

			def template = new AccessoryTemplate()
			template.itemCode = it.getKey()
			template.accessoryType = values.accessory_type

			accessoryTemplates.add(template)
		}

		log.info "Common Accessory Templates loaded (" + accessoryTemplates.size() + ")"
		return accessoryTemplates
	}

	public static AccessoryTemplate[] parseLegendaryAccessories()
	{
		def accessoryTemplates = []
		def configObject = new ConfigSlurper().parse(ItemTemplatesHelper.getContext().getResource("properties/items-accessories.groovy").getURL())

		configObject.legendaries.each() {
			def values = it.getValue()

			def template = new AccessoryTemplate()
			template.itemCode = it.getKey()
			template.accessoryType = values.accessory_type
			template.isLegendary = true

			accessoryTemplates.add(template)
		}

		log.info "Legendary Accessory Templates loaded (" + accessoryTemplates.size() + ")"
		return accessoryTemplates
	}

	public static ArmorTemplate[] parseCommonArmors()
	{
		def armorTemplates = []
		def configObject = new ConfigSlurper().parse(ItemTemplatesHelper.getContext().getResource("properties/items-armors.groovy").getURL())

		configObject.armors.each() {
			def values = it.getValue()

			def template = new ArmorTemplate()
			template.itemCode = it.getKey()
			template.armorType = values.armor_type

			armorTemplates.add(template)
		}

		log.info "Common Armor Templates loaded (" + armorTemplates.size() + ")"
		return armorTemplates
	}

	public static ArmorTemplate[] parseLegendaryArmors()
	{
		def armorTemplates = []
		def configObject = new ConfigSlurper().parse(ItemTemplatesHelper.getContext().getResource("properties/items-armors.groovy").getURL())

		configObject.legendaries.each() {
			def values = it.getValue()

			def template = new ArmorTemplate()
			template.itemCode = it.getKey()
			template.armorValue = values.armor_value
			template.armorType = values.armor_type
			template.isLegendary = true

			armorTemplates.add(template)
		}

		log.info "Legendary Armor Templates loaded (" + armorTemplates.size() + ")"
		return armorTemplates
	}

	public static WeaponTemplate[] parseCommonWeapons()
	{
		def weaponTemplates = []
		def configObject = new ConfigSlurper().parse(ItemTemplatesHelper.getContext().getResource("properties/items-weapons.groovy").getURL())

		configObject.weapons.each() {
			def values = it.getValue()

			def template = new WeaponTemplate()
			template.itemCode = it.getKey()
			template.weaponType = values.weapon_type

			weaponTemplates.add(template)
		}

		log.info "Common Weapon Templates loaded (" + weaponTemplates.size() + ")"
		return weaponTemplates
	}

	public static WeaponTemplate[] parseLegendaryWeapons()
	{
		def weaponTemplates = []
		def configObject = new ConfigSlurper().parse(ItemTemplatesHelper.getContext().getResource("properties/items-weapons.groovy").getURL())

		configObject.legendaries.each() {
			def values = it.getValue()

			def template = new WeaponTemplate()
			template.itemCode = it.getKey()
			template.minDamage = values.min_damage
			template.maxDamage = values.max_damage
			template.damageType = values.damage_type
			template.weaponType = values.weapon_type
			template.isLegendary = true

			weaponTemplates.add(template)
		}

		log.info "Legendary Weapon Templates loaded (" + weaponTemplates.size() + ")"
		return weaponTemplates
	}

	public static String[] parseItemNames(Boolean isPrefix)
	{
		def tokens = []
		def fileNameType = (isPrefix) ? "prefix" : "suffix"
		def file = ItemTemplatesHelper.getContext().getResource("properties/names-" + fileNameType + ".groovy").inputStream

		file.eachLine("utf8") {
			tokens << it.trim()
		}

		log.info "Name tokens '" + fileNameType + "' loaded (" + tokens.size() + ")"

		return tokens
	}

	def static getContext()
	{
		return Holders.getGrailsApplication().mainContext
	}
}
