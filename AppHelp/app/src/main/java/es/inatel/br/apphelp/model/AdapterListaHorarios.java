package es.inatel.br.apphelp.model;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import es.inatel.br.apphelp.R;
import es.inatel.br.apphelp.control.CriarHorarioActivity;

/**
 * Created by felipe on 30/10/17.
 */

public class AdapterListaHorarios extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<ListaHorarios> listaDias;
    private LayoutInflater inflater;

    public AdapterListaHorarios(Context context, ArrayList<ListaHorarios> listaDias) {
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
            convertView = inflater.inflate(R.layout.cabecalho_horarios, null);
        }

        ListaHorarios dias = getGroup(groupPosition);

        TextView titulo = (TextView) convertView.findViewById(R.id.cabecalhoID);

        titulo.setText(dias.getDiaDaSemana()+" ("+getChildrenCount(groupPosition)+")");

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_horarios, null);
        }

        final Horarios horarios = getChild(groupPosition, childPosition);

        TextView nome = (TextView) convertView.findViewById(R.id.viewHorarioNomeID);
        TextView hora = (TextView) convertView.findViewById(R.id.viewHorarioHoraID);
        TextView local = (TextView) convertView.findViewById(R.id.viewHorarioLocalID);

        ImageButton remover = (ImageButton) convertView.findViewById(R.id.removerHorarioID);
        ImageButton editar = (ImageButton) convertView.findViewById(R.id.editarHorarioID);

        editar.getBackground().setAlpha(0);
        remover.getBackground().setAlpha(0);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(groupPosition, childPosition);
            }
        });

        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remover(groupPosition, childPosition);
            }
        });


        hora.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        hora.setAllCaps(true);

        hora.setText(horarios.getHora().split("--")[1] + " - " + horarios.getTipo().split("--")[1]);
        nome.setText(horarios.getNome());
        local.setText("Local: " + horarios.getLocal());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void editar(int groupPosition, int childPosition){
        Horarios horarios = getChild(groupPosition, childPosition);

        String info[] = {horarios.getNome(), horarios.getCodigo(), horarios.getLocal(),
                horarios.getDiaDaSemana().split("--")[0], horarios.getHora().split("--")[0],
                horarios.getTipo().split("--")[0]};

        Intent proximaPagina = new Intent(context, CriarHorarioActivity.class);
        proximaPagina.putExtra("tipoUsuario", "Aluno");
        proximaPagina.putExtra("info", info);
        proximaPagina.putExtra("editar", true);

        context.startActivity(proximaPagina);
        ((Activity)context).finish();
    }

    public void remover(final int groupPosition, final int childPosition){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Remover horário");
        builder.setMessage("Confirmar remoção do horário?");
        builder.setCancelable(false);

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dia = getChild(groupPosition, childPosition).getDiaDaSemana();
                String hora = getChild(groupPosition, childPosition).getHora();
                new HorariosDAO(context).removerHorarios(dia, hora, true);
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
