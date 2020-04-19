package turmoil

class LanguageTagLib
{
	static namespace = "language"

	def selector = {attrs ->

		def values
		try
		{
			values = new ArrayList(attrs.langs.split(",").toList())
		}
		catch (Exception e)
		{
			values = new ArrayList("en", "pl")
		}

		def flags = new ArrayList()
		values.each {
			flags << it.toString().trim()
		}

		def selected = session["org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE"]
		selected = selected ? selected.toString() : Locale.getDefault().toString().substring(0, 2)

		String url = attrs.url
		if (url == null)
		{
			url = request.forwardURI + '?'
			def query = request.getQueryString()? request.getQueryString().replace('lang=' + selected, '') : ''
			if (query != '' && !query.endsWith('&'))
			{
				query += '&'
			}
			url += query + 'lang='
		}
		else
		{
			if (url.indexOf('?') > 0)
			{
				url += '&lang='
			}
			else
			{
				url += '?lang='
			}
		}

		out << render(template:'/common/language_selector', model:[flags: flags, selected: selected, uri: url])
	}
}
