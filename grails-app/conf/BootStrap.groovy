import turmoil.CharacterState
import turmoil.enums.Gender
import turmoil.helpers.ResourceHelper
import turmoil.helpers.ServerHelper
import turmoil.instances.CombatState
import turmoil.instances.EnumValues
import turmoil.instances.ItemTemplates

class BootStrap
{
	def init = { servletContext ->
		servletContext.itemTemplates = new ItemTemplates()
		servletContext.itemTemplates.initialize()
		log.info "Item Templates initialized"

		servletContext.enumValues = new EnumValues()
		servletContext.enumValues.init()
		log.info "Enum Values initialized"

		servletContext.characterStates = new HashMap<String, CharacterState>()
		log.info "Empty characterStates initialized"

		servletContext.combatStates = new HashMap<String, CombatState>()
		log.info "Empty combatStates initialized"

		servletContext.portraitsMale = ResourceHelper.getPortraits(Gender.MALE)
		servletContext.portraitsFemale = ResourceHelper.getPortraits(Gender.FEMALE)
		log.info "Male (${servletContext.portraitsMale.size()}) and female (${servletContext.portraitsFemale.size()}) portraits loaded"

		servletContext.windowsSettings = new HashMap<String, String>()
		log.info "Empty windowsSettings initialized"

		servletContext.experienceChart = ServerHelper.getExperiencePerLevelChart()
		log.info "Experience chart initialized (${servletContext.experienceChart.size()})"
	}

	def destroy = {
	}
}
