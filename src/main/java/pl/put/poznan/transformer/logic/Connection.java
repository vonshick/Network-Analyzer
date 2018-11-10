package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Connection {
    @JsonProperty("from")
    private int from;
    @JsonProperty("to")
    private int to;
    @JsonProperty("value")
    private Double value;


    public Connection() {
    }

    public Connection(int from, int to, Double value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public Double getValue() {
        return value;
    }
}
