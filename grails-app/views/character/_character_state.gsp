<%@ page import="java.text.DecimalFormat" %>

<table class="statsTable" cellpadding="0" cellspacing="0">
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.level"/></td>
		<td class="statsValue" nowrap>${characterState.level}</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.experience"/></td>
		<td class="statsValue" nowrap title="${characterState.experience} / ${characterState.requiredExperience} (${new DecimalFormat("#.##").format((characterState.experience/characterState.requiredExperience) * 100)}%)">${characterState.experience}</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.stat.strength"/></td>
		<td class="statsValue" nowrap>${characterState.statStrength}</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.stat.dexterity"/></td>
		<td class="statsValue" nowrap>${characterState.statDexterity}</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.stat.intelligence"/></td>
		<td class="statsValue" nowrap>${characterState.statIntelligence}</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.stat.vitality"/></td>
		<td class="statsValue" nowrap>${characterState.statVitality}</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.health"/></td>
		<td class="statsValue" nowrap>${characterState.health}</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.mana"/></td>
		<td class="statsValue" nowrap>${characterState.mana}</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.average.damage"/></td>
		<td class="statsValue" nowrap>${characterState.damageAvg}</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.crit.chance"/></td>
		<td class="statsValue" nowrap>${characterState.critChance} %</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.crit.damage"/></td>
		<td class="statsValue" nowrap>${characterState.critDamage} %</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.armor"/></td>
		<td class="statsValue" nowrap>${characterState.armor}</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.block"/></td>
		<td class="statsValue" nowrap>${characterState.evasionBlock} %</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.dodge"/></td>
		<td class="statsValue" nowrap>${characterState.evasionDodge} %</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.parry"/></td>
		<td class="statsValue" nowrap>${characterState.evasionParry} %</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.resist.fire"/></td>
		<td class="statsValue" nowrap>${characterState.resistFire} %</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.resist.cold"/></td>
		<td class="statsValue" nowrap>${characterState.resistCold} %</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.resist.lightning"/></td>
		<td class="statsValue" nowrap>${characterState.resistLightning} %</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.resist.poison"/></td>
		<td class="statsValue" nowrap>${characterState.resistPoison} %</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.resist.arcane"/></td>
		<td class="statsValue" nowrap>${characterState.resistArcane} %</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.resist.bleed"/></td>
		<td class="statsValue" nowrap>${characterState.resistBleed} %</td>
	</tr>
	<tr>
		<td class="statsLabel"><g:message code="turmoil.stats.label.resist.piercing"/></td>
		<td class="statsValue" nowrap>${characterState.resistPiercing} %</td>
	</tr>
</table>