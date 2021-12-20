package com.abramkin.multibook;


import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;


public class VoiceScreenActivity extends AppCompatActivity {

    MediaRecorder mrec = null;
    ImageButton startRecording = null;
    File audiofile;
    String filename;
    Chronometer chron;
    Boolean rec = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_screen);

        RequestUserPermission requestUserPermission = new RequestUserPermission(this);
        requestUserPermission.verifyRecordAudioPermissions();

        if (mrec == null) mrec = new MediaRecorder();

        startRecording = findViewById(R.id.startRecord);

        chron = findViewById(R.id.chron);
        chron.setBase(SystemClock.elapsedRealtime());

        chron.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime() - chron.getBase();

                if (elapsedMillis > 300000) {
                    chron.stop();
                    stopRecording();

                    startRecording.setImageResource(R.drawable.startrecord);

                    makeText(getApplicationContext(), R.string.message_saved, Toast.LENGTH_SHORT).show();
                }
            }
        });

        startRecording.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (!rec) {

                        chron.setBase(SystemClock.elapsedRealtime());
                        chron.start();

                        startRecording();

                        startRecording.setImageResource(R.drawable.stoprecord);

                        rec = true;
                    } else {

                        chron.stop();
                        stopRecording();

                        startRecording.setImageResource(R.drawable.startrecord);

                        rec = false;

                        makeText(getApplicationContext(), R.string.message_saved, Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (rec) {
            delFile(filename);
            stopRecording();
        }
    }

    void startRecording() throws IOException {

        mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
        mrec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mrec.setAudioEncodingBitRate(128000);
        mrec.setAudioSamplingRate(44100);

        if (audiofile == null) {
            String path = getExternalFilesDir(null).toString() + "/MultiBook/Voices/";

            File root = new File(path);
            if (!root.exists()) {
                root.mkdirs();
            }
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("d-MM-yyyy HH`mm`ss");

            filename = dateFormat.format(date).toString() + ".mp3";

            audiofile = new File(path, filename);
        }

        mrec.setOutputFile(audiofile.getAbsolutePath());

        mrec.prepare();
        mrec.start();
    }

    void stopRecording() {
        mrec.stop();
        mrec.release();
        this.finish();
    }

    void delFile(String filename) {
        File file = new File(getExternalFilesDir(null), filename);
        if (file.exists()) {
            file.delete();
        }
    }
}