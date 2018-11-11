package pl.put.poznan.transformer.logic;

import java.util.ArrayList;

/**
 * Answer is a class containing a list of nodes that the chosen method returned and a cost of traversing them all
 * @author      Artur Mostowski
 * @author      Dominik Szmyt
 * @author      Łukasz Grygier
 * @author      Jakub Wąsik
 * @since       1.0
 */
public class Answer {
    private ArrayList<Integer> visitedList;
    private Double cost;

    public ArrayList<Integer> getVisitedList() {
        return visitedList;
    }

    public void setVisitedList(ArrayList<Integer> visitedList) {
        this.visitedList = visitedList;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    /**
     * Class constructor
     * @param visitedList the list of consecutively visited nodes returned by the chosen algorithm
     * @param cost        the sum of costs required to travel via the chosen nodes
     * @since           1.0
     */
    public Answer(ArrayList<Integer> visitedList, Double cost) {
        this.visitedList = visitedList;
        this.cost = cost;
    }

    public Answer() {
    }
}
