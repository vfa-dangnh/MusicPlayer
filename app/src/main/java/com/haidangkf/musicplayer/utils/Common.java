package com.haidangkf.musicplayer.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.haidangkf.musicplayer.BuildConfig;
import com.haidangkf.musicplayer.service.MyService;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Common {
    public static final String TYPE_OS = "android";
    public static final int SIZE_IMAGE_MAX = 600;

    public static final String TAG = "my_log";
    public static final int SPLASH_TIME_OUT = 1500;
    public static final int DELAY_SCROLL = 3000;
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_LOADING = 1;
    public static final String KEY_ARTICLE_TYPE = "KEY_ARTICLE_TYPE";
    public static final String KEY_OBJECT_DTO = "KEY_OBJECT_DTO";
    public static final String KEY_ID_INTENT = "KEY_ID_INTENT";
    public static final String KEY_USER_NAME = "KEY_USER_NAME";
    public static final String KEY_LOGIN_ID = "KEY_LOGIN_ID";
    public static final String KEY_USER_ID = "KEY_USER_ID";
    public static final String KEY_RESTAURANT_ID = "KEY_RESTAURANT_ID";
    private static final String PROJECT_ID_REAL = "94053627987";
    private static final String PROJECT_ID_STAGING = "1067417648660";

    public static final String PUSH_SENDER = BuildConfig.FLAVOR.equals("staging") ? PROJECT_ID_STAGING : PROJECT_ID_REAL;
    public static int REQUEST_CODE_FACEBOOK = 64206;

    private Common() {
    }

    public static SEVER_API mServerApiURL = BuildConfig.FLAVOR.equals("staging") ? SEVER_API.STAGING : SEVER_API.REAL;
    public static String mCurrentServer = "";
    public static final String PT_APP_KEY = "cb2619a8b87a017ca72b948901a7ac17";
    public static final String POST_PARAM_APPS_KEY = "00637d54357b83b52449fabfdc80c98c";
    public static final String MALE = "0";
    public static final String FEMALE = "1";
    public static final String INTENT_WEB_VIEW_URL_KEY = "INTENT_WEB_VIEW_URL_KEY";
    public static final String INTENT_WEB_VIEW_PAYMENT_KEY = "INTENT_WEB_VIEW_PAYMENT_KEY";
    public static final String INTENT_WEB_VIEW_PAYMENT_RETURN = "INTENT_WEB_VIEW_PAYMENT_RETURN";
    public static final String INTENT_WEB_VIEW_TITLE_KEY = "INTENT_WEB_VIEW_TITLE_KEY";
    public static final String INTENT_BULLETIN_POST_TITLE = "INTENT_BULLETIN_POST_TITLE";
    public static final String INTENT_BULLETIN_POST_CONTENT = "INTENT_BULLETIN_POST_CONTENT";
    public static final String INTENT_BULLETIN_POST_IMAGE = "INTENT_BULLETIN_POST_IMAGE";
    public static final String INTENT_SUPPORT_CONTENT_DETAIL = "INTENT_SUPPORT_CONTENT_DETAIL";
    public static final String MESSAGE_TEXT = "0";
    public static final String MESSAGE_IMAGE = "1";
    public static final String MESSAGE_AUDIO = "2";
    public static final String MESSAGE_VIDEO = "3";
    public static final String SHOP_ITEM = "shop_item";

    public static final String IN_APP_PURCHASE_ID = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtQ7MoqoHbMYP/g+Zx6oOIvD1KDxHydfidxUETaTRW1yxVsIjEazzHUWNkSJ51LFwRvsfCKatjGQUdDGX/WtaMLkC8LqBWS3itVXOsbZDFIaOQHEjPNGffYd7Oyy0j9SLCQgwCW1EwUG7DySTLTdWyKBjU6+4S2EGhEE30ILmPzzOvBzhQ1ooBJiAK6096a+DSubS384PfBXg/JHGRqJywuK4JgwzhwNrvIi0yojdZ9/CN9gA5NDTWiySe+Pwx+QhRrWUyn1kiZW0D+/DwyEw6mb2ovNg4DUsLgihiEJsll/VHTTq80fVBFG8hPHmbn95uLyWksvpJSnfTLgOLBjZswIDAQAB";

    public enum SEVER_API {
        DEV,
        STAGING,
        REAL
    }

    public static class FoxConfig {
        public static int REGIS_ID = 6662;
        public static int PAYMENT_ID = 6663;
    }

    public static class ApiConfig {
        public static String API_ID = "API_ID_OA1";
        public static String API_KEY = "API_KEY_OA1";
        public static String METHOD_DELETE = "DELETE";
        public static String METHOD_POST = "POST";
        public static String METHOD_GET = "GET";
        public static String METHOD_PUT = "PUT";
        public static String CONNECT_TIME_OUT = "CONNECT_TIME_OUT";
        public static String SOCKET_TIME_OUT = "SOCKET_TIME_OUT";
        public static String CONNECT_ERROR = "CONNECT_ERROR";

        public static String ERROR_PARSE = "ERROR_PARSE";

        public static int ERROR_GET_API = 400;
        public static String ERROR_CLIENT_NOT_FOUND_NUM = "404";
        public static String ERROR_TOKEN_INVALID = "401";
        public static String ERROR_PARSE_NUM = "100";
        public static final int SUCCESS = 0;
        public static final int SUCCESS_200 = 200;
        public static final int DELETE_SUCCESS = 204;
        public static final int PAGE_NOT_FOUND = 404;
        public static final int NETWORK_ERROR = 805;
        public static final int SIGNTURE_INVALID = 802;
        public static final String KEY_ANDROID = "2";
        public static final int ERROR_EMAIL_EXITS = 102;

        //Config server
        public static final String SERVER_DEV_API = "http://52.197.9.169";
        public static final String SERVER_STG_API = "http://52.197.9.169";
        public static final String SERVER_REAL_API = "http://52.197.9.169";

        public static final String VERSION = "/api/v1/";
        public static final String INIT_HOME = "home/index.json";
        public static final String FOODS_CATEGORIES = "foods/categories.json";
        public static final String FOODS_MENU = "foods/menu.json";
        public static final String LIST_RESTAURANT = "restaurants.json";
        public static final String RESTAURANT_SEARCH = "restaurants/search.json";
        public static final String DATA_RESTAURANT = "restaurants/{id}.json";
        public static final String COUPON = "coupons.json";
        public static final String POST_USER_PROFILE = "users.json";
        public static final String GET_USER_PROFILE = "users/detail.json";
        public static final String PUT_USER_PROFILE = "users/profile.json";
        public static final String QUESTIONAIRES = "questionnaires.json";
        public static final String AR_COUPON_USED = "coupons/ar_coupon_used.json";
    }

    public static class ApiKey {
        public static final String HEADER_TOKEN = "X-SagamiApp-Token";
        public static final String APPS_KEY = "apps_key";
        public static final String AREA_ID = "area_id";
        public static final String CATEGORY_ID = "category_id";
        public static final String RESTAURANT_ID = "restaurant_id";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String DISTANCE = "distance";
        public static final String KEY_WORD = "keyword";
        public static final String MARKER_ID = "marker_id";
        public static final String SHOP_ID = "shop_id";
        public static final String DEVICE_ID = "device_id";
        public static final String GENDER = "gender";
        public static final String BIRTHDAY = "birthday";
        public static final String CAREER = "carrier";
        public static final String POSTAL = "postal_code";
        public static final String ID = "id";

    }

    public static void initSeverURL() {
        if (mServerApiURL == SEVER_API.DEV) {
            mCurrentServer = ApiConfig.SERVER_DEV_API;

        } else if (mServerApiURL == SEVER_API.STAGING) {
            mCurrentServer = ApiConfig.SERVER_STG_API;

        } else if (mServerApiURL == SEVER_API.REAL) {
            mCurrentServer = ApiConfig.SERVER_REAL_API;
        }
    }

    public static void startService(Context context, Class<?> serviceClass) {
        Log.d(TAG, "start Service");
        context.startService(new Intent(context, serviceClass));
    }

    public static void stopService(Context context, Class<?> serviceClass) {
        Log.d(TAG, "stop Service");
        context.stopService(new Intent(context, serviceClass));
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void exitApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (isServiceRunning(context, MyService.class)) {
            stopService(context, MyService.class);
        }
    }

    public static boolean currentVersionSupportBigNotification() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            return true;
        }
        return false;
    }

    public static boolean currentVersionSupportLockScreenControls() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return true;
        }
        return false;
    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean checkPasswordIsAlphabetNumeric(String text) {
        String textLowerCase = text.toLowerCase();
        char[] array = textLowerCase.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if ((array[i] >= 'a' && array[i] <= 'z') || (array[i] >= '0' && array[i] <= '9'))
                continue;
            return false;
        }
        return true;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else {
                return contentUri.getPath();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * dismissKeyboard
     *
     * @param context
     * @param edt
     */
    public static void dismissKeyboard(Context context, EditText edt) {
        if (edt == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }

    public static void hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void editButonDialog(AlertDialog alertDialog) {
        TextView btn1 = (TextView) alertDialog.findViewById(android.R.id.button1);
        TextView btn2 = (TextView) alertDialog.findViewById(android.R.id.button2);
        if (btn1 != null) {
            btn1.setTextSize(15);
            btn1.setTextColor(Color.parseColor("#57BCDE"));
            btn1.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        }
        if (btn2 != null) {
            btn2.setTextSize(15);
            btn2.setTextColor(Color.parseColor("#57BCDE"));
            btn2.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        }
    }

    public static AlertDialog dialogConfirmYesNo(Activity activity, int resTitle, int resMsg, DialogInterface.OnClickListener listenerPositive) {
        return dialogConfirmYesNo(activity, resTitle == 0 ? null : activity.getString(resTitle), resMsg == 0 ? null : activity.getString(resMsg), null, null, listenerPositive, null);
    }

    public static AlertDialog dialogConfirmYesNo(Activity activity, String title, String msg, DialogInterface.OnClickListener listenerPositive) {
        return dialogConfirmYesNo(activity, title, msg, null, null, listenerPositive, null);
    }

    public static AlertDialog dialogConfirmYesNo(Activity activity, String title, String msg, String resPositive, String resNegative, DialogInterface.OnClickListener listenerPositive, DialogInterface.OnClickListener listenerNegative) {
        if (activity == null) return null;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setTitle(TextUtils.isEmpty(title) ? "" : Html.fromHtml("<b>" + title + "</b>"))
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(TextUtils.isEmpty(resNegative) ? "No" : resNegative, listenerNegative != null ? listenerNegative : new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(TextUtils.isEmpty(resPositive) ? "Yes" : resPositive, listenerPositive != null ? listenerPositive : new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog[] alertDialog = new AlertDialog[1];
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog[0] = dialog.create();
                alertDialog[0].show();

                editButonDialog(alertDialog[0]);

            }
        });
        return alertDialog[0];
    }

    public static AlertDialog dialogConfirmOk(Activity activity, String title, String msg, DialogInterface.OnClickListener listenerOk) {
        if (activity == null) return null;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setTitle(Html.fromHtml("<b>" + title + "</b>"))
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, listenerOk != null ? listenerOk : new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog[] alertDialog = new AlertDialog[1];
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog[0] = dialog.create();
                alertDialog[0].show();

                editButonDialog(alertDialog[0]);

            }
        });
        return alertDialog[0];
    }

    public static AlertDialog dialogConfirmOk(Activity activity, int resTitle, int resMsg, DialogInterface.OnClickListener listenerOk) {
        if (activity == null) return null;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setTitle(resTitle == 0 ? "" : Html.fromHtml("<b>" + activity.getString(resTitle) + "</b>"))
                .setMessage(resMsg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, listenerOk != null ? listenerOk : new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog[] alertDialog = new AlertDialog[1];
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog[0] = dialog.create();
                alertDialog[0].show();

                editButonDialog(alertDialog[0]);

            }
        });
        return alertDialog[0];
    }

    public static AlertDialog dialogConfirmOkNoTitle(Activity activity, int resMsg, DialogInterface.OnClickListener listenerOk) {
        if (activity == null) return null;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setMessage(resMsg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, listenerOk != null ? listenerOk : new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog[] alertDialog = new AlertDialog[1];
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog[0] = dialog.create();
                alertDialog[0].show();

                editButonDialog(alertDialog[0]);

            }
        });
        return alertDialog[0];
    }

    public static AlertDialog dialogConfirmOk(Activity activity, int resTitle, int resMsg, int resOk, DialogInterface.OnClickListener listenerOk) {
        if (activity == null) return null;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setTitle(Html.fromHtml("<b>" + activity.getString(resTitle) + "</b>"))
                .setMessage(resMsg)
                .setCancelable(false)
                .setPositiveButton(resOk, listenerOk != null ? listenerOk : new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog[] alertDialog = new AlertDialog[1];
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog[0] = dialog.create();
                alertDialog[0].show();

                editButonDialog(alertDialog[0]);

            }
        });
        return alertDialog[0];
    }

    public static AlertDialog dialogConfirmOk(Activity activity, String resTitle, String resMsg, int resOk, DialogInterface.OnClickListener listenerOk) {
        if (activity == null) return null;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setTitle(Html.fromHtml("<b>" + resTitle + "</b>"))
                .setMessage(resMsg)
                .setCancelable(false)
                .setPositiveButton(resOk, listenerOk != null ? listenerOk : new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog[] alertDialog = new AlertDialog[1];
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog[0] = dialog.create();

                alertDialog[0].show();

                editButonDialog(alertDialog[0]);

            }
        });
        return alertDialog[0];
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public static String convertNGWords(String input) {
        String output = "";
        String convertStr = input.substring(1, input.length());
        convertStr = convertStr.replaceAll(".", "*");
        output = input.substring(0, 1) + convertStr;
        return output;
    }

    /**
     * installAppLine
     */
    public static void installAppFromGooglePlay(Context mContext,
                                                String packName) {
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("market://details?id=" + packName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("http://play.google.com/store/apps/details?id="
                            + packName)));
        }
    }

    /**
     * checkAppInstalled
     *
     * @return
     */
    public static boolean checkAppInstalled(Context mContext, String packName) {
        PackageManager pack_manager = mContext.getPackageManager();
        boolean resolved = false;
        List<ApplicationInfo> list = pack_manager.getInstalledApplications(0);
        // Check if Line is installed
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).packageName.equals(packName)) {
                // do what you want
                resolved = true;
                break;
            }
        }
        return resolved;
    }

    public static String convertStringtoBase64(String s) {
        if (TextUtils.isEmpty(s)) return s;
        String status = null;
//        byte[] byteArray;
//        try {
//            byteArray = s.getBytes("UTF-16");
//            status = new String(Base64.decode(Base64.encode(byteArray,
//                    Base64.DEFAULT), Base64.DEFAULT));
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            status = s;
//        }
        // Sending side
        byte[] data = new byte[0];
        try {
            data = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

//        // Receiving side
//        data = Base64.decode(base64, Base64.DEFAULT);
//        try {
//            status = new String(data, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return base64;
    }

    public static void deleteFolder(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

    public static void deleteFile(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            dir.delete();
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public static void restartApp(Activity context) {
//        Intent mStartActivity = new Intent(context, SplashActivity.class);
//        int mPendingIntentId = 123456;
//        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        Intent i = context.getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(context.getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.finish();
//        System.exit(0);
        context.startActivity(i);


    }

    public static String getCurrentActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        return taskInfo.get(0).topActivity.getClassName();
    }

    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
// get IMEI
        return tm.getDeviceId() == null ? "0" : tm.getDeviceId();
//        return "wieureewdsfasdasdfl;o99ugjtguyttuiotuturesdasw";
    }

    public static String getDeviceName() {
        return android.os.Build.MODEL;
//        return "wieureewdsfasdasdfl;o99ugjtguyttuiotuturesdasw";
    }

    public static String formatMilisecond(long millis) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static String formatMilisecondNotHours(long millis) {
        return String.format("%02d:%02d", //TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static String getAppVersion(Context activity) {
        String appVersion = "1";
        try {
            appVersion = activity.getPackageManager()
                    .getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    // check wifi, 3G connect
    public static boolean checkInternetConnection(Context context) {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    //loi keet noi
    public static void handleErrorConnect(Activity context) {
        if (Common.checkInternetConnection(context)) {
            Common.dialogConfirmOk(context, "", "API error has occurred", null);
//            Toast.makeText(context, "Lỗi kết nối tới máy chủ, vui lòng thử lại sau.", Toast.LENGTH_LONG).show();
//            context.getSupportActionBar().setTitle("Lỗi tải tin...");
        } else {
            Common.dialogConfirmOk(context, "", "No Internet connection", null);
//            Toast.makeText(context, "Không có kết nối internet. Vui lòng kiểm tra internet và thử lại", Toast.LENGTH_LONG).show();
//            context.getSupportActionBar().setTitle("Bạn đang offline...");
        }

    }

    //loi keet noi
    public static void handleErrorApi(Activity context, String msg) {
        if (TextUtils.isEmpty(msg)) msg = "API error has occurred";
        Common.dialogConfirmOk(context, "", msg, null);
//        if(Common.checkInternetConnection(context)){
//            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//            context.getSupportActionBar().setTitle("Lỗi tải tin...");
//        } else {
//            Toast.makeText(context, "Vui lòng kiểm tra lại kết nối 3G hoặc Wifi...", Toast.LENGTH_LONG).show();
////            context.getSupportActionBar().setTitle("Bạn đang offline...");
//        }

    }

    // loi ket noi
    public static void handleErrorApi(Activity context, ArrayList<String> msg) {
        String message;
        if (msg == null || msg.isEmpty()) message = "API error has occurred";
        message = msg.get(0);
        Common.dialogConfirmOk(context, "", message, null);
    }

    // loi ket noi
//    public static void handleErrorApi(Activity context, Response<?> response) {
//        Common.dialogConfirmOk(context, "", ErrorUtils.parseError(response).message(), null);
//    }

    public static String getFormatedDate(String strDate, String sourceFormate,
                                         String destinyFormate) {
        SimpleDateFormat df;
        df = new SimpleDateFormat(sourceFormate);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = df.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        df = new SimpleDateFormat(destinyFormate);
        return df.format(date);

    }
}
