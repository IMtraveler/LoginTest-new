package com.example.user.logintest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SpotView extends AppCompatActivity {
    LocationsDatabase myDatabase;
    private ArrayList<Locations> locationArrayList;
    private String[] locations ;
    ListView listView;
    SearchView searchView;
    private ArrayAdapter<String> listAdapter;
    Bundle bundle = new Bundle();



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
        searchView = (SearchView)findViewById(R.id.SearchView);
        listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,locations);
        listView.setAdapter(listAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //locationArrayList=myDatabase.getsearch(query);
                //for(int i=0 ;i<locationArrayList.size();i++){
                //    locations[i]=locationArrayList.get(i).name;
                //}
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lat = String.valueOf(locationArrayList.get(position).lat) ;
                String lng = String.valueOf(locationArrayList.get(position).lng) ;
                Toast.makeText(getApplicationContext(),"lat: "+lat+" lng: "+lng,Toast.LENGTH_SHORT).show();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SpotView.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog, null);
                Button mLogin = (Button) mView.findViewById(R.id.buttona);
                TextView tv_intro = (TextView)mView.findViewById(R.id.tv_name);
                TextView tv_types = (TextView)mView.findViewById(R.id.tv_type);
                ImageView imageView = (ImageView)mView.findViewById(R.id.imageView);
                String phpURL = "http://140.112.107.125:47155/html/test.php" ;

                bundle.putString("lat", lat);
                bundle.putString("lng", lng);
                String post = "";
                try {
                    post = new MyAsyncTask().execute(lat, lng, phpURL).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                bundle.putString("post", post);

                if (post.length() > 10) {
                    int begName = post.indexOf("name:");
                    int endName = post.indexOf("altHead_name");
                    int begType = post.indexOf("class_tag");
                    int endType = post.indexOf("lat");

                    String type = post.substring(begType + 10, endType - 1);
                    tv_types.setText(type);
                    bundle.putString("type", type);

                    String name = post.substring(begName + 5, endName - 1);
                    tv_intro.setText(name);
                    bundle.putString("name", name);
                    int begImg = post.indexOf("image");
                    int endIndex = post.length();

                    if ((begImg + 6) >= endIndex - 5) {
                        Picasso.with(getBaseContext()).load("http://140.112.107.125:47155/html/uploaded/null.png").into(imageView);
                        bundle.putString("imgURL", "http://140.112.107.125:47155/html/uploaded/null.png");
                    } else {
                        String imgURL = "http:" + post.substring(begImg + 6, endIndex);
                        imgURL = imgURL.replaceAll(" ", "");
                        Picasso.with(getBaseContext()).load(imgURL).into(imageView);
                        bundle.putString("imgURL", imgURL);
                    }
                }
                else{
                    bundle.putString("imgURL","http://140.112.107.125:47155/html/uploaded/null.png");
                    tv_intro.setText("no data");
                    bundle.putString("name", "no data");
                }
                mLogin.setOnClickListener(new View.OnClickListener()  {
                                              @Override
                                              public void onClick(View view){
                                                  Intent intent = new Intent();
                                                  intent.setClass(SpotView.this, AttractionsActivity.class);
                                                  intent.putExtras(bundle);
                                                  startActivity(intent);
                                              }

                                          }
                );
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });

    }



}
