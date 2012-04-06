package tam.Android.Maps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class GetDirection extends Activity {
	final Context context = this;
	Request request;
	//private String URL_GET_DIRECTION = "http://maps.googleapis.com/maps/api/directions/json?origin={0}&destination={1}&mode={2}&alternatives=true&sensor=false";
	private String mode = "driving";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getdirection);

		final EditText origin = (EditText) findViewById(R.id.placeA);
		final EditText destination = (EditText) findViewById(R.id.placeB);
		origin.setText("Truong dai hoc Sai Gon");
		destination.setText("Benh vien Cho Ray");

		final ImageButton btnDrivingMode = (ImageButton) findViewById(R.id.btnDrivingMode);
		final ImageButton btnWalkingMode = (ImageButton) findViewById(R.id.btnWalkingMode);
		final ImageButton btnBicyclingMode = (ImageButton) findViewById(R.id.btnBicyclingMode);

		btnDrivingMode.setBackgroundColor(Color.GRAY);
		btnDrivingMode.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mode = "driving";
				btnDrivingMode.setBackgroundColor(Color.GRAY);
				btnWalkingMode.setBackgroundColor(Color.TRANSPARENT);
				btnBicyclingMode.setBackgroundColor(Color.TRANSPARENT);
			}
		});

		btnWalkingMode.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mode = "walking";
				btnWalkingMode.setBackgroundColor(Color.GRAY);
				btnDrivingMode.setBackgroundColor(Color.TRANSPARENT);
				btnBicyclingMode.setBackgroundColor(Color.TRANSPARENT);
			}
		});

		btnBicyclingMode.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mode = "bicycling";
				btnBicyclingMode.setBackgroundColor(Color.GRAY);
				btnDrivingMode.setBackgroundColor(Color.TRANSPARENT);
				btnWalkingMode.setBackgroundColor(Color.TRANSPARENT);
			}
		});

		ImageButton btnGetDiretion = (ImageButton) findViewById(R.id.btnGetDirection);
		btnGetDiretion.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*
				 * URL_GET_DIRECTION = URL_GET_DIRECTION.replace("{0}",
				 * URLEncoder.encode(origin.getText().toString()));
				 * URL_GET_DIRECTION = URL_GET_DIRECTION.replace("{1}",
				 * URLEncoder.encode(destination.getText().toString()));
				 * URL_GET_DIRECTION = URL_GET_DIRECTION.replace("{2}",
				 * URLEncoder.encode(mode)); request = new
				 * Request(URL_GET_DIRECTION); jString = request.sendRequest();
				 * try { parseDirection(); } catch (JSONException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); } final
				 * Dialog dialog = new Dialog(context);
				 * dialog.setContentView(R.layout.showroute);
				 * dialog.setTitle("Suggest Route"); dialog.setCancelable(true);
				 * 
				 * TextView name = (TextView) dialog.findViewById(R.id.name);
				 * name.setText("From " + origin.getText().toString() + " To " +
				 * destination.getText().toString());
				 * 
				 * TextView via = (TextView) dialog.findViewById(R.id.via);
				 * via.setText("Via: " + summary);
				 * 
				 * //set up text TextView ori = (TextView)
				 * dialog.findViewById(R.id.origin); ori.setText("Distance: " +
				 * distance); TextView des = (TextView)
				 * dialog.findViewById(R.id.destination);
				 * des.setText("Duration: " + duration);
				 * 
				 * 
				 * TextView route = (TextView) dialog.findViewById(R.id.route);
				 * route.setText("Details: \n" +
				 * Html.fromHtml(html_instructions).toString());
				 * 
				 * //set up button Button btnCancel = (Button)
				 * dialog.findViewById(R.id.btnCancel);
				 * btnCancel.setOnClickListener(new View.OnClickListener() {
				 * 
				 * public void onClick(View arg0) { // TODO Auto-generated
				 * method stub dialog.dismiss(); } });
				 * 
				 * Button btnShowPath =
				 * (Button)dialog.findViewById(R.id.btnShowPath);
				 * btnShowPath.setOnClickListener(new View.OnClickListener() {
				 * 
				 * public void onClick(View arg0) { // TODO Auto-generated
				 * method stub Intent in = new Intent();
				 * in.putExtra("jDirection", jString); dialog.dismiss();
				 * setResult(2, in); finish(); } }); dialog.show();
				 */
				Intent in = new Intent();
				in.putExtra("origin", origin.getText().toString());
				in.putExtra("destination", destination.getText().toString());
				in.putExtra("mode", mode);
				setResult(2, in);
				finish();
			}
		});
	}
/*
	private void parseDirection() throws JSONException {
		int min = Integer.MAX_VALUE;
		int min_pos = -1;
		// TODO Auto-generated method stub
		JSONObject jObject = new JSONObject(jString);

		JSONArray routes = jObject.getJSONArray("routes");
		for (int i = 0; i < routes.length(); i++) {
			JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");
			JSONObject distance = legs.getJSONObject(0).getJSONObject(
					"distance");

			int value = Integer.parseInt(distance.getString("value"));
			if (value < min) {
				min = value;
				min_pos = i;
			}
		}
		JSONArray legs = routes.getJSONObject(min_pos).getJSONArray("legs");
		JSONObject dis = legs.getJSONObject(0).getJSONObject("distance");
		distance = dis.getString("text");
		JSONObject dur = legs.getJSONObject(0).getJSONObject("duration");
		duration = dur.getString("text");
		summary = routes.getJSONObject(min_pos).getString("summary");
		html_instructions = "";
		JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");

		for (int i = 0; i < steps.length(); i++) {
			html_instructions += (i + 1) + ". "
					+ steps.getJSONObject(i).getString("html_instructions")
					+ "<br/>";
			Log.v("a",
					(i + 1)
							+ ". "
							+ steps.getJSONObject(i).getString(
									"html_instructions") + "\n\n");
		}

	}
	*/
}
