package es.inatel.br.apphelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class CadastroActivity extends AppCompatActivity {

    private Button botaoContinuar;
    private EditText nomeCompleto;
    private EditText email;
    private EditText telefoneContato;
    private EditText senhaCadastro;
    private EditText confirmarSenha;
    private TextView senhaErrada;
    private TextView camposObrigatorios;
    private RadioButton radioAdm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        botaoContinuar = (Button) findViewById(R.id.botaoContinuarID);
        nomeCompleto = (EditText) findViewById(R.id.nomeCompletoID);
        email = (EditText) findViewById(R.id.emailID);
        telefoneContato = (EditText) findViewById(R.id.telefoneContatoID);
        senhaCadastro = (EditText) findViewById(R.id.senhaCadastroID);
        confirmarSenha = (EditText) findViewById(R.id.confirmarSenhaID);
        senhaErrada = (TextView) findViewById(R.id.senhaErradaID);
        camposObrigatorios = (TextView) findViewById(R.id.camposObrigatoriosID);
        radioAdm = (RadioButton) findViewById(R.id.radioAdmID);

        botaoContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!senhaCadastro.getText().toString().equals(confirmarSenha.getText().toString())){
                    senhaErrada.setVisibility(View.VISIBLE);
                    camposObrigatorios.setVisibility(View.INVISIBLE);
                    confirmarSenha.setText("");

                }else if(nomeCompleto.getText().toString().length() == 0 || email.getText().toString().length() == 0 ||
                    telefoneContato.getText().toString().length() == 0 || senhaCadastro.getText().toString().length() == 0){
                    senhaErrada.setVisibility(View.INVISIBLE);
                    camposObrigatorios.setVisibility(View.VISIBLE);

                }else{
                    Usuario usuario;

                    if(radioAdm.isSelected()){
                        usuario = new Administrador();
                    }else{
                        usuario = new Aluno();
                    }

                    usuario.setNomeCompleto(nomeCompleto.getText().toString());
                    usuario.setEmail(email.getText().toString());
                    usuario.setTelefoneContato(telefoneContato.getText().toString());
                    usuario.setSenha(senhaCadastro.getText().toString());
                }
            }
        });
    }
}
