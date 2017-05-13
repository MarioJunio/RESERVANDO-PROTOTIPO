package com.br.reserva_prototipo.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.adapter.EstabelecimentosAdapter;
import com.br.reserva_prototipo.model.Estabelecimento;
import com.br.reserva_prototipo.model.Reserva;
import com.br.reserva_prototipo.model.filter.FiltroEstabelecimentos;
import com.br.reserva_prototipo.util.ImageUtils.Images;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.io.File;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by MarioJ on 15/04/16.
 */
public class EstabelecimentoController extends Controller {

    public EstabelecimentoController() {
        super("/api/estabelecimentos");
    }

    public static EstabelecimentoController create() {
        return new EstabelecimentoController();
    }

    public void buscarTodosToView(Context context, final View view, final FiltroEstabelecimentos filtro, final EstabelecimentosAdapter adapter) throws UnsupportedEncodingException {

        get("", new StringEntity(filtro.toJson()), new JsonHttpResponseHandler() {

            ProgressBar progress;
            View panelSemRegistros;
            View panelError;

            @Override
            public void onStart() {
                progress = (ProgressBar) view.findViewById(R.id.progress);
                panelSemRegistros = view.findViewById(R.id.panel_not_found);
                panelError = view.findViewById(R.id.panel_error);

                progress.setVisibility(View.VISIBLE);
                panelSemRegistros.setVisibility(View.GONE);
                panelError.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                // esconde o painel que
                panelSemRegistros.setVisibility(View.GONE);
                panelError.setVisibility(View.GONE);

                // apresenta progressbar
                progress.setVisibility(View.GONE);

                try {

                    if (statusCode == HttpStatus.SC_OK) {

                        System.out.println("Estabelecimentos encontrados: " + response.length());

                        if (response.length() > 0) {

                            // percorre a lista de estabelecimentos encontrados e adiciona-os a lista
                            for (int i = 0; i < response.length(); i++)
                                adapter.add(gson.fromJson(response.get(i).toString(), Estabelecimento.class));

                        } else
                            panelSemRegistros.setVisibility(View.VISIBLE);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    progress.setVisibility(View.GONE);
                }

                Log.d("DEBUG", "BUSCAR ESTABELECIMENTOS SUCESSO");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.setVisibility(View.GONE);

                if (statusCode == HttpStatus.SC_NOT_FOUND)
                    panelSemRegistros.setVisibility(View.VISIBLE);
                else
                    panelError.setVisibility(View.VISIBLE);

                Log.d("DEBUG", "BUSCAR ESTABELECIMENTOS FALHA");
            }

        }, true, context);

    }

    public void reservar(final View view, final Reserva reserva) throws UnsupportedEncodingException {

        post("/reservar", new StringEntity(reserva.toJson()), new AsyncHttpResponseHandler() {

            // widgets
            private RadioGroup rgTickets;
            private TextView lbHorarioReserva;

            // layouts
            private View layoutEsgotado;
            private View layoutErro;
            private View layoutSucesso;

            // text fields
            private TextView textResponseEsgotado;
            private TextView textResponseErro;
            private TextView textResponseSucesso;

            @Override
            public void onStart() {

                // widgets
                rgTickets = (RadioGroup) view.findViewById(R.id.rg_tickets);
                lbHorarioReserva = (TextView) view.findViewById(R.id.lb_horario_reserva);

                layoutEsgotado = view.findViewById(R.id.layout_esgotado);
                layoutErro = view.findViewById(R.id.layout_erro_inesperado);
                layoutSucesso = view.findViewById(R.id.layout_sucesso);

                textResponseEsgotado = (TextView) view.findViewById(R.id.text_response_esgotado);
                textResponseErro = (TextView) view.findViewById(R.id.text_response_erro);
                textResponseSucesso = (TextView) view.findViewById(R.id.text_response_sucesso);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mostrarMensagemSucesso("Reserva efetuada para a data: " + new String(responseBody));
                limparFormulario();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                // se o estabelecimento está cheio
                if (statusCode == HttpStatus.SC_EXPECTATION_FAILED)
                    mostrarMensagemEsgotado(new String(responseBody));
                else
                    mostrarMensagemErro(new String(responseBody));

            }

            private void mostrarMensagemSucesso(String mensagem) {
                esconderMensagens();
                textResponseSucesso.setText(mensagem);
                layoutSucesso.setVisibility(View.VISIBLE);
            }

            private void mostrarMensagemErro(String mensagem) {
                esconderMensagens();
                textResponseErro.setText(mensagem);
                layoutErro.setVisibility(View.VISIBLE);
            }

            private void mostrarMensagemEsgotado(String mensagem) {
                esconderMensagens();
                textResponseEsgotado.setText(mensagem);
                layoutEsgotado.setVisibility(View.VISIBLE);

            }

            private void esconderMensagens() {
                layoutSucesso.setVisibility(View.GONE);
                layoutErro.setVisibility(View.GONE);
                layoutEsgotado.setVisibility(View.GONE);
            }

            private void limparFormulario() {
                lbHorarioReserva.setText(view.getContext().getString(R.string.label_data_reserva));
                rgTickets.clearCheck();
                reserva.setDataReserva(null);
            }

        }, true, view.getContext());

    }

    public void carregarFotoPerfil(final Context context, final Estabelecimento estabelecimento, final ImageView foto, final ProgressBar progress) {

        get("/foto/" + estabelecimento.getId(), null, new FileAsyncHttpResponseHandler(context) {

            @Override
            public void onStart() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progress.setVisibility(View.GONE);

                if (statusCode == HttpStatus.SC_NOT_FOUND) {
                    foto.setScaleType(ImageView.ScaleType.CENTER);
                    foto.setImageDrawable(context.getDrawable(R.drawable.photo_default));
                    estabelecimento.setFoto(null);
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                progress.setVisibility(View.GONE);

                if (file == null) {
                    Log.d("Carregar Foto Perfil", "Imagem nao encontrada");
                    return;
                }

                // se retornou OK
                if (statusCode == HttpStatus.SC_OK) {

                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    // Atribui imagem no imageview
                    foto.setScaleType(ImageView.ScaleType.FIT_XY);
                    foto.setImageBitmap(bitmap);
                    estabelecimento.setFoto(Images.parse(bitmap));

                }

            }
        }, true, context);

    }

}
