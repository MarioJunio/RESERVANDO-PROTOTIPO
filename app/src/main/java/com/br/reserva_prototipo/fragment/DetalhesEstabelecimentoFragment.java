package com.br.reserva_prototipo.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.dialog.ReservarEstabelecimentoDialog;
import com.br.reserva_prototipo.holder.TipoReservaViewHolder;
import com.br.reserva_prototipo.model.Estabelecimento;
import com.br.reserva_prototipo.model.TipoReserva;
import com.br.reserva_prototipo.util.App;
import com.br.reserva_prototipo.util.Times;

/**
 * Created by MarioJ on 15/03/16.
 */
public class DetalhesEstabelecimentoFragment extends Fragment {

    private Estabelecimento estabelecimento;

    private ImageView icAberto;
    private TextView textAberto;
    private TextView textAcomodacao;
    private TextView textEndereco;
    private TextView textEnderecoBairro;
    private TextView textCidadeEstado;
    private Button btReservar;
    private LinearLayout layoutTiposReserva;
    private LinearLayout layoutAtrativos;

    // dialog
    private ReservarEstabelecimentoDialog reservarEstabelecimentoDialog;

    public DetalhesEstabelecimentoFragment() {
    }

    public DetalhesEstabelecimentoFragment(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public static DetalhesEstabelecimentoFragment newInstance(Estabelecimento estabelecimento) {
        return new DetalhesEstabelecimentoFragment(estabelecimento);
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
        btReservar = (Button) getActivity().findViewById(R.id.bt_reservar);
        layoutTiposReserva = (LinearLayout) view.findViewById(R.id.layout_tipos_reserva);
        layoutAtrativos = (LinearLayout) view.findViewById(R.id.layout_atrativos);

        App.setButtonTint(btReservar, Color.parseColor(getString(R.color.green)), PorterDuff.Mode.MULTIPLY);

        // checa se o estabelecimento esta aberto ou nao
        if (Times.isOpen(estabelecimento.getHorarioAbrir(), estabelecimento.getHorarioFechar()))
            App.opened(icAberto, textAberto, getActivity().getApplicationContext());
        else
            App.closed(icAberto, textAberto, getActivity().getApplicationContext());

        textAcomodacao.setText(getString(R.string.acomodacao) + " " + estabelecimento.getAcomodacao() + " " + getString(R.string.pessoas));
        textEndereco.setText(estabelecimento.getEndereco().getLogradouro() + " " + estabelecimento.getEndereco().getNumero());
        textEnderecoBairro.setText(estabelecimento.getEndereco().getBairro());
        textCidadeEstado.setText(estabelecimento.getEndereco().getCidade().getMunicipio() + " " + estabelecimento.getEndereco().getCidade().getUf());

        for (TipoReserva tipoReserva : estabelecimento.getTiposReserva()) {

            // inflate um item do tipo reserva
            View viewTipoReserva = LayoutInflater.from(getActivity()).inflate(R.layout.view_tipo_reserva, null);

            TipoReservaViewHolder tipoReservaViewHolder = new TipoReservaViewHolder(viewTipoReserva);
            tipoReservaViewHolder.textCodigo.setText(String.valueOf(tipoReserva.getId()));
            tipoReservaViewHolder.textTipo.setText(tipoReserva.getTipo());
            tipoReservaViewHolder.textValor.setText("R$ " + App.getFormatCurrency().format(tipoReserva.getValor()));
            tipoReservaViewHolder.textQuantidadePessoas.setText("para " + String.valueOf(tipoReserva.getQuantidadePessoas()) + " pessoa(s)");

            if (tipoReserva.getPromocaoReserva() != null)
                tipoReservaViewHolder.textPromocao.setText("Promoção " + App.getFormatPercent().format(tipoReserva.getPromocaoReserva().getDesconto()) + "% desconto");
            else
                tipoReservaViewHolder.textPromocao.setVisibility(View.GONE);

            // adiciona view do tipo reserva ao layout principal
            layoutTiposReserva.addView(viewTipoReserva);
        }

        // inicializa os eventos da view
        setupListeners();

    }

    public void setupListeners() {

        btReservar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                System.out.println(String.format("[%s] Tipos reserva: %s", estabelecimento.getNomeFantasia(), estabelecimento.getTiposReserva()));

                reservarEstabelecimentoDialog = new ReservarEstabelecimentoDialog((AppCompatActivity) getActivity(), estabelecimento);
                reservarEstabelecimentoDialog.prepare();
                reservarEstabelecimentoDialog.show();
            }
        });
    }
}
