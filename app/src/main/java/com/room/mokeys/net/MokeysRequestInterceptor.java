package com.room.mokeys.net;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.droidlover.xdroidmvp.log.XLog;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by wanglei on 2017/1/7.
 */

public class MokeysRequestInterceptor implements Interceptor {
    public static final String TAG = "XDroid_Net";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request;
        request = originalRequest.newBuilder().addHeader("host","www.mokeys.cn").build();
//        String sign ="";
//        String url = originalRequest.url().toString();
//        Map<String,String> urlMap = getUrlParams(url.substring(url.indexOf("?")+1,url.length()));
//        String autoCookie = (String)SPUtil.get(App.getContext(),Constants.ZYGG_USER_COOKIE,"");
//        Log.d("yhh","autoCookie = " +autoCookie);
//        if(!TextUtils.isEmpty(autoCookie)){
//            urlMap.put("authCookie",autoCookie);
//        }
//        try {
//            sign = SignUtils.sign(urlMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.d("yhh","sign = " +sign);
//        if (TextUtils.isEmpty(sign)) {
//            request = originalRequest;
//        }else {
//            HttpUrl modifiedUrl ;
//            if(TextUtils.isEmpty(autoCookie)){
//                 modifiedUrl = originalRequest.url().newBuilder()
//                        // Provide your custom parameter here
//                        .addQueryParameter("sign", sign)
//                        .addQueryParameter("sign_type", "RSA")
//                        .build();
//            }else {
//                 modifiedUrl = originalRequest.url().newBuilder()
//                        // Provide your custom parameter here
//                        .addQueryParameter("authCookie", autoCookie)
//                        .addQueryParameter("sign", sign)
//                        .addQueryParameter("sign_type", "RSA")
//                        .build();
//            }
//            request = originalRequest.newBuilder().url(modifiedUrl).build();
//        }
        Response response = chain.proceed(request);
        return response;
    }
    /**
     * 将map转换成url
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = (String)map.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
    /**
     * 将url参数转换成map
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, String> getUrlParams(String param) {
        Map<String, String> map = new HashMap<String, String>(0);
        if (TextUtils.isEmpty(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        Log.d("yhh","map = "+map.toString());
        return map;
    }

    private void logRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();
            Set<String> mPar = request.url().queryParameterNames();
            XLog.d(TAG, "url : " + url);
            XLog.d(TAG, "method : " + request.method());
            if (headers != null && headers.size() > 0) {
                XLog.e(TAG, "headers : " + headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        XLog.d(TAG, "params : " + bodyToString(request));
                    } else {
                        XLog.d(TAG, "params : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private boolean isText(MediaType mediaType) {
        if (mediaType == null) return false;

        return ("text".equals(mediaType.subtype())
                || "json".equals(mediaType.subtype())
                || "xml".equals(mediaType.subtype())
                || "html".equals(mediaType.subtype())
                || "webviewhtml".equals(mediaType.subtype())
                || "x-www-form-urlencoded".equals(mediaType.subtype()));
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
