package com.br.reserva_prototipo.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.br.reserva_prototipo.activity.Login;

/**
 * Created by MarioJ on 08/07/16.
 */
public class SessionManager {

    public static final String PREF_SESSION = "SESSION";
    public static final String SESSION_IDENTIFIER = "AUTH-SESSION-ID";
    public static final String SESSION_TOKEN = "AUTH-SESSION-TOKEN";

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private static void instance(Context context) {
        preferences = context.getSharedPreferences(PREF_SESSION, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void createLoginSession(Context context, String sessionId, String sessionToken) {
        instance(context);

        editor.putString(SESSION_IDENTIFIER, sessionId);
        editor.putString(SESSION_TOKEN, sessionToken);
        editor.commit();
    }

    public static void checkLogin(Context context) {
        instance(context);

        // checa se o sessionid é vazio, se sim então não está logado
        if (preferences.getString(SESSION_IDENTIFIER, "").isEmpty() || preferences.getString(SESSION_TOKEN, "").isEmpty())
            redirectToLogin(context);

    }

    public static void logout(Context context) {
        instance(context);

        editor.clear().commit();
        redirectToLogin(context);
    }

    public static String readLoginSessionIdentifier(Context context) {
        instance(context);
        return preferences.getString(SESSION_IDENTIFIER, "");
    }

    public static String readLoginSessionToken(Context context) {
        instance(context);
        return preferences.getString(SESSION_TOKEN, "");
    }

    private static void redirectToLogin(Context context) {

        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

}
