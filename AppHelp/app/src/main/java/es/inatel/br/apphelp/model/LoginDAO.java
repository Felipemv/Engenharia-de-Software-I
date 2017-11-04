package es.inatel.br.apphelp.model;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import es.inatel.br.apphelp.control.LoginActivity;

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

    public LoginDAO(String email, String senha, String tipoUsuario){
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        CAMINHO = "Usuarios/" + tipoUsuario;
    }

    public LoginDAO(String email, String senha, String tipoUsuario, LoginActivity loginActivity){
                this.email = email;
                this.senha = senha;
                this.tipoUsuario = tipoUsuario;
                this.tela = loginActivity;
                CAMINHO = "Usuarios/" + tipoUsuario;
            }

    public FirebaseAuth autenticacao(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mAuth.signOut();
                }
            }
        });
        return mAuth;

    }
    public boolean sair(FirebaseAuth auth){
        auth.signOut();

        if(auth.getCurrentUser() == null)return false;

        return true;
    }


}
