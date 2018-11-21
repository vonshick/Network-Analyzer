package pl.put.poznan.transformer.logic;

import java.util.ArrayList;

/**
 * Answer is a class containing a list of nodes that the chosen method returned and a value of traversing them all
 * @author      Artur Mostowski
 * @author      Dominik Szmyt
 * @author      Łukasz Grygier
 * @author      Jakub Wąsik
 * @since       1.0
 */
public class Answer {
    private ArrayList<Integer> visitedList;
    private Double value;

    public ArrayList<Integer> getVisitedList() {
        return visitedList;
    }

    public void setVisitedList(ArrayList<Integer> visitedList) {
        this.visitedList = visitedList;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Class constructor
     * @param visitedList the list of consecutively visited nodes returned by the chosen algorithm
     * @param value        the sum of values required to travel via the chosen nodes
     * @since           1.0
     */
    public Answer(ArrayList<Integer> visitedList, Double value) {
        this.visitedList = visitedList;
        this.value = value;
    }

    public Answer() {
    }
}
