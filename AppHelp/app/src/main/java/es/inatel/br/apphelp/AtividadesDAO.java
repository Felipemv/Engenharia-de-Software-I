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

public class AtividadesDAO {
    private final String CAMINHO_Aluno;
    private final String CAMINHO_ADM;
    private FirebaseAuth mAuth;
    private DatabaseReference user;

    public AtividadesDAO(FirebaseAuth mAuth){
        this.mAuth = mAuth;
        CAMINHO_ADM = "Usuarios/Administrador/"+mAuth.getCurrentUser().getUid()+"Atividades";
        CAMINHO_Aluno = "Usuarios/Aluno/"+mAuth.getCurrentUser().getUid()+"Horarios/Extras";
    }

    public void criarAtividade(Atividades atividades){
        user = new BancoDeDados().conexao(CAMINHO_ADM);
        user.setValue(atividades);

        user = new BancoDeDados().conexao(CAMINHO_Aluno);
        user.child(atividades.getNome()).setValue(atividades);
    }

    public ArrayList<Atividades> listarAtividades(){

        List<Atividades> lista = new ArrayList<>();

        user = new BancoDeDados().conexao(CAMINHO_ADM);
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Cria o codigo pra listar todos os valores
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return (ArrayList<Atividades>) lista;
    }

    public boolean editarAtividades(){
        //Codigo para editar (UPDATE)
        return true;
    }

    public boolean removerAtividades(){
        //Codigo para remover
        return false;
    }
}
