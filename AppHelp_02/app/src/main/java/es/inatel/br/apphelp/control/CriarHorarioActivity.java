package es.inatel.br.apphelp.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.model.Horarios;

public class CriarHorarioActivity extends AppCompatActivity {

    private Button voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_horario);
        referenciaComponentes();
        adicionarListeners();
    }

    private void adicionarListeners() {
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CriarHorarioActivity.this, "Retorno a Tela de Horario com sucesso!!", Toast.LENGTH_LONG).show();
                Intent proximaTela = new Intent(CriarHorarioActivity.this, HorariosActivity.class);
                startActivity(proximaTela);
            }
        });

    }
    private void referenciaComponentes() {

        voltar = (Button) findViewById(R.id.botaoVoltarID);

    }

}
