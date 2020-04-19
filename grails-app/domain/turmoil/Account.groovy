package turmoil

class Account
{
	String username
	String password

	String toString() {
		"${username}"
	}

	static constraints = {
		username size: 3..24, blank: false, unique: true
		password size: 6..24, blank: false
	}
}
