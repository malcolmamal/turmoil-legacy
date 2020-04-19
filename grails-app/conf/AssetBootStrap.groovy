import turmoil.assets.AudioAssetFile
import asset.pipeline.AssetHelper

class AssetBootStrap
{
	def init = {
		// does not seem to be needed actually and messes up the audio files if it's turned on
		//AssetHelper.assetSpecs << AudioAssetFile
	}

	def destroy = {
	}
}
