package com.example.user.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    LoginButton loginButton;
    TextView textView;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //performClick() 實現自動點擊
        //accessToken之後或許還會用到 先存起來
        // accessToken = loginResult.getAccessToken();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (LoginButton)findViewById(R.id.fb_login_bn);
        textView = (TextView)findViewById(R.id.textView);
        textView.setText("Facebook登入");
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,CheckpersonActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
               // String UserId = loginResult.getAccessToken().getUserId() 這個是那個用戶的id 但是似乎無法放這裡
               // String token = loginResult.getAccessToken().getToken();  這個是那個用戶的token(還要查查是啥，但是應該是相關基本資料吧?
            }

            @Override
            public void onCancel() {
                textView.setText("Login Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                textView.setText("Login Error");
            }
        });

        TextView tv_skip = (TextView) findViewById(R.id.tv_skip);

        tv_skip.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,MainPageActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
