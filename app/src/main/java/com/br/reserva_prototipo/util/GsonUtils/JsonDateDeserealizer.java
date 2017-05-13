package com.br.reserva_prototipo.util.GsonUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MarioJ on 18/04/16.
 */
public class JsonDateDeserealizer implements JsonDeserializer<Date> {

    private SimpleDateFormat dateFormatDataNascimento = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatHorario = new SimpleDateFormat("HH:mm:ss");

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Date data = null;

        try {
            data = new Date(json.getAsJsonPrimitive().getAsLong());
        } catch (Exception e) {

            try {
                data = formatarData(json.getAsJsonPrimitive().getAsString());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return data;
    }

    private Date formatarData(String data) throws Exception {

        if (data.contains(":"))
            return dateFormatHorario.parse(data);
        else if (data.contains("-"))
            return dateFormatDataNascimento.parse(data);

        throw new Exception("Data invalida");
    }
}
