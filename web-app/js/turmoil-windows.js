var windowSizes = {};

function initWindow(windowType, isScalable)
{
	var isVisible = false;
	var scale;
	var verticalPos;
	var horizontalPos;

	if (typeof(turmoil.windowSettings[windowType]) == 'undefined')
	{
		turmoil.windowSettings[windowType] = {};
	}
	else
	{
		verticalPos = turmoil.windowSettings[windowType].top;
		horizontalPos = turmoil.windowSettings[windowType].left;
		isVisible = turmoil.windowSettings[windowType].visible;
	}

	var windowResizer = jQuery("#window_" + windowType + "_resizer");
	windowResizer.draggable({
		handle: "#handle_" + windowType + "_container",
		containment: ".turmoilBody",
		stack: ".windowResizer",
		snap: ".windowResizer",
		snapMode: "outer",
		start: function() {
			hideAllTooltips();
		},
		stop: function() {
			hideAllTooltips();

			turmoil.windowSettings[windowType].left = windowResizer.css('left');
			turmoil.windowSettings[windowType].top = windowResizer.css('top');
			saveWindowsPositions();
		}
	});

	windowResizer.resizable({
		aspectRatio: true,
		helper: "ui-resizable-helper",
		start: function() {
			hideAllTooltips();
		},
		stop: function() {
			hideAllTooltips();

			var keyWidth = windowType + 'Width';
			if (isScalable && windowSizes[keyWidth] != 0)
			{
				var scale = jQuery("#window_" + windowType + "_resizer").width() / windowSizes[keyWidth];

				var windowWrapper = jQuery("#window_" + windowType + "_wrapper");
				windowWrapper.css('transform', 'scale(' + scale +')');
				windowWrapper.css('-webkit-transform', 'scale(' + scale +')');
				windowWrapper.css('-moz-transform', 'scale(' + scale +')');
				windowWrapper.css('-o-transform', 'scale(' + scale +')');

				// var positionFix = (jQuery("#window_" + windowType + "_resizer").width() - windowSizes[keyWidth]) / 2;
				// console.log('positionFix', positionFix);
				// jQuery("#window_" + windowType + "_wrapper").css('top', positionFix + 'px');

				// fixing the horizontal alignment
				fixHorizontalAlignment('window_' + windowType + '_resizer', 'window_' + windowType + '_wrapper');

				// TODO: save changes with ajax

				turmoil.windowSettings[windowType].scale = scale;
				saveWindowsPositions();
			}
		}
	});

	if (typeof(isVisible) != 'undefined' && isVisible)
	{
		if (typeof(verticalPos) == 'undefined' || typeof(horizontalPos) == 'undefined')
		{
			resizeToDefault(windowType, true)
		}
		else
		{
			windowResizer.css('left', turmoil.windowSettings[windowType].left);
			windowResizer.css('top', turmoil.windowSettings[windowType].top);
			actionShow(windowType);
		}
	}
	else
	{
		actionClose(windowType);
	}

	// TODO: check if it is not out of bounds
	windowResizer.css('top', verticalPos + 'px');
	windowResizer.css('left', horizontalPos + 'px');
}

// fixing the horizontal alignment
function fixHorizontalAlignment(parentId, childId)
{
	var parent = jQuery("#" + parentId);
	var child = jQuery("#" + childId);

	child.css('left', '0px');

	var properLeftPosition = parent.get(0).getBoundingClientRect().left;
	var wrongLeftPosition = child.get(0).getBoundingClientRect().left;
	var newPosition = Math.round(properLeftPosition - wrongLeftPosition);

	child.css('left', newPosition + 'px');
}

function resizeToDefault(windowType, setToCenter)
{
	// TODO: check if it is necessary (and possible) to move the window higher (so it would not go over the footer)

	actionMaximize(windowType, setToCenter);

	jQuery("#window_" + windowType + "_minimizer").show();

	var keyWidth = windowType + 'Width';
	var keyHeight = windowType + 'Height';
	var fullHeight = Math.round(windowSizes[keyHeight] + 40);

	var windowResizer = jQuery("#window_" + windowType + "_resizer");
	windowResizer.css('width', windowSizes[keyWidth] + 'px');
	windowResizer.css('height', fullHeight + 'px');

	var scale = 1;
	var windowWrapper = jQuery("#window_" + windowType + "_wrapper");
	windowWrapper.css('transform', 'scale(' + scale +')');
	windowWrapper.css('-webkit-transform', 'scale(' + scale +')');
	windowWrapper.css('-moz-transform', 'scale(' + scale +')');
	windowWrapper.css('-o-transform', 'scale(' + scale +')');

	turmoil.windowSettings[windowType].scale = scale;

	// fixing the horizontal alignment
	fixHorizontalAlignment('window_' + windowType + '_resizer', 'window_' + windowType + '_wrapper');
}

