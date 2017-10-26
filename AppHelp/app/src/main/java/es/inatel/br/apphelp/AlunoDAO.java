package es.inatel.br.apphelp;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by felipe on 25/10/17.
 */

public class AlunoDAO {
    private final String CAMINHO = "Usuarios/Aluno";
    private final CadastroActivity tela;
    private FirebaseAuth mAuth;
    private DatabaseReference user;

    public AlunoDAO(CadastroActivity telaCadastro){
        this.tela = telaCadastro;
    }

    public boolean cadastroAluno(Aluno aluno){

        mAuth.createUserWithEmailAndPassword(aluno.getEmail(), aluno.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(tela, "Usuário criado com sucesso", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(tela, "Falha ao cadastrar usuário", Toast.LENGTH_LONG).show();
                }
            }
        });

        DatabaseReference user = new BancoDeDados().conexao(CAMINHO);
        user.child(mAuth.getCurrentUser().getUid()).setValue(aluno);

        if(mAuth.getCurrentUser() == null) return false;
        else                               return true;

    }

    public Aluno visualizarPerfilAluno(String uId){
        Aluno aluno;

        user = new BancoDeDados().conexao(CAMINHO+uId);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Codigo para receber classe
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return null;
    }

    public boolean editarPerfilAluno(Aluno aluno, String uId){

        user = new BancoDeDados().conexao(CAMINHO+uId);
        //Codigo para atualizar os valores
        return true;
    }
}
