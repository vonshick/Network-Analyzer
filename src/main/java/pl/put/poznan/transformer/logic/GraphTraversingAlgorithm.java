package pl.put.poznan.transformer.logic;

public abstract class GraphTraversingAlgorithm {
    Network network;
    int entry;
    int exit;

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setEntry(int entry) {
        this.entry = entry;
    }

    public void setExit(int exit) {
        this.exit = exit;
    }

    public GraphTraversingAlgorithm() {
    }

    public abstract Answer traverse();
}
