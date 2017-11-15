package es.inatel.br.apphelp.model;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.control.CriarAtividadeActivity;
import es.inatel.br.apphelp.control.MarcarPontoActivity;

/**
 * Created by felipe on 09/11/17.
 */

public class AdapterListaAtividades extends BaseAdapter{

    private Context context;
    private ArrayList<Atividades> listaAtividades;
    private ArrayList<String> listaAlunos;
    private LayoutInflater inflater;

    private DatabaseReference database;
    private FirebaseAuth mAuth;

    public AdapterListaAtividades(Context context, ArrayList<Atividades> listaAtividades, ArrayList<String> listaAlunos) {
        this.context = context;
        this.listaAtividades = listaAtividades;
        this.listaAlunos = listaAlunos;

        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listaAtividades.size();
    }

    @Override
    public Atividades getItem(int position) {
        return listaAtividades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_atividade, null);
        }

        TextView tipo = (TextView) convertView.findViewById(R.id.viewAtividadeTipoID);
        TextView nome = (TextView) convertView.findViewById(R.id.viewAtividadeNomeID);
        TextView tempo = (TextView) convertView.findViewById(R.id.viewAtividadeTempoObrigID);
        final TextView aluno = (TextView) convertView.findViewById(R.id.viewAtividadeAlunosID);

        ImageButton listar = (ImageButton) convertView.findViewById(R.id.listarTabelaAtividadesID);
        ImageButton editar = (ImageButton) convertView.findViewById(R.id.editarAtividadeID);
        ImageButton remover = (ImageButton) convertView.findViewById(R.id.removerAtividadeID);

        listar.getBackground().setAlpha(0);
        editar.getBackground().setAlpha(0);
        remover.getBackground().setAlpha(0);

        //Listeners dos botoes
        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removerAtividade(position);
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarAtividade(position);
            }
        });

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String caminho = "Usuarios/Aluno/"+listaAlunos.get(position)+"/nomeCompleto";
                database = new BancoDeDados().conexao(caminho);
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Intent proximaPagina = new Intent(context, MarcarPontoActivity.class);
                        proximaPagina.putExtra("tipoUsuario", "Administrador");
                        proximaPagina.putExtra("idAluno", listaAlunos.get(position));

                        proximaPagina.putExtra("nomeAluno", dataSnapshot.getValue().toString());
                        context.startActivity(proximaPagina);
                        ((Activity)context).finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Erro ao carregar planilha de pontos!", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        Atividades ativ = getItem(position);

        String type[] = ativ.getTipo().split("-");
        String time[] = ativ.getTempoMensal().split("-");

        tipo.setText(type[1]);
        nome.setText(ativ.getNome());
        tempo.setText(time[1]);

        String caminho = "Usuarios/Aluno/" + listaAlunos.get(position);
        database = new BancoDeDados().conexao(caminho);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                aluno.setText(dataSnapshot.getValue(Aluno.class).getNomeCompleto());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Erro ao carregar atividades!", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private void editarAtividade(int position) {
        mAuth = FirebaseAuth.getInstance();

        Atividades atividades = getItem(position);

        String idAluno = listaAlunos.get(position);
        String idAdmin = mAuth.getCurrentUser().getUid();

        String time[] = atividades.getTempoMensal().split("-");
        String type[] = atividades.getTipo().split("-");

        Intent proximaPagina = new Intent(context, CriarAtividadeActivity.class);
        proximaPagina.putExtra("tipoUsuario", "Administrador");
        proximaPagina.putExtra("editar", true);
        proximaPagina.putExtra("idAluno", idAluno);
        proximaPagina.putExtra("idAdmin", idAdmin);
        proximaPagina.putExtra("nome", atividades.getNome());
        proximaPagina.putExtra("tipo", type[0]);
        proximaPagina.putExtra("tempo", time[0]);

        context.startActivity(proximaPagina);
        ((Activity)context).finish();
    }

    private void removerAtividade(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Remover atividade");
        builder.setMessage("Confirmar remoção de atividade?");
        builder.setCancelable(false);

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth = FirebaseAuth.getInstance();

                String idAluno = listaAlunos.get(position);
                String idAdmin = mAuth.getCurrentUser().getUid();

                new AtividadesDAO(idAluno, idAdmin, context).removerAtividades();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create();
        builder.show();
    }
}
