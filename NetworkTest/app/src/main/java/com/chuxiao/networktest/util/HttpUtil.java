package com.chuxiao.networktest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 12525 on 2018/5/4.
 */

public class HttpUtil {

    /**
     * 发送OkHttp网络请求
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        // enqueue方法内部已开启子线程
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 发送HttpURLConnection网络请求
     */
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getErrorStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
//                    return response.toString();
                    if (listener != null) {
                        // 回调OnFinish方法
                        listener.onFinish(response.toString());
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
//                    return e.getMessage();
                    if (listener != null) {
                        // 回调onError方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

}
