package com.abramkin.multibook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class ChoiceClass extends AppCompatActivity {

    static final String PATH = Environment.getExternalStorageDirectory().toString() + "/MultiBook/";
    static final String ATTRIBUTE_NAME_IMAGE = "image";

    String names, absolutePath;
    String[] namesOfFiles;
    File root;
    File[] filesArray;
    ArrayList<Map<String, Object>> data;
    Context context;
    boolean isText, isPicture, empty = false;

    public void showFiles(Context cntx, boolean isT, boolean isP, String path, ListView lv, Class<?> cl) {

        context = cntx;
        isText = isT;
        isPicture = isP;

        absolutePath = PATH + path;
        root = new File(this.absolutePath);

        filesArray = root.listFiles();


        if ((filesArray != null) && (filesArray.length != 0)) {

            Arrays.sort(filesArray, new Comparator<File>() {
                public int compare(File o1, File o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });

            if (isPicture) {

                data = new ArrayList<Map<String, Object>>(filesArray.length);

                Map<String, Object> m;
                for (int i = 0; i < filesArray.length; i++) {
                    m = new HashMap<String, Object>();
                    m.put(ATTRIBUTE_NAME_IMAGE, filesArray[filesArray.length - i - 1]);
                    data.add(m);
                }

                String[] from = {ATTRIBUTE_NAME_IMAGE};

                int[] to = {R.id.im};

                SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);

                lv.setAdapter(sAdapter);
            } else {

                namesOfFiles = new String[filesArray.length];

                for (int i = 0; i < filesArray.length; i++) {

                    if (filesArray[i].isFile()) {

                        if (isText) {

                            names = openFile(filesArray[i].toString()
                                    .substring(filesArray[i].toString().indexOf(path) + path.length())).replace("\n", " ");

                            if (names.length() > 50) {
                                namesOfFiles[filesArray.length - i - 1] = names.substring(0, 50) + "...";
                            } else {
                                namesOfFiles[filesArray.length - i - 1] = names;
                            }
                        } else {

                            names = filesArray[i].toString()
                                    .substring(filesArray[i].toString().indexOf(path) + path.length()).replace("-", "/");
                            namesOfFiles[filesArray.length - i - 1] = names.replace("`", ":");
                        }

                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.simple_list, namesOfFiles);

                lv.setAdapter(adapter);
            }
        } else {

            if (empty) {
                this.finish();
            } else {
                empty = true;
                Intent intent = new Intent(context, cl);
                startActivity(intent);

            }
        }


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setTitle(R.string.title_alert_delete);

                ad.setPositiveButton(R.string.button_alert_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        delFile(filesArray[filesArray.length - position - 1].getName().toString());

                        empty = true;

                        onResume();

                        Toast.makeText(context, R.string.message_deleted, Toast.LENGTH_SHORT).show();
                    }
                });
                ad.setNegativeButton(R.string.button_alert_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                ad.setCancelable(true);
                ad.create();
                ad.show();


                return true;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (isPicture) {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(filesArray[filesArray.length - position - 1]), "image/*");

                    context.startActivity(intent);
                } else {

                    if (isText) {

                        Intent intent = new Intent(context, TextScreenActivity.class);
                        String nameOfFile = (String) filesArray[filesArray.length - position - 1].getName();
                        intent.putExtra("BODY", openFile(nameOfFile));
                        intent.putExtra("FILENAME", nameOfFile);

                        context.startActivity(intent);
                    } else {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(filesArray[filesArray.length - position - 1]), "audio/*");
                        context.startActivity(intent);

                    }
                }

            }
        });

    }

    void delFile(String filename) {
        File file = new File(this.absolutePath, filename);
        if (file.exists()) {
            file.delete();
        }
    }

    String openFile(String filename) {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File(this.absolutePath, filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text.toString();
    }
}
