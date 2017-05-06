package com.abramkin.multibook;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TextScreenActivity extends AppCompatActivity {

    String path = Environment.getExternalStorageDirectory().toString()+"/MultiBook/Texts/";
    EditText edt;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_text_screen);
        edt = (EditText) findViewById(R.id.editText);

        if (getIntent().getStringExtra("BODY")!=null)
        edt.setText(getIntent().getStringExtra("BODY"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (getIntent().getStringExtra("FILENAME")!=null) {
            saveFile(getIntent().getStringExtra("FILENAME"),edt.getText().toString());
        }
        else {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("d-MM-yyyy HH-mm-ss");

            filename = dateFormat.format(date).toString()+".txt";

            if (!edt.getText().toString().isEmpty())
                saveFile(filename,edt.getText().toString()); }
    }

    void saveFile(String filename, String body) {

        try {
            File root = new File(this.path);
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, filename);

            if (!file.exists())
                file.createNewFile();

            FileWriter writer = new FileWriter(file, true);

            PrintWriter printwriter = new PrintWriter(file);
            printwriter.print("");
            printwriter.close();

            writer.append(body);
            writer.flush();
            writer.close();

            Toast.makeText(TextScreenActivity.this,R.string.message_saved,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
