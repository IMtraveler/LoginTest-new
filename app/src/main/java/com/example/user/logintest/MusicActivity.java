package com.example.user.logintest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.content.Context;
import android.content.SharedPreferences;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

import static android.widget.AdapterView.*;

public class MusicActivity extends AppCompatActivity{


    Button[] bt = new Button[3];
    ListView audioListView;
    ArrayAdapter MyArrayAdapter;
    TextView attrName ;
    TextView audioIntro;
    TextView audioDuration;
    TextView audioType ;
    private SimpleAdapter MySimpleAdapter ;
    RatingBar ratingBar;
    Button submitRating;


    MediaPlayer mp = new MediaPlayer() ;
    Bundle bundle = new Bundle();

    String[] audioInfo ;
    Uri uri;
    String post = "Audio File";
    ArrayList<Integer> beginURLpos = new ArrayList<>();
    ArrayList<Integer> beginNamepos = new ArrayList<>();
    ArrayList<Integer> endURLpos = new ArrayList<>();
    ArrayList<Integer> endNamepos = new ArrayList<>();
    ArrayList<Integer> AudioID = new ArrayList<>();
    ArrayList<String> AudioName = new ArrayList<>();
    ArrayList<String> AudioURL = new ArrayList<>();
    ArrayList<String> GuideName = new ArrayList<>();
    ArrayList<String> AudioIntro = new ArrayList<>();
    ArrayList<String> AudioType = new ArrayList<>();
    ArrayList<Integer> AudioClick = new ArrayList<>();
    ArrayList<Integer> stars =  new ArrayList<>();
    int num=0;
    String userId = XclSingleton.getInstance().get("AccountID").toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        bt[0] = (Button)findViewById(R.id.button2);
        bt[1] = (Button)findViewById(R.id.btn_pause);
        bt[2] = (Button)findViewById(R.id.button4);
        attrName = (TextView)findViewById(R.id.tv_attrName);
        audioIntro = (TextView)findViewById(R.id.tv_audioIntro);
        audioDuration = (TextView)findViewById(R.id.tv_duration);
        audioType = (TextView)findViewById(R.id.tv_audiotype);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        submitRating = (Button) findViewById(R.id.btn_rating);
        //TextView tv_audioIntro = (TextView)findViewById(R.id.tv_audioIntro) ;

        Bundle bundleFromAttr = getIntent().getExtras();
        final String lat = bundleFromAttr.getString("lat") ;
        final String lng = bundleFromAttr.getString("lng") ;
        String name = bundleFromAttr.getString("name");

