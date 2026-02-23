package com.example.localizapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import java.util.Locale;

public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static Context setLocale(Context context, String language) {
        persist(context, language);
        return updateResources(context, language);
    }

    private static void persist(Context context, String language) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    public static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    public static String getLanguage(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(SELECTED_LANGUAGE, "en"); // Default is English
    }
}
