package com.br.reserva_prototipo.controller;

import android.app.Activity;

import com.br.reserva_prototipo.model.Usuario;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by MarioJ on 30/06/16.
 */
public class AuthController extends Controller {

    public AuthController() {
        super("/api/auth");
    }

    public void autenticar(final Activity context, Usuario usuario, ResponseHandlerInterface handler) throws UnsupportedEncodingException {
        post("", new StringEntity(usuario.toJson()), handler, false, context);
    }

    public static AuthController create() {
        return new AuthController();
    }

}
