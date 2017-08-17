package com.example.user.logintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class AttractionsActivity extends AppCompatActivity {

    Bundle bundleAudio = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions);

        ImageView imageView = (ImageView)findViewById(R.id.iv_attr);
        TextView tv_name = (TextView)findViewById(R.id.tv_attrName);
        TextView tv_intro = (TextView)findViewById(R.id.tv_attrIntro);
        Bundle bundle = getIntent().getExtras();


        String imgURL = bundle.getString("imgURL");
        String name = bundle.getString("name") ;
        String post = bundle.getString("post");
        String audiouri = bundle.getString("audioURL") ;
        //
        bundleAudio.putString("audioURL",audiouri);
        Picasso.with(getBaseContext()).load(imgURL).into(imageView);
        tv_name.setText(name);
        int begIntro = post.indexOf("intro:");
        int endIntro = post.indexOf("imgURL") ;
        String intro = post.substring(begIntro+6, endIntro-1);
        tv_intro.setText(intro);


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

    }

}
