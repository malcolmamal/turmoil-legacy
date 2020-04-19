package turmoil

import org.springframework.web.servlet.ModelAndView

import turmoil.enums.ItemType

class TooltipController
{
	def index()
	{
		render "no default action, probably redirect in the future"
	}

	def showItem()
	{
		def item = Item.get((String)params.id)

		if (request.xhr)
		{
			// if from ajax

			if (item == null)
			{
				render "item not found!"
			}
			else
			{
				item.attributes.size()

				switch (item.getItemType())
				{
					case ItemType.ACCESSORY:
						return showAccessory()
					case ItemType.ARMOR:
						return showArmor()
					case ItemType.WEAPON:
						return showWeapon()
				}
			}
			return new ModelAndView("/tooltip/item", [item: item])
		}
	}

	def showAccessory()
	{
		def accessory = Accessory.get((String)params.id)
		if (request.xhr)
		{
			// if from ajax

			if (accessory == null)
			{
				render "accessory not found!"
				return null
			}
			return new ModelAndView("/tooltip/accessory", [accessory: accessory])
		}
	}

	def showArmor()
	{
		def armor = Armor.get((String)params.id)
		if (request.xhr)
		{
			// if from ajax

			if (armor == null)
			{
				render "armor not found!"
				return null
			}
			return new ModelAndView("/tooltip/armor", [armor: armor])
		}
	}

	def showWeapon()
	{
		def weapon = Weapon.get((String)params.id)
		if (request.xhr)
		{
			// if from ajax

			if (weapon == null)
			{
				render "weapon not found!"
				return null
			}
			return new ModelAndView("/tooltip/weapon", [weapon: weapon])
		}
	}
}
