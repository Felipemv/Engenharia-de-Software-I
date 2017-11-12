package es.inatel.br.apphelp.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.inatel.br.apphelp.control.MarcarPontoActivity;

/**
 * Created by felipe on 26/10/17.
 */

public class AtividadesDAO {
    private String caminho;
    private String idAluno;
    private String idAdm;

    private boolean ok;

    private Context context;

    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private DatabaseReference databaseAtividade;

    public AtividadesDAO(String idAluno, Context context){
        this.idAluno = idAluno;
        this.context = context;

        mAuth = FirebaseAuth.getInstance();
        idAdm = mAuth.getCurrentUser().getUid();

        caminho = "Usuarios/Administrador/"+idAdm+"/Atividades";

        ok = false;

    }

    public void criarAtividade(final Atividades atividades, final String aluno){

        database = new BancoDeDados().conexao(caminho);

        //Adiciona aluno na arvore de dados do adms
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot. hasChild(idAluno)){
                    Toast.makeText(context, "Voce ja administra uma atividade desse aluno!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    database.child(idAluno).setValue(atividades);

                    String caminhoAluno = "Usuarios/Aluno/"+idAluno+"/Atividades/"+idAdm;

                    databaseAtividade = new BancoDeDados().conexao(caminhoAluno);
                    databaseAtividade.setValue(atividades);

                    Intent proximaPagina = new Intent(context, MarcarPontoActivity.class);
                    context.startActivity(proximaPagina);
                    ((Activity) context).finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Erro ao cadastrar atividade!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public ArrayList<Atividades> listarAtividades(){

        List<Atividades> lista = new ArrayList<>();

        //database = new BancoDeDados().conexao(CAMINHO_ADM);
        database.addValueEventListener(new ValueEventListener() {
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
