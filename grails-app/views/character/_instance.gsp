<g:set var="windowWidth" value="650" />
<g:set var="windowHeight" value="550" />

<r:script>
	windowSizes.${windowCode}Width = ${windowWidth};
	windowSizes.${windowCode}Height = ${windowHeight};

	function svgAddClass(element, className)
	{
		var newClasses = '';
		var hasClass = false;
		jQuery.each(element.attr('class').replace(/[\s]+/g, ' ').trim().split(' '), function( index, value ) {
			newClasses += ' ' + value;
			if (className == value)
			{
				hasClass = true;
			}
		});

		if (!hasClass)
		{
			newClasses += ' ' + className;
		}

		element.attr('class', jQuery.trim(newClasses));
	}

	function svgRemoveClass(element, className)
	{
		var newClasses = '';
		jQuery.each(element.attr('class').replace(/[\s]+/g, ' ').trim().split(' '), function( index, value ) {
			if (className != value)
			{
				newClasses += ' ' + value;
			}
		});
		element.attr('class', jQuery.trim(newClasses));
	}

	function svgHasClass(element, className)
	{
		var hasClass = false;
		jQuery.each(element.attr('class').replace(/[\s]+/g, ' ').trim().split(' '), function( index, value ) {
			if (className == value)
			{
				hasClass = true;
				return false;
			}
		});
		return hasClass;
	}

	function svgPrintClasses(element)
	{
		console.log(element.attr('class'));
	}

	function blink(element)
	{
		jQuery(element).fadeTo(1000, 0.4, function() {
			jQuery(this).fadeTo(750, 0.9, function() {
				blink(this);
			});
		});
	}

	var initialPolygonMove = true;
	var activeUnit = null;

	function getPolygonForUnit(unit)
	{
		return jQuery('#' + jQuery(unit).data('previousPolygonId'));
	}

	function actionOnUnit(unit)
	{
		jQuery(unit).hasClass('enemyUnit')
		{
			var polygon = jQuery(getPolygonForUnit(unit));
			var url = 'account/instanceAttack/' + polygon.attr('id');

			if (svgHasClass(polygon, 'instancePolygonEnemy'))
			{
				window.turmoil.ajax.exec({
					url: url,
					onSuccess: finalizeActionOnPolygon
				});
			}
		}
	}

	function actionOnPolygon(polygon, unit)
	{
		if (typeof(unit) == 'undefined')
		{
			if (typeof(activeUnit) == 'undefined')
			{
				turmoil.logDebug('there is no active unit', arguments);
			}
			unit = activeUnit;
		}

		var url = 'account/';
		if (unit.hasClass('enemyUnit'))
		{
			url += 'instanceActionEnemy/' + unit.attr('id');
		}
		else
		{
			if (typeof(polygon) == 'undefined' || polygon == null)
			{
				turmoil.logDebug('wrong polygon parameter', arguments);
				return;
			}

			if (svgHasClass(polygon, 'instancePolygon'))
			{
				url += 'instanceMove/' + polygon.attr('id');
			}
			else
			{
				turmoil.logDebug('not possible to move to polygon ' + polygon.attr('id'), arguments);
				return;
			}
		}

		window.turmoil.ajax.exec({
			url: url,
			onSuccess: finalizeActionOnPolygon
		});
	}

	function handleMoveToPolygon(polygon, unit)
	{
		turmoil.log('Unit ' + unit.attr('id') + ' moves to ' + polygon.attr('id'));

		if (unit.data('previousPolygonId') != null)
		{
			var previousPolygon = jQuery('#' + unit.data('previousPolygonId'));

			svgAddClass(previousPolygon, 'instancePolygon');
			if (unit.hasClass('enemyUnit'))
			{
				svgRemoveClass(previousPolygon, 'instancePolygonEnemy');
			}
			else
			{
				svgRemoveClass(previousPolygon, 'instancePolygonActive');
			}
			previousPolygon.data('unit', '');
		}

		var offsetContainer = jQuery('#instanceContainer').offset();

		var offset = polygon.offset();
		var width = polygon.width();
		var height = polygon.height() ;

		var centerX = offset.left + width / 2 - offsetContainer.left + 17;
		var centerY = offset.top + height / 2 - offsetContainer.top + 17;

		unit.stop().animate({
				left: centerX,
				top: centerY
			},
			250,
			function() {
				if (unit.hasClass('enemyUnit'))
				{
					svgAddClass(polygon, 'instancePolygonEnemy');
				}
				else
				{
					svgAddClass(polygon, 'instancePolygonActive');
					blink('#testElement');
				}
				svgRemoveClass(polygon, 'instancePolygon');
				stopAudioLoop('soundMoveLeather', unit.attr('id'));
			}
		);
		//jQuery('#testElement').css('left', centerX);
		//jQuery('#testElement').css('top', centerY);

		unit.data('previousPolygonId', polygon.attr('id'));
		polygon.data('unit', unit.attr('id'));
	}

	function finalizeActionOnPolygon(data)
	{
		if (data != null && data.success == true && typeof(data.polygonId) != 'undefined')
		{
			var unit = activeUnit;

			var polygon = jQuery('#' + data.polygonId);
			if (polygon.length > 0 && typeof(data.actionType) != 'undefined')
			{
				if (data.actionType == 'attack')
				{
					if (typeof(data.attackingUnit) != 'undefined')
					{
						unit = jQuery('#' + data.attackingUnit);
					}
					handleAttackPolygon(polygon, unit, data);
				}
				else if (data.actionType == 'move')
				{
					if (typeof(data.unitToMove) != 'undefined')
					{
						unit = jQuery('#' + data.unitToMove);
					}
					handleMoveToPolygon(polygon, unit);
					playAudioLoop('soundMoveLeather', unit.attr('id'));
				}
			}

			if (typeof(data.newEnemyPosition) != 'undefined')
			{
				handleMoveToPolygon(jQuery('#' + data.newEnemyPosition), jQuery('#testEnemy'));
			}

			if (typeof(data.stashedItemId) != 'undefined' && typeof(data.stashedItemContent) != 'undefined')
			{
				putItemToStash(data);
			}

			if (typeof(data.friendlyTurn) != 'undefined' && data.friendlyTurn == true)
			{
				setTimeout(function(){actionOnPolygon(null, jQuery('#testEnemy'));}, 350);
			}
		}
	}

	function handleAttackPolygon(polygon, unit, data)
	{
		var targetUnit = jQuery('#' + polygon.data('unit'));

		var damageDealt = 0;
		if (typeof(data.damageDealt) != 'undefined')
		{
			var hitType = null;
			if (typeof(data.type) != 'undefined')
			{
				hitType = data.type;
			}

			damageDealt = data.damageDealt;
			addDamageIndicator(targetUnit, damageDealt, hitType)
		}
		turmoil.logCombat('Unit ' + unit.attr('id') + ' attacks unit ' + polygon.data('unit') + ' on ' + polygon.attr('id') + ' dealing ' + damageDealt + ' damage');

		if (typeof(data.healthBar) != 'undefined')
		{
			jQuery('#' + polygon.data('unit') + 'Health').css('width', data.healthBar);
		}

		var effect = jQuery('#' + targetUnit.attr('id') + 'Effect');
		effect.addClass('attackSwing');
		playAudio('soundAttackMelee' + randomInt(3));

		setTimeout(function(){effect.removeClass('attackSwing');}, 500);
	}

	function animateToTop(id)
	{
		var element = jQuery('#' + id);
		if (element.length > 0)
		{
			var direction = 1;
			if (element.data('direction') == 2)
			{
				direction = -1;
			}
			element.css('top', '-=2px');
			if (direction != 0)
			{
				element.css('left', '-=' + element.data('variable') + 'px');
				if (element.data('variable') > 5)
				{
					element.data('variable', element.data('variable') + 0.05 * direction);
				}
				else
				{
					element.data('variable', element.data('variable') + 0.1 * direction);
				}
			}
			setTimeout(function(){animateToTop(id);}, 25);
		}
	}

	function animateIndicator(id)
	{
		var element = jQuery('#' + id);
		element.data('variable', 0);
		element.data('direction', randomInt(2));
		animateToTop(id);
		element.fadeTo(2000, 0.0, function () {
			element.remove();
		});
	}

	function addDamageIndicator(unit, value, type)
	{
		var ident = 'indicator_' + new Date().getTime();
		var styleClass = 'damageIndicator';

		if (typeof(type) != 'undefined')
		{
			styleClass += ' ';
			switch (type)
			{
				case 'critical':
				{
					styleClass += 'damageIndicatorCritical';
					break;
				}
				case 'devastate':
				{
					styleClass += 'damageIndicatorDevastate';
					break;
				}
				case 'healing':
				{
					styleClass += 'damageIndicatorHealing';
					break;
				}
			}
		}

		var damageIndicator = '<div id="' + ident + '" class="' + styleClass + '">' + value + '</div>';
		unit.prepend(damageIndicator);
		animateIndicator(ident);
	}

	$(function() {

		handleMoveToPolygon(jQuery('#${combatState.friend.instancePosition}'), jQuery('#testElement'));
		handleMoveToPolygon(jQuery('#${combatState.enemy.instancePosition}'), jQuery('#testEnemy'));

		jQuery(".instancePolygon").click(function() {
			var polygon = jQuery(this);
			actionOnPolygon(polygon);
		});

		jQuery(".flatSubMenu").mouseenter(function() {
			resetZIndex();
		});

		activeUnit = jQuery('#testElement');
	});

