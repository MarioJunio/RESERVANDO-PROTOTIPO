package com.br.reserva_prototipo.util;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.reserva_prototipo.R;

/**
 * Created by MarioJ on 17/03/16.
 */
public class AppUtils {

    public static void opened(ImageView imgView, TextView text, Context context) {
        imgView.setColorFilter(Color.parseColor(context.getString(R.color.green)));
        imgView.setImageResource(R.drawable.ic_unlock);
        text.setText(context.getString(R.string.estabelecimento_aberto));
        text.setTextColor(Color.parseColor(context.getString(R.color.green)));
    }

    public static void closed(ImageView imgView, TextView text, Context context) {
        imgView.setColorFilter(Color.parseColor(context.getString(R.color.red)));
        imgView.setImageResource(R.drawable.ic_lock);
        text.setText(context.getString(R.string.estabelecimento_fechado));
        text.setTextColor(Color.parseColor(context.getString(R.color.red)));
    }

}
