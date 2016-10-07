package com.haidangkf.musicplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.Set;

public final class SharedPreferencesUtil {

    ////////////////////////////////////////////////////////////////////////////
    // Constant
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default name
     */
    private static final String DEFAULT_NAME = "AppName";
    /**
     * start app
     */
    public static final String START_APP_FIRST = "START_APP_FIRST";
    public static final String TOKEN = "TOKEN";
    public static final String LIST_COUPON_USED = "LIST_COUPON_USED";

    ////////////////////////////////////////////////////////////////////////////
    // public Constant
    ////////////////////////////////////////////////////////////////////////////


    /**
     * private constructor
     */
    private SharedPreferencesUtil() {

    }

    ////////////////////////////////////////////////////////////////////////////
    // Public Constant
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Static functions
    ////////////////////////////////////////////////////////////////////////////

    /**
     * set string value to preference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setString(Context context, String key, String value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        if (TextUtils.isEmpty(value)) {
            edit.remove(key);
        } else {
            edit.putString(key, value);
        }
        edit.apply();
    }

    public static boolean checkKey(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.contains(key);
    }

    /**
     * get string value
     *
     * @param context
     * @param key
     * @return 該当なし:null
     */
    public static String getString(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }


    /**
     * get string value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return 該当なし:null
     */
    public static String getString(Context context, String key, String defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, defaultValue);
    }

    /**
     * set boolean value to preference
     *
     * @param context
     * @param key
     * @param bool
     */
    public static void setBoolean(Context context, String key, boolean bool) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        edit.putBoolean(key, bool);
        edit.apply();
    }

    /**
     * get boolean value
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    /**
     * get boolean value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultValue);
    }

    /**
     * save long value to preference
     *
     * @param context
     * @param key
     * @param l
     */
    public static void setLong(Context context, String key, long l) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        edit.putLong(key, l);
        edit.apply();
    }

    /**
     * save long value to preference
     *
     * @param context
     * @param key
     * @param set
     */
    public static void setArraySetString(Context context, String key, Set<String> set) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        edit.putStringSet(key, set);
        edit.apply();
    }

    /**
     * get long value
     *
     * @param context
     * @param key
     * @return
     */
    public static Set<String> getArraySetString(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return new HashSet<String>();
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getStringSet(key, new HashSet<String>());
    }

    /**
     * get long value
     *
     * @param context
     * @param key
     * @return
     */
    public static long getLong(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return 0L;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getLong(key, 0L);
    }

    /**
     * save int value to preference
     *
     * @param context
     * @param key
     * @param integer
     */
    public static void setInt(Context context, String key, int integer) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        edit.putInt(key, integer);
        edit.apply();
    }

    /**
     * get int value
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return 0;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    /**
     * get int value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Context context, String key, int defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, defaultValue);
    }

    /**
     * remove key SharedPreferences
     *
     * @param context Context
     * @param key
     */
    public static boolean removeKey(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = settings.edit();
        boolean result = edit.remove(key).commit();
        edit.apply();
        return result;
    }

    /**
     * delete all SharedPreferences
     *
     * @param context Context
     */
    public static boolean deleteAll(Context context) {
        SharedPreferences settings = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        boolean result = settings.edit().clear().commit();
        Editor edit = settings.edit();
        edit.apply();
        return result;
    }

}
