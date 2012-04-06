package tam.Android.Maps;

import java.util.ArrayList;
import java.util.List;

import tam.Android.API.GetDirectionAPI;
import tam.Android.API.SearchPlaceAPI;
import tam.Android.Data.MyPath;
import tam.Android.Data.Route;
import tam.Android.Data.Step;
import tam.Android.Database.MapDatabase;
import tam.Android.Overlays.MyItemizedOverlay;
import tam.Android.Overlays.MyOverlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.icons.draw.view.MapLocation;
import com.icons.draw.view.MapLocationOverlay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MapsActivity extends MapActivity {
	private MapLocationOverlay mapLocationOverlay;
	private MapDatabase db;
	private ProgressDialog progressDialog;
	private MapView mapView;
	private ListView list;
	private int LISTVIEW_HEIGHT;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mapView = (MapView) findViewById(R.id.myMapView);
		list = (ListView) findViewById(R.id.myPath);
		db = new MapDatabase(getBaseContext());
		refreshPlaces();
	}

	public void calc() {
		int t = list.getAdapter().getCount();
		if (t > 3) t = 3;
		int totalHeight = 0;
		for (int i = 0; i < t; i++) {
			View listItem = list.getAdapter().getView(i, null, list);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		LISTVIEW_HEIGHT = totalHeight + (list.getDividerHeight() * (t - 1));
	}

	public void refreshPlaces() {
		List<MapLocation> list = getMapLocations();
		if (list.size() > 0) {
			mapLocationOverlay = new MapLocationOverlay(this);
			mapView.getOverlays().add(mapLocationOverlay);
			mapView.getController().setZoom(15);
			mapView.getController().setCenter(
					list.get(list.size() - 1).getPoint());
			mapView.invalidate();
		}
	}

	public List<MapLocation> getMapLocations() {
		ArrayList<MapLocation> list = db.getAllGeoPoints();
		return list;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		openOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater myMenuInflater = getMenuInflater();
		myMenuInflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		ViewGroup.LayoutParams lp = list.getLayoutParams();
		switch (item.getItemId()) {
		case (R.id.search_place):
			lp.height = 0;
			list.setLayoutParams(lp);
			Intent searchPlace = new Intent(getBaseContext(), SearchPlace.class);
			startActivityForResult(searchPlace, 1);// <========= code = 1
			break;
		case (R.id.get_direction):
			lp.height = 0;
			list.setLayoutParams(lp);
			Intent getDirection = new Intent(getBaseContext(),
					GetDirection.class);
			startActivityForResult(getDirection, 2);
			break;
		case (R.id.add_place):
			lp.height = 0;
			list.setLayoutParams(lp);
			mapView.getOverlays().clear();

			final Dialog dialog = new Dialog(MapsActivity.this);
			dialog.setContentView(R.layout.addplace);
			dialog.setTitle("Add place");
			dialog.setCancelable(true);
			Button btnAddPlace = (Button) dialog.findViewById(R.id.btnAddPlace);
			TextView txtAddress = (TextView) dialog
					.findViewById(R.id.txtAddress);
			txtAddress.setText("273 An Duong Vuong, Quan 5, Tp. HCM");
			btnAddPlace.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String name = ((TextView) dialog.findViewById(R.id.txtName))
							.getText().toString();
					String address = ((TextView) dialog
							.findViewById(R.id.txtAddress)).getText()
							.toString();
					new AddPlaceTask().execute(name, address);
					dialog.dismiss();
				}
			});
			Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
			btnCancel.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});

			dialog.show();

			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) // <============= get du lieu, code = 1
		{
			new SearchPlaceTask().execute(data.getStringExtra("placeToSearch"));
		} else if (resultCode == 2) {
			new GetDirectionTask().execute(data.getStringExtra("origin"),data.getStringExtra("destination"),data.getStringExtra("mode"));
		}
	}
	
	//luc search ra/ko ra, clear overlays, no clear het may cai diem trong database (add place) roi`, lam` sao ve~ lai ?
	public class GetDirectionTask extends
			AsyncTask<String, Void, ArrayList<MyPath>> {

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog = new ProgressDialog(MapsActivity.this);
			progressDialog.setMessage("Searching...");
			progressDialog.show();
		}

		@Override
		protected ArrayList<MyPath> doInBackground(String... urls) {
			return new GetDirectionAPI(urls[0], urls[1], urls[2]).getResult();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(final ArrayList<MyPath> p) {
			// TODO Auto-generated method stub
			if (p == null) {
				Toast.makeText(getBaseContext(),
						"Anh Viet la` trum`, da~ bat' dc loi~ nay`",
						Toast.LENGTH_LONG).show();
				progressDialog.dismiss();
				return;
			}
			drawPath(p.get(0).steps, 0); //<~ focus ra duong` ngan nhat
			
			String[] s = new String[p.size()];
			for (int i = 0; i < p.size(); i++) {
				MyPath mp = p.get(i);
				Route r = mp.getRoute();
				s[i] = (i + 1) + ". " + r.summary + " - " + r.distance + "m - "
						+ r.duration + "s";
			}
			
			list.setAdapter(new ArrayAdapter<String>(MapsActivity.this,
					R.layout.mylistview, s));
			calc();
			ViewGroup.LayoutParams lp = list.getLayoutParams();
			lp.height = LISTVIEW_HEIGHT;
			list.setLayoutParams(lp);
			list.setOnItemLongClickListener(new OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int pos, long id) {
					// TODO Auto-generated method stub

					final Dialog dialog = new Dialog(MapsActivity.this);
					dialog.setContentView(R.layout.showroute);
					dialog.setTitle("Suggest Route");
					dialog.setCancelable(true);

					TextView name = (TextView) dialog.findViewById(R.id.name);
					name.setText("From " + "A" + " To " + "B");

					TextView via = (TextView) dialog.findViewById(R.id.via);
					via.setText("Via: " + p.get(pos).getRoute().summary);

					// set up text
					TextView dis = (TextView) dialog
							.findViewById(R.id.distance);
					dis.setText("Distance: " + p.get(pos).getRoute().distance
							+ "m");
					TextView dur = (TextView) dialog
							.findViewById(R.id.duration);
					dur.setText("Duration: " + p.get(pos).getRoute().duration
							+ "s");

					String ins = "";
					for (int i = 0; i < p.get(pos).steps.size(); i++) {
						ins += (i + 1) + ". "
								+ p.get(pos).steps.get(i).html_instructions;
					}
					TextView route = (TextView) dialog.findViewById(R.id.route);
					route.setText("Details: \n\n"
							+ Html.fromHtml(ins).toString());

					// set up button
					Button btnCancel = (Button) dialog
							.findViewById(R.id.btnCancel);
					btnCancel.setOnClickListener(new View.OnClickListener() {

						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});

					Button btnShowPath = (Button) dialog
							.findViewById(R.id.btnShowPath);
					btnShowPath.setOnClickListener(new View.OnClickListener() {

						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							drawPath(p.get(pos).getStep(), pos);
							dialog.dismiss();
						}
					});
					dialog.show();
					return false;
				}
			});
			list.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View view,
						int pos, long id) {
					// TODO Auto-generated method stub
					drawPath(p.get(pos).getStep(), pos);
				}
			});
			progressDialog.dismiss();
		}

		public void drawPath(ArrayList<Step> steps, int pos) {
			int col = Color.RED;
			if (pos == 0)
				col = Color.BLUE;
			mapView.getOverlays().clear();
			for (int i = 0; i < steps.size(); i++) {
				drawLine(steps.get(i).pDetails, col, mapView);
			}

			mapView.setBuiltInZoomControls(true);
			MapController mc = mapView.getController();
			mc.setZoom(15);
			mc.setCenter(steps.get(0).startLocation);

			Drawable markerOrigin = MapsActivity.this.getResources()
					.getDrawable(R.drawable.pinorigin);
			int markerWidth1 = markerOrigin.getIntrinsicWidth();
			int markerHeight1 = markerOrigin.getIntrinsicHeight();
			markerOrigin.setBounds(0, markerHeight1, markerWidth1, 0);

			Drawable markerDestination = MapsActivity.this.getResources()
					.getDrawable(R.drawable.pindestination);
			int markerWidth2 = markerDestination.getIntrinsicWidth();
			int markerHeight2 = markerDestination.getIntrinsicHeight();
			markerOrigin.setBounds(0, markerHeight2, markerWidth2, 0);

			MyItemizedOverlay myItemizedOverlay = new MyItemizedOverlay(
					markerOrigin);
			mapView.getOverlays().add(myItemizedOverlay);
			myItemizedOverlay.addItem(steps.get(0).startLocation, "Origin",
					"Origin");

			myItemizedOverlay = new MyItemizedOverlay(markerDestination);
			mapView.getOverlays().add(myItemizedOverlay);
			myItemizedOverlay.addItem(steps.get(steps.size() - 1).endLocation,
					"Destination", "Destination");
		}

		private void drawLine(ArrayList<GeoPoint> points, int color,
				MapView mMapView) {
			for (int i = 1; i < points.size(); i++) // the last one would be
													// crash
			{
				mMapView.getOverlays().add(
						new MyOverlay(points.get(i - 1), points.get(i), 2,
								color));
			}
		}
	}

	// http://developer.android.com/reference/android/os/AsyncTask.html
	public class SearchPlaceTask extends AsyncTask<String, Void, GeoPoint> {
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog = new ProgressDialog(MapsActivity.this);
			progressDialog.setMessage("Searching...");
			progressDialog.show();
		}

		@Override
		protected GeoPoint doInBackground(String... urls) {
			// TODO Auto-generated method stub
			GeoPoint gp = db.getLocation(urls[0]);
			if (gp == null) gp = new SearchPlaceAPI(urls[0]).getResult();
			return gp;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(GeoPoint gp) {
			// TODO Auto-generated method stub
			if (gp != null) {
				mapView.getOverlays().clear();
				mapView.setBuiltInZoomControls(true);
				MapController mc = mapView.getController();
				mc.setZoom(15);
				mc.setCenter(gp);

				Drawable marker = MapsActivity.this.getResources().getDrawable(
						R.drawable.pin);
				int markerWidth = marker.getIntrinsicWidth();
				int markerHeight = marker.getIntrinsicHeight();
				marker.setBounds(0, markerHeight, markerWidth, 0);

				MyItemizedOverlay myItemizedOverlay = new MyItemizedOverlay(
						marker);
				mapView.getOverlays().add(myItemizedOverlay);
				myItemizedOverlay.addItem(gp, "myPoint", "myPoint");
			} else {
				Toast.makeText(getBaseContext(), "Not found", Toast.LENGTH_LONG)
						.show();
			}
			progressDialog.dismiss();
		}
	}

	public class AddPlaceTask extends AsyncTask<String, Void, GeoPoint> {
		private String name;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog = new ProgressDialog(MapsActivity.this);
			progressDialog.setMessage("Searching position to add...");
			progressDialog.show();
		}

		@Override
		protected GeoPoint doInBackground(String... urls) {
			// TODO Auto-generated method stub
			name = urls[0];
			return new SearchPlaceAPI(urls[1]).getResult();
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(GeoPoint gp) {
			if (gp != null) {
				Log.v("point", gp.getLatitudeE6() + " " + Double.toString(gp.getLatitudeE6()));
				long id = db.insertPlace(name,
						Double.toString(gp.getLatitudeE6()),
						Double.toString(gp.getLongitudeE6()));
				if (id != -1) {
					Toast.makeText(getBaseContext(), "Added successful",
							Toast.LENGTH_SHORT).show();
					refreshPlaces();
				}
			} else {
				Toast.makeText(getBaseContext(), "Not found", Toast.LENGTH_LONG)
						.show();
			}
			progressDialog.dismiss();
		}

	}

	public static final class LatLonPoint extends GeoPoint {
		public LatLonPoint(double latitude, double longitude) {
			super((int) (latitude * 1E6), (int) (longitude * 1E6));
		}

		public LatLonPoint(String latitude, String longitude) {
			super((int) (Double.parseDouble(latitude) * 1E6), (int) (Double
					.parseDouble(longitude) * 1E6));
		}
	}
}
