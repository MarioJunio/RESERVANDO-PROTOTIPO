package com.br.reserva_prototipo.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.reserva_prototipo.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static DecimalFormat getFormatCurrency() {
        return new DecimalFormat("#,###.00");
    }

    public static DecimalFormat getFormatPercent() {
        return new DecimalFormat("#.#");
    }

    public static void setButtonTint(Button button, int color, PorterDuff.Mode mode) {

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP && button instanceof AppCompatButton) {
            ((AppCompatButton) button).getBackground().setColorFilter(color, mode);
        }
    }

    public static String formatDateReserva(Date date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE',' dd 'de' MMMM 'as' HH:mm");
        return simpleDateFormat.format(date);
    }
}
