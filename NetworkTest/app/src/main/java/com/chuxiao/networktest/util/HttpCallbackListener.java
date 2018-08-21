package com.chuxiao.networktest.util;

/**
 * Created by 12525 on 2018/5/4.
 */


/**
 * HttpURLConnection方式才会调用的自定义接口（okHttp方式中：enqueue方法内部已开启子线程）
 */
public interface HttpCallbackListener {

    /**
     * 服务器成功响应的时候回调
     */
    void onFinish(String response);

    /**
     * 网络操作出现错误的时候回调
     */
    void onError(Exception e);

}
