package es.inatel.br.apphelp;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button botaoLogin;
    private TextView botaoCadastrarLogin;
    private EditText emailLogin;
    private EditText senhaLogin;
    private TextView erroLogin;
    private RadioButton radioAluno;

    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase dataBase;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        botaoLogin = (Button) findViewById(R.id.botaoLoginID);
        botaoCadastrarLogin = (TextView) findViewById(R.id.botaoCadastrarLoginID);
        emailLogin = (EditText) findViewById(R.id.emailLoginID);
        senhaLogin = (EditText) findViewById(R.id.senhaLoginID);
        erroLogin = (TextView) findViewById(R.id.erroLoginID);
        radioAluno = (RadioButton) findViewById(R.id.radioAlunoLoginID);

        //emailLogin.setText("felipevitor@gec.inatel.br");
        //senhaLogin.setText("felipe");
        //radioAluno.setChecked(true);

        mAuth = FirebaseAuth.getInstance();
        botaoCadastrarLogin.setOnClickListener(this);

        emailLogin.setText("felipevitor@gec.inatel.br");
        senhaLogin.setText("felipe");

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
                            String tipoUsuario;
                            if (radioAluno.isChecked())tipoUsuario = "Aluno";
                            else    tipoUsuario = "Administrador";
                            String uID = mAuth.getCurrentUser().getUid();

                            dataBase = FirebaseDatabase.getInstance();
                            user = dataBase.getReference("Usuarios/");


                            user.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                    String id = mAuth.getCurrentUser().getUid();
                                    String tipoUser;

                                    if(radioAluno.isChecked())tipoUser = "Aluno";
                                    else                      tipoUser = "Administrador";

                                    if(dataSnapshot.child(tipoUser).hasChild(id)){
                                        Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Falha ao executar login!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(LoginActivity.this, "Falha ao executar login!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        Intent intentAbrirTelaCadastro = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intentAbrirTelaCadastro);
        finish();
    }


}

