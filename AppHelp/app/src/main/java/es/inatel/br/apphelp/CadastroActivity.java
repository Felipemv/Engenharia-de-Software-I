package es.inatel.br.apphelp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.file.Files;
import java.util.concurrent.Delayed;

public class CadastroActivity extends AppCompatActivity{

    private EditText emailCadastro;
    private EditText senhaCadastro;
    private EditText confirmarSenhaCadastro;
    private EditText nome;
    private EditText telefone;
    private EditText curso;
    private EditText periodo;
    private EditText matricula;
    private EditText ocupacao;
    private EditText atividadeResponsavel;

    private RadioButton radioAluno;
    private RadioButton radioAdm;

    private TextView erroCadastro;

    private Button botaoCadastrar;

    private LinearLayout viewSwitcher;

    private String tipoUsuario;

    private FirebaseAuth mAuth;
    private FirebaseDatabase dataBase;
    private DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        referenciaComponentes();
        adicionarListeners();
        teste();

    }

    public void cadastrarUsuario(final String email, final String senha, final String confirmarSenha){


        if(emailCadastro.getText().toString().trim().length() == 0
                || senhaCadastro.getText().toString().trim().length() == 0
                || nome.getText().toString().trim().length() == 0){

            erroCadastro.setText("Os campos obrigatórios não foram preenchidos");
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

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Usuario usuario;
                    if(tipoUsuario.equals("Aluno")){
                        Aluno aluno = new Aluno();

                        aluno.setEmail(email);
                        aluno.setSenha(senha);
                        aluno.setNomeCompleto(nome.getText().toString());
                        aluno.setTelefoneContato(telefone.getText().toString());
                        aluno.setCurso(curso.getText().toString());
                        aluno.setPeriodo(Integer.parseInt(periodo.getText().toString()));
                        aluno.setMatricula(Integer.parseInt(matricula.getText().toString()));

                        new BancoDeDados().cadastro(aluno, mAuth.getCurrentUser().getUid(), tipoUsuario);
                    }else{
                        Administrador adm = new Administrador();

                        adm.setEmail(email);
                        adm.setSenha(senha);
                        adm.setNomeCompleto(nome.getText().toString());
                        adm.setTelefoneContato(telefone.getText().toString());
                        adm.setOcupacao(ocupacao.getText().toString());
                        adm.setAtividadeResponsavel(atividadeResponsavel.getText().toString());

                        new BancoDeDados().cadastro(adm, mAuth.getCurrentUser().getUid(), tipoUsuario);
                    }
                    Toast.makeText(CadastroActivity.this, "Cadastrado com sucesso", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(CadastroActivity.this, "Falha ao cadastrar usuario", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void referenciaComponentes(){
        emailCadastro = (EditText) findViewById(R.id.emailCadastroID);
        senhaCadastro = (EditText) findViewById(R.id.senhaCadastroID);
        confirmarSenhaCadastro = (EditText) findViewById(R.id.confirmarSenhaCadastroID);
        nome = (EditText) findViewById(R.id.nomeCadastroID);
        telefone = (EditText) findViewById(R.id.telefoneContatoID);

        curso = (EditText) findViewById(R.id.cursoID);
        periodo = (EditText) findViewById(R.id.periodoID);
        matricula = (EditText) findViewById(R.id.matriculaID);

        ocupacao = (EditText) findViewById(R.id.ocupacaoID);
        atividadeResponsavel = (EditText) findViewById(R.id.atividadeRespID);

        radioAluno = (RadioButton) findViewById(R.id.radioAlunoCadastroID);
        radioAdm = (RadioButton) findViewById(R.id.radioAdmCadastroID);

        erroCadastro = (TextView) findViewById(R.id.erroCadastroID);

        botaoCadastrar = (Button) findViewById(R.id.botaoCadastrarID);

        viewSwitcher = (LinearLayout) findViewById(R.id.viewSwitcherID);
    }

    public void adicionarListeners(){

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailCadastro.getText().toString();
                String senha = senhaCadastro.getText().toString();
                String confirmarSenha = confirmarSenhaCadastro.getText().toString();

                cadastrarUsuario(email, senha, confirmarSenha);
            }
        });

        radioAluno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                viewSwitcher.setVisibility(View.VISIBLE);
                if(radioAluno.isChecked()){
                    visibilidadeAluno();
                }
            }
        });

        radioAdm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                viewSwitcher.setVisibility(View.VISIBLE);
                if (radioAdm.isChecked()){
                    visibilidadeAdm();
                }
            }
        });


    }

    private void visibilidadeAdm() {
        ocupacao.setVisibility(View.VISIBLE);
        atividadeResponsavel.setVisibility(View.VISIBLE);

        curso.setVisibility(View.GONE);
        periodo.setVisibility(View.GONE);
        matricula.setVisibility(View.GONE);
    }

    private void visibilidadeAluno() {
        curso.setVisibility(View.VISIBLE);
        periodo.setVisibility(View.VISIBLE);
        matricula.setVisibility(View.VISIBLE);

        ocupacao.setVisibility(View.GONE);
        atividadeResponsavel.setVisibility(View.GONE);
    }

    private void teste(){
        nome.setText("Felipe");
        emailCadastro.setText("felipevitor@gec.inatel.br");
        senhaCadastro.setText("felipe");
        confirmarSenhaCadastro.setText("felipe");
        telefone.setText("123");
        curso.setText("gec");
        periodo.setText("6");
        matricula.setText("1147");
        radioAluno.setChecked(true);
    }
}

