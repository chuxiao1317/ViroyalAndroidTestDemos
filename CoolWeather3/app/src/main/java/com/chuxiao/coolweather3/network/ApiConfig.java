package com.chuxiao.coolweather3.network;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.viroyal.sloth.util.FileUtils;
import com.viroyal.sloth.util.SPUtils;
import com.viroyal.sloth.util.Slog;

import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 获取api主地址信息
 * Created by zaiyu on 2016/9/18.
 */
public class ApiConfig {
    private static final String TAG  = ApiConfig.class.getSimpleName();

    private static final String API_IP_CONFIG_KEY = "api_ip_config";

    public static  class ApiIpConfig {
        @SerializedName("app_ips")
        private ArrayList<String> appIps;
        @SerializedName("file_ips")
        private ArrayList<String> fileIps;
    }

    private ApiIpConfig mApiIpConfig;

    private Context mContext;

    private static ApiConfig sInstance;

    private ApiConfig() {
    }

    public static synchronized ApiConfig getInstance() {
        if (sInstance  == null) {
            sInstance = new ApiConfig();
        }
        return sInstance;
    }

    /**
     * 初始化接口ip地址，帮忙信息等的url
     * @param context context
     */
    public void init(Context context) {
        mContext = context;

        SPUtils spUtils = SPUtils.getInstance(context);
        String config = spUtils.get(API_IP_CONFIG_KEY);
        if (TextUtils.isEmpty(config)) {
            try {
                Slog.d(TAG, "from json");
                InputStreamReader ir = new InputStreamReader(FileUtils.getAssetsStream(context, "api_config.json"), "utf-8");
                char[] buffer = new char[1024];
                int len = ir.read(buffer);
                ir.close();
                config = new String(buffer, 0, len);
                spUtils.put(API_IP_CONFIG_KEY, config);
            } catch (Exception e) {
                Slog.e(TAG, "error:" + e);
            }
            if (TextUtils.isEmpty(config)) {
                Slog.e(TAG, "panic!! can't get api config");
                return;
            }
        }
        Gson gson = new Gson();
        ApiIpConfig ipConfig = gson.fromJson(config, ApiIpConfig.class);
        if (ipConfig != null) {
            mApiIpConfig = ipConfig;
        }

        Api.get().setBaseApi(mApiIpConfig.appIps.get(0));

//        getApiIpFromNet();
    }

//    private void getApiIpFromNet() {
//        Api.get().getMainApi(MainInterfaceApi.class)
//                .getMainInterface(BuildConfig.MAIN_BASE_URL)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseRspObserver<ApiConfigResponse>(
//                        ApiConfigResponse.class, (rsp) -> {
//                    if (ErrorCode.isSuccess(rsp.error_code) && rsp.apiConfig != null) {
//                        mApiIpConfig = rsp.apiConfig;
//                        Api.getInstance().setBaseApi(mApiIpConfig.appIps.get(0));
//                        //save to share preference
//                        Gson gson = new Gson();
//                        SPUtils.getInstance(mContext).put(API_IP_CONFIG_KEY, gson.toJson(mApiIpConfig));
//                    } else {
//                        Slog.d(TAG, "MAIN interface is out of service!");
//                    }
//                }));
//
//    }
}
