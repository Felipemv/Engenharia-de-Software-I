package es.inatel.br.apphelp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.file.Files;
import java.util.concurrent.Delayed;

public class CadastroActivity extends AppCompatActivity {

    private EditText emailCadastro;
    private EditText senhaCadastro;
    private EditText confirmarSenhaCadastro;
    private RadioButton radioAluno;
    private RadioButton radioAdm;
    private TextView erroCadastro;
    private Button botaoCadastrar;
    private ProgressDialog progressDialog;
    private static int autoIncremento = 0;

    FirebaseAuth autenticacao;
    FirebaseDatabase dataBase;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        emailCadastro = (EditText) findViewById(R.id.emailCadastroID);
        senhaCadastro = (EditText) findViewById(R.id.senhaCadastroID);
        confirmarSenhaCadastro = (EditText) findViewById(R.id.confirmarSenhaCadastroID);

        radioAluno = (RadioButton) findViewById(R.id.radioAlunoCadastroID);
        radioAdm = (RadioButton) findViewById(R.id.radioAdmCadastroID);

        erroCadastro = (TextView) findViewById(R.id.erroCadastroID);
        botaoCadastrar = (Button) findViewById(R.id.botaoCadastrarID);
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            String email = emailCadastro.getText().toString();
            String senha = senhaCadastro.getText().toString();
            String confirmarSenha = confirmarSenhaCadastro.getText().toString();

            cadastrarUsuario(email, senha, confirmarSenha);
            }
        });
    }

    public void cadastrarUsuario(final String email, String senha, String confirmarSenha){

        String tipoUsuario;

        if(emailCadastro.getText().toString().trim().length() == 0
                || senhaCadastro.getText().toString().trim().length() == 0){

            erroCadastro.setText("Os campos não foram preenchidos");
            erroCadastro.setVisibility(View.VISIBLE);
            return;

        }

        if(!senha.equals(confirmarSenha)){
            erroCadastro.setText("As senhas precisam ser iguais");
            erroCadastro.setVisibility(View.VISIBLE);
            return;

        }

        if(!radioAdm.isChecked()) {
            tipoUsuario = "Administrador";

            if(!radioAluno.isChecked()){
                erroCadastro.setText("Tipo de usuário não selecionado ");
                return;
            }else{
                tipoUsuario = "Aluno";
            }
        }else{
            tipoUsuario = "Administrador";
        }

        autenticacao = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Cadastrando usuário...");
        progressDialog.show();

        autenticacao.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    dataBase = FirebaseDatabase.getInstance();
                    user = dataBase.getReference("Usuários");
                    String id = user.child("Aluno").push().getKey();
                    user.child("Aluno").child("ID").setValue(id);

                    if(radioAluno.isChecked()){
                        user.child("Aluno").child("Email").setValue(email);
                    }else{
                        user.child("Email").child("").setValue(email);
                    }



                }else{
                    Toast.makeText(CadastroActivity.this, "Impossível cadastrar!", Toast.LENGTH_LONG).show();
                }
            }
        });

        progressDialog.dismiss();
    }
}

