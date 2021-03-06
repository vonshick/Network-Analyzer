package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * GraphTraversingAlgorithm is an abstract class that serves as a parent for all the implementations of graph
 * traversing algorithms. It includes methods checking whether the input is correct.
 * @author      Artur Mostowski
 * @author      Dominik Szmyt
 * @author      Łukasz Grygier
 * @author      Jakub Wąsik
 * @since       1.0.2
 */

public abstract class GraphTraversingAlgorithm {
    Network network;
    int entry=-1;
    int exit=-1;

    private static Logger logger;
    private static boolean graphCorrect;

    public boolean setNetwork(String networkJSON) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            network= mapper.readValue(networkJSON, Network.class);
        } catch (IOException e) {
            if (logger != null) {
                logger.debug("Failed to map input to Network class");
            }
            e.printStackTrace();
        }

        network.getNodes().forEach(node->{
            if(node.getType().equals("entry")){
                entry=node.getId();
            }
            else if(node.getType().equals("exit")){
                exit=node.getId();
            }
        });
        if (logger != null) {
            logger.info("Entry: "+entry);
            logger.info("Exit: "+exit);
        }
        graphCorrect = checkEntryAndExit() && checkintegrity();
        //to może być niepotrzebne
        for(int i=0;i<network.getNodes().size();i++){
            for (int j=0;j<network.getNode(i).getOutgoing().size();j++){
                logger.debug("From "+i+", to "+network.getNode(i).getOutgoing().get(j).getTo()+", value: "+network.getNode(i).getOutgoing().get(j).getValue());
            }
        }
        return graphCorrect;
    }

    public boolean isGraphCorrect(){
        return graphCorrect;
    }

    public GraphTraversingAlgorithm() {
    }

    public GraphTraversingAlgorithm(Logger logger) {
        this.logger=logger;

    }
    public abstract Answer traverse();

    /**
     * Function checking whether network contains one and only one node of type "entry" and "exit"
     * and seting entry and exit to their values
     * @return <code>true</code> if graph is correct <code>flase</code> if not
     */
    public boolean checkEntryAndExit(){
        if (network == null){
            if (logger != null) {
                logger.debug("Failed to map input to Network class");
            }
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
                    if (logger != null) {
                        logger.debug("Multiple entries in graph");
                    }
                    return false;
                }
            }
            else if(node.getType().equals("exit")) {
                if (!exitFound) {
                    exitFound = true;
                    exit = node.getId();
                } else {
                    if (logger != null) {
                        logger.debug("Multiple exits in graph");
                    }
                    return false;
                }
            }
        }
        if(!entryFound){
            if (logger != null) {
                logger.debug("No entry in graph");
            }
            return false;
        }
        if(!exitFound){
            if (logger != null) {
                logger.debug("No exit in graph");
            }
            return false;
        }
        return true;
    }

    public boolean checkintegrity() {
        if (network == null) {
            if (logger != null) logger.debug("Failed to map input to Network class");
            return false;
        }
        for(Node node:network.getNodes()){
            for(Connection i:node.getIncoming()){
                if(i.getTo()!=node.getId()){
                    if (logger != null) logger.debug("Mismatched incoming node for node "+ node.getId());

                    return false;
                }
                boolean correspondingExists=false;

                for (Connection o: network.getNode(i.getFrom()).getOutgoing()){
                    if(node.getId()==o.getTo()) {
                        if(i.getValue().equals(o.getValue())){
                            correspondingExists=true;
                        }
                        else{
                            if (logger != null) logger.debug("Mismatched incoming value to node "+ node.getId()+ " from node "+i.getFrom());
                        }
                    }
                }
                if(!correspondingExists){
                    if (logger != null) logger.debug("No corresponding outgoing exists for outgoing value from node "+ node.getId()+ " to node "+i.getFrom());
                    return false;
                }
            }
            for(Connection o:node.getOutgoing()){
                if(o.getFrom()!=node.getId()){
                    if (logger != null) logger.debug("Mismatched outgoing node for node "+ node.getId()+" given from: "+o.getFrom());
                    return false;
                }
                boolean correspondingExists=false;

                for (Connection i: network.getNode(o.getTo()).getIncoming()){
                    if(i.getFrom()==node.getId()) {
                        if(i.getValue().equals(o.getValue())){
                            correspondingExists=true;
                        }
                        else{
                            if (logger != null) logger.debug("Mismatched outgoing value from node "+ node.getId()+ " to node "+o.getTo());
                        }

                    }
                }
                if(!correspondingExists){
                    if (logger != null) logger.debug("No corresponding incoming exists for outgoing value from node "+ node.getId()+ " to node "+o.getTo());
                    return false;
                }
            }
        }
        return true;
    }
}
