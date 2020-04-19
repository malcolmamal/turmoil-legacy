package turmoil.helpers

import groovy.util.logging.Log4j
import turmoil.Accessory
import turmoil.Armor
import turmoil.Attribute
import turmoil.Character
import turmoil.CharacterState
import turmoil.Item
import turmoil.Weapon
import turmoil.enums.AttributeType
import turmoil.enums.DamageType
import turmoil.enums.ItemSlot
import turmoil.enums.ItemType

@Log4j
class CharacterStateHelper
{
	public static CharacterState getCharacterState(Character character)
	{
		CharacterState characterState = ServerHelper.getCharacterState(character)
		if (characterState == null)
		{
			characterState = new CharacterState()
			characterState.character = character
		}
		return characterState
	}

	public static setCharacterState(Character character, CharacterState characterState)
	{
		ServerHelper.setCharacterState(character, characterState)
	}

	public static computeValuesForCharacterState(Character character)
	{
		computeValuesForCharacterState(getCharacterState(character), character)
	}

	public static computeValuesForCharacterState(CharacterState characterState, Character character)
	{
		characterState.level = character.level

		characterState.experience = character.experience
		characterState.requiredExperience = ExperienceHelper.getRequiredExperience(character.level + 1)

		characterState.applyResistAll()
		characterState.applyPercentageDamage()
		characterState.updateHealthAndMana()
		characterState.capValues()
		characterState.computeAverageDamage()
	}

	/**
	 * Used during character selection action
	 * Prepares the character state and fills it with items and computes values based on them
	 *
	 * @param character
	 * @param items
	 * @return
	 */
	public static updateCharacterState(Character character, ArrayList<Item> items)
	{
		CharacterState characterState = getCharacterState(character)
		characterState.resetValues()

		items.each {
			updateCharacterStateWithItem(characterState, it, it.itemSlot)
		}

		computeValuesForCharacterState(characterState, character)
		setCharacterState(character, characterState)
	}

	/**
	 * Used when an item is changed for a character
	 *
	 * @param character
	 * @param item
	 * @param itemSlot
	 * @return
	 */
	public static updateCharacterStateForItem(Character character, Item item, ItemSlot itemSlot)
	{
		CharacterState characterState = ServerHelper.getCharacterState(character)
		characterState.items.put(itemSlot.toString(), item)
		characterState.resetValues()

		characterState.items.each {
			if (it.getValue() != null)
			{
				updateCharacterStateWithItem(characterState, it.getValue(), it.getValue().itemSlot)
			}
		}

		computeValuesForCharacterState(characterState, character)
		setCharacterState(character, characterState)
	}

	public static updateCharacterStateWithItem(CharacterState characterState, Item item, ItemSlot itemSlot)
	{
		characterState.items.put(itemSlot.toString(), item)
		switch (item.itemType)
		{
			case ItemType.ACCESSORY:
				updateCharacterStateWithAccessory(characterState, item)
				break
			case ItemType.ARMOR:
				updateCharacterStateWithArmor(characterState, item)
				break
			case ItemType.WEAPON:
				updateCharacterStateWithWeapon(characterState, item)
				break
		}

		if (item.attributes != null && item.attributes.size() > 0)
		{
			item.attributes.each {
				updateCharacterStateWithAttribute(characterState, it)
			}
		}
	}

	public static updateCharacterStateWithAccessory(CharacterState characterState, Accessory accessory)
	{

	}

	public static updateCharacterStateWithArmor(CharacterState characterState, Armor armor)
	{
		characterState.armor += armor.armorValue
	}

	public static updateCharacterStateWithWeapon(CharacterState characterState, Weapon weapon)
	{
		switch (weapon.damageType)
		{
			case DamageType.PHYSICAL:
				characterState.damageMinPhysical += weapon.minDamage
				characterState.damageMaxPhysical += weapon.maxDamage
				break
			case DamageType.FIRE:
				characterState.damageMinFire += weapon.minDamage
				characterState.damageMaxFire += weapon.maxDamage
				break
			case DamageType.COLD:
				characterState.damageMinCold += weapon.minDamage
				characterState.damageMaxCold += weapon.maxDamage
				break
			case DamageType.LIGHTNING:
				characterState.damageMinLightning += weapon.minDamage
				characterState.damageMaxLightning += weapon.maxDamage
				break
			case DamageType.POISON:
				characterState.damageMinPoison += weapon.minDamage
				characterState.damageMaxPoison += weapon.maxDamage
				break
			case DamageType.ARCANE:
				characterState.damageMinArcane += weapon.minDamage
				characterState.damageMaxArcane += weapon.maxDamage
				break
		}
	}

