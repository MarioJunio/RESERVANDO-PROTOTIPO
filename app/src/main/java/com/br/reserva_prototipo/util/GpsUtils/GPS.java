package com.br.reserva_prototipo.util.GpsUtils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.br.reserva_prototipo.model.filter.Coordenada;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by MarioJ on 27/04/16.
 */
public class GPS {

    public static final String TAG = "GPS";

    public static String getCidadeAtual(Context context) throws IOException {

        Coordenada coordenada = getCoordenadaAtual(context);

        if (coordenada != null) {

            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            if (geocoder.isPresent()) {

                List<Address> addresses = geocoder.getFromLocation(coordenada.getLatitude(), coordenada.getLongitude(), 1);

                if (!addresses.isEmpty()) {
                    String cidade = addresses.get(0).getLocality();
                    Log.d(TAG, "Localidade: " + cidade);
                    return cidade;
                }

            }

        }

        return null;
    }

    public static Coordenada getCoordenadaAtual(Context context) {

        GPSTracker gpsTracker = new GPSTracker(context);

        Coordenada coordenada = null;

        if (gpsTracker.canGetLocation())
            coordenada = new Coordenada(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        else
            gpsTracker.showSettingsAlert();

        return coordenada;
    }

}
