var emptyContent = "<div id='something-_ID_'>_CONTENT_</div>";
var tooltipContents = {};

function hideAllTooltips()
{
	jQuery('.ui-tooltip').hide();
}

function prepareTooltip(id, data)
{
	tooltipContents[id] = emptyContent.replace('_CONTENT_', data).replace('_ID_', id);
	jQuery('#something-' + id).html(data);
}

function isElementVisibleOrAlreadyGone(element)
{
	if (jQuery(element).length == 0)
	{
		// element is already gone so no need to do anything
		return true;
	}

	var topView = jQuery(window).scrollTop();
	var bottomView = topView + jQuery(window).height();
	var topElement = jQuery(element).offset().top;
	var bottomElement = topElement + jQuery(element).height();

	return ((bottomElement <= bottomView) && (topElement >= topView));
}

function reopenTooltipIfNotVisible(element, tooltipId)
{
	if (!isElementVisibleOrAlreadyGone(tooltipId))
	{
		element.tooltip().mouseout();
		setTimeout(function(){ element.tooltip().mouseover(); }, 10);
	}
}

function handleItemTooltipContent(element)
{
	var item = element.attr('item');

	var content = emptyContent.replace('_CONTENT_', '').replace('_ID_', item);
	if (tooltipContents[item])
	{
		content = tooltipContents[item]
	}
	else
	{
		// hide all the other existing tooltips
		hideAllTooltips();

		jQuery.ajax({
			type:'POST',
			url:'/turmoil/tooltip/showItem/' + item,
			success: function(data, textStatus) {
				if (textStatus == 'success')
				{
					prepareTooltip(item, data);

					// in case the tooltip will be partially outside the viewport, it has to be closed and opened again for jqueryui to reposition the tooltip
					setTimeout(function () {
						reopenTooltipIfNotVisible(element, '#something-' + item);
					}, 10);
				}
				else if (debug)
				{
					console.log('Tooltip Ajax error', textStatus, item, data);
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				jQuery('#error').html(XMLHttpRequest.responseText);

				if (debug)
				{
					console.log('Error in ajax call', errorThrown);
				}
			}
		});

		//content = tooltipContents[id];
	}
	return content;
}

$(function() {

	/*
	$(document).tooltip({
		items:'.tooltip',
		content: function () {
			return $(this).prop('title');
		},
	});
	*/

	$(document).tooltip({
		items:'.tooltip',
		hide: false,
		show: false,
		tooltipClass:'fancyTooltip',
		position: { my: "left+15 top", at: "right center" },
		content: function () {

			var content;
			if (jQuery(this).hasClass('itemTooltip'))
			{
				content = handleItemTooltipContent($(this));
			}
			else
			{
				content = jQuery(this).prop('title');
			}

			return content;
		},
		open: function(event, ui) {

			// closing current tooltip after 20 seconds
			setTimeout(function () {
				jQuery(ui.tooltip).hide();
			}, 20000 * 100);
		}

	});

	if (debug)
	{
		console.log('tooltip initialized...');
	}

});