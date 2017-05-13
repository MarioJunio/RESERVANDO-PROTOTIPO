package com.br.reserva_prototipo.util.ImageUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by MarioJ on 15/12/15.
 */
public class Images {

    private static final String TAG = "Images";

    public static final String PROFILE_PICTURES = "Reservando/";
    public static final String PICTURE_COMPRESSION = ".png";
    public static final int PEGAR_IMAGEM = 1;


    public enum Identidade {

        Usuario("U"), Estabelecimento("E");

        private String id;

        Identidade(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

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
     * @param nomeImagem nome do arquivo que contera a imagem
     * @param identidade identidade da imagem
     * @param imagem     bytes contendo a imagem a ser salva
     */
    public static final void writePicture(final String nomeImagem, final Identidade identidade, final byte[] imagem) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Escrevendo imagem...");

                // verifica se há imagem para ser gravada no disco
                if (imagem == null)
                    return;

                // cria diretorio padrão das imagem do app
                criarDiretorioPadraoImagens();

                // Diretorio padrão das imagens usadas pelo App
                File dirImagens = new File(Environment.getExternalStorageDirectory(), PROFILE_PICTURES);

                // Arquivo representando a imagem a ser salva no disco
                File fileImagem = new File(dirImagens, identidade.getId() + nomeImagem + PICTURE_COMPRESSION);

                try {

                    // cria novo arquivo para salvar a imagem, se foi criado, salve os bytes da imagem no arquivo recem criado
                    if (fileImagem.createNewFile()) {
                        FileOutputStream fos = new FileOutputStream(fileImagem.getPath());
                        fos.write(imagem);
                        fos.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        executorService.shutdown();

        // Nao ha imagem carregada ainda
        if (imagem == null) {
            return;
        }

    }

    public static File readImage(String nomeImagem, Identidade identidade) {
        return new File(new File(Environment.getExternalStorageDirectory(), Images.PROFILE_PICTURES), identidade.getId() + nomeImagem + Images.PICTURE_COMPRESSION);
    }

    /**
     * Cria diretorio para salvar as imagens de perfil dos estabelecimentos carregadas da internet
     */
    private static void criarDiretorioPadraoImagens() {

        File profilePicturesDir = new File(Environment.getExternalStorageDirectory(), PROFILE_PICTURES);

        if (!profilePicturesDir.exists())
            profilePicturesDir.mkdirs();
    }

    public static void pegarImagem(Activity activity) {

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Selecione a foto de perfil");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        activity.startActivityForResult(chooserIntent, Images.PEGAR_IMAGEM);
    }

    public static byte[] getBytesArquivo(InputStream is) throws IOException {
        return IOUtils.toByteArray(is);
    }

    public static byte[] getImagemComprimida(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

}
