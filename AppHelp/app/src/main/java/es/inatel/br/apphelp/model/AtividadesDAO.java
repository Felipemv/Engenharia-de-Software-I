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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.inatel.br.apphelp.control.MarcarPontoActivity;
import es.inatel.br.apphelp.control.MostrarAtividadesActivity;

/**
 * Created by felipe on 26/10/17.
 */

public class AtividadesDAO {
    private String idAluno;
    private String idAdmin;
    private String caminhoAluno;
    private String caminhoAdmin;

    private Context context;

    private DatabaseReference database;
    private DatabaseReference databaseAtividade;

    public AtividadesDAO(String idAluno, String idAdmin, Context context){
        this.context = context;

        this.idAluno = idAluno;
        this.idAdmin = idAdmin;

        this.caminhoAdmin = "Usuarios/Administrador/"+idAdmin+"/Atividades";
        this.caminhoAluno = "Usuarios/Aluno/"+idAluno+"/Atividades";
    }

    //Cria as atividades no banco de dados
    public void criarAtividade(final Atividades atividades, final String aluno){

        database = new BancoDeDados().conexao(caminhoAdmin);

        //Adiciona aluno na arvore de dados do adms
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot. hasChild(idAluno)){
                    Toast.makeText(context, "Voce ja administra uma atividade desse aluno!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    database.child(idAluno).setValue(atividades);

                    databaseAtividade = new BancoDeDados().conexao(caminhoAluno);
                    databaseAtividade.child(idAdmin).setValue(atividades);

                    Intent proximaPagina = new Intent(context, MostrarAtividadesActivity.class);
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

    //Edita as atividades no banco de dados
    public void editarAtividade(String nome, String tempo, String tipo){

        Map<String, Object> childUpdates = new HashMap<>();

        caminhoAluno = caminhoAluno+"/"+idAdmin+"/";
        caminhoAdmin = caminhoAdmin+"/"+idAluno+"/";

        try{
            //Atualiza dados na arvore do aluno
            database = new BancoDeDados().conexao("");
            childUpdates.put(caminhoAluno+"nome/", nome);
            database.updateChildren(childUpdates);

            childUpdates.clear();
            childUpdates.put(caminhoAluno+"tempoMensal/", tempo);
            database.updateChildren(childUpdates);

            childUpdates.clear();
            childUpdates.put(caminhoAluno+"tipo/", tipo);
            database.updateChildren(childUpdates);

            //Atualiza dados na arvore do administrador
            childUpdates.clear();
            childUpdates.put(caminhoAdmin+"nome/", nome);
            database.updateChildren(childUpdates);

            childUpdates.clear();
            childUpdates.put(caminhoAdmin+"tempoMensal/", tempo);
            database.updateChildren(childUpdates);

            childUpdates.clear();
            childUpdates.put(caminhoAdmin+"tipo/", tipo);
            database.updateChildren(childUpdates);


            Toast.makeText(context, "Atividade editada com sucesso!",
                    Toast.LENGTH_SHORT).show();

            Intent proximaPagina = new Intent(context, MostrarAtividadesActivity.class);
            context.startActivity(proximaPagina);
            ((Activity)context).finish();


        }catch (Exception e){
            Toast.makeText(context, "Erro ao editar atividade!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Remove as atividades no banco de dados
    public void removerAtividades(){
        try{
            database = new BancoDeDados().conexao(caminhoAdmin);
            database.child(idAluno).removeValue();

            database = new BancoDeDados().conexao(caminhoAluno);
            database.child(idAdmin).removeValue();

            Toast.makeText(context, "Atividade removido com sucesso!",
                        Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(context, "Erro ao remover atividade!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
