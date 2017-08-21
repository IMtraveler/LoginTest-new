package com.example.user.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.HashMap;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

    LoginButton loginButton;
    TextView textView;
    CallbackManager callbackManager;
    private RequestQueue mQueue; //初始化 取得volley的request物件, 建議將mQueue設為單一物件全域使用,避免浪費資源
    private final static String mUrl = "http://140.112.107.125:47155/html/login.php";
    private StringRequest getRequest;
    private TextView msg;
    private EditText account;
    private EditText password;
    private String re_String;




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



        Button b_login = (Button) findViewById(R.id.b_login); //登入按鈕
        Button b_register = (Button) findViewById(R.id.b_register);   //註冊按鈕
        mQueue = Volley.newRequestQueue(this);


        b_login.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                account = (EditText) findViewById(R.id.AccountID); //輸入的帳號
                password = (EditText) findViewById(R.id.Password); //輸入的密碼

                msg = (TextView) findViewById(R.id.data) ;
                getRequest = new StringRequest(Request.Method.POST,mUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                //msg.setText(s.trim());

                                String check = "accountID: "+account.getText().toString()+"\npassword: "+password.getText().toString();


                                if(check.trim().equals(s.trim())){
                                // TODO Auto-generated method stub
                                   Intent intent = new Intent();
                                   intent.setClass(MainActivity.this,MainPageActivity.class);
                                   startActivity(intent);
                                   MainActivity.this.finish();
                                }else{
                                    msg.setText("帳號或密碼錯誤");
                                }


                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                msg.setText(volleyError.getMessage());
                            }
                        }){
                    //攜帶參數
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap();
                        map.put("account", account.getText().toString());
                        map.put("password",password.getText().toString());
                        return map;
                    }



                };
                mQueue.add(getRequest);
               // String check = "Connected successfully[{\"accountID\":\""+account.getText().toString()+"\",\"password\":\""+password.getText().toString()+"\"}]";
               // if(check.equals(s)){
                //    // TODO Auto-generated method stub
                //    Intent intent = new Intent();
                //    intent.setClass(MainActivity.this,MainPageActivity.class);
                //    startActivity(intent);
                //    MainActivity.this.finish();
              //  }








                 /*
                             ##這裡要確認是否有此帳號密碼  XD
                             */
             // if(check.equals(msg)){
                  // TODO Auto-generated method stub
               //   Intent intent = new Intent();
               //   intent.setClass(MainActivity.this,MainPageActivity.class);
               //   startActivity(intent);
              //    MainActivity.this.finish();
           //    }

            }
        });

        b_register.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //跳到註冊頁面
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
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




