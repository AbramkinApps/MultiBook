package com.abramkin.multibook;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PhotoScreenActivity extends AppCompatActivity {

    final int CAMERA_ID = 0;
    final boolean FULL_SCREEN = true;
    String path = Environment.getExternalStorageDirectory().toString()+"/MultiBook/Photos/";
    SurfaceView sv;
    SurfaceHolder holder;
    HolderCallback holderCallback;
    Camera camera;
    float size = 1.3f;
    FloatingActionButton photoButton, Ok, No;
    byte[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_screen);

        photoButton = (FloatingActionButton) findViewById(R.id.photo);

        photoButton.setScaleX(size);
        photoButton.setScaleY(size);


        Ok = (FloatingActionButton) findViewById(R.id.butOK);

        Ok.setScaleX(size);
        Ok.setScaleY(size);

        No = (FloatingActionButton) findViewById(R.id.butNo);

        No.setScaleX(size);
        No.setScaleY(size);




        sv = (SurfaceView) findViewById(R.id.surfaceView);
        holder = sv.getHolder();

        holderCallback = new HolderCallback();
        holder.addCallback(holderCallback);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, new Camera.PictureCallback() {

                    @Override
                    public void onPictureTaken(byte[] newData, Camera camera) {

                        photoButton.setVisibility(View.INVISIBLE);
                        Ok.setVisibility(View.VISIBLE);
                        No.setVisibility(View.VISIBLE);

                        data = newData;

                        Ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    File root = new File(path);
                                    if (!root.exists()) {
                                        root.mkdirs();
                                    }
                                    Date date = new Date();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("d-MM-yyyy HH-mm-ss");

                                    String filename = dateFormat.format(date).toString()+".jpg";

                                    File photoFile = new File(path, filename);

                                    FileOutputStream fos = new FileOutputStream(photoFile);
                                    fos.write(data);
                                    fos.close();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Ok.setVisibility(View.INVISIBLE);
                                No.setVisibility(View.INVISIBLE);
                                photoButton.setVisibility(View.VISIBLE);

                                resetCam();

                            }

                        });

                    }

                });

            }
        });

        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ok.setVisibility(View.INVISIBLE);
                No.setVisibility(View.INVISIBLE);
                photoButton.setVisibility(View.VISIBLE);

                resetCam();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera = Camera.open(CAMERA_ID);
        Camera.Parameters p = camera.getParameters();

        p.set("jpeg-quality", 100);
        p.set("rotation", 90);

        camera.setParameters(p);

        setPreviewSize(FULL_SCREEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null)
            camera.release();
        camera = null;
    }
    private void resetCam() {
        camera.startPreview();
        setPreviewSize(FULL_SCREEN);
    }
    class HolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            camera.stopPreview();
            setCameraDisplayOrientation(CAMERA_ID);
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

    }

    void setPreviewSize(boolean fullScreen) {

        Display display = getWindowManager().getDefaultDisplay();
        Point sizePoint = new Point();
        display.getSize(sizePoint);
        int width = sizePoint.x;
        int height = sizePoint.y;
        boolean widthIsMax = width > height;

        Camera.Size size = camera.getParameters().getPreviewSize();

        RectF rectDisplay = new RectF();
        RectF rectPreview = new RectF();

        rectDisplay.set(0, 0, width, height);

        if (widthIsMax) {

            rectPreview.set(0, 0, size.width, size.height);
        } else {

            rectPreview.set(0, 0, size.height, size.width);
        }

        Matrix matrix = new Matrix();

        if (!fullScreen) {

            matrix.setRectToRect(rectPreview, rectDisplay, Matrix.ScaleToFit.START);
        } else {
            matrix.setRectToRect(rectDisplay, rectPreview, Matrix.ScaleToFit.START);
            matrix.invert(matrix);
        }

        matrix.mapRect(rectPreview);

        sv.getLayoutParams().height = (int) (rectPreview.bottom);
        sv.getLayoutParams().width = (int) (rectPreview.right);
    }

  void setCameraDisplayOrientation(int cameraId) {

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result = 0;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        result = ((360 + degrees) + info.orientation);

        result = result % 360;
        camera.setDisplayOrientation(result);
    }
}



