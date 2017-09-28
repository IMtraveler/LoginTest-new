package com.example.user.logintest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent ;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.database.Cursor ;
import android.widget.SimpleCursorAdapter ;
import android.widget.TextView;


public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        SQLiteHelper helper =SQLiteHelper.getInstance(this);
        Cursor res = helper.getAllData();
        ListView listview = (ListView) findViewById(R.id.listview);
        if(res.getCount() == 0) {
            String [] err={"沒有最愛的景點"};
            ArrayAdapter err_adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1,
                    err);
            listview.setAdapter(err_adapter);
            return;
        }
 //       StringBuffer buffer = new StringBuffer();
        //ListView listview = (ListView) findViewById(R.id.listview);
        //ListView 要顯示的內容
        int j = res.getCount();
        String[] str= new String [j];
        //android.R.layout.simple_list_item_1 為內建樣式，還有其他樣式可自行研究
        int i=0;
        while (res.moveToNext()) {
            str[i] = res.getString(1);
            i++;
        }
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                str);
        listview.setAdapter(adapter);
        // Show all data
        //showMessage("我最愛的景點",buffer.toString());

    }


  /*  public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }*/
}
