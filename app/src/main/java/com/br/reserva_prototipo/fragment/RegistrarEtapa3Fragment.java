package com.br.reserva_prototipo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.activity.Login;
import com.br.reserva_prototipo.application.GlobalApplication;
import com.br.reserva_prototipo.model.Usuario;
import com.br.reserva_prototipo.util.ImageUtils.Images;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by MarioJ on 12/07/16.
 */
public class RegistrarEtapa3Fragment extends Fragment {

    public static final String TAG = "RegistrarEtapa3Fragment";

    private ProgressBar progressBar;
    private ImageView imgFotoPerfil;
    private Button btnAvancar;
    private Button btnPularEtapa;
    private Usuario usuario;

    public static RegistrarEtapa3Fragment newInstance() {
        return new RegistrarEtapa3Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = ((GlobalApplication) getActivity().getApplicationContext()).getUsuarioAtivo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_registrar_etapa_3, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ((Login) getActivity()).getSupportActionBar().setTitle("Nova conta - Passo 3");

        // recupera componentes da tela
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        progressBar.bringToFront();
        imgFotoPerfil = (ImageView) view.findViewById(R.id.img_foto_perfil);
        btnAvancar = (Button) view.findViewById(R.id.btn_avancar);
        btnPularEtapa = (Button) view.findViewById(R.id.btn_pular_etapa);

        // evento para selecionar a foto de perfil
        imgFotoPerfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Images.pegarImagem(getActivity());
            }
        });

        // avança para a próxima tela
        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // verifica se o usuario já selecionou a foto de perfil
                if (usuario.getFoto() != null)
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, RegistrarEtapa4Fragment.newInstance()).addToBackStack("nova-conta-passo-3").commit();
                else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Atenção")
                            .setMessage("Selecione a foto de perfil para avançar.")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });

        btnPularEtapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, RegistrarEtapa4Fragment.newInstance()).addToBackStack("nova-conta-passo-3").commit();
            }
        });

        // carrega imagem do usuario atual
        carregarImagem();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == Images.PEGAR_IMAGEM) {

            new AsyncTask<Void, Void, Object>() {

                @Override
                protected void onPreExecute() {
                    if (imgFotoPerfil != null)
                        imgFotoPerfil.setImageBitmap(null);

                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected Object doInBackground(Void... params) {

                    try {

                        // obtem o input stream da imagem selecionada
                        InputStream is = getContext().getContentResolver().openInputStream(data.getData());

                        usuario.setFotoPerfil(is);

                        // comprimi a imagem e retorna os bytes
                        return Images.getImagemComprimida(BitmapFactory.decodeStream(is));

                    } catch (FileNotFoundException e1) {
                        return new Byte[0];
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Object obj) {

                    byte[] bytes = (byte[]) obj;

                    try {

                        if (bytes != null && bytes.length > 0) {
                            imgFotoPerfil.setScaleType(ImageView.ScaleType.FIT_XY);
                            imgFotoPerfil.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            usuario.setFoto(bytes);

                        } else if (bytes != null && bytes.length == 0) {

                            imgFotoPerfil.setScaleType(ImageView.ScaleType.CENTER);
                            imgFotoPerfil.setImageDrawable(getActivity().getDrawable(R.drawable.ic_account));

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                            builder.setTitle("Atenção");
                            builder.setMessage("A imagem selecionada pode estar corrompida, ou pode ter sido movida para outra pasta.");
                            builder.setPositiveButton("OK", null);
                            builder.show();
                        }

                    } finally {
                        progressBar.setVisibility(View.GONE);
                    }
                }

            }.execute();

        }

    }

    private void carregarImagem() {

        if (usuario.getFoto() != null)
            imgFotoPerfil.setImageBitmap(BitmapFactory.decodeByteArray(usuario.getFoto(), 0, usuario.getFoto().length));
    }
}
