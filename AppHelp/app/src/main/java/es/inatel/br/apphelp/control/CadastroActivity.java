package es.inatel.br.apphelp.control;

import android.app.ProgressDialog;
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
import es.inatel.br.apphelp.model.Aluno;
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

    //Prepara para o cadastro
    public void cadastrarUsuario(){
        if(radioAdm.isChecked())    tipoUsuario = "Administrador";
        else tipoUsuario = "Aluno";

        if(validaEntrada() == -1){
            erroCadastro.setText("Tipo de usuário não selecionado!");
        }else if (validaEntrada() == 0){
            erroCadastro.setText("Todos os campos devem ser preenchidos!");
        }else{
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Cadastrando usuário...");
            progressDialog.show();

            if(tipoUsuario.equals("Aluno")){
                Aluno aluno = new Aluno();

                aluno.setEmail(emailCadastro.getText().toString());
                aluno.setNomeCompleto(nome.getText().toString());
                aluno.setTelefoneContato(telefone.getText().toString());
                aluno.setCurso(curso.getText().toString());
                aluno.setPeriodo(Integer.parseInt(periodo.getText().toString()));
                aluno.setMatricula(Integer.parseInt(matricula.getText().toString()));

                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                new UsuarioDAO(aluno, this).cadastroUsuario(emailCadastro.getText().toString(),
                        senhaCadastro.getText().toString());
            }else{
                Administrador adm = new Administrador();

                adm.setEmail(emailCadastro.getText().toString());
                adm.setNomeCompleto(nome.getText().toString());
                adm.setTelefoneContato(telefone.getText().toString());
                adm.setOcupacao(ocupacao.getText().toString());
                adm.setAtividadeResponsavel(atividadeResponsavel.getText().toString());

                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                new UsuarioDAO(adm, this).cadastroUsuario(emailCadastro.getText().toString(),
                        senhaCadastro.getText().toString());
            }

            progressDialog.dismiss();
        }

    }

    //Referencia os componentes da tela para serem usados
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

    //Adiciona Listeners aos botoes e demais componentes da tela
    public void adicionarListeners(){

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarUsuario();
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

    //Mostra as informações de cadastro referentes ao administrador e esconde as do aluno
    private void visibilidadeAdm() {
        ocupacao.setVisibility(View.VISIBLE);
        atividadeResponsavel.setVisibility(View.VISIBLE);

        curso.setVisibility(View.GONE);
        periodo.setVisibility(View.GONE);
        matricula.setVisibility(View.GONE);
    }

    //Mostra as informações de cadastro referentes ao aluno e esconde as do administrador
    private void visibilidadeAluno() {
        curso.setVisibility(View.VISIBLE);
        periodo.setVisibility(View.VISIBLE);
        matricula.setVisibility(View.VISIBLE);

        ocupacao.setVisibility(View.GONE);
        atividadeResponsavel.setVisibility(View.GONE);
    }

    //Apaga todos os campos
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

    //Valores de teste para poupar tempo
    public void dados(){
        nome.setText("Felipe");
        emailCadastro.setText("felipe.martinsvitor@gmail.com");
        senhaCadastro.setText("101010");
        confirmarSenhaCadastro.setText("101010");
        telefone.setText("4324233");
        curso.setText("GEC");
        periodo.setText("6");
        matricula.setText("123");
        radioAluno.setChecked(true);
    }

    //Faz a validação dos dados entrados pelo usuário
    public int validaEntrada(){
        String email = emailCadastro.getText().toString().trim();
        String senha = senhaCadastro.getText().toString().trim();
        String confS = confirmarSenhaCadastro.getText().toString().trim();
        String nomeC = nome.getText().toString().trim();
        String telef = telefone.getText().toString().trim();

        if(email.equals("")) return 0;
        if(senha.equals("")) return 0;
        if(confS.equals("")) return 0;
        if(nomeC.equals("")) return 0;
        if(telef.equals("")) return 0;


        if(radioAluno.isChecked()){
            String cursoA = curso.getText().toString().trim();
            String matric = matricula.getText().toString().trim();
            String period = periodo.getText().toString();

            if(cursoA.equals("")) return 0;
            if(period.equals("")) return 0;
            if(matric.equals("")) return 0;
        }else if (radioAdm.isChecked()){
            String ocup = ocupacao.getText().toString().trim();
            String ativ = atividadeResponsavel.getText().toString().trim();

            if(ocup.equals("")) return 0;
            if(ativ.equals("")) return 0;
        }else{
            return -1;
        }

        return 1;
    }
}

