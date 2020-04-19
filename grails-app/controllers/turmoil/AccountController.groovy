package turmoil

import grails.converters.JSON
import groovy.json.JsonSlurper
import org.jgrapht.Graphs
import org.jgrapht.alg.KShortestPaths
import org.springframework.web.servlet.ModelAndView


import turmoil.enums.Gender
import turmoil.helpers.CharacterStateHelper
import turmoil.helpers.CombatHelper
import turmoil.helpers.ExperienceHelper
import turmoil.helpers.InstanceHelper
import turmoil.helpers.LoginHelper
import turmoil.generators.ItemGenerator
import turmoil.helpers.ServerHelper
import turmoil.instances.CombatState

class CreateCharacterCommand
{
	String name
	Gender gender
	String portrait
	Boolean wasSent = false

	static constraints = {
		name(blank: false, minSize: 3)
		gender inList: [Gender.MALE, Gender.FEMALE]
		portrait(blank: false, minSize: 5)
	}
}

class AccountController extends BaseController
{
	public static int healthBarValue = 60

	def sessionFactory

	def characterService
	def itemService

	def index()
	{
		redirect(action: 'listCharacters')
	}

	def createCharacter(CreateCharacterCommand cmd)
	{
		return new ModelAndView("/account/createCharacter", [ cmd: cmd ])
	}

	def createCharacterForm(CreateCharacterCommand cmd)
	{
		if (cmd.hasErrors())
		{
			redirect(action: 'createCharacter', params: [ name: cmd.name, gender: cmd.gender, portrait: cmd.portrait, wasSent: true ])
			return null
		}

		characterService.addCharacter(LoginHelper.getLoggedAccount(), cmd.name, cmd.gender, cmd.portrait)

		redirect(action: 'listCharacters')
	}

	def deleteCharacter()
	{
		def loggedAccount = LoginHelper.getLoggedAccount()
		//characterService.deleteCharacter(character)
	}

	def listPortraits()
	{
		if (request.xhr)
		{
			def gender = Gender.getProperGender(params.gender)

			Integer max = 9
			Integer indexFrom = 0
			if (params.offset != null && Integer.parseInt((String)params.offset) > 0)
			{
				indexFrom = Integer.parseInt((String)params.offset)
			}

			def allPortraits = ServerHelper.getPortraits(gender)

			def isFirst = (indexFrom == 0)
			def isLast = false

			Integer indexTo = indexFrom + max
			if (indexTo > allPortraits.size())
			{
				indexTo = allPortraits.size()
				isLast = true
			}
			indexTo--

			def portraits = allPortraits[indexFrom..indexTo]

			return new ModelAndView("/account/portraits", [	portraits: portraits,
															portraitsQuantity: allPortraits.size(),
															isFirst: isFirst,
															isLast: isLast,
															params: [gender: gender.toString().toLowerCase(), offset: indexFrom, perPage: max]])
		}
	}

	def listCharacters()
	{
		def characters = characterService.listCharacters(LoginHelper.getLoggedAccount())

		def selectedCharacter = null
		if (session.character != null)
		{
			selectedCharacter = session.character.id
		}

		return new ModelAndView("/account/listCharacters", [characters: characters, selectedCharacter: selectedCharacter])
	}

	def selectCharacter()
	{
		if (request.xhr && params.id != null && LoginHelper.isLogged())
		{
			Character character = characterService.getCharacter((String)params.id)
			if (character != null && character.owner.id == LoginHelper.getLoggedAccount().id)
			{
				CharacterStateHelper.updateCharacterState(character, characterService.getCharacterItems(character))
				session.character = character
				session.windowsSettings = ServerHelper.getCharacterWindowsSettings(character)

				if (ServerHelper.getCombatState(character) == null)
				{
					ServerHelper.setCombatState(character, InstanceHelper.createCombatState(character))
				}

				def resultMap = [success: true, selectedCharacter: character.id]
				render resultMap as JSON
			}
		}
	}

	def rollItem()
	{
		if (session.character != null)
		{
			Character character = session.character
			def item = ItemGenerator.rollItem(character)

			if (item != null)
			{
				if (!request.xhr)
				{
					render "[${item.getClass()}]"
					render "[whatever ${item}]"
				}

				itemService.saveItem(item, character)

				if (request.xhr)
				{
					def resultMap = [success: true]
					resultMap << [stashedItemId: item.id]
					resultMap << [stashedItemContent: g.render(contextPath: "../character/", template: "item_slot_stash", model: [item: item])]
					render resultMap as JSON
				}
				else
				{
					render "[len: " + character.getPrimaryStash().getItems().size() + "]"
					for (localItem in character.getPrimaryStash().items)
					{
						//render "[id: " + localItem.id + "]"
					}
					render "[saved!]"

					render "[whatever ${item}]"
					render "[${item.getClass()}]"
				}
			}
		}
		else
		{
			render "no character!"
		}
	}

	def showEquipment()
	{
		if (session.character == null)
		{
			redirect(action: 'listCharacters')
			return null
		}

		Character character = session.character
		return new ModelAndView("/character/windows", [	character: session.character,
														characterState: CharacterStateHelper.getCharacterState(character),
														combatState: InstanceHelper.getCombatState(character)])
	}

