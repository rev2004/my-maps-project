package tam.Android.API;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tam.Android.Maps.Request;
import tam.Android.Maps.MapsActivity.LatLonPoint;

import com.google.android.maps.GeoPoint;

public class SearchPlaceAPI {
	private final String URL_SEARCH_PLACE = "http://maps.googleapis.com/maps/api/geocode/json?address={0}&sensor=false";
	private String url;

	public SearchPlaceAPI(String placeToSearch) {
		url = URL_SEARCH_PLACE;
		url = url.replace("{0}", URLEncoder.encode(placeToSearch));
		
	}

	public GeoPoint getResult() {
		Request request = new Request(url);
		String jString = request.sendRequest();
		GeoPoint gp = null;
		// 2 vi tri trung ten !?
		try {
			gp = parse(jString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return gp;
	}

	private GeoPoint parse(String jString) throws JSONException {
		// TODO Auto-generated method stub
		GeoPoint gp = null;
		JSONObject jObject = new JSONObject(jString);
		String result = jObject.getString("status");
		if (result.equals("OK")) {
			JSONArray resultsObject = jObject.getJSONArray("results");
			JSONObject geometry = resultsObject.getJSONObject(0).getJSONObject(
					"geometry");
			JSONObject locationObject = geometry.getJSONObject("location");

			String lat = locationObject.getString("lat");
			String lng = locationObject.getString("lng");

			gp = new LatLonPoint(lat, lng);
		}
		return gp;
	}
}