function actionClose(windowType)
{
	hideAllTooltips();

	jQuery('#window_' + windowType + '_resizer').hide();
	turmoil.windowSettings[windowType].visible = false;

	saveWindowsPositions();
}

function actionShow(windowType)
{
	hideAllTooltips();
	bringToTheTop(windowType);

	jQuery('#window_' + windowType + '_resizer').show();
	turmoil.windowSettings[windowType].visible = true;

	saveWindowsPositions();
}

function actionMaximize(windowType, setToCenter)
{
	// TODO: check if it is necessary (and possible) to move the window higher (so it would not go over the footer)

	actionShow(windowType);

	var windowContentWrapper = jQuery('#window_' + windowType + '_content_wrapper');
	var windowContainer = jQuery('#handle_' + windowType + '_container');

	windowContentWrapper.show();
	jQuery('#' + windowType + 'ButtonMaximize').hide();
	jQuery('#' + windowType + 'ButtonMinimize').show();

	var handleHeight = windowContainer.get(0).getBoundingClientRect().bottom - windowContainer.get(0).getBoundingClientRect().top;
	var contentHeight = windowContentWrapper.get(0).getBoundingClientRect().bottom - windowContentWrapper.get(0).getBoundingClientRect().top;
	var totalHeight = Math.round(handleHeight + contentHeight);

	var windowResizer = jQuery('#window_' + windowType + '_resizer');
	windowResizer.height(totalHeight);

	if (typeof(setToCenter) != 'undefined' && setToCenter == true)
	{
		centerContentVertically(windowResizer);
		centerContentHorizontally(windowResizer);

		turmoil.windowSettings[windowType].left = windowResizer.css('left');
		turmoil.windowSettings[windowType].top = windowResizer.css('top');
	}
}

function actionMinimize(windowType)
{
	hideAllTooltips();

	jQuery('#window_' + windowType + '_content_wrapper').hide();
	jQuery('#' + windowType + 'ButtonMaximize').show();
	jQuery('#' + windowType + 'ButtonMinimize').hide();

	var handleContainer = jQuery('#handle_' + windowType + '_container');
	var handleHeight = handleContainer.get(0).getBoundingClientRect().bottom - handleContainer.get(0).getBoundingClientRect().top;
	jQuery('#window_' + windowType + '_resizer').height(Math.round(handleHeight));
}

function switchShowClose(windowType, setToCenter)
{
	if (jQuery('#window_' + windowType + '_content_wrapper').is(":visible"))
	{
		actionClose(windowType);
	}
	else
	{
		if (typeof(turmoil.windowSettings[windowType]) == 'undefined'
			|| typeof(turmoil.windowSettings[windowType].left) == 'undefined'
			|| typeof(turmoil.windowSettings[windowType].top) == 'undefined')
		{
			resizeToDefault(windowType, setToCenter);
		}
		else
		{
			actionShow(windowType);
		}
	}
}

function switchMinimizeMaximize(windowType)
{
	if (jQuery('#window_' + windowType + '_content_wrapper').is(":visible"))
	{
		actionMinimize(windowType);
	}
	else
	{
		actionMaximize(windowType);
	}
}

function bringToTheTop(windowType)
{
	hideAllTooltips();

	var highestZIndexValue = 0;
	jQuery('.windowResizer').each(function(index) {
		if (jQuery(this).css('z-index') > highestZIndexValue)
		{
			highestZIndexValue = parseInt(jQuery(this).css('z-index'));
		}
	});

	jQuery('#window_' + windowType + '_resizer').css('z-index', ++highestZIndexValue);
}

function resetZIndex()
{
	jQuery('.windowResizer').each(function(index) {
		jQuery(this).css('z-index', 0);
	});
}

function saveWindowsPositions(forceSave)
{
	if (typeof(forceSave) == 'undefined')
	{
		forceSave = false;
	}

	window.turmoil.ajax.exec({
		url: 'account/saveWindowsSettings/?settings=' + JSON.stringify(turmoil.windowSettings) + '&save=' + forceSave
	});
}