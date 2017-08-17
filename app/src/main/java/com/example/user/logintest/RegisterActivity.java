package com.example.user.logintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText newaccount = (EditText)findViewById(R.id.new_id); //新註冊的帳號
        EditText newpassword = (EditText)findViewById(R.id.new_password);  //新註冊的密碼
        /*
              這裡要跟資料庫核對有沒有一樣帳號的人，如果沒有，將它存到資料庫中
              如果有，跳出提示，註冊失敗(可能帳號已被註冊)
            */
        Button register = (Button)findViewById(R.id.b_toRegister); //註冊按鈕
        //如果註冊成功，就回到登入頁面
       register.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*
                               ##這裡要確認是否有此帳號密碼  XD
                             */
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
        });

    }

}
