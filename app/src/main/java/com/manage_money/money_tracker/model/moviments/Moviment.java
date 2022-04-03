package com.manage_money.money_tracker.model.moviments;

import java.util.Date;

public class Moviment {
    int quantitat;
    String descripcio;
    Date data;
    TipusMoviment tipus;

    public Moviment(int quantitat, String descripcio, Date data, TipusMoviment tipus) {
        this.quantitat = quantitat;
        this.descripcio = descripcio;
        this.data = data;
        this.tipus = tipus;
    }

    public int getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(int quantitat) {
        this.quantitat = quantitat;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public TipusMoviment getTipus() {
        return tipus;
    }

    public void setTipus(TipusMoviment tipus) {
        this.tipus = tipus;
    }
}
