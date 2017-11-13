package es.inatel.br.apphelp.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.model.AdapterListaAtividades;
import es.inatel.br.apphelp.model.AdapterListaHorarios;
import es.inatel.br.apphelp.model.Atividades;
import es.inatel.br.apphelp.model.BancoDeDados;
import es.inatel.br.apphelp.model.Horarios;
import es.inatel.br.apphelp.model.ListaHorarios;

public class MostrarAtividadesActivity extends AppCompatActivity {

    private Button voltar;
    private Button criar;
    private ListView viewAtividades;

    private ArrayList<Atividades> atividades;
    private ArrayList<String> alunos;

    private DatabaseReference database;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_atividades);

        referenciaComponentes();
        adicionaListeners();
        carregarAtividades();
    }

    // Referencia os componentes da tela para serem usados
    public void referenciaComponentes(){
        voltar = (Button) findViewById(R.id.voltarListAtivID);
        criar = (Button) findViewById(R.id.botaoCriarAtivID);

        viewAtividades = (ListView) findViewById(R.id.listViewAtividades);
    }

    //Adiciona Listeners aos botoes e demais componentes da tela
    public void adicionaListeners(){
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaPagina = new Intent(MostrarAtividadesActivity.this,
                        MenuPrincipalActivity.class);

                proximaPagina.putExtra("tipoUsuario", "Administrador");
                startActivity(proximaPagina);
                finish();
            }
        });

        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaPagina = new Intent(MostrarAtividadesActivity.this,
                        CriarAtividadeActivity.class);

                proximaPagina.putExtra("tipoUsuario", "Administrador");
                startActivity(proximaPagina);
                finish();
            }
        });
    }


    public void carregarAtividades(){
        mAuth = FirebaseAuth.getInstance();

        String idAdm = mAuth.getCurrentUser().getUid();
        String caminho = "Usuarios/Administrador/"+idAdm+"/Atividades";

        database = new BancoDeDados().conexao(caminho);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> alunosAux = new ArrayList<>();
                ArrayList<Atividades> atividadesAux = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String a = ds.getKey();
                    Atividades ativ = new Atividades();

                    ativ.setTipo(ds.getValue(Atividades.class).getTipo());
                    ativ.setNome(ds.getValue(Atividades.class).getNome());
                    ativ.setTempoMensal(ds.getValue(Atividades.class).getTempoMensal());

                    atividadesAux.add(ativ);
                    alunosAux.add(a);
                }
                atividades = atividadesAux;
                alunos = alunosAux;
                viewAtividades = (ListView) findViewById(R.id.listViewAtividades);
                AdapterListaAtividades adapter = new AdapterListaAtividades(MostrarAtividadesActivity.this, atividades, alunos);
                viewAtividades.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MostrarAtividadesActivity.this, "Erro ao carregar " +
                        "atividades!", Toast.LENGTH_LONG).show();
            }
        });





    }
}
