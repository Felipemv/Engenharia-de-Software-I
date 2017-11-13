package es.inatel.br.apphelp.model;

import java.util.ArrayList;

/**
 * Created by felipe on 13/11/17.
 */

public class Datas {
    private String data;
    private ArrayList<Ponto> lisPontos;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ArrayList<Ponto> getLisPontos() {
        return lisPontos;
    }

    public void setLisPontos(ArrayList<Ponto> lisPontos) {
        this.lisPontos = lisPontos;
    }
}
