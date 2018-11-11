package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Graphtraverser is a class storing and traversing the graph sent to the server by the client
 * @author      Artur Mostowski
 * @author      Dominik Szmyt
 * @author      Łukasz Grygier
 * @author      Jakub Wąsik
 * @since       1.0
 */
public class GraphTraverser {

    private static Logger logger;
    private final String[] requestedAlgorithm;
    
    private Network network=null;

    private Answer answer = new Answer();

    private int entry;
    private int exit;

    private ArrayList<ArrayList<Connection>> incidenceList = null;
    
    private ArrayList<ArrayList<Integer>> arrayOfPaths = null;
    private ArrayList<Integer> resultPath = null;
    private Double resultValue = Double.MAX_VALUE;


    /**
    *Debug class, should be deleted in the final version
     */
    public void setGraph () {
        incidenceList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            incidenceList.set(i, new ArrayList<>());
        }
        incidenceList.get(1).add(new Connection(1,2,2.0));
        incidenceList.get(1).add(new Connection(1,3,1.0));
        
        incidenceList.get(2).add(new Connection(2,3,5.0));
        incidenceList.get(2).add(new Connection(2,4,1.0));
        incidenceList.get(2).add(new Connection(2,5,2.0));
        
        incidenceList.get(3).add(new Connection(3,5,3.0));
        
        incidenceList.get(4).add(new Connection(4,6,5.0));
        
        incidenceList.get(5).add(new Connection(5,4,5.0));
        
