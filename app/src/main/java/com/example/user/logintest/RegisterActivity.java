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
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private RequestQueue mQueue; //初始化 取得volley的request物件, 建議將mQueue設為單一物件全域使用,避免浪費資源
    private final static String mUrl = "http://140.112.107.125:47155/html/Register.php";
    private StringRequest getRequest;
    private TextView msg3;
    private EditText newaccount;
    private EditText newpassword;
    private EditText newnickname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button b_toregister = (Button) findViewById(R.id.b_toregister);   //註冊按鈕
        mQueue = Volley.newRequestQueue(this);
        msg3 = (TextView) findViewById(R.id.data3) ;
        msg3.setText("歡迎使用註冊系統");
        final boolean[] checkEmail = new boolean[1];


        b_toregister.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
               newaccount = (EditText) findViewById(R.id.newAccountID); //輸入的帳號
               newpassword = (EditText) findViewById(R.id.newPassword); //輸入的密碼
               newnickname = (EditText)findViewById(R.id.newnickname); //輸入的暱稱

               checkEmail[0] = isVaildEmailFormat(newaccount);
                if (checkEmail[0]==true){
                    getRequest = new StringRequest(Request.Method.POST,mUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    msg3.setText(s);
                                    if(s.trim().equals("成功註冊")){
                                        Intent intent = new Intent();
                                        intent.setClass(RegisterActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        RegisterActivity.this.finish();
                                    }
                                }
                            },
                            new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    msg3.setText(volleyError.getMessage());
                                }
                            }){
                        //攜帶參數
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap();
                            map.put("account", newaccount.getText().toString());
                            map.put("nickname",newnickname.getText().toString());
                            map.put("password",newpassword.getText().toString());

                            return map;
                        }

                    };
                    mQueue.add(getRequest);

                }else{
                    msg3.setText("註冊失敗");
                }



               // String check = "Connected successfully[{\"accountID\":\""+account.getText().toString()+"\",\"password\":\""+password.getText().toString()+"\"}]";
                //msg.setText(check);
                //TextView msg2 = (TextView)findViewById(R.id.data2);
                //msg2.setText();

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




    }
    private boolean isVaildEmailFormat(EditText etMail)
    {
        //EditText etMail = (EditText)   findViewById(R.id.edittext_email);
        if (etMail == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(etMail.getText().toString()).matches();
    }


}

