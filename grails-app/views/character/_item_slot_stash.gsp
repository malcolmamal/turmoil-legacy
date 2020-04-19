<%@ page import="turmoil.enums.ItemType" %>

<g:if test="${!iconItemSize}">
	<g:set var="iconItemSize" value="big"/>
</g:if>

<li class="stashItemListEntry" id="stash_item_${item.id}" item="${item.id}" oncontextmenu="actionRightClickOnStashedItem('${item.id}'); return false;">
	<a class="slot slot-mainHand tooltip itemTooltip" id="tooltip_${item.getFileCode()}_${item.id}" item="${item.id}">
		<span class="stashItem d3-icon d3-icon-item stash-icon-item-large d3-icon-item-${item.getRarityClass()}">
			<span class="icon-item-gradient">
				<span class="icon-item-inner stash-icon-item-default"  style="background-image: url('${resource(dir: item.getImagePath(), file: item.getImageFile())}');"></span>
			</span>
		</span>
	</a>
</li>