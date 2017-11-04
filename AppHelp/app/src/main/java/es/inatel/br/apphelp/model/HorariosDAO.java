package es.inatel.br.apphelp.model;

import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.inatel.br.apphelp.control.CriarHorarioActivity;

/**
 * Created by felipe on 26/10/17.
 */

public class HorariosDAO {

    private boolean existe = false;
    private final String CAMINHO;
    private FirebaseAuth mAuth;
    private DatabaseReference user;

    public HorariosDAO(){
        mAuth = FirebaseAuth.getInstance();
        CAMINHO = "Usuarios/Aluno/"+mAuth.getCurrentUser().getUid()+"/Horarios/";
    }

    public void criarHorarios(final Horarios horarios, String diaSemana){
        user = new BancoDeDados().conexao(CAMINHO+diaSemana);

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(horarios.getHora())) {
                    funcao(dataSnapshot);
                    user.child(horarios.getHora()).setValue(horarios);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        boolean v = existe;
    }

    public void funcao(DataSnapshot dataSnapshot){
        existe = true;
    }

    public boolean editarHorarios(){
        //Codigo para editar (UPDATE)
        return true;
    }

    public boolean removerHorarios(String dia, String horario){

        return false;
    }

}
