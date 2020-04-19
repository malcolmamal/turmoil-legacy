<g:set var="windowWidth" value="300"/>
<g:set var="windowHeight" value="700"/>

<r:script>
	windowSizes.${windowCode}Width = ${windowWidth};
	windowSizes.${windowCode}Height = ${windowHeight};
</r:script>

<style>

	.statsContainer {
		padding: 8px;
	}

	.statsLabel {
	}

	.statsValue {
		text-align: right;
		width: auto;
	}

	.statsTable tr:hover {
		background-color: transparent;
	}

	.statsTable tr:hover td {
		background-color: transparent;
		border-bottom: 1px solid white;
	}

	.statsTable tr td {
		border-bottom: 1px solid transparent;
		font-size: 13px;
		padding-left: 10px;
		padding-top: 8px;
		padding-bottom: 2px;
	}

</style>

<div id="window_${windowCode}" class="windowContent ${windowCode}WindowContent" style="transform:scale(${panelScale}); -webkit-transform:scale(${panelScale}); -moz-transform:scale(${panelScale}); -o-transform:scale(${panelScale});">
	<div class="windowContentInner" style="background-image: url('${resource(dir: 'images/backgrounds', file: 'background_brown_fabric_300x700.png')}'); width: ${windowWidth}px; height: ${windowHeight}px;">

		<div id="statsContent" class="scrollableContainer statsContainer" style="width: ${(windowWidth.toInteger()-16)}px; height: ${windowHeight.toInteger()-20}px;">
			<g:render template="/character/character_state" model="['characterState': characterState]"/>
		</div>

	</div>
</div>