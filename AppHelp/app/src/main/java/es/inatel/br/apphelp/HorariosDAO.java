package es.inatel.br.apphelp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe on 26/10/17.
 */

public class HorariosDAO {

    private final String CAMINHO;
    private FirebaseAuth mAuth;
    private DatabaseReference user;

    public HorariosDAO(FirebaseAuth mAuth){
        this.mAuth = mAuth;
        CAMINHO = "Usuarios/Administrador/"+mAuth.getCurrentUser().getUid()+"Horarios";
    }

    public void criarHorarios(Horarios horarios){
        user = new BancoDeDados().conexao(CAMINHO+horarios.getDiaDaSemana());
        user.setValue(horarios);
    }

    public ArrayList<Horarios> listarHorarios(){

        List<Horarios> lista = new ArrayList<>();

        user = new BancoDeDados().conexao(CAMINHO);
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Cria o codigo pra listar todos os valores
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return (ArrayList<Horarios>) lista;
    }

    public boolean editarHorarios(){
        //Codigo para editar (UPDATE)
        return true;
    }

    public boolean removerHorarios(){
        //Codigo para remover
        return false;
    }

}
