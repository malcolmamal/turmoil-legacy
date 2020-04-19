package turmoil

class LogTagLib
{
	static defaultEncodeAs = [taglib:'html']
	//static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

	def logMsg = { attrs, body ->
		def logLevel = (attrs['level'] ?: 'debug').toLowerCase()
		log."$logLevel"(body())
	}
}
