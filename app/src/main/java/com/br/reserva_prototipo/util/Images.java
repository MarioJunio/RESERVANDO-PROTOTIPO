package com.br.reserva_prototipo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by MarioJ on 15/12/15.
 */
public class Images {

    public static byte[] parse(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return outputStream.toByteArray();
        }

        return null;
    }

    public static Bitmap bitmap(byte[] imageBytes) {

        if (imageBytes == null)
            return null;

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

}
