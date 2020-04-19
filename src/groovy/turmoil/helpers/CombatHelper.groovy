package turmoil.helpers

import turmoil.Character
import turmoil.CharacterState

class CombatHelper
{
	def static computeDamageToDeal(Character character)
	{
		def damageToDeal
		def resultMap = [:]

		CharacterState characterState = ServerHelper.getCharacterState(character)

		def isCriticalHit = false
		if (characterState.critChance > 0 && (100 * new Random().nextDouble()).round(1) <= characterState.critChance)
		{
			isCriticalHit = true
			def damageMinCrit = characterState.damageMin * characterState.critDamage / 100
			def damageMaxCrit = characterState.damageMax * characterState.critDamage / 100

			damageToDeal = (damageMinCrit + (damageMaxCrit - damageMinCrit) * new Random().nextDouble()).round()
		}
		else
		{
			damageToDeal = (characterState.damageMin + (characterState.damageMax - characterState.damageMin) * new Random().nextDouble()).round()
		}

		resultMap << [damageToDeal: damageToDeal]
		resultMap << [isCriticalHit: isCriticalHit]

		return resultMap
	}
}
