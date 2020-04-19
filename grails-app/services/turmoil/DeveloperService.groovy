package turmoil

import grails.transaction.Transactional

@Transactional
class DeveloperService {

    def deleteAll() {

		def accessories = Accessory.getAll()
		accessories.each {
			it.delete()
		}

		def armors = Armor.getAll()
		armors.each {
			it.delete()
		}

		def weapons = Weapon.getAll()
		weapons.each {
			it.delete()
		}

		def stashes = Stash.getAll()
		stashes.each {
			it.delete()
		}

		def characterStates = CharacterState.getAll()
		characterStates.each {
			it.delete()
		}

		def characters = Character.getAll()
		characters.each {
			it.delete()
		}
    }
}
