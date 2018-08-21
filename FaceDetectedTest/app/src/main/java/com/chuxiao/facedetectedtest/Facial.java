package com.chuxiao.facedetectedtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class Facial {

    private static final String LOG_TAG = Facial.class.getSimpleName();

    static void detectFaces(Context context, Bitmap bitmap) {
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_LANDMARKS)
                .build();

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        // 发现人脸
        SparseArray<Face> faceSparseArray = detector.detect(frame);

        Toast.makeText(context, "发现人脸数：" + faceSparseArray.size(), Toast.LENGTH_SHORT).show();

        detector.release();
    }

}
