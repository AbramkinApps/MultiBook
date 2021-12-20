package com.abramkin.multibook;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoChoiceScreenActivity extends ChoiceClass {

    FloatingActionButton newPhoto;

    int TAKE_PICTURE_REQUEST = 1;

    ListView lv;
    static Uri imageUri;

    static String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_choice_screen);

        lv = (ListView) findViewById(R.id.listViewPhoto);

        newPhoto = (FloatingActionButton) findViewById(R.id.newPhotoButton);
        newPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (StartScreenActivity.checkDirectory(getExternalFilesDir(null) + "/MultiBook/" + "Photos")) {

            showFiles(this, false, true, getExternalFilesDir(null) + "/MultiBook/" + "Photos", lv);

            // Animation anim_lv = AnimationUtils.loadAnimation(this, R.anim.trans_lv);
            // lv.startAnimation(anim_lv);

            Animation anim_fab = AnimationUtils.loadAnimation(this, R.anim.fab);
            newPhoto.startAnimation(anim_fab);

        } else {

            startActivity();
        }

    }

    public void startActivity() {

        startActivityForResult(PhotoChoiceScreenActivity.getCameraIntent(PhotoChoiceScreenActivity.this, getExternalFilesDir(null) + "/MultiBook/" + "Photos"), TAKE_PICTURE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {


            try {


                File fa = new File(getExternalFilesDir(null) + "/MultiBook/" + "Photos", filename);

                Bitmap bitmap = BitmapFactory.decodeFile(fa.getPath());

                FileOutputStream out = new FileOutputStream(fa);

                Matrix matrix = new Matrix();

                matrix.postRotate(90);

                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                display.getMetrics(metrics);

                Bitmap scaled = Bitmap.createScaledBitmap(rotatedBitmap, metrics.widthPixels, metrics.heightPixels, true);

                scaled.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public static Intent getCameraIntent(Context context, String path) {


        File root = new File(path);
        if (!root.exists()) {
            root.mkdirs();
        }
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-MM-yyyy HH-mm-ss");

        filename = dateFormat.format(date).toString() + ".jpg";
        File photoFile = new File(path, filename);

        imageUri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider", //(use your app signature + ".provider" )
                photoFile);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        return intent;

    }


}
