package turmoil.sandbox

import org.jgrapht.*
import org.jgrapht.graph.*
import org.jgrapht.alg.*

/**
 * Created by fox on 2014-08-06.
 */
class SandboxUnitTest extends GroovyTestCase
{
	public static boolean toOutput = true
	public static boolean toOutputFull = true

	def setup()
	{
	}

	def cleanup()
	{
	}

	void testGraph()
	{
		given:
			UndirectedGraph<String, DefaultEdge> g = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class)
		when:
			for (int i = 1; i <= 8; i++)
			{
				for (int j = 1; j <= 6; j++)
				{
					String vertex = new String("${i}-${j}")
					g.addVertex(vertex)
					if (j > 1)
					{
						String vertexAbove = new String("${i}-${j-1}")
						g.addEdge(vertexAbove, vertex)
					}
					if (i > 1)
					{
						String vertexBeside = new String("${i-1}-${j}")
						g.addEdge(vertexBeside, vertex)
					}
				}
			}
			def pathing = new KShortestPaths(g, "8-3", 2)
			def paths = pathing.getPaths("1-4")
		Graphs.getPathVertexList(paths.first())
		then:
			if (toOutput) println "paths: ${paths}"
			if (toOutput) println "first edge: ${paths.first().getEdgeList().first()}"
			if (toOutput) println "vertices ${Graphs.getPathVertexList(paths.first()).getAt(1)}"
			if (toOutput) println "first: ${paths.first()}"
			if (toOutputFull) println "graph: ${g}"
			assert true
	}
}
