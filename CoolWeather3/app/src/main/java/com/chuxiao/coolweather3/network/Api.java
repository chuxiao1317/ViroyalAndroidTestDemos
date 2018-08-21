// (c)2016 Flipboard Inc, All Rights Reserved.

package com.chuxiao.coolweather3.network;

import android.content.Context;
import android.text.TextUtils;

import com.viroyal.sloth.network.HttpManager;
import com.viroyal.sloth.network.ProgressListener;
import com.viroyal.sloth.util.SPUtils;
import com.viroyal.sloth.util.Slog;

import java.util.HashMap;
import java.util.Map;

import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;

//import com.viroyal.light.account.AccountManager;

public class Api {
    private static final String TAG = "Api";
    private static String BASE_URL = "http://guolin.tech/api/";

    //    private static String BASE_URL = "http://10.161.120.225:8000/";//192.168.1.148
//    private static String BASE_URL = "http://192.168.1.190:8000/";//192.168.1.148
//    private static String BASE_URL = "http://101.132.159.39:8000/";//192.168.1.148
    private static String BASE_IP = "192.168.1.148";
    public static String PRINT_IP = "";
    public static String deviceid = "28:f3:66:f0:62:29".toUpperCase();
    public static Map<String, String> PRINT_IG = null;

    public static int BASE_PORT = 8000;
    private static String MAIN_BASE_URL = "http://www.forexample.com/";


    private HashMap<Class, Object> mApiObjects;
    private HashMap<Class, Object> mMainApiObjects;
    private ApiConfig mApiConfig;
    private static Api sApi;

    public String operator = "";
    public String operatorname = "";
    public String qrcode = "?xf=";

    private Api() {
        mApiObjects = new HashMap<>();
    }

    public synchronized static Api getInstance() {
        if (sApi == null) {
            sApi = new Api();
        }
        return sApi;
    }

    public void init(Context context) {
        String url = SPUtils.getInstance(context).get("baseurl");
        if (null != url && !"".equals(url)) {
            BASE_URL = url;
        }
        String qr = SPUtils.getInstance(context).get("qrcode_url");
        if (null != qr && !"".equals(qr)) {
            qrcode = qr;
        } else {
            qrcode = "xf=";
        }
        mApiConfig = ApiConfig.getInstance();
        mApiConfig.init(context);
    }

    public static Api get() {
        if (sApi == null) {
            Slog.e(TAG, "Api is null !!!");
        }
        return sApi;
    }

    public synchronized void setBaseIp(String ip) {
        BASE_IP = ip;
    }

    public String getBaseIp() {
        return BASE_IP;
    }

    public synchronized void setBaseApi(String url) {
        Slog.d(TAG, "setBaseApi, url=" + url);
        if (!TextUtils.isEmpty(url)) {
            BASE_URL = url;
            if (mApiObjects != null) {
                mApiObjects.clear();
                mMainApiObjects = null;
            }
        } else {
            Slog.e(TAG, "setBaseApi, url=" + "null !!!");
        }
    }

    /**
     * 仅供 获取主接口的时候使用
     *
     * @return
     * @throws
     * @aram a
     */

    public void resetMainApi() {
        mMainApiObjects = null;
    }

    /**
     * 仅供 获取主接口的时候使用
     *
     * @return
     * @throws
     * @aram a
     */
//    public synchronized <T> T getMainApi(Class<T> tClass) {
//        if (mMainApiObjects != null) {
//            if (mMainApiObjects.get(tClass) != null) {
//                return (T) mMainApiObjects.get(tClass);
//            }
//        } else {
//            mMainApiObjects = new HashMap<>();
//        }
//        Retrofit retrofit = new Retrofit.Builder()
//                .client(HttpManager.getWorkHttpClient(Level.BODY ,AccountManager.get().getToken(), AccountManager.get().getUserId()+""))
//                .baseUrl(BASE_URL)
//                .addConverterFactory(HttpManager.sGsonConverterFactory)
//                .addCallAdapterFactory(HttpManager.sRxJavaCallAdapterFactory)
//                .build();
//        mMainApiObjects.put(tClass, retrofit.create(tClass));
//        return (T) mMainApiObjects.get(tClass);
//    }
    public synchronized <T> T getApi(Class<T> tClass) {
        if (mApiObjects != null) {
            if (mApiObjects.get(tClass) != null) {
                return (T) mApiObjects.get(tClass);
            }
        } else {
            mApiObjects = new HashMap<>();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .client(HttpManager.getHttpClient(Level.BODY))
                .baseUrl(BASE_URL)
                .addConverterFactory(HttpManager.sGsonConverterFactory)
                .addCallAdapterFactory(HttpManager.sRxJavaCallAdapterFactory)
                .build();
        mApiObjects.put(tClass, retrofit.create(tClass));
        return (T) mApiObjects.get(tClass);
    }

    //下载相关的API
    public synchronized <T> T getDownloadApi(Class<T> tClass, ProgressListener listener) {
        if (mApiObjects != null) {
            if (mApiObjects.get(tClass) != null) {
                return (T) mApiObjects.get(tClass);
            }
        } else {
            mApiObjects = new HashMap<>();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .client(HttpManager.getHttpDownloadClient(BASE_URL, Level.BODY, listener))
                .baseUrl(BASE_URL)
                .addConverterFactory(HttpManager.sGsonConverterFactory)
                .addCallAdapterFactory(HttpManager.sRxJavaCallAdapterFactory)
                .build();
        mApiObjects.put(tClass, retrofit.create(tClass));
        return (T) mApiObjects.get(tClass);
    }


    public String getInfoUrl(String action) {
        return BASE_URL + "action/" + action + "/info";
    }

    public String getBomUrl() {
        return BASE_URL + "bom";
    }

    public String getValiBomUrl(String id, String product_code) {
        return BASE_URL + "valid/type/" + id + "/" + product_code;
    }

    public String getUsedMatUrl(String code) {
        return BASE_URL + "usedMat/" + code;
    }

    public String getValidataUrl(String action) {
        return BASE_URL + "action/" + action + "/validate";
    }

    public String getSearchUrl(String action) {
        return BASE_URL + "action/" + action;
    }

    public String getDryDeleteUrl(int id) {
        return BASE_URL + "test/4/" + id;
    }

    public String getTestUrl() {
        return BASE_URL + "test/5";
    }

    public String getVersionUrl() {
        return BASE_URL + "version/mobile/0";
    }

    public String getDeleteUrl(String id) {
        return BASE_URL + "record/" + id;
    }

    public String getDevicevaliDataUrl(String action, String code) {
        return BASE_URL + "device/" + action + "/" + code;
    }

    public String getProcessUrl(String action, String code, int status) {
        return BASE_URL + "action/" + code + "/mes/" + action + "/" + status;
    }

    public String getNumberUrl(String action, String code, int status) {
        return BASE_URL + "action/" + code + "/" + action + "/" + status;
    }

    public String getPrintIp() {
        return BASE_URL + "IP";
    }

    public String getPrintIg() {
        return BASE_URL + "print";
    }

    public String getImgUrl(String id) {
        return BASE_URL + "upload/" + id;
    }

}