        incidenceList.get(6).add(new Connection(6,5,4.0));
    }

    /**
     * Class constructor
     *
     * @param requestedAlgorithm    The algorithm chosen by the client ("naive", "BFS", or "DFS")
     * @param logger                Logger used to output Debug or Info messages
     */
    public GraphTraverser(String[] requestedAlgorithm, Logger logger){
        this.logger=logger;
        this.requestedAlgorithm = requestedAlgorithm;
    }

    /**
     * Method parsing the network into Incidence List that is a list of lists of Connectons
     * @see Connection
     */
     private void tramsformToIncidenceList(){
        incidenceList = new ArrayList<ArrayList<Connection>>();
        network.getNodes().forEach(e->{
                    incidenceList.add(new ArrayList<Connection>());
                }
                );
        network.getConnections().forEach(conn-> {
            incidenceList.get(conn.getFrom()).add(conn);
        });
    }
    
    /**
     * This function finds path with minimal value and asigns it to answer
     */
    private void finalResults() {
        //We are going to search for a path that has minimal value
        for (int pathId = 0; pathId < arrayOfPaths.size(); pathId++) {
            ArrayList<Integer> path = arrayOfPaths.get(pathId);
            double tempValue = 0.0;
            
            //We are going to add up values of connections in our path
            for (int nodeId = 0; nodeId < path.size() - 1; nodeId++) {
                int nextNode = nodeId + 1;
                for (Connection conn : incidenceList.get(nodeId)) {
                    if (conn.getTo() == nextNode) {
                        tempValue += conn.getValue();
                    }
                }
            }
            
            //Assigning final result if minimal
            if (tempValue < resultValue) {
                resultValue = tempValue;
                resultPath.clear();
                resultPath = arrayOfPaths.get(pathId);
            }
        }
        
        answer.setVisitedList(resultPath);
        answer.setCost(resultValue);
        resultValue = Double.MAX_VALUE;
        resultPath.clear();
        arrayOfPaths.clear();
    }
    
    /**
     * function that finds the neighbor with minimum cost, that hasn't been
     * visited yet
     * @param dist
     * @param visited
     * @return 
     */
    private int minDistance(double dist[], ArrayList<Boolean[]> visited) 
    { 
        double min = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < incidenceList.size(); i++) {
            for (int j = 0; j < incidenceList.get(i).size(); j++) {
                if (!visited.get(i)[j] && dist[i] <= min) { 
                    min = dist[i];           
                    minIndex = i;
                } 
            }
        }
        return minIndex; 
    }

    /**
     * Algorithm traversing the network naively and setting answer to the path given by this algorithm
     */
     private void naive(){
        double koszt = 0.0;
        ArrayList<Integer> lista = new ArrayList<Integer>();
        int current = entry;

        lista.add(entry);

        while(current!=exit){
            double potencjalnyKoszt = 0.0;
            double min=-1.0;
            int minIndex = -1;
            boolean leadsToUnvisited = false;

            for (Connection conn:incidenceList.get(current)){
                if(!lista.contains(conn.getTo())){
                    leadsToUnvisited = true;
                    break;
                }
            }

            for (Connection conn:incidenceList.get(current)) {
                if ((minIndex == -1 || min > conn.getValue()) && (!leadsToUnvisited || !lista.contains(conn.getTo()))) {
                    min = conn.getValue();
                    minIndex = conn.getTo();
                    potencjalnyKoszt = conn.getValue();
                }
            }
            current = minIndex;

            lista.add(minIndex);
            koszt+=potencjalnyKoszt;
            System.out.println(current);

        }

        answer = new Answer(lista,koszt);
    }

    private void greedy() {
        double dist[] = new double[incidenceList.size()];
        ArrayList<Boolean[]> visited = new ArrayList<>();
        
        for (int i = 0; i < incidenceList.size(); i++) {
            visited.set(i, new Boolean[incidenceList.get(i).size()]);
        }
        
        for (int i = 0; i < incidenceList.size(); i++) {
            dist[i] = Integer.MAX_VALUE;
            for (int j = 0; j < incidenceList.get(i).size(); j++) {
                visited.get(i)[j] = false;
            }
        }
        int neighbor = -1;
        dist[entry] = 0;
               
        for (int  i = 0; i < incidenceList.size() - 1; i++) {
            
            int min = minDistance(dist, visited);
            if(min == neighbor) {
                visited.get(min)[neighbor] = true;
                resultPath.add(min);
                if (min == exit) {
                    resultValue = dist[min];
                    break;
                }
            }
            
            for (int j = 0; j < incidenceList.get(min).size(); j++) {
                
                neighbor = incidenceList.get(min).get(j).getTo();
                double valueOfNeighbor = incidenceList.get(min).get(j).getValue();
                
                if(!visited.get(min)[neighbor] && dist[min] != Integer.MAX_VALUE 
                        && dist[min] +  valueOfNeighbor < dist[neighbor]) {
                    
                    dist[neighbor] = dist[min] + valueOfNeighbor;
                }
            }
        }
        
        answer.setCost(resultValue);
        answer.setVisitedList(resultPath);
    }
    
    /**
     * BFS traversal from a entry to exit.
     * Function finds an array of possible paths from entry to exit using BFS
     * and assigns the path with minmal value to answer
     */
    private void BFS(){
        
        ArrayList<Connection> currentVertex = incidenceList.get(entry);
        LinkedList<ArrayList<Connection>> previousVertecies 
                = new LinkedList<ArrayList<Connection>>();
        LinkedList<ArrayList<Connection>> queue 
                = new LinkedList<ArrayList<Connection>>();
        queue.add(currentVertex);
        resultPath = new ArrayList<>();
        
        //Starting algorithm
        while (!queue.isEmpty()) {
            
            currentVertex = queue.element();
            resultPath.add(incidenceList.indexOf(currentVertex));
            
            if(incidenceList.indexOf(currentVertex) == exit) {
                arrayOfPaths.add(resultPath);
                
                //When we find the exit we no longer need to search through
                //the graph so we remove the current node from queue
                queue.remove();
                resultPath.remove(incidenceList.indexOf(currentVertex));
                currentVertex = queue.element();
                
                //If the current head of queue is our previous node,
                //we are going to remove it as, because that node has been checked
                while(incidenceList.indexOf(currentVertex) == 
                        incidenceList.indexOf(previousVertecies.element())) {
                    queue.remove();
                    resultPath.remove(incidenceList.indexOf(currentVertex));
                    previousVertecies.remove();
                    currentVertex = queue.element();
                }
                resultPath.add(incidenceList.indexOf(currentVertex));
            }
            
            //We are adding neighbours to queue
            for(int i = 0; i < currentVertex.size(); i++) {
                int neighbor = currentVertex.get(i).getTo();
                queue.add(incidenceList.get(neighbor));
            }
            //We are going to remember our currentVertex to track down our path
            previousVertecies.add(currentVertex);
        }
        
        finalResults();
    }
    
    /**
     * DFS traversal from entry to exit
     * @param currentVertex 
     */
    private void DFSrecursive(ArrayList<Connection> currentVertex) {
        
        //We are adding current node to result path
        resultPath.add(incidenceList.indexOf(currentVertex));
        
        //and if this our destination we are adding that path to an arrayOfPaths
        if(incidenceList.indexOf(currentVertex) == exit) {
            arrayOfPaths.add(resultPath);
            return;
        }
        
        for(int i = 0; i < currentVertex.size(); i++) {
            int n = currentVertex.get(i).getTo();
            DFSrecursive(incidenceList.get(n));
            
            //We are deleting checked node from resultPath because
            //[1] it was probably our exit and we already have saved that path
            //[2] or it was dead end
            resultPath.remove(n);
        }
    }
    
    /**
     * DFS traversal from entry to exit.
     * Function finds an array of possible paths from entry to exit using DFS
     * and assigns the path with minmal value to answer
     */
    private void DFS(){
        ArrayList<Connection> currentVertex = incidenceList.get(entry);        
        resultPath = new ArrayList<>();
        arrayOfPaths = new ArrayList<>();
        for (ArrayList<Integer> path : arrayOfPaths) {
            path = new ArrayList<>();
        }
        
        //Starting algorithm
        DFSrecursive(currentVertex);
        finalResults();        
    }

    /**
     * Function checking whether network contains one and only one node of type "entry" and "exit"
     * and seting entry and exit to their values
     * @return <code>true</code> if graph is correct <code>flase</code> if not
     */
    private boolean checkEntryAndExit(){
        if (network == null){
            logger.debug("Failed to map input to Network class");
            return false;
        }

        //checkijng if only one entry and only one exit
        boolean entryFound = false, exitFound = false;

        for(Node node: network.getNodes()){
            if(node.getType().equals("entry")){
                if(!entryFound) {
                    entryFound = true;
                    entry = node.getId();
                }
                else{
                    logger.debug("Multiple entries in graph");
                    return false;
                }
            }
            else if(node.getType().equals("exit")) {
                if (!exitFound) {
                    exitFound = true;
                    exit = node.getId();
                } else {
                    logger.debug("Multiple exits in graph");
                    return false;
                }
            }
        }
        if(!entryFound){
            logger.debug("No entry in graph");
            return false;
        }
        if(!exitFound){
            logger.debug("No exit in graph");
            return false;
        }
        return true;
    }

    /**
     * Main function of the class,
     * it parses JSON input sent by the client,
     * checks whether it's correct,
     * calls traversing function
     * and returns the effects of the called function
     *  
     * @return JSON containing an object of class Answer with visitedList set to the list of nodes given by the desired algorithm
     * and cost set to the cost of traversing the graph via these nodes in this order
     */
    public String transform(String text){
        ObjectMapper mapper = new ObjectMapper();
        try {
            network= mapper.readValue(text, Network.class);
        } catch (IOException e) {
            logger.debug("Failed to map input to Network class");
            e.printStackTrace();
        }

        if(!checkEntryAndExit()){
            return "BLAD";
        }

        logger.info("Entry: "+entry);
        logger.info("Exit: "+exit);

        logger.info("Transforming graph to incidence list");
        tramsformToIncidenceList();

        //to może być niepotrzebne
        for(int i=0;i<incidenceList.size();i++){
            for (int j=0;j<incidenceList.get(i).size();j++){
                logger.debug("From "+i+", to "+incidenceList.get(i).get(j).getTo()+", value: "+incidenceList.get(i).get(j).getValue());
            }
        }

        if(requestedAlgorithm[0].equals("BFS")) {
            logger.info("Starting BFS algorithm");
            naive();
        }
        else{
            if(requestedAlgorithm[0].equals("DFS")) {
                logger.info("Starting DFS algorithm");
                naive();
            }
            else{
                logger.info("Starting naive algorithm");
                naive();
            }
        }

        try {
            String jsonInString = mapper.writeValueAsString(answer);
            System.out.println(jsonInString);
            return "["+jsonInString+"]";
        } catch (JsonProcessingException e) {
            logger.debug("Failed to map answer to Network class");
            return "[{\"lista\":[0,1,3,4,5,2,6,8,7,9],\"koszt\":-1.3}]";
        }
        
    }
}
