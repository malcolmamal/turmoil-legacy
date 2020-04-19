package turmoil.helpers

public class LoggerHelper
{
	StringBuilder builder
	
	private static LoggerHelper instance = null
	
	protected LoggerHelper()
	{
		// Exists only to defeat instantiation.
	}
	
	public static LoggerHelper getInstance()
	{
		if (instance == null)
		{
			instance = new LoggerHelper()
			instance.builder = new StringBuilder()
		}
		return instance
	}
	
	public log(Object object)
	{
		builder.append(object).append("\n")
	}
	
	public String toString()
	{
		return builder.toString()
	}
	
	public void clean()
	{
		builder.setLength(0)
	}
}
