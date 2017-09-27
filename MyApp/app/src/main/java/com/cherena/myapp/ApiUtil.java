package com.cherena.myapp;

import android.content.Context;
import android.util.Log;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.model.Direction;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cherena on 26/09/2017.
 */

public class ApiUtil
{
    private static final String TAG = "ApiUtil";
    private static ApiUtil instance = null;

    private static final String API_KEY = "&apiKey=089c7b4cf8de20a2b563d9e016d6b73563996885";
    private static final String URL = "https://api.jcdecaux.com/vls/v1/stations?contract=Dublin" + API_KEY;

    //for Volley API
    public RequestQueue requestQueue;


    private ApiUtil(Context context)
    {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());

    }

    public static synchronized ApiUtil getInstance(Context context)
    {
        if (null == instance)
            instance = new ApiUtil(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized ApiUtil getInstance()
    {
        if (null == instance)
        {
            throw new IllegalStateException(ApiUtil.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void fetchStations(final RequestListener<List<BikeStation>> listener) {

        final Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                List<BikeStation> stations = Arrays.asList(gson.fromJson(response, BikeStation[].class));
//                    updateBikeLocations(allStations);
                if (stations != null) {
                    listener.getResult(stations);
                }

                for (BikeStation station : stations) {

                    Log.i("MapsActivity", station.Name + ": " + station.Address + " : " + station.AvailableBikes);
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PostActivity", "Error response code : " + error.networkResponse.statusCode);
                listener.getResult(null);
            }
        });





        requestQueue.add(request);
    }



//    public void fetchStations(Object param1, final RequestListener<String> listener)
//    {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gson = gsonBuilder.create();
//
//        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>(){
//            @Override
//            public void onResponse(String response) {
//                List<BikeStation> stations = Arrays.asList(gson.fromJson(response, BikeStation[].class));
//
////original method for accessing info     updateBikeLocations(allStations);
//                if()
//
//                for(BikeStation station : stations){
//
//                    Log.i("MapsActivity", station.Name + ": " + station.Address + " : " + station.AvailableBikes);
//                }
//            }
//         }, new ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError error){
//                Log.e("PostActivity", error.toString());
//            }
//
//        });

        // because its alreadya t the bottom requestQueue.add(request);



//        private final ErrorListener onInfoError = new ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("PostActivity", error.toString());
//            }
//        };



//        Map<String, Object> jsonParams = new HashMap<>();
//        jsonParams.put("param1", param1);
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response)
//                    {
//                        Log.d(TAG + ": ", "somePostRequest Response : " + response.toString());
//                        if(null != response.toString())
//                            listener.getResult(response.toString());
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//                        if (null != error.networkResponse)
//                        {
//                            Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
//                            listener.getResult(false);
//                        }
//                    }
//                });

//        requestQueue.add(request);
//    }
}
