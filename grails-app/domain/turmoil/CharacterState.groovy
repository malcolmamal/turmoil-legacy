package turmoil

class CharacterState
{
	Integer level
	Integer experience
	Integer requiredExperience

	double statStrength
	double statDexterity
	double statIntelligence
	double statVitality

	double health
	double mana

	double healthPercentage
	double manaPercentage

	double damageMin
	double damageMax

	double damageMinPhysical
	double damageMaxPhysical
	double damageMinFire
	double damageMaxFire
	double damageMinCold
	double damageMaxCold
	double damageMinLightning
	double damageMaxLightning
	double damageMinPoison
	double damageMaxPoison
	double damageMinArcane
	double damageMaxArcane

	double damagePercentage
	double damagePercentagePhysical
	double damagePercentageFire
	double damagePercentageCold
	double damagePercentageLightning
	double damagePercentagePoison
	double damagePercentageArcane

	double damageAvg

	double critChance
	double critDamage

	double armor

	double evasionDodge
	double evasionBlock
	double evasionParry

	double resistFire
	double resistCold
	double resistLightning
	double resistPoison
	double resistArcane
	double resistBleed
	double resistPiercing
	double resistAll

	double lifeHit
	double lifeLeech
	double lifeRegen

	double manaHit
	double manaLeech
	double manaRegen

	double findQuantity
	double findQuality
	double findGold

	double reducedDamageMelee
	double reducedDamageRanged

	double movement

	double chanceToStun
	double chanceToSap
	double chanceToConfuse
	double chanceToBurn
	double chanceToChill
	double chanceToShock
	double chanceToDevastate

	Character character

	HashMap<String, Item> items = new HashMap<String, Item>()

	static transients = ['items']

	static constraints = {
		character nullable: false
	}

	/*
	static belongsTo = {
		character: Character
	}
	*/

	def resetValues()
	{
		level = 0

		statStrength = 0
		statDexterity = 0
		statIntelligence = 0
		statVitality = 0

		health = 0
		mana = 0

		healthPercentage = 0
		manaPercentage = 0

		damageMin = 0
		damageMax = 0

		damageMinPhysical = 0
		damageMaxPhysical = 0
		damageMinFire = 0
		damageMaxFire = 0
		damageMinCold = 0
		damageMaxCold = 0
		damageMinLightning = 0
		damageMaxLightning = 0
		damageMinPoison = 0
		damageMaxPoison = 0
		damageMinArcane = 0
		damageMaxArcane = 0

		damagePercentage = 0
		damagePercentagePhysical = 0
		damagePercentageFire = 0
		damagePercentageCold = 0
		damagePercentageLightning = 0
		damagePercentagePoison = 0
		damagePercentageArcane = 0

		damageAvg = 0

		critChance = 0
		critDamage = 100

		armor = 0

		evasionDodge = 0
		evasionBlock = 0
		evasionParry = 0

		resistFire = 0
		resistCold = 0
		resistLightning = 0
		resistPoison = 0
		resistArcane = 0
		resistBleed = 0
		resistPiercing = 0
		resistAll = 0

		lifeHit = 0
		lifeLeech = 0
		lifeRegen = 0

		manaHit = 0
		manaLeech = 0
		manaRegen = 0

		findQuantity = 0
		findQuality = 0
		findGold = 0

		reducedDamageMelee = 0
		reducedDamageRanged = 0

		movement = 0

		chanceToStun = 0
		chanceToSap = 0
		chanceToConfuse = 0
		chanceToBurn = 0
		chanceToChill = 0
		chanceToShock = 0
		chanceToDevastate = 0
	}

	def computeAverageDamage()
	{
		damageMin = damageMinPhysical + damageMinFire + damageMinCold + damageMinLightning + damageMinPoison + damageMinArcane
		damageMax = damageMaxPhysical + damageMaxFire + damageMaxCold + damageMaxLightning + damageMaxPoison + damageMaxArcane

		def damageMinCrit = damageMin * critDamage / 100
		def damageMaxCrit = damageMax * critDamage / 100

		damageAvg = (((damageMin + damageMax)/2) * ((100 - critChance)/100.0) + ((damageMinCrit + damageMaxCrit)/2) * (critChance/100.0)).round(1)
	}

	def applyResistAll()
	{
		if (resistAll != 0)
		{
			resistFire += resistAll
			resistCold += resistAll
			resistLightning += resistAll
			resistPoison += resistAll
			resistArcane += resistAll
			resistBleed += resistAll
			resistPiercing += resistAll
		}
	}

	def applyPercentageDamage()
	{
		damagePercentage = 0
		damagePercentagePhysical = 0

		if (damagePercentage != 0)
		{
			damageMin = (damageMin + damageMin * damagePercentage / 100).round(1)
			damageMax = (damageMax + damageMax * damagePercentage / 100).round(1)
		}

		if (damagePercentagePhysical != 0)
		{
			damageMinPhysical = (damageMinPhysical + damageMinPhysical * damagePercentagePhysical / 100).round(1)
			damageMaxPhysical = (damageMaxPhysical + damageMaxPhysical * damagePercentagePhysical / 100).round(1)
		}

		if (damagePercentageFire != 0)
		{
			damageMinFire = (damageMinFire + damageMinFire * damagePercentageFire / 100).round(1)
			damageMaxFire = (damageMaxFire + damageMaxFire * damagePercentageFire / 100).round(1)
		}

		if (damagePercentageCold != 0)
		{
			damageMinCold = (damageMinCold + damageMinCold * damagePercentageCold / 100).round(1)
			damageMaxCold = (damageMaxCold + damageMaxCold * damagePercentageCold / 100).round(1)
		}

		if (damagePercentageLightning != 0)
		{
			damageMinLightning = (damageMinLightning + damageMinLightning * damagePercentageLightning / 100).round(1)
			damageMaxLightning = (damageMaxLightning + damageMaxLightning * damagePercentageLightning / 100).round(1)
		}

		if (damagePercentagePoison != 0)
		{
			damageMinPoison = (damageMinPoison + damageMinPoison * damagePercentagePoison / 100).round(1)
			damageMaxPoison = (damageMaxPoison + damageMaxPoison * damagePercentagePoison / 100).round(1)
		}

		if (damagePercentageArcane != 0)
		{
			damageMinArcane = (damageMinArcane + damageMinArcane * damagePercentageArcane / 100).round(1)
			damageMaxArcane = (damageMaxArcane + damageMaxArcane * damagePercentageArcane / 100).round(1)
		}
	}

	def updateHealthAndMana()
	{
		if (healthPercentage != 0)
		{
			health = (health + health * healthPercentage / 100).round(1)
		}

		if (manaPercentage != 0)
		{
			mana = (mana + mana * manaPercentage / 100).round(1)
		}
	}

	def capValues()
	{
		if (critChance > 75)
		{
			critChance = 75
		}

		if (evasionBlock > 80)
		{
			evasionBlock = 80
		}

		if (evasionDodge > 75)
		{
			evasionDodge = 75
		}

		if (evasionParry > 75)
		{
			evasionParry = 75
		}
	}
}
