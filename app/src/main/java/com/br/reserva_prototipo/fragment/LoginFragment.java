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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.activity.Login;
import com.br.reserva_prototipo.activity.Main;
import com.br.reserva_prototipo.application.GlobalApplication;
import com.br.reserva_prototipo.controller.AuthController;
import com.br.reserva_prototipo.model.Usuario;
import com.br.reserva_prototipo.session.SessionManager;
import com.br.reserva_prototipo.util.regex.PatternValidation;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;

/**
 * Created by MarioJ on 11/07/16.
 */
public class LoginFragment extends Fragment {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button btEntrarEmail;
    private Button btNovaConta;

    // controladores
    private AuthController authController;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authController = AuthController.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_login, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        ((Login) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((Login) getActivity()).getSupportActionBar().setTitle("Autentique-se");

        // instancia componentes
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
        mPasswordView = (EditText) view.findViewById(R.id.password);
        btEntrarEmail = (Button) view.findViewById(R.id.email_sign_in_button);
        btNovaConta = (Button) view.findViewById(R.id.nova_conta);

        // evento de click no botao entrar
        btEntrarEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                autenticar();
            }
        });

        btNovaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, RegistrarEtapa1Fragment.newInstance()).addToBackStack("login").commit();
            }
        });

    }

    // tenta realizar o login
    private void autenticar() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            mEmailView.requestFocus();
            return;
        }

        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
            return;
        }

        Usuario usuario = new Usuario(mEmailView.getText().toString(), mPasswordView.getText().toString());
        usuario.codificarSenha();

        try {
            authController.autenticar(getActivity(), usuario, new JsonHttpResponseHandler() {

                private ProgressBar progressBar;

                @Override
                public void onStart() {

                    progressBar = ((Login) getActivity()).progressBar;

                    btEntrarEmail.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    callback(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    callback(statusCode, headers, null);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    callback(statusCode, headers, null);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    callback(statusCode, headers, null);
                }

                @Override
                public void onFinish() {
                    btEntrarEmail.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }

                private void callback(int statusCode, Header[] headers, JSONObject response) {

                    if (statusCode == HttpStatus.SC_OK) {

                        // usuario autenticado
                        final Usuario getUsuario = authController.gson.fromJson(response.toString(), Usuario.class);

                        // pega auth token
                        String token = authController.getAuthToken(headers);

                        // cria sessão local
                        SessionManager.createLoginSession(getContext(), getUsuario.getEmail(), token);

                        // ativa usuario
                        ((GlobalApplication) getContext().getApplicationContext()).setUsuarioAtivo(getUsuario);

                        Intent intent = new Intent(getContext(), Main.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        getContext().startActivity(intent);

                    } else if (statusCode == HttpStatus.SC_FORBIDDEN) {

                        new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppDialogTheme))
                                .setTitle("Falha")
                                .setMessage("Não foi identificado nenhuma conta com esse email e senha.")
                                .setPositiveButton(android.R.string.yes, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }

                }

            });

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmailValid(String email) {
        return PatternValidation.isEmailValid(email);
    }

    private boolean isPasswordValid(String password) {
        return PatternValidation.isPasswordValid(password);
    }

}
