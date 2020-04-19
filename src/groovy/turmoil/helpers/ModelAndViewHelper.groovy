package turmoil.helpers

import org.springframework.web.servlet.ModelAndView

public class ModelAndViewHelper
{
	public static getInfo()
	{
		return new ModelAndView("/starter/info", [ log: LoggerHelper.getInstance().toString() ])
	}
}
