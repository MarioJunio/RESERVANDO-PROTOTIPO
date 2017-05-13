package com.br.reserva_prototipo.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MarioJ on 17/07/16.
 */
public class PatternValidation {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN = "^[A-Za-z0-9]{3,}$";

    public static boolean isEmailValid(String email) {
        return matches(email, EMAIL_PATTERN);
    }

    public static boolean isPasswordValid(String password) {
        return matches(password, PASSWORD_PATTERN);
    }

    private static boolean matches(String value, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }

}
