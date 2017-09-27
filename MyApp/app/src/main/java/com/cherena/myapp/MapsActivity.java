package com.cherena.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.request.*;
import com.akexorcist.googledirection.request.DirectionRequest;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//imports for laction
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    //entry points to places api
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    //fused location provider entry point
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Dublin) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-53.3498, -6.2603);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

//    private static final String API_KEY = "&apiKey=089c7b4cf8de20a2b563d9e016d6b73563996885";
//    private static final String URL = "https://api.jcdecaux.com/vls/v1/stations?contract=Dublin" + API_KEY;

    private RequestQueue requestQueue;

    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ApiUtil.getInstance(this);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

//    private void fetchStations(){
//        StringRequest request = new StringRequest(Request.Method.GET, URL, onInfoLoaded, onInfoError);
//        requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(request);
//    }
//
//    private final Response.Listener<String> onInfoLoaded = new Response.Listener<String>(){
//        @Override
//        public void onResponse(String response){
//            List<BikeStation> stations = Arrays.asList(gson.fromJson(response, BikeStation[].class));
//
//            updateBikeLocations(stations);
//
//            for(BikeStation station : stations){
//
//                Log.i("MapsActivity", station.Name + ": " + station.Address + " : " + station.AvailableBikes);
//            }
//        }
//    };
//
//    private final Response.ErrorListener onInfoError = new Response.ErrorListener(){
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            Log.e("PostActivity", error.toString());
//        }
//    };
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
                ApiUtil.getInstance().fetchStations(new RequestListener<List<BikeStation>>(){
                @Override
                public void getResult(List<BikeStation> object) {
                    if(!object.isEmpty()){
                        for (BikeStation station : object) {
                            LatLng s = new LatLng(station.Position.lat, station.Position.lng);
//                            String availableB = Integer.toString(station.AvailableBikes);
                            mMap.addMarker(new MarkerOptions()
                                    .position(s)
                                    .title("Available: " + Integer.toString(station.AvailableBikes)));
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {

                                 LatLng destination = marker.getPosition();
                                 LatLng origin =  returnDeviceLocation();
                                 com.akexorcist.googledirection.request.DirectionRequest dr;
                                 try{
                                     dr = new DirectionRequest("AIzaSyCC2T5COdYWmNhYF43BYpD0IZTJtXjQ3lI", origin, destination)
                                        .transportMode(TransportMode.WALKING);
                                     dr.execute(new DirectionCallback() {
                                         @Override
                                         public void onDirectionSuccess(Direction direction, String rawBody) {

                                             String status = direction.getStatus();

                                             if(status.equals(RequestResult.OK))
                                                placeDirection(direction);

                                            }

                                            @Override
                                            public void onDirectionFailure(Throwable t) {
                                                t.printStackTrace();

                                            }
                                        });

                                    } catch(SecurityException e)  {
                                        Log.e("Exception: %s", e.getMessage());
                                    }
                                 return false;
                                }
                            });

                        }
                    }

                }
            });
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
//        updateBikeLocations();


        //Add a marker for top result in station list
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        String value = sp.getString("Result", "Default");

//        for(BikeStation station : stations){
//            LatLng s = new LatLng(station.Position.lat, station.Position.lng);
//
//            mMap.addMarker(new MarkerOptions().position(s).title(station.Name));
//            //mMap.moveCamera(CameraUpdateFactory.newLatLng(station));
//        }

//         Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void updateBikeLocations(List<BikeStation> allStations){
        if(mMap != null){
//            ApiUtil.getInstance().fetchStations(new RequestListener<List<BikeStation>>(){
//                @Override
//                public void getResult(List<BikeStation> object) {
//                    if(!object.isEmpty()){
//                        for (BikeStation station : allStations) {
//                            LatLng s = new LatLng(station.Position.lat, station.Position.lng);
//                            mMap.addMarker(new MarkerOptions().position(s).title(station.Name));
//                        }
//                    }
//
//                }
//            });
        for (BikeStation station : allStations) {
            LatLng s = new LatLng(station.Position.lat, station.Position.lng);
            mMap.addMarker(new MarkerOptions().position(s).title(station.Name));
        }
        }


    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    //Handles the result of the request for location permissions.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private LatLng returnDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
        return new LatLng(mLastKnownLocation.getLatitude(),
                mLastKnownLocation.getLongitude());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    //list to hold polyline as they are created to allow for removal
    List<Polyline> Polylines = new ArrayList<>();

    public void placeDirection(Direction direction) {
        // Remove polylines from map
        for (Polyline polyline : Polylines) {
            polyline.remove();
        }
        // Clear polyline array
        Polylines.clear();
        Route route = direction.getRouteList().get(0);
        Leg leg = route.getLegList().get(0);
        ArrayList<LatLng> pointList = leg.getDirectionPoint();
        PolylineOptions polylineOptions = DirectionConverter.createPolyline(getApplicationContext(), pointList, 5, Color.BLUE);
        Polylines.add(mMap.addPolyline(polylineOptions));

    }


}

