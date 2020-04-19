package turmoil.templates

import turmoil.enums.DamageType
import turmoil.enums.WeaponType

class WeaponTemplate extends ItemTemplate
{
	Integer minDamage
	Integer maxDamage

	DamageType damageType
	WeaponType weaponType

	public String toString()
	{
		return	"[ " 			+ super.toString()	+ ", " +
				"minDamage: "	+ minDamage			+ ", " +
				"maxDamage: "	+ maxDamage			+ ", " +
				"damageType: "	+ damageType		+ ", " +
				"weaponType: "	+ weaponType		+
				" ]"
	}

	public Map toMap()
	{
		return super.toMap() + [
			minDamage: minDamage,
			maxDamage: maxDamage,
			damageType: damageType,
			weaponType: weaponType
		]
	}
}
