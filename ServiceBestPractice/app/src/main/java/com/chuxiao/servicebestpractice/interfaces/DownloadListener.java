package com.chuxiao.servicebestpractice.interfaces;

/**
 * Created by 12525 on 2018/5/8.
 */

public interface DownloadListener {

    /**
     * 显示下载进度
     */
    void onProgress(int progress);

    /**
     * 下载成功
     */
    void onSuccess();

    /**
     * 下载失败
     */
    void onFailed();

    /**
     * 暂停下载
     */
    void onPaused();

    /**
     * 取消下载
     */
    void onCanceled();

}
