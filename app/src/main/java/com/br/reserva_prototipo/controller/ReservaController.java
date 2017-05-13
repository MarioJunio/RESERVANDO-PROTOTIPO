package com.br.reserva_prototipo.controller;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.adapter.ReservasAdapter;
import com.br.reserva_prototipo.model.Reserva;
import com.br.reserva_prototipo.model.Usuario;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;

/**
 * Created by MarioJ on 29/07/16.
 */
public class ReservaController extends Controller {

    public ReservaController() {
        super("/api/reservas");
    }

    public static ReservaController create() {
        return new ReservaController();
    }

    public void buscarTodas(final View view, Usuario usuario, final ReservasAdapter reservasAdapter) {

        get("/todas/" + usuario.getId(), null, new JsonHttpResponseHandler() {

            ProgressBar progress;
            View panelSemRegistros;
            View panelError;

            @Override
            public void onStart() {
                progress = (ProgressBar) view.findViewById(R.id.progress);
                panelSemRegistros = view.findViewById(R.id.panel_not_found);
                panelError = view.findViewById(R.id.panel_error);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                if (statusCode == HttpStatus.SC_OK) {

                    try {
                        sucesso(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                falha(statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                falha(statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                falha(statusCode);
            }

            private void sucesso(JSONArray jsonArrayReservas) throws JSONException {

                // lima a lista de reservas atual
                reservasAdapter.clear();

                if (jsonArrayReservas.length() > 0) {

                    for (int i = 0; i < jsonArrayReservas.length(); i++) {

                        Reserva reserva = gson.fromJson(jsonArrayReservas.get(i).toString(), Reserva.class);

                        Log.d("DEBUG", "Reserva: " + reserva.toString());

                        reservasAdapter.add(reserva);

                    }

                } else
                    panelSemRegistros.setVisibility(View.VISIBLE);

            }

            private void falha(int statusCode) {

                if (statusCode == HttpStatus.SC_NOT_FOUND)
                    semReservas();
                else
                    erro();

            }

            private void semReservas() {
                panelSemRegistros.setVisibility(View.VISIBLE);
            }

            private void erro() {
                panelError.setVisibility(View.VISIBLE);
            }

        }, true, view.getContext());

    }
}
