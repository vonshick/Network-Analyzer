package pl.put.poznan.transformer.logic;

import org.slf4j.Logger;

import java.util.ArrayList;

public class Naive extends GraphTraversingAlgorithm{
    public Naive() {
        super();
    }

    public Naive(Logger logger) {
        super(logger);
    }

    /**
     * Algorithm traversing the network naively and setting answer to the path given by this algorithm
     */
    @Override
    public Answer traverse() {
        double koszt = 0.0;
        ArrayList<Integer> lista = new ArrayList<Integer>();
        int current = entry;

        lista.add(entry);

        while(current!=exit){
            double potencjalnyKoszt = 0.0;
            double min=-1.0;
            int minIndex = -1;
            boolean leadsToUnvisited = false;

            for (Connection conn:network.getNode(current).getOutgoing()){
                if(!lista.contains(conn.getTo())){
                    leadsToUnvisited = true;
                    break;
                }
            }

            for (Connection conn:network.getNode(current).getOutgoing()) {
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

        return new Answer(lista,koszt);
    }

}
