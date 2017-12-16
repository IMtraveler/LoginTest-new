package com.example.user.logintest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private ArrayList<Locations> locationArrayList=new ArrayList<>();

    //private ArrayList<String>spotlist=new ArrayList<>();
    private ArrayAdapter<String> listAdapter;

    private ArrayList<String> locations=new ArrayList<>() ;
    ListView listView;
    SearchView searchView;

    Bundle bundle = new Bundle();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_view);
        myDatabase = new LocationsDatabase(SpotView.this);
        locationArrayList=myDatabase.getLocations();


        for (int i =0;i<locationArrayList.size();i++){
            locations.add(locationArrayList.get(i).name);
        }

        //fillData();
        listView = (ListView)findViewById(R.id.spot_list);
        searchView = (SearchView)findViewById(R.id.SearchView);
        listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,locations);
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
                //get position
                //Toast.makeText(getApplicationContext(),listAdapter.getItem(position),Toast.LENGTH_SHORT).show();
                position=locations.indexOf(listAdapter.getItem(position));
                String lat = String.valueOf(locationArrayList.get(position).lat) ;
                String lng = String.valueOf(locationArrayList.get(position).lng) ;

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SpotView.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog, null);
                Button mLogin = (Button) mView.findViewById(R.id.buttona);
                TextView tv_intro = (TextView)mView.findViewById(R.id.tv_name);
                TextView tv_types = (TextView)mView.findViewById(R.id.tv_type);
                ImageView imageView = (ImageView)mView.findViewById(R.id.imageView);
                String phpURL = "http://140.112.107.125:47155/html/getdata.php" ;

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
                    int endEName = post.indexOf("address");
                    int begType = post.indexOf("class_tag");
                    int endType = post.indexOf("lat");
                    int begClass = post.indexOf("classified");
                    int endClass = post.indexOf("audioNum");
                    String classi_str = post.substring(begClass + 11, endClass-1);
                    int classi =  Integer.valueOf(classi_str.trim().replaceAll(" ", ""));

                    String type = post.substring(begType + 10, endType - 1);
                    tv_types.setText(type);
                    bundle.putString("type", type);
                    String name = post.substring(begName + 5, endName - 1);
                    tv_intro.setText(name);
                    bundle.putString("name", name);

                    String ename = post.substring(endName + 13, endEName - 1);
                    bundle.putString("ename", ename);
                    String addr = post.substring(endEName + 7, begType - 1);
                    bundle.putString("addr", addr);
                    int begImg = post.indexOf("image");
                    int endIndex = post.indexOf("classified")-1;
                    String audioNumStr =post.substring(post.indexOf("audioNum:")+10, post.length());
                    int audioNum =  Integer.valueOf(audioNumStr.trim().replaceAll("/n ", ""));
                    bundle.putInt("audioNum", audioNum);
                    bundle.putInt("classifi", classi);
                    if ((begImg + 6) >= endIndex - 5) {
                       // Picasso.with(getBaseContext()).load("http://140.112.107.125:47155/html/uploaded/null.png").into(imageView);
                        bundle.putString("imgURL", "http://140.112.107.125:47155/html/uploaded/null.png");
                    } else {
                        String imgURL = post.substring(begImg + 6, endIndex);
                        imgURL = "http:" + imgURL.replaceAll(" ", "");
                        Picasso.with(SpotView.this).load(imgURL).into(imageView);
                        //Log.e("img_url",imgURL);
                        //Log.e("test","test");
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
    /*
    private void fillData(){
        for (int i =0;i<locationArrayList.size();i++){
            //name, tyoe, lart, lng
            GetSpot s = new GetSpot(locationArrayList.get(i).name.toString(),locationArrayList.get(i).type.toString(),Double.toString(locationArrayList.get(i).lat),Double.toString(locationArrayList.get(i).lng));
            spotlist.add(s);
        }

    }*/
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
                intent.setClass(SpotView.this,FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.我上傳的音檔:
                //跳到上傳音檔頁，顯示上傳的音檔名字
                // TODO Auto-generated method stub
                Intent intent2 = new Intent();
                intent2.setClass(SpotView.this,MyuploadMusic.class);
                //儲存帳號
                Bundle bundle = new Bundle();
                bundle.putString("AccountID",UserID);
                //將Bundle物件assign給intent
                intent2.putExtras(bundle);
                startActivity(intent2);
                //顯示按鈕的名字
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.我的帳戶:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent();
                intent6.setClass(SpotView.this,MyAccountActivity.class);
                startActivity(intent6);
                break;
            case R.id.條列式瀏覽:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent();
                intent5.setClass(SpotView.this,SpotView.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
