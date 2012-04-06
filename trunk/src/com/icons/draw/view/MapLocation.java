package com.icons.draw.view;

import com.google.android.maps.GeoPoint;

/** Class to hold our location information */
public class MapLocation {

	private GeoPoint	mPoint;
	private String		mName;

	public MapLocation(double latitude, double longitude,String name) {
		this.mName = name;
		mPoint = new GeoPoint((int)(latitude*1e6),(int)(longitude*1e6));
	}

	public MapLocation(GeoPoint p, String name) {
		this.mPoint = p;
		this.mName = name;
	}
	
	public GeoPoint getPoint() {
		return mPoint;
	}

	public String getName() {
		return mName;
	}
}
