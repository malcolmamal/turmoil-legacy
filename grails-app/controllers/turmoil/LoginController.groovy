package turmoil

import org.springframework.web.servlet.ModelAndView
import turmoil.helpers.LoggerHelper
import turmoil.helpers.ModelAndViewHelper

class LoginCommand
{
	String username
	String password

	static constraints = {
		username(blank: false, minSize: 3)
		password(blank: false, minSize: 6)
	}
}

class LoginController
{
	def index(LoginCommand cmd)
	{
		if (servletContext.loggedAccount == null)
		{
			return new ModelAndView("/login/authenticate", [ cmd: cmd ])
		}
		else
		{
			redirect(controller: 'starter', action: 'index')
		}
	}

	def authenticate(LoginCommand cmd)
	{
		if (cmd.hasErrors())
		{
			redirect(action: 'index', params: [username: cmd.username])
			return
		}

		def account = Account.findByUsername(cmd.username)
		if (account != null)
		{
			log.info "cmd pass is ${cmd.password} while account pass is ${account.password}"
			if (account.password == cmd.password)
			{
				servletContext.loggedAccount = account
				redirect(controller: 'starter', action: 'index')
			}
			else
			{
				redirect(action: 'index', params: [username: cmd.username])
			}
		}
		else
		{
			redirect(action: 'index', params: [username: cmd.username])
		}
	}

	def logout()
	{
		servletContext.loggedAccount = null
		session.invalidate()
		redirect(action: 'index')
	}

	def addTestUser()
	{
		def account = Account.findByUsername("fox")
		if (account == null)
		{
			account = new Account()
			account.username = 'fox'
			account.password = 'trustno1'
			account.save()

			LoggerHelper.getInstance().log("user ${account.username} added")
		}
		else
		{
			LoggerHelper.getInstance().log("user already exists")
		}
		return ModelAndViewHelper.getInfo()
	}
}
