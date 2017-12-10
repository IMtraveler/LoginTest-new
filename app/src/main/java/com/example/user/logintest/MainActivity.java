package com.example.user.logintest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import android.support.v7.app.AlertDialog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

    Button loginButton;
    TextView textView;
    CallbackManager callbackManager;
    private RequestQueue mQueue; //初始化 取得volley的request物件, 建議將mQueue設為單一物件全域使用,避免浪費資源
    private final static String mUrl = "http://140.112.107.125:47155/html/login.php";
    private StringRequest getRequest;
    private TextView msg;
    private EditText account;
    private EditText password;
    private CheckBox  login_check1,fb_login_check;
    private String UserId;
    private final static String rUrl = "http://140.112.107.125:47155/html/Facebook.php";
    private AccessToken accessToken;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //performClick() 實現自動點擊
        //accessToken之後或許還會用到 先存起來
        //accessToken = loginResult.getAccessToken();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //找到button (fb_login)
        loginButton = (Button)findViewById(R.id.fb_login_bn);
        textView = (TextView)findViewById(R.id.textView);
        textView.setText("Facebook登入");
        callbackManager = CallbackManager.Factory.create();
        msg = (TextView) findViewById(R.id.data) ;
        //SharedPreferences將name 和 pass 記錄起來 每次進去軟體時 開始從中讀取資料 放入login_name，login_password中
        SharedPreferences remdname=getPreferences(Activity.MODE_PRIVATE);
        String name_str=remdname.getString("name", "");
        String pass_str=remdname.getString("pass", "");

        loginButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "user_friends"));
            }
        });


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                /*Fackbook ID------------------------------------------------------------------------*/
                UserId = loginResult.getAccessToken().getUserId();
                accessToken = loginResult.getAccessToken();
                Log.d("FB","access token got");
                //  msg.setText(UserId);
                /*--------------------------------------------------------------------------------------*/
                // String token = loginResult.getAccessToken().getToken();  這個是那個用戶的token(還要查查是啥，但是應該是相關基本資料吧?
                getRequest = new StringRequest(Request.Method.POST,rUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {

                                Intent intent = new Intent();
                                msg.setText(s);
                                intent.setClass(MainActivity.this,MapsActivity.class);
                                //儲存帳號
                                Bundle bundle = new Bundle();
                                bundle.putString("AccountID",UserId );
                                //將Bundle物件assign給intent
                                intent.putExtras(bundle);
                                XclSingleton.getInstance().put("AccountID",UserId);
                                //跳到首頁
                                startActivity(intent);
                                MainActivity.this.finish();
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
                        map.put("account", UserId);
                        map.put("password","Fb_pwd");
                        return map;
                    }

                };
                mQueue.add(getRequest);
            }

            @Override
            public void onCancel() {
                textView.setText("Login Cancelled");
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.login_dialog, null);
                Button changeUser = (Button) mView.findViewById(R.id.changeUser);
                Button cancel = (Button) mView.findViewById(R.id.cancel);
                changeUser.setOnClickListener(new View.OnClickListener()  {
                                              @Override
                                              public void onClick(View view){
                                                  LoginManager.getInstance().logOut();
                                                  loginButton.performClick();

                                              }

                                          }
                );
                cancel.setOnClickListener(new View.OnClickListener()  {
                                                  @Override
                                                  public void onClick(View view){
                                                      Intent intent = new Intent();
                                                      intent.setClass(MainActivity.this, MainActivity.class);
                                                      startActivity(intent);

                                                  }

                                              }
                );
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }

            @Override
            public void onError(FacebookException error) {
                textView.setText("Login Error");
            }
        });



        Button b_login = (Button) findViewById(R.id.b_login); //登入按鈕
        Button b_register = (Button) findViewById(R.id.b_register);   //註冊按鈕
        mQueue = Volley.newRequestQueue(this);

        account = (EditText) findViewById(R.id.AccountID); //輸入的帳號
        password = (EditText) findViewById(R.id.Password); //輸入的密碼
        login_check1=(CheckBox) findViewById(R.id.login_check1);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());  //隱藏密碼
        account.setText(name_str);
        password.setText(pass_str);
        login_check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    SharedPreferences remdname=getPreferences(Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit=remdname.edit();
                    edit.putString("name", account.getText().toString());
                    edit.putString("pass", password.getText().toString());
                    edit.commit();
                }
                if(!isChecked)
                {

                    SharedPreferences remdname=getPreferences(Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit=remdname.edit();
                    edit.putString("name", "");
                    edit.putString("pass", "");
                    edit.commit();
                }
            }
        });

        b_login.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(login_check1.isChecked())//檢測使用者名密碼
                {
                    SharedPreferences remdname=getPreferences(Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit=remdname.edit();
                    edit.putString("name", account.getText().toString());
                    edit.putString("pass", password.getText().toString());
                    edit.commit();
                }

                getRequest = new StringRequest(Request.Method.POST,mUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                //msg.setText(s.trim());

                                String checke = "accountID: "+account.getText().toString();
                                String checkp = "password: "+password.getText().toString();
                                String checkn = "nickname: "+account.getText().toString();
                                int email_begindex = s.indexOf("accountID: ") + 11;
                                int email_endindex = s.indexOf("nickname: ") -1;
                                int begindex = s.indexOf("nickname: ") + 10;
                                int endindex = s.indexOf("password: ")-1;
                                String emailID = s.substring(email_begindex,email_endindex).trim();
                                String nickname = s.substring(begindex,endindex).trim();
                                msg.setText(nickname);


                                if(s.trim().indexOf(checkn) != -1 || s.trim().indexOf(checke) != -1 && s.trim().indexOf(checkp) != -1){


                                    //intent.setClass(MainActivity.this, MyuploadMusic.class);


                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this,MapsActivity.class);
                                    //儲存帳號
                                    Bundle bundle = new Bundle();
                                    bundle.putString("AccountID",account.getText().toString() );
                                    //將Bundle物件assign給intent
                                    intent.putExtras(bundle);
                                    //Intent intent2 = new Intent();
                                    //intent2.setClass(MainActivity.this,MainPageActivity.class);
                                    XclSingleton.getInstance().put("Email",emailID);
                                    XclSingleton.getInstance().put("AccountID",nickname);
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


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }







}
