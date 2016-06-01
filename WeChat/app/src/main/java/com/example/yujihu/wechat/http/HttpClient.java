package com.example.yujihu.wechat.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.yujihu.wechat.R;
import com.example.yujihu.wechat.common.AppContext;
import com.example.yujihu.wechat.entity.LoginParam;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yujiu on 16/6/1.
 */
public class HttpClient {

    private static final int CONNECT_TIME_OUT = 10;
    private static final int WRITE_TIME_OUT = 60;
    private static final int READ_TIME_OUT = 60;
    private static final int MAX_REQUESTS_PER_HOST = 10;
    private static final String TAG = HttpClient.class.getSimpleName();
    private static final String UTF_8 = "UTF-8";
    //private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain; charset=utf-8");
    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new LoggingInterceptor());
        client = builder.build();
        client.dispatcher().setMaxRequestsPerHost(MAX_REQUESTS_PER_HOST);
    }

    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.i(TAG, String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.i(TAG, String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }

    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        } catch (Exception e) {
            Log.v("ConnectivityManager", e.getMessage());
        }
        return false;
    }

    public static void get(String url, Map<String, String> param, final HttpResponseHandler httpResponseHandler) {
        if (!isNetworkAvailable()) {
            Toast.makeText(AppContext.getInstance(), R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        if (param != null && param.size() > 0) {
            url = url + "?" + mapToQueryString(param);
        }
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                httpResponseHandler.sendSuccessMessage(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                httpResponseHandler.sendFailureMessage(call.request(), e);
            }
        });
    }

    public static void post(String url, Map<String, String> param, final HttpResponseHandler handler) {
        if (!isNetworkAvailable()) {
            Toast.makeText(AppContext.getInstance(), R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        //if (param != null && param.size() > 0) {
            FormBody.Builder builder = new FormBody.Builder();
            try {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    builder.add(entry.getKey(), URLEncoder.encode(entry.getValue(), UTF_8));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        //}

        Request request = new Request.Builder().url(url).post(builder.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handler.sendSuccessMessage(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendFailureMessage(call.request(), e);
            }
        });
    }

    public static String mapToQueryString(Map<String, String> map) {
        StringBuilder string = new StringBuilder();
        /*if(map.size() > 0) {
            string.append("?");
        }*/
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                string.append(entry.getKey());
                string.append("=");
                string.append(URLEncoder.encode(entry.getValue(), UTF_8));
                string.append("&");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string.toString();
    }


    //*************************get方式登录************************************//

    private static final String GET_LOGIN = "http:///myqq/login2.php";

    public static void getLogin(String username, String password, HttpResponseHandler httpResponseHandler) {

        Map<String, String> rq = new HashMap();
        rq.put("user_name", username);
        rq.put("user_pwd", password);

//        String url = HTTP_DOMAIN + "?" + URLEncodedUtils.format(rq, UTF_8);
        get(GET_LOGIN, rq, httpResponseHandler);
    }
    //*************************************************************//

    //*************************get方式注册************************************//
    private static final String GET_REGISTER = "http:///myqq/register2.php";

    public static void getRegister(String username, String password, HttpResponseHandler httpResponseHandler) {

        Map<String, String> rq = new HashMap();
        rq.put("user_name", username);
        rq.put("user_pwd", password);

//        String url = HTTP_DOMAIN + "?" + URLEncodedUtils.format(rq, UTF_8);
        get(GET_REGISTER, rq, httpResponseHandler);
    }
    //*************************************************************//

    //*************************post方式登录************************************//

    private static final String POST_LOGIN = "http:///myqq/login.php";

    public static void postLogin(String username, String password, HttpResponseHandler httpResponseHandler) {

        Map<String, String> rq = new HashMap();
        rq.put("user_name", username);
        rq.put("user_pwd", password);

//        String url = HTTP_DOMAIN + "?" + URLEncodedUtils.format(rq, UTF_8);
        post(POST_LOGIN, rq, httpResponseHandler);
    }
    //*************************************************************//

    //*************************post方式注册************************************//
    private static final String POST_REGISTER = "http:///myqq/register.php";

    public static void postRegister(String username, String password, HttpResponseHandler httpResponseHandler) {

        Map<String, String> rq = new HashMap();
        rq.put("user_name", username);
        rq.put("user_pwd", password);

//        String url = HTTP_DOMAIN + "?" + URLEncodedUtils.format(rq, UTF_8);
        post(POST_REGISTER, rq, httpResponseHandler);
    }
    //*************************************************************//
}
