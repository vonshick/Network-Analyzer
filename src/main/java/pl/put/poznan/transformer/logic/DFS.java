package pl.put.poznan.transformer.logic;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;

/**
 * DFS is a child of GraphTraversingAlgorithm. It implements recurring DFS for finding the shortest path in a graph
 * @author      Artur Mostowski
 * @author      Dominik Szmyt
 * @author      Łukasz Grygier
 * @author      Jakub Wąsik
 * @since       1.0.2
 */
public class DFS extends GraphTraversingAlgorithm {
    public DFS() {
        super();
    }


    public DFS(Logger logger) {
        super(logger);
    }


    public boolean DFSrecursion(ArrayList<Integer> listaIndeksow, ArrayList<Integer> visited ){

        listaIndeksow.set(0,listaIndeksow.get(0)+1);

        if(listaIndeksow.size()==1){
            if(listaIndeksow.get(listaIndeksow.size()-1)<network.getNode(entry).getOutgoing().size()) return false;
        }

        if(listaIndeksow.get(0)>=network.getNode(visited.get(0)).getOutgoing().size()){
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
    @Override
    public Answer traverse() {

        if(!isGraphCorrect()) return new Answer(new ArrayList<>(),0.0);

        ArrayList<Answer> listaOdpowiedzi = new ArrayList<>();
        ArrayList<Integer> visited = new ArrayList<>();
        ArrayList<Integer> listaIndeksow = new ArrayList<>();
        Double koszt = 0.0;

        visited.add(entry);
        listaIndeksow.add(0);

        boolean checkedAll = true;
        while(checkedAll){

            while(visited.get(0) != exit){
                int potencjalnyNowy = network.getNode(visited.get(0)).getOutgoing().get(listaIndeksow.get(0)).getTo();
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
                for(int i=0;i<visited.size()-1;i++){
                    for(Connection conn:network.getNode(visited.get(i)).getOutgoing()){
                        if(conn.getTo()==visited.get(i+1)){
                            koszt+=conn.getValue();
                            break;
                        }
                    }
                }
                listaOdpowiedzi.add(new Answer(new ArrayList<>(visited),koszt));
                Collections.reverse(visited);


                visited.remove(0);
                listaIndeksow.remove(0);
                checkedAll = DFSrecursion(listaIndeksow,visited);
            }
        }

        Answer answer = listaOdpowiedzi.get(0);
//        logger.debug("Liczba znalezionych ścieżek: "+listaOdpowiedzi.size());
//        for(Answer ans:listaOdpowiedzi){
//            wypiszlisteintow(ans.getVisitedList());
//        }
        for(Answer ans:listaOdpowiedzi){
            if(ans.getValue()<answer.getValue()) answer = ans;
        }
        return answer;
    }
}
