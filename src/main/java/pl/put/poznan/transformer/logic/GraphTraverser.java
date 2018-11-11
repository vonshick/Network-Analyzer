package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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

        }

        answer = new Answer(lista,koszt);
    }

    /**
     *
     * @param d list of ints to find argmin in
     * @param q list that contains potential argmins
     * @return argmin of d ommiting every potentioal argmin that is not contained in q
     */
    private int argmin(ArrayList<Double> d, ArrayList<Integer> q){
         Double min = Double.MAX_VALUE;
         int argmin = -1;
         for(int i=0;i<d.size();i++){
             if(q.contains(i) && d.get(i)<min){

                 min=d.get(i);
                 logger.debug("min="+min);
                 logger.debug("argmin="+i);
                 argmin=i;
             }
         }
         return argmin;
    }

    /**
     * BFS traversal from a entry to exit.
     * Function finds an array of possible paths from entry to exit using BFS
     * and assigns the path with minimal value to answer
     */
    private void dijkstra(){
        //dystanse
        ArrayList<Double> d = new ArrayList<>();
        //poprzedniki
        ArrayList<Integer> p = new ArrayList<>();
        //kopia listy wierzchołków
        ArrayList<Integer> q = new ArrayList<>();
        //lista do odpwoiedzi generowana z p
        ArrayList<Integer> l = new ArrayList<>();


        int u;
        for(int i=0;i<incidenceList.size();i++){
            d.add(Double.MAX_VALUE);
            p.add(null);
            q.add(i);
        }
        d.set(entry,0.0);
        while(q.size()>0){
            int n = argmin(d,q);
            u=n;
            q.removeIf(s -> s == n);
            logger.debug("u="+u);
            for(Connection v:incidenceList.get(u)){
                if(d.get(v.getTo())>d.get(u)+v.getValue()){
                    d.set(v.getTo(),d.get(u)+v.getValue());
                    logger.debug(""+v.getTo()+" "+d.get(v.getTo()));
                    p.set(v.getTo(),u);
                }
            }
        }
        u=exit;
        l.add(exit);
        while(u!=entry){
            u=p.get(u);
            l.add(0,u);
        }
        answer = new Answer(l,d.get(exit));
    }

    private void wypiszlisteintow(ArrayList<Integer> l){
        String a="";
        for(Integer i:l){
            a+=""+i+" ";
        }
        logger.debug(a);
    }

    private boolean DFSrecursion(ArrayList<Integer> listaIndeksow,ArrayList<Integer> visited ){

        listaIndeksow.set(0,listaIndeksow.get(0)+1);

        if(listaIndeksow.size()==1){
            if(listaIndeksow.get(listaIndeksow.size()-1)<incidenceList.get(entry).size()) return false;
        }

        if(listaIndeksow.get(0)>=incidenceList.get(visited.get(0)).size()){
           listaIndeksow.remove(0);
           visited.remove(0);
           if(listaIndeksow.size()<1)   return false;
           return DFSrecursion(listaIndeksow,visited);
        }

        return true;
    }

    /**
     * DFS traversal from entry to exit.
     * Function finds an array of possible paths from entry to exit using DFS
     * and assigns the path with minmal value to answer
     */
    private void DFS(){
        ArrayList<Answer> listaOdpowiedzi = new ArrayList<>();
        ArrayList<Integer> visited = new ArrayList<>();
        ArrayList<Integer> listaIndeksow = new ArrayList<>();
        Double koszt = 0.0;

        visited.add(entry);
        listaIndeksow.add(0);

        boolean checkedAll = true;
        while(checkedAll){

            while(visited.get(0) != exit){

                int potencjalnyNowy = incidenceList.get(visited.get(0)).get(listaIndeksow.get(0)).getTo();
                if(!visited.contains(potencjalnyNowy)){
                    visited.add(0,potencjalnyNowy);
                    listaIndeksow.add(0,0);
                }
                else{
                    checkedAll = DFSrecursion(listaIndeksow,visited);
                }
            }
            if (checkedAll){
                Collections.reverse(visited);
                koszt=0.0;
                for(int i=0;i<visited.size()-2;i++){
                    for(Connection conn:incidenceList.get(visited.get(i))){
                        if(conn.getTo()==visited.get(i+1)){
                            koszt+=conn.getValue();
                            break;
                        }
                    }
                }
                listaOdpowiedzi.add(new Answer(new ArrayList<>(visited),koszt));
                Collections.reverse(visited);

                logger.debug("vis:");
                wypiszlisteintow(visited);


                visited.remove(0);
                listaIndeksow.remove(0);
                checkedAll = DFSrecursion(listaIndeksow,visited);
            }
        }

        answer = listaOdpowiedzi.get(0);
        logger.debug("Liczba znalezionych ścieżek: "+listaOdpowiedzi.size());
        for(Answer ans:listaOdpowiedzi){
            wypiszlisteintow(ans.getVisitedList());
        }
        for(Answer ans:listaOdpowiedzi){
            if(ans.getValue()<answer.getValue()) answer = ans;
        }
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
            dijkstra();
            logger.info("BFS algorithm ended");
        }
        else{
            if(requestedAlgorithm[0].equals("DFS")) {
                logger.info("Starting DFS algorithm");
                DFS();
                logger.info("DFS algorithm ended");
            }
            else{
                logger.info("Starting naive algorithm");
                naive();
                logger.info("Naive algorithm ended");
            }
        }

        try {
            String jsonInString = mapper.writeValueAsString(answer);
            logger.info("Sending output to client:");
            logger.info(jsonInString);
            return "["+jsonInString+"]";
        } catch (JsonProcessingException e) {
            logger.debug("Failed to map answer to Network class");
            return "[{\"lista\":[0,1,3,4,5,2,6,8,7,9],\"koszt\":-1.3}]";
        }
        
    }
}
