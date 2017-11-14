package es.inatel.br.apphelp.model;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.inatel.br.apphelp.R;

/**
 * Created by felipe on 09/11/17.
 */

public class AdapterPlanilhaHoras extends BaseAdapter {

    private Context context;
    private ArrayList<Ponto> pontos;
    private LayoutInflater inflater;

    private int posicao;

    public AdapterPlanilhaHoras(Context context, ArrayList<Ponto> pontos) {
        this.context = context;
        this.pontos = pontos;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return pontos.size();
    }

    @Override
    public Ponto getItem(int position) {
        return pontos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.tabela_ponto, null);
        }

        Ponto point = getItem(position);


        TextView data = (TextView) convertView.findViewById(R.id.dataTabelaID);
        TextView entrada = (TextView) convertView.findViewById(R.id.entradaTabelaID);
        TextView saida = (TextView) convertView.findViewById(R.id.saidaTabelaID);

        if(position == 0)data.setText(point.getData().replace(":", "/"));
        else    data.setText(point.getData().replace(":", "/").substring(0,5));


        entrada.setText(point.getEntrada());
        saida.setText(point.getSaida());

        return convertView;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

}
