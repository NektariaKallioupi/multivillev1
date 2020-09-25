package com.example.multivillev1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class SaveSharedPreference {

    static final String PREF_ID= "ID";
    static final String PREF_FIRSTVIEW="FIRSTVIEW";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setId(Context ctx, String id)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_ID, id);
        editor.commit();
    }

    public static String getId(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_ID, "");
    }

    public static void clearId(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }

    public  static void setFirstTimeViewLessonOrNot(Context ctx,boolean firstview){

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_FIRSTVIEW,firstview);
        editor.commit();
    }

    public static boolean getFirstTimeViewLessonOrNot(Context ctx){

        return  getSharedPreferences(ctx).getBoolean(PREF_FIRSTVIEW,FALSE);
    }
}
