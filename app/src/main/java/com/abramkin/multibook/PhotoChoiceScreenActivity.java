package com.abramkin.multibook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

public class PhotoChoiceScreenActivity extends ChoiceClass {

    FloatingActionButton newPhoto;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_choice_screen);

        lv = (ListView) findViewById(R.id.listViewPhoto) ;

        newPhoto = (FloatingActionButton) findViewById(R.id.newPhotoButton);
        newPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoChoiceScreenActivity.this, PhotoScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_new,R.anim.alpha);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        showFiles(this, false, true, "Photos/", lv, PhotoScreenActivity.class);

        Animation anim_lv = AnimationUtils.loadAnimation(this, R.anim.trans_lv);
        lv.startAnimation(anim_lv);

        Animation anim_fab = AnimationUtils.loadAnimation(this, R.anim.fab);
        newPhoto.startAnimation(anim_fab);
    }
}
