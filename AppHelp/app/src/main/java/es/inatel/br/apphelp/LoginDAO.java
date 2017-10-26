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
 * Created by felipe on 26/10/17.
 */

public class LoginDAO {

    private final String CAMINHO;
    private String email;
    private String senha;
    private String tipoUsuario;
    private LoginActivity tela;
    FirebaseAuth mAuth;
    DatabaseReference user;

    public LoginDAO(String email, String senha, String tipoUsuario, LoginActivity loginActivity){
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.tela = loginActivity;
        CAMINHO = "Usuarios/" + tipoUsuario;
    }

    public boolean entrar(){
        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(tela, "Login efetuado com sucesso!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(tela, "Falha ao executar login!", Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                }
            }
        });

        user = new BancoDeDados().conexao(CAMINHO);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())){
                    Toast.makeText(tela, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(tela, "Falha ao executar login!", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(tela, "Falha ao executar login!", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
            }
        });

        if (mAuth.getCurrentUser() == null) return false;
        else                                return true;

    }
}
