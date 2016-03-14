package com.br.reserva_prototipo.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by MarioJ on 10/03/16.
 */
public class Times {

    private static final String TAG = "Times";

    public static boolean isOpen(Calendar horarioInicio, Calendar horarioFim) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Calendar now = Calendar.getInstance();
        Calendar dataInicio = Calendar.getInstance();
        Calendar dataFim = Calendar.getInstance();

        dataInicio.set(Calendar.HOUR_OF_DAY, horarioInicio.get(Calendar.HOUR_OF_DAY));
        dataInicio.set(Calendar.MINUTE, horarioInicio.get(Calendar.MINUTE));
        dataInicio.set(Calendar.SECOND, 0);

        dataFim.set(Calendar.HOUR_OF_DAY, horarioFim.get(Calendar.HOUR_OF_DAY));
        dataFim.set(Calendar.MINUTE, horarioFim.get(Calendar.MINUTE));
        dataFim.set(Calendar.SECOND, 0);

        Log.d(TAG, dateFormat.format(now.getTime()));
        Log.d(TAG, dateFormat.format(dataInicio.getTime()));
        Log.d(TAG, dateFormat.format(dataFim.getTime()));

        return now.after(dataInicio) && now.before(dataFim);
    }
}
