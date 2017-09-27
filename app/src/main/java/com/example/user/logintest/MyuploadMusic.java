package com.example.user.logintest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MyuploadMusic extends AppCompatActivity {
    String post="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myupload_music);
        //取的intent中的bundle物件
        Bundle bundleID =this.getIntent().getExtras();

        String account = bundleID.getString("AccountID");
        TextView audiolist = (TextView)findViewById(R.id.audiolist);
        //ID:帳號
        //連接php，將相關的音檔載下來
        String phpURL = "http://140.112.107.125:47155/html/myMusic.php" ;

        try {
            post = new MyMusicAsyncTask().execute(account, phpURL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        audiolist.setText(post);

    }
}
