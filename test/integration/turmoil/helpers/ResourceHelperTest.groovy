package turmoil.helpers

import turmoil.enums.Gender

/**
 * Created by fox on 2014-07-26.
 */
class ResourceHelperTest extends GroovyTestCase
{
	void testGetPortraits()
	{
		when:
			def portraitsFemale = ResourceHelper.getPortraits(Gender.FEMALE)
			def portraitsMale = ResourceHelper.getPortraits(Gender.MALE)
		then:
			def quantityFemale = portraitsFemale.size()
			def quantityMale = portraitsMale.size()

			assert quantityFemale > 0
			assert quantityMale > 0

			println "Found ${quantityFemale} female portraits and ${quantityMale} male portraits"
	}
}
