package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Network is a class representing a complete network. It contains all the connections and nodes appearing in a graph.
 * A singular network has only one node of type "entry" and one of type "exit".
 * @author      Artur Mostowski
 * @author      Dominik Szmyt
 * @author      Łukasz Grygier
 * @author      Jakub Wąsik
 * @see Connection
 * @see Node
 * @since       1.0
 */
public class Network {
    @JsonProperty("connections")
    private ArrayList<Connection> connections;
    @JsonProperty("nodes")
    private ArrayList<Node> nodes;

    public Network() {
    }

    /**
     * Class constructor, the parameters consist of all the connections existing in a network as well as all the nodes.
     * @param connections   list of all connections in the graph
     * @param nodes         list of all nodes in the network
     * @since               1.0
     */
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

    public Connection getConnection(int i) {
        return connections.get(i);
    }

    public Node getNode(int i) {
        return nodes.get(i);
    }
}
