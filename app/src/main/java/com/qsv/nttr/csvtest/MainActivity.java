package com.qsv.nttr.csvtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
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
    private ShowcaseView ShowCase;
    private int counter = 0;
    RelativeLayout.LayoutParams lps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //List view
        Contents = new ArrayList<>();
        listView = (ListView)findViewById(R.id.listView);
        adapter = new CsvAdapter(MainActivity.this);

        //set scan button
        Button button_scan = findViewById(R.id.buttom_scan);
        button_scan.setOnClickListener(this);
        Button button_export = findViewById(R.id.button_export);
        button_export.setOnClickListener(this);
        Button button_read = findViewById(R.id.button_read);
        button_read.setOnClickListener(this);
        Button button_clear = findViewById(R.id.button_clear);
        button_clear.setOnClickListener(this);

        //showcase layout
        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        int margin2 = ((Number) (getResources().getDisplayMetrics().density * 75)).intValue();
        lps.setMargins(margin, margin, margin, margin2);

        //set showcase
        //super.onCreate(savedInstanceState);
        ShowCase = new ShowcaseView.Builder(this)
                .setTarget(Target.NONE)
                .setContentTitle("ようこそ")
                .setContentText( "QRコードから読み込んだデータを「,」区切りでCSV化して保存するアプリです")
                .setStyle(R.style.CustomShowcaseTheme2)
                .replaceEndButton(R.layout.button_layout)
                .singleShot(42)
                .setOnClickListener(this)
                .build();
        ShowCase.setButtonText("Next");
        ShowCase.setButtonPosition(lps);
    }

    @Override
    public void onClick(View v) {
        Button button_export = findViewById(R.id.button_export);
        Button button_read = findViewById(R.id.button_read);
        Button button_clear = findViewById(R.id.button_clear);
        Button button_scan = findViewById(R.id.buttom_scan);

        if (counter < 5) {
            switch (counter) {
                case 0:
                    ShowCase.setTarget(new ViewTarget(button_scan));
                    ShowCase.setContentText("Scan: QRコードをスキャンします");
                    counter++;
                    break;
                case 1:
                    ShowCase.setShowcase(new ViewTarget(button_export), true);
                    ShowCase.setContentText("Export：本体にデータを保存します");
                    counter++;
                    break;
                case 2:
                    setRight();
                    ShowCase.setButtonPosition(lps);
                    ShowCase.setShowcase(new ViewTarget(button_read), true);
                    ShowCase.setContentText("Read：本体に保存したデータを読み出します　※ストレージのアクセス権限を許可してください");
                    counter++;
                    break;
                case 3:
                    ShowCase.setShowcase(new ViewTarget(button_clear), true);
                    ShowCase.setContentText("Clear：スキャンしたデータをクリアします");
                    ShowCase.setButtonText("close");
                    counter++;
                    break;
                case 4:
                    ShowCase.hide();
                    counter++;
                    break;
            }
        }
        if (v != null) {
            switch (v.getId()) {
                case R.id.buttom_scan:
                    scan_QR();
                    break;

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

    //set QR reader
    protected void scan_QR() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
        return;
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
        return;
    }

    //export data
    public void export_csv(){
        try {
             // 出力ファイルの作成
            String dir =Environment.getExternalStorageDirectory().getPath();
            FileWriter f = new FileWriter(dir+"/QRcsv.csv", false);
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
            Toast.makeText(this, "Export: "+dir+"/QRcsv.csv", Toast.LENGTH_LONG).show();
            Log.d("Export!!!!!!", dir+"/test.csv");

        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Export: failed\r\n※ストレージのアクセス権限を確認してください", Toast.LENGTH_LONG).show();
        }
        return;
    }

    //read data
    public void read_data() {
        String dir =Environment.getExternalStorageDirectory().getPath();
        CSVreader parser = new CSVreader();
        try {
            parser.reader(Contents);
            adapter.clear();
            adapter.addAll(Contents);
            listView.setAdapter(adapter);
            Toast.makeText(this, "Read: "+dir+"/QRcsv.csv", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Read: failed\n" +
                    "\r\n※ストレージのアクセス権限を確認してください", Toast.LENGTH_LONG).show();
        }
        return;
    }

    //clear data
    public void clear_data(){
        Contents.clear();
        adapter.clear();
        Toast.makeText(this, "Clear: compleat", Toast.LENGTH_LONG).show();
        return;
    }


    private void setRight(){
        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        return;
    }

}


