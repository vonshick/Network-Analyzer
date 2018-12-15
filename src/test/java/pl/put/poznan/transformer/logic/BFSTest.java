package pl.put.poznan.transformer.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BFSTest {

    private BFS bfs;
    private String simpleGraph;
    private String oppositeConnection;

    @BeforeEach
    void setUp() {
        bfs = new BFS(null);
        simpleGraph = "{\"nodes\":[{\"id\":0,\"name\":\"Wezel 0\"," +
                "\"type\":\"entry\",\"outgoing\":[{\"to\":1,\"from\":0,\"value\":1}],\"incoming\":[]},"
                + "{\"id\":1,\"name\":\"Wezel 1\"," +
                "\"type\":\"exit\",\"outgoing\":[],\"incoming\":[{\"to\":1,\"from\":0,\"value\":1}]}],"
                + "\"connections\":[{\"to\":1,\"from\":0,\"value\":1}]}";

        oppositeConnection = "{\"nodes\":[{\"id\":0,\"name\":\"Wezel 0\"," +
                "\"type\":\"entry\",\"outgoing\":[{\"to\":0,\"from\":1,\"value\":1}],\"incoming\":[]},"
                + "{\"id\":1,\"name\":\"Wezel 1\"," +
                "\"type\":\"exit\",\"outgoing\":[],\"incoming\":[{\"to\":0,\"from\":1,\"value\":1}]}],"
                + "\"connections\":[{\"to\":1,\"from\":0,\"value\":1}]}";
    }

    @AfterEach
    void tearDown() {
        bfs = null;
        simpleGraph = null;
        oppositeConnection = null;
    }

    @Test
    void testTraverseSimpleGraph() {
        bfs.setNetwork(simpleGraph);
        assertTrue(bfs.traverse().getValue() == 1);
    }

    @Test
    void testTraverseOppositeConnection() {
        bfs.setNetwork(oppositeConnection);
        try {
            bfs.traverse().getValue();
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}