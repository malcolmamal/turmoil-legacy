package turmoil.enums

public enum Gender {

	MALE,
	FEMALE,
	UNKNOWN

	Gender() {}

	public static Gender[] allowedForCreation()
	{
		return [MALE, FEMALE]
	}

	public static Gender getProperGender(def gender)
	{
		if (gender != null && gender.toLowerCase() == Gender.FEMALE.toString().toLowerCase())
		{
			return Gender.FEMALE
		}
		return Gender.MALE
	}
}