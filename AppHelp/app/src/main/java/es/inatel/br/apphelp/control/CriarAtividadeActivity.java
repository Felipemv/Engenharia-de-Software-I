package es.inatel.br.apphelp.control;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.model.Aluno;
import es.inatel.br.apphelp.model.Atividades;
import es.inatel.br.apphelp.model.AtividadesDAO;
import es.inatel.br.apphelp.model.BancoDeDados;

public class CriarAtividadeActivity extends AppCompatActivity {

    private String itemTipo[] = {"", "Iniciação Científica", "Estágio", "Monitoria"};
    private String itemTempo[] = {"", "48 horas","80 horas"};
    private String[] itemAluno;

    private String tipoUsuario = "Administrador";
    private HashMap<String, String> map;

    private ArrayAdapter<String> opcoesTipo;
    private ArrayAdapter<String> opcoesTempo;

    private Spinner spinnerTipo;
    private Spinner spinnerTempo;

    private Button botaoVoltar;
    private Button botaoCriar;
    private Button adicionarAluno;

    private ImageButton removerAluno;

    private EditText nomeAtividade;

    private TextView alunoSelecionado;
    private TextView erro;

    private AutoCompleteTextView autoComplete;

    private DatabaseReference database;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_atividade);


        referenciarComponentes();
        adicionarListeners();
        criarSpinners();
        carregarAlunos();
        teste();
    }

    //Adiciona Listeners aos botoes e demais componentes da tela
    private void adicionarListeners() {
        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CriarAtividadeActivity.this,"Retorno ao Menu com sucesso!!",Toast.LENGTH_LONG).show();
                Intent proximaTela = new Intent(CriarAtividadeActivity.this, MenuPrincipalActivity.class);
                startActivity(proximaTela);
                finish();
            }
        });

        adicionarAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoComplete.getText().toString().trim().equals("")){
                    Toast.makeText(CriarAtividadeActivity.this, "Nenhum aluno selecionado", Toast.LENGTH_LONG).show();
                }else{
                    String nome = autoComplete.getText().toString();
                    if(map.containsKey(nome)){
                        alunoSelecionado.setText(nome);
                        removerAluno.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(CriarAtividadeActivity.this, "Aluno nao existente!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        removerAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alunoSelecionado.setText("");
                removerAluno.setVisibility(View.INVISIBLE);
            }
        });

        botaoCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarAtividade();
            }
        });
    }

    //Junta as informaçoes para fazer o cadastro
    private void criarAtividade() {

        if(validaEntrada() == -3){
            erro.setText("Nome da atividade nao preenchido!");
            erro.setVisibility(View.VISIBLE);
        }else if(validaEntrada() == -2){
            erro.setVisibility(View.VISIBLE);
            erro.setText("Tipo de atividade no selecionada!");
        }else if(validaEntrada() == -1){
            erro.setVisibility(View.VISIBLE);
            erro.setText("Tempo obrigatorio nao selecionado!");
        }else if(validaEntrada() == -0){
            erro.setVisibility(View.VISIBLE);
            erro.setText("Nenhum aluno selecionado para atividade!");
        }else{
            mAuth = FirebaseAuth.getInstance();

            final String idAdm = mAuth.getCurrentUser().getUid();
            String aluno = alunoSelecionado.getText().toString();
            String caminho = "Usuarios/Administrador/"+idAdm+"/Atividades";

            final String idAluno = map.get(aluno);
            Atividades atividades = new Atividades();

            atividades.setNome(nomeAtividade.getText().toString());
            atividades.setTempo_mensal(spinnerTempo.getSelectedItem().toString());
            atividades.setTipo(spinnerTipo.getSelectedItem().toString());

            new AtividadesDAO(idAluno, CriarAtividadeActivity.this)
                    .criarAtividade(atividades, aluno);

        }
    }

    //Valida dados entrados pelo usuario
    private int validaEntrada() {

        if(nomeAtividade.getText().toString().trim().equals(""))        return -3;
        if(spinnerTipo.getSelectedItem().toString().equals(""))         return -2;
        else if(spinnerTempo.getSelectedItem().toString().equals(""))   return -1;
        else if(alunoSelecionado.getText().toString().equals(""))       return -0;

        return 1;
    }

    // Referencia os componentes da tela para serem usados
    private void referenciarComponentes() {
        botaoVoltar = (Button) findViewById(R.id.voltarAtivID);
        botaoCriar = (Button) findViewById(R.id.criarAtividadeID);
        adicionarAluno = (Button) findViewById(R.id.adicionarAlunoID);
        removerAluno = (ImageButton) findViewById(R.id.removerAlunoAtivID);

        nomeAtividade = (EditText) findViewById(R.id.nomeAtividadeID);

        erro = (TextView) findViewById(R.id.erroAtividadeID);
        alunoSelecionado = (TextView) findViewById(R.id.alunoSelecAtividadeID);

        autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteID);

        spinnerTipo = (Spinner) findViewById(R.id.tipoID);
        spinnerTempo = (Spinner) findViewById(R.id.tempoID);
        removerAluno.getBackground().setAlpha(0);
    }

    //Cria os spinners de opções
    private void criarSpinners(){
        opcoesTipo = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itemTipo);
        spinnerTipo.setAdapter(opcoesTipo);

        opcoesTempo = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itemTempo);
        spinnerTempo.setAdapter(opcoesTempo);
    }

    //Cria a lista de alunos para selecionar
    private void carregarAlunos(){

        final ArrayAdapter<String> opcoesAluno = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        final ArrayList<String> alunoAux = new ArrayList<>();
        final HashMap<String, String> mapAux = new HashMap<>();
        database = new BancoDeDados().conexao("Usuarios/Aluno");

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> mapAux = new HashMap<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Aluno a = ds.getValue(Aluno.class);

                    String nome = a.getMatricula() + " - " + a.getNomeCompleto();

                    mapAux.put(nome, ds.getKey());
                    opcoesAluno.add(nome);
                }
                autoComplete.setAdapter(opcoesAluno);
                map = mapAux;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CriarAtividadeActivity.this, "Erro ao carregar a lista " +
                        "de alunos!", Toast.LENGTH_LONG).show();
            }


        });
    }

    //Valores de teste para poupar tempo
    private void teste(){
        nomeAtividade.setText("Teste");
        spinnerTipo.setSelection(1);
        spinnerTempo.setSelection(1);
        alunoSelecionado.setText("123 - Felipe Martins Vitor");
    }
}
