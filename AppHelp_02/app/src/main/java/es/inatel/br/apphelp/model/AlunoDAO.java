package es.inatel.br.apphelp.model;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.inatel.br.apphelp.control.CadastroActivity;
import es.inatel.br.apphelp.control.PerfilActivity;

/**
 * Created by felipe on 25/10/17.
 */

public class AlunoDAO {
    private final String CAMINHO = "Usuarios/Aluno/";
    private FirebaseAuth mAuth;
    private DatabaseReference user;

    private Aluno aluno;

    public FirebaseUser cadastroAluno(final Aluno aluno){
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) mAuth.signOut();

        mAuth.createUserWithEmailAndPassword(aluno.getEmail(), aluno.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    mAuth.signInWithEmailAndPassword(aluno.getEmail(), aluno.getSenha());
                }
            }
        });

        if(mAuth.getCurrentUser() == null) return mAuth.getCurrentUser();


        String id = mAuth.getCurrentUser().getUid();
        DatabaseReference user = new BancoDeDados().conexao(CAMINHO);
        user.child(id).setValue(aluno);

        return mAuth.getCurrentUser();

    }


    public boolean editarPerfilAluno(Aluno aluno, String uId){

        user = new BancoDeDados().conexao("");

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(CAMINHO + uId, aluno);

        user.updateChildren(childUpdates);

        return true;
    }
}
