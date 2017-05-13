package com.br.reserva_prototipo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.activity.EstabelecimentoDetail;
import com.br.reserva_prototipo.adapter.EstabelecimentosAdapter;
import com.br.reserva_prototipo.controller.EstabelecimentoController;
import com.br.reserva_prototipo.dialog.FiltroEstabelecimentosDialog;
import com.br.reserva_prototipo.model.Estabelecimento;
import com.br.reserva_prototipo.model.filter.FiltroEstabelecimentos;
import com.br.reserva_prototipo.util.GpsUtils.GPS;
import com.br.reserva_prototipo.util.ImageUtils.Images;

import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by MarioJ on 10/03/16.
 */
public class ListarEstabelecimentosFragment extends Fragment {

    private RecyclerView estabelecimentosView;
    private EstabelecimentosAdapter estabelecimentosAdapter;
    private FloatingActionButton fabFilter;
    private FiltroEstabelecimentosDialog filtroEstabelecimentosDialog;

    private MenuItem miSortPopular;
    private MenuItem miSortPreco;

    // Filtro de estabelecimentos
    private FiltroEstabelecimentos filtroEstabelecimentos;

    // controladores
    private EstabelecimentoController estabelecimentoController;

    public static Fragment newInstance() {
        return new ListarEstabelecimentosFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        estabelecimentoController = EstabelecimentoController.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_listar_estabelecimentos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupWidgets(view);

        // busca os estabelecimentos no back-end
        buscaEstabelecimentos(view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_estabelecimento_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);

        miSortPopular = menu.findItem(R.id.sort_mais_popular);
        miSortPreco = menu.findItem(R.id.sort_menos_caro);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String title = item.getTitle().toString();

        if (item == miSortPopular) {

            if (title.equals(getString(R.string.sort_mais_popular))) {
                miSortPopular.setIcon(R.drawable.ic_piores);
                miSortPopular.setTitle(getString(R.string.sort_menos_popular));
                //TODO: realizar busca por estabeleciementos ordenados pela quantidade de votos mais alta
                ordenarPorPopularidade(true);
            } else {
                miSortPopular.setIcon(R.drawable.ic_melhores);
                miSortPopular.setTitle(getString(R.string.sort_mais_popular));
                //TODO: realizar busca por estabelecimentos ordenados pela quantiade de votos mais baixa
                ordenarPorPopularidade(false);
            }

        } else if (item == miSortPreco) {

            if (title.equals(getString(R.string.sort_maior_preco))) {
                miSortPreco.setIcon(R.drawable.ic_cheaper);
                miSortPreco.setTitle(getString(R.string.sort_menor_preco));
            } else {
                miSortPreco.setIcon(R.drawable.ic_expensive);
                miSortPreco.setTitle(getString(R.string.sort_maior_preco));
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void ordenarPorPopularidade(boolean maisPopular) {
        filtroEstabelecimentos.setOrdenarPor(Estabelecimento.SortField.rates);
        filtroEstabelecimentos.setOrdenacaoAsc(!maisPopular);
    }

    private void setupWidgets(View view) {

        fabFilter = (FloatingActionButton) getActivity().findViewById(R.id.fab_filter);
        estabelecimentosView = (RecyclerView) view.findViewById(R.id.rec_estabelecimentos);
        estabelecimentosView.setHasFixedSize(true);

        // cria gerenciador do layout do recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        estabelecimentosView.setLayoutManager(layoutManager);

        // cria adapter para acomodar os elementos do recyclerview
        estabelecimentosAdapter = new EstabelecimentosAdapter(getActivity().getApplicationContext());
        estabelecimentosAdapter.itemOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // posicao do layout clicado
                int position = estabelecimentosView.getChildLayoutPosition(v);

                // Estabelecimento na posicao escolhida
                Estabelecimento estabelecimento = estabelecimentosAdapter.getEstabelecimento(position);

                Estabelecimento estabelecimentoClone = SerializationUtils.clone(estabelecimento);
                estabelecimentoClone.setFoto(null);

                // escreve imagem no disco se houver, para recuperar na proxima tela
                Images.writePicture(String.valueOf(estabelecimento.getId()), Images.Identidade.Estabelecimento, estabelecimento.getFoto());

                // inicia tela de detalhes do estabelecimento
                Intent intent = new Intent(getActivity(), EstabelecimentoDetail.class);
                intent.putExtra(Estabelecimento.KEY, estabelecimentoClone);
                startActivity(intent);

            }
        });

        estabelecimentosView.setAdapter(estabelecimentosAdapter);

        // evento de click no botao de filtro de estabelecimentos
        fabFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                filtroEstabelecimentosDialog.createAndShow();
            }
        });

        // instancia dialog de filtro
        filtroEstabelecimentosDialog = new FiltroEstabelecimentosDialog(getContext(), estabelecimentosAdapter, view);
    }

    /**
     * Realiza a busca de estabelecimentos utilizando um Filtro especifico no back-end
     */
    private void buscaEstabelecimentos(View view) {

        String cidade = null;

        try {
            cidade = GPS.getCidadeAtual(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (cidade == null)
            cidade = "Monte Carmelo";

        Log.d("DEBUG", "--- Localidade Atual ---> " + cidade);

        filtroEstabelecimentos = filtroEstabelecimentosDialog.criarFiltroPadraoPorCidade(cidade);

        try {
            // tenta buscar estabelecimentos
            estabelecimentoController.buscarTodosToView(getActivity(), view, filtroEstabelecimentos, estabelecimentosAdapter);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
