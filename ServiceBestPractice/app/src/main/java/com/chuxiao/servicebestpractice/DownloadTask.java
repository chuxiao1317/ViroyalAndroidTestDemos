package com.chuxiao.servicebestpractice;

import android.os.AsyncTask;
import android.os.Environment;

import com.chuxiao.servicebestpractice.interfaces.DownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * AsyncTask：为了在子线程中更方便地对UI进行操作；其原理就是封装了异步消息处理机制
 */
public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    private static final int TYPE_SUCCESS = 0;
    private static final int TYPE_FAILED = 1;
    private static final int TYPE_PAUSED = 2;
    private static final int TYPE_CANCELED = 3;

    private DownloadListener listener;

    private boolean isCanceled = false;

    private boolean isPaused = false;

    private int lastProgress;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    /**
     * 此方法中的所有代码都会在子线程中运行，应在此处处理所有耗时任务，但不可以进行UI操作
     */
    @Override
    protected Integer doInBackground(String... params) {
        InputStream inputStream = null;
        /**
         * RandomAccessFile类不算是IO体系中的子类。
         * 而是直接继承自Object
         * 但是它是IO包中的成员，因为它具备读和写的功能。
         * 能完成读写的原理是内部封装了字节输入流和输出流。
         * 而且内部还封装了一个数组，通过指针对数组的元素进行操作。
         * 可通过getFilePointer获取指针位置。
         * 也可通过seek改变指针位置。
         * 通过构造函数可以看出，该类只能操作文件。
         * 操作文件有4种模式："r"、"rw"、"rws" 或 "rwd"
         */
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            // 记录已下载的文件长度
            long downloadedLength = 0;
            // 获取下载URL地址
            String downloadUrl = params[0];
            // 根据URL地址解析出文件名
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            //指定文件下载之后的保存地址
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            // 判断要下载的文件是否已存在
            if (file.exists()) {
                // 已存在，读取已下载的字节数，方面后面断点续传
                downloadedLength = file.length();
            }
            // 获取待下载文件的总长度
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
                // 已下载字节和文件总字节相等，说明已经下载完成，直接返回
                return TYPE_SUCCESS;
            }
            OkHttpClient okHttpClient = new OkHttpClient();
            // 发送OkHtp网络请求
            Request request = new Request.Builder()
                    // 断点下载，指定从哪个字节开始下载
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            // 接收响应数据
            Response response = okHttpClient.newCall(request).execute();
            // 如果响应不为空则准备开始下载工作
            if (response != null) {
                // 获取响应数据中的字节流
                inputStream = response.body().byteStream();
                //rw(读写)模式创建文件：如果文件不存在则创建文件，反之则不创建，若r(只读)则只会读取已存在的文件，若文件不存在则抛异常
                savedFile = new RandomAccessFile(file, "rw");
                // 设置文件指针偏移量（指针），跳过已下载的字节，读写文件时从此处开始
                savedFile.seek(downloadedLength);
                // 设定每次读取的字节数，每次读取这么多存入缓冲区
                byte[] b = new byte[1024];
                // 统计已下载的字节数
                int total = 0;
                // 用来读取缓冲区中的字节总数
                int len;
                // 读取一些字节存入缓冲区数组b中，将缓冲区的字节总数返回给len，读写文件的同时需要判断用户是否进行暂停或取消的操作，以便及时做出相应的响应
                while ((len = inputStream.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        //在下载过程中用户无操作，可正常下载
                        total += len;
                        // 将将缓冲区b中的io流写入文件，0为开始写入的偏移量，len为缓冲区中的字节总数
                        savedFile.write(b, 0, len);
                        // 计算已下载的百分比
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        //反馈当前任务进行的执行进度
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    /**
     * 当后台任务中调用了publishProgress(progress...)方法后，很快就会调用此方法，此处可进行UI操作，如：更新下载进度
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    /**
     * 当后台任务执行完毕并return之后，调用此方法，返回数据作为参数传入，可进行UI操作，如：通知下载结果
     */
    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }

    /**
     * 控制是否暂停下载
     */
    public void pauseDownload() {
        isPaused = true;
    }

    /**
     * 控制是否取消下载
     */
    public void cancelDownload() {
        isCanceled = true;
    }

    /**
     * 获取待下载文件的总长度
     */
    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }

}
