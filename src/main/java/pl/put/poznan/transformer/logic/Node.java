package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

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
