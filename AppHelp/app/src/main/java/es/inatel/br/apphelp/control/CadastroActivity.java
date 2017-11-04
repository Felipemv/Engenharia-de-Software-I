package es.inatel.br.apphelp.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.model.Administrador;
import es.inatel.br.apphelp.model.AdministradorDAO;
import es.inatel.br.apphelp.model.Aluno;
import es.inatel.br.apphelp.model.AlunoDAO;
import es.inatel.br.apphelp.model.Usuario;
import es.inatel.br.apphelp.model.UsuarioDAO;

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
    private Button botaoVoltar;

    private LinearLayout viewSwitcher;

    private String tipoUsuario;

    private FirebaseAuth mAuth;
    private FirebaseDatabase dataBase;
    private DatabaseReference user;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        referenciaComponentes();
        adicionarListeners();
        dados();
       // limpar_campos();
    }

    public boolean cadastrarUsuario(final String email, final String senha, final String confirmarSenha){


        if(emailCadastro.getText().toString().trim().length() == 0
                || senhaCadastro.getText().toString().trim().length() == 0
                || nome.getText().toString().trim().length() == 0){

            erroCadastro.setText("Os campos obrigatórios não foram preenchidos");
            erroCadastro.setVisibility(View.VISIBLE);
            return false;

        }

        if(!senha.equals(confirmarSenha)){
            erroCadastro.setText("As senhas precisam ser iguais");
            erroCadastro.setVisibility(View.VISIBLE);
            return false;

        }

        if(!radioAdm.isChecked()) {
            tipoUsuario = "Administrador";

            if(!radioAluno.isChecked()){
                erroCadastro.setText("Tipo de usuário não selecionado ");
                return false;
            }else{
                tipoUsuario = "Aluno";
            }
        }else{
            tipoUsuario = "Administrador";
        }



        if(tipoUsuario.equals("Aluno")){
            Aluno aluno = new Aluno();

            aluno.setEmail(email);
            aluno.setNomeCompleto(nome.getText().toString());
            aluno.setTelefoneContato(telefone.getText().toString());
            aluno.setSenha(senhaCadastro.getText().toString());
            aluno.setCurso(curso.getText().toString());
            aluno.setPeriodo(Integer.parseInt(periodo.getText().toString()));
            aluno.setMatricula(Integer.parseInt(matricula.getText().toString()));

            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();

            firebaseUser = new UsuarioDAO(aluno).cadastroUsuario();
        }else{
            Administrador adm = new Administrador();

            adm.setEmail(email);
            adm.setNomeCompleto(nome.getText().toString());
            adm.setTelefoneContato(telefone.getText().toString());
            adm.setOcupacao(ocupacao.getText().toString());
            adm.setAtividadeResponsavel(atividadeResponsavel.getText().toString());

            firebaseUser = new UsuarioDAO(adm).cadastroUsuario();
        }

        if(firebaseUser != null){
            Toast.makeText(CadastroActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_LONG).show();
            return true;
        }

//        firebaseUser.delete();
        Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário!", Toast.LENGTH_LONG).show();
        return false;
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

        botaoVoltar = (Button) findViewById(R.id.botaoVoltarID);


    }

    public void adicionarListeners(){

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailCadastro.getText().toString();
                String senha = senhaCadastro.getText().toString();
                String confirmarSenha = confirmarSenhaCadastro.getText().toString();

                if(cadastrarUsuario(email, senha, confirmarSenha)){
                    Intent proximaTela = new Intent(CadastroActivity.this, LoginActivity.class);
                    startActivity(proximaTela);
                    finish();
                }
            }
        });

        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CadastroActivity.this,"Retorno ao login com sucesso!",Toast.LENGTH_LONG).show();
                Intent proximaTela = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(proximaTela);
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

    private void limpar_campos(){
        nome.setText("");
        emailCadastro.setText("");
        senhaCadastro.setText("");
        confirmarSenhaCadastro.setText("");
        telefone.setText("");
        curso.setText("");
        periodo.setText("");
        matricula.setText("");
        radioAluno.setChecked(true);
    }

    public void dados()
    {
        nome.setText("Ensley");
        emailCadastro.setText("ensleyfmr@gmail.com");
        senhaCadastro.setText("101010");
        confirmarSenhaCadastro.setText("101010");
        telefone.setText("984178330");
        curso.setText("GEC");
        periodo.setText("6");
        matricula.setText("1242");
        radioAluno.setChecked(true);
    }


}

