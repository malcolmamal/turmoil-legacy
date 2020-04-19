package turmoil.helpers

import groovy.util.logging.Log4j
import org.jgrapht.UndirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph
import turmoil.Character
import turmoil.Monster
import turmoil.Person
import turmoil.generators.ItemGenerator
import turmoil.instances.CombatState

@Log4j
class InstanceHelper
{
	public static UndirectedGraph<String, DefaultEdge> getInstanceGraph()
	{
		UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class)

		def maxWidth = 8
		def maxHeight = 6

		for (int i = 1; i <= maxWidth; i++)
		{
			for (int j = 1; j <= maxHeight; j++)
			{
				String vertex = new String("${i}-${j}")
				graph.addVertex(vertex)

				if (j > 1)
				{
					String vertexAbove = new String("${i}-${j-1}")
					graph.addEdge(vertexAbove, vertex)

					if (i > 1 && i % 2 != 0)
					{
						String vertexDiagonalRight = new String("${i - 1}-${j - 1}")
						graph.addEdge(vertexDiagonalRight, vertex)
					}
				}

				if (i > 1)
				{
					String vertexBeside = new String("${i-1}-${j}")
					graph.addEdge(vertexBeside, vertex)

					if (i % 2 == 0 && j < maxHeight)
					{
						String vertexDiagonalLeft = new String("${i - 1}-${j + 1}")
						graph.addEdge(vertexDiagonalLeft, vertex)
					}
				}
			}
		}

		return graph
	}

	public static CombatState createCombatState(Character character)
	{
		def combatState = new CombatState()
		combatState.friend = character
		combatState.friend.currentHealth = combatState.friend.health
		combatState.friend.instancePosition = "polygon-1-4"
		combatState.enemy = InstanceHelper.createMonster(character)

		return combatState
	}

	public static CombatState getCombatState(Character character)
	{
		def combatState = ServerHelper.getCombatState(character)
		if (combatState == null)
		{
			combatState = createCombatState(character)
		}
		return combatState
	}

	public static Monster createMonster(Character character)
	{
		def monster = new Monster()
		monster.level = character.level
		monster.currentHealth = 100
		monster.instancePosition = "polygon-8-3"

		monster.slotRightHand = ItemGenerator.rollMonsterWeapon(monster)
		monster.lootBag.put("loot", ItemGenerator.rollItem(character))

		return monster
	}
}
