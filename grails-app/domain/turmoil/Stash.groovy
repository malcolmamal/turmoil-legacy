package turmoil

class Stash {

	Account owner

	/*
	static hasOne = {
		owner: Account
	}
	*/

	String toString() {
		"Stash #${id}"
	}

	static constraints = {
		owner nullable: false
	}

	def getItems()
	{
		return Item.findAllByStash(this)
	}
}
