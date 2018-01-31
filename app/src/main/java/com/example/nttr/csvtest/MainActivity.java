package com.example.nttr.csvtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //List<Content> Contents = new ArrayList<>();

    //★★★set list★★★
    CsvAdapter adapter;
    private  ListView listView;
    ArrayList<Content> Contents = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set scan button
        Button button_scan = findViewById(R.id.buttom_scan);
        Button button_export = findViewById(R.id.button_export);
        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReadIsbn();
            }
        });
        listView = (ListView)findViewById(R.id.listView);
    }

    //set QR reader?
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
                Log.d("read Data!!!!!!",adddata[0]);

                //★★★set list★★★
                if (Contents.size() < 1){
                    Contents.add(newcontent);
                    adapter = new CsvAdapter(MainActivity.this);
                    adapter.addAll(Contents);
                    listView.setAdapter(adapter);
                }else {
                    Contents.add(newcontent);
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
