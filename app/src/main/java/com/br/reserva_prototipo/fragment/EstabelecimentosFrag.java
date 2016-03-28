package com.br.reserva_prototipo.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.activity.EstabelecimentoDetail;
import com.br.reserva_prototipo.adapter.EstabelecimentosAdapter;
import com.br.reserva_prototipo.model.Atrativo;
import com.br.reserva_prototipo.model.Cidade;
import com.br.reserva_prototipo.model.Endereco;
import com.br.reserva_prototipo.model.Estabelecimento;
import com.br.reserva_prototipo.model.Estado;
import com.br.reserva_prototipo.model.PromocaoReserva;
import com.br.reserva_prototipo.model.TipoReserva;
import com.br.reserva_prototipo.util.Images;

import org.apache.commons.lang3.SerializationUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MarioJ on 10/03/16.
 */
public class EstabelecimentosFrag extends Fragment {

    private static final String TAG = "EstabelecimentosFrag";
    private RecyclerView recEstabelecimentos;
    private EstabelecimentosAdapter estabelecimentosAdapter;

    public static Fragment newInstance() {
        return new EstabelecimentosFrag();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_estabelecimentos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupWidgets(view);

        createMock();
    }

    private void setupWidgets(View view) {

        recEstabelecimentos = (RecyclerView) view.findViewById(R.id.rec_estabelecimentos);
        recEstabelecimentos.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recEstabelecimentos.setLayoutManager(layoutManager);

        estabelecimentosAdapter = new EstabelecimentosAdapter(getActivity().getApplicationContext());
        estabelecimentosAdapter.itemOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // posicao do layout clicado
                int position = recEstabelecimentos.getChildLayoutPosition(v);

                // Estabelecimento na posicao escolhida
                Estabelecimento estabelecimento = estabelecimentosAdapter.getEstabelecimento(position);

                Estabelecimento estabelecimentoClone = SerializationUtils.clone(estabelecimento);
                estabelecimentoClone.setFotoPerfil(null);

                // escreve imagem no disco, para recuperar na proxima tela
                Images.writePicture(estabelecimento.getId(), estabelecimento.getFotoPerfil());

                // inicia tela de detalhes do estabelecimento
                Intent intent = new Intent(getActivity(), EstabelecimentoDetail.class);
                intent.putExtra(Estabelecimento.KEY, estabelecimentoClone);
                startActivity(intent);

            }
        });

        recEstabelecimentos.setAdapter(estabelecimentosAdapter);
    }

    private void createMock() {

        Estado estado = new Estado();
        estado.setId(1l);
        estado.setUf("MG");

        Cidade cidade = new Cidade();
        cidade.setId(1L);
        cidade.setNome("Monte Carmelo");
        cidade.setEstado(estado);

        Endereco endereco = new Endereco();
        endereco.setId(1L);
        endereco.setCidade(cidade);
        endereco.setCep("38500-000");
        endereco.setBairro("Jardim dos Ipes");
        endereco.setLogradouro("Rua duarte da costa");
        endereco.setNumero(1575);

        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setId(1l);
        estabelecimento.setNomeFantasia("Outback Steak House");
        estabelecimento.setAcomodacao(600);
        estabelecimento.setDescricao("Teste de descricao");
        estabelecimento.setFotoPerfil(Images.parse(((BitmapDrawable) getResources().getDrawable(R.drawable.outback)).getBitmap()));
        estabelecimento.setHorarioAbrir(criarHorario(10, 40));
        estabelecimento.setHorarioFechar(criarHorario(14, 10));
        estabelecimento.setCategoria(Estabelecimento.Categoria.RESTAURANTE);
        estabelecimento.setEndereco(endereco);

        Estabelecimento estabelecimento2 = new Estabelecimento();
        estabelecimento2.setId(2l);
        estabelecimento2.setNomeFantasia("Ratinho Lanches");
        estabelecimento2.setAcomodacao(20);
        estabelecimento2.setDescricao("Teste de descricao");
        estabelecimento2.setFotoPerfil(Images.parse(((BitmapDrawable) getResources().getDrawable(R.drawable.outback)).getBitmap()));
        estabelecimento2.setHorarioAbrir(criarHorario(19, 10));
        estabelecimento2.setHorarioFechar(criarHorario(23, 59));
        estabelecimento2.setCategoria(Estabelecimento.Categoria.LANCHONETE);
        estabelecimento2.setEndereco(endereco);

        PromocaoReserva promocaoReserva = new PromocaoReserva();
        promocaoReserva.setId(1L);
        promocaoReserva.setDataInicio(new Date());
        promocaoReserva.setFataFim(new Date());
        promocaoReserva.setDesconto(5);

        TipoReserva tipoReserva = new TipoReserva();
        tipoReserva.setId(1L);
        tipoReserva.setQuantidadePessoas(1);
        tipoReserva.setTipo("NORMAL");
        tipoReserva.setValor(new BigDecimal(104.3));
        tipoReserva.setEstabelecimento(estabelecimento);
        tipoReserva.setPromocaoReserva(promocaoReserva);

        TipoReserva tipoReserva2 = new TipoReserva();
        tipoReserva2.setId(2L);
        tipoReserva2.setQuantidadePessoas(2);
        tipoReserva2.setTipo("TWO HALF");
        tipoReserva2.setValor(new BigDecimal(174.3));
        tipoReserva2.setEstabelecimento(estabelecimento);

        Atrativo atrativo1 = new Atrativo();
        atrativo1.setId(1L);
        atrativo1.setDescricao("Navegue na internet com conforto e rapidez.");
        atrativo1.setTipoAtrativo(Atrativo.TipoAtrativo.WIFI);
        atrativo1.setEstabelecimento(estabelecimento);

        estabelecimento.getTiposReserva().add(tipoReserva);
        estabelecimento.getTiposReserva().add(tipoReserva2);
        estabelecimento.getAtrativos().add(atrativo1);

        for (TipoReserva tp : estabelecimento.getTiposReserva())
            Log.d(TAG, tp.toString());

        estabelecimentosAdapter.add(estabelecimento);
        estabelecimentosAdapter.add(estabelecimento2);
    }

    private Calendar criarHorario(int horario, int minutos) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, horario);
        calendar.set(Calendar.MINUTE, minutos);

        return calendar;
    }

}
