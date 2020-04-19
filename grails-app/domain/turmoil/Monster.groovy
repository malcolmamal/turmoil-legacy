package turmoil

class Monster extends Person
{
	Weapon slotRightHand
	HashMap<String, Item> lootBag = new HashMap<String, Item>()

	String toString() {
		"${name}"
	}

	static transients = ['slotRightHand', 'lootBag']

	static constraints = {
	}
}
