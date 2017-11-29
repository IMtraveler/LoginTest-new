package com.example.user.logintest;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class AttractionsActivity extends AppCompatActivity {

    Bundle bundleAudio = new Bundle();
    Bundle bundleUpload = new Bundle();
    private SQLiteHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions);
        helper =SQLiteHelper.getInstance(this);
        ImageView imageView = (ImageView)findViewById(R.id.iv_attr);
        TextView tv_name = (TextView)findViewById(R.id.tv_attrName);
        TextView tv_intro = (TextView)findViewById(R.id.tv_attrIntro);
        TextView tv_types = (TextView)findViewById(R.id.tv_attrtype);


        Bundle bundle = getIntent().getExtras();


        String imgURL = bundle.getString("imgURL");
        final String name = bundle.getString("name");
        String post = bundle.getString("post");
        String lat = bundle.getString("lat");
        String lng = bundle.getString("lng");
        String type = bundle.getString("type");
        tv_types.setText(type);


        bundleAudio.putString("lat",lat);
        bundleAudio.putString("lng",lng);
        bundleAudio.putString("name",name);
        Picasso.with(getBaseContext()).load(imgURL).into(imageView);
        tv_name.setText(name);
        if(post.length()>10) {
            int begIntro = post.indexOf("intro:");
            int endIntro = post.indexOf("image");
            String intro = post.substring(begIntro + 6, endIntro - 1);
            tv_intro.setText(intro);
        }
        else {
            tv_intro.setText("no data");
        }

        bundleUpload.putString("lat",lat);
        bundleUpload.putString("lng",lng);
        bundleUpload.putString("name",name);
        Button btn_toAudio = (Button)findViewById(R.id.btn_toAudio);
        btn_toAudio.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(AttractionsActivity.this,MusicActivity.class);
                intent.putExtras(bundleAudio);
                startActivity(intent);
                AttractionsActivity.this.finish();

            }
        });

        Button btn_upload = (Button)findViewById(R.id.btn_upload);
        Global check = (Global)getApplicationContext();
        int checkk = check.getWord();
        if ( checkk == 0 ) //guide when checkk==0
        {
            //SHOW the button
            btn_upload.setEnabled(true);
        }
        else
        {
            btn_upload.setEnabled(false);
        }
        btn_upload.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(AttractionsActivity.this,UploadActivity.class);
                intent.putExtras(bundleUpload);
                startActivity(intent);
                AttractionsActivity.this.finish();

            }
        });
        Button Like = (Button)findViewById(R.id.imageView3);
        Like.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("name",name);
                //values.put("lat", lat);
                //values.put("lng", lng);
                long id = helper.getWritableDatabase().insert("attr", null, values);
                Log.d("ADD", id+"");
                Toast.makeText(getApplicationContext(), "已設為最愛", Toast.LENGTH_SHORT).show();
            }
        });
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
                intent.setClass(AttractionsActivity.this,FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.我上傳的音檔:
                //跳到上傳音檔頁，顯示上傳的音檔名字
                // TODO Auto-generated method stub
                Intent intent2 = new Intent();
                intent2.setClass(AttractionsActivity.this,MyuploadMusic.class);
                //儲存帳號
                Bundle bundle = new Bundle();
                bundle.putString("AccountID",UserID);
                //將Bundle物件assign給intent
                intent2.putExtras(bundle);
                startActivity(intent2);
                //顯示按鈕的名字
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.音檔種類:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.我的帳戶:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent();
                intent6.setClass(AttractionsActivity.this,MyAccountActivity.class);
                startActivity(intent6);
                break;
            case R.id.條列式瀏覽:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent();
                intent5.setClass(AttractionsActivity.this,SpotView.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
