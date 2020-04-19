package turmoil.helpers

import java.security.SecureRandom

/**
 * Created by fox on 2014-07-20.
 */
class EnumHelper
{
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz)
	{
		int x = new SecureRandom().nextInt(clazz.getEnumConstants().length)
		return clazz.getEnumConstants()[x]
	}
}
