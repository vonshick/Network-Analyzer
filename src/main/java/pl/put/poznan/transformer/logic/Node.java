package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Node is a class representing an individual node of the network. Each node has an ID, a name (optional),
 *  a type ("entry", "exit", or "regulat"), a list of outcoming connections, and a list of outgoing connections.
 * @author      Artur Mostowski
 * @author      Dominik Szmyt
 * @author      Łukasz Grygier
 * @author      Jakub Wąsik
 * @since       1.0
 */
public class Node {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("outgoing")
    private ArrayList<Integer> outgoing;
    @JsonProperty("incoming")
    private ArrayList<Integer> incoming;

    /**
     * Class constructor
     * @param id            numeric ID of the node
     * @param name          optional human-friendly name of the node
     * @param type          type of the node, it must be either "entry", "exit" or "regular"
     * @param outgoing      list of all the connections originating in the node
     * @param incoming      list of all the connections ending in the node
     * @since               1.0
     */
    public Node(int id, String name, String type, ArrayList outgoing, ArrayList incoming) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.outgoing = outgoing;
        this.incoming = incoming;
    }

    public Node() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Integer> getOutgoing() {
        return outgoing;
    }

    public ArrayList<Integer> getIncoming() {
        return incoming;
    }
}
