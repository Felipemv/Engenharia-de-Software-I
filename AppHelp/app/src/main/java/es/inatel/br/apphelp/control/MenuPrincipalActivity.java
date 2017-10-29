package es.inatel.br.apphelp.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.inatel.br.apphelp.R;

public class MenuPrincipalActivity extends AppCompatActivity {

    private String tipoUsuario;

    private Bundle bundle;

    private Button menuPerfil;
    private Button menuHorario;
    private Button menuAtividade;
    private Button menuSair;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        bundle = getIntent().getExtras();
        if(bundle.containsKey("tipoUsuario")){
            tipoUsuario = bundle.getString("tipoUsuario");
        }
        referenciaComponentes();
        adicionarListeners();
    }



    public void referenciaComponentes(){
        menuPerfil =        (Button) findViewById(R.id.menuPerfilID);
        menuHorario =       (Button) findViewById(R.id.menuHorarioID);
        menuAtividade =     (Button) findViewById(R.id.menuAtividadeID);
        menuSair =          (Button) findViewById(R.id.menuSairID);
    }

    private void adicionarListeners() {

        menuPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaTela = new Intent(MenuPrincipalActivity.this, PerfilActivity.class);
                proximaTela.putExtra("tipoUsuario", tipoUsuario);
                startActivity(proximaTela);
                finish();
            }
        });

        menuHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent proximaTela = new Intent(MenuPrincipalActivity.this, HorarioActivity.class);
                //startActivity(proximaTela);
                //finish();
            }
        });

        menuAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaTela = new Intent(MenuPrincipalActivity.this, CriarAtividadeActivity.class);
                startActivity(proximaTela);
                finish();
            }
        });

        menuSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
