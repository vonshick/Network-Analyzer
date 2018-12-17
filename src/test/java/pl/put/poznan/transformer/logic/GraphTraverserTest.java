package pl.put.poznan.transformer.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import pl.put.poznan.transformer.rest.GraphTraverserController;

import static org.junit.jupiter.api.Assertions.*;

class GraphTraverserTest {

    private GraphTraverser graphTraverser;
    private String graph;
    private String[] algorithm;

    @BeforeEach
    void setUp() {
        graph = "{\"nodes\":[{\"id\":0,\"name\":\"Wezel 0\"," +
                "\"type\":\"entry\",\"outgoing\":[{\"to\":1,\"from\":0,\"value\":1}],\"incoming\":[]},"
                + "{\"id\":1,\"name\":\"Wezel 1\"," +
                "\"type\":\"exit\",\"outgoing\":[],\"incoming\":[{\"to\":1,\"from\":0,\"value\":1}]}],"
                + "\"connections\":[{\"to\":1,\"from\":0,\"value\":1}]}";
        algorithm = new String[1];
    }

    @AfterEach
    void tearDown() {
        graph = null;
        algorithm = null;
        graphTraverser = null;
    }

    @Test
    void testGraphTraverserWithValidInput() {
        algorithm[0] = "BFS";
        graphTraverser = new GraphTraverser(algorithm, LoggerFactory.getLogger(GraphTraverserController.class));
        assertTrue(graphTraverser.getAlgorithm() != null);
    }

    @Test
    void testGraphTraverserWithInvalidInput() {
        algorithm[0] = "InvalidInput";
        graphTraverser = new GraphTraverser(algorithm, LoggerFactory.getLogger(GraphTraverserController.class));
        assertTrue(graphTraverser.getAlgorithm().getClass().equals(Naive.class));
    }

    @Test
    void testTransformWithInvalidInput() {
        algorithm[0] = "BFS";
        graphTraverser = new GraphTraverser(algorithm, LoggerFactory.getLogger(GraphTraverserController.class));
        try {
            graphTraverser.transform("InvalidInput");
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    void testTransformWithValidInput() {
        algorithm[0] = "BFS";
        graphTraverser = new GraphTraverser(algorithm, LoggerFactory.getLogger(GraphTraverserController.class));
        assertTrue(graphTraverser.transform(graph) != null);
    }
}