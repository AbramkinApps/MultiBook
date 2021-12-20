package com.abramkin.multibook;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TextChoiceScreenActivity extends ChoiceClass {

    FloatingActionButton newText;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_choice_screen);

        lv = findViewById(R.id.listViewText);

        newText = findViewById(R.id.newTextButton);
        newText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TextChoiceScreenActivity.this, TextScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        showFiles(this, true, false, getExternalFilesDir(null) + "/MultiBook/" + "Texts", lv);

        Animation anim_lv = AnimationUtils.loadAnimation(this, R.anim.trans_lv);
        lv.startAnimation(anim_lv);

        Animation anim_fab = AnimationUtils.loadAnimation(this, R.anim.fab);
        newText.startAnimation(anim_fab);
    }
}
