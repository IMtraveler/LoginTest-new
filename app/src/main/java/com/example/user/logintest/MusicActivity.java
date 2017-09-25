package com.example.user.logintest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import layout.SecondAudioFragment;
import layout.FirstAudioFragment;

public class MusicActivity extends AppCompatActivity implements OnClickListener{


    Button[] bt = new Button[3];
    Button nextbn;
    Button prebn;
    Button btn3 ;

    MediaPlayer mp = new MediaPlayer() ;
    Bundle bundle = new Bundle();

    String[] audioInfo ;
    Uri uri;
    String post = "Audio File";
    ArrayList<Integer> beginURLpos = new ArrayList<>();
    ArrayList<Integer> beginNamepos = new ArrayList<>();
    ArrayList<Integer> endURLpos = new ArrayList<>();
    ArrayList<Integer> endNamepos = new ArrayList<>();
    ArrayList<String> AudioName = new ArrayList<>();
    ArrayList<String> AudioURL = new ArrayList<>();
    int num=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        bt[0] = (Button)findViewById(R.id.button2);
        bt[1] = (Button)findViewById(R.id.btn_pause);
        bt[2] = (Button)findViewById(R.id.button4);
        nextbn = (Button)findViewById( R.id.btn_nextAudio);
        prebn  = (Button)findViewById( R.id.btn_preAudio);
        btn3 = (Button)findViewById( R.id.button3);
        //TextView tv_audioIntro = (TextView)findViewById(R.id.tv_audioIntro) ;

        Bundle bundleFromAttr = getIntent().getExtras();
        String lat = bundleFromAttr.getString("lat") ;
        String lng = bundleFromAttr.getString("lng") ;
        String name = bundleFromAttr.getString("name");

        String phpURL = "http://140.112.107.125:47155/html/testAudio.php" ;

        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //FragmentManager fm ;
        //fm = getSupportFragmentManager() ;


