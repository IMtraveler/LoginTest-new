package com.example.user.logintest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent ;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.database.Cursor ;
import android.widget.SimpleCursorAdapter ;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class FavoriteActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> locations=new ArrayList<>() ;
    private ArrayList<String> fav_locations=new ArrayList<>() ;
    Bundle bundle = new Bundle();
    LocationsDatabase myFAVDatabase;
    private ArrayList<Locations> locationArrayList=new ArrayList<>();
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        myFAVDatabase = new LocationsDatabase(FavoriteActivity.this);
        locationArrayList=myFAVDatabase.getLocations();
        for (int k =0;k<locationArrayList.size();k++){
            locations.add(locationArrayList.get(k).name);
        }
        Log.e("LocationArrayList", String.valueOf(locationArrayList.size()));
        Log.e("locationArraylist[1]",locationArrayList.get(1).name);
        listview = (ListView) findViewById(R.id.listview);
        SQLiteHelper helper =SQLiteHelper.getInstance(this);
        Cursor res = helper.getAllData();
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
        final String[] str= new String [j];
        // final String[] lat_str=new String[j];
        //final String[] lng_str=new String[j];
        //android.R.layout.simple_list_item_1 為內建樣式，還有其他樣式可自行研究
        int i=0;
        while (res.moveToNext()) {
            str[i] = res.getString(1);
            fav_locations.add(res.getString(1));
            //lat_str[i]=res.getString(2);
            //lng_str[i]=res.getString(3);
            i++;
            //   Log.d("here", fav_locations.get(i) +"is in the favorite db");
        }

        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,str);
        listview.setAdapter(adapter);
        // Show all data
        //showMessage("我最愛的景點",buffer.toString());

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get position
                //Toast.makeText(getApplicationContext(),adapter.getItem(position),Toast.LENGTH_SHORT).show();
                Log.e("fav_locations_name",adapter.getItem(position).trim());
                //Toast.makeText(getApplicationContext(),listAdapter.getItem(position),Toast.LENGTH_SHORT).show();

                position=locations.indexOf(adapter.getItem(position).trim());
                Log.e("msg", String.valueOf(position));
                String lat = String.valueOf(locationArrayList.get(position).lat) ;
                String lng = String.valueOf(locationArrayList.get(position).lng) ;
                //    String lat = String.valueOf(locationArrayList.get(locationArrayList.indexOf(adapter.getItem(position))).lat) ;
                //   String lng = String.valueOf(locationArrayList.get(locationArrayList.indexOf(adapter.getItem(position))).lng) ;
                //         String lng = String.valueOf(lng_str[position]) ;

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FavoriteActivity.this);
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
                        //Picasso.with(getBaseContext()).load("http://140.112.107.125:47155/html/uploaded/null.png").into(imageView);
                        bundle.putString("imgURL", "http://140.112.107.125:47155/html/uploaded/null.png");
                    } else {
                        String imgURL = post.substring(begImg + 6, endIndex);
                        imgURL = "http:" + imgURL.replaceAll(" ", "");
                        Picasso.with(FavoriteActivity.this).load(imgURL).into(imageView);
                      //  Log.e("img_url",imgURL);
                       // Log.e("test","test");
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
                                                  intent.setClass(FavoriteActivity.this, AttractionsActivity.class);
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
                intent.setClass(FavoriteActivity.this,FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.我上傳的音檔:
                //跳到上傳音檔頁，顯示上傳的音檔名字
                // TODO Auto-generated method stub
                Intent intent2 = new Intent();
                intent2.setClass(FavoriteActivity.this,MyuploadMusic.class);
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
                intent6.setClass(FavoriteActivity.this,MyAccountActivity.class);
                startActivity(intent6);
                break;
            case R.id.條列式瀏覽:
                //Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent();
                intent5.setClass(FavoriteActivity.this,SpotView.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

  /*  public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }*/
}
