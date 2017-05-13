package com.br.reserva_prototipo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.controller.EstabelecimentoController;
import com.br.reserva_prototipo.model.Reserva;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarioJ on 01/08/16.
 */
public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ReservaViewHolder> {

    private Context context;
    private List<Reserva> listaReservas;
    private View.OnClickListener clickListener;

    private EstabelecimentoController estabelecimentoController;

    private LayoutInflater layoutInflater;

    private SimpleDateFormat dateFormat;

    public ReservasAdapter(Context context) {
        this.context = context;

        listaReservas = new ArrayList<>();
        estabelecimentoController = EstabelecimentoController.create();
        layoutInflater = LayoutInflater.from(context);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy ' ás ' HH:mm");
    }

    @Override
    public ReservasAdapter.ReservaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReservaViewHolder(layoutInflater.inflate(R.layout.card_reserva, parent, false));
    }

    @Override
    public void onBindViewHolder(ReservaViewHolder holder, int position) {

        Reserva reserva = listaReservas.get(position);

        if (reserva != null) {

            // carrega foto de perfil do estabelecimeto
            estabelecimentoController.carregarFotoPerfil(context, reserva.getEstabelecimento(), holder.imgEstabelecimento, holder.progressBar);

            // informacoes da reserva
            holder.textNumeroReserva.setText(String.valueOf(reserva.getId()));
            holder.textTipoReserva.setText(reserva.getTipoReserva().getTipo());
            holder.textNomeEstabelecimento.setText(reserva.getEstabelecimento().getNomeFantasia());

            if (reserva.getDataReserva() != null)
                holder.textDataReserva.setText(dateFormat.format(reserva.getDataReserva()));
            else
                holder.textDataReserva.setText("Indisponível");

            holder.textStatusReserva.setText(reserva.getStatusReserva().getNome());

            // evento ao clicar em uma reserva
            holder.setOnClickListener(clickListener);
        }

    }

    @Override
    public int getItemCount() {
        return listaReservas.size();
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public Reserva getReservaAtPosition(int position) {
        return listaReservas.get(position);
    }

    public void add(Reserva reserva) {
        listaReservas.add(reserva);
        notifyItemInserted(listaReservas.size() - 1);
    }

    public void remover(Reserva reserva) {
        remover(listaReservas.indexOf(reserva));
    }

    public void remover(int position) {
        listaReservas.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        listaReservas.clear();
        notifyDataSetChanged();
    }

    class ReservaViewHolder extends RecyclerView.ViewHolder {

        protected View view;
        protected ImageView imgEstabelecimento;
        protected TextView textNumeroReserva;
        protected TextView textTipoReserva;
        protected TextView textNomeEstabelecimento;
        protected TextView textDataReserva;
        protected TextView textStatusReserva;
        protected ProgressBar progressBar;

        public ReservaViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            imgEstabelecimento = (ImageView) view.findViewById(R.id.estabelecimento_foto);
            textNumeroReserva = (TextView) view.findViewById(R.id.reserva_numero);
            textTipoReserva = (TextView) view.findViewById(R.id.tipo_reserva);
            textNomeEstabelecimento = (TextView) view.findViewById(R.id.estabelecimento_nome);
            textDataReserva = (TextView) view.findViewById(R.id.data_reserva);
            textStatusReserva = (TextView) view.findViewById(R.id.status_reserva);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            view.setOnClickListener(onClickListener);
        }

    }
}
