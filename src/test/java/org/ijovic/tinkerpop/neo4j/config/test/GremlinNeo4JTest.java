package org.ijovic.tinkerpop.neo4j.config.test;

import org.apache.tinkerpop.gremlin.jsr223.JavaTranslator;
import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * Created by ivanj on 25.06.2017.
 */
public class GremlinNeo4JTest {

    private Graph g;
    private static final String DB_PATH = "target/neo4j-embedded";

    @Before
    public void init(){
        g = Neo4jGraph.open(DB_PATH);
    }

    @Test
    public void testNeo4JWithGremlin(){

        Vertex v1 = g.addVertex("name", "ivan", "age", "28");
        Vertex v2 = g.addVertex("name", "david", "age", "22");
        Assert.assertTrue(IteratorUtils.count(g.vertices()) == 2);

        v1.addEdge("brother", v2);
        v2.addEdge("brother", v1);
        Assert.assertTrue(IteratorUtils.count(g.edges()) == 2);

        GraphTraversalSource gs = g.traversal();
        Vertex vResult;
        vResult = gs.V().has("name", "ivan").out("brother").next();
        assertNotNull(vResult);
        printVertexProperties(vResult);


        Neo4jGraph neoG = (Neo4jGraph) g;
        GraphTraversal<String, Map<String, Vertex> > cypherResult = neoG.cypher("MATCH (a {name:\"ivan\"}) RETURN a" );
        assertNotNull(cypherResult);
        for (Vertex v : cypherResult.next().values()){
            printVertexProperties(v);
        }
    }

    private void printVertexProperties(Vertex vResult) {
        System.out.println(vResult.toString());
        System.out.println(vResult.label());
        System.out.println(IteratorUtils.asList(vResult.properties()));
        System.out.println(IteratorUtils.asList(vResult.edges(Direction.BOTH)));
    }

}
