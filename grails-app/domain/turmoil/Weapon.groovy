package turmoil

import groovy.util.logging.Log4j
import turmoil.enums.DamageType
import turmoil.enums.ItemType
import turmoil.enums.WeaponType

@Log4j
class Weapon extends Item
{
	ItemType itemType = ItemType.WEAPON
	WeaponType weaponType
	DamageType damageType

	Integer minDamage = 1
	Integer maxDamage = 2

	String toString() {
		"${itemName}, #${id}"
	}

	static constraints = {
	}

	def getAverageDamage()
	{
		Integer damage = 0;

		if (minDamage != null)
		{
			damage += minDamage
		}

		if (maxDamage != null)
		{
			damage += maxDamage
		}

		return damage / 2
	}

	def isOneHanded()
	{
		switch (weaponType)
		{
			case WeaponType.STAFF:
			case WeaponType.BOW:
			case WeaponType.CROSSBOW:
			case WeaponType.TWO_HANDED_AXE:
			case WeaponType.TWO_HANDED_MACE:
			case WeaponType.TWO_HANDED_SWORD:
			case WeaponType.POLEARM:
				return false
			default:
				return true
		}
	}

	def getImagePath()
	{
		return super.getImagePath() + "weapons" + (rarity.isPlain() ? "/" + getWeaponFileCode() : "")
	}

	def String getWeaponFileCode()
	{
		switch (weaponType)
		{
			case WeaponType.STAFF:
				return "staves"
			case WeaponType.BOW:
				return "bows"
			case WeaponType.CROSSBOW:
				return "crossbows"
			case WeaponType.ONE_HANDED_AXE:
			case WeaponType.TWO_HANDED_AXE:
				return "axes"
			case WeaponType.ONE_HANDED_MACE:
			case WeaponType.TWO_HANDED_MACE:
				return "maces"
			case WeaponType.ONE_HANDED_SWORD:
			case WeaponType.TWO_HANDED_SWORD:
				return "swords"
			case WeaponType.POLEARM:
				return "polearms"
			case WeaponType.WAND:
				return "wands"
			case WeaponType.DAGGER:
				return "daggers"
			case WeaponType.SPEAR:
				return "spears"
		}
	}

	public String toStringFull()
	{
		return	"[ " 			+ super.toStringFull()	+ ", " +
				"itemType: "	+ itemType				+ ", " +
				"weaponType: "	+ weaponType			+ ", " +
				"damageType: "	+ damageType			+ ", " +
				"minDamage: "	+ minDamage				+ ", " +
				"maxDamage: "	+ maxDamage				+ ", " +
				"avgDamage: "	+ getAverageDamage()	+
				" ]"
	}
}
