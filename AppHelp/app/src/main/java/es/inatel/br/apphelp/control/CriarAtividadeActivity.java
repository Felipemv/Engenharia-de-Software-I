package es.inatel.br.apphelp.control;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import es.inatel.br.apphelp.R;

public class CriarAtividadeActivity extends AppCompatActivity {

    private String itemTipo[] = {"Iniciação Científica", "Estágio", "Monitoria"};
    private String itemTempo[] = {"80 horas", "48 horas"};

    private ArrayAdapter<String> opcoesTipo;
    private ArrayAdapter<String> opcoesTempo;

    private Spinner spinnerTipo;
    private Spinner spinnerTempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_atividade);

        referenciarComponentes();
        adicionarListeners();
        criarSpinners();
    }

    private void adicionarListeners() {

    }

    private void referenciarComponentes() {
        spinnerTipo = (Spinner) findViewById(R.id.tipoID);
        spinnerTempo = (Spinner) findViewById(R.id.tempoID);
    }

    private void criarSpinners(){
        opcoesTipo = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itemTipo);
        spinnerTipo.setAdapter(opcoesTipo);

        opcoesTempo = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itemTempo);
        spinnerTempo.setAdapter(opcoesTempo);
    }
}
