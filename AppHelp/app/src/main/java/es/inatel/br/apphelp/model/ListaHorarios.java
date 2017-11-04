package es.inatel.br.apphelp.model;

import java.util.ArrayList;

/**
 * Created by felipe on 03/11/17.
 */

public class ListaHorarios {
    private String diaDaSemana;
    private ArrayList<Horarios> horarios;

    public String getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(String diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public ArrayList<Horarios> getHorarios() {
        return horarios;
    }

    public void setHorarios(ArrayList<Horarios> horarios) {
        this.horarios = horarios;
    }
}
