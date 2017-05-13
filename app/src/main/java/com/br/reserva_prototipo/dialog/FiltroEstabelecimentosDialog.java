package com.br.reserva_prototipo.dialog;

import android.content.Context;
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
import android.widget.Toast;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.adapter.EstabelecimentosAdapter;
import com.br.reserva_prototipo.controller.EstabelecimentoController;
import com.br.reserva_prototipo.model.Estabelecimento;
import com.br.reserva_prototipo.model.filter.FiltroEstabelecimentos;
import com.br.reserva_prototipo.util.App;
import com.br.reserva_prototipo.util.GpsUtils.GPS;
import com.br.reserva_prototipo.util.Ini;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MarioJ on 31/03/16.
 */
public class FiltroEstabelecimentosDialog {

    private static final String TAG = "Filtro";

    private Context context;
    private EstabelecimentosAdapter estabelecimentosAdapter;

    private View view;
    private View viewEstabelecimentos;
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

    // filtro atual
    private FiltroEstabelecimentos filtro;

    // controladores
    private EstabelecimentoController estabelecimentoController;

    public FiltroEstabelecimentosDialog(Context context, EstabelecimentosAdapter estabelecimentosAdapter, View viewEstabelecimentos) {
        this.context = context;
        this.estabelecimentosAdapter = estabelecimentosAdapter;
        this.viewEstabelecimentos = viewEstabelecimentos;
        this.dialog = new AlertDialog.Builder(context).create();

        // cria controlador de estabelecimento
        estabelecimentoController = EstabelecimentoController.create();
    }

    /**
     * Cria filtro de estabelecimentos padrao, utilizado ao entrar na aplicação
     */
    public FiltroEstabelecimentos criarFiltroPadraoPorCidade(String cidade) {

        FiltroEstabelecimentos filtroEstabelecimentos = new FiltroEstabelecimentos();
        filtroEstabelecimentos.setCidade(cidade);
        filtroEstabelecimentos.setOrdenarPor(Estabelecimento.SortField.nomeFantasia);
        filtroEstabelecimentos.setOrdenacaoAsc(true);

        if (cidade == null)
            filtroEstabelecimentos.setCoordenada(GPS.getCoordenadaAtual(context));

        return filtroEstabelecimentos;
    }

    /**
     * Prepara o dialog para ser exibido
     */
    private void prepare() {

        if (view != null)
            return;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = layoutInflater.inflate(R.layout.dg_filtrar_estabelecimentos, null);

        dialog.setView(view);

        // instancia widgets da view
        setupWidgets();

        // seta eventos
        setupListener();

        // carrega preferencias ja salvas anteriormente
        atualizaFormulario(Ini.carregarPreferenciasFiltroEstabelecimentos(context));

    }

    private void atualizaFormulario(FiltroEstabelecimentos filtro) {
        updateDistancia(filtro.getDistancia());

        inCidade.setText(filtro.getCidade());

        if (filtro.getPrecoMin() != null && filtro.getPrecoMin().floatValue() != 0f)
            inMinPreco.setText(filtro.getPrecoMin().toPlainString());

        if (filtro.getPrecoMax() != null && filtro.getPrecoMax().floatValue() != 0f)
            inMaxPreco.setText(filtro.getPrecoMax().toPlainString());

        if (filtro.getHorario() == Estabelecimento.Horario.Aberto)
            rbAberto.setChecked(true);

        if (filtro.getHorario() == Estabelecimento.Horario.Fechado)
            rbFechado.setChecked(true);

        if (filtro.getHorario() == null)
            rbAmbos.setChecked(true);

        if (filtro.getCategoria() != null)
            spinCategoria.setSelection(filtro.getCategoria().ordinal());

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

                // valida e cria o objeto filtro
                filtro = validarFormulario();

                if (filtro != null) {

                    // salvar preferencias
                    Ini.salvarPreferenciasFiltroEstabelecimentos(context, filtro);

                    // aplicar filtro
                    aplicarFiltro();

                    // sair do dialog
                    dialog.dismiss();
                }
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

        App.setButtonTint(btCancelar, Color.parseColor(context.getString(R.color.red)), PorterDuff.Mode.MULTIPLY);
        App.setButtonTint(btAplicar, Color.parseColor(context.getString(R.color.green)), PorterDuff.Mode.MULTIPLY);

    }

    private void updateDistancia(double progress) {
        String progressStr = String.valueOf(new DecimalFormat("##0").format(progress));
        lbDistancia.setText(progressStr.equals("0") ? "Cidade toda" : progressStr.concat(" KM"));
        seekDistancia.setProgress((int) progress);

    }

    private FiltroEstabelecimentos validarFormulario() {

        // dados dos widgets
        double distancia = seekDistancia.getProgress();
        String cidade = inCidade.getText().toString();
        Estabelecimento.Horario horario = rbAberto.isChecked() ? Estabelecimento.Horario.Aberto : rbFechado.isChecked() ? Estabelecimento.Horario.Fechado : null;
        Estabelecimento.Categoria categoria = Estabelecimento.Categoria.parse(spinCategoria.getSelectedItem().toString());

        BigDecimal minPreco = null;
        BigDecimal maxPreco = null;

        if (!inMinPreco.getText().toString().isEmpty()) {

            try {
                minPreco = new BigDecimal(inMinPreco.getText().toString());
                inMinPreco.setBackground(context.getDrawable(R.drawable.edittext_rounded_corners));
            } catch (Exception e) {
                inMinPreco.setBackground(context.getDrawable(R.drawable.edittext_rounded_corners_error));
                Toast.makeText(context, context.getString(R.string.et_min_preco), Toast.LENGTH_LONG).show();
                return null;
            }
        }

        if (!inMaxPreco.getText().toString().isEmpty()) {

            try {
                maxPreco = new BigDecimal(inMaxPreco.getText().toString());
                inMaxPreco.setBackground(context.getDrawable(R.drawable.edittext_rounded_corners));
            } catch (Exception e) {
                inMaxPreco.setBackground(context.getDrawable(R.drawable.edittext_rounded_corners_error));
                Toast.makeText(context, context.getString(R.string.et_max_preco), Toast.LENGTH_LONG).show();
                return null;
            }
        }

        // cria filtro default
        FiltroEstabelecimentos filtroEstabelecimentos = new FiltroEstabelecimentos();
        filtroEstabelecimentos.setCidade(cidade);
        filtroEstabelecimentos.setDistancia(distancia);
        filtroEstabelecimentos.setPrecoMin(minPreco);
        filtroEstabelecimentos.setPrecoMax(maxPreco);
        filtroEstabelecimentos.setHorario(horario);
        filtroEstabelecimentos.setOrdenarPor(Estabelecimento.SortField.nomeFantasia);
        filtroEstabelecimentos.setCategoria(categoria);
        filtroEstabelecimentos.setOrdenacaoAsc(true);

        return filtroEstabelecimentos;

    }

    private void aplicarFiltro() {

        System.out.println(filtro.toJson());

        // seta hora atual do dispositivo, para buscas baseadas em horario
        if (filtro.getHorario() != null)
            filtro.setData(Calendar.getInstance().getTime());

        try {

            // limpa registros atuais
            estabelecimentosAdapter.clear();

            // tenta buscar estabelecimentos
            estabelecimentoController.buscarTodosToView(context, viewEstabelecimentos, filtro, estabelecimentosAdapter);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public FiltroEstabelecimentos getFiltro() {
        return filtro;
    }
}
