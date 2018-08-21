package com.chuxiao.cameratest;

import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceView mCameraPreview;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private boolean isBackCameraOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initViews();
    }

    private void initViews() {
        mCameraPreview = (SurfaceView) findViewById(R.id.sv_camera);
        mSurfaceHolder = mCameraPreview.getHolder();
        mSurfaceHolder.addCallback(this);
        mCameraPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) && (mCamera == null)) {
            mCamera = getCamera();
            if (mSurfaceHolder != null) {
                setStartPreview(mCamera, mSurfaceHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * 在SurfaceView中预览相机内容
     *
     * @param camera camera
     * @param holder SurfaceHolder
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化相机
     *
     * @return camera
     */
    private Camera getCamera() {
        Camera camera;
        try {
            camera = Camera.open(1);
        } catch (Exception e) {
            camera = null;
        }
        return camera;
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            // 中止回调，防止释放出错
            mCamera.setPreviewCallback(null);
            // 停止捕捉并绘制预览帧到表面，并重新设置相机以备将来调用startPreview
            mCamera.stopPreview();
            // 释放相机资源
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera, mSurfaceHolder);
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                addContentView(new DrawView(CameraActivity.this),
                        new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT)
                );
            }
        });
    }

    /**
     * Surface（格式或尺寸）发生改变时触发，此时需要更新视图
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        if (mSurfaceHolder.getSurface() == null) {
//            return;
//        }
//        try {
//            mCamera.stopPreview();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        setStartPreview(mCamera, mSurfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }
}
