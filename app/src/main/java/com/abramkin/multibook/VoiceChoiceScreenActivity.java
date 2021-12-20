package com.abramkin.multibook;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VoiceChoiceScreenActivity extends ChoiceClass {

    FloatingActionButton newVoice;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_voice_choice_screen);

        lv = findViewById(R.id.listViewVoice);

        newVoice = findViewById(R.id.newVoiceButton);
        newVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoiceChoiceScreenActivity.this, VoiceScreenActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        showFiles(this, false, false, getExternalFilesDir(null) + "/MultiBook/" + "Voices", lv);

        Animation anim_lv = AnimationUtils.loadAnimation(this, R.anim.trans_lv);
        lv.startAnimation(anim_lv);

        Animation anim_fab = AnimationUtils.loadAnimation(this, R.anim.fab);
        newVoice.startAnimation(anim_fab);
    }
}
