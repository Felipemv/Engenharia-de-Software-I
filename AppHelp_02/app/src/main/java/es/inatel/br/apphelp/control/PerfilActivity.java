package es.inatel.br.apphelp.control;

import android.content.Intent;
import android.support.constraint.solver.widgets.WidgetContainer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.model.Administrador;
import es.inatel.br.apphelp.model.Aluno;
import es.inatel.br.apphelp.model.AlunoDAO;
import es.inatel.br.apphelp.model.BancoDeDados;
import es.inatel.br.apphelp.model.UsuarioDAO;

public class PerfilActivity extends AppCompatActivity {

    private Bundle bundle;

    private String tipoUsuario;
    private String caminho;

    private boolean editar = false;

    private EditText email;
    private EditText nome;
    private EditText telefone;
    private EditText curso;
    private EditText periodo;
    private EditText matricula;
    private EditText ocupacao;
    private EditText atividadeResponsavel;

    private TextView erroCadastro;

    private Button botaoSalvar;
    private Button botaoVoltar;
    private Button botaoEditar;

    private LinearLayout telaUsuario;

    private FirebaseAuth mAuth;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        bundle = getIntent().getExtras();
        if(bundle.containsKey("tipoUsuario")){
            tipoUsuario = bundle.getString("tipoUsuario");
            caminho = "Usuarios/"+tipoUsuario+"/";

        }

        refereciaComponentes();
        adicionaListeners();
        mostrarInfoUsuario();
        carregarPerfilDAO();
    }

    private void refereciaComponentes() {
        email = (EditText) findViewById(R.id.emailPerfilID);
        nome = (EditText) findViewById(R.id.nomePerfilID);
        telefone = (EditText) findViewById(R.id.telefonePerfilID);

        curso = (EditText) findViewById(R.id.cursoPerfilID);
        periodo = (EditText) findViewById(R.id.periodoPerfilID);
        matricula = (EditText) findViewById(R.id.matriculaPerfilID);

        ocupacao = (EditText) findViewById(R.id.ocupacaoID);
        atividadeResponsavel = (EditText) findViewById(R.id.atividadeRespID);

        erroCadastro = (TextView) findViewById(R.id.erroCadastroID);

        botaoSalvar = (Button) findViewById(R.id.botaoSalvarID);
        botaoEditar = (Button) findViewById(R.id.botaoEditarID);
        botaoVoltar = (Button) findViewById(R.id.botaoVoltarID);

        telaUsuario = (LinearLayout) findViewById(R.id.telaUsuarioID);
    }

    private void adicionaListeners() {
        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaTela = new Intent(PerfilActivity.this, MenuPrincipalActivity.class);
                proximaTela.putExtra("tipoUsuario", tipoUsuario);
                startActivity(proximaTela);
                finish();
            }
        });

        botaoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                botaoSalvar.setText("Salvar");

                editar = true;

                nome.setEnabled(true);
                email.setEnabled(true);
                telefone.setEnabled(true);
                curso.setEnabled(true);
                periodo.setEnabled(true);
                matricula.setEnabled(true);
                ocupacao.setEnabled(true);
                atividadeResponsavel.setEnabled(true);
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editar){
                    boolean edicaoOk;
                    if(tipoUsuario.equals("Aluno")){
                        Aluno aluno = new Aluno();

                        aluno.setNomeCompleto(nome.getText().toString());
                        aluno.setEmail(email.getText().toString());
                        aluno.setTelefoneContato(telefone.getText().toString());
                        aluno.setCurso(curso.getText().toString());
                        aluno.setPeriodo(Integer.parseInt(periodo.getText().toString()));
                        aluno.setMatricula(Integer.parseInt(matricula.getText().toString()));

                        edicaoOk = new UsuarioDAO(aluno).editarPerfil(mAuth.getCurrentUser().getUid());
                    }else{
                        Administrador adm = new Administrador();

                        adm.setNomeCompleto(nome.getText().toString());
                        adm.setEmail(email.getText().toString());
                        adm.setTelefoneContato(telefone.getText().toString());
                        adm.setOcupacao(ocupacao.getText().toString());
                        adm.setAtividadeResponsavel(atividadeResponsavel.getText().toString());

                        edicaoOk = new UsuarioDAO(adm).editarPerfil(mAuth.getCurrentUser().getUid());
                    }

                    if (edicaoOk){
                        Toast.makeText(PerfilActivity.this, "Perfil editado com sucesso!", Toast.LENGTH_LONG).show();

                        Intent proximaPagina = new Intent(PerfilActivity.this, MenuPrincipalActivity.class);
                        proximaPagina.putExtra("tipoUsuario", tipoUsuario);

                        startActivity(proximaPagina);
                        finish();
                    }else{
                        Toast.makeText(PerfilActivity.this, "Erro ao editar perfil!", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Intent proximaPagina = new Intent(PerfilActivity.this, MenuPrincipalActivity.class);
                    proximaPagina.putExtra("tipoUsuario", tipoUsuario);

                    startActivity(proximaPagina);
                    finish();
                }
            }
        });
    }

    private void mostrarInfoUsuario() {
        if(tipoUsuario.equals("Aluno")){
            curso.setVisibility(View.VISIBLE);
            periodo.setVisibility(View.VISIBLE);
            matricula.setVisibility(View.VISIBLE);

            ocupacao.setVisibility(View.GONE);
            atividadeResponsavel.setVisibility(View.GONE);
        }else{
            curso.setVisibility(View.GONE);
            periodo.setVisibility(View.GONE);
            matricula.setVisibility(View.GONE);

            ocupacao.setVisibility(View.VISIBLE);
            atividadeResponsavel.setVisibility(View.VISIBLE);
        }
    }

    private void carregarPerfilDAO(){
        mAuth = FirebaseAuth.getInstance();
        user = new BancoDeDados().conexao(caminho+mAuth.getCurrentUser().getUid());

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    if(tipoUsuario.equals("Aluno")){
                        Aluno aluno = dataSnapshot.getValue(Aluno.class);

                        nome.setText(aluno.getNomeCompleto());
                        email.setText(aluno.getEmail());
                        telefone.setText(aluno.getTelefoneContato());
                        curso.setText(aluno.getCurso());
                        periodo.setText(Integer.toString(aluno.getPeriodo()));
                        matricula.setText(Integer.toString(aluno.getMatricula()));
                    }else{
                        Administrador adm = dataSnapshot.getValue(Administrador.class);

                        nome.setText(adm.getNomeCompleto());
                        email.setText(adm.getEmail());
                        telefone.setText(adm.getTelefoneContato());
                        ocupacao.setText(adm.getOcupacao());
                        atividadeResponsavel.setText(adm.getAtividadeResponsavel());

                    }
                    Toast.makeText(PerfilActivity.this, "Perfil carregado com sucesso!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PerfilActivity.this, "Erro ao carregar perfil!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PerfilActivity.this, "Erro ao carregar perfil!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
