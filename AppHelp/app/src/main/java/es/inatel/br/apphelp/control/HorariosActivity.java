package es.inatel.br.apphelp.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpRetryException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.model.BancoDeDados;
import es.inatel.br.apphelp.model.ExpandableListAdapter;
import es.inatel.br.apphelp.model.Horarios;
import es.inatel.br.apphelp.model.ListaHorarios;

public class HorariosActivity extends AppCompatActivity{

    private Bundle bundle;

    private String tipoUsuario;
    private String caminho;
    private String[] diasDaSemana = {"Segunda", "Terca", "Quarta", "Quinta", "Sexta", "Sabado"};
    private String semana[] = {"Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira",
            "Sexta-Feira", "Sábado"};

    private Button criar;
    private Button editar;
    private Button excluir;

    private DatabaseReference database;
    FirebaseAuth mAuth;
    private FirebaseUser user;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        bundle = getIntent().getExtras();
        if(bundle.containsKey("tipoUsuario")){
            tipoUsuario = bundle.getString("tipoUsuario");
        }

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        caminho = "Usuarios/"+tipoUsuario+"/"+user.getUid()+"/Horarios/";

        referenciaComponentes();
        adicionarListeners();

        carregarListas();

    }

    private void carregarListas() {
        database = new BancoDeDados().conexao(caminho);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    String nome;

                    ArrayList<ListaHorarios> list = new ArrayList<>();

                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        nome = ds.getKey();

                        Horarios horarios;
                        ArrayList<Horarios> h = new ArrayList<>();

                        for (DataSnapshot ds1: ds.getChildren()){
                            horarios = new Horarios();

                            horarios = ds1.getValue(Horarios.class);
                            h.add(horarios);
                            nome = horarios.getDiaDaSemana();
                        }

                        ListaHorarios listaHorarios = new ListaHorarios();
                        listaHorarios.setDiaDaSemana(nome);
                        listaHorarios.setHorarios(h);

                        list.add(listaHorarios);
                    }

                    organizaLista(list);

                    ExpandableListView elv = (ExpandableListView) findViewById(R.id.lvExp);
                    ExpandableListAdapter adapter = new ExpandableListAdapter(HorariosActivity.this, list);
                    elv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void organizaLista(ArrayList<ListaHorarios> list){

        //Pega os valores da lista pra procurar os valores que faltam
        ArrayList<String> valores = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            valores.add(list.get(i).getDiaDaSemana());
        }
        ListaHorarios l;


        //Procura e completa com os dias que estão faltando
        boolean ok;
        for (int i = 0; i < semana.length; i++) {
            ok = false;
            for (int j = 0; j < list.size(); j ++){
                if(list.get(j).getDiaDaSemana().equals(semana[i])) {
                    ok = true;
                    break;
                }
            }
            if(!ok){
                l = new ListaHorarios();
                l.setDiaDaSemana(semana[i]);
                l.setHorarios(new ArrayList<Horarios>());

                list.add(l);
            }
        }

        //Ordena os dias da semana para listagem
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getDiaDaSemana().equals("Segunda-Feira"))          Collections.swap(list, i,0);
            else if(list.get(i).getDiaDaSemana().equals("Terça-Feira"))       Collections.swap(list, i, 1);
            else if(list.get(i).getDiaDaSemana().equals("Quarta-Feira"))      Collections.swap(list, i, 2);
            else if(list.get(i).getDiaDaSemana().equals("Quinta-Feira"))      Collections.swap(list, i, 3);
            else if(list.get(i).getDiaDaSemana().equals("Sexta-Feira"))       Collections.swap(list, i, 4);
            else   Collections.swap(list, i, 5);
        }

    }

    private void adicionarListeners() {
        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaTela = new Intent(HorariosActivity.this,
                CriarHorarioActivity.class);
                proximaTela.putExtra("tipoUsuario", tipoUsuario);
                startActivity(proximaTela);
            }
        });
    }

    private void referenciaComponentes() {
        criar = (Button) findViewById(R.id.criarHorarioID);
    }

    private void criarAdapters(String diaSemana, ArrayList list){

    }
}
