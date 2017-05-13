package com.br.reserva_prototipo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.adapter.ReservasAdapter;
import com.br.reserva_prototipo.application.GlobalApplication;
import com.br.reserva_prototipo.controller.ReservaController;
import com.br.reserva_prototipo.model.Reserva;

/**
 * Created by MarioJ on 30/07/16.
 */
public class ListarReservasFragment extends Fragment {

    private GlobalApplication globalApplication;
    private RecyclerView rcReservas;

    private ReservaController reservaController;
    private ReservasAdapter reservasAdapter;

    // items do menu
    private MenuItem miDataReserva;
    private MenuItem miStatusReserva;

    public static ListarReservasFragment newInstance() {
        return new ListarReservasFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalApplication = (GlobalApplication) getActivity().getApplicationContext();
        reservaController = ReservaController.create();
        reservasAdapter = new ReservasAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_listar_reservas, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        rcReservas = (RecyclerView) view.findViewById(R.id.lista_reservas);
        rcReservas.setHasFixedSize(true);

        // cria gerenciador do layout do recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcReservas.setLayoutManager(layoutManager);
        rcReservas.setAdapter(reservasAdapter);

        buscarReservas(view);

        // cria adapter para acomodar os elementos do recyclerview
        reservasAdapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // posicao do layout clicado
                int position = rcReservas.getChildLayoutPosition(v);

                // Estabelecimento na posicao escolhida
                Reserva reserva = reservasAdapter.getReservaAtPosition(position);

                Toast.makeText(getContext(), "Reserva número " + reserva.getId(), Toast.LENGTH_SHORT).show();

                /*Estabelecimento estabelecimentoClone = SerializationUtils.clone(estabelecimento);
                estabelecimentoClone.setFoto(null);

                // escreve imagem no disco se houver, para recuperar na proxima tela
                Images.writePicture(String.valueOf(estabelecimento.getId()), Images.Identidade.Estabelecimento, estabelecimento.getFoto());

                // inicia tela de detalhes do estabelecimento
                Intent intent = new Intent(getActivity(), EstabelecimentoDetail.class);
                intent.putExtra(Estabelecimento.KEY, estabelecimentoClone);
                startActivity(intent);*/

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.listar_reservas_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        miDataReserva = menu.findItem(R.id.sort_data_reserva);
        miStatusReserva = menu.findItem(R.id.sort_status_reserva);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String title = item.getTitle().toString();

        if (item == miDataReserva) {

            if (title.equals(getString(R.string.sort_data_reserva_desc))) {
                miDataReserva.setIcon(R.drawable.ic_calendar_asc);
                miDataReserva.setTitle(getString(R.string.sort_data_reserva_asc));
                ordenarPorDataReserva(true);
            } else {
                miDataReserva.setIcon(R.drawable.ic_calendar_desc);
                miDataReserva.setTitle(getString(R.string.sort_data_reserva_desc));
                ordenarPorDataReserva(false);
            }

        } else if (item == miStatusReserva) {
            mostrarModalStatus();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ordenarPorDataReserva(boolean b) {
    }

    private void mostrarModalStatus() {
    }

    private void buscarReservas(View view) {
        reservaController.buscarTodas(view, globalApplication.getUsuarioAtivo(), reservasAdapter);
    }
}
