package com.example.bas.mobiledev4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //CREATING THE GOOGLE MAPS
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is  Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready!");
        mMap = googleMap; //MAPS definition


        //ASK FOR THE USERS PERMISSION TO SHOW LOCATION
        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,  //BOTH FINE AND COARSE LOCATION DEFINED
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);                       //LOCATION ENABLED TRUE
            mMap.getUiSettings().setTiltGesturesEnabled(true);     //TILT GESTURE
            mMap.getUiSettings().isRotateGesturesEnabled();        //MAP ROTATION
        }
        LatLng rotterdam = new LatLng(51, 49);              //placing a marker in Rotterdam city
        mMap.addMarker(new MarkerOptions().position(rotterdam).title("Marker in Rotterdam"));


    }

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;           //FINE LOCATION
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;       //COARSE LOCATION
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;                               //PERMISSION REQUEST CODE
    private static final float DEFAULT_ZOOM = 15f;                                                  //DEFAULT ZOOM

    //vars
    private Boolean mLocationPermissionsGranted = false;                                            //CHECK IF THE LOCATION PERMISSION IS GRANTED
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment_3);                                                                         //CREATE ON TAB 3
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);    //MAPFRAGMENTS
        mapFragment.getMapAsync(this);

        getLocationPermission();                                                                                         //GET PERMISSION
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                                                                                                                //GET CURRENT LOCATION

        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();

            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {                                                           //WHEN LOCATION IS FOUND
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: Found your location ");
                        Location currentLocation = (Location) task.getResult();

                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude() ),
                                DEFAULT_ZOOM);                                                                   //DEFAULT CAMERA

                    }else {
                        Log.d(TAG, "onComplete: Could NOT find your location");
                        Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show(); //ERROR HANDLER
                    }
                }

            });

            }

        }catch(SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );                                 //EXCEPTIONS
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat:"  + latLng.latitude + ", lng: " + latLng.longitude);     //MOVING THE CAMERA
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }


    private void initMap() {
        Log.d(TAG, "initMap: Initializing map ");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);           //INITIALISING THE MAP
        mapFragment.getMapAsync(MapsActivity.this);

    }


    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location Permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,                                                       //GETTING LOCATION PERMISSION
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

                                                                                                                        //WHEN GRANTED YOU'LL SEE THE MAP
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called ");
        mLocationPermissionsGranted = false;                                                                                //CHECK FOR THE PERMISSIONS

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;

                }
            }

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
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
}
