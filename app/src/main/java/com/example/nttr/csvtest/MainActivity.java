package com.example.nttr.csvtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //set list
    CsvAdapter adapter;
    private  ListView listView;
    ArrayList<Content> Contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Contents = new ArrayList<>();
        //set scan button
        Button button_scan = findViewById(R.id.buttom_scan);
        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReadIsbn();
            }
        });

        //read and write… button
        Button button_export = findViewById(R.id.button_export);
        button_export.setOnClickListener(this);
        Button button_read = findViewById(R.id.button_read);
        button_read.setOnClickListener(this);
        Button button_clear = findViewById(R.id.button_clear);
        button_clear.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.listView);
        adapter = new CsvAdapter(MainActivity.this);
    }

    //set QR reader
    protected void btnReadIsbn() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    // Get QR the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String[] adddata = result.getContents().split(",", 0);

                Content newcontent = new Content(adddata);

                //★★★set list★★★
                if (Contents.size() < 1){
                    Contents.add(newcontent);
                    adapter.addAll(Contents);
                    listView.setAdapter(adapter);
                }else {
                    Contents.add(newcontent);
                    adapter.add(newcontent);
                    adapter.notifyDataSetChanged();
                }
                Log.d("Size!!!!!!", Contents.get(Contents.size()-1).content[0]);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.button_export:
                    export_csv();
                    break;

                case R.id.button_read:
                    read_data();
                    break;

                case R.id.button_clear:
                    clear_data();
                    break;

                default:
                    break;
            }
        }
    }

    //export
    public void export_csv(){
        try {
             // 出力ファイルの作成
            String dir =Environment.getExternalStorageDirectory().getPath();
            FileWriter f = new FileWriter(dir+"/test.csv", false);
            PrintWriter p = new PrintWriter(new BufferedWriter(f));

            // 内容をセットする
            for(int i = 0; i < Contents.size(); i++){
                for(int j=0;j<5;j++) {
                    p.print(Contents.get(i).content[j]);
                    if(j != 4) p.print(",");
                }
                p.println();    // 改行
            }
            // ファイルに書き出し閉じる
            p.close();
            Toast.makeText(this, "Export:"+dir+"/test.csv", Toast.LENGTH_LONG).show();
            Log.d("Export!!!!!!", dir+"/test.csv");

        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Export: failed", Toast.LENGTH_LONG).show();
        }
        return;
    }

    //save data
    public void read_data() {
        String dir =Environment.getExternalStorageDirectory().getPath();
        CSVreader  parser = new CSVreader();

        try {
            parser.reader(Contents);
            adapter.clear();
            adapter.addAll(Contents);
            listView.setAdapter(adapter);
            Toast.makeText(this, "Read:"+dir+"/test.csv", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Read: failed", Toast.LENGTH_LONG).show();
        }
    }
    public void clear_data(){
        Contents.clear();
        adapter.clear();
        Toast.makeText(this, "Clear: compleat", Toast.LENGTH_LONG).show();
    }
}


