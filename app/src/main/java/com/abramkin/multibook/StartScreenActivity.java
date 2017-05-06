package com.abramkin.multibook;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class StartScreenActivity extends AppCompatActivity implements View.OnTouchListener {

    ImageButton imText, imPhoto, imVoice, imPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        imText = (ImageButton) findViewById(R.id.imageText);
        imPhoto = (ImageButton) findViewById(R.id.imagePhoto);
        imVoice = (ImageButton) findViewById(R.id.imageVoice);
        imPicture = (ImageButton) findViewById(R.id.imagePicture);

        imText.setOnTouchListener(this);
        imPhoto.setOnTouchListener(this);
        imVoice.setOnTouchListener(this);
        imPicture.setOnTouchListener(this);
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
        Intent intent;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            imb.setBackgroundColor(Color.parseColor("#F4C76C"));
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

            imb.setBackgroundColor(Color.parseColor("#FFECC6"));

            switch (v.getId()) {
                case R.id.imageText:

                    intent = new Intent(StartScreenActivity.this, TextChoiceScreenActivity.class);
                    startActivity(intent);
                    break;

                case R.id.imagePhoto:

                    intent = new Intent(StartScreenActivity.this, PhotoChoiceScreenActivity.class);
                    startActivity(intent);
                    break;

                case R.id.imageVoice:

                    intent = new Intent(StartScreenActivity.this, VoiceChoiceScreenActivity.class);
                    startActivity(intent);
                    break;

                case R.id.imagePicture:

                    intent = new Intent(StartScreenActivity.this, PictureChoiceScreenActivity.class);
                    startActivity(intent);
                    break;

            }
        }
        return true;
    }
}
