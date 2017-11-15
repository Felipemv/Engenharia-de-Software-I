package es.inatel.br.apphelp.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.model.AdapterPlanilhaHoras;
import es.inatel.br.apphelp.model.BancoDeDados;
import es.inatel.br.apphelp.model.Ponto;

public class MarcarPontoActivity extends AppCompatActivity {

    private Button botaoEntrada;
    private Button botaoSaida;
    private Button botaoVoltar;

    private TextView data;
    private TextView nome;

    private ListView listPontos;

    private TextClock relogio;

    private String tipoUsuario;
    private String idAdmin;
    private String idAluno;
    private String nomeAluno;

    private ArrayList<Ponto> pontos;

    private Bundle bundle;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_ponto);

        bundle = getIntent().getExtras();
        if(bundle != null) {
            tipoUsuario = bundle.getString("tipoUsuario");
            idAluno = bundle.getString("idAluno");
            idAdmin = bundle.getString("idAdmin");
            if(tipoUsuario.equals("Administrador")){
                nomeAluno = bundle.getString("nomeAluno");
            }
        }

        referenciaComponentes();
        adicionarListeners();
        carregarListaPontos();
    }

    //Carrega a planilha de pontos
    private void carregarListaPontos() {

        if(tipoUsuario.equals("Aluno")){
            idAluno = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        String caminho = "Usuarios/Aluno/"+idAluno+"/Atividades";

        database = new BancoDeDados().conexao(caminho);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()) {

                    String key = d.getKey();

                    ArrayList<Ponto> pontosAux = new ArrayList<>();
                    Ponto cabecalho = new Ponto();

                    cabecalho.setData("Data");
                    cabecalho.setEntrada("Entrada");
                    cabecalho.setSaida("Saída");
                    pontosAux.add(cabecalho);

                    for (DataSnapshot ds : d.child("Planilha").getChildren()){
                        for (DataSnapshot ds1 : ds.getChildren()){
                            pontosAux.add(ds1.getValue(Ponto.class));
                        }

                    }
                    pontos = pontosAux;
                    listPontos = (ListView) findViewById(R.id.listViewPontosID);
                    AdapterPlanilhaHoras adapter = new AdapterPlanilhaHoras(MarcarPontoActivity.this, pontos);
                    listPontos.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MarcarPontoActivity.this, "Erro ao carregar " +
                        "atividades!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Referencia os componentes da tela para serem usados
    public void referenciaComponentes(){
        botaoEntrada = (Button) findViewById(R.id.botaoEntradaID);
        botaoSaida = (Button) findViewById(R.id.botaoSaidaID);
        botaoVoltar = (Button) findViewById(R.id.botaoVoltarPontoID);

        relogio = (TextClock) findViewById(R.id.relogioID);

        data = (TextView) findViewById(R.id.dataPontoID);
        nome = (TextView) findViewById(R.id.nomePontoID);

        listPontos = (ListView) findViewById(R.id.listViewPontosID);

        botaoEntrada.setBackgroundResource(R.color.Green);
        botaoSaida.setBackgroundResource(R.color.Red);
        data.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        if(tipoUsuario.equals("Administrador")){
            botaoSaida.setVisibility(View.GONE);
            botaoEntrada.setVisibility(View.GONE);
            nome.setVisibility(View.VISIBLE);
            relogio.setVisibility(View.GONE);
            data.setVisibility(View.GONE);
            nome.setText(nomeAluno);
        }else{
            botaoSaida.setVisibility(View.VISIBLE);
            botaoEntrada.setVisibility(View.VISIBLE);
            nome.setVisibility(View.GONE);
            relogio.setVisibility(View.VISIBLE);
            data.setVisibility(View.VISIBLE);
            relogio.setFormat24Hour("HH:mm:ss");
        }
    }

    //Adiciona Listeners aos botoes e demais componentes da tela
    public void adicionarListeners(){
        botaoEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baterPontoEntrada();

            }
        });

        botaoSaida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baterPontoSaida();

            }
        });

        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaPagina;

                if (tipoUsuario.equals("Administrador")){
                    proximaPagina = new Intent(MarcarPontoActivity.this,
                            MostrarAtividadesActivity.class);

                }else{
                    proximaPagina = new Intent(MarcarPontoActivity.this,
                            MenuPrincipalActivity.class);
                }

                proximaPagina.putExtra("tipoUsuario", tipoUsuario);
                startActivity(proximaPagina);
                finish();
            }
        });

    }

    //Marcar inicio de atividade
    public void baterPontoEntrada(){
        final String horario = (String) relogio.getText();
        final String dataEntrada = data.getText().toString().replace("/", ":");
        if(tipoUsuario.equals("Aluno")){
            idAluno = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        final String caminho = "Usuarios/Aluno/"+idAluno+"/Atividades";

        database = new BancoDeDados().conexao(caminho);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()) {

                    String key = ds.getKey();
                    long numPontos = ds.child("Planilha").child(dataEntrada).getChildrenCount();

                    Ponto p = new Ponto();

                    p.setData(dataEntrada);
                    p.setEntrada(horario);
                    p.setSaida("-");

                    String novoCaminho = "Planilha/"+dataEntrada+"/Ponto"
                            +Long.toString(numPontos-1)+"/saida";

                    if(numPontos == 0){
                        database.child(key).child("Planilha").child(dataEntrada)
                                .child("Ponto" + Long.toString(numPontos)).setValue(p);

                        Toast.makeText(MarcarPontoActivity.this,
                                "Início de atividade marcado!", Toast.LENGTH_SHORT).show();

                    }else{
                        if(ds.child(novoCaminho).getValue().toString().equals("-")) {
                            Toast.makeText(MarcarPontoActivity.this,
                                    "Erro!Há uma atividade não finalizada!", Toast.LENGTH_SHORT).show();
                        }else{
                            database.child(key).child("Planilha").child(dataEntrada)
                                    .child("Ponto" + Long.toString(numPontos)).setValue(p);

                            Toast.makeText(MarcarPontoActivity.this,
                                    "Início de atividade marcado!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MarcarPontoActivity.this, "Erro ao bater ponto de entrada", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //Marcar término de atividade
    public void baterPontoSaida(){

        final String horario = (String) relogio.getText();
        final String dataSaida = data.getText().toString().replace("/", ":");
        if(tipoUsuario.equals("Aluno")){
            idAluno = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        final String caminho = "Usuarios/Aluno/"+idAluno+"/Atividades";

        database = new BancoDeDados().conexao(caminho);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    long numPontos = ds.child("Planilha").child(dataSaida).getChildrenCount();

                    String novoCaminho = "Planilha/"+dataSaida+"/Ponto"+Long.toString(numPontos-1)+"/saida";


                    if(ds.child(novoCaminho).getValue() != null
                            && ds.child(novoCaminho).getValue().toString().equals("-") ){
                        Map<String, Object> childUpdate = new HashMap<>();

                        DatabaseReference db = new BancoDeDados().conexao("");
                        childUpdate.put(caminho+"/"+key+"/"+novoCaminho, horario);
                        db.updateChildren(childUpdate);

                        Toast.makeText(MarcarPontoActivity.this,
                                "Hora de saída adicinada com sucesso!!", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(MarcarPontoActivity.this,
                                "Erro! Nenhum horário iniciado!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MarcarPontoActivity.this,
                        "Erro ao bater ponto de entrada", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
