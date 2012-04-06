package tam.Android.API;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tam.Android.Data.MyPath;
import tam.Android.Data.Route;
import tam.Android.Data.Step;
import tam.Android.Maps.Request;
import tam.Android.Maps.MapsActivity.LatLonPoint;

import com.google.android.maps.GeoPoint;

public class GetDirectionAPI {
	private final String URL_GET_DIRECTION = "http://maps.googleapis.com/maps/api/directions/json?origin={0}&destination={1}&mode={2}&alternatives=true&sensor=false&language=vi";
	private String url;
	
	public GetDirectionAPI(String origin, String destination, String mode)
	{
		//luc tim co nen trim() ?
		url = URL_GET_DIRECTION;
		url = url.replace("{0}", URLEncoder.encode(origin));
		url = url.replace("{1}", URLEncoder.encode(destination));
		url = url.replace("{2}", URLEncoder.encode(mode));
	}
	
	public ArrayList<MyPath> getResult()
	{
		Request request = new Request(url);
		String jString = request.sendRequest();
		ArrayList<MyPath> path = new ArrayList<MyPath>();
		try {
			path = parseDirection(jString);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}
	
	private ArrayList<MyPath> parseDirection(String jString)
			throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jObject = new JSONObject(jString);
		if (jObject.getString("status").equals("NOT_FOUND")) {
			return null;
		}

		JSONArray routes = jObject.getJSONArray("routes");

		ArrayList<MyPath> p = new ArrayList<MyPath>();
		for (int i = 0; i < routes.length(); i++) {
			JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");
			JSONObject distance = legs.getJSONObject(0).getJSONObject(
					"distance");
			JSONObject duration = legs.getJSONObject(0).getJSONObject(
					"duration");
			JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");

			MyPath mp = new MyPath();
			String summaries = routes.getJSONObject(i).getString("summary");
			Route route = new Route(summaries, distance.getString("value"),
					duration.getString("value"));
			mp.editRoute(route);

			Double lat, lng;

			for (int j = 0; j < steps.length(); j++) {
				JSONObject start_location = steps.getJSONObject(j)
						.getJSONObject("start_location");
				lat = Double.parseDouble(start_location.getString("lat"));
				lng = Double.parseDouble(start_location.getString("lng"));
				GeoPoint start = new LatLonPoint(lat, lng);

				JSONObject end_location = steps.getJSONObject(j)
						.getJSONObject("end_location");
				lat = Double.parseDouble(end_location.getString("lat"));
				lng = Double.parseDouble(end_location.getString("lng"));
				GeoPoint end = new LatLonPoint(lat, lng);

				distance = steps.getJSONObject(j).getJSONObject("distance");
				duration = steps.getJSONObject(j).getJSONObject("duration");

				JSONObject polyline = steps.getJSONObject(j).getJSONObject(
						"polyline");
				String html_instructions = steps.getJSONObject(j)
						.getString("html_instructions");
				Step step = new Step(distance.getString("value"),
						duration.getString("value"), start, end,
						html_instructions, polyline.getString("points"));
				mp.addStep(step);
			}
			p.add(mp);
		}
		Collections.sort(p, new Comparator<MyPath>() {

			public int compare(MyPath a, MyPath b) {
				// TODO Auto-generated method stub
				int d1 = Integer.parseInt(a.getRoute().distance);
				int d2 = Integer.parseInt(b.getRoute().distance);

				if (d1 > d2)
					return 1;
				if (d1 < d2)
					return -1;
				return 0;
			}

		});
		return p;
	}
}