	public static updateCharacterStateWithAttribute(CharacterState characterState, Attribute attribute)
	{
		switch (attribute.type)
		{
			case AttributeType.PRIMARY_STRENGTH:
				characterState.statStrength += attribute.primaryValue
				break
			case AttributeType.PRIMARY_DEXTERITY:
				characterState.statDexterity += attribute.primaryValue
				break
			case AttributeType.PRIMARY_INTELLIGENCE:
				characterState.statIntelligence += attribute.primaryValue
				break
			case AttributeType.PRIMARY_VITALITY:
				characterState.statVitality += attribute.primaryValue
				break
			case AttributeType.PRIMARY_STRENGTH_AND_VITALITY:
				characterState.statStrength += attribute.primaryValue
				characterState.statVitality += attribute.secondaryValue
				break
			case AttributeType.PRIMARY_DEXTERITY_AND_VITALITY:
				characterState.statDexterity += attribute.primaryValue
				characterState.statVitality += attribute.secondaryValue
				break
			case AttributeType.PRIMARY_INTELLIGENCE_AND_VITALITY:
				characterState.statIntelligence += attribute.primaryValue
				characterState.statVitality += attribute.secondaryValue
				break
			case AttributeType.CRITICAL_CHANCE:
				characterState.critChance += attribute.primaryValue
				break
			case AttributeType.CRITICAL_DAMAGE:
				characterState.critDamage += attribute.primaryValue
				break
			case AttributeType.DAMAGE_VALUE:
				characterState.damageMin += attribute.primaryValue
				characterState.damageMax += attribute.secondaryValue
				break
			case AttributeType.DAMAGE_PHYSICAL_VALUE:
				characterState.damageMinPhysical += attribute.primaryValue
				characterState.damageMaxPhysical += attribute.secondaryValue
				break
			case AttributeType.DAMAGE_FIRE_VALUE:
				characterState.damageMinFire += attribute.primaryValue
				characterState.damageMaxFire += attribute.secondaryValue
				break
			case AttributeType.DAMAGE_COLD_VALUE:
				characterState.damageMinCold += attribute.primaryValue
				characterState.damageMaxCold += attribute.secondaryValue
				break
			case AttributeType.DAMAGE_LIGHTNING_VALUE:
				characterState.damageMinLightning += attribute.primaryValue
				characterState.damageMaxLightning += attribute.secondaryValue
				break
			case AttributeType.DAMAGE_POISON_VALUE:
				characterState.damageMinPoison += attribute.primaryValue
				characterState.damageMaxPoison += attribute.secondaryValue
				break
			case AttributeType.DAMAGE_ARCANE_VALUE:
				characterState.damageMinArcane += attribute.primaryValue
				characterState.damageMaxArcane += attribute.secondaryValue
				break
			case AttributeType.DAMAGE_PERCENTAGE:
				characterState.damagePercentage += attribute.primaryValue
				break
			case AttributeType.DAMAGE_PHYSICAL_PERCENTAGE:
				characterState.damagePercentagePhysical += attribute.primaryValue
				break
			case AttributeType.DAMAGE_FIRE_PERCENTAGE:
				characterState.damagePercentageFire += attribute.primaryValue
				break
			case AttributeType.DAMAGE_COLD_PERCENTAGE:
				characterState.damagePercentageCold += attribute.primaryValue
				break
			case AttributeType.DAMAGE_LIGHTNING_PERCENTAGE:
				characterState.damagePercentageLightning += attribute.primaryValue
				break
			case AttributeType.DAMAGE_POISON_PERCENTAGE:
				characterState.damagePercentagePoison += attribute.primaryValue
				break
			case AttributeType.DAMAGE_ARCANE_PERCENTAGE:
				characterState.damagePercentageArcane += attribute.primaryValue
				break
			case AttributeType.RESIST_FIRE:
				characterState.resistFire += attribute.primaryValue
				break
			case AttributeType.RESIST_COLD:
				characterState.resistCold += attribute.primaryValue
				break
			case AttributeType.RESIST_LIGHTNING:
				characterState.resistLightning += attribute.primaryValue
				break
			case AttributeType.RESIST_POISON:
				characterState.resistPoison += attribute.primaryValue
				break
			case AttributeType.RESIST_ARCANE:
				characterState.resistArcane += attribute.primaryValue
				break
			case AttributeType.RESIST_BLEED:
				characterState.resistBleed += attribute.primaryValue
				break
			case AttributeType.RESIST_PIERCING:
				characterState.resistPiercing += attribute.primaryValue
				break
			case AttributeType.RESIST_ALL:
				characterState.resistAll += attribute.primaryValue
				break
			case AttributeType.ARMOR:
				characterState.armor += attribute.primaryValue
				break
			case AttributeType.EVASION_BLOCK:
				characterState.evasionBlock += attribute.primaryValue
				break
			case AttributeType.EVASION_DODGE:
				characterState.evasionDodge += attribute.primaryValue
				break
			case AttributeType.EVASION_PARRY:
				characterState.evasionParry += attribute.primaryValue
				break
			case AttributeType.HEALTH_PERCENTAGE:
				characterState.healthPercentage += attribute.primaryValue
				break
			case AttributeType.MANA_PERCENTAGE:
				characterState.manaPercentage += attribute.primaryValue
				break
			case AttributeType.LIFE_HIT:
				characterState.lifeHit += attribute.primaryValue
				break
			case AttributeType.LIFE_LEECH:
				characterState.lifeLeech += attribute.primaryValue
				break
			case AttributeType.LIFE_REGEN:
				characterState.lifeRegen += attribute.primaryValue
				break
			case AttributeType.MANA_HIT:
				characterState.manaHit += attribute.primaryValue
				break
			case AttributeType.MANA_LEECH:
				characterState.manaLeech += attribute.primaryValue
				break
			case AttributeType.MANA_REGEN:
				characterState.manaRegen += attribute.primaryValue
				break
			case AttributeType.FIND_QUANTITY:
				characterState.findQuantity += attribute.primaryValue
				break
			case AttributeType.FIND_QUALITY:
				characterState.findQuality += attribute.primaryValue
				break
			case AttributeType.FIND_GOLD:
				characterState.findGold += attribute.primaryValue
				break
			case AttributeType.REDUCED_DAMAGE_MELEE:
				characterState.reducedDamageMelee += attribute.primaryValue
				break
			case AttributeType.REDUCED_DAMAGE_RANGE:
				characterState.reducedDamageRanged += attribute.primaryValue
				break
			case AttributeType.CHANCE_TO_STUN:
				characterState.chanceToStun += attribute.primaryValue
				break
			case AttributeType.CHANCE_TO_SAP:
				characterState.chanceToSap += attribute.primaryValue
				break
			case AttributeType.CHANCE_TO_CONFUSE:
				characterState.chanceToConfuse += attribute.primaryValue
				break
			case AttributeType.CHANCE_TO_BURN:
				characterState.chanceToBurn += attribute.primaryValue
				break
			case AttributeType.CHANCE_TO_CHILL:
				characterState.chanceToChill += attribute.primaryValue
				break
			case AttributeType.CHANCE_TO_SHOCK:
				characterState.chanceToShock += attribute.primaryValue
				break
			case AttributeType.CHANCE_TO_DEVASTATE:
				characterState.chanceToDevastate += attribute.primaryValue
				break
			case AttributeType.MOVEMENT:
				characterState.movement += attribute.primaryValue
				break
		/*
			// ACCURACY,

			//REDUCED_CONTROL_DURATION,
			//REDUCED_COOLDOWN,
			//REDUCED_MANA_COST,

			//EFFECT_CULL,
			//EFFECT_BLEED, // to stack or not to stack?

			//REFLECT_DAMAGE,
		*/
		}
	}
}
