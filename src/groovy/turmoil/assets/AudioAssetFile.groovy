package turmoil.assets

import asset.pipeline.AbstractAssetFile

class AudioAssetFile extends AbstractAssetFile
{
	static final String contentType = 'audio/x-wav'
	static extensions = ['wav']
	static compiledExtension = 'wav'
	static processors = []

	String directiveForLine(String line)
	{
		//return null
		line.find(/\/\/=(.*)/) { fullMatch, directive -> return directive }
	}
}