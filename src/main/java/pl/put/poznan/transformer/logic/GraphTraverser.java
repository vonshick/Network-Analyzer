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

    private Network network=null;


    /**
     *
     */
    private GraphTraversingAlgorithm algorithm;

    /**
     * Class constructor
     *
     * @param requestedAlgorithm    The algorithm chosen by the client ("naive", "BFS", or "DFS")
     * @param logger                Logger used to output Debug or Info messages
     */
    public GraphTraverser(String[] requestedAlgorithm, Logger logger){
        this.logger=logger;
        if(requestedAlgorithm[0].equals("BFS")) {
            algorithm = new BFS(logger);
        }
        else{
            if(requestedAlgorithm[0].equals("DFS")) {
                algorithm = new DFS(logger);
            }
            else{
                algorithm = new Naive(logger);
            }
        }
    }

    /**
     * Main function of the class,
     * it parses JSON input sent by the client,
     * checks whether it's correct,
     * calls traversing function
     * and returns the effects of the called function
     *
     * @param text - JSON input
     * @return JSON containing an object of class Answer with visitedList set to the list of nodes given by the desired algorithm
     * and cost set to the cost of traversing the graph via these nodes in this order
     */

    public String transform(String text){


        //to może być niepotrzebne
//        for(int i=0;i<network.getNodes().size();i++){
//            for (int j=0;j<network.getNode(i).getOutgoing().size();j++){
//                logger.debug("From "+i+", to "+network.getNode(i).getOutgoing().get(j).getTo()+", value: "+network.getNode(i).getOutgoing().get(j).getValue());
//            }
//        }
        algorithm.setNetwork(text);
        if(!algorithm.checkEntryAndExit()){
            return "[{\"lista\":[0,1,3,4,5,2,6,8,7,9],\"koszt\":-1.3}]";
        }
        Answer answer = algorithm.traverse();

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(answer);
            if(logger != null) {
                logger.info("Sending output to client:");
                logger.info(jsonInString);
            }
            return "["+jsonInString+"]";
        } catch (JsonProcessingException e) {
            if (logger != null) {
                logger.debug("Failed to map answer to Network class");
            }
            return "[{\"lista\":[0,1,3,4,5,2,6,8,7,9],\"koszt\":-1.3}]";
        }
        
    }
}
