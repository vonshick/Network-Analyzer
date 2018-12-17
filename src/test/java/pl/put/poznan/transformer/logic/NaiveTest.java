package pl.put.poznan.transformer.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class NaiveTest {

    private Naive naive;
    private String simpleGraph;
    private String oppositeConnection;

    @BeforeEach
    void setUp() {
        naive = new Naive(null);
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
        naive = null;
        simpleGraph = null;
        oppositeConnection = null;
    }

    @Test
    void testTraverseSimpleGraph() {
        naive.setNetwork(simpleGraph);
        assertTrue(naive.traverse().getValue() == 1);
    }

    @Test
    public void testTraverseOppositeConnection() {
        naive.setNetwork(oppositeConnection);
        try {
            assertTimeoutPreemptively(Duration.ofMillis(1000), () -> {
                naive.traverse();
            });
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}