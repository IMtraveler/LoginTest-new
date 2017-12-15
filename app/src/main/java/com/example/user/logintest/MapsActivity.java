package com.example.user.logintest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent ;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.squareup.picasso.Picasso ;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient Client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    double latitude,longitude;
    public  static  final  int REQUEST_LOCATION_CODE = 99;
    Bundle bundle = new Bundle();

    LocationsDatabase myDatabase;

    private ArrayList<Locations> locationArrayList;
    private ArrayList<Locations> searchArrayList;

    private String[] locations ;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView tf_location;
    String input=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        myDatabase = new LocationsDatabase(MapsActivity.this);
        locationArrayList=myDatabase.getLocations();
        tf_location = (AutoCompleteTextView) findViewById(R.id.TF_location);


        //searchArrayList=myDatabase.getsearch("故");
       /* tf_location.addTextChangedListener(new  TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("beforeTextChanged");
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                System.out.println("onTextChanged");
                //tf_location.setText(tf_location.getText());
                input = tf_location.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                System.out.println("afterTextChanged");

            }
        });*/
        //searchArrayList=myDatabase.getsearch(input);
        locations = new String[locationArrayList.size()];
        //所有景點的list
        for (int i =0;i<locationArrayList.size();i++){
            locations[i]=locationArrayList.get(i).name;
        }
        adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,locations);
        tf_location.setThreshold(1);
        tf_location.setAdapter(adapter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //permission is granted
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if (Client == null)
                        {
                            buildGoogleApiClient();

                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else //permission
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    List<Marker> classified1 = new ArrayList<>();
    List<Marker> classified2 = new ArrayList<>();
    List<Marker> classified3 = new ArrayList<>();
    List<Marker> classified4 = new ArrayList<>();
    List<Marker> classified5 = new ArrayList<>();
    List<Marker> classified6 = new ArrayList<>();
    List<Marker> classified7 = new ArrayList<>();
    List<Marker> classified8 = new ArrayList<>();
    List<Marker> classified9 = new ArrayList<>();
    List<Marker> classified10 = new ArrayList<>();
    List<Marker> classified11 = new ArrayList<>();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        //初始位置設在公館附近
        GPSTrackerActivity gps;
        gps = new GPSTrackerActivity(this);
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude) , 14.0f) );
        }else{
            //Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
            gps.showSettingsAlert();
        }
        /*
        LatLng taipei_1 = new LatLng(25.0327792, 121.5636894);
        googleMap.addMarker(new MarkerOptions().position(taipei_1)
                .title("Marker in 台北101"));*/


        // add markers
        myDatabase = new LocationsDatabase(MapsActivity.this);
        locationArrayList=myDatabase.getLocations();
        for(int i=0;i<locationArrayList.size();i++) {
            LatLng test = new LatLng(locationArrayList.get(i).lat, locationArrayList.get(i).lng);

           // Marker perth = mMap.addMarker(new MarkerOptions().position(test));
            String classified = locationArrayList.get(i).classified;
            if(classified.equals("1")){
                Marker perk = mMap.addMarker(new MarkerOptions().position(test));
                perk.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("a01", 100 , 100)));
                classified1.add(perk);
            }
            else if(classified.equals("2")){
                Marker perk = mMap.addMarker(new MarkerOptions().position(test));
                perk.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("a02", 100 , 100)));
                classified2.add(perk);
            }
            else if(classified.equals("3")){
              Marker perk = mMap.addMarker(new MarkerOptions().position(test));
              perk.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("a03", 100 , 100)));
              classified3.add(perk);
            }
            else if(classified.equals("4")){
               Marker perk = mMap.addMarker(new MarkerOptions().position(test));
               perk.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("a04", 100 , 100)));
               classified4.add(perk);
            }
            else if(classified.equals("5")){
                Marker perk = mMap.addMarker(new MarkerOptions().position(test));
                perk.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("a05", 100 , 100)));
                classified5.add(perk);
            }
            else if(classified.equals("6")){
                Marker perk = mMap.addMarker(new MarkerOptions().position(test));
                perk.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("a06", 100 , 100)));
                classified6.add(perk);
            }
            else if(classified.equals("7")){
                Marker perk = mMap.addMarker(new MarkerOptions().position(test));
                perk.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("a07", 100 , 100)));
                classified7.add(perk);
            }
            else if(classified.equals("8")){
                Marker perk = mMap.addMarker(new MarkerOptions().position(test));
                perk.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("a08", 100 , 100)));
                classified8.add(perk);
            }
            else if(classified.equals("9")){
                Marker perk = mMap.addMarker(new MarkerOptions().position(test));
                perk.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("a09", 100 , 100)));
                classified9.add(perk);
            }
            else if(classified.equals("10")){
                Marker perk = mMap.addMarker(new MarkerOptions().position(test));
                perk.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("a10", 100 , 100)));
                classified10.add(perk);
            }
            else if(classified.equals("11")){
                Marker perk = mMap.addMarker(new MarkerOptions().position(test));
                perk.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("a11", 100 , 100)));
                classified11.add(perk);
            }

        }

    }

    protected synchronized void buildGoogleApiClient() {
        Client = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).
                build();

        Client.connect();

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }
        /*
        LatLng lating = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(lating);
        markerOptions.title("Current Location:");
        //markerOptions.snippet("can add snippet right under title");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMarker = mMap.addMarker(markerOptions);*/
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(lating));
        //mMap.animateCamera(CameraUpdateFactory.zoomBy(20));


        if (Client != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(Client, locationRequest, this);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog, null);
                //final EditText mEmail = (EditText) mView.findViewById(R.id.etEmail);
                //final EditText mPassword = (EditText) mView.findViewById(R.id.etpassword);
                Button mLogin = (Button) mView.findViewById(R.id.buttona);
                //ProgressBar mProgressBar = (ProgressBar) mView.findViewById(R.id.progressBar2);
                TextView tv_intro = (TextView)mView.findViewById(R.id.tv_name);
                TextView tv_types = (TextView)mView.findViewById(R.id.tv_type);

                ImageView imageView = (ImageView)mView.findViewById(R.id.imageView);

                final String locating = marker.getPosition().toString() ;
                String lat = String.valueOf(marker.getPosition().latitude) ;
                String lng = String.valueOf(marker.getPosition().longitude) ;
                String phpURL = "http://140.112.107.125:47155/html/getdata.php" ;
                //final String post = Post(lat, lng);
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

                    String classi = post.substring(begClass + 11, endClass-1);
                    int classifi =  Integer.valueOf(classi.trim().replaceAll(" ", ""));
                    bundle.putInt("classifi", classifi);

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
                    if ((begImg + 6) >= endIndex - 5) {
                        //Picasso.with(getBaseContext()).load("http://140.112.107.125:47155/html/uploaded/null.png").into(imageView);
                        bundle.putString("imgURL", "http://140.112.107.125:47155/html/uploaded/null.png");
                    } else {
                        String imgURL = post.substring(begImg + 6, endIndex);
                        imgURL = "http:" + imgURL.replaceAll(" ", "");
                        Picasso.with(MapsActivity.this).load(imgURL).into(imageView);
                        Log.e("img_url",imgURL);
                        Log.e("test","test");
                        bundle.putString("imgURL", imgURL);
                    }
                }
                else{
                    bundle.putString("imgURL","http://140.112.107.125:47155/html/uploaded/null.png");
                    tv_intro.setText("no data");
                    bundle.putString("name", "no data");
                }

