package com.br.reserva_prototipo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.activity.Login;
import com.br.reserva_prototipo.application.GlobalApplication;
import com.br.reserva_prototipo.model.Usuario;

import java.util.Calendar;

/**
 * Created by MarioJ on 12/07/16.
 */
public class RegistrarEtapa2Fragment extends Fragment {

    private Button btnAvancar;
    private DatePicker dpDataAniversario;
    private Usuario usuario;

    public static RegistrarEtapa2Fragment newInstance() {
        return new RegistrarEtapa2Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = ((GlobalApplication) getActivity().getApplicationContext()).getUsuarioAtivo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_registrar_etapa_2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ((Login) getActivity()).getSupportActionBar().setTitle("Nova conta - Passo 2");

        btnAvancar = (Button) view.findViewById(R.id.btn_avancar);
        dpDataAniversario = (DatePicker) view.findViewById(R.id.dp_data_nascimento);

        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // pega data selecionada
                Calendar calendar = Calendar.getInstance();
                calendar.set(dpDataAniversario.getYear(), dpDataAniversario.getMonth(), dpDataAniversario.getDayOfMonth());

                usuario.setDataNascimento(calendar.getTime());

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, RegistrarEtapa3Fragment.newInstance()).addToBackStack("nova-conta-passo-2").commit();
            }
        });

        carregarFormulario();
    }

    private void carregarFormulario() {

        if (usuario.getDataNascimento() != null)
            dpDataAniversario.getCalendarView().setDate(usuario.getDataNascimento().getTime(), true, true);
    }
}
