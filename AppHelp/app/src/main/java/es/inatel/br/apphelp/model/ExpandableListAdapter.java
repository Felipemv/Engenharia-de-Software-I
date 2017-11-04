package es.inatel.br.apphelp.model;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import es.inatel.br.apphelp.R;

/**
 * Created by felipe on 30/10/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private TextView titulo;
    private TextView nome;
    private TextView hora;
    private TextView local;

    private ImageButton remover;
    private ImageButton editar;

    private Context context;
    private ArrayList<ListaHorarios> listaDias;
    private LayoutInflater inflater;

    public ExpandableListAdapter(Context context, ArrayList<ListaHorarios> listaDias) {
        this.context = context;
        this.listaDias = listaDias;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getGroupCount() {
        return listaDias.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listaDias.get(groupPosition).getHorarios().size();
    }

    @Override
    public ListaHorarios getGroup(int groupPosition) {
        return listaDias.get(groupPosition);
    }

    @Override
    public Horarios getChild(int groupPosition, int childPosition) {
        return listaDias.get(groupPosition).getHorarios().get(childPosition);
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

        if (convertView == null){
            convertView = inflater.inflate(R.layout.cabecalho, null);
        }

        ListaHorarios dias = getGroup(groupPosition);

        titulo = (TextView) convertView.findViewById(R.id.cabecalhoID);

        titulo.setText(dias.getDiaDaSemana());

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_horarios, null);
        }

        final Horarios horarios = getChild(groupPosition, childPosition);

        nome = (TextView) convertView.findViewById(R.id.viewHorarioNomeID);
        hora = (TextView) convertView.findViewById(R.id.viewHorarioHoraID);
        local = (TextView) convertView.findViewById(R.id.viewHorarioLocalID);

        remover = (ImageButton) convertView.findViewById(R.id.removerHorarioID);
        editar = (ImageButton) convertView.findViewById(R.id.editarHorarioID);


        hora.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        hora.setAllCaps(true);

        hora.setText(horarios.getHora() + " - " + horarios.getTipo());
        nome.setText(horarios.getNome());
        local.setText("Local: " + horarios.getLocal());

        remover.setVisibility(View.VISIBLE);
        editar.setVisibility(View.VISIBLE);
        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaDias.get(groupPosition).getHorarios().remove(childPosition);
                //new HorariosDAO().removerHorarios();
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
