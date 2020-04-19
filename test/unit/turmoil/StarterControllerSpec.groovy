package turmoil

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(StarterController)
class StarterControllerSpec extends Specification
{

    def setup()
	{
    }

    def cleanup()
	{
    }

    void "test something"()
	{
    }

	void "test hello"()
	{
		when: controller.checkMe()

		then: response.text == 'hello'
	}
}
