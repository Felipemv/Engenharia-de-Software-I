package es.inatel.br.apphelp.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.model.Administrador;
import es.inatel.br.apphelp.model.Aluno;
import es.inatel.br.apphelp.model.BancoDeDados;
import es.inatel.br.apphelp.model.Horarios;
import es.inatel.br.apphelp.model.ListaAdapterHorario;

public class HorariosActivity extends AppCompatActivity{

    private Bundle bundle;

    private String tipoUsuario;
    private String caminho;
    private String[] diasDaSemana = {"Segunda", "Terca", "Quarta", "Quinta", "Sexta", "Sabado"};

    private Button criar;
    private Button editar;
    private Button excluir;

    private ListView lvSegunda;
    private ListView lvTerca;
    private ListView lvQuarta;
    private ListView lvQuinta;
    private ListView lvSexta;
    private ListView lvSabado;

    private ListaAdapterHorario adapterSegunda;
    private ListaAdapterHorario adapterTerca;
    private ListaAdapterHorario adapterQuarta;
    private ListaAdapterHorario adapterQuinta;
    private ListaAdapterHorario adapterSexta;
    private ListaAdapterHorario adapterSabado;

    private DatabaseReference database;
    FirebaseAuth mAuth;
    private FirebaseUser user;

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
        for (int i = 0; i < diasDaSemana.length; i++)  {
            carregarListas(diasDaSemana[i]);
        }
    }

    private void carregarListas(final String diaSemana) {
        database = new BancoDeDados().conexao(caminho+diaSemana);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    ArrayList<Horarios> list = new ArrayList<>();
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        list.add(ds.getValue(Horarios.class));
                    }

                    criarAdapters(diaSemana, list);

                }else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void adicionarListeners() {
        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaTela = new Intent(HorariosActivity.this,
                        CriarHorarioActivity.class);
                startActivity(proximaTela);
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaTela = new Intent(HorariosActivity.this,
                        CriarHorarioActivity.class);
                startActivity(proximaTela);
            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void referenciaComponentes() {
        criar = (Button) findViewById(R.id.criarHorarioID);
        editar = (Button) findViewById(R.id.editarHorarioID);
        excluir = (Button) findViewById(R.id.excluirHorarioID);

        lvSegunda = (ListView) findViewById(R.id.listSegundaID);
        lvTerca = (ListView) findViewById(R.id.listTercaID);
        lvQuarta = (ListView) findViewById(R.id.listQuartaID);
        lvQuinta = (ListView) findViewById(R.id.listQuintaID);
        lvSexta = (ListView) findViewById(R.id.listSextaID);
        lvSabado = (ListView) findViewById(R.id.listSabadoID);
    }

    private void criarAdapters(String diaSemana, ArrayList list){
        if(diaSemana.equals("Segunda")){
            adapterSegunda = new ListaAdapterHorario(this, list);
            lvSegunda.setAdapter(adapterSegunda);
        }

        else if(diaSemana.equals("Terca")){
            adapterTerca = new ListaAdapterHorario(this, list);
            lvTerca.setAdapter(adapterTerca);
        }

        else if(diaSemana.equals("Quarta")){
            adapterQuarta = new ListaAdapterHorario(this, list);
            lvQuarta.setAdapter(adapterQuarta);
        }

        else if(diaSemana.equals("Quinta")){
            adapterQuinta = new ListaAdapterHorario(this, list);
            lvQuinta.setAdapter(adapterQuinta);
        }

        else if(diaSemana.equals("Sexta")){
            adapterSexta = new ListaAdapterHorario(this, list);
            lvSexta.setAdapter(adapterSexta);
        }

        else{
            adapterSabado = new ListaAdapterHorario(this, list);
            lvSabado.setAdapter(adapterSabado);
        }
    }
}
