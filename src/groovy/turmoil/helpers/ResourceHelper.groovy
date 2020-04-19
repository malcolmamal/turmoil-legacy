package turmoil.helpers

import grails.util.Holders
import turmoil.enums.Gender

/**
 * Created by fox on 2014-07-26.
 */
class ResourceHelper
{
	public static String[] getPortraits(Gender gender)
	{
		def portraitsFolder = Holders.getGrailsApplication().mainContext.getResource('images/portraits/' + gender.toString().toLowerCase() + '/').getFile()
		def availablePortraits = []

		portraitsFolder.listFiles().each {
			availablePortraits << it.getName()
		}
		return availablePortraits
	}
}
