package turmoil

import turmoil.helpers.LoginHelper

class BaseController
{
	def beforeInterceptor = [action: this.&auth]

	protected Boolean checkLogin()
	{
		if (!LoginHelper.isLogged())
		{
			redirect(controller: 'login', action: 'index')
			return false
		}
		return true
	}

	def auth()
	{
		return checkLogin()
	}
}
