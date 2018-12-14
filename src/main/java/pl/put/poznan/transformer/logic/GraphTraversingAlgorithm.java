package pl.put.poznan.transformer.logic;

import org.slf4j.Logger;

public abstract class GraphTraversingAlgorithm {
    Network network;
    int entry=-1;
    int exit=-1;

    private static Logger logger;

    public void setNetwork(Network network) {
        this.network = network;

        network.getNodes().forEach(node->{
            if(node.getType().equals("entry")){
                entry=node.getId();
            }
            else if(node.getType().equals("exit")){
                exit=node.getId();
            }
        });
        logger.info("Entry: "+entry);
        logger.info("Exit: "+exit);
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
}
