package turmoil

import turmoil.enums.Gender

abstract class Person
{
	String name
	Integer level = 1
	Date dateCreated
	Date lastUpdated

	Integer strength = 1
	Integer intelligence = 1
	Integer dexterity = 1
	Integer vitality = 1

	Float health = 100
	Integer currentHealth = 100

	Gender gender = Gender.UNKNOWN

	String toString() {
		"${name}"
	}

	String instancePosition

		static transients = ['instancePosition', 'currentHealth']

	static mapping = {
		autoTimestamp false
		tablePerHierarchy false
	}

	static constraints = {
	}

	def beforeInsert()
	{
		log.info "date before insert is ${dateCreated}"
		if (dateCreated == null)
		{
			dateCreated = new Date()
		}
		lastUpdated = new Date()
	}

	def beforeUpdate()
	{
		lastUpdated = new Date()
	}
}
