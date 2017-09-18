package course.examples.networking.androidhttpclientjson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ListActivity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class NetworkingAndroidHttpClientJSONActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new HttpGetTask().execute();
	}

	public class HttpGetTask extends AsyncTask<Void, Void, List<String>> {

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
			setListAdapter(new ArrayAdapter<String>(
					NetworkingAndroidHttpClientJSONActivity.this,
					R.layout.list_item, result));
            Intent intent = new Intent();

            intent.putExtra("result", result.toArray());


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

                    result.add("Bike Stand Name: " + stationObject.get(BIKE_STAND_NAME) + ", Available Bikes: " + stationObject.get(AVAILABLE_BIKES_TAG) + ", Location lat: " + stationLocation.get("lat") + ", Location long: " + stationLocation.get("lng"));
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