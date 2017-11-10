package es.inatel.br.apphelp.model;

/**
 * Created by felipe on 23/09/17.
 */

public class Administrador extends Usuario {
    private String ocupacao;
    private String atividadeResponsavel;


    public String getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(String ocupacao) {
        this.ocupacao = ocupacao;
    }

    public String getAtividadeResponsavel() {
        return atividadeResponsavel;
    }

    public void setAtividadeResponsavel(String atividadeResponsavel) {
        this.atividadeResponsavel = atividadeResponsavel;
    }
}
