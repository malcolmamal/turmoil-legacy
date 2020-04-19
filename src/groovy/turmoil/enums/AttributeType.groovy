package turmoil.enums

public enum AttributeType {

	// TODO: add toughness (critical defence)

	PRIMARY_STRENGTH,
	PRIMARY_DEXTERITY,
	PRIMARY_INTELLIGENCE,
	PRIMARY_VITALITY,

	PRIMARY_STRENGTH_AND_VITALITY,
	PRIMARY_DEXTERITY_AND_VITALITY,
	PRIMARY_INTELLIGENCE_AND_VITALITY,

	CRITICAL_CHANCE,
	CRITICAL_DAMAGE,

	// ACCURACY,

	DAMAGE_PHYSICAL_VALUE,
	DAMAGE_PHYSICAL_PERCENTAGE,

	DAMAGE_FIRE_VALUE,
	DAMAGE_FIRE_PERCENTAGE,

	DAMAGE_COLD_VALUE,
	DAMAGE_COLD_PERCENTAGE,

	DAMAGE_LIGHTNING_VALUE,
	DAMAGE_LIGHTNING_PERCENTAGE,

	DAMAGE_POISON_VALUE,
	DAMAGE_POISON_PERCENTAGE,

	DAMAGE_ARCANE_VALUE,
	DAMAGE_ARCANE_PERCENTAGE,

	DAMAGE_VALUE,
	DAMAGE_PERCENTAGE,

	ARMOR,

	RESIST_FIRE,
	RESIST_COLD,
	RESIST_LIGHTNING,
	RESIST_POISON,
	RESIST_ARCANE,
	RESIST_BLEED,
	RESIST_PIERCING,

	RESIST_ALL, // most likely on legendaries

	EVASION_BLOCK,
	EVASION_DODGE,
	EVASION_PARRY,

	HEALTH_PERCENTAGE,
	MANA_PERCENTAGE,

	LIFE_HIT,
	LIFE_LEECH,
	LIFE_REGEN,

	MANA_HIT,
	MANA_LEECH,
	MANA_REGEN,

	FIND_QUANTITY,
	FIND_QUALITY,
	FIND_GOLD,

	INDESTRUCTIBLE,

	REDUCED_DAMAGE_MELEE,
	REDUCED_DAMAGE_RANGE,

	/*
	REDUCED_CONTROL_DURATION,
	REDUCED_COOLDOWN,
	REDUCED_MANA_COST,
	*/

	CHANCE_TO_STUN, // object cannot do anything
	CHANCE_TO_SAP, // object cannot do anything but effect ends right away if object is attacked
	CHANCE_TO_CONFUSE, // object will pick random target on any side
	CHANCE_TO_BURN, // applies dot
	CHANCE_TO_CHILL, // reduced object's damage due to slowness
	CHANCE_TO_SHOCK, // object is more vulnerable to damage
	CHANCE_TO_DEVASTATE, // x times the critical damage applied

	EFFECT_CULL,
	EFFECT_BLEED, // to stack or not to stack?

	/*
	REFLECT_DAMAGE,
	*/

	MOVEMENT // boots only

	private Boolean isPrimarySingleStat = false
	private Boolean isPrimaryDoubleStat = false
	private Boolean isResist = false
	private Boolean isEvasion = false

	static {
		PRIMARY_STRENGTH.isPrimarySingleStat = true
		PRIMARY_DEXTERITY.isPrimarySingleStat = true
		PRIMARY_INTELLIGENCE.isPrimarySingleStat = true
		PRIMARY_VITALITY.isPrimarySingleStat = true

		PRIMARY_STRENGTH_AND_VITALITY.isPrimaryDoubleStat = true
		PRIMARY_DEXTERITY_AND_VITALITY.isPrimaryDoubleStat = true
		PRIMARY_INTELLIGENCE_AND_VITALITY.isPrimaryDoubleStat = true

		RESIST_FIRE.isResist = true
		RESIST_COLD.isResist = true
		RESIST_LIGHTNING.isResist = true
		RESIST_POISON.isResist = true
		RESIST_ARCANE.isResist = true
		RESIST_BLEED.isResist = true
		RESIST_PIERCING.isResist = true
		RESIST_ALL.isResist = true

		EVASION_BLOCK.isEvasion = true
		EVASION_DODGE.isEvasion = true
		EVASION_PARRY.isEvasion = true
	}

	public Boolean isPrimaryStat()
	{
		return isPrimarySingleStat || isPrimaryDoubleStat
	}

	public Boolean isPrimarySingleStat()
	{
		return isPrimaryDoubleStat
	}

	public Boolean isPrimaryDoubleStat()
	{
		return isPrimaryDoubleStat
	}

	public Boolean isResist()
	{
		return isResist
	}

	public Boolean isEvasion()
	{
		return isEvasion
	}
}


