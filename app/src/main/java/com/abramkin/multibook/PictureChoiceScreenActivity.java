package com.abramkin.multibook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

public class PictureChoiceScreenActivity extends ChoiceClass {

    FloatingActionButton newPicture;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_choice_screen);

        lv = (ListView) findViewById(R.id.listViewPicture) ;

        newPicture = (FloatingActionButton) findViewById(R.id.newPictureButton);
        newPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PictureChoiceScreenActivity.this, PictureScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_new,R.anim.alpha);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        showFiles(this, false, true, "Pictures/", lv, PictureScreenActivity.class);

        Animation anim_lv = AnimationUtils.loadAnimation(this, R.anim.trans_lv);
        lv.startAnimation(anim_lv);

        Animation anim_fab = AnimationUtils.loadAnimation(this, R.anim.fab);
        newPicture.startAnimation(anim_fab);
    }
}
