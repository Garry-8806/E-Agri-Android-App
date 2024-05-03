package com.sourcey.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by SDinesh on 10-02-2018.
 */

public class Preferences {
    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }

    public static void setDefaults(String str_key, String value, Context context) {
        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = shre.edit();
        edit.putString(str_key, value);
        edit.apply();
    }
}
