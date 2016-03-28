package com.br.reserva_prototipo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.holder.TipoReservaViewHolder;
import com.br.reserva_prototipo.model.Estabelecimento;
import com.br.reserva_prototipo.model.TipoReserva;
import com.br.reserva_prototipo.util.AppUtils;
import com.br.reserva_prototipo.util.Times;

import java.text.DecimalFormat;

/**
 * Created by MarioJ on 15/03/16.
 */
public class EstabelecimentoDetailFrag extends Fragment {

    private static final String TAG = "EstabelecimentoDetailFrag";

    private Estabelecimento estabelecimento;

    private ImageView icAberto;
    private TextView textAberto;
    private TextView textAcomodacao;
    private TextView textEndereco;
    private TextView textEnderecoBairro;
    private TextView textCidadeEstado;
    private LinearLayout layoutTiposReserva;
    private LinearLayout layoutAtrativos;

    public EstabelecimentoDetailFrag(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public static EstabelecimentoDetailFrag newInstance(Estabelecimento estabelecimento) {
        return new EstabelecimentoDetailFrag(estabelecimento);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_estabelecimento_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        icAberto = (ImageView) view.findViewById(R.id.ic_aberto);
        textAberto = (TextView) view.findViewById(R.id.text_aberto);
        textAcomodacao = (TextView) view.findViewById(R.id.text_acomodacao);
        textEndereco = (TextView) view.findViewById(R.id.text_endereco);
        textEnderecoBairro = (TextView) view.findViewById(R.id.text_endereco_bairro);
        textCidadeEstado = (TextView) view.findViewById(R.id.text_cidade_estado);
        layoutTiposReserva = (LinearLayout) view.findViewById(R.id.layout_tipos_reserva);
        layoutAtrativos = (LinearLayout) view.findViewById(R.id.layout_atrativos);

        // checa se o estabelecimento esta aberto ou nao
        if (Times.isOpen(estabelecimento.getHorarioAbrir(), estabelecimento.getHorarioFechar()))
            AppUtils.opened(icAberto, textAberto, getActivity().getApplicationContext());
        else
            AppUtils.closed(icAberto, textAberto, getActivity().getApplicationContext());

        textAcomodacao.setText(getString(R.string.acomodacao) + " " + estabelecimento.getAcomodacao() + " " + getString(R.string.pessoas));
        textEndereco.setText(estabelecimento.getEndereco().getLogradouro() + " " + estabelecimento.getEndereco().getNumero());
        textEnderecoBairro.setText(estabelecimento.getEndereco().getBairro());
        textCidadeEstado.setText(estabelecimento.getEndereco().getCidade().getNome() + " " + estabelecimento.getEndereco().getCidade().getEstado().getUf());

        for (TipoReserva tipoReserva : estabelecimento.getTiposReserva()) {

            DecimalFormat formatCurrency = new DecimalFormat("#,###.00");
            DecimalFormat formatPercent = new DecimalFormat("#.#");

            // inflate um item do tipo reserva
            View viewTipoReserva = LayoutInflater.from(getActivity()).inflate(R.layout.view_tipo_reserva, null);

            TipoReservaViewHolder tipoReservaViewHolder = new TipoReservaViewHolder(viewTipoReserva);
            tipoReservaViewHolder.textCodigo.setText(String.valueOf(tipoReserva.getId()));
            tipoReservaViewHolder.textTipo.setText(tipoReserva.getTipo());
            tipoReservaViewHolder.textValor.setText("R$ " + formatCurrency.format(tipoReserva.getValor()));
            tipoReservaViewHolder.textQuantidadePessoas.setText("para " + String.valueOf(tipoReserva.getQuantidadePessoas()) + " pessoa(s)");

            if (tipoReserva.getPromocaoReserva() != null)
                tipoReservaViewHolder.textPromocao.setText("Promoção " + formatPercent.format(tipoReserva.getPromocaoReserva().getDesconto()) + "% desconto");
            else
                tipoReservaViewHolder.textPromocao.setVisibility(View.GONE);

            // adiciona view do tipo reserva ao layout principal
            layoutTiposReserva.addView(viewTipoReserva);
        }

    }
}
