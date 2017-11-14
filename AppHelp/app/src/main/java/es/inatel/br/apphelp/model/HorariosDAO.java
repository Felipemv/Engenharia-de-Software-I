package es.inatel.br.apphelp.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.inatel.br.apphelp.control.CriarHorarioActivity;
import es.inatel.br.apphelp.control.HorariosActivity;
import es.inatel.br.apphelp.control.MenuPrincipalActivity;

/**
 * Created by felipe on 26/10/17.
 */

public class HorariosDAO {

    private final String CAMINHO;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private Context context;

    public HorariosDAO(Context context){
        mAuth = FirebaseAuth.getInstance();
        CAMINHO = "Usuarios/Aluno/"+mAuth.getCurrentUser().getUid()+"/Horarios/";
        this.context = context;
    }

    //Criar Horários no banco de dados
    public void criarHorarios(final Horarios horarios, String diaSemana){
        database = new BancoDeDados().conexao(CAMINHO+diaSemana.split("--")[1]);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(horarios.getHora())) {
                    Toast.makeText(context, "Atividade já cadastrada para esse horário!",
                            Toast.LENGTH_LONG).show();
                }else{
                    database.child(horarios.getHora()).setValue(horarios);
                    Toast.makeText(context, "Atividade cadastrada com sucesso!",
                            Toast.LENGTH_LONG).show();

                    Intent proximaPagina = new Intent(context, HorariosActivity.class);
                    proximaPagina.putExtra("tipoUsuario", "Aluno");

                    context.startActivity(proximaPagina);
                    ((Activity)context).finish();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Erro ao cadastrar horário!",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    //Editar Horários no banco de dados
    public void editarHorarios(Horarios horarios, String dia, String hora){

        removerHorarios(dia, hora, false);

        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getCurrentUser().getUid();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("Usuarios/Aluno/"+id+"/Horarios/"+horarios.getDiaDaSemana()
                .split("--")[1]+"/"+horarios.getHora(),horarios);

        database = new BancoDeDados().conexao("");

        database.updateChildren(childUpdates);
        Toast.makeText(context, "Horário editado com sucesso!", Toast.LENGTH_LONG).show();

        Intent proximaPagina = new Intent(context, HorariosActivity.class);
        proximaPagina.putExtra("tipoUsuario", "Aluno");

        context.startActivity(proximaPagina);
        ((Activity)context).finish();

    }

    //Remover Horários no banco de dados
    public void removerHorarios(String dia, String horario, boolean remover){
        database = new BancoDeDados().conexao(CAMINHO);

        try{
            database.child(dia.split("--")[1]).child(horario).removeValue();
            if(remover){
                Toast.makeText(context, "Horário removido com sucesso!",
                        Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(context, "Erro ao remover horário!",
                    Toast.LENGTH_SHORT).show();
        }


    }

}
