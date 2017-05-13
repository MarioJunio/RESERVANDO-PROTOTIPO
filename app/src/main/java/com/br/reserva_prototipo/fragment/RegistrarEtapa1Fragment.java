package com.br.reserva_prototipo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.activity.Login;
import com.br.reserva_prototipo.application.GlobalApplication;
import com.br.reserva_prototipo.model.Usuario;

/**
 * Created by MarioJ on 12/07/16.
 */
public class RegistrarEtapa1Fragment extends Fragment {

    private EditText etNomeCompleto;
    private EditText etCPF;
    private Button btnAvancar;
    private Usuario usuario;

    public static RegistrarEtapa1Fragment newInstance() {
        return new RegistrarEtapa1Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // pega usuario que esta sendo cadastrado
        usuario = ((GlobalApplication) getActivity().getApplicationContext()).getUsuarioAtivo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_registrar_etapa_1, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        ((Login) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((Login) getActivity()).getSupportActionBar().setTitle("Nova conta - Passo 1");

        etNomeCompleto = (EditText) view.findViewById(R.id.et_nome_completo);
        etCPF = (EditText) view.findViewById(R.id.et_cpf);
        btnAvancar = (Button) view.findViewById(R.id.btn_avancar);

        btnAvancar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (etNomeCompleto.getText().toString().isEmpty()) {
                    etNomeCompleto.setError("Favor informar seu nome completo");
                    etNomeCompleto.requestFocus();
                    return;
                }

                if (etCPF.getText().toString().isEmpty()) {
                    etCPF.setError("Favor informar seu CPF");
                    etCPF.requestFocus();
                    return;
                }

                usuario.setNome(etNomeCompleto.getText().toString());
                usuario.setCpf(etCPF.getText().toString());

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, RegistrarEtapa2Fragment.newInstance()).addToBackStack("nova-conta-passo-1").commit();
            }
        });

        // carrega os campos do formulario
        carregarFormulario();
    }

    private void carregarFormulario() {
        etNomeCompleto.setText(usuario.getNome());
        etCPF.setText(usuario.getCpf());
    }
}
