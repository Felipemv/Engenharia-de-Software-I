package es.inatel.br.apphelp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private Button botaoLogin;
    private Button botaoCadastrar;
    private TextView usuario;
    private TextView senha;
    private TextView loginErrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botaoLogin = (Button) findViewById(R.id.botaoLogarID);
        botaoCadastrar = (Button) findViewById(R.id.botaoCadastrarID);
        usuario = (TextView) findViewById(R.id.usuarioId);
        senha = (TextView) findViewById(R.id.senhaId);
        loginErrado = (TextView) findViewById(R.id.loginErradoID);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAbrirTelaCadastro = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(intentAbrirTelaCadastro);
            }
        });

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usuario.equals("")){
                    loginErrado.setVisibility(View.VISIBLE);
                }else{
                    loginErrado.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}

