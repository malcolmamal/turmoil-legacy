var debug = true;

window.turmoil = {};
window.turmoil.soundLoops = {};
window.turmoil.windowSettings = {};

window.turmoil.lastLogDate = null;
window.turmoil.log = function(content, target)
{
	if (typeof(target) == 'undefined')
	{
		target = 'all';
	}

	var consoleTarget = jQuery('#console-' + target);
	if (consoleTarget.length > 0)
	{
		var currentDate;
		if (typeof(moment) == 'function')
		{
			currentDate = moment().format("YYYY-MM-DD HH:mm:ss.SSS");
		}
		else
		{
			currentDate = getCurrentDateTime();
		}
		currentDate = '[' + currentDate + '] ';

		var currentDateObject = new Date();
		if (window.turmoil.lastLogDate != null)
		{
			var difference = currentDateObject.getTime() - window.turmoil.lastLogDate.getTime();
			currentDate += ' (' + difference + 'ms) ';
		}

		consoleTarget.find('.mCSB_container').prepend(currentDate + content + '<br>');

		if (target != 'all')
		{
			jQuery('#console-all').find('.mCSB_container').prepend(currentDate + content + '<br>');
		}

		window.turmoil.lastLogDate = currentDateObject;
	}
	else
	{
		console.log('[' + target + ']', content);
	}
};

window.turmoil.logDebug = function(content, arguments)
{
	var caller = '';
	if (typeof(arguments) == 'object')
	{
		if (typeof(arguments.callee) == 'function' && typeof(arguments.callee.name) == 'string') {
			caller = arguments.callee.name + '() - ';
		}
	}
	window.turmoil.log(caller + content, 'all');
	console.log('[debug]', caller + content);
};

window.turmoil.logCombat = function(content)
{
	window.turmoil.log(content, 'combat');
};

/**
 * window.turmoil.ajax.exec({
 *		url: 'controller/action/id',
 *		onSuccess: someFunction
 * });
 *
 * @type {{baseUrl: string, exec: exec}}
 */
window.turmoil.ajax = {

	debugInfo: '',
	baseUrl: '/turmoil/',
	exec: function()
	{
		if (arguments.length == 1)
		{
			var params = arguments[0];
			if (typeof(params.url) !== 'undefined')
			{
				var dataString = null;
				if (typeof(params.args) !== 'undefined')
				{
					jQuery.each(params.args, function(name, value) {
						dataString += "&arg[" + name + "]=" + value;
					});
				}

				showSpinner();
				jQuery.ajax({
					type: "POST",
					url: this.baseUrl + params.url,
					data: dataString,
					//dataType:"script",
					success: function(data, textStatus) {
						if (textStatus == 'success')
						{
							if (typeof(params.eval) !== 'undefined' && params.eval == true) {
								eval(data);
							}

							if (typeof(params.onSuccess) !== 'undefined') {
								params.onSuccess(data);
							}
						}
						else if (debug)
						{
							console.log('Ajax error', textStatus, params.url, data);
						}
						hideSpinner();
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						handleAjaxError(XMLHttpRequest.responseText, errorThrown);
					}
				});
			}
			else
			{
				if (debug)
				{
					console.log('Missing url param for ajax call');
				}
			}
		}
		else
		{
			if (debug)
			{
				console.log('Missing arguments for ajax call');
			}
		}
	}
};

function handleAjaxError(responseText, errorThrown)
{
	jQuery('#error').html(responseText);
	if (debug)
	{
		console.log('Error in ajax call', errorThrown);
		turmoil.ajax.debugInfo = responseText;
	}

	hideSpinner();
}

function randomInt(max)
{
	return Math.floor((Math.random() * max) + 1);
}

function getCurrentDateTime()
{
	var currentDate = new Date();
	return currentDate.toJSON().slice(0,10) + ' ' + currentDate.getHours() + ':' + currentDate.getMinutes() + ':' + currentDate.getSeconds() + '.' + currentDate.getMilliseconds();
}

function playAudio(audio)
{
	var sound = document.getElementById(audio);
	sound.load();
	sound.play();
}

function playAudioLoop(audio, suffix)
{
	var ident = audio + '_' + suffix;
	var sound = document.getElementById(audio);
	turmoil.soundLoops[ident] = sound;
	turmoil.soundLoops[ident + '_loop'] = true;

	sound.load();
	sound.addEventListener('ended', function() {
		if (turmoil.soundLoops[ident + '_loop'])
		{
			this.currentTime = 0;
			this.play();
		}
	}, false);
	sound.play();
}

function stopAudioLoop(audio, suffix)
{
	var ident = audio + '_' + suffix;
	if (typeof(turmoil.soundLoops[ident]) != 'undefined')
	{
		var sound = turmoil.soundLoops[ident];
		sound.pause();
		turmoil.soundLoops[ident + '_loop'] = false;
	}
}

