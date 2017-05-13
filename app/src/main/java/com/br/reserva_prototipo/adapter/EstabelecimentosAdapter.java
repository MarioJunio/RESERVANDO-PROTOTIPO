package com.br.reserva_prototipo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.controller.EstabelecimentoController;
import com.br.reserva_prototipo.model.Estabelecimento;
import com.br.reserva_prototipo.util.ImageUtils.Images;
import com.br.reserva_prototipo.util.Times;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarioJ on 10/03/16.
 */
public class EstabelecimentosAdapter extends RecyclerView.Adapter<EstabelecimentosAdapter.EstabelecimentosViewHolder> {

    // contexto da aplicação
    private Context context;

    // lista de estabelecimentos para renderizar
    private List<Estabelecimento> estabelecimentos;

    // trata eventos de toque simples
    private View.OnClickListener onClickListener;

    // controladores
    private EstabelecimentoController estabelecimentoController;

    public EstabelecimentosAdapter(Context context) {
        this.context = context;

        // instancia lista de estabelecimentos
        estabelecimentos = new ArrayList<>();

        estabelecimentoController = EstabelecimentoController.create();
    }

    @Override
    public EstabelecimentosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EstabelecimentosViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_estabelecimento, parent, false));
    }

    @Override
    public void onBindViewHolder(EstabelecimentosViewHolder holder, int position) {

        // estabelecimento na posicao 'position'
        Estabelecimento estabelecimento = estabelecimentos.get(position);

        // verifica se o estabelecimento está aberto ou fechado
        boolean isOpen = Times.isOpen(estabelecimento.getHorarioAbrir(), estabelecimento.getHorarioFechar());

        holder.categoria.setText(estabelecimento.getCategoria().getNome());
        holder.nomeFantansia.setText(estabelecimento.getNomeFantasia());
        holder.icLock.setImageResource(isOpen ? R.drawable.ic_unlock : R.drawable.ic_lock);
        holder.horario.setText(isOpen ? Estabelecimento.Horario.Aberto.name() : Estabelecimento.Horario.Fechado.name());
        holder.localizacao.setText(estabelecimento.getEndereco().getCidade().getMunicipio());
        holder.fotoPerfil.setImageBitmap(Images.toBitmap(estabelecimento.getFoto()));

        // carrega foto de perfil
        estabelecimentoController.carregarFotoPerfil(context, estabelecimento, holder.fotoPerfil, holder.progress);

        // cor do texto que apresenta o horario
        holder.horario.setTextColor(isOpen ? Color.parseColor(context.getString(R.color.green)) : Color.parseColor(context.getString(R.color.red)));
        holder.icLock.setColorFilter(isOpen ? Color.parseColor(context.getString(R.color.green)) : Color.parseColor(context.getString(R.color.red)));

        holder.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return estabelecimentos.size();
    }

    public Estabelecimento getEstabelecimento(int index) {
        return estabelecimentos.get(index);
    }

    public void add(Estabelecimento estabelecimento) {
        estabelecimentos.add(estabelecimento);
        notifyItemInserted(estabelecimentos.size() - 1);
    }

    public void itemOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void delete(int index) {
        estabelecimentos.remove(index);
        notifyItemRemoved(index);
    }

    public void clear() {
        estabelecimentos.clear();
    }

    class EstabelecimentosViewHolder extends RecyclerView.ViewHolder {

        private View view;
        public TextView categoria;
        public ImageView fotoPerfil;
        public ProgressBar progress;
        public TextView nomeFantansia;
        private ImageView icLock;
        public TextView horario;
        public TextView localizacao;

        public EstabelecimentosViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            categoria = (TextView) itemView.findViewById(R.id.categoria);
            icLock = (ImageView) itemView.findViewById(R.id.ic_lock);
            fotoPerfil = (ImageView) itemView.findViewById(R.id.foto_perfil);
            progress = (ProgressBar) itemView.findViewById(R.id.progress);
            nomeFantansia = (TextView) itemView.findViewById(R.id.nome_fantasia);
            horario = (TextView) itemView.findViewById(R.id.horario);
            localizacao = (TextView) itemView.findViewById(R.id.localizacao);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            view.setOnClickListener(onClickListener);
        }

    }
}
