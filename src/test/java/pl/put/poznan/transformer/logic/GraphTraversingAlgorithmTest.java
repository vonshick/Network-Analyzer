package pl.put.poznan.transformer.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

class GraphTraversingAlgorithmTest {

    private GraphTraversingAlgorithm graphTraversingAlgorithm;
    private String validGraph;
    private String twoExits;
    private String twoEntries;
    private String noEntry;
    private String noExit;

    @BeforeEach
    void setUp() {
        graphTraversingAlgorithm = new GraphTraversingAlgorithm() {
            public Answer traverse() {
                return null;
            }
        };

        validGraph = "{\"nodes\":[{\"id\":0,\"name\":\"Wezel 0\"," +
                "\"type\":\"entry\",\"outgoing\":[{\"to\":1,\"from\":0,\"value\":1}],\"incoming\":[]},"
                + "{\"id\":1,\"name\":\"Wezel 1\"," +
                "\"type\":\"exit\",\"outgoing\":[],\"incoming\":[{\"to\":1,\"from\":0,\"value\":1}]}],"
                + "\"connections\":[{\"to\":1,\"from\":0,\"value\":1}]}";

        twoExits = "{\"nodes\":[{\"id\":0,\"name\":\"Wezel 0\"," +
                "\"type\":\"exit\",\"outgoing\":[{\"to\":1,\"from\":0,\"value\":1}],\"incoming\":[]},"
                + "{\"id\":1,\"name\":\"Wezel 1\"," +
                "\"type\":\"exit\",\"outgoing\":[],\"incoming\":[{\"to\":1,\"from\":0,\"value\":1}]}],"
                + "\"connections\":[{\"to\":1,\"from\":0,\"value\":1}]}";

        twoEntries = "{\"nodes\":[{\"id\":0,\"name\":\"Wezel 0\"," +
                "\"type\":\"entry\",\"outgoing\":[{\"to\":1,\"from\":0,\"value\":1}],\"incoming\":[]},"
                + "{\"id\":1,\"name\":\"Wezel 1\"," +
                "\"type\":\"entry\",\"outgoing\":[],\"incoming\":[{\"to\":1,\"from\":0,\"value\":1}]}],"
                + "\"connections\":[{\"to\":1,\"from\":0,\"value\":1}]}";

        noEntry = "{\"nodes\":[{\"id\":0,\"name\":\"Wezel 0\"," +
                "\"type\":\"regular\",\"outgoing\":[{\"to\":1,\"from\":0,\"value\":1}],\"incoming\":[]},"
                + "{\"id\":1,\"name\":\"Wezel 1\"," +
                "\"type\":\"entry\",\"outgoing\":[],\"incoming\":[{\"to\":1,\"from\":0,\"value\":1}]}],"
                + "\"connections\":[{\"to\":1,\"from\":0,\"value\":1}]}";

        noExit = "{\"nodes\":[{\"id\":0,\"name\":\"Wezel 0\"," +
                "\"type\":\"entry\",\"outgoing\":[{\"to\":1,\"from\":0,\"value\":1}],\"incoming\":[]},"
                + "{\"id\":1,\"name\":\"Wezel 1\"," +
                "\"type\":\"regular\",\"outgoing\":[],\"incoming\":[{\"to\":1,\"from\":0,\"value\":1}]}],"
                + "\"connections\":[{\"to\":1,\"from\":0,\"value\":1}]}";
    }

    @AfterEach
    void tearDown() {
        graphTraversingAlgorithm = null;
        validGraph = "";
    }

    @Test
    void testSetNetworkIfNetworkExists() {
        graphTraversingAlgorithm.setNetwork(validGraph);
        assertTrue(graphTraversingAlgorithm.network != null);
    }

    @Test
    void testSetNetworkEntryExists() {
        graphTraversingAlgorithm.setNetwork(validGraph);
        assertTrue(graphTraversingAlgorithm.entry == 0);
    }

    @Test
    void testSetNetworkExitExists() {
        graphTraversingAlgorithm.setNetwork(validGraph);
        assertTrue(graphTraversingAlgorithm.exit == 1);
    }

    @Test
    void testSetNetworkWrongInput() {
        try {
            graphTraversingAlgorithm.setNetwork("test");
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    void testCheckEntryAndExitWhenValidNetwork() {
        graphTraversingAlgorithm.setNetwork(validGraph);
        assertTrue(graphTraversingAlgorithm.checkEntryAndExit());
    }

    @Test
    void testCheckEntryAndExitWhenNetworkIsNull() {
        assertFalse(graphTraversingAlgorithm.checkEntryAndExit());
    }


    @Test
    void testCheckEntryAndExitWhenTwoExits() {
        graphTraversingAlgorithm.setNetwork(twoExits);
        assertFalse(graphTraversingAlgorithm.checkEntryAndExit());
    }

    @Test
    void testCheckEntryAndExitWhenTwoEntries() {
        graphTraversingAlgorithm.setNetwork(twoEntries);
        assertFalse(graphTraversingAlgorithm.checkEntryAndExit());
    }

    @Test
    void testCheckEntryAndExitWhenNoEntry() {
        graphTraversingAlgorithm.setNetwork(noEntry);
        assertFalse(graphTraversingAlgorithm.checkEntryAndExit());
    }

    @Test
    void testCheckEntryAndExitWhenNoExit() {
        graphTraversingAlgorithm.setNetwork(noExit);
        assertFalse(graphTraversingAlgorithm.checkEntryAndExit());
    }
}