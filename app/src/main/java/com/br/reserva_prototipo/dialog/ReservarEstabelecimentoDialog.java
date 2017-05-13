package com.br.reserva_prototipo.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.activity.Login;
import com.br.reserva_prototipo.application.GlobalApplication;
import com.br.reserva_prototipo.controller.EstabelecimentoController;
import com.br.reserva_prototipo.model.Estabelecimento;
import com.br.reserva_prototipo.model.Reserva;
import com.br.reserva_prototipo.model.TipoReserva;
import com.br.reserva_prototipo.util.App;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MarioJ on 04/04/16.
 */
public class ReservarEstabelecimentoDialog extends SlideDateTimeListener {

    private GlobalApplication globalApplication;
    private AppCompatActivity activity;
    private View view;
    private AlertDialog alertDialog;
    private Estabelecimento estabelecimento;
    private Reserva reserva;

    // widgets
    private TextView lbClickDetalhesReserva;
    private RadioGroup rgTickets;
    private TextView lbHorarioReserva;
    private TextView textTipoReserva;
    private Button btVerHorarios;
    private Button btCancelar, btConcluir;

    // controladores
    private EstabelecimentoController estabelecimentoController;

    public ReservarEstabelecimentoDialog(AppCompatActivity activity, Estabelecimento estabelecimento) {
        this.globalApplication = (GlobalApplication) activity.getApplicationContext();
        this.activity = activity;
        this.alertDialog = new AlertDialog.Builder(activity).create();
        this.estabelecimento = estabelecimento;
        this.reserva = new Reserva();
        this.estabelecimentoController = EstabelecimentoController.create();
    }

    public void prepare() {

        if (view != null)
            return;

        // instancia objeto para inflar o layout
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // infla a view
        view = layoutInflater.inflate(R.layout.dg_reservar_estabelecimento, null);

        // set view ao dialog
        alertDialog.setView(view);

        setupWidgets(view);
        setupListeners();

    }

    public void show() {
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void setupWidgets(View view) {

        rgTickets = (RadioGroup) view.findViewById(R.id.rg_tickets);
        lbClickDetalhesReserva = (TextView) view.findViewById(R.id.lb_detalhes_reserva);
        lbHorarioReserva = (TextView) view.findViewById(R.id.lb_horario_reserva);
        textTipoReserva = (TextView) view.findViewById(R.id.text_tipo_reserva);
        btVerHorarios = (Button) view.findViewById(R.id.bt_ver_horarios);
        btCancelar = (Button) view.findViewById(R.id.bt_cancelar);
        btConcluir = (Button) view.findViewById(R.id.bt_concluir);

        App.setButtonTint(btCancelar, Color.parseColor(activity.getString(R.color.red)), PorterDuff.Mode.MULTIPLY);
        App.setButtonTint(btConcluir, Color.parseColor(activity.getString(R.color.green)), PorterDuff.Mode.MULTIPLY);
        App.setButtonTint(btVerHorarios, Color.parseColor(activity.getString(R.color.blue)), PorterDuff.Mode.MULTIPLY);

        for (TipoReserva tipoReserva : estabelecimento.getTiposReserva()) {

            RadioButton radioButton = new RadioButton(activity);
            radioButton.setId(tipoReserva.getId().intValue());
            radioButton.setText(tipoReserva.getTipo() + " - R$ " + App.getFormatCurrency().format(tipoReserva.getValor()) + " para " + String.valueOf(tipoReserva.getQuantidadePessoas()) + " pessoa" + (tipoReserva.getQuantidadePessoas() > 1 ? "(s)" : ""));

            rgTickets.addView(radioButton);
        }
    }

    private void setupListeners() {

        btVerHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SlideDateTimePicker.Builder(activity.getSupportFragmentManager())
                        .setListener(ReservarEstabelecimentoDialog.this)
                        .setInitialDate(new Date())
                        .setIs24HourTime(true)
                        .setMinDate(Calendar.getInstance().getTime())
                        .build()
                        .show();

            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean hasError = false;
                long rbSelectedID = rgTickets.getCheckedRadioButtonId();

                // verifica se há algum usuário na sessão da aplicação
                if (globalApplication.getUsuarioAtivo() == null) {
                    Intent i = new Intent(activity, Login.class);
                    activity.startActivity(i);
                    return;
                }

                if (reserva.getDataReserva() == null) {
                    lbHorarioReserva.setError("Informe a data da reserva");
                    lbHorarioReserva.requestFocus();
                    hasError = true;
                }

                if (rbSelectedID == -1) {
                    textTipoReserva.setError("Selecione o tipo da reserva");
                    textTipoReserva.requestFocus();
                    hasError = true;
                }

                // se nao contem erros
                if (!hasError) {

                    TipoReserva tipoReservaSelecionada = null;

                    for (TipoReserva tipoReserva : estabelecimento.getTiposReserva()) {

                        if (tipoReserva.getId() == rbSelectedID) {
                            tipoReservaSelecionada = tipoReserva;
                            break;
                        }
                    }

                    reserva.setCliente(globalApplication.getUsuarioAtivo());
                    reserva.setEstabelecimento(estabelecimento);
                    reserva.setQuantidadePessoas(tipoReservaSelecionada.getQuantidadePessoas());
                    reserva.setTipoReserva(tipoReservaSelecionada);
                    reserva.setTotal(tipoReservaSelecionada.getValor());
                    reserva.setStatusReserva(Reserva.StatusReserva.PENDENTE);
                    reserva.setData(new Date());

                    try {
                        estabelecimentoController.reservar(view, reserva);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        lbClickDetalhesReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "Detalhes da Reserva");
            }
        });
    }

    @Override
    public void onDateTimeSet(Date date) {
        lbHorarioReserva.setText("Reserva para " + App.formatDateReserva(date));
        reserva.setDataReserva(date);
    }
}
