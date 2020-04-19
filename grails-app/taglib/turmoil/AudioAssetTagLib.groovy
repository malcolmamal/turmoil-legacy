package turmoil

import grails.util.Environment

class AudioAssetTagLib
{
	static namespace = "asset"
	static returnObjectForTags = ['assetPath']
	//static defaultEncodeAs = [taglib:'html']
	//static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

	def grailsApplication
	def assetProcessorService

	/**
	 * @attr src REQUIRED
	 * @attr id REQUIRED
	 */
	def audio = { attrs ->
		def src = attrs.remove('src')
		def id = attrs.remove('id')
		out << "<audio id=\"${id}\" src=\"${assetPath(src:src)}\" preload=\"auto\"></audio>"
	}
}
