package turmoil.helpers

import spock.lang.Specification

/**
 * Created by fox on 2014-07-20.
 */
class ItemTemplatesHelperTest extends Specification
{
	def setup()
	{
	}

	def cleanup()
	{
	}

	void testParseAccessories()
	{

	}

	void testParseArmors()
	{

	}

	void testParseWeapons()
	{
		assert true
	}

	void testParseForTest()
	{

	}

	void testGetContext()
	{

	}

	void "test something else"()
	{

		//ItemTemplatesHelper.metaClass.getContext = { -> return grailsApplication.mainContext }

		//mockLogging(ItemTemplatesHelper, true)
		def result = true
		//def output = ItemTemplatesHelper.parseForTest()
		//def output2 = ItemTemplatesHelper.getContext()

		//assertEquals(true, false)
		//assertEquals(ItemTemplatesHelper.getContext(), "asd")

		log.info "Starting tests"

		ItemTemplatesHelper.log.error "yeah"

		assert result
	}

	void "test templates"()
	{
		when:
		def result = 'from testing!!'

		then:
		result == 'from testing!!'
	}

	void "test templates with context"()
	{
		when:
			def commonWeaponTemplates = ItemTemplatesHelper.parseCommonWeapons()
			def legendaryWeaponTemplates = ItemTemplatesHelper.parseLegendaryWeapons()
		then:
			assert commonWeaponTemplates.size() > 0
			assert legendaryWeaponTemplates.size() > 0

			println "sizes: ${commonWeaponTemplates.size()} and ${legendaryWeaponTemplates.size()}"
	}
}
