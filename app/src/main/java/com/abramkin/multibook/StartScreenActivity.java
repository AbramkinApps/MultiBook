package com.abramkin.multibook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class StartScreenActivity extends AppCompatActivity implements View.OnTouchListener {

    ImageButton imText, imPhoto, imVoice, imPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        imText = findViewById(R.id.imageText);
        imPhoto = findViewById(R.id.imagePhoto);
        imVoice = findViewById(R.id.imageVoice);
        imPicture = findViewById(R.id.imagePicture);

        imText.setOnTouchListener(this);
        imPhoto.setOnTouchListener(this);
        imVoice.setOnTouchListener(this);
        imPicture.setOnTouchListener(this);


        RequestUserPermission requestUserPermission = new RequestUserPermission(this);
        requestUserPermission.verifyStoragePermissions();


    }

    @Override
    protected void onResume() {
        super.onResume();

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.trans);
        imText.startAnimation(animation);

        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.trans2);
        imPhoto.startAnimation(animation2);

        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.trans4);
        imVoice.startAnimation(animation3);

        Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.trans3);
        imPicture.startAnimation(animation4);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        ImageButton imb = (ImageButton) v;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            imb.setBackgroundColor(Color.parseColor("#F4C76C"));
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

            imb.setBackgroundColor(Color.parseColor("#FFECC6"));

            switch (v.getId()) {
                case R.id.imageText:

                    if (checkDirectory(getExternalFilesDir(null) + "/MultiBook/" + "Texts")) {

                        startNewActivity(this, TextChoiceScreenActivity.class);

                    } else {

                        startNewActivity(this, TextScreenActivity.class);
                    }
                    break;

                case R.id.imagePhoto:

                    startNewActivity(this, PhotoChoiceScreenActivity.class);

                    break;

                case R.id.imageVoice:

                    if (checkDirectory(getExternalFilesDir(null) + "/MultiBook/" + "Voices")) {

                        startNewActivity(this, VoiceChoiceScreenActivity.class);

                    } else {

                        startNewActivity(this, VoiceScreenActivity.class);
                    }
                    break;

                case R.id.imagePicture:

                    if (checkDirectory(getExternalFilesDir(null).toString() + "/MultiBook/" + "Pictures")) {

                        startNewActivity(this, PictureChoiceScreenActivity.class);

                    } else {

                        startNewActivity(this, PictureScreenActivity.class);
                    }
                    break;

            }
        }
        return true;
    }

    public void startNewActivity(Context cntx, Class<?> cl) {

        Intent intent;
        intent = new Intent(cntx, cl);
        startActivity(intent);
    }

    static Boolean checkDirectory(String path) {

        File root;
        File[] filesArray;

        root = new File(path);

        if (!root.exists()) {
            root.mkdirs();
        }

        filesArray = root.listFiles();


        return ((filesArray != null) && (filesArray.length != 0));

    }
}
