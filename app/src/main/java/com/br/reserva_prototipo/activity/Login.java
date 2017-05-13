package com.br.reserva_prototipo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.fragment.LoginFragment;

public class Login extends AppCompatActivity {

    public ProgressBar progressBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // layout da atividade
        setContentView(R.layout.activity_login);

        // recupera componentes
        progressBar = (ProgressBar) findViewById(R.id.progress);

        // cria barra de ferramentas na parte superior da tela
        criarToolbar();

        // troca para tela de login
        getSupportFragmentManager().beginTransaction().replace(R.id.content, LoginFragment.newInstance()).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {

            if (fragment != null)
                fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void criarToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStack();
            }
        });
    }

}

