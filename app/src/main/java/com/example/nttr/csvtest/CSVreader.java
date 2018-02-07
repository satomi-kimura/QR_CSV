package com.example.nttr.csvtest;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by nttr on 2018/02/07.
 */

public class CSVreader {

    ArrayList<Content> Contents = new ArrayList<Content>();

    public ArrayList<Content> reader(ArrayList<Content> Contents) throws IOException {
        String dir =Environment.getExternalStorageDirectory().getPath();
        BufferedReader reader;
        String line;

        try {
            Log.d("Read!!!!!!", dir+"/test.csv");
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(
                    dir+"/test.csv"), "utf-8"));

            // 出力ファイルの作成
            try {
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    Log.d("Read!!!!!!", line);
                    String[] adddata = line.split(",", 0);
                    Content newcontent = new Content(adddata);
                    Contents.add(newcontent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Contents;
    }
}