        try {
            post = new MyAsyncTask().execute(lat, lng, phpURL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(post.length()> 3) {
           // tv_audioIntro.setText(post);
        }
        else {
            post = "No data";
        }
        audioInfo = post.split(";");

        //sendData(fm);
        bundle.putString("audioInfo", "test???");
// set Fragmentclass Arguments
        /*
        SecondAudioFragment secondAF = new SecondAudioFragment();
        secondAF.setArguments(bundle);
        */


        bt[0].setText("start");
        bt[1].setText("pause");
        bt[2].setText("stop");

        bt[0].setEnabled(false);
        bt[1].setEnabled(false);
        bt[2].setEnabled(false);

        //for(int i=0;i< bt.length;i++){
         //   bt[i].setOnClickListener(new SampleClickListener());
        //}


        //TextView namelist = (TextView)findViewById(R.id.namelist) ;
       if( post.trim().length()>8){
            for(int i=0;i<post.length()-8;i++) {
                if (post.substring(i, i + 8).equals("audioURL")) {
                    beginURLpos.add(i+9); //紀錄URL初始位置
                }
            }
            for(int i=0;i<post.length()-5;i++) {
                if(post.substring(i,i+5).equals("intro")){
                    beginNamepos.add(i+6); //紀錄Name初始位置
                    endURLpos.add(i-1); //紀錄URL末端位置
                }
                if(post.substring(i,i+5).equals("guide")){
                    endNamepos.add(i-1); //紀錄Name末端位置
                }
            }
        }
        if(beginNamepos.size()==0){
            AudioName.add("No Audio");
            AudioURL.add("http://140.112.107.125:47155/html/uploaded/noAudio.m4a");
        }else{
            for(int i =0;i<beginNamepos.size();i++){
                AudioName.add(post.substring(beginNamepos.get(i),endNamepos.get(i)+1));
                AudioURL.add(post.substring(beginURLpos.get(i),endURLpos.get(i)+1));
            }
        }



        //namelist.setText(AudioName.get(0)+" "+AudioURL.get(0));
        TextView attrName = (TextView)findViewById(R.id.tv_attrName);
        attrName.setText(AudioName.get(0));

        for(int i=0;i< bt.length;i++){
            bt[i].setOnClickListener(new SampleClickListener());
        }

        // setContentView(R.layout.activity_main);
    }

    /*
    public void sendData(FragmentManager fm) {
        //PACK DATA IN A BUNDLE
        Bundle bundle = new Bundle();
        bundle.putString("audioInfo", audioInfo[1]);
        SecondAudioFragment myFragment = new SecondAudioFragment();
        myFragment.setArguments(bundle);
        //THEN NOW SHOW OUR FRAGMENT
        fm.beginTransaction().replace(R.id.fragment_place,myFragment).commit();
    }*/



    public void onResume()
    {
        super.onResume();


       /* try {
            mp.setDataSource(audiourl);
            mp.prepare();
            //mp.start();
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            Log.e("here's url", audiourl);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        mp.setOnCompletionListener(new SampleCompletionListener());
    }
    public void onPause()
    {
        super.onPause();
        mp.reset();
    }


    @Override
    public void onClick(View view) {
        Fragment fragment;
        String audiourl ;
        TextView attrName = (TextView) findViewById(R.id.tv_attrName);


        if (view == findViewById(R.id.btn_nextAudio)) {
            audiourl = AudioURL.get(num).trim();
            attrName.setText(AudioName.get(num));
            if (mp.isPlaying()){
                mp.stop();
            }
            try {
                mp.reset();
                mp.setDataSource(audiourl);
                mp.prepare();
                //mp.start();
            } catch (IllegalArgumentException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                Log.e("here's url", audiourl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
            bt[0].setEnabled(false);
            bt[1].setEnabled(true);
            bt[2].setEnabled(true);
            nextbn.setEnabled(false);
            prebn.setEnabled(true);
            btn3.setEnabled(true);


        } /*
            /*
            Bundle SecondAudioBundle = new Bundle();
            SecondAudioBundle.putString("audioInfo", audioInfo[2]);
            fragment = new SecondAudioFragment();
            fragment.setArguments(SecondAudioBundle);
            FragmentManager fragmentManager = getSupportFragmentManager()  ;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction() ;
            fragmentTransaction.replace(R.id.fragment_place, fragment);
            fragmentTransaction.commit();


            mp.pause();
            mp.seekTo(0);
            mp.reset();
            audiourl= AudioURL.get(num).trim();

            uri = Uri.parse(audiourl);
            try {
                mp.setDataSource(this,uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.prepareAsync();

        } */


       if (view == findViewById(R.id.btn_preAudio)) {
           audiourl = AudioURL.get(1).trim();
           attrName.setText(AudioName.get(1));
           if (mp.isPlaying()){
               mp.stop();
           }
           try {
               mp.reset();
               mp.setDataSource(audiourl);
               mp.prepare();
               //mp.start();
           } catch (IllegalArgumentException e) {
               Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
               Log.e("here's url", audiourl);
           } catch (IOException e) {
               e.printStackTrace();
           }
           mp.start();
           bt[0].setEnabled(false);
           bt[1].setEnabled(true);
           bt[2].setEnabled(true);
           prebn.setEnabled(false);
           btn3.setEnabled(true);
           nextbn.setEnabled(true);

        }

        if (view == findViewById(R.id.button3)) {
            audiourl = AudioURL.get(2).trim();
            attrName.setText(AudioName.get(2));
            if (mp.isPlaying()){
                mp.stop();
            }
            try {
                mp.reset();
                mp.setDataSource(audiourl);
                mp.prepare();
                //mp.start();
            } catch (IllegalArgumentException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                Log.e("here's url", audiourl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
            bt[0].setEnabled(false);
            bt[1].setEnabled(true);
            bt[2].setEnabled(true);
            prebn.setEnabled(false);
            btn3.setEnabled(true);
            nextbn.setEnabled(true);

        }
        /*
            Bundle FirstAudioBundle = new Bundle();
            FirstAudioBundle.putString("audioInfo",audioInfo[1]);
            fragment = new FirstAudioFragment();
            FragmentManager fragmentManager = getSupportFragmentManager()  ;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction() ;
            fragmentTransaction.replace(R.id.fragment_place, fragment);
            fragment.setArguments(FirstAudioBundle);
            fragmentTransaction.commit();


            audiourl= AudioURL.get(num).trim();
            uri = Uri.parse(audiourl);
            try {
                mp.setDataSource(this,uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.prepareAsync();*/



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
                bt[2].setEnabled(true);

                try
                {
                    mp.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mp.start();
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
                btn3.setEnabled(true);
                prebn.setEnabled(true);
                nextbn.setEnabled(true);

            }
        }
    }
}