</r:script>

<style>
	.instanceContainerWrapper {
		position: relative;
	}

	.instanceContainer {
		position: absolute;
		left: 26px;
		top: 6px;
	}

	.instancePolygon {
		opacity: 0.5;
		stroke: none;
		fill: white;
		cursor: url("../images/ui/cursors/cursor_move_positive.png"), auto;
	}

	.instancePolygon:hover {
		fill: lime;
	}

	.instancePolygonActive {
		opacity: 0.5;
		stroke: none;
		fill: yellow;
	}

	.instancePolygonEnemy {
		opacity: 0.5;
		stroke: none;
		fill: darkred;
		cursor: url("../images/ui/cursors/cursor_move_negative.png"), auto;
	}

	.instanceCircle {
		display: none;
	}

	.instanceText {
		text-align: center;
		text-anchor: middle;
		font-size: 12px;
	}

	.instanceElement {
		position: absolute;
		top: 0;
		left: 0;
		color: red;
		z-index: 2;
	}

	.instanceSvg {
		position: absolute;
		z-index: 1;
	}

	.instancePortraitHealthBar {
		width: 60px;
		height: 4px;
		border: 1px solid black;
		background-color: red;
		position: absolute;
		top: -7px;
	}

	.instancePortraitHealthBarInner {
		background-color:  green;
		width: 60px;
		height: 4px;
	}

	.instancePortrait {
		width: 60px;
		border: 1px solid black;
	}

	.instancePortraitFlipped {
		-moz-transform: scaleX(-1);
		-o-transform: scaleX(-1);
		-webkit-transform: scaleX(-1);
		transform: scaleX(-1);
		filter: FlipH;
		-ms-filter: "FlipH";
		cursor: url("../images/ui/cursors/cursor_attack.png"), auto;
	}

	.instanceEnemy {
		border: 1px solid crimson;
	}

	.attackSwing {
		position: absolute;
		top: -15px;
		left: -65px;
		background: url("../images/animations/animation_swing.png") 0 0 no-repeat;
		animation: walk-cycle 0.5s steps(5) infinite;
		-webkit-animation: walk-cycle 0.5s steps(5) infinite;

		transform: scale(0.4);
		-webkit-transform: scale(0.4);
		-moz-transform: scale(0.4);
		-o-transform: scale(0.4);
		opacity: 0.8;
		z-index: 10000;

		width: 192px;
		height: 80px;
	}

	@keyframes walk-cycle {
		0% { background-position: 0 0; }
		100% { background-position: -960px 0; }
	}

	@-webkit-keyframes walk-cycle {
		0% { background-position: 0 0; }
		100% { background-position: -960px 0; }
	}

	.damageIndicator {
		color: #ce1e15;
		text-shadow: 2px 2px 2px rgb(5, 5, 5);
		font-weight: bold;
		font-size: 20px;
		font-family: 'Comic Sans MS', Arial, sans-serif;
		position: absolute;
		top: 10px;
		left: 20px;
		height: 30px;
		z-index: 20000;

		-webkit-touch-callout: none;
		-webkit-user-select: none;
		-khtml-user-select: none;
		-moz-user-select: none;
		-ms-user-select: none;
		user-select: none;
	}

	.damageIndicatorCritical {
		color: #ffdc22;
		font-size: 25px;
	}

	.damageIndicatorDevastate {
		color: #53d2d9;
		font-size: 30px;
	}

	.damageIndicatorHealing {
		color: #2ec913;
	}

