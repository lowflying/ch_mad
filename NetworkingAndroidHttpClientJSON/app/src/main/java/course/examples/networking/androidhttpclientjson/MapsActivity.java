package course.examples.networking.androidhttpclientjson;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    List<LatLng> latLngs = new ArrayList<LatLng>();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(53.3498, 6.2603)));
        NetworkingAndroidHttpClientJSONActivity networking = new NetworkingAndroidHttpClientJSONActivity();
        NetworkingAndroidHttpClientJSONActivity.HttpGetTask task = networking.new HttpGetTask();


        //iterate through the list of locations and add pins to google map
        for (int i = 0; i < latLngs.toArray().length; i++ ){

            LatLng station = latLngs.get(i);
            mMap.addMarker(new MarkerOptions().position(station));
            Log.i("Error", station.toString());
            //mMap.addMarker(new MarkerOptions().getPosition(station));
        }

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {

        // Get your own user name at http://www.geonames.org/login
        private static final String API_KEY = "&apiKey=089c7b4cf8de20a2b563d9e016d6b73563996885";

        private static final String URL = "https://api.jcdecaux.com/vls/v1/stations?contract=Dublin" + API_KEY;

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<String> doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            try {
                return mClient.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (null != mClient)
                mClient.close();
            for (int i = 0; i < result.size(); i++){
                String[] lats = result.get(i).split(",");
                Double lat = Double.parseDouble(lats[0]);
                Double lng = Double.parseDouble(lats[1]);
                latLngs.add(new LatLng(lat, lng));
            }

            //setListAdapter(new ArrayAdapter<String>(
            //        NetworkingAndroidHttpClientJSONActivity.this,
            //        R.layout.list_item, result));

        }
    }
    private class JSONResponseHandler implements ResponseHandler<List<String>> {

        private static final String LONGITUDE_TAG = "lng";
        private static final String LATITUDE_TAG = "lat";
        private static final String BIKE_STANDS_TAG = "bike_stands";
        private static final String OPEN_STANDS_TAG = "available_bike_stands";
        private static final String AVAILABLE_BIKES_TAG = "available_bikes";
        private static final String BIKE_STAND_NAME = "name";

        @Override
        public List<String> handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            List<String> result = new ArrayList<String>();
            String JSONResponse = new BasicResponseHandler()
                    .handleResponse(response);
            try {
                //get top level object, this may couse errors as the top level object seems to be an array
                JSONArray responseArray = (JSONArray) new JSONTokener(JSONResponse).nextValue();

                for(int i=0;i<responseArray.length();i++){
                    JSONObject stationObject = responseArray.getJSONObject(i);
                    JSONObject stationLocation = stationObject.getJSONObject("position");

                    result.add(stationLocation.get("lat") + "," +stationLocation.get("lng"));
                }


                // Extract value of "earthquakes" key -- a List

                //int bikesAvailable = responseObject.getInt(AVAILABLE_BIKES_TAG);
                //JSONArray earthquakes = responseObject
                //		.getJSONArray(EARTHQUAKE_TAG);

                //JSONObject stand = responseObject
                // Iterate over earthquakes list
                //for (int idx = 0; idx < earthquakes.length(); idx++) {

                // Get single earthquake data - a Map
                //	JSONObject earthquake = (JSONObject) earthquakes.get(idx);

                // Summarize earthquake data as a string and add it to
                // result
                //	result.add("Earthquake " + earthquake.getString("eqid") + " was\n"
                //			+ MAGNITUDE_TAG + " = " + earthquake.get(MAGNITUDE_TAG) + "\n"
                //			+ LATITUDE_TAG + " = "	+ earthquake.getString(LATITUDE_TAG) + "\n"
                //			+ LONGITUDE_TAG + " = " + earthquake.get(LONGITUDE_TAG) + "\n"
                //			+ "Source = " + earthquake.getString("src"));
                //}
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