function isVerticalScrollPresent()
{
	// currently disabled
	return false;
	//return (document.documentElement.scrollHeight !== document.documentElement.clientHeight);
}

function setProperContentHeight()
{
	var turmoilBody = jQuery('#turmoilBody');
	var turmoilFooter = jQuery('#turmoilFooter');
	var newFooterPosition;
	if (isVerticalScrollPresent())
	{
		// making sure the footer is always at the bottom
		// however there is a problem, if somehow the scroll appears and the footer is moved to the bottom, there is no way to undo the changes currently
		// perhaps going through all the windows and checking bottom positions and comparing to the visible content height would yield some results (changing height if possible)
		// alternatively the scroll could just be disabled (not sure if the mouse scrolling would be still possible then or if that could be disabled as well)
		newFooterPosition = Math.round(document.documentElement.scrollHeight - turmoilFooter.height());
	}
	else
	{
		newFooterPosition = Math.round(jQuery(window).height() - turmoilFooter.height());
	}
	turmoilFooter.css('top', newFooterPosition + 'px');

	var headerPosition = jQuery('#turmoilHeader').get(0).getBoundingClientRect().bottom;
	var footerPosition = turmoilFooter.get(0).getBoundingClientRect().top;
	var contentHeight = Math.round(footerPosition - headerPosition - 2);

	turmoilBody.css('height', contentHeight + 'px');

	var tallContentContainer = jQuery('.tallContentContainer');
	if (tallContentContainer.length)
	{
		var tallContainerHeight = turmoilBody.height() - 25;
		tallContentContainer.height(tallContainerHeight);
	}
}

function addEvent(element, type, eventHandle)
{
	if (element == null || typeof(element) == 'undefined')
	{
		return;
	}

	if (element.addEventListener)
	{
		element.addEventListener(type, eventHandle, false);
	}
	else if (element.attachEvent)
	{
		element.attachEvent("on" + type, eventHandle);
	}
	else
	{
		element["on" + type] = eventHandle;
	}
}

function resizeEvent()
{
	setLayout();
}

function setCenteredContent()
{
	centerContentVertically(jQuery('#centeredContentWrapper'));
}

function centerContentVertically(centeredContentWrapper)
{
	if (centeredContentWrapper.length)
	{
		var parentOffset = 0;
		if (centeredContentWrapper.parent().length)
		{
			parentOffset = centeredContentWrapper.parent().get(0).getBoundingClientRect().top;
		}
		var halfOfContentHeight = Math.round((centeredContentWrapper.get(0).getBoundingClientRect().bottom - centeredContentWrapper.get(0).getBoundingClientRect().top) / 2);
		var halfOfWindowHeight = Math.round(jQuery(window).height() / 2);

		var topPosition = halfOfWindowHeight - halfOfContentHeight - parentOffset;
		if (topPosition < 0)
		{
			topPosition = 0;
		}

		centeredContentWrapper.css('top', topPosition + 'px')
	}
}

function centerContentHorizontally(centeredContentWrapper)
{
	if (centeredContentWrapper.length)
	{
		var parentOffset = 0;
		if (centeredContentWrapper.parent().length)
		{
			parentOffset = centeredContentWrapper.parent().get(0).getBoundingClientRect().left;
		}
		var halfOfContentWidth = Math.round((centeredContentWrapper.get(0).getBoundingClientRect().right - centeredContentWrapper.get(0).getBoundingClientRect().left) / 2);
		var halfOfWindowWidth = Math.round(jQuery(window).width() / 2);

		var leftPosition = halfOfWindowWidth - halfOfContentWidth - parentOffset;
		if (leftPosition < 0)
		{
			leftPosition = 0;
		}

		centeredContentWrapper.css('left', leftPosition + 'px')
	}
}

function setLayout()
{
	setProperContentHeight();
	setCenteredContent();
}

function showSpinner()
{
	jQuery('#spinner').show();
}

function hideSpinner()
{
	jQuery('#spinner').hide();
}

function hideSpinnerWithDelay()
{
	setTimeout(function(){hideSpinner();}, 100);
}

function showAjaxError()
{
	var windowId = window.open('', 'ajaxError', 'height=900, width=1600');
	windowId.document.write(turmoil.ajax.debugInfo);
	windowId.focus();

	hideSpinnerWithDelay();
}

$(function() {

	setLayout();

	addEvent(window, "resize", resizeEvent);

	var scrollableContainer = jQuery('.scrollableContainer');
	if (scrollableContainer.length)
	{
		if (jQuery.isFunction(jQuery().mCustomScrollbar))
		{
			scrollableContainer.mCustomScrollbar();
		}
		else if (debug)
		{
			console.log('scrollableContainer found, but custom-scrollbar module is not active...')
		}
	}

	jQuery.each(jQuery('.flatMenu').find('li'), function(index, value) {jQuery(value).click(function(){showSpinner();});})

	// TODO: handle browser window resize
});