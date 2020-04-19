package turmoil.sandbox

import org.jgrapht.*
import org.jgrapht.graph.*
import org.jgrapht.alg.*

class SandboxTest extends GroovyTestCase
{
	public static boolean toOutput = true
	public static boolean toOutputFull = true

	void testSandbox()
	{
		given:
			UndirectedGraph<String, DefaultEdge> g = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		when:
			String v1 = "v1";
			String v2 = "v2";
			String v3 = "v3";
			String v4 = "v4";
			String v5 = "v5";
			String v6 = "v6";

			// add the vertices
			g.addVertex(v1);
			g.addVertex(v2);
			g.addVertex(v3);
			g.addVertex(v4);
			g.addVertex(v5);
			g.addVertex(v6);

			// add edges to create a circuit
			g.addEdge(v1, v2);
			g.addEdge(v2, v3);
			g.addEdge(v3, v4);
			g.addEdge(v4, v1);
			g.addEdge(v4, v5);
			g.addEdge(v5, v6);

			def pathing = new KShortestPaths(g, "v1", 1)
			def paths = pathing.getPaths("v6")
		then:
			assert true

			// note undirected edges are printed as: {<v1>,<v2>}
			if (toOutput) println(g.toString());
			if (toOutput) println paths

			if (toOutput) println "nothing to do"
			if (toOutputFull) println "all fine"
	}
}