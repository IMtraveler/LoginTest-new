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
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;


public class RecommendAttractionActivity extends AppCompatActivity {


    Bundle bundleAudio_recom = new Bundle();
    private SQLiteHelper helper_recom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_attraction);

        helper_recom =SQLiteHelper.getInstance(this);
        ImageView imageView = (ImageView)findViewById(R.id.iv_attr_recom);
        TextView tv_name = (TextView)findViewById(R.id.tv_attrName_recom);
        TextView tv_intro = (TextView)findViewById(R.id.tv_attrIntro_recom);
        TextView tv_types = (TextView)findViewById(R.id.tv_attrtype_recom);
        TextView tv_addr = (TextView)findViewById(R.id.attr_address_recom);
        TextView tv_ename = (TextView)findViewById(R.id.tv_attrNameEn_recom);
        ImageView iv_type = (ImageView) findViewById(R.id.iv_type_recom);
        Bundle bundle = getIntent().getExtras();
        String lat = bundle.getString("lat");
        String lng = bundle.getString("lng");
        String phpURL = "http://140.112.107.125:47155/html/getdata.php" ;
        String post = "";
        try {
            post = new MyAsyncTask().execute(lat, lng, phpURL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        int begName = post.indexOf("name:");
        int endName = post.indexOf("altHead_name");
        int endEName = post.indexOf("address");
        int begType = post.indexOf("class_tag");
        int endType = post.indexOf("lat");

        String type = post.substring(begType + 10, endType - 1);
        final String name = post.substring(begName + 5, endName - 1);
        String ename = post.substring(endName + 13, endEName - 1);
        String addr = post.substring(endEName + 7, begType - 1);

        int begIntro = post.indexOf("intro:");
        int endIntro = post.indexOf("image");
        String intro = post.substring(begIntro + 6, endIntro - 1);
        int begImg = post.indexOf("image");
        int endIndex = post.indexOf("classified")-1;
        String audioNumStr =post.substring(post.indexOf("audioNum:")+10, post.length());
        int audioNum =  Integer.valueOf(audioNumStr.trim().replaceAll("/n ", ""));
        if ((begImg + 6) >= endIndex - 5) {
            bundle.putString("imgURL", "http://140.112.107.125:47155/html/uploaded/null.png");
        } else {
            String imgURL = post.substring(begImg + 6, endIndex);
            imgURL = "http:" + imgURL.replaceAll(" ", "");
            Picasso.with(RecommendAttractionActivity.this).load(imgURL).into(imageView);
            Log.e("img_url",imgURL);
            Log.e("test","test");
            bundle.putString("imgURL", imgURL);
        }

        int begClass = post.indexOf("classified");
        int endClass = post.indexOf("audioNum");

        String classi_str = post.substring(begClass + 11, endClass-1);
        int classi =  Integer.valueOf(classi_str.trim().replaceAll(" ", ""));

        tv_intro.setText(intro);
        tv_name.setText(name);
        tv_types.setText(type);
        tv_ename.setText(ename);
        tv_addr.setText(addr);

        bundleAudio_recom.putString("lat",lat);
        bundleAudio_recom.putString("lng",lng);
        bundleAudio_recom.putString("name",name);

        Button btn_toAudio = (Button)findViewById(R.id.btn_RecomtoAudio);
        btn_toAudio.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(RecommendAttractionActivity.this, MusicActivity.class);
                intent.putExtras(bundleAudio_recom);
                startActivity(intent);
                RecommendAttractionActivity.this.finish();

            }
        });



        if(classi==1)
            iv_type.setImageResource(R.drawable.a01);
        else if(classi==2)
            iv_type.setImageResource(R.drawable.a02);
        else if(classi==3)
            iv_type.setImageResource(R.drawable.a03);
        else if(classi==4)
            iv_type.setImageResource(R.drawable.a04);
        else if(classi==5)
            iv_type.setImageResource(R.drawable.a05);
        else if(classi==6)
            iv_type.setImageResource(R.drawable.a06);
        else if(classi==7)
            iv_type.setImageResource(R.drawable.a07);
        else if(classi==8)
            iv_type.setImageResource(R.drawable.a08);
        else if(classi==9)
            iv_type.setImageResource(R.drawable.a09);
        else if(classi==10)
            iv_type.setImageResource(R.drawable.a10);
        else if(classi==11)
            iv_type.setImageResource(R.drawable.a11);



        Button Like = findViewById(R.id.imageView3_recom);
        Like.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("name",name);
                //values.put("lat", lat);
                //values.put("lng", lng);
                long id = helper_recom.getWritableDatabase().insert("attr", null, values);
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
                intent.setClass(RecommendAttractionActivity.this,FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.我上傳的音檔:
                //跳到上傳音檔頁，顯示上傳的音檔名字
                // TODO Auto-generated method stub
                Intent intent2 = new Intent();
                intent2.setClass(RecommendAttractionActivity.this,MyuploadMusic.class);
                //儲存帳號
                Bundle bundle = new Bundle();
                bundle.putString("AccountID",UserID);
                //將Bundle物件assign給intent
                intent2.putExtras(bundle);
                startActivity(intent2);
                //顯示按鈕的名字
                //Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.景點種類:
                //Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.我的帳戶:
                //Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent();
                intent6.setClass(RecommendAttractionActivity.this,MyAccountActivity.class);
                startActivity(intent6);
                break;
            case R.id.條列式瀏覽:
                //Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent();
                intent5.setClass(RecommendAttractionActivity.this,SpotView.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
