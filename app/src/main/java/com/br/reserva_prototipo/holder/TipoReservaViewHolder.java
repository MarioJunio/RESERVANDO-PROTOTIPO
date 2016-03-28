package com.br.reserva_prototipo.holder;

import android.view.View;
import android.widget.TextView;

import com.br.reserva_prototipo.R;

/**
 * Created by MarioJ on 21/03/16.
 */
public class TipoReservaViewHolder {

    public TextView textCodigo;
    public TextView textTipo;
    public TextView textValor;
    public TextView textQuantidadePessoas;
    public TextView textPromocao;

    public TipoReservaViewHolder(View view) {
        textCodigo = (TextView) view.findViewById(R.id.codigo);
        textTipo = (TextView) view.findViewById(R.id.tipo);
        textValor = (TextView) view.findViewById(R.id.valor);
        textQuantidadePessoas = (TextView) view.findViewById(R.id.quantidade_pessoas);
        textPromocao = (TextView) view.findViewById(R.id.promocao);
    }
}
