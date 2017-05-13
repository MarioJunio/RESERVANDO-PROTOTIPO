package com.br.reserva_prototipo.controller;

import android.content.Context;
import android.util.Log;

import com.br.reserva_prototipo.model.Usuario;
import com.br.reserva_prototipo.net.Callback;
import com.br.reserva_prototipo.net.HttpFileUpload;
import com.br.reserva_prototipo.session.SessionManager;
import com.br.reserva_prototipo.util.GsonUtils.JsonDateDeserealizer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by MarioJ on 10/07/16.
 */
public abstract class Controller {

    protected String DOMAIN;
    protected String DataType;
    public Gson gson;

    // SERVER REST SESSION NAME
    public static final String REST_IDENTIFIER = "X-AUTH-IDENTIFIER";
    public static final String REST_TOKEN = "X-AUTH-TOKEN";

    // endereço do recurso a ser acessado
    protected String uri;

    public Controller(String uri) {
        DOMAIN = "http://192.168.1.6:8080";
        DataType = "application/json";
        gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserealizer()).create();

        // uri utilizada para acessar o recurso
        this.uri = uri;
    }

    protected static AsyncHttpClient newAsyncHttpClient() {
        return new AsyncHttpClient();
    }

    protected static AsyncHttpClient newAsyncHttpClientWithRestToken(Context context) {
        AsyncHttpClient asyncHttpClient = newAsyncHttpClient();
        asyncHttpClient.addHeader(REST_IDENTIFIER, SessionManager.readLoginSessionIdentifier(context));
        asyncHttpClient.addHeader(REST_TOKEN, SessionManager.readLoginSessionToken(context));

        Log.d("Autenticação", String.format("REST [%s, %s]", SessionManager.readLoginSessionIdentifier(context), SessionManager.readLoginSessionToken(context)));

        return asyncHttpClient;
    }

    protected void get(String resource, StringEntity entity, ResponseHandlerInterface handler, boolean authenticated, Context context) {
        AsyncHttpClient httpClient;

        if (authenticated)
            httpClient = newAsyncHttpClientWithRestToken(context);
        else
            httpClient = newAsyncHttpClient();

        httpClient.get(context, getDomain() + resource, entity, DataType, handler);
    }

    protected void post(String resource, StringEntity params, ResponseHandlerInterface handler, boolean authenticated, Context context) {
        AsyncHttpClient httpClient;

        if (authenticated)
            httpClient = newAsyncHttpClientWithRestToken(context);
        else
            httpClient = newAsyncHttpClient();

        httpClient.post(context, getDomain() + resource, params, DataType, handler);
    }

    protected void post(String resource, RequestParams params, ResponseHandlerInterface handler, boolean authenticated, Context context) {
        AsyncHttpClient httpClient;

        if (authenticated)
            httpClient = newAsyncHttpClientWithRestToken(context);
        else
            httpClient = newAsyncHttpClient();

        httpClient.post(context, getDomain() + resource, null, params, DataType, handler);
    }

    protected void postUpload(Context context, String resource, String email, byte foto[], boolean auth, Callback callback) throws Exception {

        String domain = getDomain() + resource;

        HttpFileUpload fileUpload = new HttpFileUpload(domain);

        fileUpload.connectForMultipart(context, auth);
        fileUpload.addFormPart(Usuario.Fields.email.name(), email);
        fileUpload.addFilePart(Usuario.Fields.foto.name(), null, foto);
        fileUpload.finishMultipart();

        boolean success = fileUpload.success();

        fileUpload.close();

        if (success)
            callback.sucesso();
        else
            callback.falha();
    }

    protected String getDomain() {
        return DOMAIN + uri;
    }

    public String getAuthToken(Header[] headers) {

        for (Header header : headers) {

            if (header.getName().equals("Set-Cookie")) {

                if (header.getValue().startsWith(REST_TOKEN))
                    return header.getValue().split("=")[1];
            }
        }

        return null;
    }
}
