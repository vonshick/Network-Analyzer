package pl.put.poznan.transformer.logic;

import java.util.ArrayList;

public class Odpowiedz {
    private ArrayList<Integer> lista;
    private Double koszt;

    public ArrayList<Integer> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Integer> lista) {
        this.lista = lista;
    }

    public Double getKoszt() {
        return koszt;
    }

    public void setKoszt(Double koszt) {
        this.koszt = koszt;
    }

    public Odpowiedz(ArrayList<Integer> lista, Double koszt) {
        this.lista = lista;
        this.koszt = koszt;
    }

    public Odpowiedz() {
    }
}
