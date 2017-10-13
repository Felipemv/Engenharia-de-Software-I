package es.inatel.br.apphelp;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;
import static android.support.v4.content.ContextCompat.startActivity;
import static android.widget.Toast.makeText;

/**
 * Created by felipe on 27/09/17.
 */

public class BancoDeDados{


    private FirebaseAuth mAuth;
    private FirebaseDatabase dataBase;
    private DatabaseReference user;

    public void cadastro(Usuario usuario, String id, String tipoUsuario){

        dataBase = FirebaseDatabase.getInstance();
        user = dataBase.getReference("Usuarios/"+tipoUsuario);

        user.child(id).setValue(usuario);

    }

    public void login(){
    }

    public void logout() {
        mAuth.signOut();
    }
}