</style>

<div id="window_${windowCode}" class="windowContent ${windowCode}WindowContent" style="transform:scale(${panelScale}); -webkit-transform:scale(${panelScale}); -moz-transform:scale(${panelScale}); -o-transform:scale(${panelScale});">
	<div class="windowContentInner" style="background-image: url('${resource(dir: 'images/backgrounds', file: 'background_grunge_650x550.png')}'); width: ${windowWidth}px; height: ${windowHeight}px;">
		<div id="instanceContainerWrapper" class="instanceContainerWrapper">

			<div id="instanceContainer" class="instanceContainer">
				<div class="instanceElement" id="testElement">
					<div class="instancePortraitHealthBar">
						<div class="instancePortraitHealthBarInner" id="testElementHealth" style="width: ${(combatState.friend.health * 60) / 100}px"></div>
					</div>
					<img class="instancePortrait" src="<g:resource dir="/images/portraits/${character.gender.toString().toLowerCase()}" file="${character.portrait}"/>"/>
					<div id="testElementEffect"></div>
				</div>
				<div class="instanceElement enemyUnit" id="testEnemy" onclick="actionOnUnit(this);">
					<div class="instancePortraitHealthBar">
						<div class="instancePortraitHealthBarInner" id="testEnemyHealth" style="width: ${(combatState.enemy.health * 60) / 100}px"></div>
					</div>
					<img class="instancePortrait instancePortraitFlipped instanceEnemy" src="<g:resource dir="/images/portraits/male" file="male_portrait_055.png"/>"/>
					<div id="testEnemyEffect"></div>
				</div>
				<div class="instanceSvg">
					<svg width="600" height="545" id="svgElement">
						<g>
							<polygon id="polygon-1-1" class="instancePolygon" points="72.0,0.0 96.0,41.6 72.0,83.1 24.0,83.1 0.0,41.6 24.0,0.0"/>
							<polygon id="polygon-1-2" class="instancePolygon" points="72.0,83.1 96.0,124.7 72.0,166.3 24.0,166.3 0.0,124.7 24.0,83.1"/>
							<polygon id="polygon-1-3" class="instancePolygon" points="72.0,166.3 96.0,207.8 72.0,249.4 24.0,249.4 0.0,207.8 24.0,166.3"/>
							<polygon id="polygon-1-4" class="instancePolygon" points="72.0,249.4 96.0,291.0 72.0,332.6 24.0,332.6 0.0,291.0 24.0,249.4"/>
							<polygon id="polygon-1-5" class="instancePolygon" points="72.0,332.6 96.0,374.1 72.0,415.7 24.0,415.7 0.0,374.1 24.0,332.6"/>
							<polygon id="polygon-1-6" class="instancePolygon" points="72.0,415.7 96.0,457.3 72.0,498.8 24.0,498.8 0.0,457.3 24.0,415.7"/>
							<polygon id="polygon-2-1" class="instancePolygon" points="144.0,41.6 168.0,83.1 144.0,124.7 96.0,124.7 72.0,83.1 96.0,41.6"/>
							<polygon id="polygon-2-2" class="instancePolygon" points="144.0,124.7 168.0,166.3 144.0,207.8 96.0,207.8 72.0,166.3 96.0,124.7"/>
							<polygon id="polygon-2-3" class="instancePolygon" points="144.0,207.8 168.0,249.4 144.0,291.0 96.0,291.0 72.0,249.4 96.0,207.8"/>
							<polygon id="polygon-2-4" class="instancePolygon" points="144.0,291.0 168.0,332.6 144.0,374.1 96.0,374.1 72.0,332.6 96.0,291.0"/>
							<polygon id="polygon-2-5" class="instancePolygon" points="144.0,374.1 168.0,415.7 144.0,457.3 96.0,457.3 72.0,415.7 96.0,374.1"/>
							<polygon id="polygon-2-6" class="instancePolygon" points="144.0,457.3 168.0,498.8 144.0,540.4 96.0,540.4 72.0,498.8 96.0,457.3"/>
							<polygon id="polygon-3-1" class="instancePolygon" points="216.0,0.0 240.0,41.6 216.0,83.1 168.0,83.1 144.0,41.6 168.0,0.0"/>
							<polygon id="polygon-3-2" class="instancePolygon" points="216.0,83.1 240.0,124.7 216.0,166.3 168.0,166.3 144.0,124.7 168.0,83.1"/>
							<polygon id="polygon-3-3" class="instancePolygon" points="216.0,166.3 240.0,207.8 216.0,249.4 168.0,249.4 144.0,207.8 168.0,166.3"/>
							<polygon id="polygon-3-4" class="instancePolygon" points="216.0,249.4 240.0,291.0 216.0,332.6 168.0,332.6 144.0,291.0 168.0,249.4"/>
							<polygon id="polygon-3-5" class="instancePolygon" points="216.0,332.6 240.0,374.1 216.0,415.7 168.0,415.7 144.0,374.1 168.0,332.6"/>
							<polygon id="polygon-3-6" class="instancePolygon" points="216.0,415.7 240.0,457.3 216.0,498.8 168.0,498.8 144.0,457.3 168.0,415.7"/>
							<polygon id="polygon-4-1" class="instancePolygon" points="288.0,41.6 312.0,83.1 288.0,124.7 240.0,124.7 216.0,83.1 240.0,41.6"/>
							<polygon id="polygon-4-2" class="instancePolygon" points="288.0,124.7 312.0,166.3 288.0,207.8 240.0,207.8 216.0,166.3 240.0,124.7"/>
							<polygon id="polygon-4-3" class="instancePolygon" points="288.0,207.8 312.0,249.4 288.0,291.0 240.0,291.0 216.0,249.4 240.0,207.8"/>
							<polygon id="polygon-4-4" class="instancePolygon" points="288.0,291.0 312.0,332.6 288.0,374.1 240.0,374.1 216.0,332.6 240.0,291.0"/>
							<polygon id="polygon-4-5" class="instancePolygon" points="288.0,374.1 312.0,415.7 288.0,457.3 240.0,457.3 216.0,415.7 240.0,374.1"/>
							<polygon id="polygon-4-6" class="instancePolygon" points="288.0,457.3 312.0,498.8 288.0,540.4 240.0,540.4 216.0,498.8 240.0,457.3"/>
							<polygon id="polygon-5-1" class="instancePolygon" points="360.0,0.0 384.0,41.6 360.0,83.1 312.0,83.1 288.0,41.6 312.0,0.0"/>
							<polygon id="polygon-5-2" class="instancePolygon" points="360.0,83.1 384.0,124.7 360.0,166.3 312.0,166.3 288.0,124.7 312.0,83.1"/>
							<polygon id="polygon-5-3" class="instancePolygon" points="360.0,166.3 384.0,207.8 360.0,249.4 312.0,249.4 288.0,207.8 312.0,166.3"/>
							<polygon id="polygon-5-4" class="instancePolygon" points="360.0,249.4 384.0,291.0 360.0,332.6 312.0,332.6 288.0,291.0 312.0,249.4"/>
							<polygon id="polygon-5-5" class="instancePolygon" points="360.0,332.6 384.0,374.1 360.0,415.7 312.0,415.7 288.0,374.1 312.0,332.6"/>
							<polygon id="polygon-5-6" class="instancePolygon" points="360.0,415.7 384.0,457.3 360.0,498.8 312.0,498.8 288.0,457.3 312.0,415.7"/>
							<polygon id="polygon-6-1" class="instancePolygon" points="432.0,41.6 456.0,83.1 432.0,124.7 384.0,124.7 360.0,83.1 384.0,41.6"/>
							<polygon id="polygon-6-2" class="instancePolygon" points="432.0,124.7 456.0,166.3 432.0,207.8 384.0,207.8 360.0,166.3 384.0,124.7"/>
							<polygon id="polygon-6-3" class="instancePolygon" points="432.0,207.8 456.0,249.4 432.0,291.0 384.0,291.0 360.0,249.4 384.0,207.8"/>
							<polygon id="polygon-6-4" class="instancePolygon" points="432.0,291.0 456.0,332.6 432.0,374.1 384.0,374.1 360.0,332.6 384.0,291.0"/>
							<polygon id="polygon-6-5" class="instancePolygon" points="432.0,374.1 456.0,415.7 432.0,457.3 384.0,457.3 360.0,415.7 384.0,374.1"/>
							<polygon id="polygon-6-6" class="instancePolygon" points="432.0,457.3 456.0,498.8 432.0,540.4 384.0,540.4 360.0,498.8 384.0,457.3"/>
							<polygon id="polygon-7-1" class="instancePolygon" points="504.0,0.0 528.0,41.6 504.0,83.1 456.0,83.1 432.0,41.6 456.0,0.0"/>
							<polygon id="polygon-7-2" class="instancePolygon" points="504.0,83.1 528.0,124.7 504.0,166.3 456.0,166.3 432.0,124.7 456.0,83.1"/>
							<polygon id="polygon-7-3" class="instancePolygon" points="504.0,166.3 528.0,207.8 504.0,249.4 456.0,249.4 432.0,207.8 456.0,166.3"/>
							<polygon id="polygon-7-4" class="instancePolygon" points="504.0,249.4 528.0,291.0 504.0,332.6 456.0,332.6 432.0,291.0 456.0,249.4"/>
							<polygon id="polygon-7-5" class="instancePolygon" points="504.0,332.6 528.0,374.1 504.0,415.7 456.0,415.7 432.0,374.1 456.0,332.6"/>
							<polygon id="polygon-7-6" class="instancePolygon" points="504.0,415.7 528.0,457.3 504.0,498.8 456.0,498.8 432.0,457.3 456.0,415.7"/>
							<polygon id="polygon-8-1" class="instancePolygon" points="576.0,41.6 600.0,83.1 576.0,124.7 528.0,124.7 504.0,83.1 528.0,41.6"/>
							<polygon id="polygon-8-2" class="instancePolygon" points="576.0,124.7 600.0,166.3 576.0,207.8 528.0,207.8 504.0,166.3 528.0,124.7"/>
							<polygon id="polygon-8-3" class="instancePolygon" points="576.0,207.8 600.0,249.4 576.0,291.0 528.0,291.0 504.0,249.4 528.0,207.8"/>
							<polygon id="polygon-8-4" class="instancePolygon" points="576.0,291.0 600.0,332.6 576.0,374.1 528.0,374.1 504.0,332.6 528.0,291.0"/>
							<polygon id="polygon-8-5" class="instancePolygon" points="576.0,374.1 600.0,415.7 576.0,457.3 528.0,457.3 504.0,415.7 528.0,374.1"/>
							<polygon id="polygon-8-6" class="instancePolygon" points="576.0,457.3 600.0,498.8 576.0,540.4 528.0,540.4 504.0,498.8 528.0,457.3"/>
						</g>

						<g>
							<line stroke="black" x1="24" y1="0" x2="72" y2="0" />
							<line stroke="black" x1="24" y1="0" x2="0" y2="42" />
							<line stroke="black" x1="72" y1="0" x2="96" y2="42" />
							<line stroke="black" x1="0" y1="42" x2="24" y2="83" />
							<line stroke="black" x1="24" y1="83" x2="72" y2="83" />
							<line stroke="black" x1="24" y1="83" x2="0" y2="125" />
							<line stroke="black" x1="0" y1="125" x2="24" y2="166" />
							<line stroke="black" x1="24" y1="166" x2="72" y2="166" />
							<line stroke="black" x1="24" y1="166" x2="0" y2="208" />
							<line stroke="black" x1="0" y1="208" x2="24" y2="249" />
							<line stroke="black" x1="24" y1="249" x2="72" y2="249" />
							<line stroke="black" x1="24" y1="249" x2="0" y2="291" />
							<line stroke="black" x1="0" y1="291" x2="24" y2="333" />
							<line stroke="black" x1="24" y1="333" x2="72" y2="333" />
							<line stroke="black" x1="24" y1="333" x2="0" y2="374" />
							<line stroke="black" x1="0" y1="374" x2="24" y2="416" />
							<line stroke="black" x1="24" y1="416" x2="72" y2="416" />
							<line stroke="black" x1="24" y1="416" x2="0" y2="457" />
							<line stroke="black" x1="0" y1="457" x2="24" y2="499" />
							<line stroke="black" x1="24" y1="499" x2="72" y2="499" />
							<line stroke="black" x1="96" y1="42" x2="144" y2="42" />
							<line stroke="black" x1="96" y1="42" x2="72" y2="83" />
							<line stroke="black" x1="72" y1="83" x2="96" y2="125" />
							<line stroke="black" x1="96" y1="125" x2="144" y2="125" />
							<line stroke="black" x1="96" y1="125" x2="72" y2="166" />
							<line stroke="black" x1="72" y1="166" x2="96" y2="208" />
							<line stroke="black" x1="96" y1="208" x2="144" y2="208" />
							<line stroke="black" x1="96" y1="208" x2="72" y2="249" />
							<line stroke="black" x1="72" y1="249" x2="96" y2="291" />
							<line stroke="black" x1="96" y1="291" x2="144" y2="291" />
							<line stroke="black" x1="96" y1="291" x2="72" y2="333" />
							<line stroke="black" x1="72" y1="333" x2="96" y2="374" />
							<line stroke="black" x1="96" y1="374" x2="144" y2="374" />
							<line stroke="black" x1="96" y1="374" x2="72" y2="416" />
							<line stroke="black" x1="72" y1="416" x2="96" y2="457" />
							<line stroke="black" x1="96" y1="457" x2="144" y2="457" />
							<line stroke="black" x1="96" y1="457" x2="72" y2="499" />
							<line stroke="black" x1="72" y1="499" x2="96" y2="540" />
							<line stroke="black" x1="168" y1="499" x2="144" y2="540" />
							<line stroke="black" x1="96" y1="540" x2="144" y2="540" />
							<line stroke="black" x1="168" y1="0" x2="216" y2="0" />
							<line stroke="black" x1="168" y1="0" x2="144" y2="42" />
							<line stroke="black" x1="216" y1="0" x2="240" y2="42" />
							<line stroke="black" x1="144" y1="42" x2="168" y2="83" />
							<line stroke="black" x1="168" y1="83" x2="216" y2="83" />
							<line stroke="black" x1="168" y1="83" x2="144" y2="125" />
							<line stroke="black" x1="144" y1="125" x2="168" y2="166" />
							<line stroke="black" x1="168" y1="166" x2="216" y2="166" />
							<line stroke="black" x1="168" y1="166" x2="144" y2="208" />
							<line stroke="black" x1="144" y1="208" x2="168" y2="249" />
							<line stroke="black" x1="168" y1="249" x2="216" y2="249" />
							<line stroke="black" x1="168" y1="249" x2="144" y2="291" />
							<line stroke="black" x1="144" y1="291" x2="168" y2="333" />
							<line stroke="black" x1="168" y1="333" x2="216" y2="333" />
							<line stroke="black" x1="168" y1="333" x2="144" y2="374" />
							<line stroke="black" x1="144" y1="374" x2="168" y2="416" />
							<line stroke="black" x1="168" y1="416" x2="216" y2="416" />
							<line stroke="black" x1="168" y1="416" x2="144" y2="457" />
							<line stroke="black" x1="144" y1="457" x2="168" y2="499" />
							<line stroke="black" x1="168" y1="499" x2="216" y2="499" />
							<line stroke="black" x1="240" y1="42" x2="288" y2="42" />
							<line stroke="black" x1="240" y1="42" x2="216" y2="83" />
							<line stroke="black" x1="216" y1="83" x2="240" y2="125" />
							<line stroke="black" x1="240" y1="125" x2="288" y2="125" />
							<line stroke="black" x1="240" y1="125" x2="216" y2="166" />
							<line stroke="black" x1="216" y1="166" x2="240" y2="208" />
							<line stroke="black" x1="240" y1="208" x2="288" y2="208" />
							<line stroke="black" x1="240" y1="208" x2="216" y2="249" />
							<line stroke="black" x1="216" y1="249" x2="240" y2="291" />
							<line stroke="black" x1="240" y1="291" x2="288" y2="291" />
							<line stroke="black" x1="240" y1="291" x2="216" y2="333" />
							<line stroke="black" x1="216" y1="333" x2="240" y2="374" />
							<line stroke="black" x1="240" y1="374" x2="288" y2="374" />
							<line stroke="black" x1="240" y1="374" x2="216" y2="416" />
							<line stroke="black" x1="216" y1="416" x2="240" y2="457" />
							<line stroke="black" x1="240" y1="457" x2="288" y2="457" />
							<line stroke="black" x1="240" y1="457" x2="216" y2="499" />
							<line stroke="black" x1="216" y1="499" x2="240" y2="540" />
							<line stroke="black" x1="312" y1="499" x2="288" y2="540" />
							<line stroke="black" x1="240" y1="540" x2="288" y2="540" />
							<line stroke="black" x1="312" y1="0" x2="360" y2="0" />
							<line stroke="black" x1="312" y1="0" x2="288" y2="42" />
							<line stroke="black" x1="360" y1="0" x2="384" y2="42" />
							<line stroke="black" x1="288" y1="42" x2="312" y2="83" />
							<line stroke="black" x1="312" y1="83" x2="360" y2="83" />
							<line stroke="black" x1="312" y1="83" x2="288" y2="125" />
							<line stroke="black" x1="288" y1="125" x2="312" y2="166" />
							<line stroke="black" x1="312" y1="166" x2="360" y2="166" />
							<line stroke="black" x1="312" y1="166" x2="288" y2="208" />
							<line stroke="black" x1="288" y1="208" x2="312" y2="249" />
							<line stroke="black" x1="312" y1="249" x2="360" y2="249" />
							<line stroke="black" x1="312" y1="249" x2="288" y2="291" />
							<line stroke="black" x1="288" y1="291" x2="312" y2="333" />
							<line stroke="black" x1="312" y1="333" x2="360" y2="333" />
							<line stroke="black" x1="312" y1="333" x2="288" y2="374" />
							<line stroke="black" x1="288" y1="374" x2="312" y2="416" />
							<line stroke="black" x1="312" y1="416" x2="360" y2="416" />
							<line stroke="black" x1="312" y1="416" x2="288" y2="457" />
							<line stroke="black" x1="288" y1="457" x2="312" y2="499" />
							<line stroke="black" x1="312" y1="499" x2="360" y2="499" />
							<line stroke="black" x1="384" y1="42" x2="432" y2="42" />
							<line stroke="black" x1="384" y1="42" x2="360" y2="83" />
							<line stroke="black" x1="360" y1="83" x2="384" y2="125" />
							<line stroke="black" x1="384" y1="125" x2="432" y2="125" />
							<line stroke="black" x1="384" y1="125" x2="360" y2="166" />
							<line stroke="black" x1="360" y1="166" x2="384" y2="208" />
							<line stroke="black" x1="384" y1="208" x2="432" y2="208" />
							<line stroke="black" x1="384" y1="208" x2="360" y2="249" />
							<line stroke="black" x1="360" y1="249" x2="384" y2="291" />
							<line stroke="black" x1="384" y1="291" x2="432" y2="291" />
							<line stroke="black" x1="384" y1="291" x2="360" y2="333" />
							<line stroke="black" x1="360" y1="333" x2="384" y2="374" />
							<line stroke="black" x1="384" y1="374" x2="432" y2="374" />
							<line stroke="black" x1="384" y1="374" x2="360" y2="416" />
							<line stroke="black" x1="360" y1="416" x2="384" y2="457" />
							<line stroke="black" x1="384" y1="457" x2="432" y2="457" />
							<line stroke="black" x1="384" y1="457" x2="360" y2="499" />
							<line stroke="black" x1="360" y1="499" x2="384" y2="540" />
							<line stroke="black" x1="456" y1="499" x2="432" y2="540" />
							<line stroke="black" x1="384" y1="540" x2="432" y2="540" />
							<line stroke="black" x1="456" y1="0" x2="504" y2="0" />
							<line stroke="black" x1="456" y1="0" x2="432" y2="42" />
							<line stroke="black" x1="504" y1="0" x2="528" y2="42" />
							<line stroke="black" x1="432" y1="42" x2="456" y2="83" />
							<line stroke="black" x1="456" y1="83" x2="504" y2="83" />
							<line stroke="black" x1="456" y1="83" x2="432" y2="125" />
							<line stroke="black" x1="432" y1="125" x2="456" y2="166" />
							<line stroke="black" x1="456" y1="166" x2="504" y2="166" />
							<line stroke="black" x1="456" y1="166" x2="432" y2="208" />
							<line stroke="black" x1="432" y1="208" x2="456" y2="249" />
							<line stroke="black" x1="456" y1="249" x2="504" y2="249" />
							<line stroke="black" x1="456" y1="249" x2="432" y2="291" />
							<line stroke="black" x1="432" y1="291" x2="456" y2="333" />
							<line stroke="black" x1="456" y1="333" x2="504" y2="333" />
							<line stroke="black" x1="456" y1="333" x2="432" y2="374" />
							<line stroke="black" x1="432" y1="374" x2="456" y2="416" />
							<line stroke="black" x1="456" y1="416" x2="504" y2="416" />
							<line stroke="black" x1="456" y1="416" x2="432" y2="457" />
							<line stroke="black" x1="432" y1="457" x2="456" y2="499" />
							<line stroke="black" x1="456" y1="499" x2="504" y2="499" />
							<line stroke="black" x1="528" y1="42" x2="576" y2="42" />
							<line stroke="black" x1="528" y1="42" x2="504" y2="83" />
							<line stroke="black" x1="504" y1="83" x2="528" y2="125" />
							<line stroke="black" x1="528" y1="125" x2="576" y2="125" />
							<line stroke="black" x1="528" y1="125" x2="504" y2="166" />
							<line stroke="black" x1="504" y1="166" x2="528" y2="208" />
							<line stroke="black" x1="528" y1="208" x2="576" y2="208" />
							<line stroke="black" x1="528" y1="208" x2="504" y2="249" />
							<line stroke="black" x1="504" y1="249" x2="528" y2="291" />
							<line stroke="black" x1="528" y1="291" x2="576" y2="291" />
							<line stroke="black" x1="528" y1="291" x2="504" y2="333" />
							<line stroke="black" x1="504" y1="333" x2="528" y2="374" />
							<line stroke="black" x1="528" y1="374" x2="576" y2="374" />
							<line stroke="black" x1="528" y1="374" x2="504" y2="416" />
							<line stroke="black" x1="504" y1="416" x2="528" y2="457" />
							<line stroke="black" x1="528" y1="457" x2="576" y2="457" />
							<line stroke="black" x1="528" y1="457" x2="504" y2="499" />
							<line stroke="black" x1="504" y1="499" x2="528" y2="540" />
							<line stroke="black" x1="600" y1="499" x2="576" y2="540" />
							<line stroke="black" x1="528" y1="540" x2="576" y2="540" />
							<line stroke="black" x1="576" y1="42" x2="600" y2="83" />
							<line stroke="black" x1="600" y1="83" x2="576" y2="125" />
							<line stroke="black" x1="576" y1="125" x2="600" y2="166" />
							<line stroke="black" x1="600" y1="166" x2="576" y2="208" />
							<line stroke="black" x1="576" y1="208" x2="600" y2="249" />
							<line stroke="black" x1="600" y1="249" x2="576" y2="291" />
							<line stroke="black" x1="576" y1="291" x2="600" y2="333" />
							<line stroke="black" x1="600" y1="333" x2="576" y2="374" />
							<line stroke="black" x1="576" y1="374" x2="600" y2="416" />
							<line stroke="black" x1="600" y1="416" x2="576" y2="457" />
							<line stroke="black" x1="576" y1="457" x2="600" y2="499" />
						</g>

						<g>
							<text class="instanceText" x="48" y="79">1:1</text>
							<text class="instanceText" x="48" y="162">1:2</text>
							<text class="instanceText" x="48" y="245">1:3</text>
							<text class="instanceText" x="48" y="328">1:4</text>
							<text class="instanceText" x="48" y="412">1:5</text>
							<text class="instanceText" x="48" y="495">1:6</text>
							<text class="instanceText" x="120" y="121">2:1</text>
							<text class="instanceText" x="120" y="204">2:2</text>
							<text class="instanceText" x="120" y="287">2:3</text>
							<text class="instanceText" x="120" y="370">2:4</text>
							<text class="instanceText" x="120" y="453">2:5</text>
							<text class="instanceText" x="120" y="536">2:6</text>
							<text class="instanceText" x="192" y="79">3:1</text>
							<text class="instanceText" x="192" y="162">3:2</text>
							<text class="instanceText" x="192" y="245">3:3</text>
							<text class="instanceText" x="192" y="328">3:4</text>
							<text class="instanceText" x="192" y="412">3:5</text>
							<text class="instanceText" x="192" y="495">3:6</text>
							<text class="instanceText" x="264" y="121">4:1</text>
							<text class="instanceText" x="264" y="204">4:2</text>
							<text class="instanceText" x="264" y="287">4:3</text>
							<text class="instanceText" x="264" y="370">4:4</text>
							<text class="instanceText" x="264" y="453">4:5</text>
							<text class="instanceText" x="264" y="536">4:6</text>
							<text class="instanceText" x="336" y="79">5:1</text>
							<text class="instanceText" x="336" y="162">5:2</text>
							<text class="instanceText" x="336" y="245">5:3</text>
							<text class="instanceText" x="336" y="328">5:4</text>
							<text class="instanceText" x="336" y="412">5:5</text>
							<text class="instanceText" x="336" y="495">5:6</text>
							<text class="instanceText" x="408" y="121">6:1</text>
							<text class="instanceText" x="408" y="204">6:2</text>
							<text class="instanceText" x="408" y="287">6:3</text>
							<text class="instanceText" x="408" y="370">6:4</text>
							<text class="instanceText" x="408" y="453">6:5</text>
							<text class="instanceText" x="408" y="536">6:6</text>
							<text class="instanceText" x="480" y="79">7:1</text>
							<text class="instanceText" x="480" y="162">7:2</text>
							<text class="instanceText" x="480" y="245">7:3</text>
							<text class="instanceText" x="480" y="328">7:4</text>
							<text class="instanceText" x="480" y="412">7:5</text>
							<text class="instanceText" x="480" y="495">7:6</text>
							<text class="instanceText" x="552" y="121">8:1</text>
							<text class="instanceText" x="552" y="204">8:2</text>
							<text class="instanceText" x="552" y="287">8:3</text>
							<text class="instanceText" x="552" y="370">8:4</text>
							<text class="instanceText" x="552" y="453">8:5</text>
							<text class="instanceText" x="552" y="536">8:6</text>
						</g>

						<g>
							<circle class="instanceCircle" cx="48" cy="42" r="1.66" />
							<circle class="instanceCircle" cx="48" cy="125" r="1.66" />
							<circle class="instanceCircle" cx="48" cy="208" r="1.66" />
							<circle class="instanceCircle" cx="48" cy="291" r="1.66" />
							<circle class="instanceCircle" cx="48" cy="374" r="1.66" />
							<circle class="instanceCircle" cx="48" cy="457" r="1.66" />
							<circle class="instanceCircle" cx="120" cy="83" r="1.66" />
							<circle class="instanceCircle" cx="120" cy="166" r="1.66" />
							<circle class="instanceCircle" cx="120" cy="249" r="1.66" />
							<circle class="instanceCircle" cx="120" cy="333" r="1.66" />
							<circle class="instanceCircle" cx="120" cy="416" r="1.66" />
							<circle class="instanceCircle" cx="120" cy="499" r="1.66" />
							<circle class="instanceCircle" cx="192" cy="42" r="1.66" />
							<circle class="instanceCircle" cx="192" cy="125" r="1.66" />
							<circle class="instanceCircle" cx="192" cy="208" r="1.66" />
							<circle class="instanceCircle" cx="192" cy="291" r="1.66" />
							<circle class="instanceCircle" cx="192" cy="374" r="1.66" />
							<circle class="instanceCircle" cx="192" cy="457" r="1.66" />
							<circle class="instanceCircle" cx="264" cy="83" r="1.66" />
							<circle class="instanceCircle" cx="264" cy="166" r="1.66" />
							<circle class="instanceCircle" cx="264" cy="249" r="1.66" />
							<circle class="instanceCircle" cx="264" cy="333" r="1.66" />
							<circle class="instanceCircle" cx="264" cy="416" r="1.66" />
							<circle class="instanceCircle" cx="264" cy="499" r="1.66" />
							<circle class="instanceCircle" cx="336" cy="42" r="1.66" />
							<circle class="instanceCircle" cx="336" cy="125" r="1.66" />
							<circle class="instanceCircle" cx="336" cy="208" r="1.66" />
							<circle class="instanceCircle" cx="336" cy="291" r="1.66" />
							<circle class="instanceCircle" cx="336" cy="374" r="1.66" />
							<circle class="instanceCircle" cx="336" cy="457" r="1.66" />
							<circle class="instanceCircle" cx="408" cy="83" r="1.66" />
							<circle class="instanceCircle" cx="408" cy="166" r="1.66" />
							<circle class="instanceCircle" cx="408" cy="249" r="1.66" />
							<circle class="instanceCircle" cx="408" cy="333" r="1.66" />
							<circle class="instanceCircle" cx="408" cy="416" r="1.66" />
							<circle class="instanceCircle" cx="408" cy="499" r="1.66" />
							<circle class="instanceCircle" cx="480" cy="42" r="1.66" />
							<circle class="instanceCircle" cx="480" cy="125" r="1.66" />
							<circle class="instanceCircle" cx="480" cy="208" r="1.66" />
							<circle class="instanceCircle" cx="480" cy="291" r="1.66" />
							<circle class="instanceCircle" cx="480" cy="374" r="1.66" />
							<circle class="instanceCircle" cx="480" cy="457" r="1.66" />
							<circle class="instanceCircle" cx="552" cy="83" r="1.66" />
							<circle class="instanceCircle" cx="552" cy="166" r="1.66" />
							<circle class="instanceCircle" cx="552" cy="249" r="1.66" />
							<circle class="instanceCircle" cx="552" cy="333" r="1.66" />
							<circle class="instanceCircle" cx="552" cy="416" r="1.66" />
							<circle class="instanceCircle" cx="552" cy="499" r="1.66" />
						</g>
					</svg>
				</div>
			</div>
		</div>

	</div>
</div>