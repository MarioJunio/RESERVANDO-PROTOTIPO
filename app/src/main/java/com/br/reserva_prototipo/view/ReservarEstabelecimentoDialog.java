package com.br.reserva_prototipo.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.adapter.TipoReservaAdapter;
import com.br.reserva_prototipo.model.TipoReserva;
import com.br.reserva_prototipo.util.AppUtils;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by MarioJ on 04/04/16.
 */
public class ReservarEstabelecimentoDialog extends SlideDateTimeListener {

    private static final String TAG = "ReservatEstabelecimento";

    // utils
    private AppCompatActivity activity;
    private View view;
    private AlertDialog alertDialog;
    private List<TipoReserva> tiposReserva;
    private TipoReservaAdapter tipoReservaAdapter;

    // widgets
    private TextView lbHorarioReserva;
    private NumberPicker npQuantidadePessoas;
    private Spinner spinTiposReserva;
    private Button btVerHorarios;
    private Button btCancelar, btConcluir;

    public ReservarEstabelecimentoDialog(AppCompatActivity activity, List<TipoReserva> tiposReserva) {
        this.activity = activity;

        TipoReserva def = new TipoReserva();
        def.setId(0l);
        def.setTipo("Selecione");

        tiposReserva.add(0, def);

        alertDialog = new AlertDialog.Builder(activity).create();
        this.tiposReserva = tiposReserva;
        this.tipoReservaAdapter = new TipoReservaAdapter(activity, tiposReserva);
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

        lbHorarioReserva = (TextView) view.findViewById(R.id.lb_horario_reserva);
        npQuantidadePessoas = (NumberPicker) view.findViewById(R.id.np_quantidade_pessoas);
        btVerHorarios = (Button) view.findViewById(R.id.bt_ver_horarios);
        spinTiposReserva = (Spinner) view.findViewById(R.id.spin_tipos_reserva);
        btCancelar = (Button) view.findViewById(R.id.bt_cancelar);
        btConcluir = (Button) view.findViewById(R.id.bt_concluir);

        npQuantidadePessoas.setMinValue(1);
        npQuantidadePessoas.setMaxValue(10);
        npQuantidadePessoas.setWrapSelectorWheel(true);

        // carrega os tipos de reserva para selecionar
        spinTiposReserva.setAdapter(tipoReservaAdapter);

        AppUtils.setButtonTint(btCancelar, Color.parseColor(activity.getString(R.color.red)), PorterDuff.Mode.MULTIPLY);
        AppUtils.setButtonTint(btConcluir, Color.parseColor(activity.getString(R.color.green)), PorterDuff.Mode.MULTIPLY);
        AppUtils.setButtonTint(btVerHorarios, Color.parseColor(activity.getString(R.color.blue)), PorterDuff.Mode.MULTIPLY);
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
                //TODO: Realizar reserva
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onDateTimeSet(Date date) {
        lbHorarioReserva.setText("Reserva para " + AppUtils.formatDateReserva(date));
    }
}
