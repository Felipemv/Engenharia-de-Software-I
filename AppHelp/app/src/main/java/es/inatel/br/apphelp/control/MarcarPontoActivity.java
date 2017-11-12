package es.inatel.br.apphelp.control;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import es.inatel.br.apphelp.R;

public class MarcarPontoActivity extends AppCompatActivity {

    private Button botaoInicio;
    private Button botaoFim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_ponto);

        referenciaComponentes();
        adicionarListeners();

    }

    // Referencia os componentes da tela para serem usados
    public void referenciaComponentes(){
        botaoInicio = (Button) findViewById(R.id.botaoInicioID);
        botaoInicio.setBackgroundResource(R.color.Green);

        botaoFim = (Button) findViewById(R.id.botaoFimID);
        botaoFim.setBackgroundResource(R.color.Red);

    }

    //Adiciona Listeners aos botoes e demais componentes da tela
    public void adicionarListeners(){
        botaoInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MarcarPontoActivity.this, "Hora de entrada adicinada com sucesso!!", Toast.LENGTH_LONG).show();

            }
        });

        botaoFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MarcarPontoActivity.this, "Hora de saída adicinada com sucesso!!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
