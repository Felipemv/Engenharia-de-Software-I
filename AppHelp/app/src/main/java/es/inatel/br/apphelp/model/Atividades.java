package es.inatel.br.apphelp.model;

import java.util.ArrayList;

/**
 * Created by felipe on 27/09/17.
 */

public class Atividades {

    private String inicio;
    private String fim;
    private String tipo;
    private String nome;
    private String tempo_mensal;

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTempo_mensal() {
        return tempo_mensal;
    }

    public void setTempo_mensal(String tempo_mensal) {
        this.tempo_mensal = tempo_mensal;
    }

}
