package com.example.user.logintest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.os.StrictMode ;
import android.app.DownloadManager ;
import android.content.Context ;
import android.net.Uri ;
import org.json.JSONArray;
import org.json.JSONObject;


public class MainPageActivity extends AppCompatActivity  {

    DownloadManager downloadManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Switch sw1 = (Switch)findViewById(R.id.onff);
        final Global check = (Global)getApplicationContext();

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    check.setWord(1);

                }else{
                    check.setWord(0);

                }

            }
        });
        findViews();
        setListeners();
        //取的intent中的bundle物件
        Bundle bundleID =this.getIntent().getExtras();
        final String userID = bundleID.getString("AccountID");

        Button button03 = (Button)findViewById(R.id.Button03);
        button03.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainPageActivity.this,MainActivity.class);
                startActivity(intent);
                MainPageActivity.this.finish();
            }
        });
        Button button04 = (Button)findViewById(R.id.Button04);

        button04.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                //intent.setClass(MainPageActivity.this,MusicActivity.class);
                //startActivity(intent);
                //MainPageActivity.this.finish();
            }
        });

        Button btn_toMap = (Button)findViewById(R.id.btn_toMap);

        btn_toMap.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainPageActivity.this,MapsActivity.class);
                //儲存帳號
                Bundle bundle = new Bundle();
                bundle.putString("AccountID",userID);
                //將Bundle物件assign給intent
                intent.putExtras(bundle);
                startActivity(intent);
                MainPageActivity.this.finish();
            }
        });

        Button btn_toUpload = (Button)findViewById(R.id.btn_toUpload);


        Button btn_toLocation = (Button)findViewById(R.id.btn_toLocation);

        btn_toLocation.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainPageActivity.this,LocationListActivity.class);
                startActivity(intent);
                //MainPageActivity.this.finish();
            }
        });


        Button btn_download = (Button)findViewById(R.id.btn_download);
        final Downloads downloads = new Downloads(this);
        btn_download.setOnClickListener(new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            //downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE) ;
            //Uri uri = Uri.parse("http://140.112.107.125:47155/html/uploaded/sample.mp4") ;
            //DownloadManager.Request request = new DownloadManager.Request(uri) ;
            //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            //Long reference = downloadManager.enqueue(request) ;

            String imguri = downloads.DownloadImg("http://140.112.107.125:47155/html/uploaded/IMG_20170730_151527.jpg");
            Toast.makeText(MainPageActivity.this, imguri, Toast.LENGTH_SHORT).show();
            Log.e("",imguri);


        }
    });
}


    private Button button_get_record;
    private void findViews() {
        button_get_record = (Button)findViewById(R.id.btn_mysql);
    }
    private void setListeners() {
        button_get_record.setOnClickListener(getDBRecord);
    }
    String result = "" ;
    private Button.OnClickListener getDBRecord = new
            Button.OnClickListener() {
                public void onClick(View v) {
                    try {
                        result = MySQLConnection.executeQuery();

                    } catch(Exception e) {
                        Log.e("log_tag", e.toString());
                    }
                    TextView showdb= (TextView) findViewById(R.id.tv_data);
                    showdb.setText(result);
                }
            };
}
