package es.inatel.br.apphelp.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.model.LoginDAO;

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

        if(bundle != null) {
            if (bundle.containsKey("tipoUsuario")) {
                tipoUsuario = bundle.getString("tipoUsuario");
            }
        }
        referenciaComponentes();
        adicionarListeners();
        modificaMenu();
    }

    //Troca o menu para o tipo de usuario conectado
    public void modificaMenu(){
        if(tipoUsuario.equals("Administrador")){

            menuHorario.setText("Atividades");
            menuAtividade.setVisibility(View.GONE);

        }else{
            menuHorario.setText("Quadro de Horarios");
            menuAtividade.setText("Relogio de Ponto");
        }
    }

    //Referencia os componentes da tela para serem usados
    public void referenciaComponentes(){
        menuPerfil =        (Button) findViewById(R.id.menuPerfilID);
        menuHorario =       (Button) findViewById(R.id.menuHorarioID);
        menuAtividade =     (Button) findViewById(R.id.menuAtividadeID);
        menuSair =          (Button) findViewById(R.id.menuSairID);
    }

    //Adiciona Listeners aos botoes e demais componentes da tela
    private void adicionarListeners() {

        menuPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaTela = new Intent(MenuPrincipalActivity.this,
                        PerfilActivity.class);
                proximaTela.putExtra("tipoUsuario", tipoUsuario);
                startActivity(proximaTela);
                finish();
            }
        });

        menuHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaTela;
                if(tipoUsuario.equals("Aluno")){
                    proximaTela = new Intent(MenuPrincipalActivity.this,
                            HorariosActivity.class);

                }else{
                    proximaTela = new Intent(MenuPrincipalActivity.this,
                            MostrarAtividadesActivity.class);
                }

                proximaTela.putExtra("tipoUsuario", tipoUsuario);
                startActivity(proximaTela);
                finish();
            }
        });

        menuAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaTela = new Intent(MenuPrincipalActivity.this,
                        MarcarPontoActivity.class);
                startActivity(proximaTela);
                finish();
            }
        });

        menuSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginDAO(MenuPrincipalActivity.this).sair();
                finish();

            }
        });
    }
}
