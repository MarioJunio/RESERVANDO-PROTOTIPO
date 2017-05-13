package com.br.reserva_prototipo.controller;

import android.content.Context;

import com.br.reserva_prototipo.model.Usuario;
import com.br.reserva_prototipo.net.Callback;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * Created by MarioJ on 06/07/16.
 */
public class UsuarioController extends Controller {

    public UsuarioController() {
        super("/api/usuarios");
    }

    public UsuarioController(String uri) {
        super(uri);
    }

    public static UsuarioController create() {
        return new UsuarioController();
    }

    public static UsuarioController create(String uri) {
        return new UsuarioController(uri);
    }

    public void buscarFotoPerfil(Long id, ResponseHandlerInterface responseHandlerInterface, Context context) {
        get("/foto/" + id, null, responseHandlerInterface, true, context);
    }

    public void novaConta(Context context, Usuario usuario, ResponseHandlerInterface handler) {

        RequestParams params = new RequestParams();
        params.put(Usuario.Fields.nome.name(), usuario.getNome());
        params.put(Usuario.Fields.email.name(), usuario.getEmail());
        params.put(Usuario.Fields.senha.name(), usuario.getSenha());
        params.put(Usuario.Fields.cpf.name(), usuario.getCpf());
        params.put(Usuario.Fields.dataNascimento.name(), usuario.getDataNascimentoFormatadaParaEnvio());
        params.setUseJsonStreamer(true);

        post("/novaConta", params, handler, false, context);
    }

    public void salvarFoto(final Context context, final String email, final byte foto[], final Callback callBack) {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    postUpload(context, "/salvarFoto", email, foto, true, callBack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

}
