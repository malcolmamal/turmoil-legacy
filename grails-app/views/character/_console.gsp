<g:set var="windowWidth" value="600"/>
<g:set var="windowHeight" value="160"/>

<r:script>
	windowSizes.${windowCode}Width = ${windowWidth};
	windowSizes.${windowCode}Height = ${windowHeight};

	jQuery("#consoleTabs").tabs();
</r:script>

<style>
	.consoleTabs {
		padding: 0px;
		margin: 0px;
		background: none;
		border: 0px;
	}

	.consoleTab {
		height: 100px;
	}

	.ui-widget {
		font-size: 13px;
	}

	.ui-widget-header {
		background: none;
		border: 0px;
		padding: 0px;
		margin: 0px;
	}

	.ui-tabs .ui-tabs-nav .ui-tabs-anchor {
		padding: 0px;
	}

	.ui-tabs .ui-tabs-nav {
		margin: 0px;
		padding: 0px;
		border-bottom: 1px solid #666666;
	}

	.ui-tabs .ui-tabs-panel {
		padding: 4px 4px;
		background: none;
	}
</style>

<div id="window_${windowCode}" class="windowContent ${windowCode}WindowContent" style="transform:scale(${panelScale}); -webkit-transform:scale(${panelScale}); -moz-transform:scale(${panelScale}); -o-transform:scale(${panelScale});">
	<div class="windowContentInner" style="background-image: url('${resource(dir: 'images/backgrounds', file: 'background_paper_texture_1920x480.jpg')}'); width: ${windowWidth}px; height: ${windowHeight}px;">

		<div id="statsContent" class="statsContainer" style="width: ${(windowWidth.toInteger()-16)}px; height: ${windowHeight.toInteger()-20}px;">
			<div id="consoleTabs" class="consoleTabs">
				<ul>
					<li><a href="#console-all"><span>All</span></a></li>
					<li><a href="#console-combat"><span>Combat</span></a></li>
					<li><a href="#console-loot"><span>Loot</span></a></li>
					<li><a href="#console-chat"><span>Chat</span></a></li>
					<li><a href="#console-other"><span>Other</span></a></li>
				</ul>
				<div id="console-all" class="scrollableContainer consoleTab"></div>
				<div id="console-combat" class="scrollableContainer consoleTab">
					Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
					Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
				</div>
				<div id="console-loot" class="scrollableContainer consoleTab">
					Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
					Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
					Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
					Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
					Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
					Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
				</div>
				<div id="console-chat" class="scrollableContainer consoleTab">
					Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
				</div>
				<div id="console-other" class="scrollableContainer consoleTab"></div>
			</div>
		</div>

	</div>
</div>