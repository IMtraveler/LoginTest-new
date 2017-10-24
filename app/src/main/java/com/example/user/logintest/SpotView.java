package com.example.user.logintest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import java.util.ArrayList;

public class SpotView extends AppCompatActivity {
    LocationsDatabase myDatabase;
    private ArrayList<Locations> locationArrayList;
    private String[] locations ;
    ListView listView;
    private ArrayAdapter<String> listAdapter;
    ArrayAdapter mSearchAdapter;
    ArrayList<String> mSearchList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView searchbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_view);
        myDatabase = new LocationsDatabase(SpotView.this);
        locationArrayList=myDatabase.getLocations();
        locations = new String[locationArrayList.size()];
        for (int i =0;i<locationArrayList.size();i++){
            locations[i]=locationArrayList.get(i).name;
        }
        listView = (ListView)findViewById(R.id.spot_list);
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,locations);
        listView.setAdapter(listAdapter);
       // adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,locations);
        searchbar = (AutoCompleteTextView) findViewById(R.id.seachbar);
        searchbar.setThreshold(1);
        searchbar.setAdapter(listAdapter);
    }



}
