package com.example.user.logintest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.logintest.MySQLConnection.Post;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient Client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public  static  final  int REQUEST_LOCATION_CODE = 99;

    LocationsDatabase myDatabase;
    private ArrayList<Locations> locationArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        LatLng taipei_1 = new LatLng(25.0327792, 121.5636894);
        googleMap.addMarker(new MarkerOptions().position(taipei_1)
                .title("Marker in 台北101"));


        // add markers
       myDatabase = new LocationsDatabase(MapsActivity.this);
       locationArrayList=myDatabase.getLocations();
       for(int i=0;i<locationArrayList.size();i++) {
          LatLng test = new LatLng(locationArrayList.get(i).lat, locationArrayList.get(i).lng);
           Marker perth = mMap.addMarker(new MarkerOptions()
                    .position(test));
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
        LatLng lating = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(lating);
        markerOptions.title("Current Location:");
        //markerOptions.snippet("can add snippet right under title");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMarker = mMap.addMarker(markerOptions);
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
                TextView tv_intro = (TextView)mView.findViewById(R.id.tv_intro);
                final String pos = marker.getPosition().toString() ;
                String lat = String.valueOf(marker.getPosition().latitude) ;
                String lng = String.valueOf(marker.getPosition().longitude) ;
                final String post = Post(lat, lng);
                tv_intro.setText(post) ;
                mLogin.setOnClickListener(new View.OnClickListener()  {
                                              @Override
                                              public void onClick(View view){
                                                  Toast.makeText(MapsActivity.this,
                                                          pos,
                                                          Toast.LENGTH_SHORT).show();
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
            String location = tf_location.getText().toString();
            for(int i =0;i<locationArrayList.size();i++){
                if(location.equals(locationArrayList.get(i).name.replaceAll("[a-zA-z]+","")) || location.equals(locationArrayList.get(i).name.replaceAll("[^a-zA-z]+",""))){
                    LatLng s_research = new LatLng(locationArrayList.get(i).lat,locationArrayList.get(i).lng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(s_research));
                    mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
                }
            }
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
}
