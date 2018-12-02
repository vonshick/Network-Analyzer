package pl.put.poznan.transformer.logic;

import java.util.ArrayList;

public class BFS extends GraphTraversingAlgorithm {

    public BFS() {
        super();
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
    @Override
    public Answer traverse() {
        //dystanse
        ArrayList<Double> d = new ArrayList<>();
        //poprzedniki
        ArrayList<Integer> p = new ArrayList<>();
        //kopia listy wierzchołków
        ArrayList<Integer> q = new ArrayList<>();
        //lista do odpwoiedzi generowana z p
        ArrayList<Integer> l = new ArrayList<>();


        int u;
        for(int i=0;i<network.getNodes().size();i++){
            d.add(Double.MAX_VALUE);
            p.add(null);
            q.add(i);
        }
        d.set(entry,0.0);
        while(q.size()>0){
            int n = argmin(d,q);
            u=n;
            q.removeIf(s -> s == n);
            for(Connection v:network.getNode(u).getOutgoing()){
                if(d.get(v.getTo())>d.get(u)+v.getValue()){
                    d.set(v.getTo(),d.get(u)+v.getValue());
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
        return new Answer(l,d.get(exit));
    }
}
