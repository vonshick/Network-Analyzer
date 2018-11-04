package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This is just an example to show that the logic should be outside the REST service.
 */
public class TextTransformer {

    private final String[] transforms;
    
    private Network network=null;

    private Odpowiedz odpowiedz = new Odpowiedz();

    private ArrayList<ArrayList<Double>> graph = null;
    
    private Integer V;
    private LinkedList<Integer> coolerGraph[];
    
    private int entry;
    private int exit;
    
    public TextTransformer(String[] transforms){
        this.transforms = transforms;
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
    private void naive() {
     ArrayList<Integer> lista = new ArrayList<Integer>();
     Double koszt=8.8;
     for (int i=0;i<8*2;i++){
         lista.add(i);
     }
     odpowiedz.setLista(lista);
     odpowiedz.setKoszt(koszt);
    }

    private void naive2(){
        int current=entry;
        ArrayList<Integer> lista= new ArrayList<Integer>();
        lista.add(entry);
        Double koszt=0.0;
        while(true){
            Double min=0.0;
            int minIndex=-1;
            for (int i=0;i<graph.get(current).size();i++){
                if(lista.contains(i)) continue;
                if(minIndex==-1||min>graph.get(current).get(i)){
                    minIndex=i;
                    min=graph.get(current).get(i);
                }
            }
            koszt+=min;
            current=minIndex;
            lista.add(current);
            if(current==exit) break;

        }
        odpowiedz.setLista(lista);
        odpowiedz.setKoszt(koszt);

    }

    private void BFS(){
        int currentVertex;
        boolean visited[] = new boolean[V];
        LinkedList<Integer> queue = new LinkedList<Integer>();
        visited[entry] = true;
        queue.add(entry);
        
        while (!queue.isEmpty()) {
            currentVertex =  queue.poll();
            Iterator<Integer> i = coolerGraph[currentVertex].listIterator();
            while(i.hasNext()) {
                int n = i.next();
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
    }

    private void DFSrecursive(int currentVertex, boolean visited[]) {
        visited[currentVertex] = true;
        
        Iterator<Integer> i = coolerGraph[currentVertex].listIterator();
        while(i.hasNext()) {
            int n = i.next();
            if (!visited[n]) {
                DFSrecursive(n, visited);
            }
        }
    }
    
    private void DFS(){
        int currentVertex = entry;
        boolean visited[] = new boolean[V];
        
        DFSrecursive(currentVertex, visited);
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
        if(transforms[0].equals("BFS")) BFS();
        else{
            if(transforms[0].equals("DFS")) DFS();
            else{
                naive();
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
