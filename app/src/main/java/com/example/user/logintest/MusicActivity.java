package com.example.user.logintest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.app.*;
import android.view.*;
import android.view.View;
import android.widget.*;
import android.media.*;
import android.media.MediaPlayer.*;

public class MusicActivity extends AppCompatActivity {

    Button[] bt = new Button[3];
    MediaPlayer mp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        bt[0] = (Button)findViewById(R.id.button2);
        bt[1] = (Button)findViewById(R.id.button3);
        bt[2] = (Button)findViewById(R.id.button4);
        Button buttonback = (Button)findViewById(R.id.button5);
        buttonback.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MusicActivity.this,MainPageActivity.class);
                startActivity(intent);
                MusicActivity.this.finish();
            }
        });
        bt[0].setText("start");
        bt[1].setText("pause");
        bt[2].setText("stop");

        bt[0].setEnabled(true);
        bt[1].setEnabled(false);
        bt[2].setEnabled(false);

        for(int i=0;i< bt.length;i++){
            bt[i].setOnClickListener(new SampleClickListener());
        }





        // setContentView(R.layout.activity_main);
    }
    public void onResume()
    {
        super.onResume();
        //mp = MediaPlayer.create(this, R.raw.music); //���oMediaPlayer���O������
        //Uri uri = Uri.parse("http://140.112.107.125:47155/html/uploaded/Huaientang.m4a");
        Bundle extras = getIntent().getExtras();
        String audiouri = extras.getString("audioURL");
        Uri uri = Uri.parse("http://140.112.107.125:47155/html/uploaded/noAudio.m4a");
        if (audiouri.length() > 10){
            uri = Uri.parse(audiouri);
        }
        //Uri uri = Uri.parse(audiouri);
        mp = MediaPlayer.create(this,uri );
        mp.setOnCompletionListener(new SampleCompletionListener()); //�n�����񧹲���ť��
    }
    public void onPause()
    {
        super.onPause();
        mp.release();
    }
    private class SampleCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            bt[0].setEnabled(true);
            bt[1].setEnabled(false);
            bt[2].setEnabled(false);
        }
    }
    private class SampleClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            if(v == bt[0])
            {
                bt[0].setEnabled(false);
                bt[1].setEnabled(true);
                bt[2].setEnabled(false);
                try
                {
                    mp.prepare();
                } catch (Exception e) {
                    mp.start();
                }
            }else if(v == bt[1])
            {
                if(mp.isPlaying())
                {
                    bt[0].setEnabled(false);
                    bt[1].setEnabled(true);
                    bt[2].setEnabled(true);
                    mp.pause();
                    bt[1].setText("continue");
                }else{
                    bt[0].setEnabled(false);
                    bt[1].setEnabled(true);
                    bt[2].setEnabled(true);
                    mp.start();
                    bt[1].setText("pause");
                }
            }else if(v == bt[2])
            {
                bt[0].setEnabled(true);
                bt[1].setEnabled(false);
                bt[2].setEnabled(false);
                mp.pause();
                mp.seekTo(0);
                bt[0].setText("start");

            }
        }
    }
}
