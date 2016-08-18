package com.progressivecommunications.commonfile;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by twilightuser on 28/7/16.
 */
public class PreferenceConnector {


    public static final String PREF_NAME = "com.progressivecommunications";


    public static int getUserId(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getInt("userId", 0);

    }

    public static int getonsite(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getInt("onSite", 0);

    }

    public static String getLoginTime(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getString("loginTime", "");

    }

    public static String getServerTime(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getString("serverTime", "");

    }

    public static String getOldPassword(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getString("oldpassword", "");

    }

    public static String getEmail(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getString("email", "");

    }
}
