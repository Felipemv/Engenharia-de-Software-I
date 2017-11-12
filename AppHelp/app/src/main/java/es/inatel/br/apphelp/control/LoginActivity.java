package es.inatel.br.apphelp.control;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    //Reune informações para realizar o login
    public void login(String email, String senha) throws InterruptedException {

        if(radioAluno.isChecked())  tipoUsuario = "Aluno";
        else                        tipoUsuario = "Administrador";

        new LoginDAO(this).autenticacao(email, senha, tipoUsuario);
    }

    // Referencia os componentes da tela para serem usados
    private void referenciaComponentes() {
        botaoLogin = (Button) findViewById(R.id.botaoLoginID);

        emailLogin = (EditText) findViewById(R.id.emailLoginID);
        senhaLogin = (EditText) findViewById(R.id.senhaLoginID);

        erroLogin = (TextView) findViewById(R.id.erroLoginID);
        botaoCadastrarLogin = (TextView) findViewById(R.id.botaoCadastrarLoginID);

        radioAluno = (RadioButton) findViewById(R.id.radioAlunoLoginID);
        radioAdm = (RadioButton) findViewById(R.id.radioAdmLoginID);
    }

    //Adiciona Listeners aos botoes e demais componentes da tela
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

                ConnectivityManager cm = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

                if(netInfo != null && netInfo.isConnected()){
                    int validacao = validaEntrada();

                    if(validacao == 0){
                        erroLogin.setText("Os campos não foram preenchidos");
                        erroLogin.setVisibility(View.VISIBLE);

                    }else if (validacao == -1){
                        erroLogin.setText("Tipo de usuário não selecionado");
                    }else {
                        try {
                            login(emailLogin.getText().toString(), senhaLogin.getText().toString());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                        Toast.makeText(LoginActivity.this, "Impossivel conectar! Verifique sua conexao com a " +
                                "internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Limpa todos os campos de entrada
    public void limpar_campo(){
        emailLogin.setText("");
        senhaLogin.setText("");
    }

    //Faz a validação dos dados entrados pelo usuário
    public int validaEntrada(){
        String email = emailLogin.getText().toString().trim();
        String senha = senhaLogin.getText().toString().trim();

        if(email.equals("")) return 0;
        if(senha.equals("")) return 0;
        if(!radioAluno.isChecked() && !radioAdm.isChecked()) return -1;

        return 1;
    }

    //Valores de teste para poupar tempo
    public void dados(){
        emailLogin.setText("felipe.martinsvitor@gmail.com");
        senhaLogin.setText("101010");
        radioAluno.setChecked(true);
    }
}

