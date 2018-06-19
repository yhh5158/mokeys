
package com.room.mokeys.kit;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

@SuppressLint("DefaultLocale")
public class Utils {
    private static final String TAG = "Utils";
    private static float sLayoutScale = 0.0f;

    public static int[] getPositionInWindow(View v) {
        int[] location = {
                0, 0
        };
        if (v != null) {
            v.getLocationInWindow(location);
        }

        return location;
    }

    public static int[] getOffsetInWindow(View v) {
        int[] location = {
                0, 0
        };

        while (v != null) {
            location[0] += v.getLeft();
            location[1] += v.getTop();


            ViewParent parent = v.getParent();
            if (parent != null && parent instanceof View) {
                v = (View) parent;

                continue;
            }

            break;
        }

        return location;
    }

    /**
     * 获取sign
     *
     * @param treeMap
     * @param secretKey
     * @return
     */
    public static String getSignedParams(TreeMap<String, String> treeMap, String secretKey) {
        StringBuilder signedParams = new StringBuilder();
        if (treeMap != null) {
            // 1.将treeMap中的值用#连接并与secretKey合并。
            Set<String> keySet = treeMap.keySet();
            Iterator<String> iterator = keySet.iterator();
            StringBuilder unsignedValues = new StringBuilder();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Log.d(TAG, "key="+key);
                if (!TextUtils.isEmpty(key)) {
                    String value = treeMap.get(key);
                    Log.d(TAG, "raw__value="+value);
                    if (!TextUtils.isEmpty(value)) {
                        value = value.trim(); // trim的行为：remove开始结尾的<= 32的字符
                        Log.d(TAG, "trim_value="+value);
                        if (!TextUtils.isEmpty(value)) {
                            try {
                                String encodeValue = URLEncoder.encode(value, "UTF-8");
                                signedParams.append(key).append('=').append(encodeValue)
                                        .append('&');

                                if (!value.equals("0")) {
                                    unsignedValues.append(value).append('#');
                                }
                            } catch (UnsupportedEncodingException e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                    }
                }
            }

            if (signedParams.length() > 0) {
                unsignedValues.append(secretKey);
                Log.d(TAG, "unsignedValues="+unsignedValues.toString());
                // 2.进行换算sign
                try {
                    String sign = URLEncoder.encode(getHash(unsignedValues.toString()),
                            "UTF-8").toLowerCase();

                    treeMap.put("sign", sign); // 将sign值放入map以备其它模块使用

                    signedParams.append("sign=").append(sign);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }

        return signedParams.toString();
    }

    public static long toLong(String s) {
        long l = 0;

        try {
            if (TextUtils.isEmpty(s) || s.startsWith(".")) {
                return l;
            }

            int begin = s.indexOf(".");
            if (begin > 0) {
                s = s.substring(0, begin);
            }

            s = s.replaceAll(",", "");
            Log.d(TAG, "--> toLong(String s)： "+ s);

            if (!TextUtils.isDigitsOnly(s)) {
                s = retriveDigit(s);
            }

            l = Long.valueOf(s);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return l;
    }

    public static String getHash(String uri) {
        String ret = getHash(uri.getBytes());
        if (TextUtils.isEmpty(ret)) {
            ret = uri;
        }
        return ret;
    }

    private static String getHash(byte[] bytes) {
        MessageDigest mDigest;
        try {
            mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(bytes);

            byte d[] = mDigest.digest();

            return toHexString(d);

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private static final char HEX_DIGITS[] = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static String toHexString(byte[] b) { // String to byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static void initLayoutScale(Context context) {
        int[] size = getDeviceSize(context);
        int w = size[0];
        int h = size[1];

        if (w > h) {
            w = w * 2 / 3;
        }

        float scale = 0.0f;
        if (w < Config.LAYOUT_MEDIUM) {
            scale = 1.0f;
        }
        else if (w < Config.LAYOUT_LARGE) {
            scale = (float) Config.LAYOUT_MEDIUM / Config.LAYOUT_NORMAL;
        }
        else if (w < Config.LAYOUT_XLARGE) {
            scale = (float) Config.LAYOUT_LARGE / Config.LAYOUT_NORMAL;
        }
        else {
            scale = (float) Config.LAYOUT_XLARGE / Config.LAYOUT_NORMAL;
        }

        float density = context.getResources().getDisplayMetrics().density;
        if (density >= 1.0f && scale > density) {
            scale /= density;
        }
        else {
            scale = 1.0f;
        }

        sLayoutScale = scale;
    }

    public static float getScale(Context context) {
        if (sLayoutScale <= 0) {
            initLayoutScale(context);
        }

        return sLayoutScale;
    }

    public static float parseSize(Context context, float fontSize) {
        float scale = getScale(context);
        return fontSize * scale;
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = getScale(context);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipValue * scale, context.getResources().getDisplayMetrics());
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, context.getResources().getDisplayMetrics());
    }

    public static String retriveDigit(String paramString) {
        StringBuilder localStringBuilder = new StringBuilder();

        boolean isNegative = false;
        if (paramString.startsWith("-")) {
            isNegative = true;
        }
        int len = paramString.length();

        for (int i = 0; i < len; ++i) {
            char c = paramString.charAt(i);

            if ((c < '0') || (c > '9')) {
                continue;
            }
            localStringBuilder.append(c);
        }

        if (localStringBuilder.toString().length() <= 0) {
            return "0";
        } else {
            if (isNegative) {
                return "-" + localStringBuilder.toString();
            } else {
                return localStringBuilder.toString();
            }
        }
    }

    public static int getNetType(Context context) {
        int netType = 0;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String extraInfo = info.getExtraInfo();
            Log.d(TAG, "extraInfo="+extraInfo);
            int type = info.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                netType = 1;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                int subType = info.getSubtype();
                switch (subType) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        netType = get2GApnConfig(extraInfo);
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case NETWORK_TYPE_EVDO_B:
                    case NETWORK_TYPE_LTE:
                    case NETWORK_TYPE_EHRPD:
                    case NETWORK_TYPE_HSPAP:
                        netType = get3GApnConfig(extraInfo);
                        
                }
            }
        }
        return netType;
    }

    private static final int NET_TYPE_3GWAP = 2;
    private static final int NET_TYPE_3GNET = 3;
    private static final int NET_TYPE_2GWAP = 4;
    private static final int NET_TYPE_2GNET = 5;
    private static final int NETWORK_TYPE_EVDO_B = 12;
    private static final int NETWORK_TYPE_LTE = 13;
    private static final int NETWORK_TYPE_EHRPD = 14;
    private static final int NETWORK_TYPE_HSPAP = 15;
    private static final String CHINA_MOBILE_46000 = "46000";
    private static final String CHINA_MOBILE_46002 = "46002";
    private static final String CHINA_MOBILE_46007 = "46007";

    private static int get2GApnConfig(String extraInfo) {
        if (extraInfo != null) {
            String info = extraInfo.toLowerCase();
            if (info != null && info.contains("wap")) {
                return NET_TYPE_2GWAP;
            }
        }

        return NET_TYPE_2GNET;
    }

    private static int get3GApnConfig(String extraInfo) {
        if (extraInfo != null) {
            String info = extraInfo.toLowerCase();
            if (info != null && info.contains("wap")) {
                return NET_TYPE_3GWAP;
            }
        }

        return NET_TYPE_3GNET;
    }

    public static void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = ((Activity) context).getCurrentFocus();
        if (focus != null) {
            imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }

    public static void inputMethodShowImplicit(Context context, View view) {
        InputMethodManager inputMgr = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMgr != null) {
            inputMgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void inputMethodShowForce(Context context, View view) {
        InputMethodManager inputMgr = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMgr != null) {
            inputMgr.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public static void inputMethodHideNotAlways(Activity activity) {
        InputMethodManager inputMgr = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusView = activity.getCurrentFocus();
        if (focusView != null && inputMgr != null) {
            IBinder ib = focusView.getWindowToken();
            if (ib != null) {
                inputMgr.hideSoftInputFromWindow(ib, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static boolean isInputMethodActive(Activity activity, View view) {
        InputMethodManager inputMgr = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMgr != null && inputMgr.isActive(view);
    }

    public static int getDensityDpi(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        int densityDpi = metrics.densityDpi;
        Log.d(TAG, "densityDpi = "+ densityDpi);

        return densityDpi;
    }

    public static boolean isMDpi(Context context) {
        int densityDpi = getDensityDpi(context);
        return densityDpi == 160;
    }

    public static int[] getDeviceSize(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        int width = metrics.widthPixels; // 屏幕绝对宽度（px）
        int height = metrics.heightPixels; // 屏幕绝对高度（px）

        // 转换为基准dp
        float density = metrics.density;
        width = (int) (width / density);
        height = (int) (height / density);

        return (new int[] {
                width, height
        });
    }

    public static boolean hasIntentActivities(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);

        return list.size() > 0;
    }

    public static boolean hasIntentActivities(Context context, String action, Uri uri) {
        Intent intent = new Intent(action, uri);
        return hasIntentActivities(context, intent);
    }

    public static boolean hasIntentActivities(Context context, String action, String type) {
        Intent intent = new Intent(action);
        // 无法确认发送类型
        // intent.setType("*/*");
        // 纯文本发送
        // intent.setType("plain/text");
        // 有附件发送
        // intent.setType("application/octet-stream");
        intent.setType(type);

        return hasIntentActivities(context, intent);
    }

    /** 上次浏览的充值记录最新的充值时间 **/
    private static final String LAST_BIGGEST_PAYRECORD_ITEM_TIME = "last_biggest_payrecord_item_time";
    /** 当前浏览的充值记录最新的充值时间 **/
    public static long mCurrentBigItemTime = 0;

    public static void saveCurrentBigItemTime(Context context) {
        if (mCurrentBigItemTime > 0 && mCurrentBigItemTime > getLastBigItemTime(context)) {
            SharedPreferences spre = PreferenceManager
                    .getDefaultSharedPreferences(context);
            Editor editor = spre.edit();
            editor.putLong(LAST_BIGGEST_PAYRECORD_ITEM_TIME, mCurrentBigItemTime);
            editor.commit();
        }
    }

    public static long getLastBigItemTime(Context context) {
        SharedPreferences spre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return spre.getLong(LAST_BIGGEST_PAYRECORD_ITEM_TIME, 0);
    }

    public static boolean isMobileWap(Context context) {
        int netType = getNetType(context);
        return NET_TYPE_2GWAP == netType || NET_TYPE_3GWAP == netType;
    }


    public static SpannableString formatSpannableString(String formatSubStr, String allStr,
                                                        int colorValue, ClickableSpan onClick) {
        if (TextUtils.isEmpty(formatSubStr) || TextUtils.isEmpty(allStr)) {
            return null;
        }

        SpannableString spanString = new SpannableString(allStr);
        int start = allStr.indexOf(formatSubStr, 0);
        if (start != -1) {
            int end = start + formatSubStr.length();
            spanString.setSpan(onClick, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new ForegroundColorSpan(colorValue), start, end,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            spanString = null;
        }

        return spanString;
    }

    public static String getImei(Context context) {
        String imei = null;
        if (context != null) {
            TelephonyManager telManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            try{
                
                if (telManager != null) {
                    imei = telManager.getDeviceId();
                }
            }catch(Exception e){
                
            }
        }

        Log.d(TAG, "imei="+imei);
        return imei;
    }

    public static String getSerialNo() {
        String sNo = null;
        try
        {
            Class<?> localClass = Class.forName("android.os.SystemProperties");
            Method localMethod = localClass.getMethod("get", new Class[] {
                String.class
            });
            sNo = (String) localMethod.invoke(localClass, new Object[] {
                "ro.serialno"
            });
        } catch (Exception localException)
        {
            sNo = "";
        }
        return sNo;
    }

    public static String getAndroidId(Context context) {
        String androidId = Settings.System.getString(context.getContentResolver(), "android_id");
        return androidId;
    }

//    public static String getDeviceInfo(Context context) {
//        JSONObject deviceInfo = new JSONObject();
//        String os = AndroidSystemPropertyWrap.get("ro.build.uiversion");
//        String brand = Build.BRAND;
//        String model = Build.MODEL;
//        String m2 = MD5Util.md5LowerCase(getDeviceId(context) + getAndroidId(context)
//                + getSerialNo());
//        Log.d(TAG, "m2="+ m2);
//        try {
//
//            deviceInfo.put("os_version", os);
//
//            deviceInfo.put("device_brand", brand);
//
//            deviceInfo.put("device_model", model);
//
//            deviceInfo.put("device_id", m2);
//
//        } catch (JSONException e) {
//            Log.w(TAG, e.toString());
//        }
//
//        String info = deviceInfo.toString();
//        Log.d(TAG, "deviceInfo="+ info);
//        return RSA.getInstance().getEncryptChars(info);
//    }

    public static String getM2(Context context) {
        try {
            String m2 = MD5Util.md5LowerCase(getDeviceId(context) + getAndroidId(context) + getSerialNo());
            Log.d(TAG, "m2="+ m2);
            return m2;
        } catch (Throwable tr) {
            Log.e(TAG, "getM2 error: ", tr);
            tr.printStackTrace();
        }
        return "";
    }

    /**
     * 支付密码相关     部分打点限制
     * @param context
     * @return
     */
    public static boolean isStat(Context context) {
//        boolean isStat = false;
//        String imei = getImei(context);
//        if (!TextUtils.isEmpty(imei)) {
//            int size = imei.length();
//            String sub = imei.substring(size - 2, size);
//            Log.d(TAG, "isStat.sub:" + sub);
//            if (sub.equals("36")) {
//                isStat = true;
//            }
//        }
//        Log.d(TAG, "isStat:" + isStat);
        //1.1.2 修改为全部打点
        return true;
    }

    // getDeviceId or wifiMac Address
    public static String getDeviceId(Context context) {
        String deviceId = null;
        if (context != null) {
            deviceId = getImei(context);

            if (TextUtils.isEmpty(deviceId)) {
                deviceId = getWifiMac(context);
            }
        }

        deviceId = TextUtils.isEmpty(deviceId) ? "DEFAULT": deviceId;

        Log.d(TAG, "deviceId="+deviceId);
        return deviceId;
    }

    public static String getImsi(Context context) {
        String imsi = null;
        if (context != null) {
            TelephonyManager telManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            try{
                if (telManager != null) {
                    imsi = telManager.getSubscriberId();
                }
                
            }catch (Exception e){
                
            }
        }

        Log.d(TAG, "imsi="+ imsi);
        return imsi;
    }

    public static String getWifiMac(Context context) {
        String macAddr = null;
        if (context != null) {
            try {
                WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (wifiMgr != null) {
                    WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                    if (wifiInfo != null) {
                        macAddr = wifiInfo.getMacAddress();
                    }
                }
            } catch (Exception e) {
            } catch (Error e) {
            }
        }

        Log.d(TAG, "macAddr="+ macAddr);
        return macAddr;
    }

    // 使用null标记，表示尚未得到返回值。
    private static String sLocalPhoneNumber = null;

    public static String getLocalPhoneNumber(Context context) {
        if (sLocalPhoneNumber != null) {
            Log.d(TAG, "memory sLocalPhoneNumber="+ sLocalPhoneNumber);
            if (TextUtils.isEmpty(sLocalPhoneNumber)) {
                return null;
            }
            return sLocalPhoneNumber;
        }

        if (context == null) {
            return null;
        }

        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return null;
        }

        new Thread() {

            @Override
            public void run() {
                String localPhone = "";
                try{
                     localPhone = convertToPhoneNumber(tm.getLine1Number());
                }catch(Exception e){
                    
                }
                if (!TextUtils.isEmpty(localPhone)) {
                    sLocalPhoneNumber = localPhone;
                } else {
                    sLocalPhoneNumber = "";
                }
            };

        }.start();

        long count = 1;
        while (sLocalPhoneNumber == null && count < 5) {
            try {
                Thread.sleep(50L * count);
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }

            count++;
        }
        Log.d(TAG, "count=" +count);
        Log.d(TAG, "first sLocalPhoneNumber="+sLocalPhoneNumber);
        return sLocalPhoneNumber;
    }

    public static String convertToPhoneNumber(String src) {
        if (src == null) {
            return null;
        }
        src.trim();
        if (!isNumeric(src)) {
            StringBuilder sb = new StringBuilder();
            for (int i = src.length(); --i >= 0;) {
                char ch = src.charAt(i);
                if (Character.isDigit(ch)) {
                    sb.insert(0, ch);
                }
            }
            src = sb.toString();
        }
        if (src.length() < 11) {
            return null;
        } else {
            return src.substring(src.length() - 11);
        }
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 英文长度为1 中文为2
     *
     * @param s
     * @return
     */
    public static int getCharLength(String s) {
        if (!TextUtils.isEmpty(s)) {
            s = s.replaceAll("[^\\x00-\\xff]", "**");
            return s.length();
        }

        return 0;
    }

    /**
     * 获取文件后缀名
     *
     * @param filename
     * @return 不含"."的小写后缀名，如：png 如果filename为null或空，返回null
     */
    public static String getFileSuffix(String filename) {
        if (TextUtils.isEmpty(filename)) {
            return null;
        }

        int idx = filename.lastIndexOf(".") + 1;
        if (idx <= 0 || idx == filename.length()) {
            return null;
        }

        return filename.substring(idx).toLowerCase();
    }

    public static int getInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getCompressedBitmap(String filePath, int w, int h) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = getInSampleSize(options, w, h);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap getZoomBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float sx = ((float) w) / width;
        float sy = ((float) h) / height;
        if (sx < sy) {
            sx = sy;
        } else {
            sy = sx;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        /*
         * Bitmap scaledBtm = Bitmap.createBitmap(bitmap, 0, 0, width, height,
         * matrix, true); if (!bitmap.isRecycled()) { bitmap.recycle(); bitmap =
         * null; } System.gc();
         */
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap getZoomBitmap(File resFile, int w, int h) {
        if (!resFile.isFile() || !resFile.exists()) {
            return null;
        }

        InputStream is = null;
        try {
            is = new FileInputStream(resFile);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        return getZoomBitmap(is, w, h);
    }

    public static Bitmap getZoomBitmap(InputStream is, int w, int h) {
        if (is == null) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }

        return getZoomBitmap(bitmap, w, h);
    }

    public static boolean isExternalStorageAvailable(long size) {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File externalStorage = Environment.getExternalStorageDirectory();

            long freeSpace = getFreeSpace(externalStorage.getPath());

            if (freeSpace > size) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInternalStorageAvailable(Context context, long size) {
        File internalStorage = context.getFilesDir();
        long freeSpace = getFreeSpace(internalStorage.getPath());

        if (freeSpace > size) {
            return true;
        }

        return false;
    }

    public static long getFreeSpace(String dir) {

        StatFs statFs = new StatFs(dir);

        // 获取block的SIZE
        long blockSize = statFs.getBlockSize();

        // 可使用的Block的数量
        long availableBlock = statFs.getAvailableBlocks();

        return (availableBlock * blockSize);
    }

    public static boolean isAppRunning(Context context) {
        if (context == null) {
            return false;
        }

        String myPkgName = context.getPackageName();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
        RunningTaskInfo taskInfo = runningTasks.get(0);
        ComponentName component = taskInfo.topActivity;
        String topPkgName = component.getPackageName();


        if (topPkgName.contains(myPkgName)) {
            return true;
        }

        return false;
    }

    /**
     * 是否移动sim卡
     *
     * @param context
     * @return
     */
    public static boolean isMobileCMCC(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();
        if (TelephonyManager.SIM_STATE_READY != simState) {
            return false;
        }

        String operator = telephonyManager.getSimOperator();
        if (!TextUtils.isEmpty(operator) &&
                (operator.equals(CHINA_MOBILE_46000)
                        || operator.equals(CHINA_MOBILE_46002)
                        || operator.equals(CHINA_MOBILE_46007))) {
            return true;
        }

        String subscribeId = telephonyManager.getSubscriberId();
        if (!TextUtils.isEmpty(subscribeId)
                && (subscribeId.startsWith(CHINA_MOBILE_46000)
                        || subscribeId.startsWith(CHINA_MOBILE_46002) || subscribeId
                            .startsWith(CHINA_MOBILE_46007))) {
            return true;
        }

        return false;
    }

    public static boolean isAirModeOn(Context context) {
        if (context == null) {
            return false;
        }

        ContentResolver cr = context.getContentResolver();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(cr, Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        }

        return Settings.Global.getInt(cr, Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }


    public static String optStringArray(int index, String[] array) {
        if (array != null) {
            return (array.length > index && index >= 0) ? array[index] : null;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T optArray(int index, Object[] array, Class<T> c) {
        if (array == null || array.length <= index || index < 0) {
            return null;
        }

        Object o = array[index];
        if (o != null && c.isInstance(o)) {
            return (T) o;
        }

        return null;
    }


    private static Random sRand = new Random(System.currentTimeMillis());

    public static String getRandom() {

        String s = generateRandomString();
        if (s != null && s.length() > 10) {
            s = s.substring(0, 10);
        }

        if (s == null) {
            long num = (sRand.nextLong() % 10000000000L + 10000000000L) % 10000000000L;
            s = String.valueOf(num);
        }

        // 补零
        for (int i = 0; i < 10 - s.length(); i++) {
            s = "0" + s;
        }

        return s;
    }

    public static String generateDesKey() {
        String s = generateRandomString();
        if (s != null && s.length() > 8) {
            s = s.substring(0, 8);
        }

        if (s == null) {
            long num = (sRand.nextLong() % 100000000L + 100000000L) % 100000000L;
            s = String.valueOf(num);
        }

        // 补零
        for (int i = 0; i < 8 - s.length(); i++) {
            s = "0" + s;
        }


        return s;
    }

    private static String generateRandomString() {
        SecureRandom sr;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] bytes = new byte[16];
            sr.nextBytes(bytes);

            return toHexString(bytes);

        } catch (NoSuchAlgorithmException e) {
        }

        return null;
    }


    public static boolean isPkgInstalled(Context context, String pkgName) {
        if (null == context || TextUtils.isEmpty(pkgName)) {
            return false;
        }

        try {
            PackageManager mPm = context.getPackageManager();
            if (mPm == null) {
                return false;
            }
            PackageInfo pkginfo = null;
            try {
                pkginfo = mPm.getPackageInfo(pkgName, 0);
            } catch (NameNotFoundException e) {
            }
            return pkginfo == null ? false : true;
        } catch (Throwable tr) {
            Log.e(TAG, "is app install excepton"+tr.getMessage());
        }
        return false;
    }

    public static boolean isNetAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            NetworkInfo[] networkInfos = connectivity.getAllNetworkInfo();
            if (networkInfos == null) {
                return false;
            }
            for (NetworkInfo networkInfo : networkInfos) {
                if (networkInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }
            return false;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 获取指定包名的签名HASH
     * @param context
     * @param packageName
     * @return
     */
    public static String[] getAllPackageSignatureHash(Context context, String packageName) {
        if (null == context || TextUtils.isEmpty(packageName)) {
            return null;
        }

        String[] ret = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pkgInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

            int sigNum = pkgInfo.signatures.length;
            ret = new String[sigNum];
            for (int i = 0; i < sigNum; i++) {
                X509Certificate cert = (X509Certificate) certFactory
                        .generateCertificate(new ByteArrayInputStream(pkgInfo.signatures[i]
                                .toByteArray()));
                ret[i] = getHash(cert.getPublicKey().getEncoded());
            }

        } catch (Throwable tr) {
            Log.e(TAG, "getPackageSignatureHash error: ", tr);
            tr.printStackTrace();
        }

        return ret;
    }

    /**
     * 检查指定包名的应用签名是否合法
     * @param context
     * @param packageName 包名
     * @param legalSignatureHash 合法的签名hash值
     * @return true, 合法；否则返回false
     */
    public static boolean isPackageSignatureLegal(Context context, String packageName, String legalSignatureHash) {
        if (null == context || TextUtils.isEmpty(packageName) || TextUtils.isEmpty(legalSignatureHash)) {
            return false;
        }
        String[] signatures = getAllPackageSignatureHash(context, packageName);
        if (null == signatures || signatures.length <= 0) {
            return false;
        }

        for (String sig : signatures) {
            if (legalSignatureHash.equalsIgnoreCase(sig)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isWifiAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            NetworkInfo wifi =connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if(wifi.isConnectedOrConnecting()){
                return true;
            }else{
                return false;
            }
        } catch (Throwable e) {
            return false;
        }
    }
    public static String getLocalIpAddress() {
        try {  
            String ipv4;
            List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni: nilist)
            {  
                List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                for (InetAddress address: ialist){
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address)
                    {   
                        return address.getHostAddress();
                    }  
                }  
   
            }  
   
        } catch (SocketException ex) {
            Log.d(TAG, ex.toString());
        }  
        return null;  
    }
    public static String getLocalMacAddressFromIp(Context context) {
        String mac_s= "";
       try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            mac_s = byte2hex(mac);
       } catch (Exception e) {
           Log.d(TAG, e.toString());
       }
       
        return mac_s;
    }
    
    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
         stmp = Integer.toHexString(b[n] & 0xFF);
         if (stmp.length() == 1)
          hs = hs.append("0").append(stmp);
         else {
          hs = hs.append(stmp);
         }
        }
        return String.valueOf(hs);
       }
    
    public static String getWifiIpAddress(final Context context) {
        try {
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        final int ipAddress = wifiInfo.getIpAddress();
            return InetAddress.getByName(String.format("%d.%d.%d.%d", (ipAddress & 0xff), ((ipAddress >> 8) & 0xff), ((ipAddress >> 16) & 0xff), ((ipAddress >> 24) & 0xff))).getHostAddress();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    private static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    
    public static class UrlNameValuePair {
        private String name;

        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public UrlNameValuePair(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return TextUtils.isEmpty(value) ? name : name + value;
            // return name + value;
        }
    }
    
    

}
