package es.inatel.br.apphelp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.Inflater;

import es.inatel.br.apphelp.R;

/**
 * Created by felipe on 09/11/17.
 */

public class AdapterPlanilhaHoras extends BaseExpandableListAdapter{

    private Context context;
    private ArrayList<Aluno> alunos;
    private LayoutInflater inflater;

    public AdapterPlanilhaHoras(Context context, ArrayList<Aluno> alunos) {
        this.context = context;
        this.alunos = alunos;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return alunos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return alunos.get(groupPosition).getAtividades().size();
    }

    @Override
    public Aluno getGroup(int groupPosition) {
        return alunos.get(groupPosition);
    }

    @Override
    public Atividades getChild(int groupPosition, int childPosition) {
        return alunos.get(groupPosition).getAtividades().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.cabecalho_tabela, null);
        }

        Aluno a = getGroup(groupPosition);

        TextView nome = (TextView) convertView.findViewById(R.id.alunoAtividadeID);


        nome.setText(a.getNomeCompleto());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.tabela_ponto, null);
        }

        Atividades atividades = getGroup(groupPosition).getAtividades().get(childPosition);

        TextView data = (TextView) convertView.findViewById(R.id.dataID);
        TextView entrada = (TextView) convertView.findViewById(R.id.entradaID);
        TextView saida = (TextView) convertView.findViewById(R.id.saidaID);
        TextView duracao = (TextView) convertView.findViewById(R.id.dataID);

        data.setText("Data atual");
        entrada.setText(atividades.getInicio());
        saida.setText(atividades.getFim());
        duracao.setText("Duração");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
