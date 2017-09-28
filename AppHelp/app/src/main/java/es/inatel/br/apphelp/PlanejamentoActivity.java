package es.inatel.br.apphelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlanejamentoActivity extends AppCompatActivity {

    private Button botaoSair;
    private Button botaoAddAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planejamento);

        botaoSair = (Button) findViewById(R.id.botaoSairID);
        botaoAddAtividade = (Button) findViewById(R.id.adicionarAtividadeID);

        botaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BancoDeDados().logout();
                Intent intentAbrirTelaCadastro = new Intent(PlanejamentoActivity.this, MainActivity.class);
                startActivity(intentAbrirTelaCadastro);
            }
        });

        botaoAddAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAbrirTelaCadastro = new Intent(PlanejamentoActivity.this, AdicionarAtividadeActivity.class);
                startActivity(intentAbrirTelaCadastro);
            }
        });
    }
}
