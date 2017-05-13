package com.br.reserva_prototipo.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by MarioJ on 10/03/16.
 */
public class Times {

    private static final String TAG = "Times";

    public static boolean isOpen(Date horarioInicio, Date horarioFim) {

        Calendar now = Calendar.getInstance();
        Calendar dataInicio = Calendar.getInstance();
        Calendar dataFim = Calendar.getInstance();

        // temp
        Calendar dataInicioTemp = Calendar.getInstance();
        Calendar dataFimTemp = Calendar.getInstance();

        dataInicioTemp.setTime(horarioInicio);
        dataFimTemp.setTime(horarioFim);

        dataInicio.set(Calendar.HOUR_OF_DAY, dataInicioTemp.get(Calendar.HOUR_OF_DAY));
        dataInicio.set(Calendar.MINUTE, dataInicioTemp.get(Calendar.MINUTE));
        dataInicio.set(Calendar.SECOND, 0);

        dataFim.set(Calendar.HOUR_OF_DAY, dataFimTemp.get(Calendar.HOUR_OF_DAY));
        dataFim.set(Calendar.MINUTE, dataFimTemp.get(Calendar.MINUTE));
        dataFim.set(Calendar.SECOND, 0);

        return now.after(dataInicio) && now.before(dataFim);
    }
}
