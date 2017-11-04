package es.inatel.br.apphelp.model;

import android.content.Context;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import es.inatel.br.apphelp.R;

/**
 * Created by felipe on 30/10/17.
 */

public class ListaAdapterHorario extends ArrayAdapter<Horarios> {

    private Context context;
    private ArrayList<Horarios> lista;

    private TextView hora;
    private TextView tipo;
    private TextView nome;
    private TextView codigo;
    private TextView local;

    public ListaAdapterHorario(@NonNull Context context, ArrayList<Horarios> lista) {
        super(context,0, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Horarios horarioPosicao = this.lista.get(position);

        convertView = LayoutInflater.from(this.context).
                inflate(R.layout.list_view_horario, null);


        referenciaComponentes(convertView);
        adicionaTextos(horarioPosicao);

        return convertView;
    }

    private void adicionaTextos(Horarios horarios) {
        hora.setText("Hora: " + horarios.getHora());
        hora.setAllCaps(true);
        hora.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        hora.setText("Hora: " + horarios.getHora());
        tipo.setText("Tipo: " + horarios.getTipo());
        nome.setText("Nome: " + horarios.getNome());
        codigo.setText("Código: " + horarios.getCodigo());
        local.setText("Local: " + horarios.getLocal());
    }

    public void referenciaComponentes(View convertView){
        hora =      (TextView) convertView.findViewById(R.id.viewHorarioHoraID);
        tipo =      (TextView) convertView.findViewById(R.id.viewHorarioTipoID);
        nome =      (TextView) convertView.findViewById(R.id.viewHorarioNomeID);
        codigo =    (TextView) convertView.findViewById(R.id.viewHorarioCodigoID);
        local =     (TextView) convertView.findViewById(R.id.viewHorarioLocalID);;
    }
}
