package es.inatel.br.apphelp.control;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.model.BancoDeDados;
import es.inatel.br.apphelp.model.LoginDAO;


public class LoginActivity extends AppCompatActivity{

    private String caminho;
    private String tipoUsuario;

    private Button botaoLogin;

    private TextView botaoCadastrarLogin;
    private TextView erroLogin;

    private EditText emailLogin;
    private EditText senhaLogin;

    private RadioButton radioAluno;
    private RadioButton radioAdm;

    private FirebaseAuth mAuth;
    private FirebaseUser fbUser;
    private DatabaseReference user;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        referenciaComponentes();
        adicionaListeners();
        dados();
        //limpar_campo();
    }

    public void login(String email, String senha) throws InterruptedException {
        if(radioAluno.isChecked())  tipoUsuario = "Aluno";
        else                        tipoUsuario = "Administrador";

        caminho = "Usuarios/" +tipoUsuario;
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    mAuth.signOut();
                }
            }
        });

        fbUser = mAuth.getCurrentUser();
        // id de quem eu estou procurando
        final String id = fbUser.getUid();

        if(fbUser != null){
            user = new BancoDeDados().conexao(caminho);

            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    boolean id_que_estou_procurando_existe = dataSnapshot.hasChild(id);

                    if (id_que_estou_procurando_existe) {
                        Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_LONG).show();
                        Intent proximaPagina = new Intent(LoginActivity.this, MenuPrincipalActivity.class);
                        proximaPagina.putExtra("tipoUsuario", tipoUsuario);
                        startActivity(proximaPagina);
                        //finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "E-mail ou senha inválido!", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, "Erro ao efetuar login!", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(LoginActivity.this, "Erro ao efetuar login!", Toast.LENGTH_LONG).show();
        }


    }

    private void referenciaComponentes() {
        botaoLogin = (Button) findViewById(R.id.botaoLoginID);

        emailLogin = (EditText) findViewById(R.id.emailLoginID);
        senhaLogin = (EditText) findViewById(R.id.senhaLoginID);

        erroLogin = (TextView) findViewById(R.id.erroLoginID);
        botaoCadastrarLogin = (TextView) findViewById(R.id.botaoCadastrarLoginID);

        radioAluno = (RadioButton) findViewById(R.id.radioAlunoLoginID);
        radioAdm = (RadioButton) findViewById(R.id.radioAdmLoginID);
    }

    private void adicionaListeners() {
        botaoCadastrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaTela = new Intent(LoginActivity.this, CadastroActivity.class);

                startActivity(proximaTela);
                finish();
            }
        });

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(LoginActivity.this, "AppHelp", "Logando...");
                int validacao = validaEntrada();

                if(validacao == 0){
                    erroLogin.setText("Os campos não foram preenchidos");
                    erroLogin.setVisibility(View.VISIBLE);

                }else if (validacao == -1){
                    erroLogin.setText("Tipo de usuário não selecionado");
                }else {
                    try {
                        login(emailLogin.getText().toString(), senhaLogin.getText().toString());
                        progressDialog.dismiss();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void limpar_campo()
    {
        emailLogin.setText("");
        senhaLogin.setText("");

    }

    public void teste(){
        emailLogin.setText("felipe.martinsvitor@gmail.com");
        senhaLogin.setText("felipe");
        radioAluno.setChecked(true);

        //emailLogin.setFocusable(false);
        //senhaLogin.setFocusable(false);
    }

    public int validaEntrada(){
        String email = emailLogin.getText().toString().trim();
        String senha = senhaLogin.getText().toString().trim();

        if(email.equals("")) return 0;
        if(senha.equals("")) return 0;
        if(!radioAluno.isChecked() && !radioAdm.isChecked()) return -1;

        return 1;
    }

    public void dados()
    {
        emailLogin.setText("ensleyfmr@gmail.com");
        senhaLogin.setText("101010");
        radioAluno.setChecked(true);
    }
}

