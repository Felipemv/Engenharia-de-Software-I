package es.inatel.br.apphelp.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;
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
import es.inatel.br.apphelp.control.MenuPrincipalActivity;

/**
 * Created by felipe on 26/10/17.
 */

public class LoginDAO {

    private String caminho;

    private String tipoUsuario;

    private Context context;

    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private FirebaseUser user;


    public LoginDAO(Context context){
        this.context = context;
    }

    public void autenticacao(String email, String senha, final String tipoUsuario){

        caminho = "Usuarios/" + tipoUsuario;

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(context, "Usuário, senha ou tipo de usuário incorretos!", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                }else{

                }
            }
        });

        // Delay para corrigir problemas de tempo de autenticação
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        user = mAuth.getCurrentUser();


        if(user != null){
            database = new BancoDeDados().conexao(caminho);
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())){
                        mAuth.signOut();
                        Toast.makeText(context, "Usuário, senha ou tipo de usuário incorretos!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context, "Login efetuado com sucesso!", Toast.LENGTH_LONG).show();

                        Intent proximaPagina = new Intent(context, MenuPrincipalActivity.class);
                        proximaPagina.putExtra("tipoUsuario", tipoUsuario);
                        context.startActivity(proximaPagina);
                        ((Activity) context).finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, "Erro ao efetuar login!", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(context, "Erro ao efetuar login!", Toast.LENGTH_LONG).show();
        }

    }

    public void sair(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        if(mAuth.getCurrentUser() == null){
            Toast.makeText(context, "Logout feito com sucesso!!", Toast.LENGTH_LONG).show();
        }
    }

}
