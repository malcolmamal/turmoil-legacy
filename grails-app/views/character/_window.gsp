<g:if test="${!panelScale}">
	<g:set var="panelScale" value="1.0"/>
</g:if>

<g:if test="${!windowHandlePosition}">
	<g:set var="windowHandlePosition" value="-40"/>
</g:if>

<div class="windowIcon ${windowCode}WindowIcon noSelection" onclick="switchShowClose('${windowCode}', true);"><div class="windowIconHover"></div><div class="windowIconText noSelection"><g:message code="turmoil.name.window.${windowCode}"/></div></div>

<div id="window_${windowCode}_resizer" class="windowResizer ${windowCode}WindowResizer" style="display: none;">
	<div id="window_${windowCode}_wrapper" class="windowWrapper">
		<div id="handle_${windowCode}_container" class="handleContainer ${windowCode}HandleContainer" style=" background-position: 0 ${windowHandlePosition}px;" onclick="bringToTheTop('${windowCode}');" ondblclick="switchMinimizeMaximize('${windowCode}')" oncontextmenu="resizeToDefault('${windowCode}'); return false;">

			<div class="handleLeft" style="background-position: 0 ${windowHandlePosition}px;"></div>
			<div class="handleBox ${windowCode}HandleBox"><g:message code="turmoil.name.window.${windowCode}"/></div>
			<div class="handleRight" style="background-position: 0 ${windowHandlePosition}px;">

				<div id="windowButtons" style="height: 40px; width: 75px;">
					<div id="${windowCode}ButtonMaximize" class="icons iconMaximize" style="position: absolute; top: 7px; right: 33px; display: none;" title="<g:message code="turmoil.icon.name.maximize"/>" onClick="actionMaximize('${windowCode}');">&nbsp;</div>
					<div id="${windowCode}ButtonMinimize" class="icons iconMinimize" style="position: absolute; top: 7px; right: 33px;" title="<g:message code="turmoil.icon.name.minimize"/>" onClick="actionMinimize('${windowCode}');">&nbsp;</div>
					<div class="icons iconClose" style="position: absolute; top: 7px; right: 8px;" title="<g:message code="turmoil.icon.name.close"/>" onClick="actionClose('${windowCode}');">&nbsp;</div>
				</div>
			</div>

		</div>
		<div id="window_${windowCode}_content_wrapper" style="position: absolute; top: 40px; left: 0px;" onclick="bringToTheTop('${windowCode}');">
			<g:render template="/character/${windowCode}" model="['character': character, 'windowCode': windowCode, 'panelScale': panelScale]"/>
		</div>
	</div>
</div>