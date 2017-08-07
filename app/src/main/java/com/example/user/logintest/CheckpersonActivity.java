package com.example.user.logintest;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class CheckpersonActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkperson);

        Button button01 = (Button)findViewById(R.id.Button01);

        button01.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(CheckpersonActivity.this,MainPageActivity.class);
                startActivity(intent);
                CheckpersonActivity.this.finish();
            }
        });

    }
}
