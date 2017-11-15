package es.inatel.br.apphelp.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import es.inatel.br.apphelp.control.LoginActivity;
import es.inatel.br.apphelp.control.MenuPrincipalActivity;
import es.inatel.br.apphelp.control.PerfilActivity;

/**
 * Created by felipe on 28/10/17.
 */

public class UsuarioDAO {

    private String caminho;
    private String tipoUsuario;
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    private Context context;

    private Usuario usuario;

    public UsuarioDAO(Usuario usuario, Context context){
        if(usuario instanceof Aluno)   tipoUsuario = "Aluno";
        else                           tipoUsuario = "Administrador";

        this.usuario = usuario;
        this.context = context;

        caminho = "Usuarios/"+tipoUsuario+"/";
    }

    //Cadastra email e senha de autenticação e adiciona perfil no banco de dados
    public void cadastroUsuario(String email, String senha){
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = mAuth.getCurrentUser().getUid();

                    database = new BancoDeDados().conexao(caminho+id);
                    database.setValue(usuario);

                    Intent proximaPagina = new Intent(context, LoginActivity.class);
                    context.startActivity(proximaPagina);
                    ((Activity)context).finish();

                    Toast.makeText(context, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context, "Falha ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Edita informações do perfil do usuário
    public void editarPerfil(final String uId){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Editar perfil");
        builder.setMessage("Confirmar edição do perfil?");
        builder.setCancelable(false);

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database = new BancoDeDados().conexao("");

                Map<String, Object> childUpdates = new HashMap<>();

                childUpdates.put(caminho + uId+"/nomeCompleto", usuario.getNomeCompleto());
                childUpdates.put(caminho + uId+"/email", usuario.getEmail());
                childUpdates.put(caminho + uId+"/telefoneContato", usuario.getTelefoneContato());

                if(tipoUsuario.equals("Aluno")){
                    childUpdates.put(caminho + uId+"/curso", ((Aluno)usuario).getCurso());
                    childUpdates.put(caminho + uId+"/periodo", ((Aluno)usuario).getPeriodo());
                    childUpdates.put(caminho + uId+"/matricula", ((Aluno)usuario).getMatricula());
                }else{
                    childUpdates.put(caminho + uId+"/ocupacao", ((Administrador)usuario).getOcupacao());
                    childUpdates.put(caminho + uId+"/atividadeResponsavel", ((Administrador)usuario).getAtividadeResponsavel());
                }
                try {

                    database.updateChildren(childUpdates);
                    Toast.makeText(context, "Perfil atualizado com sucesso!", Toast.LENGTH_LONG).show();

                    Intent proximaPagina = new Intent(context, MenuPrincipalActivity.class);
                    proximaPagina.putExtra("tipoUsuario", tipoUsuario);

                    context.startActivity(proximaPagina);
                    ((Activity)context).finish();
                }catch (Exception e){
                    Toast.makeText(context, "Erro ao atualizar perfil!", Toast.LENGTH_LONG);
                }
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(context, "Retornando ao menu!", Toast.LENGTH_LONG);

                Intent proximaPagina = new Intent(context, MenuPrincipalActivity.class);
                proximaPagina.putExtra("tipoUsuario", tipoUsuario);

                context.startActivity(proximaPagina);
                ((Activity)context).finish();
            }
        });

        builder.create();
        builder.show();


    }
}