	def updateStatisticsWindow()
	{
		if (request.xhr && session.character != null)
		{
			Character character = session.character
			def characterState = CharacterStateHelper.getCharacterState(character)
			return new ModelAndView("/character/_character_state", [characterState: characterState])
		}
	}

	def instanceMove()
	{
		def resultMap = [:]
		if (request.xhr && params.id != null && session.character != null)
		{
			Character character = session.character
			CombatState cs = InstanceHelper.getCombatState(character)
			cs.friend.instancePosition = params.id

			resultMap << [success: true]
			resultMap << [friendlyTurn: true]
			resultMap << [actionType: 'move']
			resultMap << [polygonId: cs.friend.instancePosition]
		}
		render resultMap as JSON
	}

	def instanceAttack()
	{
		def resultMap = [:]
		if (request.xhr && params.id != null && session.character != null)
		{
			Character character = session.character
			CombatState cs = InstanceHelper.getCombatState(character)
			def computedAttack = CombatHelper.computeDamageToDeal(character)
			def damageDealt = (int)computedAttack['damageToDeal']
			cs.enemy.currentHealth -= damageDealt

			if (cs.enemy.currentHealth < 0)
			{
				Item item = cs.enemy.lootBag.get("loot")
				if (item != null)
				{
					itemService.saveItem(item, character)

					resultMap << [stashedItemId: item.id]
					resultMap << [stashedItemContent: g.render(contextPath: "../character/", template: "item_slot_stash", model: [item: item])]
				}

				cs.enemy = InstanceHelper.createMonster(character)
				resultMap << [newEnemyPosition: cs.enemy.instancePosition]

				//TODO: handle it properly
				character.experience += 10
				if (character.experience >= ExperienceHelper.getRequiredExperience(character.level+1))
				{
					character.level++
					character.experience = ExperienceHelper.getRequiredExperience(character.level) - character.experience
				}
				CharacterStateHelper.computeValuesForCharacterState(character)
			}

			resultMap << [success: true]
			resultMap << [friendlyTurn: true]
			resultMap << [actionType: 'attack']
			if (computedAttack['isCriticalHit'])
			{
				resultMap << [type: 'critical']
			}
			resultMap << [polygonId: params.id]
			resultMap << [damageDealt: damageDealt]
			resultMap << [healthBar: Math.floor(cs.enemy.currentHealth * healthBarValue / cs.enemy.health)]
		}
		render resultMap as JSON
	}

	def instanceActionEnemy()
	{
		//TODO: investigate issue: The end vertex is the same as the start vertex!.
		def resultMap = [:]
		if (request.xhr && params.id != null && session.character != null)
		{
			resultMap << [success: true]

			Character character = session.character
			CombatState cs = InstanceHelper.getCombatState(character)

			def graph = InstanceHelper.getInstanceGraph()
			def pathing = new KShortestPaths(graph, cs.enemy.instancePosition[8..-1], 1)
			def path = pathing.getPaths(cs.friend.instancePosition[8..-1])
			def newPosition = Graphs.getPathVertexList(path.first()).getAt(1)
			def newPolygon = "polygon-${newPosition}"

			resultMap << [polygonId: newPolygon]
			if (path.first().getEdgeList().size() == 1)
			{
				cs.friend.currentHealth -= 5

				resultMap << [actionType: 'attack']
				resultMap << [attackingUnit: params.id]
				resultMap << [damageDealt: 5]
				resultMap << [healthBar: Math.floor(cs.friend.currentHealth * healthBarValue / cs.friend.health)]
			}
			else
			{
				cs.enemy.instancePosition = newPolygon

				resultMap << [actionType: 'move']
				resultMap << [unitToMove: params.id]
				resultMap << [path: path]
			}
		}
		render resultMap as JSON
	}

	def cleanStash()
	{
		if (session.character == null)
		{
			redirect(action: 'listCharacters')
			return null
		}

		def stash = session.character.getPrimaryStash()
		def items = stash.getItems()
		def i = 0
		for (item in items)
		{
			i++
			render item

			if (i > 20)
			{
				item.delete()
			}
		}
		sessionFactory.currentSession.flush()
	}

	def equip()
	{
		if (request.xhr && params.id != null && LoginHelper.isLogged() && session.character != null)
		{
			Character character = session.character
			def item = Item.get((String)params.id)
			if (item != null)
			{
				def resultMap = characterService.equipItem(character, item)
				render resultMap as JSON
			}
		}
	}

	def unequip()
	{
		if (request.xhr && params.id != null && LoginHelper.isLogged() && session.character != null)
		{
			Character character = session.character
			def item = Item.get((String)params.id)
			if (item != null)
			{
				def resultMap = characterService.unequipItem(character, item)
				render resultMap as JSON
			}
		}
	}

	def saveWindowsSettings()
	{
		if (request.xhr && session.character != null && params.settings != null)
		{
			try
			{
				new JsonSlurper().parseText((String)params.settings)
				session.windowsSettings = params.settings
			}
			catch(groovy.json.JsonException ex)
			{
				session.windowsSettings = "{}"
			}
			ServerHelper.setCharacterWindowsSettings((Character)session.character, (String)session.windowsSettings)
		}
		render ""
	}
}

