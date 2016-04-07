package com.br.reserva_prototipo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.model.TipoReserva;
import com.br.reserva_prototipo.util.AppUtils;

import java.util.List;

/**
 * Created by MarioJ on 06/04/16.
 */
public class TipoReservaAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<TipoReserva> tiposReserva;

    public TipoReservaAdapter(Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public TipoReservaAdapter(Context context, List<TipoReserva> tiposReserva) {
        this(context);
        this.context = context;
        this.tiposReserva = tiposReserva;
    }

    @Override
    public int getCount() {
        return tiposReserva.size();
    }

    @Override
    public Object getItem(int position) {
        return tiposReserva.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tiposReserva.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;
        TipoReserva tipoReserva = tiposReserva.get(position);

        boolean def = tipoReserva.getTipo().equals("Selecione");

        if (view == null) {

            view = layoutInflater.inflate(R.layout.view_tipo_reserva, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

        } else
            viewHolder = (ViewHolder) view.getTag();

        if (!def)
            viewHolder.textCodigo.setText(String.valueOf(tipoReserva.getId()));

        viewHolder.textTipo.setText(tipoReserva.getTipo());

        if (!def)
            viewHolder.textValor.setText(AppUtils.getFormatCurrency().format(tipoReserva.getValor()));

        if (!def)
            viewHolder.textQuantidadePessoas.setText("para " + String.valueOf(tipoReserva.getQuantidadePessoas()) + " pessoa(s)");

        if (tipoReserva.getPromocaoReserva() != null && !def)
            viewHolder.textPromocao.setText(AppUtils.getFormatPercent().format(tipoReserva.getPromocaoReserva().getDesconto()) + "% desconto");
        else
            viewHolder.textPromocao.setVisibility(View.GONE);

        return view;
    }

    class ViewHolder {

        public TextView textCodigo;
        public TextView textTipo;
        public TextView textValor;
        public TextView textQuantidadePessoas;
        public TextView textPromocao;

        public ViewHolder(View view) {
            textCodigo = (TextView) view.findViewById(R.id.codigo);
            textTipo = (TextView) view.findViewById(R.id.tipo);
            textValor = (TextView) view.findViewById(R.id.valor);
            textQuantidadePessoas = (TextView) view.findViewById(R.id.quantidade_pessoas);
            textPromocao = (TextView) view.findViewById(R.id.promocao);
        }

    }
}
