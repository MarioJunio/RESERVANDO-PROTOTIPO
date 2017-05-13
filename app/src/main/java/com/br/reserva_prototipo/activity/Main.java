package com.br.reserva_prototipo.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.application.GlobalApplication;
import com.br.reserva_prototipo.controller.UsuarioController;
import com.br.reserva_prototipo.fragment.ListarEstabelecimentosFragment;
import com.br.reserva_prototipo.fragment.ListarReservasFragment;
import com.br.reserva_prototipo.model.Usuario;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Main";

    private Usuario usuarioAtivo;

    // widgets
    private ImageView imgFotoPerfilUsuario;

    // controladores para acessar recursors na internet
    UsuarioController usuarioController;

    public void setTitulo(String novoTitulo) {
        getSupportActionBar().setTitle(novoTitulo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // cria controladores
        usuarioController = UsuarioController.create();

        // carrega usuario ativo na sessão
        usuarioAtivo = ((GlobalApplication) getApplication()).getUsuarioAtivo();

        setContentView(R.layout.activity_main);

        createToolbarAndDrawer();

        setupWidgets();

        getSupportFragmentManager().beginTransaction().add(R.id.content_main, ListarEstabelecimentosFragment.newInstance()).commit();
    }

    private void setupWidgets() {

        // cria navegacao do drawer lateral
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // carrega foto perfil do usuario ativo
        carregarFotoPerfilDoUsuarioAtivo();

        // indice 0 - representa o menu bem vindo, que contem dentro dele outro menu
        MenuItem miBemVindo = navigationView.getMenu().getItem(0);

        // MenuItem que contem nome do usuario
        miBemVindo.getSubMenu().getItem(0).setTitle(usuarioAtivo.getNome());
    }

    private void carregarFotoPerfilDoUsuarioAtivo() {
        imgFotoPerfilUsuario = (ImageView) findViewById(R.id.usuario_foto_perfil);
        usuarioController.buscarFotoPerfil(usuarioAtivo.getId(), new FileAsyncHttpResponseHandler(getApplicationContext()) {

            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.d(TAG, "Foto nao encontrada");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {

                if (statusCode == HttpStatus.SC_OK) {
                    imgFotoPerfilUsuario.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imgFotoPerfilUsuario.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                }

            }

        }, getApplicationContext());
    }

    private void createToolbarAndDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_estabelecimentos) {
            telaListarEstabelecimentos();
        } else if (id == R.id.menu_reservas) {
            telaListarReservas();
        } else if (id == R.id.menu_pedidos) {

        } else if (id == R.id.menu_configuracoes) {

        } else if (id == R.id.menu_compartilhar) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * Carrega tela de estabelecimentos para o usuário navegar
     */
    private void telaListarEstabelecimentos() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, ListarEstabelecimentosFragment.newInstance()).commit();
    }

    /**
     * Carrega tela de reservas para que o usuário possa consultar suas reservas
     */
    private void telaListarReservas() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, ListarReservasFragment.newInstance()).commit();
    }
}
