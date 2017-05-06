package com.abramkin.multibook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PictureScreenActivity extends AppCompatActivity {

    String path = Environment.getExternalStorageDirectory().toString()+"/MultiBook/Pictures/";
    MySurfaceView msf;
    Bitmap bmp;
    Canvas imCanvas;
    boolean notEmpty = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        msf = new MySurfaceView(this);
        msf.setZOrderOnTop(true);
        msf.setBackgroundColor(Color.parseColor("#FFDF9C"));

        SurfaceHolder sfh = msf.getHolder();
        sfh.setFormat(PixelFormat.TRANSPARENT);

        setContentView(msf);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        bmp = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.RGB_565);
        bmp.eraseColor(Color.parseColor("#FFDF9C"));

        imCanvas = new Canvas(bmp);
    }

    @Override
    public void onBackPressed() {

        if (notEmpty) {

            AlertDialog.Builder ad = new AlertDialog.Builder(PictureScreenActivity.this);

            ad.setTitle(R.string.title_alert_save);

            ad.setPositiveButton(R.string.button_alert_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    try {

                        File root = new File(path);
                        if (!root.exists()) {
                            root.mkdirs();
                        }
                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("d-MM-yyyy HH-mm-ss");

                        String filename = dateFormat.format(date).toString() + ".jpg";
                        File pictureFile = new File(path, filename);
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    PictureScreenActivity.this.finish();

                    Toast.makeText(PictureScreenActivity.this, R.string.message_saved, Toast.LENGTH_SHORT).show();
                }
            });
            ad.setNegativeButton(R.string.button_alert_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    PictureScreenActivity.this.finish();

                }
            });

            ad.setCancelable(true);
            ad.create();
            ad.show();
        }
        else PictureScreenActivity.this.finish();
    }

    class MySurfaceView extends SurfaceView {

        Path path;
        Canvas canvas;
        SurfaceHolder surfaceHolder;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        public MySurfaceView(Context context) {
            super(context);

            surfaceHolder = getHolder();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            paint.setColor(Color.parseColor("#FFB77D49"));
            path = new Path();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            notEmpty = true;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                path.moveTo(event.getX(), event.getY());
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                path.lineTo(event.getX(), event.getY());
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                path.lineTo(event.getX(), event.getY());
            }

            if (path != null) {

                canvas = surfaceHolder.lockCanvas();
                canvas.drawPath(path, paint);
                imCanvas.drawPath(path, paint);

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
            return true;
        }
    }
}
