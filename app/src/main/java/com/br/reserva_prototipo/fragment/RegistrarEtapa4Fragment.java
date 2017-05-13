package com.br.reserva_prototipo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.activity.Login;
import com.br.reserva_prototipo.activity.Main;
import com.br.reserva_prototipo.application.GlobalApplication;
import com.br.reserva_prototipo.controller.AuthController;
import com.br.reserva_prototipo.controller.UsuarioController;
import com.br.reserva_prototipo.model.Usuario;
import com.br.reserva_prototipo.net.Callback;
import com.br.reserva_prototipo.session.SessionManager;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;

/**
 * Created by MarioJ on 12/07/16.
 */
public class RegistrarEtapa4Fragment extends Fragment {

    private EditText etEmail;
    private EditText etSenha, etSenha2;
    private Button btnConcluir;
    private Usuario usuario;
    private ProgressBar progressBar;

    // controladores
    private AuthController authController;
    private UsuarioController usuarioController;

    public static RegistrarEtapa4Fragment newInstance() {
        return new RegistrarEtapa4Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = ((GlobalApplication) getActivity().getApplicationContext()).getUsuarioAtivo();
        authController = AuthController.create();
        usuarioController = UsuarioController.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_registrar_etapa_4, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        ((Login) getActivity()).getSupportActionBar().setTitle("Nova conta - Passo 4");

        progressBar = ((Login) getActivity()).progressBar;
        etEmail = (EditText) view.findViewById(R.id.et_email);
        etSenha = (EditText) view.findViewById(R.id.et_senha);
        etSenha2 = (EditText) view.findViewById(R.id.et_senha2);
        btnConcluir = (Button) view.findViewById(R.id.btn_concluir);

        btnConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Informe seu email que será usado para se autenticar");
                    etEmail.requestFocus();
                    return;
                }

                if (etSenha.getText().toString().isEmpty()) {
                    etSenha.setError("Informe sua senha que será usada para autenticação");
                    etSenha.requestFocus();
                    return;
                }

                if (etSenha.getText().toString().length() < 3) {
                    etSenha.setError("Sua senha deve ter pelo menos 3 caracteres alfanuméricos");
                    etSenha.requestFocus();
                    return;
                }

                if (!etSenha.getText().toString().equals(etSenha2.getText().toString())) {
                    etSenha2.setError("As senhas não correspondem");
                    etSenha2.requestFocus();
                    return;
                }

                usuario.setEmail(etEmail.getText().toString());
                usuario.setSenha(etSenha.getText().toString());

                try {
                    cadastrar();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppDialogTheme))
                            .setTitle("Erro")
                            .setMessage("Aconteceu uma situação inesperada ao completar cadastrar.")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert);
                }
            }
        });

    }

    private void cadastrar() throws UnsupportedEncodingException {


        // calcula hash da senha do usuario
        usuario.codificarSenha();

        // envia requisicao ao servidor para cadastrar nova conta
        usuarioController.novaConta(getContext(), usuario, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                alternarProgressBar(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                callback(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                callback(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                callback(statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback(statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                callback(statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                callback(statusCode);
            }

            public void callback(int statusCode) {

                AlertDialog.Builder alert = null;

                // se o cadastro foi efetuado
                if (statusCode == HttpStatus.SC_OK) {

                    try {
                        autenticar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        alert = new AlertDialog.Builder(getContext())
                                .setTitle("Erro")
                                .setMessage("Aconteceu uma situação inesperada ao autenticar.")
                                .setPositiveButton(android.R.string.yes, null)
                                .setIcon(android.R.drawable.ic_dialog_alert);
                    }

                } else if (statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE) {

                    alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppDialogTheme))
                            .setTitle("Falha")
                            .setMessage("Serviço temporariamente indisponível. Tente novamente em alguns minutinhos.")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert);
                } else {

                    alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppDialogTheme))
                            .setTitle("Falha")
                            .setMessage("Serviço temporariamente indisponível. Tente novamente em alguns minutinhos.")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert);
                }

                // verifica se há alguma mensagem a ser mostrada na tela
                if (alert != null)
                    alert.show();

            }

        });

    }

    public void autenticar() throws UnsupportedEncodingException {
        authController.autenticar(getActivity(), usuario, new Autenticar());
    }

    public void alternarProgressBar(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private class Autenticar extends JsonHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            // usuario autenticado
            final Usuario getUsuario = usuarioController.gson.fromJson(response.toString(), Usuario.class);
            getUsuario.setFoto(usuario.getFoto());
            getUsuario.setFotoPerfil(usuario.getFotoPerfil());


            // pega auth token
            String token = usuarioController.getAuthToken(headers);

            // cria sessão local
            SessionManager.createLoginSession(getContext(), getUsuario.getEmail(), token);

            // ativa usuario
            ((GlobalApplication) getContext().getApplicationContext()).setUsuarioAtivo(getUsuario);

            try {
                // salva foto de perfil selecionada anteriormente
                usuarioController.salvarFoto(getContext(), getUsuario.getEmail(), getUsuario.getFoto(), new SalvarFotoPerfil());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            alternarProgressBar(false);
            falha();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            alternarProgressBar(false);
            falha();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            alternarProgressBar(false);
            falha();
        }

        private void falha() {
            new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppDialogTheme))
                    .setTitle("Atenção")
                    .setMessage("Email / Senha estão incorreto(s)")
                    .setPositiveButton(android.R.string.yes, null)
                    .setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }

    /**
     * Inicializa os componentes da tela com as informações do usuário atual
     */
    private void carregarFormulario() {
        etEmail.setText(usuario.getEmail());
    }

    private class SalvarFotoPerfil implements Callback {

        @Override
        public void sucesso() {
            Intent intent = new Intent(getContext(), Main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }

        @Override
        public void falha() {
            alternarProgressBar(false);
            new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppDialogTheme))
                    .setTitle("Atenção")
                    .setMessage("Não foi possível salvar a foto de perfil")
                    .setPositiveButton(android.R.string.yes, null)
                    .setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }

}
