package tam.Android.Data;

import java.util.ArrayList;

import tam.Android.Utilities.PolylineDecoder;

import com.google.android.maps.GeoPoint;

public class Step {
	public final String levels = "BBBBBBBBBBBBBBBB";
	public String distance;
	public String duration;
	public GeoPoint startLocation;
	public GeoPoint endLocation;
	public String html_instructions;
	public ArrayList<GeoPoint> pDetails;
	
	public Step(String distance, String duration, GeoPoint start, GeoPoint end, String ins, String points)
	{
		this.distance = distance;
		this.duration = duration;
		this.startLocation = start;
		this.endLocation = end;
		this.html_instructions = ins;
		getPointDetail(points);
	}
	
	private void getPointDetail(String points) {
		// TODO Auto-generated method stub
		int[] decodedZoomLevels = PolylineDecoder.decodeZoomLevels(levels);
		pDetails = PolylineDecoder.decodePoints(points, decodedZoomLevels.length);
	}
}