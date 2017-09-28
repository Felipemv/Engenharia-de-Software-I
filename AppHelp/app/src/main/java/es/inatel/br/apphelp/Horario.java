package es.inatel.br.apphelp;

import java.util.Date;

/**
 * Created by felipe on 27/09/17.
 */

public class Horario {

    private String local;
    private Date data;
    private int hora;

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }
}
