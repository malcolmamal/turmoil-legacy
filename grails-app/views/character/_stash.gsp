<g:set var="windowWidth" value="500"/>
<g:set var="windowHeight" value="700"/>

<r:script>
	windowSizes.${windowCode}Width = ${windowWidth};
	windowSizes.${windowCode}Height = ${windowHeight};

	$(function() {
		jQuery("#stashItemListContainer").sortable({
			//forceHelperSize: true,
			containment: "#stashItemContainer",
			//grid: [ 6, 3 ],
			distance: 45,
			items: "> li",
			update: function(event, ui) {
				var resultOrder = $(this).sortable('toArray').toString();
				console.log(resultOrder);
			}
		});
		jQuery("#stashItemListContainer").disableSelection();
	});

</r:script>

<style>

	#stashItemListContainer {
		list-style-type: none;
		width: 443px;
		height: 647px;
		overflow:hidden;
	}

	.stashItemContainerWrapper {
		width: 443px;
		height: 647px;
	}

	.stashItemContainer {
		position: absolute;

		left: 29px;
		top: 27px;
/*
		left: 30px;
		top: 27px;
*/
		width: 443px;
		height: 647px;
	}

	.stashItemListEntry {
		float: left;
		position: relative;
		margin: 2.5px;
		padding-bottom: 6px;
		width: 58px;
		height: 118px;
	}

	.stashItem {
/*
		float: left;
		position: relative;

		margin: 3px;
		width: 55px;
		height: 121px;
*/

/*
		margin: 2px;
		width: 57px;
		height: 123px;
*/

/*
		margin: 1px;
		width: 59px;
		height: 125px;
*/
	}

</style>

<div id="window_${windowCode}" class="windowContent ${windowCode}WindowContent" style="transform:scale(${panelScale}); -webkit-transform:scale(${panelScale}); -moz-transform:scale(${panelScale}); -o-transform:scale(${panelScale});">
	<div class="windowContentInner" style="background-image: url('${resource(dir: 'images/windows', file: 'stash.png')}'); width: ${windowWidth}px; height: ${windowHeight}px;">

		<g:set var="stash" value="${character.getPrimaryStash()}"/>

		<g:if test="${stash != null}">
			<g:set var="items" value="${stash.getItems()}"/>
		</g:if>

		<div id="stashItemContainerWrapper">
			<div id="stashItemContainer" class="stashItemContainer">
				<ul id="stashItemListContainer">
					<g:if test="${items.size() > 0}">
						<g:each in="${items}">
							<g:render template="/character/item_slot_stash" model="['item': it]"/>
						</g:each>
					</g:if>
				</ul>
			</div>
		</div>

	</div>
</div>