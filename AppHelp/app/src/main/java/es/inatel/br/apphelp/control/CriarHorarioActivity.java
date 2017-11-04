package es.inatel.br.apphelp.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.model.BancoDeDados;
import es.inatel.br.apphelp.model.Horarios;
import es.inatel.br.apphelp.model.HorariosDAO;

public class CriarHorarioActivity extends AppCompatActivity {

    private Bundle bundle;
    private String tipoUsuario;

    private Horarios horarios;

    private Button voltar;
    private Button criar;

    private TextView erro;

    private EditText nome;
    private EditText codigo;
    private EditText local;

    private Spinner tipo;
    private Spinner horario;
    private Spinner diaSemana;

    private String diaDaSemana;
    private String itemTipo[] = {"", "Aula", "Curso","Iniciação Científica", "Estágio", "Monitoria"};
    private String itemHorario[] = {"", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00",
            "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00",
            "20:30", "21:00", "21:30", "22:00"};
    private String itemSemana[] = {"", "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira",
            "Sexta-Feira", "Sábado"};

    private ArrayAdapter<String> opcoesTipo;
    private ArrayAdapter<String> opcoesHorario;
    private ArrayAdapter<String> opcoesSemana;

    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_horario);

        bundle = getIntent().getExtras();
        if(bundle.containsKey("tipoUsuario")){
            tipoUsuario = bundle.getString("tipoUsuario");
        }

        referenciaComponentes();
        adicionarListener();
        criarSpinners();
        teste();
    }

    private void adicionarListener() {
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proximaPagina = new Intent(CriarHorarioActivity.this, HorariosActivity.class);
                proximaPagina.putExtra("tipoUsuario", tipoUsuario);
                startActivity(proximaPagina);
                finish();
            }
        });

        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarHorario();
            }
        });
    }

    private void referenciaComponentes() {
        voltar = (Button) findViewById(R.id.botaoVoltarHorarioID);
        criar = (Button) findViewById(R.id.botaoSalvarHorarioID);

        erro = (TextView) findViewById(R.id.erroHorariosID);

        nome = (EditText) findViewById(R.id.nomeHorarioID);
        codigo = (EditText) findViewById(R.id.codigoHorarioID);
        local = (EditText) findViewById(R.id.localHorarioID);

        tipo = (Spinner) findViewById(R.id.tipoHorarioID);
        horario = (Spinner) findViewById(R.id.horaHorarioID);
        diaSemana = (Spinner) findViewById(R.id.diaSemanaHorarioID);
    }

    private void criarSpinners(){
        opcoesTipo = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itemTipo);
        tipo.setAdapter(opcoesTipo);

        opcoesHorario = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itemHorario);
        horario.setAdapter(opcoesHorario);

        opcoesSemana = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, itemSemana);
        diaSemana.setAdapter(opcoesSemana);
    }

    private void criarHorario(){
        if (validaEntrada()){
            horarios = new Horarios();

            horarios.setNome(nome.getText().toString());
            horarios.setCodigo(codigo.getText().toString());
            horarios.setLocal(local.getText().toString());

            horarios.setTipo(tipo.getSelectedItem().toString());
            horarios.setHora(horario.getSelectedItem().toString());
            horarios.setDiaDaSemana(diaSemana.getSelectedItem().toString());

            mAuth = FirebaseAuth.getInstance();

            semana(horarios);

            database = new BancoDeDados().conexao("Usuarios/Aluno/"+mAuth.getCurrentUser().
                    getUid()+"/Horarios/"+diaDaSemana);
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(horarios.getHora())){
                        new HorariosDAO().criarHorarios(horarios, diaDaSemana);

                        Toast.makeText(CriarHorarioActivity.this, "Atividade " +
                                "cadastrada com sucesso!", Toast.LENGTH_LONG).show();

                        Intent proximaPagina = new Intent(CriarHorarioActivity.this,
                                HorariosActivity.class);
                        proximaPagina.putExtra("tipoUsuario", tipoUsuario);
                        startActivity(proximaPagina);
                        finish();

                    }else{
                        Toast.makeText(CriarHorarioActivity.this, "Atividade já " +
                                "cadastrada para esse horário!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CriarHorarioActivity.this, "Erro ao cadastrar " +
                            "horário!", Toast.LENGTH_LONG).show();
                }
            });

        }else{
            erro.setVisibility(View.VISIBLE);
        }
    }

    private void semana(Horarios horarios) {
        String diaSemana = horarios.getDiaDaSemana();

        if(diaSemana.equals("Segunda-Feira"))       diaDaSemana = "Segunda";
        else if(diaSemana.equals("Terça-Feira"))    diaDaSemana = "Terca";
        else if(diaSemana.equals("Quarta-Feira"))   diaDaSemana = "Quarta";
        else if(diaSemana.equals("Quinta-Feira"))   diaDaSemana = "Quinta";
        else if(diaSemana.equals("Sexta-Feira"))    diaDaSemana = "Sexta";
        else                                        diaDaSemana = "Sabado";

    }

    private boolean validaEntrada(){
        if(nome.getText().toString().trim().equals(""))                 return false;
        if(codigo.getText().toString().trim().equals(""))               return false;
        if(local.getText().toString().trim().equals(""))                return false;

        if(tipo.getSelectedItem().toString().trim().equals(""))         return false;
        if(horario.getSelectedItem().toString().trim().equals(""))      return false;
        if(diaSemana.getSelectedItem().toString().trim().equals(""))    return false;

        return true;
    }

    private void teste(){
        nome.setText("Ingles");
        codigo.setText("123");
        local.setText("Wizard");
        tipo.setSelection(2);
        horario.setSelection(5);
        diaSemana.setSelection(2);
    }
}
