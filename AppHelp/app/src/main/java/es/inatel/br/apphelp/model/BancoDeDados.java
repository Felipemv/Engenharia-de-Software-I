package es.inatel.br.apphelp.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by felipe on 27/09/17.
 */

public class BancoDeDados{


    private FirebaseAuth mAuth;
    private FirebaseDatabase dataBase;
    private DatabaseReference user;

    //Realiza a conexão com o firebase no caminho especificado
    public DatabaseReference conexao(String caminho){

        dataBase = FirebaseDatabase.getInstance();
        user = dataBase.getReference(caminho);
        return user;
    }


}
