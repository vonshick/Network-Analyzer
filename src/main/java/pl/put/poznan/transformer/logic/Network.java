package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Network {
    @JsonProperty("connections")
    private ArrayList<Connection> connections;
    @JsonProperty("nodes")
    private ArrayList<Node> nodes;

    public Network() {
    }

    public Network(ArrayList<Connection> connections, ArrayList<Node> nodes) {
        this.connections = connections;
        this.nodes = nodes;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }
}
