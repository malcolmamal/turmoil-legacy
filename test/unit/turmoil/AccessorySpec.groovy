package turmoil

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Accessory)
class AccessorySpec extends Specification
{

	def setup()
	{
	}

	def cleanup()
	{
	}

	void "test something"()
	{
		given:
			def value
		when:
			value = true
		then:
			value == true
			println "was ok"
			assert true
	}

	void testIsSquareLayout()
	{
		assert true
	}
}
