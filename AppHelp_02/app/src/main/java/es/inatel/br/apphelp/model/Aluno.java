package es.inatel.br.apphelp.model;

/**
 * Created by felipe on 23/09/17.
 */

public class Aluno extends Usuario {
    private String curso;
    private int periodo;
    private int matricula;

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
}
