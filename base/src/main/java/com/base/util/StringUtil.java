package com.base.util;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by on 04/01/2016.
 * <p>
 * Utility for string
 */
public class StringUtil {

    public static boolean checkEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}