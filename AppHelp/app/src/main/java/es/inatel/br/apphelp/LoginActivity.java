package es.inatel.br.apphelp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button botaoLogin;
    private TextView botaoCadastrarLogin;
    private EditText emailLogin;
    private EditText senhaLogin;
    private TextView erroLogin;

    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        botaoLogin = (Button) findViewById(R.id.botaoLoginID);
        botaoCadastrarLogin = (TextView) findViewById(R.id.botaoCadastrarLoginID);
        emailLogin = (EditText) findViewById(R.id.emailLoginID);
        senhaLogin = (EditText) findViewById(R.id.senhaLoginID);
        erroLogin = (TextView) findViewById(R.id.erroLoginID);

        mAuth = FirebaseAuth.getInstance();
        botaoCadastrarLogin.setOnClickListener(this);

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailLogin.getText().toString().trim().length() == 0 ||
                        emailLogin.getText().toString().trim().length() == 0){

                    erroLogin.setText("Os campos não foram preenchidos");
                    erroLogin.setVisibility(View.VISIBLE);
                }else{
                    login(emailLogin.getText().toString(), senhaLogin.getText().toString());
                }
            }
        });
    }

    public void login(String email, String senha){
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Falha ao executar login!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Login executado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intentAbrirTelaCadastro = new Intent(LoginActivity.this, CadastroAdminActivity.class);
                            startActivity(intentAbrirTelaCadastro);
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        Intent intentAbrirTelaCadastro = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intentAbrirTelaCadastro);
    }
}

