package es.inatel.br.apphelp.model;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by felipe on 28/10/17.
 */

public class UsuarioDAO {

    private String caminho;
    private FirebaseAuth mAuth;
    private DatabaseReference user;

    private Usuario usuario;

    public UsuarioDAO(Usuario usuario){
        if(usuario instanceof Aluno)    caminho = "Usuarios/Aluno/";
        else                            caminho = "Usuarios/Administrador/";

        this.usuario = usuario;
    }

    public FirebaseUser cadastroUsuario(){

        mAuth = FirebaseAuth.getInstance();
        String email = usuario.getEmail();
        String senha = usuario.getSenha();
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String s;

                boolean la = task.isSuccessful();
                if (task.isSuccessful())
                {
                    try
                    {
                        wait(200);
                        s = "Cadastro realizado!";

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        FirebaseUser fb = mAuth.getCurrentUser();

        if(mAuth.getCurrentUser() == null) return null;

        String id = mAuth.getCurrentUser().getUid();
        user = new BancoDeDados().conexao(caminho+id);

        user.setValue(usuario);

        return mAuth.getCurrentUser();
    }


    public boolean editarPerfil(String uId){
        user = new BancoDeDados().conexao("");

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(caminho + uId, usuario);

        try {
            user.updateChildren(childUpdates);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
