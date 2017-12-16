package com.example.user.logintest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MyAccountActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    TextView textView;
    private Switch switch1;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Button btn_toMain = (Button)findViewById(R.id.btn_toMain);
        btn_toMain.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(MyAccountActivity.this,MainActivity.class);
                startActivity(intent);
                MyAccountActivity.this.finish();
            }
        });
        String account = XclSingleton.getInstance().get("AccountID").toString();
        //nickname
        String emailid = XclSingleton.getInstance().get("Email").toString();
        //email
        final TextView simpleTextView = (TextView) findViewById(R.id.account_id); //get the id for TextView
        simpleTextView.setText("My Nickname: " + account);
        final TextView simpleTextView2 = (TextView) findViewById(R.id.emailid); //get the id for TextView
        simpleTextView2.setText("My Email: " + emailid);
        textView = (TextView) findViewById(R.id.textView);
        switch1 = (Switch) findViewById(R.id.switch1);
        sharedPrefs = getSharedPreferences("data", MODE_PRIVATE);
        switch1.setChecked(sharedPrefs.getBoolean("data", false));
        switch1.setOnCheckedChangeListener(this);
    }
    @Override
    public void onCheckedChanged(CompoundButton CompoundButton, boolean b) {
        final Global check = (Global)getApplicationContext();
        if(switch1.isChecked()){
            check.setWord(0);
            textView.setText("目前為導遊模式");
            sharedPrefs.edit().putBoolean("data", true).commit();
        }else {
            check.setWord(1);
            textView.setText("目前為遊客模式");
            sharedPrefs.edit().putBoolean("data", false).commit();
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
                intent.setClass(MyAccountActivity.this,FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.我上傳的音檔:
                //跳到上傳音檔頁，顯示上傳的音檔名字
                // TODO Auto-generated method stub
                Intent intent2 = new Intent();
                intent2.setClass(MyAccountActivity.this,MyuploadMusic.class);
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
                intent6.setClass(MyAccountActivity.this,MyAccountActivity.class);
                startActivity(intent6);
                break;
            case R.id.條列式瀏覽:
                //Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent();
                intent5.setClass(MyAccountActivity.this,SpotView.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