/*
                if((begAudio+9) >= (endIndex-5)){
                    String audioURL = "null" ;
                    bundle.putString("audioURL", audioURL);
                }
                else{
                    String audioURL = post.substring(begAudio+9,endIndex);
                    audioURL = audioURL.replaceAll(" ","");
                    bundle.putString("audioURL", audioURL);
                }
*/
                mLogin.setOnClickListener(new View.OnClickListener()  {
                                              @Override
                                              public void onClick(View view){
                                                  Intent intent = new Intent();
                                                  intent.setClass(MapsActivity.this, AttractionsActivity.class);
                                                  intent.putExtras(bundle);
                                                  startActivity(intent);
                                              }

                                          }
                );
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

                return false;
            }
        });



    }
    public void onClick(View v)
    {
        if(v.getId() == R.id.B_search){
            EditText tf_location = (EditText) findViewById(R.id.TF_location);
            String location = tf_location.getText().toString(); //搜尋的字
            searchArrayList=myDatabase.getsearch(location);
            if(searchArrayList.size()==1){
                LatLng s_research = new LatLng(searchArrayList.get(0).lat,searchArrayList.get(0).lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(s_research,20));
            }else if(searchArrayList.size()==0) {
                Toast.makeText(getApplicationContext(),"搜尋不到相關的景點", Toast.LENGTH_SHORT).show();
            }else {
                final AlertDialog.Builder sBuilder = new AlertDialog.Builder(MapsActivity.this);
                View sView = getLayoutInflater().inflate(R.layout.search_dialog, null);
                ListView listView = (ListView)sView.findViewById(R.id.seachList);

                final String[] NameList = new String[searchArrayList.size()];
                for(int i=0 ;i<searchArrayList.size();i++){
                    NameList[i]=searchArrayList.get(i).name;
                }

                ArrayAdapter listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,NameList);
                listView.setAdapter(listAdapter);
                sBuilder.setView(sView);
                final AlertDialog dialog = sBuilder.create();
                dialog.show();


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.cancel();
                        //這裡應該要顯示點下去應該要出現的頁面
                        LatLng s_research = new LatLng(searchArrayList.get(position).lat,searchArrayList.get(position).lng);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(s_research,20));

                    }
                });
            }


           /* for(int i =0;i<searchArrayList.size();i++){
                if(location.equals(searchArrayList.get(i).name.replaceAll("[a-zA-z]+","")) || location.equals(searchArrayList.get(i).name.replaceAll("[^a-zA-z]+",""))){
                    LatLng s_research = new LatLng(searchArrayList.get(i).lat,searchArrayList.get(i).lng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(s_research));
                    mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
                }
            }*/
        }

        /*if(v.getId() == R.id.B_search)
        {
            EditText tf_location = (EditText)findViewById(R.id.TF_location);
            String location = tf_location.getText().toString();
            List<Address> addressList = null;
            MarkerOptions mo = new MarkerOptions();
            if(! location.equals(" "))
            {
                Geocoder geocoder = new Geocoder(this);
                try{
                    addressList = geocoder.getFromLocationName(location,5);
                } catch (IOException e){
                    e.printStackTrace();
                }
                for(int i=0; i< addressList.size();i++)
                {
                    Address myAddress = addressList.get(i);
                    LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                    mo.position(latLng);
                    mo.title("Your search result");
                    mMap.addMarker(mo);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        }*/
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(Client,locationRequest, this);
        }
    }



    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            return  false;

        }
        else
            return  true;
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.layout_menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        Global check = (Global)getApplicationContext();
        int checkk = check.getWord();
        int checkall = check.getall();
        MenuItem registrar = menu.findItem(R.id.我上傳的音檔);
        registrar.setVisible(checkk==0); //if is  guide then is visible
        MenuItem itemcheck1 = menu.findItem(R.id.checkable_item_1);
        MenuItem itemcheck2 = menu.findItem(R.id.checkable_item_2);
        MenuItem itemcheck3 = menu.findItem(R.id.checkable_item_3);
        MenuItem itemcheck4 = menu.findItem(R.id.checkable_item_4);
        MenuItem itemcheck5 = menu.findItem(R.id.checkable_item_5);
        MenuItem itemcheck6 = menu.findItem(R.id.checkable_item_6);
        MenuItem itemcheck7 = menu.findItem(R.id.checkable_item_7);
        MenuItem itemcheck8 = menu.findItem(R.id.checkable_item_8);
        MenuItem itemcheck9 = menu.findItem(R.id.checkable_item_9);
        MenuItem itemcheck10 = menu.findItem(R.id.checkable_item_10);
        MenuItem itemcheck11 = menu.findItem(R.id.checkable_item_11);
        if(checkall==0){
            itemcheck1.setCheckable(false);
            itemcheck2.setCheckable(false);
            itemcheck3.setCheckable(false);
            itemcheck4.setCheckable(false);
            itemcheck5.setCheckable(false);
            itemcheck6.setCheckable(false);
            itemcheck7.setCheckable(false);
            itemcheck8.setCheckable(false);
            itemcheck9.setCheckable(false);
            itemcheck10.setCheckable(false);
            itemcheck11.setCheckable(false);
            check.setall(2);
        }
        else if(checkall==1){
            itemcheck1.setCheckable(true);
            itemcheck2.setCheckable(true);
            itemcheck3.setCheckable(true);
            itemcheck4.setCheckable(true);
            itemcheck5.setCheckable(true);
            itemcheck6.setCheckable(true);
            itemcheck7.setCheckable(true);
            itemcheck8.setCheckable(true);
            itemcheck9.setCheckable(true);
            itemcheck10.setCheckable(true);
            itemcheck11.setCheckable(true);
            check.setall(2);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //取的intent中的bundle物件
        Global check = (Global)getApplicationContext();
        Bundle bundleID =this.getIntent().getExtras();
        int checkall = check.getall();
        String UserID = bundleID.getString("AccountID");
        switch (item.getItemId())
        {
            case R.id.我的最愛:
                //放入點擊後的結果
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this,FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.我上傳的音檔:
                //跳到上傳音檔頁，顯示上傳的音檔名字
                // TODO Auto-generated method stub
                Intent intent2 = new Intent();
                intent2.setClass(MapsActivity.this,MyuploadMusic.class);
                //儲存帳號
                Bundle bundle = new Bundle();
                bundle.putString("AccountID",UserID);
                //將Bundle物件assign給intent
                intent2.putExtras(bundle);
                startActivity(intent2);
                //顯示按鈕的名字
                //Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.setall:
                check.setall(0);
                if(item.isCheckable()==true) {
                    item.setCheckable(false);
                    for (Marker m : classified1) {
                        m.setVisible(false);
                    }
                    for (Marker m : classified2) {
                        m.setVisible(false);
                    }
                    for (Marker m : classified3) {
                        m.setVisible(false);
                    }
                    for (Marker m : classified4) {
                        m.setVisible(false);
                    }
                    for (Marker m : classified5) {
                        m.setVisible(false);
                    }
                    for (Marker m : classified6) {
                        m.setVisible(false);
                    }
                    for (Marker m : classified7) {
                        m.setVisible(false);
                    }
                    for (Marker m : classified8) {
                        m.setVisible(false);
                    }
                    for (Marker m : classified9) {
                        m.setVisible(false);
                    }
                    for (Marker m : classified10) {
                        m.setVisible(false);
                    }
                    for (Marker m : classified11) {
                        m.setVisible(false);
                    }
                }
                else {
                    check.setall(1);
                    item.setCheckable(true);
                    for (Marker m : classified1) {
                        m.setVisible(true);
                    }
                    for (Marker m : classified2) {
                        m.setVisible(true);
                    }
                    for (Marker m : classified3) {
                        m.setVisible(true);
                    }
                    for (Marker m : classified4) {
                        m.setVisible(true);
                    }
                    for (Marker m : classified5) {
                        m.setVisible(true);
                    }
                    for (Marker m : classified6) {
                        m.setVisible(true);
                    }
                    for (Marker m : classified7) {
                        m.setVisible(true);
                    }
                    for (Marker m : classified8) {
                        m.setVisible(true);
                    }
                    for (Marker m : classified9) {
                        m.setVisible(true);
                    }
                    for (Marker m : classified10) {
                        m.setVisible(true);
                    }
                    for (Marker m : classified11) {
                        m.setVisible(true);
                    }
                }
                break;
            case R.id.checkable_item_1:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();

                if(item.isCheckable()==true) {
                    item.setCheckable(false);
                    for (Marker m : classified1) {
                        m.setVisible(false);
                    }
                }
                else {
                    item.setCheckable(true);
                    for (Marker m : classified1) {
                        m.setVisible(true);
                    }
                }

                break;
            case R.id.checkable_item_2:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                if(item.isCheckable()==true) {
                    item.setCheckable(false);
                    for (Marker m : classified2) {
                        m.setVisible(false);
                    }
                }
                else {
                    item.setCheckable(true);
                    for (Marker m : classified2) {
                        m.setVisible(true);
                    }
                }
                break;
            case R.id.checkable_item_3:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                if(item.isCheckable()==true) {
                    item.setCheckable(false);
                    for (Marker m : classified3) {
                        m.setVisible(false);
                    }
                }
                else {
                    item.setCheckable(true);
                    for (Marker m : classified3) {
                        m.setVisible(true);
                    }
                }
                break;
            case R.id.checkable_item_4:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                if(item.isCheckable()==true) {
                    item.setCheckable(false);
                    for (Marker m : classified4) {
                        m.setVisible(false);
                    }
                }
                else {
                    item.setCheckable(true);
                    for (Marker m : classified4) {
                        m.setVisible(true);
                    }
                }
                break;
            case R.id.checkable_item_5:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                if(item.isCheckable()==true) {
                    item.setCheckable(false);
                    for (Marker m : classified5) {
                        m.setVisible(false);
                    }
                }
                else {
                    item.setCheckable(true);
                    for (Marker m : classified5) {
                        m.setVisible(true);
                    }
                }
                break;
            case R.id.checkable_item_6:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                if(item.isCheckable()==true) {
                    item.setCheckable(false);
                    for (Marker m : classified6) {
                        m.setVisible(false);
                    }
                }
                else {
                    item.setCheckable(true);
                    for (Marker m : classified6) {
                        m.setVisible(true);
                    }
                }
                break;
            case R.id.checkable_item_7:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                if(item.isCheckable()==true) {
                    item.setCheckable(false);
                    for (Marker m : classified7) {
                        m.setVisible(false);
                    }
                }
                else {
                    item.setCheckable(true);
                    for (Marker m : classified7) {
                        m.setVisible(true);
                    }
                }
                break;
            case R.id.checkable_item_8:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                if(item.isCheckable()==true) {
                    item.setCheckable(false);
                    for (Marker m : classified8) {
                        m.setVisible(false);
                    }
                }
                else {
                    item.setCheckable(true);
                    for (Marker m : classified8) {
                        m.setVisible(true);
                    }
                }
                break;
            case R.id.checkable_item_9:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                if(item.isCheckable()==true) {
                    item.setCheckable(false);
                    for (Marker m : classified9) {
                        m.setVisible(false);
                    }
                }
                else {
                    item.setCheckable(true);
                    for (Marker m : classified9) {
                        m.setVisible(true);
                    }
                }
                break;
            case R.id.checkable_item_10:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                if(item.isCheckable()==true) {
                    item.setCheckable(false);
                    for (Marker m : classified10) {
                        m.setVisible(false);
                    }
                }
                else {
                    item.setCheckable(true);
                    for (Marker m : classified10) {
                        m.setVisible(true);
                    }
                }
                break;
            case R.id.checkable_item_11:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                if(item.isCheckable()==true) {
                    item.setCheckable(false);
                    for (Marker m : classified11) {
                        m.setVisible(false);
                    }
                }
                else {
                    item.setCheckable(true);
                    for (Marker m : classified11) {
                        m.setVisible(true);
                    }
                }
                break;
            case R.id.我的帳戶:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent();
                intent6.setClass(MapsActivity.this,MyAccountActivity.class);
                startActivity(intent6);
                break;
            case R.id.條列式瀏覽:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent();
                intent5.setClass(MapsActivity.this,SpotView.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
