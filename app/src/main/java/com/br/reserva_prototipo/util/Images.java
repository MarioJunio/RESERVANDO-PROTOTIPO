package com.br.reserva_prototipo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by MarioJ on 15/12/15.
 */
public class Images {

    public static final String PROFILE_PICTURES = "Reservando/";
    public static final String PICTURE_COMPRESSION = ".jpg";
    private static final String TAG = "Images";

    public static byte[] parse(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return outputStream.toByteArray();
        }

        return null;
    }

    public static Bitmap toBitmap(byte[] imageBytes) {

        if (imageBytes == null)
            return null;

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public static Bitmap toBitmap(String path) {
        return BitmapFactory.decodeFile(path);
    }

    /**
     * Transforma e escreve os bytes da imagem no disco
     *
     * @param id      nome do arquivo de imagem
     * @param picture bytes a serem escritos no disco como imagem
     */
    public static final void writePicture(final long id, final byte[] picture) {

        new AsyncTask<byte[], String, String>() {

            File profilePicturesDir;

            @Override
            protected void onPreExecute() {
                profilePicturesDir = new File(Environment.getExternalStorageDirectory(), PROFILE_PICTURES);
            }

            @Override
            protected String doInBackground(byte[]... params) {

                createProfilePictureStorageDir();

                File filePicture = new File(profilePicturesDir, String.valueOf(id).concat(PICTURE_COMPRESSION));

                try {
                    filePicture.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    FileOutputStream fos = new FileOutputStream(filePicture.getPath());
                    fos.write(picture);
                    fos.close();

                    Log.d(TAG, "WROTE: " + String.valueOf(id).concat(PICTURE_COMPRESSION));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

        }.execute();

    }

    /**
     * Cria diretorio para salvar as imagens de perfil dos estabelecimentos carregadas da internet
     */
    private static void createProfilePictureStorageDir() {

        File profilePicturesDir = new File(Environment.getExternalStorageDirectory(), PROFILE_PICTURES);

        if (!profilePicturesDir.exists())
            profilePicturesDir.mkdirs();


    }

}
