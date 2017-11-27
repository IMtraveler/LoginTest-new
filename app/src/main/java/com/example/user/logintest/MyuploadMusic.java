package com.example.user.logintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.logintest.Adapter.NormalExpandableListAdapter;
import com.example.user.logintest.Constant;
import com.example.user.logintest.R;
import com.example.user.logintest.Adapter.onGroupExpandedListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyuploadMusic extends AppCompatActivity {
    String post="";
    private ListView listView;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myupload_music);

        String account = XclSingleton.getInstance().get("AccountID").toString();
        //ID:帳號
        //連接php，將相關的音檔載下來
        String phpURL = "http://140.112.107.125:47155/html/myMusic.php" ;

        try {
            post = new MyMusicAsyncTask().execute(account, phpURL).get();
            if(post.trim().equals("")){
                post="你沒有上傳的音檔";
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        final String[] AudioList = post.split(";");
        final String[] NameList = new String[AudioList.length-1];
        final String[] introList = new String[AudioList.length-1];
        final String[] typeList = new String[AudioList.length-1];
        final String[] CTR = new String[AudioList.length-1];
        final String[][] content = new String[AudioList.length-1][3];

        for(int i=0;i<AudioList.length-1;i++){
            int index1 = AudioList[i].indexOf("audioName:");
            int index2 = AudioList[i].indexOf("intro:");
            int index3 = AudioList[i].indexOf("type:");
            int index4 = AudioList[i].indexOf("CTR:");
            NameList[i]=AudioList[i].substring(index1+10,index2);
            introList[i]="簡介:\n"+AudioList[i].substring(index2+6,index3);
            typeList[i]="類別:    "+AudioList[i].substring(index3+5,index4);
            CTR[i]="瀏覽次數:   "+AudioList[i].substring(index4+4);
            content[i][0]=introList[i];
            content[i][1]=typeList[i];
            content[i][2]=CTR[i];
        }

        final ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandable_list);
        final NormalExpandableListAdapter adapter = new NormalExpandableListAdapter(NameList, content);
        adapter.setOnGroupExpandedListener(new onGroupExpandedListener() {

            @Override
            public void onGroupExpanded(int groupPosition) {
                expandOnlyOne(listView, groupPosition, NameList.length);
            }
        });

        listView.setAdapter(adapter);
        //  设置分组项的点击监听事件
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
               // Log.d(TAG, "onGroupClick: groupPosition:" + groupPosition + ", id:" + id);
                // 请务必返回 false，否则分组不会展开
                return false;
            }
        });

        //  设置子选项点击监听事件
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(MyuploadMusic.this, content[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    // 每次展开一个分组后，关闭其他的分组
    private boolean expandOnlyOne(ExpandableListView view, int expandedPosition, int groupLength) {
        boolean result = true;
        for (int i = 0; i < groupLength; i++) {
            if (i != expandedPosition && view.isGroupExpanded(i)) {
                result &= view.collapseGroup(i);
            }
        }
        return result;
    }
        /*listView = (ListView)findViewById(R.id.list_view);
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,NameList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //這裡應該要顯示點下去應該要出現的頁面
                Toast.makeText(getApplicationContext(), "你上傳的音檔名稱是:  " + NameList[position], Toast.LENGTH_SHORT).show();
            }
        });*/
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
                intent.setClass(MyuploadMusic.this,FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.我上傳的音檔:
                //跳到上傳音檔頁，顯示上傳的音檔名字
                // TODO Auto-generated method stub
                Intent intent2 = new Intent();
                intent2.setClass(MyuploadMusic.this,MyuploadMusic.class);
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
                intent6.setClass(MyuploadMusic.this,MyAccountActivity.class);
                startActivity(intent6);
                break;
            case R.id.條列式瀏覽:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent();
                intent5.setClass(MyuploadMusic.this,SpotView.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
