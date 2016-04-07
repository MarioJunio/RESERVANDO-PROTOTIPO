package com.br.reserva_prototipo.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.model.Estabelecimento;
import com.br.reserva_prototipo.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarioJ on 31/03/16.
 */
public class FiltroEstabelecimentosDialog {

    private static final String TAG = "Filtro";

    // utils
    private Context context;
    private View view;
    private AlertDialog dialog;

    // widgets
    private TextView lbDistancia;
    private SeekBar seekDistancia;
    private EditText inCidade;
    private EditText inMinPreco;
    private EditText inMaxPreco;
    private RadioButton rbAberto;
    private RadioButton rbFechado;
    private RadioButton rbAmbos;
    private Spinner spinCategoria;
    private Button btAplicar;
    private Button btCancelar;

    // preferences
    private SharedPreferences sharedPreferences;

    public FiltroEstabelecimentosDialog(Context context) {
        this.context = context;

        sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        dialog = new AlertDialog.Builder(context).create();
    }

    /**
     * Prepara o dialog para ser exibido
     */
    private void prepare() {

        if (view != null)
            return;

        // remove title bar
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = layoutInflater.inflate(R.layout.dg_filtrar_estabelecimentos, null);

        dialog.setView(view);

        // instancia widgets da view
        setupWidgets();

        // seta eventos
        setupListener();

        // load preferences
        loadPreferences();

    }

    private void show() {
        dialog.setCancelable(false);
        dialog.show();
    }

    private void setupListener() {

        seekDistancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateDistancia(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btAplicar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt(context.getString(R.string.distancia), seekDistancia.getProgress());
                editor.putString(context.getString(R.string.cidade), inCidade.getText().toString());

                try {
                    editor.putFloat(context.getString(R.string.preco_min), Float.parseFloat(inMinPreco.getText().toString()));
                    editor.putFloat(context.getString(R.string.preco_max), Float.parseFloat(inMaxPreco.getText().toString()));
                } catch (NumberFormatException nfe) {
                    //DO NOTHING
                }

                editor.putString(context.getString(R.string.horario), rbAberto.isChecked() ? "aberto" : rbFechado.isChecked() ? "fechado" : "ambos");
                editor.putLong(context.getString(R.string.categoria), spinCategoria.getSelectedItemId());

                editor.commit();

                // sair do dialog
                dialog.dismiss();
            }
        });
    }

    public void createAndShow() {
        prepare();
        show();
    }

    /**
     * Instancia os widgets da view a partir do layout
     */
    private void setupWidgets() {

        lbDistancia = (TextView) view.findViewById(R.id.lb_distancia);

        seekDistancia = (SeekBar) view.findViewById(R.id.seek_distancia);

        inCidade = (EditText) view.findViewById(R.id.in_cidade);

        inMinPreco = (EditText) view.findViewById(R.id.in_min_preco);
        inMaxPreco = (EditText) view.findViewById(R.id.in_max_preco);

        rbAberto = (RadioButton) view.findViewById(R.id.radio_aberto);
        rbFechado = (RadioButton) view.findViewById(R.id.radio_fechado);
        rbAmbos = (RadioButton) view.findViewById(R.id.radio_ambos);

        spinCategoria = (Spinner) view.findViewById(R.id.spin_categoria);

        btAplicar = (Button) view.findViewById(R.id.bt_aplicar);
        btCancelar = (Button) view.findViewById(R.id.bt_cancelar);

        // carrega categoria para adicionar ao spinner
        List<String> categorias = new ArrayList<>();
        categorias.add("Selecione a categoria");

        for (Estabelecimento.Categoria categoria : Estabelecimento.Categoria.values())
            categorias.add(categoria.getNome());

        // adiciona categorias selecionaveis
        spinCategoria.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, categorias));

        AppUtils.setButtonTint(btCancelar, Color.parseColor(context.getString(R.color.red)), PorterDuff.Mode.MULTIPLY);
        AppUtils.setButtonTint(btAplicar, Color.parseColor(context.getString(R.color.green)), PorterDuff.Mode.MULTIPLY);

    }

    /**
     * Carrega as preferencias inicialmente configuradas se existirem
     */
    private void loadPreferences() {

        int distancia = sharedPreferences.getInt(context.getString(R.string.distancia), 0);
        String cidade = sharedPreferences.getString(context.getString(R.string.cidade), "");

        updateDistancia(distancia);
        seekDistancia.setProgress(distancia);
        inCidade.setText(cidade);
    }

    private void updateDistancia(int progress) {
        String progressStr = String.valueOf(progress);

        lbDistancia.setText(progressStr.equals("0") ? "Cidade toda" : progressStr.concat(" KM"));
    }
}
