package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This is just an example to show that the logic should be outside the REST service.
 */
public class TextTransformer {

    private final String[] transforms;
    
    private Network network=null;

    private Odpowiedz odpowiedz = new Odpowiedz();

    private ArrayList<ArrayList<Double>> graph = null;
    
    private int entry;
    private int exit;

    private ArrayList<ArrayList<Connection>> incidenceList = null;

    public TextTransformer(String[] transforms){
        this.transforms = transforms;
    }

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

    private void tramsformToGraph(){
        if(network==null) return;
        graph=new ArrayList<ArrayList<Double>>();
        for (int i=0;i<network.getNodes().size();i++){
            graph.add(new ArrayList<>());
            for(int j=0;j<network.getNodes().size();j++){
                graph.get(i).add(0.0);
            }
        }

        network.getNodes().forEach(node -> {
            if(node.getType().equals("entry")) entry=node.getId();
            else{
                if(node.getType().equals("exit")) exit=node.getId();
            }
        });

        network.getConnections().forEach(conn-> graph.get(conn.getFrom()).set(conn.getTo(),conn.getValue()));

        for (int i=0;i<graph.size();i++){
            for(int j=0;j<graph.get(i).size();j++){
                System.out.print(graph.get(i).get(j));
                System.out.print(" ");
            }
            System.out.println();
        }

    }
    private Double lengthBetween(int from, int to){
        final Double[] w = new Double[1];
        network.getConnections().forEach(conn->{
            if(conn.getFrom()==from && conn.getTo()==to){
             w[0] = conn.getValue();
            }
        });
        return w[0];
    }
    
    /**
     * function to find the neighbor with minimum cost, that hasn't been
     * visited yet
     * @param dist
     * @param visited
     * @return 
     */
    private int minDistance(double dist[], Boolean visited[]) { 
        double min = Integer.MAX_VALUE;
        int min_index = -1; 
        for (int i = 0; i < incidenceList.size(); i++) {
            if (visited[i] == false && dist[i] <= min) { 
                min = dist[i]; 
                min_index = i; 
            } 
        } 
        return min_index; 
    } 
    
    private void greedy() {
        double dist[] = new double[incidenceList.size()];
        Boolean visited[] = new Boolean[incidenceList.size()];
        
        for (int i = 0; i < incidenceList.size(); i++) {
            dist[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }
        
        dist[entry] = 0;
        
        for (int  i = 0; i < incidenceList.size() - 1; i++) {
            int min = minDistance(dist, visited);
            visited[min] = true;
            
            for (int j = 0; j < incidenceList.get(min).size(); j++) {
                int neighbor = incidenceList.get(min).get(j).getTo();
                double valueOfNeighbor = incidenceList.get(min).get(j).getValue();
                
                if(!visited[neighbor] && dist[min] != Integer.MAX_VALUE 
                        && dist[min] +  valueOfNeighbor < dist[neighbor]) {
                    dist[neighbor] = dist[min] + valueOfNeighbor;
                }
            }
        }
    }
    /**
     * BFS traversal from a entry to exit.
     * Creates path from entry to exit as an array and the value of that path.
     */
    private void BFS(){
        //Assigning first vertex
        ArrayList<Connection> currentVertex = incidenceList.get(entry);
        
        //Creating visited array for algorithm
        boolean visited[] = new boolean[incidenceList.size()];
        
        //Creating queue list for visited vertices
        LinkedList<ArrayList<Connection>> queue 
                = new LinkedList<ArrayList<Connection>>();
        
        //Marking the first vertex as visited and adding it to queue
        visited[incidenceList.indexOf(currentVertex)] = true;
        queue.add(currentVertex);
        
        ArrayList<Integer> resultPath = new ArrayList<>();
        Double resultValue = 0.0;
        
        while (!queue.isEmpty()) {
            currentVertex = queue.poll();
            if(!resultPath.contains(incidenceList.indexOf(currentVertex))) {
                resultPath.add(incidenceList.indexOf(currentVertex));
            }
            for(int i = 0; i < currentVertex.size(); i++) {
                int neighbor = currentVertex.get(i).getTo();
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(incidenceList.get(neighbor));
                }
            }
        }
        
        
        
        odpowiedz.setLista(resultPath);
        odpowiedz.setKoszt(resultValue);
    }

    private void DFSrecursive(ArrayList<Connection> currentVertex, boolean visited[]) {
        visited[incidenceList.indexOf(currentVertex)] = true;
        
        for(int i = 0; i < currentVertex.size(); i++) {
                int n = currentVertex.get(i).getTo();
                if (!visited[n]) {
                    DFSrecursive(incidenceList.get(n), visited);
                }
            }
    }
    
    private void DFS(){
        ArrayList<Connection> currentVertex = incidenceList.get(entry);
        boolean visited[] = new boolean[incidenceList.size()];
        
        ArrayList<Integer> resultPath = new ArrayList<>();
        Double resultValue = 0.0;
        
        DFSrecursive(currentVertex, visited);
        
        odpowiedz.setLista(resultPath);
        odpowiedz.setKoszt(resultValue);
    }

    public String transform(String text){
        ObjectMapper mapper = new ObjectMapper();
        //TODO check if there is 1 entry and 1 exit, check if type is from {entry,exit,regular}
        try {
            network= mapper.readValue(text, Network.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (network != null) {
            //TODO error here
            System.out.println(network.getNodes().get(0).getName());
        }
        else{
            System.out.println("netowrk is null");
            return "BLAD";
        }

        tramsformToGraph();
        tramsformToIncidenceList();
        for(int i=0;i<incidenceList.size();i++){
            for (int j=0;j<incidenceList.get(i).size();j++){
                System.out.println("From "+i+", to "+incidenceList.get(i).get(j).getTo()+", value: "+incidenceList.get(i).get(j).getValue());
            }
        }
        if(transforms[0].equals("BFS")) BFS();
        else{
            if(transforms[0].equals("DFS")) DFS();
            else{
                greedy();
            }
        }
        try {
            String jsonInString = mapper.writeValueAsString(odpowiedz);
            System.out.println(jsonInString);
            return "["+jsonInString+"]";
        } catch (JsonProcessingException e) {

            return "[{\"lista\":[0,1,3,4,5,2,6,8,7,9],\"koszt\":-1.3}]";
        }
        
    }
}
