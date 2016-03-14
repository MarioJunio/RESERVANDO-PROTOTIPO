package com.br.reserva_prototipo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.model.Estabelecimento;
import com.br.reserva_prototipo.util.Images;
import com.br.reserva_prototipo.util.Times;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarioJ on 10/03/16.
 */
public class EstabelecimentosAdapter extends RecyclerView.Adapter<EstabelecimentosAdapter.EstabelecimentosViewHolder> {

    private Context context;

    private List<Estabelecimento> estabelecimentos;

    public EstabelecimentosAdapter(Context context) {
        this.context = context;

        estabelecimentos = new ArrayList<>();
    }

    @Override
    public EstabelecimentosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EstabelecimentosViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_estabelecimento, parent, false));
    }

    @Override
    public void onBindViewHolder(EstabelecimentosViewHolder holder, int position) {

        // estabelecimento na posicao 'position'
        Estabelecimento estabelecimento = estabelecimentos.get(position);

        boolean isOpen = Times.isOpen(estabelecimento.getHorarioAbrir(), estabelecimento.getHorarioFechar());

        holder.categoria.setText(estabelecimento.getCategoria().getNome());
        holder.nomeFantansia.setText(estabelecimento.getNomeFantasia());
        holder.icLock.setImageResource(isOpen ? R.drawable.ic_unlock : R.drawable.ic_lock);
        holder.horario.setText(isOpen ? Estabelecimento.Horario.Aberto.name() : Estabelecimento.Horario.Fechado.name());
        holder.localizacao.setText(estabelecimento.getEndereco().getCidade().getNome());
        holder.fotoPerfil.setImageBitmap(Images.bitmap(estabelecimento.getFotoPerfil()));

        // cor do texto que apresenta o horario
        holder.horario.setTextColor(isOpen ? Color.parseColor(context.getString(R.color.green)) : Color.parseColor(context.getString(R.color.red)));
        holder.icLock.setColorFilter(isOpen ? Color.parseColor(context.getString(R.color.green)) : Color.parseColor(context.getString(R.color.red)));
    }

    @Override
    public int getItemCount() {
        return estabelecimentos.size();
    }

    public void add(Estabelecimento estabelecimento) {
        estabelecimentos.add(estabelecimento);
        notifyItemInserted(estabelecimentos.size() - 1);
    }

    public void delete(int index) {
        estabelecimentos.remove(index);
        notifyItemRemoved(index);
    }

    class EstabelecimentosViewHolder extends RecyclerView.ViewHolder {

        public TextView categoria;
        private ImageView fotoPerfil;
        public TextView nomeFantansia;
        private ImageView icLock;
        public TextView horario;
        public TextView localizacao;

        public EstabelecimentosViewHolder(View itemView) {
            super(itemView);

            categoria = (TextView) itemView.findViewById(R.id.categoria);
            icLock = (ImageView) itemView.findViewById(R.id.ic_lock);
            fotoPerfil = (ImageView) itemView.findViewById(R.id.foto_perfil);
            nomeFantansia = (TextView) itemView.findViewById(R.id.nome_fantasia);
            horario = (TextView) itemView.findViewById(R.id.horario);
            localizacao = (TextView) itemView.findViewById(R.id.localizacao);
        }
    }
}
