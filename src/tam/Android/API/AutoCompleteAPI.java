package tam.Android.API;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tam.Android.Maps.Request;

public class AutoCompleteAPI {
	//https://developers.google.com/maps/documentation/places/autocomplete#place_types
	public final String API_KEY = "AIzaSyC_s7d4lG-ODGXUm42_LI45YjLqRoIoU3o";
	public final String PLACE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input={0}&types=geocode&language=vi&sensor=false&componentRestrictions=country:vi&key="+API_KEY; //nho sua lai
	private String url;
	
	public AutoCompleteAPI(String address)
	{
		url = PLACE_URL.replace("{0}", URLEncoder.encode(address));
	}
	
	public ArrayList<String> getResult()
	{
		Request r = new Request(url);
		String jString = r.sendRequest();
		ArrayList<String> list = new ArrayList<String>();
		try {
			JSONObject jObject = new JSONObject(jString);
			JSONArray predictions = jObject.getJSONArray("predictions");
			for (int i=0;i<predictions.length();i++)
			{
				String description = predictions.getJSONObject(i).getString("description");
				list.add(description);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
