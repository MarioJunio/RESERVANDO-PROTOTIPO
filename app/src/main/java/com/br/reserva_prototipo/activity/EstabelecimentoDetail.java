package com.br.reserva_prototipo.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.fragment.EstabelecimentoDetailFrag;
import com.br.reserva_prototipo.model.Estabelecimento;
import com.br.reserva_prototipo.util.Images;

import java.io.File;

public class EstabelecimentoDetail extends AppCompatActivity {

    private static final String TAG = "EstabelecimentoDetail";

    // widgets
    private ImageView imgFtoPerfil;
    private TextView textNomeFantasia;
    private ProgressBar progressBar;

    // Estabelecimento atual
    private Estabelecimento estabelecimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_estabelecimento_detail);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
            estabelecimento = (Estabelecimento) bundle.getSerializable(Estabelecimento.KEY);

        setupWidgets();

        createToolbar();

        getSupportFragmentManager().beginTransaction().add(R.id.content, EstabelecimentoDetailFrag.newInstance(estabelecimento)).commit();
    }

    /**
     * Inicializa Toolbar customizado e atribui como actionbar da activity
     */
    private void createToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Inicializa os widgets da activity
     */
    private void setupWidgets() {

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        imgFtoPerfil = (ImageView) findViewById(R.id.foto_perfil);
        textNomeFantasia = (TextView) findViewById(R.id.nome_fantasia);
        progressBar = (ProgressBar) findViewById(R.id.progress);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // carrega foto de perfil atual
        loadFotoPerfil();

        // altera nome fantasia do estabelecimento
        textNomeFantasia.setText(estabelecimento.getCategoria() + " - " + estabelecimento.getNomeFantasia());

    }

    /**
     * Carrega foto de perfil que está salva no disco
     */
    private void loadFotoPerfil() {

        new AsyncTask<Void, Void, Bitmap>() {

            File profilePicturesDirectory;
            File fileFotoPerfil;

            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
                profilePicturesDirectory = new File(Environment.getExternalStorageDirectory(), Images.PROFILE_PICTURES);
                fileFotoPerfil = new File(profilePicturesDirectory, String.valueOf(estabelecimento.getId()).concat(Images.PICTURE_COMPRESSION));
            }

            @Override
            protected Bitmap doInBackground(Void... params) {

                // verifica se a foto existi
                if (fileFotoPerfil.exists())
                    return Images.toBitmap(fileFotoPerfil.getPath());

                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {

                try {

                    if (bitmap != null)
                        imgFtoPerfil.setImageBitmap(bitmap);
                    else
                        Log.d(TAG, "Foto Perfil nao carregada");

                } finally {
                    progressBar.setVisibility(View.GONE);
                }
            }

        }.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemID = item.getItemId();

        switch (itemID) {

            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
