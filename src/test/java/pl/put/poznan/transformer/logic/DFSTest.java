package pl.put.poznan.transformer.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import pl.put.poznan.transformer.rest.GraphTraverserController;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DFSTest {

    private DFS dfs;
    private String simpleGraph;
    private String oppositeConnection;
    private String mediumGraph;
    private ArrayList mockIndexList;
    private ArrayList mockVisitedList;

    @BeforeEach
    void setUp() {
        dfs = new DFS(LoggerFactory.getLogger(GraphTraverserController.class));
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
        mediumGraph ="{\"nodes\":[{\"id\":0,\"name\":\"Wezel 0\",\"type\":\"entry\",\"outgoing\":[{\"to\":4,\"from\":0,\"value\":1}],\"incoming\":[]},{\"id\":1,\"name\":\"Wezel 1\",\"type\":\"regular\",\"outgoing\":[{\"to\":3,\"from\":1,\"value\":8}],\"incoming\":[{\"to\":1,\"from\":6,\"value\":5},{\"to\":1,\"from\":7,\"value\":1}]},{\"id\":2,\"name\":\"Wezel 2\",\"type\":\"regular\",\"outgoing\":[{\"to\":5,\"from\":2,\"value\":6},{\"to\":9,\"from\":2,\"value\":8}],\"incoming\":[{\"to\":2,\"from\":5,\"value\":9},{\"to\":2,\"from\":8,\"value\":8}]},{\"id\":3,\"name\":\"Wezel 3\",\"type\":\"regular\",\"outgoing\":[{\"to\":4,\"from\":3,\"value\":9},{\"to\":8,\"from\":3,\"value\":4}],\"incoming\":[{\"to\":3,\"from\":1,\"value\":8},{\"to\":3,\"from\":6,\"value\":3}]},{\"id\":4,\"name\":\"Wezel 4\",\"type\":\"regular\",\"outgoing\":[{\"to\":6,\"from\":4,\"value\":9}],\"incoming\":[{\"to\":4,\"from\":0,\"value\":1},{\"to\":4,\"from\":3,\"value\":9}]},{\"id\":5,\"name\":\"Wezel 5\",\"type\":\"regular\",\"outgoing\":[{\"to\":2,\"from\":5,\"value\":9},{\"to\":7,\"from\":5,\"value\":5}],\"incoming\":[{\"to\":5,\"from\":2,\"value\":6}]},{\"id\":6,\"name\":\"Wezel 6\",\"type\":\"regular\",\"outgoing\":[{\"to\":1,\"from\":6,\"value\":5},{\"to\":3,\"from\":6,\"value\":3},{\"to\":8,\"from\":6,\"value\":7}],\"incoming\":[{\"to\":6,\"from\":4,\"value\":9}]},{\"id\":7,\"name\":\"Wezel 7\",\"type\":\"regular\",\"outgoing\":[{\"to\":1,\"from\":7,\"value\":1},{\"to\":9,\"from\":7,\"value\":2}],\"incoming\":[{\"to\":7,\"from\":5,\"value\":5},{\"to\":7,\"from\":9,\"value\":7}]},{\"id\":8,\"name\":\"Wezel 8\",\"type\":\"regular\",\"outgoing\":[{\"to\":2,\"from\":8,\"value\":8}],\"incoming\":[{\"to\":8,\"from\":3,\"value\":4},{\"to\":8,\"from\":6,\"value\":7}]},{\"id\":9,\"name\":\"Wezel 9\",\"type\":\"exit\",\"outgoing\":[{\"to\":7,\"from\":9,\"value\":7}],\"incoming\":[{\"to\":9,\"from\":2,\"value\":8},{\"to\":9,\"from\":7,\"value\":2}]}],\"connections\":[{\"to\":4,\"from\":0,\"value\":1},{\"to\":3,\"from\":1,\"value\":8},{\"to\":5,\"from\":2,\"value\":6},{\"to\":9,\"from\":2,\"value\":8},{\"to\":4,\"from\":3,\"value\":9},{\"to\":8,\"from\":3,\"value\":4},{\"to\":6,\"from\":4,\"value\":9},{\"to\":2,\"from\":5,\"value\":9},{\"to\":7,\"from\":5,\"value\":5},{\"to\":1,\"from\":6,\"value\":5},{\"to\":3,\"from\":6,\"value\":3},{\"to\":8,\"from\":6,\"value\":7},{\"to\":1,\"from\":7,\"value\":1},{\"to\":9,\"from\":7,\"value\":2},{\"to\":2,\"from\":8,\"value\":8},{\"to\":7,\"from\":9,\"value\":7}]}";
        mockIndexList = mock(ArrayList.class);
        mockVisitedList = mock(ArrayList.class);
    }

    @AfterEach
    void tearDown() {
        dfs = null;
        simpleGraph = null;
        oppositeConnection = null;
        mockIndexList = null;
        mockVisitedList = null;
    }

    @Test
    void testTraverseSimpleGraph() {
        assertTrue(dfs.setNetwork(simpleGraph));
        assertEquals((Double)1.0,dfs.traverse().getValue());
    }

    @Test
    void testTraverseOppositeConnection() {
        dfs.setNetwork(oppositeConnection);
        try {
            dfs.traverse().getValue();
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testMediumGraph(){
        assertTrue(dfs.setNetwork(mediumGraph));
        ArrayList<Integer> lista = new ArrayList<>();
        lista.add(0);
        lista.add(4);
        lista.add(6);
        lista.add(3);
        lista.add(8);
        lista.add(2);
        lista.add(9);
        assertEquals(dfs.traverse().getVisitedList(),lista);
        assertEquals(dfs.traverse().getValue(),(Double) 33.0);
    }

    @Test
    void testSimpleMocksInDFSRecursion() {
        when(mockIndexList.get(0)).thenReturn(0);
        when(mockIndexList.set(0, 1)).thenReturn(true);
        when(mockIndexList.size()).thenReturn(0);
        when(mockIndexList.remove(0)).thenReturn(true);
        when(mockVisitedList.get(0)).thenReturn(0);
        when(mockVisitedList.remove(0)).thenReturn(true);

        dfs.setNetwork(simpleGraph);
        dfs.DFSrecursion(mockIndexList, mockVisitedList);
        verify(mockIndexList, times(2)).get(0);
        verify(mockIndexList, times(1)).size();
        verify(mockIndexList, times(1)).set(0, 1);
    }
}