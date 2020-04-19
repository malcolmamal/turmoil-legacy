package turmoil.helpers

import groovy.util.logging.Log4j
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import turmoil.CharacterState
import turmoil.Character
import turmoil.enums.Gender
import turmoil.instances.CombatState
import turmoil.instances.EnumValues
import turmoil.instances.ItemTemplates

@Log4j
public class ServerHelper
{
	public static Integer maxLevel = 1000

	public static EnumValues getEnumValues()
	{
		return ServletContextHolder.getServletContext().enumValues
	}

	public static ItemTemplates getItemTemplates()
	{
		return ServletContextHolder.getServletContext().itemTemplates
	}

	public static CharacterState getCharacterState(Character character)
	{
		if (getCharacterStates().containsKey((String)character.id))
		{
			return getCharacterStates().get((String)character.id)
		}
		return null
	}

	public static setCharacterState(Character character, CharacterState characterState)
	{
		getCharacterStates().put((String)character.id, characterState)
	}

	private static HashMap<String, CharacterState> getCharacterStates()
	{
		return ServletContextHolder.getServletContext().characterStates
	}

	public static CombatState getCombatState(Character character)
	{
		if (getCombatStates().containsKey((String)character.id))
		{
			return getCombatStates().get((String)character.id)
		}
		return null
	}

	public static setCombatState(Character character, CombatState combatState)
	{
		getCombatStates().put((String)character.id, combatState)
	}

	private static HashMap<String, CombatState> getCombatStates()
	{
		return ServletContextHolder.getServletContext().combatStates
	}

	public static String[] getPortraits(Gender gender)
	{
		if (gender == Gender.FEMALE)
		{
			return ServletContextHolder.getServletContext().portraitsFemale
		}
		return ServletContextHolder.getServletContext().portraitsMale
	}

	private static HashMap<String, String> getWindowsSettings()
	{
		return ServletContextHolder.getServletContext().windowsSettings
	}

	public static String getCharacterWindowsSettings(Character character)
	{
		if (getWindowsSettings().containsKey((String)character.id))
		{
			return getWindowsSettings().get((String)character.id)
		}
		return null
	}

	public static setCharacterWindowsSettings(Character character, String windowsSettings)
	{
		getWindowsSettings().put((String)character.id, windowsSettings)
	}

	public static HashMap<Integer, Integer> getExperiencePerLevelChart()
	{
		if (ServletContextHolder.getServletContext().experienceChart == null)
		{
			int experience = 100
			ServletContextHolder.getServletContext().experienceChart = new HashMap<Integer, Integer>()
			for (i in 1..(maxLevel-1))
			{
				experience = Math.round(Math.floor((experience + 300 * Math.log10(i)) / 10) * 10)
				if (experience > 10000)
				{
					experience = Math.round(Math.floor((experience / 100)) * 100)
				}
				ServletContextHolder.getServletContext().experienceChart.put(i+1, experience)
			}
		}
		return ServletContextHolder.getServletContext().experienceChart
	}
}
