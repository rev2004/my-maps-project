package tam.Android.Maps;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.icons.draw.view.MapLocation;

import tam.Android.Database.MapDatabase;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageButton;

public class GetDirection extends Activity {
	final Context context = this;
	Request request;
	//private String URL_GET_DIRECTION = "http://maps.googleapis.com/maps/api/directions/json?origin={0}&destination={1}&mode={2}&alternatives=true&sensor=false";
	private String mode = "driving";
	private MapDatabase db;
	private ArrayList<MapLocation> list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getdirection);
		
		db = new MapDatabase(getBaseContext());
		list = db.getAllGeoPoints();
		
		final AutoCompleteTextView origin = (AutoCompleteTextView) findViewById(R.id.placeA);
		final AutoCompleteTextView destination = (AutoCompleteTextView) findViewById(R.id.placeB);
		
		ArrayList<String> listdb = new ArrayList<String>();
		for (MapLocation ml:list) listdb.add(ml.getName());
				
		origin.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.mylistview, listdb));
		destination.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.mylistview, listdb));
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
				String orig = origin.getText().toString();
				String dest = destination.getText().toString();
				CheckBox chkOr = (CheckBox) findViewById(R.id.chkOrigin);
				CheckBox chkDs = (CheckBox) findViewById(R.id.chkDestination);
				if (chkOr.isChecked())
				{
					GeoPoint gp = db.getLocation(orig);
					if (gp != null) orig = gp.getLatitudeE6()/1E6 + "," + gp.getLongitudeE6()/1E6;
				}
				if (chkDs.isChecked())
				{
					GeoPoint gp = db.getLocation(dest);
					if (gp != null) dest = gp.getLatitudeE6()/1E6 + "," + gp.getLongitudeE6()/1E6;
				}
				Intent in = new Intent();
				in.putExtra("origin", orig);
				in.putExtra("destination", dest);
				in.putExtra("mode", mode);
				setResult(2, in);
				finish();
			}
		});
	}
}
