package es.inatel.br.apphelp.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import es.inatel.br.apphelp.control.CadastroActivity;

/**
 * Created by felipe on 25/10/17.
 */

public class AdministradorDAO {

    private final String CAMINHO = "Usuarios/Administrador";
    private final CadastroActivity tela;
    private FirebaseAuth mAuth;
    private DatabaseReference user;

    public AdministradorDAO(CadastroActivity telaCadastro){
        this.tela = telaCadastro;
    }

    public FirebaseAuth cadastroAdm(Administrador adm){

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(adm.getEmail(), adm.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    mAuth.signOut();
                }
            }
        });

        if (mAuth.getCurrentUser() == null) return mAuth;

        DatabaseReference user = new BancoDeDados().conexao(CAMINHO);
        user.child(mAuth.getCurrentUser().getUid()).setValue(adm);

        return mAuth;
    }

    public Administrador visualizarPerfilAdm(String uId){
        Administrador adm;

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

    public boolean editarPerfilAluno(Administrador adm, String uId){

        user = new BancoDeDados().conexao(CAMINHO+uId);
        //Codigo para atualizar os valores
        return true;
    }
}
