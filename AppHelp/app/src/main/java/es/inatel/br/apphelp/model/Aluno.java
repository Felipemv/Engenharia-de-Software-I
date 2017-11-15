package es.inatel.br.apphelp.model;

import java.util.ArrayList;

/**
 * Created by felipe on 23/09/17.
 */

public class Aluno extends Usuario {
    private String curso;
    private String periodo;
    private int matricula;
    private ArrayList<Atividades> atividades;

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public ArrayList<Atividades> getAtividades() {
        return atividades;
    }

    public void setAtividades(ArrayList<Atividades> atividades) {
        this.atividades = atividades;
    }
}
