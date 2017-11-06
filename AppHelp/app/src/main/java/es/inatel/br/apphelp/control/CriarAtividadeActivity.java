package es.inatel.br.apphelp.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import es.inatel.br.apphelp.R;

public class CriarAtividadeActivity extends AppCompatActivity {

    private String itemTipo[] = {"Iniciação Científica", "Estágio", "Monitoria"};
    private String itemTempo[] = {"80 horas", "48 horas"};

    private Bundle bundle;
    private String tipoUsuario;

    private ArrayAdapter<String> opcoesTipo;
    private ArrayAdapter<String> opcoesTempo;

    private Spinner spinnerTipo;
    private Spinner spinnerTempo;

    private Button botaoVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_atividade);


        referenciarComponentes();
        adicionarListeners();
        criarSpinners();
    }

    //Adiciona Listeners aos botoes e demais componentes da tela
    private void adicionarListeners() {
        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CriarAtividadeActivity.this,"Retorno ao Menu com sucesso!!",Toast.LENGTH_LONG).show();
                Intent proximaTela = new Intent(CriarAtividadeActivity.this, MenuPrincipalActivity.class);
                startActivity(proximaTela);
                finish();
            }
        });
    }

    // Referencia os componentes da tela para serem usados
    private void referenciarComponentes() {
        botaoVoltar = (Button) findViewById(R.id.botaoVoltarIDCriarAtiv);

        spinnerTipo = (Spinner) findViewById(R.id.tipoID);
        spinnerTempo = (Spinner) findViewById(R.id.tempoID);
    }

    //Cria os spinners de opções
    private void criarSpinners(){
        opcoesTipo = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itemTipo);
        spinnerTipo.setAdapter(opcoesTipo);

        opcoesTempo = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itemTempo);
        spinnerTempo.setAdapter(opcoesTempo);
    }
}
