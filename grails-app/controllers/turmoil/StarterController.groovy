package turmoil

import org.springframework.web.servlet.ModelAndView

import grails.converters.JSON
import turmoil.helpers.LoggerHelper
import turmoil.helpers.ModelAndViewHelper
import turmoil.helpers.ServerHelper
import turmoil.instances.EnumValues

class StarterController
{
	EnumValues enumValues

	def developerService

	def checkMe()
	{
		render "hello"
	}

	def deleteAll()
	{
		developerService.deleteAll()

		render "finished"
	}


	def index()
	{
		if (session.val == null)
		{
			session.val = 15
		}

		if (servletContext.value == null)
		{
			servletContext.value = 10
		}

		// test for enum values
		enumValues = ServerHelper.getEnumValues()
		log.info enumValues.itemTypes.toString()
		//log.info enumValues.getRandomWeaponType()

		/*
		def testValue = "[some test value]"
		log.info "Logger test for INFO: $testValue"
		log.error "Logger test for ERROR ${testValue}"
		LoggerHelper.getInstance().log("Logger Helper initialized, ${testValue}")
		*/

		def person = new Monster()//.get(1)
		person.name = "troll"
		person.dateCreated = new Date()
		person.lastUpdated = new Date()
		person.level = 35
		person.save()

		LoggerHelper.getInstance().log("Hello my World! ${person.name} and the value is: ${session.val} and ${servletContext.value} and ${params}")

		return ModelAndViewHelper.getInfo()
	}

	def test()
	{
		render(view: 'test')
	}

	def checkWeapons()
	{
		def itemTemplates = ServerHelper.getItemTemplates()
		render itemTemplates.accessoryTemplates
		render itemTemplates.armorTemplates
		render itemTemplates.weaponTemplates
	}

	def letUsSee()
	{
		def people = Person.findAllByName("malcolm")
		return new ModelAndView("/show/people", [ peopleToSee: people ])
	}

	def bye()
	{
		// previously: render "Time to die"
		LoggerHelper.getInstance().log("Time to die")
		return ModelAndViewHelper.getInfo()
	}

	def increase()
	{
		session.val++
		servletContext.value++
		LoggerHelper.getInstance().log("Increased to ${session.val} and ${servletContext.value}")
		return ModelAndViewHelper.getInfo()
	}

	def decrease()
	{
		session.val--
		servletContext.value--
		LoggerHelper.getInstance().log("Decreased to ${session.val} and ${servletContext.value}")
		return ModelAndViewHelper.getInfo()
	}

	def cleanLog()
	{
		LoggerHelper.getInstance().clean()
		redirect(action: 'index')
	}

	def showEquipment()
	{
		return new ModelAndView("/character/static_equipment")
	}

	def checkConfig()
	{
		render grailsApplication.config
		return ModelAndViewHelper.getInfo()
	}

	def ajaxTest()
	{
		return new ModelAndView("/starter/ajax", [messageCode: "SHARD_OF_HATE"])
	}

	def showWeapon()
	{
		def weapon = Weapon.get(params.id)

		if (request.xhr)
		{
			// if from ajax

			if (weapon == null)
			{
				render "weapon not found!"
			}
			else
			{
				render weapon as JSON
			}
		}
		else
		{
			// or from normal request

			return new ModelAndView("/starter/ajax", [weapon: weapon])
		}
	}
}
