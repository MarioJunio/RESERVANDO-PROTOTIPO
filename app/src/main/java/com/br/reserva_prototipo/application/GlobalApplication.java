package com.br.reserva_prototipo.application;

import android.app.Application;

import com.br.reserva_prototipo.model.Usuario;

/**
 * Created by MarioJ on 08/07/16.
 */
public class GlobalApplication extends Application {

    // usuario que está ativo na sessão
    private Usuario usuarioAtivo;

    @Override
    public void onCreate() {
        super.onCreate();
        usuarioAtivo = new Usuario();
    }

    public Usuario getUsuarioAtivo() {
        return usuarioAtivo;
    }

    public void setUsuarioAtivo(Usuario usuarioAtivo) {
        this.usuarioAtivo = usuarioAtivo;
    }
}
