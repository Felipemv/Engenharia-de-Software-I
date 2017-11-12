package es.inatel.br.apphelp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import es.inatel.br.apphelp.R;

/**
 * Created by felipe on 09/11/17.
 */

public class AdapterListaAtividades extends BaseAdapter{

    private Context context;
    private HashMap<String,Atividades> listaAtividades;
    private ArrayList<String> listaAlunos;
    private LayoutInflater inflater;

    private DatabaseReference database;


    public AdapterListaAtividades(Context context, HashMap<String, Atividades> listaAtividades, ArrayList<String> listaAlunos) {
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
        return listaAtividades.get(listaAlunos.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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

        Atividades ativ = getItem(position);
        Set<String> keySet = listaAtividades.keySet();
        Object[] keySetArray =  keySet.toArray();

        tipo.setText(ativ.getTipo());
        nome.setText(ativ.getNome());
        tempo.setText("20/" + ativ.getTempo_mensal());

        String caminho = "Usuarios/Aluno/" + keySetArray[position].toString();
        database = new BancoDeDados().conexao(caminho);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                aluno.setText(dataSnapshot.getValue(Aluno.class).getNomeCompleto());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return convertView;
    }
}
