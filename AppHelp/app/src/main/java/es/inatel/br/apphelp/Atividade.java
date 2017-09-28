package es.inatel.br.apphelp;

/**
 * Created by felipe on 27/09/17.
 */

public class Atividade {

    private String tipo;
    private String nome;
    private String tempo_mensal;
    private int numero_de_alunos;
    private Horario horario;
    private String codigo_disciplina;

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

    public int getNumero_de_alunos() {
        return numero_de_alunos;
    }

    public void setNumero_de_alunos(int numero_de_alunos) {
        this.numero_de_alunos = numero_de_alunos;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public String getCodigo_disciplina() {
        return codigo_disciplina;
    }

    public void setCodigo_disciplina(String codigo_disciplina) {
        this.codigo_disciplina = codigo_disciplina;
    }
}
