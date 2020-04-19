package turmoil.helpers

import org.codehaus.groovy.grails.web.context.ServletContextHolder

import turmoil.Account

public class LoginHelper
{
	public static boolean isLogged()
	{
		return (ServletContextHolder.getServletContext().loggedAccount != null)
	}
	
	public static Account getLoggedAccount()
	{
		if (LoginHelper.isLogged())
		{
			return ServletContextHolder.getServletContext().loggedAccount
		}
		return null
	}
}
