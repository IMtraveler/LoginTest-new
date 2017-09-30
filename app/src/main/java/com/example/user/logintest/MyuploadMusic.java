package com.example.user.logintest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyuploadMusic extends AppCompatActivity {
    String post="";
    private ListView listView;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myupload_music);

        String account = XclSingleton.getInstance().get("AccountID").toString();
        //ID:帳號
        //連接php，將相關的音檔載下來
        String phpURL = "http://140.112.107.125:47155/html/myMusic.php" ;

        try {
            post = new MyMusicAsyncTask().execute(account, phpURL).get();
            if(post.trim().equals("")){
                post="你沒有上傳的音檔";
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        final String[] AudioList = post.split(";");
        final String[] NameList = new String[AudioList.length-1];
        for(int i=0;i<AudioList.length-1;i++){
            if(AudioList[i].lastIndexOf("intro:")>=0){
                NameList[i]=AudioList[i].substring(AudioList[i].indexOf("intro:")+6);
            }
        }
        listView = (ListView)findViewById(R.id.list_view);
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,NameList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //這裡應該要顯示點下去應該要出現的頁面
                Toast.makeText(getApplicationContext(), "你上傳的音檔名稱是:  " + NameList[position], Toast.LENGTH_SHORT).show();
            }
        });



    }
}