        String phpURL = "http://140.112.107.125:47155/html/testAudio.php" ;

        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);



        try {
            post = new MyAsyncTask().execute(lat, lng, phpURL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /* TO-DO: there will be an space element at the end of post string */
        if(post.length()> 3) {
            audioInfo = post.split(";");
            for (int i = 0 ; i < audioInfo.length ;i++)
            {
                try {
                    AudioID.add(Integer.valueOf(audioInfo[i].substring(audioInfo[i].indexOf("audioID:")+9, audioInfo[i].indexOf("audioName:"))));
                    AudioName.add(audioInfo[i].substring(audioInfo[i].indexOf("audioName:") + 11, audioInfo[i].indexOf("audioURL:")));
                    AudioURL.add(audioInfo[i].substring(audioInfo[i].indexOf("audioURL:") + 10, audioInfo[i].indexOf("intro:")));
                    AudioIntro.add(audioInfo[i].substring(audioInfo[i].indexOf("intro:")+7, audioInfo[i].indexOf("account:")));
                    GuideName.add(audioInfo[i].substring(audioInfo[i].indexOf("account:")+9, audioInfo[i].indexOf("type:")));
                    AudioType.add(audioInfo[i].substring(audioInfo[i].indexOf("type:")+6, audioInfo[i].indexOf("clickCount")));
                    AudioClick.add(Integer.valueOf(audioInfo[i].substring(audioInfo[i].indexOf("clickCount:")+12, audioInfo[i].length())));
                    if (i == 1 || i == 3){
                        stars.add(R.drawable.crown);
                    }
                    else {
                        stars.add(0);
                    }
                }catch (Exception e){
                    Log.e("error", e.toString());
                    Log.e("what", audioInfo[i]);
                    Log.e("id", AudioID.get(0).toString());
                }

            }

        }
        else {
            post = "No data";
            AudioID.add(0);
            AudioName.add("No Audio");
            AudioURL.add("http://140.112.107.125:47155/html/uploaded/noAudio.m4a");
            AudioIntro.add("null");
            GuideName.add("null");
            AudioType.add("null");
            AudioClick.add(0);
        }


        //sendData(fm);
        //bundle.putString("audioInfo", "test???");
// set Fragmentclass Arguments
        /*
        SecondAudioFragment secondAF = new SecondAudioFragment();
        secondAF.setArguments(bundle);
        */


        /*bt[0].setText("start");
        bt[1].setText("pause");
        bt[2].setText("stop");*/

        bt[0].setEnabled(false);
        bt[1].setEnabled(false);
        bt[2].setEnabled(false);
        submitRating.setEnabled(false);

/*
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
                if(post.substring(i,i+5).equals("account")){
                    endNamepos.add(i-1); //紀錄Name末端位置
                }
            }
        } */



        //attrName.setText(AudioName.get(0));

        for(int i=0;i< bt.length;i++){
            bt[i].setOnClickListener(new SampleClickListener());
        }


        audioListView = (ListView)findViewById(R.id.list_audio);


        List<HashMap<String , String>> audiolist = new ArrayList<>();
        for(int i = 0 ; i < AudioName.size() ; i++){
            HashMap<String , String> hashMap = new HashMap<>();
            hashMap.put("audioName" , AudioName.get(i));
            hashMap.put("audioType" , AudioType.get(i));
            hashMap.put("guide" , GuideName.get(i));
            hashMap.put("clicks", String.valueOf(AudioClick.get(i)));
            hashMap.put("star", Integer.toString(stars.get(i)));
            //把title , text存入HashMap之中
            audiolist.add(hashMap);
            //把HashMap存入list之中
        }


        final SharedPreferences sharedPref =getSharedPreferences(lat+lng , MODE_PRIVATE);

        MySimpleAdapter = new SimpleAdapter(this, audiolist,
                R.layout.list_item_2,
                new String[] { "audioName", "guide","audioType" , "clicks","star"},
                new int[] {	R.id.list_audioName,R.id.list_guide, R.id.list_audtype, R.id.clicks, R.id.star});
        audioListView.setAdapter(MySimpleAdapter);

        audioListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id ){
                final int[] ratings = new int[1];
                ListView listView = (ListView) parent;
                ratingBar.setRating(0F);
                submitRating.setEnabled(true);
                int existingClick = sharedPref.getInt(String.valueOf(AudioID.get((int)id)) , 0);
                final int newClick = existingClick + 1 ;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(String.valueOf(AudioID.get((int)id)),newClick);
                editor.commit();
                playAudio(id);
                audioType.setText(AudioType.get((int)id));
                /*bt[1].setText("pause");*/
                try {
                    String c = new ClickAsyncTask().execute(userId,AudioID.get((int)id).toString(), Integer.toString(newClick),"-1","http://140.112.107.125:47155/html/clickCount.php").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                submitRating.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ratings[0] = (int)ratingBar.getRating();
                        Toast.makeText(getApplicationContext(), "已為此音檔評分", Toast.LENGTH_LONG).show();
                        ratingBar.setRating(0F);
                        try {
                            String c = new ClickAsyncTask().execute(userId,AudioID.get((int)id).toString(), Integer.toString(newClick),Integer.toString(ratings[0]),"http://140.112.107.125:47155/html/clickCount.php").get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

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

    public void playAudio(long audioID){
        int id = (int) audioID;
        String audiourl = AudioURL.get(id).trim();
        attrName.setText(AudioName.get(id));
        audioIntro.setText(AudioIntro.get(id));

        if (mp.isPlaying()){
            mp.stop();
        }
        try {
            mp.reset();
            mp.setDataSource(audiourl);
            mp.prepare();
            int duration = mp.getDuration();
            audioDuration.setText(milliSecondsToTimer(duration));
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
    }

    public static String milliSecondsToTimer(int milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        //Convert total duration into time
        int minutes =  (milliseconds) / (1000 * 60);
        int seconds = (milliseconds % (1000 * 60) / 1000);
        // Add hours if there
        // Pre appending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
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
                    //bt[1].setText("continue");
                }else{
                    bt[0].setEnabled(false);
                    bt[1].setEnabled(true);
                    bt[2].setEnabled(true);
                    mp.start();
                    //bt[1].setText("pause");
                }
            }else if(v == bt[2])
            {
                bt[0].setEnabled(true);
                bt[1].setEnabled(false);
                bt[2].setEnabled(false);
                mp.pause();
                mp.seekTo(0);
                //bt[0].setText("start");

            }
        }
    }



    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.layout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        Global check = (Global)getApplicationContext();
        int checkk = check.getWord();
        MenuItem registrar = menu.findItem(R.id.我上傳的音檔);
        registrar.setVisible(checkk==0); //if is  guide then is visible
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //取的intent中的bundle物件
        Bundle bundleID =this.getIntent().getExtras();

        String UserID = bundleID.getString("AccountID");
        switch (item.getItemId())
        {
            case R.id.我的最愛:
                //放入點擊後的結果
                Intent intent = new Intent();
                intent.setClass(MusicActivity.this,FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.我上傳的音檔:
                //跳到上傳音檔頁，顯示上傳的音檔名字
                // TODO Auto-generated method stub
                Intent intent2 = new Intent();
                intent2.setClass(MusicActivity.this,MyuploadMusic.class);
                //儲存帳號
                Bundle bundle = new Bundle();
                bundle.putString("AccountID",UserID);
                //將Bundle物件assign給intent
                intent2.putExtras(bundle);
                startActivity(intent2);
                //顯示按鈕的名字
                //Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.我的帳戶:
                //Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent();
                intent6.setClass(MusicActivity.this,MyAccountActivity.class);
                startActivity(intent6);
                break;
            case R.id.條列式瀏覽:
                //Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent();
                intent5.setClass(MusicActivity.this,SpotView.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

