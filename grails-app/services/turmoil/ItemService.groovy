package turmoil

import grails.transaction.Transactional

@Transactional
class ItemService
{
	def saveItem(Item item, Character character)
	{
		item.owner = character.owner
		item.putToStash(character.getPrimaryStash())
		item.save()
	}
}
